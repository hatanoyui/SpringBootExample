package com.slankka.io.springwebsocket.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/websocket")
    public String websocket() {
        return "websocket";
    }
}
