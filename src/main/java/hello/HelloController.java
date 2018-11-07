package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//if @Controller is used instead of @RestController, to avoid thymeleaf mappting add
//@ResponseBody after @RequestMapping

@Controller
public class HelloController {

    @Autowired
    BlockingFlowRepository blockingFlowRepository;
    @Autowired
    BlockingFLowDBService service;

    @RequestMapping(value = "/")
    public String index() {
      //  return "EAF Won the game!";
    return "index";
    }

    @RequestMapping(value = "/listall",method = RequestMethod.GET)
    @ResponseBody
    public Iterable<BlockingFlow> List2(){
        //  return "EAF Won the game!!!!";
        return service.findAll2();
        //blockingFlowRepository.List();
    }

    @RequestMapping(value = "/listall2",method = RequestMethod.GET)
    @ResponseBody
    public List<BlockingFlow> List(){
        //  return "EAF Won the game!!!!";
        return service.findall3();
                //blockingFlowRepository.List();
    }

    @RequestMapping(value = "message", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView messages() {
        ModelAndView mav = new ModelAndView("message/list");
        mav.addObject("messages", blockingFlowRepository.findAll());
        return mav;
        //blockingFlowRepository.List();
    }





}
