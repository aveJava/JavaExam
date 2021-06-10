package JavaExam.controllers.pages;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testing")
public class TestingController {

    @GetMapping
    public String displayPage(Model model) {
        return "testing/testing-page";
    }

}