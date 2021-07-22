package me.hjhng125.restapi.event;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
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

        URI uri = linkTo(EventController.class).slash(save.getId()).toUri();

        return ResponseEntity.created(uri)
            .body(save); // 컨드롤러에서 body 에 담아줄 객체를 Json 으로 변환할 때 ObjectMapper 를 사용하며,
                         // 이 과정에서 ObjectMapper 는 BeanSerializer 를 사용한다.
                         // Event 객체는 자바빈 스펙을 준수하기 때문에 BeanSerializer 를 통해 Json 으로 변환이 가능하다. - reflection 을 사용할 것임
                         // 하지만 이에 반해 Errors 객체는 자바빈 스펙을 준수하지 않기 때문에 Json 으로 변환할 수 없다.
                         // Json 으로 변환을 시도하는 이유는 컨트롤러 정의 시 produces 를 HAL_JSON_VALUE 로 선언했기 때문
    }
}
