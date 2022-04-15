package org.hkust.msit6000o.api;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/")
@Controller
public class IndexController {

    @GetMapping
    public String main(Model model) {
        return "index";
    }
}
