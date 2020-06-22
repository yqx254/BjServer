package com.ssm.maven.core.pojo.exception;

import java.text.MessageFormat;

/**
 * @author fstar
 */
public class LoginException extends RuntimeException{
    private String retCd;
    private String msgDesc;
    public LoginException(){
        super();
    }
    public LoginException(String msg){
        super(msg);
        this.msgDesc = msg;
    }

    @Override
    public Throwable fillInStackTrace(){
        return this;
    }
    @Override
    public String toString(){
        return MessageFormat.format("{0}[{1}]",this.retCd,this.msgDesc);
    }
}
