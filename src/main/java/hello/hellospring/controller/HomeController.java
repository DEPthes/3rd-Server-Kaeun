package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/") //처음 도메인 경로(localhost:8080/)
    public String home() {
        return "home"; //templates 내의 home.html 찾아 반환
    }
}
