package me.hjhng125.restapi.event;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc; // 요청을 만들고 응답을 검증할 수 있는 클래스
    // 웹서버를 띄우지 않기 때문에 더 빠르다.
    // dispatcherServlet을 만들기에 타 단위테스트보다 빠르진 않다.

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EventRepository events;

    @Test
    void createEvent_test() throws Exception {
        Event event = Event.builder()
            .name("Spring")
            .description("REST API")
            .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 21, 0, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 28, 23, 59, 59))
            .beginEventDateTime(LocalDateTime.of(2021, 8, 1, 0, 0, 0))
            .closeEventDateTime(LocalDateTime.of(2021, 8, 1, 23, 59, 59))
            .basePrice(100)
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("강남역")
            .build();

        event.setId(1);
        when(events.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event))
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists());

    }
}
