package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.entity.Role;
import com.ssm.maven.core.service.RoleService;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    private static Logger log = Logger.getLogger(RoleController.class);

    @RequestMapping("/list")
    public String list(@RequestParam(value="page", required = false)String page,
                       @RequestParam(value="rows", required = false) String rows,
                       Role role,
                       HttpServletResponse response) throws  Exception{
        Map<String, Object> query = new HashMap<>(32);
        if(page != null  && rows != null){
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    Integer.parseInt(rows));
            query.put("start",pageBean.getStart());
            query.put("size", pageBean.getPageSize());
        }
        List<Role> roleList = roleService.roleList(query);
        JSONObject result = new JSONObject();
        result.put("rows", JSONArray.fromObject(roleList));
        result.put("total",roleService.roleTotal(query));
        ResponseUtil.write(response,result);
        return null;
    }

    @RequestMapping("/add")
    public String add(Role role, HttpServletResponse response) throws Exception{
        long now = Instant.now().getEpochSecond();
        role.setCreatedAt(now);
        role.setUpdatedAt(now);
        roleService.addRole(role);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/delete")
    public String delete(String id, HttpServletResponse response) throws Exception{
        roleService.deleteRole(id);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/update")
    public String update(Role role, HttpServletResponse response) throws Exception{
        long now = System.currentTimeMillis() / 1000;
        role.setUpdatedAt(now);
        roleService.updateRole(role);
        JSONObject result = new JSONObject();
        result.put("success", true);
        ResponseUtil.write(response, result);
        return null;
    }

    @RequestMapping("/findRole")
    public String findRole(String id, HttpServletResponse response) throws  Exception{
        Role role = roleService.findRoleById(id);
        ResponseUtil.write(response,JSONObject.fromObject(role));
        return null;
    }

    @RequestMapping("/roleConfig")
    public String getRoleConf(HttpServletResponse response) throws Exception{
        ResponseUtil.write(response,JSONArray.fromObject(roleService.roleList(new HashMap<>(16))));
        return null;
    }
}
