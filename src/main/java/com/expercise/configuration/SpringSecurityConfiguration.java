package com.expercise.configuration;

import com.expercise.service.user.UserRememberMeTokenRepository;
import com.expercise.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.security.SpringSocialConfigurer;

@Configuration
@EnableWebMvcSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final int PASSWORD_ENCODING_STRENGTH = 256;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(
                        "/",
                        "/403",
                        "/404",
                        "/500",
                        "/generatedResources/**",
                        "/resources/**",
                        "/signup/**",
                        "/signin/**",
                        "/forgotMyPassword/**",
                        "/themes/**"
                )
                .permitAll();

        http.authorizeRequests()
                .regexMatchers("/user/.*/.*")
                .permitAll();

        http.authorizeRequests()
                .antMatchers(
                        "/challenges/**",
                        "/user"
                )
                .hasAnyRole("USER")
                .antMatchers("/manage/**")
                .hasRole("ADMIN")
                .anyRequest()
                .authenticated();

        http.formLogin()
                .loginPage("/signin")
                .failureUrl("/signin?error")
                .permitAll();

        http.logout()
                .logoutUrl("/signout")
                .logoutSuccessUrl("/signin?signout")
                .deleteCookies("JSESSIONID")
                .permitAll();
        http.rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(DateUtils.ONE_WEEK);

        http.apply(new SpringSocialConfigurer().signupUrl("/signup/social"));

        http.csrf().disable();
    }

    @Bean
    public UserRememberMeTokenRepository persistentTokenRepository() {
        return new UserRememberMeTokenRepository();
    }

    @Autowired
    public void configureGlobal(@SuppressWarnings("SpringJavaAutowiringInspection") AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsService).passwordEncoder(shaPasswordEncoder());
    }

    @Bean
    public ShaPasswordEncoder shaPasswordEncoder() {
        return new ShaPasswordEncoder(PASSWORD_ENCODING_STRENGTH);
    }

}
