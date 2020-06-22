package controller.test;

import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.ConfigService;
import org.apache.commons.math3.analysis.function.Add;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import test.AddCase;

import javax.annotation.Resource;

import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:/spring-context.xml", "classpath*:/spring-context-mvc.xml", "classpath*:/mybatis-config.xml"})
@WebAppConfiguration
@Transactional
public class CaseControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private MockHttpSession session;

    @Autowired
    private TaskExecutor taskExecutor;

    @Resource
    private ConfigService configService;

    private static final Logger log = Logger.getLogger(CaseControllerTest.class);

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        session = new MockHttpSession();
        User user = new User();
        user.setId(1);
        user.setUserName("admin");
        user.setRoleId("1");
        session.setAttribute("currentUser",user);
    }

    @Test
    public void testList() throws  Exception{
                ResultActions action = mockMvc.perform(MockMvcRequestBuilders
                                  .get("/case/list")
                                    .param("createKW","15812071547")
                                    .session(session));
                action.andReturn()
                        .getResponse()
                        .setCharacterEncoding("UTF-8");
                action.andDo(MockMvcResultHandlers.print())
                        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    public void testAdd() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/case/add")
                    .param("category","1")
                    .param("clientNameArr","法外狂徒赵某")
                    .param("clientNameArr", "法外狂徒赵某某")
                    .param("opponentNameArr","")
                    .param("opponentIdtArr","0")
                    .param("clientIdtArr","3")
                    .param("clientIdtArr", "3")
                     .param("dealer","吼吼吼")
                    .param("remarks","哦也")
                    .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
    @Test
    public void testDetail() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/case/get-detail")
                    .param("id","77")
                    .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    public void testSolve() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/case/solve")
                .param("id","77")
                .param("clear","0")
                .session(session))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Rollback
    public void testThread() throws InterruptedException {
        int threadCnt = 25;
        for(int i = 0; i < threadCnt; i ++){
            taskExecutor.execute(()
                    -> System.out.println(configService.setThenGetCaseCode(1)));
        }
        Thread.sleep(5000);
    }
}