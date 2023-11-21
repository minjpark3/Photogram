package com.cos.photogramstart.handler;

import com.cos.photogramstart.handler.ex.CustomVaildationException;
import com.cos.photogramstart.util.Script;
import com.cos.photogramstart.web.dto.CMRespDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(CustomVaildationException.class)
    public String vaildationException(CustomVaildationException e){
        //CMRespDto, Script비교 -개발자가 응답받을땐 CMR~ 이편하고 클라이언트가 받을땐 스크립트가 편하다.
        //1.클라이언트에게 응답할때는 Script가 좋음
        //2.Ajax통신- CMRespDto
        //3.Android통신- CMRespDto
        return Script.back(e.getErrorMap().toString());
    }
//public class ControllerExceptionHandler {
//    @ExceptionHandler(CustomVaildationException.class)
//    public CMRespDto<?> vaildationException(CustomVaildationException e){
//        return new CMRespDto(-1,e.getMessage(),e.getErrorMap());
//    }

}
