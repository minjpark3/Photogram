package com.cos.photogramstart.domain.image;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ImageRepository extends JpaRepository<Image, Integer> {
    @Query(value = "select * from image where userId in (select toUserId from subscribe where fromUserId = :principalId)", nativeQuery = true)
    Page<Image> mStroy(int principalId, Pageable pageable);
    //@Query(value = "SELECT * FROM image WHERE userId IN (SELECT toUserId FROM subscribe WHERE fromUserId = :principalId) ORDER BY id DESC", nativeQuery = true)
    //Page<Image> mStroy(int principalId, Pageable pageable);



}
