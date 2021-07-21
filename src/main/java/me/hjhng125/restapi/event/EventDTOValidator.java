package me.hjhng125.restapi.event;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventDTOValidator {

    public void validate(EventDTO eventDTO, Errors errors) {
        if (eventDTO.getMaxPrice() < eventDTO.getBasePrice() && eventDTO.getMaxPrice() != 0) {
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
        }

        LocalDateTime closeEventDateTime = eventDTO.getCloseEventDateTime();
        LocalDateTime beginEventDateTime = eventDTO.getBeginEventDateTime();
        LocalDateTime closeEnrollmentDateTime = eventDTO.getCloseEnrollmentDateTime();
        LocalDateTime beginEnrollmentDateTime = eventDTO.getBeginEnrollmentDateTime();
        if (closeEventDateTime.isBefore(beginEventDateTime) ||
            closeEventDateTime.isBefore(beginEnrollmentDateTime) ||
            closeEventDateTime.isBefore(closeEnrollmentDateTime)) {

            errors.rejectValue("closeEventDateTime", "wrongValue", "CloseEventDateTime is wrong");
        }

        if (beginEventDateTime.isBefore(beginEnrollmentDateTime) ||
            beginEventDateTime.isBefore(closeEnrollmentDateTime)) {

            errors.rejectValue("beginEventDateTime", "wrongValue", "BeginEventDateTime is wrong");
        }

        if (closeEnrollmentDateTime.isBefore(beginEnrollmentDateTime)) {

            errors.rejectValue("closeEnrollmentDateTime", "wrongValue", "CloseEnrollmentDateTime is wrong");
        }
    }
}
