//package me.hjhng125.restapi.account;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.fail;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.Set;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class AccountServiceTest {
//
//
//    @Autowired
//    AccountService accountService;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//
//    @Test
//    void findByUsername() {
//        //given
//        String usernaem = "hjhng125@nate.com";
//        String password = "pass";
//        Account account = Account.builder()
//            .email(usernaem)
//            .password(password)
//            .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
//            .build();
//        Account savedAccount = accountService.save(account);
//
//        //when
//        UserDetails userDetails = accountService.loadUserByUsername(usernaem);
//
//        //then
//        assertThat(passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
//
//    }
//
//    @Test
//    void findByUsernameFail() {
//        //given
//        String username = "who are u?";
//
//        //when
//        try {
//            accountService.loadUserByUsername(username);
//            fail("supposed to be failed"); // 테스트를 실패하게 만듦
//        } catch (UsernameNotFoundException e) {
//            assertThat(e.getMessage()).containsSequence(username);
//        }
//
//        //then
//    }
//
//    @Test
//    void findByUsernameFail2() {
//        //given
//        String username = "who are u?";
//
//        //when
//        UsernameNotFoundException usernameNotFoundException = assertThrows(UsernameNotFoundException.class, () -> accountService.loadUserByUsername(username));
//
//        //then
//        assertThat(usernameNotFoundException.getMessage()).isEqualTo(username);
//
//
//    }
//}