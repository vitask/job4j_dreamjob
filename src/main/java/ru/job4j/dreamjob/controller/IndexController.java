package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.concurrent.ThreadSafe;

@Controller
@SuppressWarnings("unused")
@ThreadSafe
public class IndexController {
    @GetMapping("/index")
    public String getIndex() {
        return "index";
    }
}
