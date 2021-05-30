package JavaExam.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // фильтр скрытых полей, позволяет использовать методы PUT, PATCH, DELETE и прочие
    @Bean
    public FilterRegistrationBean hiddenHttpMethodFilter() {
        FilterRegistrationBean filterRegBean = new FilterRegistrationBean(new HiddenHttpMethodFilter());
        filterRegBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegBean;
    }

    // настройка местонахождения ресурсов (картинок, стилей и прочего)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("**.css").addResourceLocations("classpath:/webapp/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/webapp/static/images/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/webapp/static/fonts/");
    }

    // регистрация простых контроллеров без объявления класса (в частности настройка перенаправлений)
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("", "/hello");      // автоматическое перенаправление
    }

}
