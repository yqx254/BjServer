package com.ssm.maven.core.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
import com.ssm.maven.core.admin.CaseController;
import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.service.ClientService;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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

    @RequestMapping("/edit")
    public Map<String, String> editCase(HttpServletRequest request,
                                        @RequestParam("id") String id,
                                        @RequestParam("dealer") String dealer,
                                        @RequestParam("remarks") String remarks,
                                        @RequestParam("accuser") String accuser,
                                        @RequestParam("accused") String accused){
        Map<String, String > result = new HashMap<>(16);
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
            client.setClientType((int)itemMap.get("typeid"));
            clients.add(client);
        }
        for(Object o : JSON.parseArray(accused)){
            Map<String, Object> itemMap = JSONObject.toJavaObject((JSON)o, Map.class);
            String clientName = (String)itemMap.get("accusedName");
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
            client.setIdentity(1);
            client.setCreatedAt(time);
            client.setClientType((int)itemMap.get("typeid"));
            clients.add(client);
        }
        myCase.setDealer(dealer);
        myCase.setRemarks(remarks);
        caseService.updateCase(myCase);
        //删除旧client
        return null;
    }
}
