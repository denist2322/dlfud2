package com.mysite.sbb33.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;
    private String title;
    private String body;

    @ManyToOne
    private User user;

    // 글 테이블에 파일 테이블을 연결시켰어요!!!
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Files> fileList;
}
