package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Case;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.CaseService;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@RestController
@RequestMapping("/ffff")
public class CaseApiController {


    @Resource
    private CaseService caseService;

    @RequestMapping("/list")
    public List<Case> caseList(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @RequestParam(value="keyword", required = false)String keyword) throws Exception {
        List<Case> caseList = new ArrayList<>(32);
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");

        if(user == null || user.getId() == null){
            return caseList;
        }
        Map<String, Object> map = new HashMap<>(32);
        return caseList;
    }
}
