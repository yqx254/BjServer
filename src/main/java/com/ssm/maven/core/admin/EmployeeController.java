package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Employee;
import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.service.EmployeeService;
import com.ssm.maven.core.util.ResponseUtil;
import com.ssm.maven.core.util.StringUtil;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Administrator
 * @date 20200418
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {
        private static final Logger log = Logger.getLogger(EmployeeController.class);
        @Resource
        private EmployeeService employeeService;
        @RequestMapping(value="/list", method = RequestMethod.POST)
        public String getList(
                @RequestParam(value = "page", required = false)String page,
                @RequestParam(value ="rows", required = false)String rows,
                Employee employee,
                HttpServletResponse response)throws  Exception{
                Map<String, Object> queryMap = new HashMap<>(64);
                if(page != null && rows != null){
                        PageBean pageBean = new PageBean(Integer.parseInt(page),
                                Integer.parseInt(rows));
                        queryMap.put("start", pageBean.getStart());
                        queryMap.put("size", pageBean.getPageSize());
                }
                if(employee.getKeyword() != null &&
                        !"".equals(employee.getKeyword())){
                        queryMap.put("keyword",employee.getKeyword());
                        System.out.println(queryMap.toString());
                }
                if(employee.getIsPartyMember() != -1){
                        queryMap.put("isPartyMember",
                                employee.getIsPartyMember());
                }
                List<Employee> employeeList = employeeService.findEmployee(queryMap);
                for(Employee emp : employeeList){
                    if(emp.getIsPartyMember() == 1){
                        emp.setIsPartyMemberStr("是");
                    }
                    else{
                        emp.setIsPartyMemberStr("不是");
                    }
                    if(emp.getIsMarried() == 1){
                        emp.setIsMarriedStr("已婚");
                    }
                    else{
                        emp.setIsMarriedStr("未婚");
                    }
                }
                Long total = employeeService.getTotalEmployee(queryMap);
                JSONObject result = new JSONObject();
                JSONArray jsonArray = JSONArray.fromObject(employeeList);
                result.put("rows", jsonArray);
                result.put("total", total);
                ResponseUtil.write(response,result);
                log.info("employee/list query: " + queryMap.toString());
                return null;
        }
        @RequestMapping("/total")
        public String getTotal(
                @RequestParam(value="page", required = false)String page,
                @RequestParam(value="size", required =  false)String size,
                Employee employee,
                HttpServletResponse response
        )throws  Exception{
                Map <String, Object>query = new HashMap<>(64);
                if(page != null && size != null){
                        PageBean pageBean = new PageBean(Integer.parseInt(page),
                                Integer.parseInt(size));
                        query.put("start",pageBean.getStart());
                        query.put("size",pageBean.getPageSize());
                }
               if(employee.getName() != null &&
                       !"".equals(employee.getName())){
                        query.put("name", StringUtil.formatLike(employee.getName()));
               }
               if(employee.getIsPartyMember() != -1){
                       query.put("isPartyMember",employee.getIsPartyMember());
               }
               if(employee.getProfession() != null &&
                 !"".equals(employee.getProfession())){
                       query.put("profession",StringUtil.formatLike(employee.getProfession()));
               }
               if(employee.getPosition() != null &&
                        !"".equals(employee.getPosition())){
                       query.put("position", StringUtil.formatLike(employee.getPosition()));
               }
               Long total = employeeService.getTotalEmployee(query);
               JSONObject result = new JSONObject();
              result.put("total",total);
              ResponseUtil.write(response,result);
              return  null;
        }

        @RequestMapping("/find")
        public String search(
                @RequestParam(value="id")int id,
                HttpServletResponse response
        )throws  Exception{
                Employee employee = employeeService.findEmployeeById(String.valueOf(id));
                ResponseUtil.write(response, JSONObject.fromObject(employee));
                return null;
        }

        @RequestMapping(value="/add", method=RequestMethod.POST)
    public String add(Employee employee,
                      HttpServletResponse response)
                throws Exception{
            //填入创建时间字段
            Instant instant = Instant.now();
            employee.setCreatedAt(instant.getEpochSecond());
            employee.setUpdatedAt(instant.getEpochSecond());

            int resultTotal = 0;
            resultTotal = employeeService.addEmployee(employee);
            JSONObject result = new JSONObject();
            if(resultTotal > 0){
                result.put("res",true);
            }
            else{
                result.put("res",false);
            }
            ResponseUtil.write(response,result);
            return null;
        }

        @RequestMapping("/update")
        public String update(Employee employee, HttpServletResponse response) throws  Exception{
            Instant instant = Instant.now();
            employee.setUpdatedAt(instant.getEpochSecond());
            int resultTotal = 0;
            resultTotal = employeeService.updateEmployee(employee);
            JSONObject result = new JSONObject();
            if(resultTotal > 0){
                result.put("res",true);
            }
            else{
                result.put("res",false);
            }
            ResponseUtil.write(response,result);
            return null;
        }

        @RequestMapping("/delete")
        public String delete(@RequestParam(value="ids") String ids,
                             HttpServletResponse response)
                                throws  Exception{
            String [] idStr = ids.split(",");
            for(String s : idStr){
                employeeService.deleteEmployee(String.valueOf(s));
            }
            JSONObject result = new JSONObject();
            result.put("success",true);
            ResponseUtil.write(response, result);
            return null;
        }
}
