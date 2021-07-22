package me.hjhng125.restapi.event;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

    /**
     * JUnit4 에서 사용 방법
     *
     * @Test
     * @Parameters(method = "paramsForTestFree)
     * public void free_test(int basePrice, int maxPrice, boolean isFree) {
     *     // given
     *     Event event = Event.builder()
     *         .basePrice(basePrice)
     *         .maxPrice(maxPrice)
     *         .build();
     *
     *     // when
     *     event.update();
     *
     *     // then
     *     assertThat(event.isFree()).isEqualTo(isFree);
     * }
     *
     * private Object[] paramsForTestFree() {
     *     return new Object[] {
     *         new Object[] {0, 0, true},
     *         new Object[] {100, 0, false},
     *         new Object[] {0, 100, false},
     *         new Object[] {100, 100, false}
     *     };
     * }
     */
    @ParameterizedTest
    @MethodSource
    void freeTest(int basePrice, int maxPrice, boolean isFree) {
        // given
        Event event = Event.builder()
            .basePrice(basePrice)
            .maxPrice(maxPrice)
            .build();

        // when
        event.update();

        // then
        assertThat(event.isFree()).isEqualTo(isFree);
    }

    private static Stream<Arguments> freeTest() {
        return Stream.of(
            Arguments.of(0, 0, true),
            Arguments.of(100, 0, false),
            Arguments.of(0, 100, false),
            Arguments.of(100, 100, false)
        );
    }

    @ParameterizedTest
    @MethodSource
    void offlineTest(String location, boolean isOffline) {
        //given
        Event event = Event.builder()
            .location(location)
            .build();

        //when
        event.update();

        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Stream<Arguments> offlineTest() {
        return Stream.of(
            Arguments.of("어딘가..", true),
            Arguments.of(null, false),
            Arguments.of(" ", false)
        );
    }
}