package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.user.User;
import com.cos.photogramstart.domain.user.UserRepository;
import com.cos.photogramstart.handler.ex.CustomValidationApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
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