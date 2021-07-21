package me.hjhng125.restapi.event;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void builder_test() {
        //given
        Event event;

        //when
        event = Event.builder()
            .name("Spring REST API")
            .description("REST API development with Spring")
            .build();

        //then
        assertThat(event).isNotNull();
    }

    @Test
    void javaBean_test() {
        //given
        String springRestApi = "Spring REST API";
        String restApiDevelopmentWithSpring = "REST API development with Spring";

        //when
        Event event = new Event();
        event.setName(springRestApi);
        event.setDescription(restApiDevelopmentWithSpring);

        //then
        assertThat(event).isNotNull();
        assertThat(event.getName()).isEqualTo(springRestApi);
        assertThat(event.getDescription()).isEqualTo(restApiDevelopmentWithSpring);
    }

    @Test
    void equals_hashcode_test() {
        //given

        //when

        //then

    }
}