package com.example.profdebug1.profdebug1.configurations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint

import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder(){
        new BCryptPasswordEncoder()
    }

    @Autowired
    PasswordEncoder passwordEncoder

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .formLogin()
                .loginProcessingUrl('/api/login')
                .successHandler { req, res, e -> res.status = 200 }
                .failureHandler { req, res, e -> res.status = 401 }

                .and()
                .exceptionHandling()
                .accessDeniedHandler { req, res, e -> res.status = 401 }

                .and()
                .logout()

                .and()
                .authorizeRequests()
                .antMatchers('/api/user/reg').permitAll()
                .anyRequest().authenticated()

                .and().httpBasic()
    }

}
