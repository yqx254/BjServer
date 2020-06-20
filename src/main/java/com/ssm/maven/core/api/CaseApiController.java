package com.ssm.maven.core.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ssm.maven.core.admin.CaseController;
import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.service.ClientService;
import com.ssm.maven.core.service.ConfigService;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.*;

/**
 * @author fstar
 */
@RestController
@RequestMapping("/api-case")
public class CaseApiController {

    @Resource
    private CaseService caseService;

    @Resource
    private ClientService clientService;

    @Resource
    private ConfigService configService;

    private static final Logger log = Logger.getLogger(CaseApiController.class);

    @RequestMapping(value="list", method = RequestMethod.GET)
    public List<Case> caseList(HttpServletRequest request,
                                                     @RequestParam(value="keyword", required = false)String keyword) throws Exception {
        List<Case> caseList = new ArrayList<>(32);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");

        if(user == null || user.getId() == null){
            return caseList;
        }
        log.info(user.getId());
        Map<String, Object> map = new HashMap<>(32);
        //最多取三十条
        map.put("size", 30);
        if(!"1".equals(user.getRoleId())){
            map.put("createId",user.getId());
        }
        else{
            map.put("createId","0");
        }
        //api只提供关键词查询
        if(keyword != null && !"".equals(keyword)){
            keyword = URLDecoder.decode(keyword, "UTF-8");
            //开头不是BJ表明不是案号
            if(keyword.indexOf("BJ") != 0){
                List<Integer> ids = clientService.findCase(keyword);
                if(ids.size() == 0){
                    return caseList;
                }
                map.put("clientList",ids);
            }
            else{
                map.put("caseCode", keyword);
            }
        }
        return caseService.getCase(map);
    }

    @RequestMapping("/detail")
    public Case caseDetail(HttpServletRequest request,
                                              @RequestParam("id")String id){
        Case myCase = caseService.caseDetail(id);
        List<Client> clients = clientService.clientsByCase(id);
        List<String> nameList = new ArrayList<>();
        List<String> opNameList = new ArrayList<>();
        List<Integer> idtList = new ArrayList<>();
        List<Integer>opIdtList = new ArrayList<>();
        for(Client c : clients){
            if(c.getIdentity() == 0){
                nameList.add(c.getClientName());
                idtList.add(c.getClientType());
            }
            else{
                opNameList.add(c.getClientName());
                opIdtList.add(c.getClientType());
            }
        }
        String [] nameArr = new String [nameList.size()];
        myCase.setClientNameArr(nameList.toArray(nameArr));
        myCase.setClientIdtArr(idtList.stream().mapToInt(x -> x).toArray());
        String [] opNameArr = new String [opNameList.size()];
        myCase.setOpponentNameArr(opNameList.toArray(opNameArr));
        myCase.setOpponentIdtArr(opIdtList.stream().mapToInt(x -> x).toArray());
        return myCase;
    }
    @RequestMapping("/add")
    @Transactional
    public Map<String, String> addCase(HttpServletRequest request,
            @RequestParam("category") String category,
            @RequestParam("dealer") String dealer,
            @RequestParam("remarks") String remarks,
            @RequestParam("accuser") String accuser,
            @RequestParam("accused") String accused
    ){
        Map<String, String > result = new HashMap<>(8);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            result.put("success","0");
            result.put("msg","登录状态失效，请重新登录");
            result.put("code","302");
            return result;
        }
        List<Client> clients = new ArrayList<>(64);
        Case myCase = new Case();
        if(dealer == null || "".equals(dealer)){
            result.put("success","0");
            result.put("msg","承办人不能为空");
        }
        long time = System.currentTimeMillis() / 1000;
        myCase.setCategory(Integer.parseInt(category));
        myCase.setDealer(dealer);
        myCase.setRemarks(remarks);
        myCase.setCreatedAt(time);
        myCase.setUpdatedAt(time);
        myCase.setCreateId(user.getId());
        myCase.setCreateTel(user.getUserName());
        myCase.setCreateName(user.getRealName());
        for(Object o : JSON.parseArray(accuser)){
            Map<String, Object> itemMap = JSONObject.toJavaObject((JSON)o, Map.class);
            String clientName = (String)itemMap.get("accuserName");
            Client check = clientService.conflictCheckClient(clientName);
            if (check != null) {
                String msg = "与案件" + check.getCaseCode() + "存在利冲，承办人"
                        + check.getRealName() + ",请核实";
                result.put("success", "0");
                result.put("msg", msg);
                return result;
            }
            Client client = new Client();
            client.setClientName(clientName);
            client.setIdentity(0);
            client.setCreatedAt(time);
            client.setClientType((Integer) itemMap.get("typeid"));
            clients.add(client);
        }
        Object first = JSON.parseArray(accuser).get(0);
        Map<String, Object> tmp = JSONObject.toJavaObject((JSON)first,Map.class);
        String accuserName = (String)tmp.get("accuserName");
        if(accuserName == null || "".equals(accuserName)){
            result.put("success", "0");
            result.put("msg", "委托人信息不能为空");
        }
        myCase.setClientName((String)tmp.get("accuserName"));
        myCase.setClientCount(JSON.parseArray(accuser).size());
        for(Object o : JSON.parseArray(accused)){
            Map<String, Object> itemMap = JSONObject.toJavaObject((JSON)o, Map.class);
            String clientName = (String)itemMap.get("accusedName");
            Client check = clientService.conflictCheckOpponent(clientName);
            if (check != null) {
                String msg = "与案件" + check.getCaseCode() + "存在利冲，承办人"
                        + check.getRealName() + ",请核实";
                result.put("success", "0");
                result.put("msg", msg);
                return result;
            }
            Client client = new Client();
            client.setClientName(clientName);
            client.setIdentity(1);
            client.setCreatedAt(time);
            client.setClientType((Integer)itemMap.get("typeid"));
            clients.add(client);
        }

