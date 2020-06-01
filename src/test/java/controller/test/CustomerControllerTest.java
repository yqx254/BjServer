package controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:/spring-context.xml", "classpath*:/spring-context-mvc.xml", "classpath*:/mybatis-config.xml"})

public class CustomerControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void listTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/customer/list")
                 )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addTest() throws  Exception{
        Instant instant = Instant.now();
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/customer/add")
                    .param("name","John Doe")
                    .param("mobile","1444444101")
                    .param("require","Buy a lot of toilet paper")
                    .param("empId","2")
                    .param("date",String.valueOf(instant.getEpochSecond()))
                    .param("created_at",String.valueOf(instant.getEpochSecond()))
                    .param("updated_at",String.valueOf(instant.getEpochSecond()))
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void findTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                    .get("/customer/find")
                    .param("id","1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateTest() throws Exception{
        Instant instant = Instant.now();
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/customer/update")
                    .param("id", "3")
                    .param("name","Johnny English")
                    .param("mobile", "1395959898")
                    .param("require","More toilet paper please!")
                    .param("date",String.valueOf(instant.getEpochSecond()))
                    .param("empId","3")
                    .param("updatedAt", String.valueOf(instant.getEpochSecond()))
                    )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

@Test
public void deleteTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/customer/delete")
                .param("id","3")
        )
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
    }
}
