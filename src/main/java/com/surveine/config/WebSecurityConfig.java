package com.surveine.config;

import com.surveine.security.JwtAccessDeniedHandler;
import com.surveine.security.JwtAuthenticationEntryPoint;
import com.surveine.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Component
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/", "/api/auth/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger/**", "/v3/api-docs/**", "/api/**").permitAll()
                .anyRequest().authenticated()

                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

        return http.build();
    }

//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // http 시큐리티 빌더
//        http.cors()
//                .and()
//                .csrf()
//                .disable() // WebMvcConfig에서 이미 설정했으므로 기본 cors 설정, csrf는 일단 disable.
//                .sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session 기반이 아님을 선언.
//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/api/auth/**", "/swagger-ui/**", "/swagger-resources/**", "/swagger/**", "/v3/api-docs/**", "/api/**").permitAll() // "/" 와 "/auth/**" 경로는 인증 불필요
//                .anyRequest()
//                .authenticated(); // 이외의 모든 경로는 인증 필요
//
//        return http.build();
//    }
//
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
