package project.project.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.project.service.UserService;

@Controller
public class LoginController {
    private final UserService userService;
    private final HttpSession session;

    public LoginController(UserService userService, HttpSession session) {
        this.userService = userService;
        this.session = session;
    }

    //로그인 폼을 보여주는 메소드
    @GetMapping("/login")
    public String showLoginForm() {
        return "user/login.html";
    }

    //로그인 폼 제출을 처리하는 메소드
    @PostMapping("/login")
    public String loginUser(@RequestParam("username") String username,
                            @RequestParam("password") String password) {
        Long userId = userService.login(username, password);
        if (userId != null) {
            session.setAttribute("userId", userId);
            return "redirect:/";
        } else {
            return "redirect:/login";
        }
    }
}
