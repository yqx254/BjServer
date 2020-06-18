package com.ssm.maven.core.pojo;

import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
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
public class UserAudience {
    private final  static Logger log = Logger.getLogger(UserAudience.class);

    @Pointcut("execution(* com.ssm.maven.core.admin.UserController.modifyPassword(..))")
    private void myMethod(){}

    @Before("myMethod()")
    public void before() throws Exception {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest  request= sra.getRequest();
        HttpServletResponse response = sra.getResponse();
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute("currentUser");
        if(user == null || user.getId() == null){
            JSONObject result = new JSONObject();
            result.put("success", false);
            result.put("msg","登录状态失效，请重新登录");
            ResponseUtil.write(response, result);
        }
    }
}
