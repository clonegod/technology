package clonegod.spider.web;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * used @SpringBootTest we are asking for the whole application context to be created.
 * the use of the @AutoConfigureMockMvc together with @SpringBootTest to inject a MockMvc instance.
 * 
 * The MockMvc via a set of convenient builder classes, to send HTTP requests into the DispatcherServlet
 * MockMvc使用Builder提供的便利方法将HTTP请求交给DispatchServlet
 * 
 * @author clonegod@163.com
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {
	@Autowired
    private MockMvc mvc;

    @Test
    public void getHello() throws Exception {
    	// URL与context-path的配置无关，直接从应用的根路径开始
        mvc.perform(MockMvcRequestBuilders.get("/rest/hello").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("Greetings from Spring Boot!")));
    }
}