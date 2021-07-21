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
    public ResponseEntity<Event> create(@RequestBody @Valid EventDTO eventDTO, Errors errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        validator.validate(eventDTO, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }

        Event save = events.save(mapper.mapFrom(eventDTO));
        URI uri = linkTo(EventController.class).slash(save.getId()).toUri();

        return ResponseEntity.created(uri)
            .body(save);
    }
}
