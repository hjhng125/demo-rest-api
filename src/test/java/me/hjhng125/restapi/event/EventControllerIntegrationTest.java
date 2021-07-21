package me.hjhng125.restapi.event;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    EventRepository events;

    @Autowired
    EventMapper mapper;

    /**
     * 입력값 제한 id 또는 계산되어야 하는 값들은 입력되지 않도록 제한하기 DTO로 분리하여 파라미터를 DTO로 받으면 DTO에 있는 것만 받아온다.
     */
    @Test
    @DisplayName("테스트 성공을 위해 EventDTO 로 변경")
    void createEvent_입력값_제한_test() throws Exception {

        EventDTO event = EventDTO.builder()
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

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event))
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("id").exists())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("id").value(Matchers.not(Integer.MAX_VALUE)))
            .andExpect(jsonPath("free").value(Matchers.not(true)))
            .andExpect(jsonPath("offline").value(Matchers.not(true)))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
        ;

    }

    /**
     * application.yml 에 spring.jackson.deserialization.fail-on-unknown-properties 값을 true 로 주었다. 이 옵션은 파라미터인 EventDTO 가 받을 수 없는 Property 가 넘어온 경우 BadRequest 를 리턴한다.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("입력 받을 수 없는 값을 사용한 경우 에러가 발생한다.")
    void createEvent_제한된_입력값_요청_시_에러_test() throws Exception {

        Event event = Event.builder()
            .id(2)
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
            .free(true)
            .offline(true)
            .eventStatus(EventStatus.PUBLISHED)
            .build();

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event))
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;

    }

    @Test
    @DisplayName("필수 입력 값이 없는 경우 에러가 발생한다.")
    void createEvent_필수_값이_없는_경우_에러_test() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
            .name("Spring")
            .description("REST API")
            .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 21, 0, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 28, 23, 59, 59))
            .beginEventDateTime(LocalDateTime.of(2021, 8, 1, 0, 0, 0))
            .closeEventDateTime(LocalDateTime.of(2020, 8, 1, 23, 59, 59)) // 종료일이 시작일보다 이전?
            .basePrice(1000) // base 가 max 보다 크다?
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("강남역")
            .build();

        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventDTO))
        )
            .andExpect(status().isBadRequest());
    }
}
