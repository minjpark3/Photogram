package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomException;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import com.cos.photogramstart.web.dto.user.UserProfileDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SubscribeRepository subscribeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Value("${file.path}")
    private String uploadFolder; //롬복아닌 org로 yml에 있는 path값 챙겨오기
    @Transactional
    public User 회원프로필사진변경(int principalId, MultipartFile profileImageFile){
        UUID uuid =UUID.randomUUID();
        String imageFileName =uuid+"_"+ profileImageFile.getOriginalFilename();
        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        //통신,i/o ->예외가 발생 할 수 있어서 예외처리 필요함
        try{
            Files.write(imageFilePath,profileImageFile.getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        User userEntity = userRepository.findById(principalId).orElseThrow(()->{
            throw  new CustomException("유절르 찾을수 없습니다.");
        });
        userEntity.setProfileImageUrl(imageFileName);
        return userEntity;
    }//더티체킹으로 업데이트됨

    @Transactional(readOnly = true)
    public UserProfileDto 회원프로필(int pageUserId, int principalId){
        UserProfileDto dto =new UserProfileDto();
    //SELECT *FROM image WHERE userId=:userId;
        User userEntity = userRepository.findById(pageUserId).orElseThrow(()->{
            throw new CustomException("해당 프로필 페이지는 없는 페이지입니다.");
        });
        //userEntity.getImages().get(0);
        dto.setUser(userEntity);
        dto.setPageOwnerState(pageUserId==principalId);
        dto.setImageCount(userEntity.getImages().size());
        int subscribeState =subscribeRepository.mSubscribeState(principalId,pageUserId);
        int subscribeCount = subscribeRepository.mSubscribeCount(pageUserId);

        dto.setSubscribeState(subscribeState==1);
        dto.setSubscribeCount(subscribeCount);

        //좋아요 카운트 추가하기
        //profile.jsp에서 그냥 ${image.like.size()}로 적어도 된다
        userEntity.getImages().forEach((image) -> {
            image.setLikeCount(image.getLikes().size());
        });
        return dto;
    }
    @Transactional
    public User 회원수정(int id, User user){
        //1.영속화
        //User userEntity = userRepository.findById(id).get();//1무조건찾았다 걱정마 get()2.못찾았어 익섹션발동 시킬게 orElseThrow()
//        User userEntity = userRepository.findById(10).orElseThrow(()->{
//            return new CustomValidationApiException("찾을수 없는 id입니다.");});
        User userEntity = userRepository.findById(id).orElseThrow(()-> new CustomValidationApiException("찾을수 없는 id입니다."));
        //2.영속화 된 오브젝트 수정-더티체킹(업데이트 완료)
        userEntity.setName(user.getName());
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        userEntity.setPassword(encPassword);
        userEntity.setBio(user.getBio());
        userEntity.setWebsite(user.getWebsite());
        userEntity.setPhone(user.getPhone());
        userEntity.setGender(user.getGender());
        return userEntity;
        //더티체킹이 일어나서 업데이트 완료됨
    }

}
