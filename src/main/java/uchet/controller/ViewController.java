package uchet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller

public class ViewController {

    @RequestMapping("/")
    public String show() {
        return "main_page";
    }

    @RequestMapping("/users")
    public String showUsers() {
        return "users";
    }

    @RequestMapping("/buh")
    public String showBuh() {
        return "buh";
    }

    @RequestMapping("/zamdir")
    public String showZamDir() {
        return "zamdir";
    }

    @RequestMapping("/dir")
    public String showDir() {
        return "dir";
    }

}
