package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.Client;
import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.service.ClientService;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static final Logger log = Logger.getLogger(CaseController.class);

    @RequestMapping("/list")
    public String getCaseList(
            @RequestParam(value="page", required = false)String page,
            @RequestParam(value="rows", required = false) String rows,
            Case myCase,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>(32);
        if(page != null  && rows != null){
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    20);
            map.put("start",pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
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
            map.put("clientList",clientService.findCase(myCase.getClient()));
        }
        if(myCase.getOpponent() != null && !"".equals(myCase.getOpponent())){
            map.put("opponentList", clientService.findCaseOp(myCase.getOpponent()));
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
    public String addCase(Case myCase,
                          HttpServletResponse response,
                          HttpServletRequest request) throws Exception {
        //TODO: 生成案号
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            return "login";
        }
        long time = System.currentTimeMillis() / 1000;
        myCase.setCreateId(user.getId());
        myCase.setCreateTel(user.getUserName());
        myCase.setCreateName(user.getRealName());
        myCase.setCreatedAt(time);
        myCase.setUpdatedAt(time);
        if(myCase.getClientNameArr()[0] == null || "".equals(myCase.getClientNameArr()[0])){
            result.put("success", false);
            result.put("msg", "委托人信息不能为空");
            ResponseUtil.write(response, result);
            log.error("委托人信息为空");
        }
        String [] nameArr = myCase.getClientNameArr();
        int [] idtArr = myCase.getClientIdtArr();
        String [] opNameArr = myCase.getOpponentNameArr();
        int [] opIdtArr = myCase.getOpponentIdtArr();
        myCase.setClientName(nameArr[0]);
        myCase.setClientCount(nameArr.length);
        //查利冲
        for (String s : nameArr) {
            Client client = clientService.conflictCheckClient(s);
            if (client != null) {
                String msg = "与" + client.getCaseCode() + "存在利冲，承办人"
                        + client.getRealName() + ",请核实";
                result.put("success", false);
                result.put("msg", msg);
                ResponseUtil.write(response, result);
                return null;
            }
        }

        if(opNameArr.length > 0){
            myCase.setOpponentName(opNameArr[0]);
            myCase.setOpponentCount(opNameArr.length);
            //查利冲
            for(String s : opNameArr){
                Client client = clientService.conflictCheckOpponent(s);
                if (client != null) {
                    String msg = "与" + client.getCaseCode() + "存在利冲，承办人"
                            + client.getRealName() + ",请核实";
                    result.put("success", false);
                    result.put("msg", msg);
                    ResponseUtil.write(response, result);
                    return null;
                }
            }
        }
        //TODO: 搞事务？
        int re = caseService.addCase(myCase);
        if(re <= 0){
            result.put("success", false);
            result.put("msg","数据库操作失败，请重试");
            ResponseUtil.write(response, result);
            return null;
        }
        for(int i = 0; i < nameArr.length; i ++){
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setClientName(nameArr[i]);
            client.setIdentity(0);
            client.setClientType(idtArr[i]);
            client.setRealName(myCase.getCreateName());
            client.setCreatedAt(myCase.getCreatedAt());
            clientService.addClient(client);
        }
        for(int j = 0;j < opNameArr.length; j ++){
            if(opNameArr[j] == null || "".equals(opNameArr[j])){
                continue;
            }
            Client client = new Client();
            client.setCaseId(myCase.getId());
            client.setClientName(opNameArr[j]);
            client.setIdentity(1);
            client.setClientType(opIdtArr[j]);
            client.setRealName(myCase.getCreateName());
            client.setCreatedAt(myCase.getCreatedAt());
            clientService.addClient(client);
        }
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
                        HttpServletResponse response) throws Exception{
        JSONObject result = new JSONObject();
        int res = caseService.solveCase(Integer.parseInt(id));
        result.put("success", res > 0);
        ResponseUtil.write(response, result);
        return null;
    }
}
