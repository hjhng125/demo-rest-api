package me.hjhng125.restapi.event;

import static javax.persistence.EnumType.STRING;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Builder
@EqualsAndHashCode(of = "id") // 기본은 모든 필드를 사용함.
                              // 하지만 이런경우 연관관계가 상호 참조 관계일 경우
                              // stackOverFlow가 발생할 수 있기에 id만을 사용함.
                              // @Data는 EqualsAndHashCode를 기본으로 포함하여 위와 같은 이유로 Entity에서 사용하지 말자.
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Event {

    @Id @GeneratedValue
    private Integer id;

    private String name;
    private String description;

    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime closeEventDateTime;

    private String location;

    private int basePrice;
    private int maxPrice;

    private int limitOfEnrollment;

    private boolean offline;
    private boolean free;

    @Enumerated(STRING)
    private EventStatus eventStatus;
}
