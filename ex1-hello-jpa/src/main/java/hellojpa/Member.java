package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity //JPA가 관리할 객체라는 것을 알려줌
//@Table(name="User")
public class Member {
    @Id //DB의 PK와 매핑
    private Long id;
    //@Column(name="userName")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