        Object firstOp = JSON.parseArray(accused).get(0);
        if(firstOp != null){
            Map<String, Object> tmpOp = JSONObject.toJavaObject((JSON)firstOp,Map.class);
            myCase.setOpponentName((String)tmpOp.get("accusedName"));
            myCase.setOpponentCount(JSON.parseArray(accused).size());
        }
        String caseCode = configService.setThenGetCaseCode(myCase.getCategory());
        myCase.setCaseCode(caseCode);
        for(Client c : clients){
            c.setCaseId(myCase.getId());
            c.setCaseCode(myCase.getCaseCode());
            c.setRealName(myCase.getDealer());
        }
        caseService.addCase(myCase, clients);
        result.put("success","1");
        result.put("msg","保存成功！ 案号为： " + caseCode);
        return result;
    }

    @RequestMapping("/edit")
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, String> editCase(HttpServletRequest request,
                                        @RequestParam("id") String id,
                                        @RequestParam("dealer") String dealer,
                                        @RequestParam("remarks") String remarks,
                                        @RequestParam("accuser") String accuser,
                                        @RequestParam("accused") String accused) throws Exception{
        Map<String, String > result = new HashMap<>(8);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            result.put("success","0");
            result.put("msg","登录状态失效，请重新登录");
            result.put("code","302");
            return result;
        }
        List<Client> clients = new ArrayList<>(64);
        Case myCase = caseService.caseDetail(id);
        long time = System.currentTimeMillis() / 1000;
        for(Object o : JSON.parseArray(accuser)){
            Map<String, Object> itemMap = JSONObject.toJavaObject((JSON)o, Map.class);
            String clientName = (String)itemMap.get("accuserName");
            Client check = clientService.conflictCheckClient(clientName);
            if (check != null) {
                String msg = "与案件" + check.getCaseCode() + "存在利冲，承办人"
                        + check.getRealName() + ",请核实";
                result.put("success", "0");
                result.put("msg", msg);
                return result;
            }
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setCaseCode(myCase.getCaseCode());
            client.setRealName(myCase.getDealer());
            client.setClientName(clientName);
            client.setIdentity(0);
            client.setCreatedAt(time);
            client.setClientType((Integer) itemMap.get("typeid"));
            clients.add(client);
        }
        Object first = JSON.parseArray(accuser).get(0);
        Map<String, Object> tmp = JSONObject.toJavaObject((JSON)first,Map.class);
        String accuserName = (String)tmp.get("accuserName");
        if(accuserName == null || "".equals(accuserName)){
            result.put("success", "0");
            result.put("msg", "委托人信息不能为空");
        }
        if(dealer == null || "".equals(dealer)){
            result.put("success", "0");
            result.put("msg", "承办人信息不能为空");
        }
        myCase.setClientName((String)tmp.get("accuserName"));
        myCase.setClientCount(JSON.parseArray(accuser).size());

        for(Object o : JSON.parseArray(accused)){
            Map<String, Object> itemMap = JSONObject.toJavaObject((JSON)o, Map.class);
            String clientName = (String)itemMap.get("accusedName");
            Client check = clientService.conflictCheckOpponent(clientName);
            if (check != null) {
                String msg = "与案件" + check.getCaseCode() + "存在利冲，承办人"
                        + check.getRealName() + ",请核实";
                result.put("success", "0");
                result.put("msg", msg);
                return result;
            }
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setCaseCode(myCase.getCaseCode());
            client.setRealName(myCase.getDealer());
            client.setClientName(clientName);
            client.setIdentity(1);
            client.setCreatedAt(time);
            client.setClientType((Integer)itemMap.get("typeid"));
            clients.add(client);
        }
        if(JSON.parseArray(accused).size() > 0){
            Object firstOp = JSON.parseArray(accused).get(0);
            if(firstOp != null){
                Map<String, Object> tmpOp = JSONObject.toJavaObject((JSON)firstOp,Map.class);
                myCase.setOpponentName((String)tmpOp.get("accusedName"));
                myCase.setOpponentCount(JSON.parseArray(accused).size());
            }
        }

        myCase.setDealer(dealer);
        myCase.setRemarks(remarks);
        myCase.setUpdatedAt(time);
        //更换client并更新case
        caseService.updateCase(myCase, clients);
        result.put("success", "1");
        return result;
    }
}
