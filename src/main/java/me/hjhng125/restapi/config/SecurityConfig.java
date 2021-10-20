//package me.hjhng125.restapi.config;
//
//import lombok.RequiredArgsConstructor;
//import me.hjhng125.restapi.account.AccountService;
//import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpMethod;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
//
///**
// * Spring Security 가 의존성에 들어 있으면 스프링 부트는 Spring Security 용 자동설정을 적용한다.
// * 이 설정에 의하면 모든 요청은 인증을 필요로 하게 된다.
// * 또한 Spring Security 가 사용자를 임의로 인메모리에 만들어줌.
// */
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    private final AccountService accountService;
//    private final PasswordEncoder passwordEncoder;
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new InMemoryTokenStore();
//    }
//
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManager() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(accountService)
//            .passwordEncoder(passwordEncoder);
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().mvcMatchers("/docs/index/html");
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .anonymous()
//            .and()
//            .formLogin()
//            .and()
//            .authorizeRequests()
//            .mvcMatchers(HttpMethod.GET, "/api/**").anonymous()
//            .anyRequest().authenticated();
//    }
//}
