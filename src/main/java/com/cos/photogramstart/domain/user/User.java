package com.cos.photogramstart.domain.user;

import com.cos.photogramstart.domain.image.Image;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity //디비에 테이블 생성
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 20, unique = true) //중복불가
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String name;
    private String website;
    private String bio;
    private String phone;
    private String gender;

    private String profileImageUrl;
    private String role; //권한

    //나는 연관관계의 주인이 아니다 그러므로 테이블에 칼럼 만들지마라
    //User를 Select할때 해당 User id로 등록된 image들을 다 가져와 .
    //Lazy= User를 select할때 해당 User id로 등록된 image들을 가져오지마 -대신 getImages()함수가 함수의 image들이 호출될때 가져와
    //Eager=User를 select할때 해당 User id로 등록된 image들을 전부 Join해서 가져와
    //@JsonIgnoreProperties({"user"})내부의 유저는 무시하고 파싱해줌 ..
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user"})
    private List<Image> images; //양방향 매핑!!

    private LocalDateTime createDate;

    @PrePersist //디비에 insert되기 전에 실행
    private void createDate(){
        this.createDate=LocalDateTime.now();
    }
}
