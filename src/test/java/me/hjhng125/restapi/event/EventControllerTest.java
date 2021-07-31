package me.hjhng125.restapi.event;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
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
    @MockBean
    EventMapper mapper;
    @MockBean
    EventDTOValidator validator;

    /**
     * 이제 컨트롤러가 Event 타입이 아닌 EventDTO 로 받게 되었고,
     * 그렇기에 Mocking 이 제대로 동작하지 않는다.
     *
     * 실제 컨트롤러에서 Mocking 을 하려면 테스트에서 넣은 파라미터와
     * events.save()의 인자인 event 와 같아야 한다.
     *
     * EventController 에서 DTO로 받은 후 mapper 를 통해 Event 로 변환하였기 때문에
     * mapper 의 동작까지 Mocking 하지 않는 한 slicing test 를 할 수 없다.
     * Controller 의 mapper 결과는 null 이다.
     * 따라서 Mocking 할 객체가 너무 많아지기에 편의상 통합 테스트로 해결하겠다.
     */
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

        EventDTO eventDTO = EventDTO.builder()
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
        when(events.save(mapper.mapFrom(eventDTO))).thenReturn(event);

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventDTO))
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
        ;
    }
}
