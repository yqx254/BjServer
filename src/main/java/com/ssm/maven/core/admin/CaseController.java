package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.service.ClientService;
import com.ssm.maven.core.service.ConfigService;
import com.ssm.maven.core.service.impl.ExcelImpl;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fstar
 */
@Controller
@RequestMapping("/case")
public class CaseController {
    @Resource
    private CaseService caseService;

    @Resource
    private ClientService clientService;

    @Resource
    private ConfigService configService;

    @Resource
    private ExcelImpl excelImpl;

    private static final Logger log = Logger.getLogger(CaseController.class);

    @RequestMapping("/list")
    public String getCaseList(
            @RequestParam(value="page", required = false)String page,
            @RequestParam(value="rows", required = false) String rows,
            @RequestParam(value="startDate", required = false) String startDate,
            @RequestParam(value="endDate", required = false) String endDate,
            Case myCase,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>(32);
        log.info("wtf?");
        if(page != null  && rows != null){
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    Integer.parseInt(rows));
            map.put("start",pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        if(startDate != null && !"".equals(startDate)){
            long start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate).getTime() / 1000;
            map.put("startAt",start);
        }
        if(endDate != null && !"".equals(endDate)){
            long end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate).getTime() / 1000 + 86400;
            map.put("endAt",end);
        }
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            return "login";
        }
        if(!"1".equals(user.getRoleId())){
            map.put("createId",user.getId());
        }
        else{
            map.put("createId","0");
        }
        if(myCase.getCaseCode() != null && !"".equals(myCase.getCaseCode())){
            map.put("caseCode",myCase.getCaseCode());
        }
        if(myCase.getClient() != null && !"".equals(myCase.getClient())){
            List<Integer> ids = clientService.findCase(myCase.getClient());
            if(ids.size() == 0){
                JSONObject result = new JSONObject();
                result.put("rows", JSONArray.fromObject(ids));
                result.put("total", 0);
                ResponseUtil.write(response,result);
                return null;
            }
            map.put("clientList",ids);
        }
        if(myCase.getOpponent() != null && !"".equals(myCase.getOpponent())){
            List<Integer> idsOp = clientService.findCaseOp(myCase.getOpponent());
            if(idsOp.size() == 0){
                JSONObject result = new JSONObject();
                result.put("rows", JSONArray.fromObject(idsOp));
                result.put("total", 0);
                ResponseUtil.write(response,result);
                return null;
            }
            map.put("opponentList", idsOp);
        }
        if( (myCase.getCreateKW() != null) && !"".equals(myCase.getCreateKW())){
            map.put("createkw",myCase.getCreateKW());
        }
        List<Case> list = caseService.getCase(map);
        JSONObject result = new JSONObject();
        result.put("rows", JSONArray.fromObject(list));
        result.put("total", caseService.getCaseCount(map));
        ResponseUtil.write(response,result);
        return null;
    }

    @RequestMapping("/add")
    @Transactional
    public String addCase(@RequestParam(value = "id", required = false) String id,
                          @RequestParam(value = "force", required = false) Integer force,
                          Case myCase,
                          HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            return "login";
        }
        long time = System.currentTimeMillis() / 1000;
        force = (force == null )? 0 : force;
        String resMsg = null;
        if(myCase.getClientNameArr()[0] == null || "".equals(myCase.getClientNameArr()[0])){
            result.put("success", false);
            result.put("msg", "委托人信息不能为空");
            ResponseUtil.write(response, result);
            log.error("委托人信息为空");
            return null;
        }
        if(myCase.getDealer() == null || "".equals(myCase.getDealer())){
            result.put("success", false);
            result.put("msg", "承办人信息不能为空");
            ResponseUtil.write(response, result);
            log.error("承办人信息为空");
            return null;
        }
        String [] nameArr = myCase.getClientNameArr();
        int [] idtArr = myCase.getClientIdtArr();
        String [] opNameArr = myCase.getOpponentNameArr();
        int [] opIdtArr = myCase.getOpponentIdtArr();
        //查利冲
        if(force != 1){
            for (String s : nameArr) {
                Client client = clientService.conflictCheckClient(s);
                if (client != null) {
                    String msg = "与案件" + client.getCaseCode() + "存在利冲，承办人"
                            + client.getRealName() + ",请核实";
                    result.put("success", false);
                    result.put("msg", msg);
                    ResponseUtil.write(response, result);
                    return null;
                }
            }
        }
        if(opNameArr.length > 0){
            //查利冲
            if(force != 1){
                for(String s : opNameArr){
                    Client client = clientService.conflictCheckOpponent(s);
                    if (client != null) {
                        String msg = "与案件" + client.getCaseCode() + "存在利冲，承办人"
                                + client.getRealName() + ",请核实";
                        result.put("success", false);
                        result.put("msg", msg);
                        ResponseUtil.write(response, result);
                        return null;
                    }
                }
            }
            myCase.setOpponentName(opNameArr[0]);
            myCase.setOpponentCount(opNameArr.length);
        }
        myCase.setClientName(nameArr[0]);
        myCase.setClientCount(nameArr.length);
        myCase.setCreateId(user.getId());
        myCase.setCreateTel(user.getUserName());
        myCase.setCreateName(user.getRealName());
        //编辑
        if(id != null && myCase.getCaseCode() != null){
            myCase.setUpdatedAt(time);
        }
        else{
            //新增
            String caseCode = configService.setThenGetCaseCode(myCase.getCategory());
            resMsg="保存成功！ 案号为： " + caseCode;
            if(caseCode == null || "".equals(caseCode)){
                result.put("success", false);
                result.put("msg", "案号生成失败，请重试");
                ResponseUtil.write(response, result);
                log.error("案号生成失败");
                return null;
            }
            myCase.setCaseCode(caseCode);
            myCase.setCreatedAt(time);
            myCase.setUpdatedAt(time);
        }
        List<Client> clients = new ArrayList<>(32);
        for(int i = 0; i < nameArr.length; i ++){
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setCaseCode(myCase.getCaseCode());
            client.setClientName(nameArr[i]);
            client.setIdentity(0);
            client.setClientType(idtArr[i]);
            client.setRealName(myCase.getDealer());
            client.setCreatedAt(time);
            clients.add(client);
        }
        for(int j = 0;j < opNameArr.length; j ++){
            if(opNameArr[j] == null || "".equals(opNameArr[j])){
                continue;
            }
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setCaseCode(myCase.getCaseCode());
            client.setClientName(opNameArr[j]);
            client.setIdentity(1);
            client.setClientType(opIdtArr[j]);
            client.setRealName(myCase.getDealer());
            client.setCreatedAt(time);
            clients.add(client);
        }
        if(id != null && myCase.getCaseCode() != null){
            resMsg="修改成功！";
            myCase.setUpdatedAt(time);
            //更新数据
            caseService.updateCase(myCase, clients);
        }
        else{
            caseService.addCase(myCase, clients);
        }
        result.put("success", true);
        result.put("msg",resMsg);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("get-detail")
    public String detail(@RequestParam(value = "id") String id,
                         HttpServletResponse response) throws Exception{
        JSONObject result = new JSONObject();
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
        result.put("caseInfo", myCase);
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }
    @RequestMapping("/delete")
    public String delete(@RequestParam(value = "ids") String ids,
                         HttpServletResponse response) throws Exception {
        JSONObject result = new JSONObject();
        String[] idsStr = ids.split(",");
        for (String s : idsStr) {
            caseService.deleteCase(Integer.parseInt(s));
        }
        result.put("success", true);
        log.info("request: case/delete , ids: " + ids);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/solve")
    public String solve(@RequestParam(value = "id") String id,
                        Integer clear,HttpServletResponse response) throws Exception{
        JSONObject result = new JSONObject();
        int res = caseService.solveCase(Integer.parseInt(id), clear);
        result.put("success", res > 0);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/export")
    public String export(HttpServletResponse response,
                         HttpServletRequest request,
                         @RequestParam(value="startDate", required = false) String startDate,
                         @RequestParam(value="endDate", required = false) String endDate,
                         Case myCase){
        response.setContentType("application/binary;charset=UTF-8");
        try{
            ServletOutputStream out=response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment;fileName=export.xls");
            String [] titles = {"案号","委托人","对方当事人","承办人","备注","录入时间"};
            Map<String, Object> map = new HashMap<>(32);
            HttpSession session = request.getSession();
            User user = (User)session.getAttribute("currentUser");
            if(user == null || user.getId() == null){
                return "login";
            }
            map.put("createId",user.getId());
            if(myCase.getCaseCode() != null && !"".equals(myCase.getCaseCode())){
                map.put("caseCode",myCase.getCaseCode());
            }
            if(myCase.getClient() != null && !"".equals(myCase.getClient())){
                List<Integer> ids = clientService.findCase(myCase.getClient());
                if(ids.size() == 0){
                    JSONObject result = new JSONObject();
                    result.put("rows", JSONArray.fromObject(ids));
                    result.put("total", 0);
                    ResponseUtil.write(response,result);
                    return null;
                }
                map.put("clientList",ids);
            }
            if(myCase.getOpponent() != null && !"".equals(myCase.getOpponent())){
                List<Integer> idsOp = clientService.findCaseOp(myCase.getOpponent());
                if(idsOp.size() == 0){
                    JSONObject result = new JSONObject();
                    result.put("rows", JSONArray.fromObject(idsOp));
                    result.put("total", 0);
                    ResponseUtil.write(response,result);
                    return null;
                }
                map.put("opponentList", idsOp);
            }
            if( (myCase.getCreateKW() != null) && !"".equals(myCase.getCreateKW())){
                map.put("createkw",myCase.getCreateKW());
            }
            if(startDate != null && !"".equals(startDate)){
                long start = new SimpleDateFormat("yyyy-MM-dd").parse(startDate).getTime() / 1000;
                map.put("startAt",start);
            }
            if(endDate != null && !"".equals(endDate)){
                long end = new SimpleDateFormat("yyyy-MM-dd").parse(endDate).getTime() / 1000 + 86400;
                map.put("endAt",end);
            }
            List<Case> list = caseService.getCase(map);
            excelImpl.exportCase(list, titles, out);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
