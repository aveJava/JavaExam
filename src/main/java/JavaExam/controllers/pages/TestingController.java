package JavaExam.controllers.pages;

import JavaExam.enums.Tests;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testing")
public class TestingController {
    Tests test;     // выбранный тест

    @GetMapping
    public String displayPage(Model model) {
        if (test == null) return "redirect:/hello";
        model.addAttribute("test", test);

        return "testing/testing-page";
    }

    @GetMapping("/selection")
    public String chooseTest(@RequestParam(value = "test", required = false) String test) {
        if (test != null) this.test = Tests.valueOf(test);

        return "redirect:/testing";
    }
}