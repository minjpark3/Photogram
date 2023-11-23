package com.cos.photogramstart.domain.image;

import com.cos.photogramstart.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String caption; // ex)나 오늘 너무 피곤하다~
    private String postImageUrl;// 사진을 전송받아서 그사진을 서버에 특정폴더에 저장- db에 그 저장된 경로를insert

    @JoinColumn(name = "userId")
    @ManyToOne(fetch = FetchType.EAGER)
    private User user; //한명의 유저는 N개의 이미지 가능 1개의 이미지는 1명의 유저

    //이미지 좋아요
    //이미지 댓글


    private LocalDateTime createDate;

    @PrePersist //디비에 insert되기 전에 실행
    private void createDate(){
        this.createDate=LocalDateTime.now();
    }

    //오브젝트를 콘솔에 출력할때 문제가 될수 있어서 User 부분을 출력되지 않게 함.
//    @Override
//    public String toString(){
//        return "Image[id="+id+",caption="+caption+",postImageUrl="+postImageUrl+",createDate="+createDate+"]";
//    }


}
