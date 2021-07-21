package me.hjhng125.restapi.event;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc; // 요청을 만들고 응답을 검증할 수 있는 클래스
    // 웹서버를 띄우지 않기 때문에 더 빠르다.
    // dispatcherServlet을 만들기에 타 단위테스트보다 빠르진 않다.

    @Test
    void createEvent_test() throws Exception {
        //given
        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated());

        //when

        //then

    }
}
