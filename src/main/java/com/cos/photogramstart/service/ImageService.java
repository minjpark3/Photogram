package com.cos.photogramstart.service;

import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.image.ImageRepository;
import com.cos.photogramstart.web.dto.Image.ImageUploadDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.cos.photogramstart.domain.image.Image;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;
    //private String uploadFolder = "C:/workspace/springbootwork/upload/";
    //이것도 가능하지만 다른곳에서도 업로드 경로가 사용되면 매번 바꿀수 없으니깐 아래 방법으로 사용하는것이 좋음


    @Value("${file.path}")
    private String uploadFolder; //롬복아닌 org로 yml에 있는 path값 챙겨오기

    @Transactional
    public void 사진업로드(ImageUploadDto imageUploadDto, PrincipalDetails principalDetails){
        UUID uuid =UUID.randomUUID();
        String imageFileName =uuid+"_"+ imageUploadDto.getFile().getOriginalFilename();
        //System.out.println("이미지파일이름"+imageFileName);
        Path imageFilePath = Paths.get(uploadFolder+imageFileName);

        //통신,i/o ->예외가 발생 할 수 있어서 예외처리 필요함
        try{
            Files.write(imageFilePath,imageUploadDto.getFile().getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }
        //image 테이블에 저장
        Image image = imageUploadDto.toEntity(imageFileName,principalDetails.getUser());
        Image imageEntity = imageRepository.save(image);
        //System.out.println(imageEntity.toString());
    }
    @Transactional(readOnly = true)//영속성 컨텍스트변경 감지해서 ,더티체킹, flush반영
    public Page<Image> 이미지스토리(int principalId, Pageable pageable){
        Page<Image>images = imageRepository.mStroy(principalId, pageable);
            return images;
}


}
