package me.hjhng125.restapi.event;

import java.time.LocalDateTime;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

@Component
public class EventDTOValidator {

    public void validate(EventDTO eventDTO, Errors errors) {
        if (eventDTO.getMaxPrice() < eventDTO.getBasePrice() && eventDTO.getMaxPrice() != 0) {
            // error 를 reject 할 때 rejectValue 메서드를 사용하면 해당 필드에 에러가 들어간다. - 해당 필드에 해당하는 에러임을 선언
            // 하지만 여러개의 값이 조합되어 에러가 발생한 경우 reject() 사용 - 글로벌 에러
            errors.rejectValue("basePrice", "wrongValue", "BasePrice is wrong");
            errors.rejectValue("maxPrice", "wrongValue", "MaxPrice is wrong");
            errors.reject("wrongPrice", "Values for prices are wrong");
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
