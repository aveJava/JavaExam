package JavaExam.controllers.security;

import JavaExam.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {
    UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // вход для пользователей (экзаменуемых)
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) boolean error, Model model) {
        if (error) {
            model.addAttribute("errorMsg", "Bad credentials!");
        }
        return "login/login";
    }

    // выход из системы
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    // информация о том, как получить учетку
    @GetMapping("/info")
    public String info() {
        return "login/login-info";
    }

    // предоставляет форму графического ключа
    @GetMapping("/admin-login")
    public String adminLogin() {
        return "login/login-admin_key";
    }

    // вход под админом через графический ключ
    @PostMapping("/admin-login")
    public String adminAuth(@RequestParam(value = "checkbox1", defaultValue = "false") boolean key1,
                            @RequestParam(value = "checkbox2", defaultValue = "false") boolean key2,
                            @RequestParam(value = "checkbox3", defaultValue = "false") boolean key3,
                            @RequestParam(value = "checkbox4", defaultValue = "false") boolean key4,
                            @RequestParam(value = "checkbox5", defaultValue = "false") boolean key5,
                            @RequestParam(value = "checkbox6", defaultValue = "false") boolean key6,
                            @RequestParam(value = "checkbox7", defaultValue = "false") boolean key7,
                            @RequestParam(value = "checkbox8", defaultValue = "false") boolean key8,
                            @RequestParam(value = "checkbox9", defaultValue = "false") boolean key9) {

        if (key1 && !key2 && !key3 && !key4 && !key5 && key6 && !key7 && key8 && !key9) {
            // аутентификация под админом
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            Authentication authentication =  new UsernamePasswordAuthenticationToken("default_admin", null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return "redirect:/admin";
        }

        return "login/login-admin_key";
    }
}
