package test;

import com.ssm.maven.core.service.ConfigService;
import org.apache.log4j.Logger;

import javax.annotation.Resource;


public class AddCase extends  Thread{

    private final static Logger log = Logger.getLogger(AddCase.class);

    ConfigService configService;

    public AddCase(ConfigService configService){
        this.configService = configService;
    }

    @Override
    public void run() {
        log.info("start thread");
        try {
            System.out.println(configService.setThenGetCaseCode(1));
        }
        catch (Exception e){
            e.printStackTrace();
            log.info("thread exception");
        }
        log.info("thread finished");
    }
}
