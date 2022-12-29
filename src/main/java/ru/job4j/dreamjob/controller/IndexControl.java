package ru.job4j.dreamjob.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import ru.job4j.dreamjob.util.HttpSessionUtil;

import javax.servlet.http.HttpSession;

@Controller
public class IndexControl {

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        HttpSessionUtil.passUserAttribute(model, session);
        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }

}
