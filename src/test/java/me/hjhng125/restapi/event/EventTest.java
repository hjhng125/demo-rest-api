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
    void free_test() {
        // given
        Event event = Event.builder()
            .basePrice(0)
            .maxPrice(0)
            .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isTrue();

        // given
        Event event2 = Event.builder()
            .basePrice(100)
            .maxPrice(0)
            .build();

        // when
        event2.update();

        // then
        assertThat(event2.isFree()).isFalse();

        // given
        Event event3 = Event.builder()
            .basePrice(0)
            .maxPrice(100)
            .build();

        // when
        event3.update();

        // then
        assertThat(event3.isFree()).isFalse();
    }

    @Test
    void offline_test() {
        //given
        Event event = Event.builder()
            .location("어딘가..")
            .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isTrue();

        //given
        Event event2 = Event.builder()
            .build();

        //when
        event2.update();

        //then
        assertThat(event2.isOffline()).isFalse();
    }
}