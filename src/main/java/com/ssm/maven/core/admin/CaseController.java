package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
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
                    Integer.parseInt(rows));
            map.put("start",pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user.getId() == null){
            return "login";
        }
        map.put("createId",user.getId());
        if(myCase.getCaseCode() != null && !"".equals(myCase.getCaseCode())){
            map.put("caseCode",myCase.getCaseCode());
        }
        if(myCase.getCategory() != 0){
            //后台编号从0开始，避免出现问题
            map.put("category",myCase.getCategory() + 1);
        }
        if(myCase.getClientName() != null && !"".equals(myCase.getClientName())){
            //查询后组装ID，后面补
        }
        if(myCase.getOpponentName() != null && !"".equals(myCase.getOpponentName())){
            //查询后组装ID，后面补
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
        //TODO: 查重
        //TODO: 录入客户信息
        //TODO: 生成案号
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user.getId() == null){
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
            ResponseUtil.write(response, result);
            log.error("委托人信息为空");
        }
        myCase.setClientName(myCase.getClientNameArr()[0]);
        myCase.setClientCount(myCase.getClientNameArr().length);
        if(myCase.getOpponentNameArr().length > 0){
            myCase.setOpponentName(myCase.getOpponentNameArr()[0]);
        }
        myCase.setOpponentCount(myCase.getOpponentNameArr().length);
        int re = caseService.addCase(myCase);
        if(re > 0){
            result.put("success", true);
        }
        else{
            result.put("success", false);
        }
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
