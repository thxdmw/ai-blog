package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                       @RequestParam String password,
                       HttpSession session,
                       Model model) {
        User user = userService.authenticate(username, password);
        if (user != null) {
            // 登录成功，将用户信息存入会话
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            // 登录失败，返回错误信息
            model.addAttribute("error", "用户名或密码错误");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 清除会话
        session.invalidate();
        return "redirect:/login";
    }
}