package cn.anlper.wiki.controller;

import cn.anlper.wiki.domain.Test;
import cn.anlper.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {
    @Value("${test.hello:TEST}")
    private String testHello;
    @Resource
    private TestService testService;

//    @RequestMapping(value = "/hello/1", method = RequestMethod.GET)
//    @GetMapping("/hello")
//    @PostMapping("/hello/1")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!" + testHello;
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello World! Post, " + name;
    }

    @GetMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}