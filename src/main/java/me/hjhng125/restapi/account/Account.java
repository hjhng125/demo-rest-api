package me.hjhng125.restapi.account;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class Account {

    @Id @GeneratedValue
    private Long id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 여러개의 enum 을 가질 수 있기 때문에 Collection으로 매핑해주는 어노테이션
                                 // 기본으로 List, Set의 fetchType은 Lazy 이지만, 가져올 role이 2개뿐이고, 거의 매번 필요한 정보기 때문에 Eager로 바꾼다.
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
