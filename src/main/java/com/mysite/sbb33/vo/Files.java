package com.mysite.sbb33.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Files {
    // 파일 테이블을 만들었으니 엔티티를 만드는 작업
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filename;
    // 아래가 핵심임.
    // 저희가 점프투 스프링 부트 할 때 답변을 질문에 연결시켰죠?
    // 지금은 글에 파일을 연결시키는 겁니다.!! 이럴수가!!!
    @ManyToOne
    private Article article;
}
