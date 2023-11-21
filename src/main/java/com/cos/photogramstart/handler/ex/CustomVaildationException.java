package com.cos.photogramstart.handler.ex;

import java.util.Map;

public class CustomVaildationException extends RuntimeException{

    //객체를 구분할때;
    private static final long serialVersionUID = 1L;
    private String massage;
    private Map<String,String> errorMap;

    public CustomVaildationException(String massage, Map<String,String>errorMap){
        super(massage);
        this.errorMap = errorMap;
    }
    public Map<String,String> getErrorMap(){
        return errorMap;
   }

}