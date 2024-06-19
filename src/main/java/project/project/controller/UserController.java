package project.project.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import project.project.dto.UserDto;
import project.project.service.UserService;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //회원가입 폼을 보여주는 메소드
    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "user/signup.html";
    }

    //회원가입 폼 제출을 처리하는 메소드
    @PostMapping("/signup")
    public String registerUser(@Valid UserDto userDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "user/signup.html";
        }
        userService.saveUser(userDto);
        return "redirect:/";
    }
}
