package study.data_jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
// @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    protected Member() {
        // JPA 표준스펙에 디폴트 생성자가 필수이기에
        // JPA Proxy 기술을 쓸 때 객체를 만들어내고 하기 떄문에 상속을 허용해야함
    }

    public Member(String username) {
        this.username = username;
    }

}
