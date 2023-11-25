package com.cos.photogramstart.service;

import com.cos.photogramstart.domain.subscribe.SubscribeRepository;
import com.cos.photogramstart.handler.ex.CustomApiException;
import com.cos.photogramstart.web.dto.subscribe.SubscribeDto;
import lombok.RequiredArgsConstructor;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SubscribeService {


    private final SubscribeRepository subscribeRepository;
    //쿼리를 직접 사용하기 위해서 사용
    private final EntityManager em; //레파지토리는 EntityManager를 구현해서 만들어져있는 구현체



    @Transactional(readOnly = true)//셀렉트만할거라
    public List<SubscribeDto>구독리스트(int principalId,int pageUserId){
    //쿼리 준비
        StringBuffer sb = new StringBuffer();
        sb.append("select user.id, user.username, user.profileImageUrl, ");//,뒤에 꼭 한칸 띄우기 오류생길 수 있음
        sb.append("if((select 1 from subscribe where fromUserId = ? AND toUserId = user.id),1,0) subscribeState, ");  // principalDetails.user.id
        sb.append("if((?= user.id),1,0) equalUserState "); // principalDetails.user.id
        sb.append("from user inner join subscribe  ");
        sb.append("on user.id = subscribe.toUserId " );
        sb.append("where subscribe.fromUserId =?"); // pageUserId
        //쿼리 완성
        Query query = em.createNativeQuery(sb.toString())
                .setParameter(1, principalId)
                .setParameter(2, principalId)
                .setParameter(3, pageUserId);

        //System.out.println("쿼리 : "+query.getResultList().get(0));
    //쿼리 실행
        JpaResultMapper result  = new JpaResultMapper();
        List<SubscribeDto> subscribeDtos = result.list(query, SubscribeDto.class);
        return subscribeDtos;
    }

    @Transactional
    public void 구독하기(int fromUserId, int toUserId) {
        try{
            subscribeRepository.mSubscribe(fromUserId, toUserId);
        }catch (Exception e){
            throw new CustomApiException("이미 구독중입니다.");
        }
    }

    @Transactional
    public void 구독취소하기(int fromUserId, int toUserId) {
        subscribeRepository.mUnSubscribe(fromUserId, toUserId);
    }
}