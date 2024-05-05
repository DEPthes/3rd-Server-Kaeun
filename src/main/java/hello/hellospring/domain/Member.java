package hello.hellospring.domain;

import jakarta.persistence.*;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //임의의 값, 데이터를 구분하기 위해 시스템이 저장하는 id
    private String name; //이름

    //@Column(name = "username") //만약 DB에 컬럼명이 username이면 저렇게 작성하면 매핑이 된다
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
