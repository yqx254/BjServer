package com.ssm.maven.core.admin;

import com.ssm.maven.core.entity.Customer;
import com.ssm.maven.core.entity.Employee;
import com.ssm.maven.core.entity.PageBean;
import com.ssm.maven.core.service.CustomerService;
import com.ssm.maven.core.service.EmployeeService;
import com.ssm.maven.core.util.DateUtil;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @Resource
    private EmployeeService employeeService;
    private static  final Logger log = Logger.getLogger(CustomerController.class);

    @RequestMapping("/list")
    public String customerList(
            @RequestParam(value = "page", required = false)String page,
            @RequestParam(value = "rows", required = false)String rows,
            Customer customer, HttpServletResponse response) throws Exception {
        Map<String,Object> queryMap = new HashMap<>(32);
        if(page != null && rows != null){
            PageBean pageBean = new PageBean(Integer.parseInt(page),
                    Integer.parseInt(rows));
            queryMap.put("start",pageBean.getStart());
            queryMap.put("size", pageBean.getPageSize());
        }
        if(customer.getKeyword() != null && !"".equals(customer.getKeyword())){
            queryMap.put("keyword",customer.getKeyword());
        }
        List<Customer> customerList = customerService.customerList(queryMap);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for(Customer cstm : customerList){
            cstm.setEmpName(employeeService.findEmpName(String.valueOf(cstm.getEmpId())));
            cstm.setDateStr(formatter.format(
                    ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(
                                    cstm.getDate()),
                            ZoneId.systemDefault())));
        }
        Long total = customerService.customerTotal(queryMap);
        JSONObject result = new JSONObject();
        JSONArray array = JSONArray.fromObject(customerList);
        result.put("rows",array);
        result.put("total",total);
        ResponseUtil.write(response,result);
        return null;
    }
    @RequestMapping("/find")
    public String findCustomer(@RequestParam(value="id") String id,
                               HttpServletResponse response) throws Exception{
        Customer  customer = customerService.findCustomer(id);
        ResponseUtil.write(response, JSONObject.fromObject(customer));
        return null;
    }

    @RequestMapping("/add")
    public String addCustomer(@NotNull Customer customer,
                              HttpServletResponse response)throws Exception{
        Instant instant = Instant.now();
        customer.setCreatedAt(instant.getEpochSecond());
        customer.setUpdatedAt(instant.getEpochSecond());
        customer.setDate(DateUtil.stringToStamp(customer.getDateStr()));
        customerService.addCustomer(customer);
        JSONObject result = new JSONObject();
        result.put("res",true);
        ResponseUtil.write(response,result);
        return null;
    }

    @RequestMapping("/update")
    public String updateCustomer(@NotNull Customer customer,
                                 HttpServletResponse response) throws Exception{
        Instant instant = Instant.now();
        customer.setUpdatedAt(instant.getEpochSecond());
        customer.setDate(DateUtil.stringToStamp(customer.getDateStr()));
        log.warn("fook u! " + customer.getDate());
        customerService.updateCustomer(customer);
        JSONObject result = new JSONObject();
        result.put("res",true);
        ResponseUtil.write(response,result);
        return null;
    }

    @RequestMapping("/delete")
    public String deleteCustomer(String id,
                                 HttpServletResponse response) throws Exception{
        customerService.deleteCustomer(id);
        JSONObject result = new JSONObject();
        result.put("res",true);
        ResponseUtil.write(response,result);
        return null;
    }
}
