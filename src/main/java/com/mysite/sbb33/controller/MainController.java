package com.mysite.sbb33.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @RequestMapping("/test")
    public String showMain(){
        return "user/file.html";
    }

    @RequestMapping("/")
    public String rootMain(){
        System.out.println(System.getProperty("user.dir"));
        return "redirect:article/list";
    }

}
