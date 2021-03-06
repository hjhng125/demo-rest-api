package me.hjhng125.restapi.event;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import me.hjhng125.restapi.common.RestDocsConfiguration;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class) // ?????? ????????? ??? ??????????????? ???????????? ???????????????
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
     * ????????? ?????? id ?????? ??????????????? ?????? ????????? ???????????? ????????? ???????????? DTO??? ???????????? ??????????????? DTO??? ????????? DTO??? ?????? ?????? ????????????.
     */
    @Test
    @DisplayName("???????????? ????????? ?????? ?????? ?????????")
    void createEvent_?????????_??????_test() throws Exception {

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
            .location("?????????")
            .build();

        mockMvc.perform(post("/api/events/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(event))
            .accept(MediaTypes.HAL_JSON)
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(header().exists(HttpHeaders.LOCATION))
            .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
            .andExpect(jsonPath("id").exists())
            .andExpect(jsonPath("free").value(Matchers.is(false)))
            .andExpect(jsonPath("offline").value(Matchers.is(true)))
            .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
//            .andExpect(jsonPath("_links.self").exists())
//            .andExpect(jsonPath("_links.get-events").exists())
//            .andExpect(jsonPath("_links.update-event").exists())
//            .andExpect(jsonPath("_links.profile").exists()) // ???????????? ??? ????????? ?????? ???????????? ???????????? ????????? ???????????? ??????????????? ????????????.
            .andDo(document("create-event",
                links(
                    linkWithRel("self").description("link to self"), // Specifies the description
                    linkWithRel("get-events").description("link to query events"),
                    linkWithRel("update-event").description("link to update event"),
                    linkWithRel("profile").description("link to profile")
                ),
                requestHeaders(
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("require hal json"),
                    headerWithName(HttpHeaders.ACCEPT).description("accept header")
                ),
                requestFields(
                    fieldWithPath("name").description("name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                    fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                    fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                    fieldWithPath("closeEventDateTime").description("closeEventDateTime of new event"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("basePrice of new event"),
                    fieldWithPath("maxPrice").description("maxPrice of new event"),
                    fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event")
                ),
                responseHeaders(
                    headerWithName(HttpHeaders.LOCATION).description("location header"),
                    headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header"),
                    headerWithName(HttpHeaders.CONTENT_LENGTH).description("content length header")
                ),
//                relaxedResponseFields( // relaxedResponseFields ??? ???????????? ????????? ?????? ????????? ?????? ??????????????????.
                                         //?????? _link ????????? ???????????? ??????, ????????? ?????? ????????? link ????????? ???????????? ????????? ????????? ????????????.
                                         // relaxedResponseFields ?????? : ????????? ????????? ???????????? ??? ??????.
                                         // relaxedResponseFields ?????? : ????????? ???????????? ?????????????????? ????????? ????????? ???????????? ?????? ?????????. api ????????? ??????????????? ??????????????? ??? ????????? ????????? ??? ??????.
                responseFields(
                    fieldWithPath("id").description("identifier of new event"),
                    fieldWithPath("name").description("name of new event"),
                    fieldWithPath("description").description("description of new event"),
                    fieldWithPath("beginEnrollmentDateTime").description("beginEnrollmentDateTime of new event"),
                    fieldWithPath("closeEnrollmentDateTime").description("closeEnrollmentDateTime of new event"),
                    fieldWithPath("beginEventDateTime").description("beginEventDateTime of new event"),
                    fieldWithPath("closeEventDateTime").description("closeEventDateTime of new event"),
                    fieldWithPath("location").description("location of new event"),
                    fieldWithPath("basePrice").description("basePrice of new event"),
                    fieldWithPath("maxPrice").description("maxPrice of new event"),
                    fieldWithPath("limitOfEnrollment").description("limitOfEnrollment of new event"),
                    fieldWithPath("free").description("it tells is this event is free or not"),
                    fieldWithPath("offline").description("it tells is this event is offline or not"),
                    fieldWithPath("eventStatus").description("event status"),
                    fieldWithPath("manager").description("this is manager information"),
                    fieldWithPath("_links.self.href").description("link to self"),
                    fieldWithPath("_links.get-events.href").description("link to get events"),
                    fieldWithPath("_links.update-event.href").description("link to update event"),
                    fieldWithPath("_links.profile.href").description("link to profile")
                )
            ))
        ;

    }

    /**
     * application.yml ??? spring.jackson.deserialization.fail-on-unknown-properties ?????? true ??? ?????????. ??? ????????? ??????????????? EventDTO ??? ?????? ??? ?????? Property ??? ????????? ?????? BadRequest ??? ????????????.
     *
     * @throws Exception
     */
    @Test
    @DisplayName("?????? ?????? ??? ?????? ?????? ????????? ?????? ?????????")
    void createEvent_?????????_?????????_??????_???_??????_test() throws Exception {

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
            .location("?????????")
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
    @DisplayName("?????? ?????? ?????? ?????? ?????? ?????????")
    void createEvent_??????_??????_??????_??????_??????_test() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
            .name("Spring")
            .description("REST API")
            .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 21, 0, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 28, 23, 59, 59))
            .beginEventDateTime(LocalDateTime.of(2021, 8, 1, 0, 0, 0))
            .closeEventDateTime(LocalDateTime.of(2020, 8, 1, 23, 59, 59)) // ???????????? ??????????????? ???????
            .basePrice(1000) // base ??? max ?????? ???????
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("?????????")
            .build();

        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventDTO))
        )
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("????????? ??????")
    void createEvent_??????_??????_test() throws Exception {
        EventDTO eventDTO = EventDTO.builder()
            .name("Spring")
            .description("REST API")
            .beginEnrollmentDateTime(LocalDateTime.of(2021, 7, 21, 0, 0, 0))
            .closeEnrollmentDateTime(LocalDateTime.of(2021, 7, 28, 23, 59, 59))
            .beginEventDateTime(LocalDateTime.of(2021, 8, 1, 0, 0, 0))
            .closeEventDateTime(LocalDateTime.of(2020, 8, 1, 23, 59, 59)) // ???????????? ??????????????? ???????
            .basePrice(1000) // base ??? max ?????? ???????
            .maxPrice(200)
            .limitOfEnrollment(100)
            .location("?????????")
            .build();

        mockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(eventDTO))
        )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$[0].objectName").exists())
//            .andExpect(jsonPath("$[0].field").exists())
            .andExpect(jsonPath("$[0].defaultMessage").exists())
            .andExpect(jsonPath("$[0].code").exists())
//            .andExpect(jsonPath("$[0].rejectedValue").exists())
        ;
    }
}
