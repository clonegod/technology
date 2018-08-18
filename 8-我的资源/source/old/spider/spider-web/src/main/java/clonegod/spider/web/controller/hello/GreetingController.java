package clonegod.spider.web.controller.hello;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 返回常规HTML视图
 * 
 * @author clonegod@163.com
 *
 */
@Controller
public class GreetingController {

    @RequestMapping("/greeting")
    public ModelAndView greeting(@RequestParam(value="name", required=false, defaultValue="World") String name) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("greeting");
        mav.addObject("YourName", name);
        return mav;
    }

}