package JavaExam.config;

import JavaExam.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;    // предоставляет данные о пользователе по его имени


    // настройка аутентификации (выбор менеджера и провайдеров аутентификации)
    @Autowired
    public void registerGlobalAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(getAuthProvider());
    }

    // настройка авторизации (определение прав (ролей), необходимых для доступа к тем или иным url)
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // настройка защиты csrf (в данном случае - отключение)
                .csrf()
                    .disable()

                // настройка прав доступа к ресурсам (такие-то url будут доступны для таких-то ролей)
                .authorizeRequests()            // возвращает конфигуратор ограничений доступа
                    // адреса, доступные только админам и суперадминам
                    .antMatchers("/editing_tests/**", "/registration/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERADMIN")
                    // доступ к тестированию разрешен только аутентифицированным пользователям
                    .antMatchers("/testing**").authenticated()
                    // ко всем остальным страницам разрешить доступ всем
                    .anyRequest().permitAll()
                    .and()

                // обработка ошибок доступа
                .exceptionHandling()
                    .accessDeniedPage("/login")         // куда перенаправлять в случае отказа в доступе
                    .and()

                // настройка формы аутентификации
                .formLogin()                // выбрана аутентификация через заполнение html-формы
                    .loginPage("/login")                // адрес страницы входа в систему
                    .usernameParameter("username")      // имя поля для ввода имени пользователя
                    .passwordParameter("password")      // имя поля для ввода пароля
                    .defaultSuccessUrl("/hello")        // куда перенаправить в случае успешного входа (если пользователь никуда не пытался попасть)
                    .failureUrl("/login?error=true")    // куда перенаправить в случае неудачи
                    .permitAll()                        // разрешить всем доступ к formLogin
                    .and()

                // настройка выхода пользователя из системы
                .logout()
                    .logoutUrl("/logout")           // url, запускающий выход из системы
                    .logoutSuccessUrl("/hello")     // страница, на которую будет перенаправлен пользоваетль после выхода
                    .deleteCookies("JSESSIONID", "SPRING_SECURITY_REMEMBER_MY_COOKIE")  // удалить cookie после выхода
                    .invalidateHttpSession(true)    // удалить сессию после выхода
                    .permitAll();                   // разрешить всем доступ к странице logout
    }

    // позволяет сконфигурировать глобальные параметры безопасности
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()                 // позволяет отпределить адреса и ресурсы, которые будут выведены из под защиты Spring Security
                .antMatchers("/resources/webapp/**");    // все содержимое папки webapp (html-страцицы, css-стили, картинки и пр.)
    }

    // настройка своего провайдера аутентификации (хэширует пароль, предоставленный пользователем и сравнивает с хэшем, полученным из UserService)
    @Bean
    protected DaoAuthenticationProvider getAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());      // чем хэшировать аутентифицируемый пароль
        provider.setUserDetailsService(userService);            // как получить хэш пароля из хранилища пользователей
        return provider;
    }

    // Энкодер, используемый для шифрования (хэширования) паролей
    @Bean
    protected BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}






