package hello.hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller //컨트롤러임을 알려주는 어노테이션
public class HelloController {
    @GetMapping("hello") //웹 애플리케이션에서 /hello가 들어오면 hello 메소드를 호출해줌
    public String hello(Model model) {
        model.addAttribute("data", "hello!!");
        return "hello"; //문자열 값에 맞는 html을 찾아 화면에 보여줌
    }

    @GetMapping("hello-mvc") //웹 url을 통해 파라미터를 전달(ex. ?name=spring!)
    public String helloMvc(@RequestParam("name") String name, Model model) {
        model.addAttribute("name", name);
        return "hello-template";
    }

    @GetMapping("hello-string")
    @ResponseBody //HTTP의 Body에 문자 내용을 직접 반환
    public String helloString(@RequestParam("name") String name) {
        return "hello " + name; //만약 name이 spring이라면 hello spring, view 없이 문자가 바로 전달
    }

    @GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name) {
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
