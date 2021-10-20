package me.hjhng125.restapi.event;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import java.util.Arrays;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/events", produces = HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

    private final EventRepository events;
    private final EventMapper mapper;
    private final EventDTOValidator validator;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid EventDTO eventDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        validator.validate(eventDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Event save = events.save(mapper.mapFrom(eventDTO));
        save.update();

        // Location 에 넣을 링크를 Spring HATEOAS 가 제공하는 linkTo 메서드를 통해 만들었다.
        WebMvcLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(save.getId());
        URI uri = selfLinkBuilder.toUri();

        // 원래 BeanSerializer 가 직렬화를 할 때 객체의 필드 이름을 사용하여 직렬화를 한다.
        // 그렇다면 EntityModel 의 프로퍼티인 content 가 응답에 있어야 하지만 실제 응답에선 content 라는 key 를 찾아볼 수 없다.
        // 그 이유는 content 를 가져오는 getContent 메서드에 @JsonUnwrapped 가 선언되어 있기 때문이다.
        // @JsonUnWrapped 는 해당 필드가 wrapping 된 객체라면 wrapping 된 내부의 필드를 꺼내준다.
        EntityModel<Event> entityModel = EntityModel.of(save, Arrays.asList(
            linkTo(EventController.class).withRel("get-events"),
            selfLinkBuilder.withSelfRel(),
            selfLinkBuilder.withRel("update-event"),
            Link.of("/docs/index.html#resources-events-create").withRel("profile")
        ));

        return ResponseEntity.created(uri)
            .body(entityModel); // 컨드롤러에서 body 에 담아줄 객체를 Json 으로 변환할 때 ObjectMapper 를 사용하며,
                         // 이 과정에서 ObjectMapper 는 BeanSerializer 를 사용한다.
                         // Event 객체는 자바빈 스펙을 준수하기 때문에 BeanSerializer 를 통해 Json 으로 변환이 가능하다. - reflection 을 사용할 것임
                         // 하지만 이에 반해 Errors 객체는 자바빈 스펙을 준수하지 않기 때문에 Json 으로 변환할 수 없다.
                         // Json 으로 변환을 시도하는 이유는 컨트롤러 정의 시 produces 를 HAL_JSON_VALUE 로 선언했기 때문
    }

}
