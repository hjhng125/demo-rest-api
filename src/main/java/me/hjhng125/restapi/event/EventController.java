package me.hjhng125.restapi.event;

import static org.springframework.hateoas.MediaTypes.HAL_JSON_VALUE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/events", produces = HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {


    @PostMapping
    public ResponseEntity<Event> create(@RequestBody Event event) {
        URI uri = linkTo(EventController.class).slash("{id}").toUri();
        event.setId(1);
        return ResponseEntity.created(uri)
            .body(event);
    }
}
