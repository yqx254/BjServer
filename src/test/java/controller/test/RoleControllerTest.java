package controller.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:/spring-context.xml", "classpath*:/spring-context-mvc.xml", "classpath*:/mybatis-config.xml"})

public class RoleControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Before
    public void init(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void listTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .get("/role/list")
                ).andExpect(MockMvcResultMatchers.status().isOk())
                 .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void findTest() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders
            .get("/role/findRole")
                .param("id","5")
            ).andExpect(MockMvcResultMatchers.status().isOk())
             .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/role/update")
                .param("roleId","2")
                .param("roleName","录文章")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/role/add")
                .param("roleName","小角色")
        ).andExpect(MockMvcResultMatchers.status().isOk())
         .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/role/delete")
                .param("id","4")
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void roleConfig() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
        .get("/role/roleConfig"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
}
