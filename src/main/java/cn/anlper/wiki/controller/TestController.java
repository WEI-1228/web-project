package cn.anlper.wiki.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class TestController {

//    @RequestMapping(value = "/hello/1", method = RequestMethod.GET)
//    @GetMapping("/hello")
//    @PostMapping("/hello/1")
    @RequestMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
