package clonegod.spider.web.controller.hello;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * REST API
 * 
 * @author clonegod@163.com
 *
 */
@RestController
public class HelloController {

    @RequestMapping("/rest/hello")
    public String index() {
        return "Greetings from Spring Boot!";
    }

}