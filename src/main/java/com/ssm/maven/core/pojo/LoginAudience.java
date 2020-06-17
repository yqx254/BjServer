package com.ssm.maven.core.pojo;

import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * @author fstar
 */
@Component
@Aspect
public class LoginAudience {
    private static final  Logger log = Logger.getLogger(LoginAudience.class);

    @Pointcut("execution( * com.ssm.maven.core.admin.CaseController.*(..))")
    private void myMethod(){}

    @Before("myMethod()")
    public void before() throws Exception {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)attributes;
        HttpServletRequest request = sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            JSONObject result = new JSONObject();
            result.put("msg","登录状态失效，请重新登录！");
            result.put("success", false);
            ResponseUtil.write(response,result);
        }
    }
}
