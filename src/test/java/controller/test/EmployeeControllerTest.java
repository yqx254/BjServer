package controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.maven.core.entity.Employee;
import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath*:/spring-context.xml",
                                             "classpath*:/spring-context-mvc.xml",
                                             "classpath*:/mybatis-config.xml"})
public class EmployeeControllerTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/employee/list")
                        .param("page","1")
                        .param("rows","20")
                        .param("position","领导"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testSearch() throws Exception{
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder =
                MockMvcRequestBuilders.get("/employee/find")
                        .param("id","1");
        MvcResult result =mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();
    }
    @Test
    public void testCount() throws Exception{
         MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                     .get("/employee/total")
                     .param("page","1")
                     .param("size","20")
                     .param("isPartyMember","2"))
                 .andExpect(status().isOk())
                 .andDo(print())
                 .andReturn();
    }

    @Test
    public void testAdd() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                .post("/employee/add")
                .param("name","小中锋")
                .param("mobile","15812071547")
                .param("position","战略忽悠局局长")
                .param("nationality","神族")
                .param("profession","很专业")
                .param("remarks", "哈哈哈")
                .param("isPartyMember","2")
                .param("isMarried","2")
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
//    传json的方法？以后难说有用
//    public void testAdd() throws Exception{
//        Employee employee = new Employee();
//        employee.setName("Guest");
//        employee.setMobile("15812071547");
//        employee.setIsPartyMember(0);
//        employee.setIsMarried(0);
//        employee.setRemarks("hahahaha");
//        mockMvc.perform(MockMvcRequestBuilders
//                    .post("/employee/add")
//                    .param("method","postByJson")
//                    .content(objectMapper.writeValueAsString(employee)).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }


    @Test
    public void testUpdate() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                    .post("/employee/update")
                    .param("id","6")
                    .param("name","Host")
                    .param("mobile","15812071547")
                    .param("is_party_member",String.valueOf(0))
                    .param("is_married", String.valueOf(0))
                    .param("remarks", "Here's my remarks")
                    .param("position", "小领导")
                    .param("profession", "职业玩家")
                    )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
