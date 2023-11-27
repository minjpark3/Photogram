package com.cos.photogramstart.web.api;


import com.cos.photogramstart.config.auth.PrincipalDetails;
import com.cos.photogramstart.domain.comment.Comment;
import com.cos.photogramstart.service.CommentService;
import com.cos.photogramstart.web.dto.CMRespDto;
import com.cos.photogramstart.web.dto.comment.CommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommetApiController {
    private final CommentService commentService;

    @PostMapping("/api/comment")
    public ResponseEntity<?>commnetSave(@RequestBody CommentDto commentDto, @AuthenticationPrincipal PrincipalDetails principalDetails){
        Comment comment = commentService.댓글쓰기(commentDto.getContent(),commentDto.getImageId(),principalDetails.getUser().getId());
        return new ResponseEntity<>(new CMRespDto<>(1,"댓글쓰기 성공",comment), HttpStatus.OK);
    }
    @DeleteMapping("/api/comment/{id}")
    public ResponseEntity<?>commnetDelete(@PathVariable int id){

        return null;
    }

}
