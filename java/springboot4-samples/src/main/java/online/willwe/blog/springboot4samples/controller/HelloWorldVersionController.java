package online.willwe.blog.springboot4samples.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldVersionController {

    @RequestMapping(path = "hello", version = "v1")
    public String hello_v1() {
        return "hello world:v1";
    }

    @RequestMapping(path = "hello",version="v2")
    public String hello_v2() {
        return "hello world:v2";
    }
}
