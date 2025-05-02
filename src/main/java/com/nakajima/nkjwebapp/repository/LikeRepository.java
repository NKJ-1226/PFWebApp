package com.nakajima.nkjwebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nakajima.nkjwebapp.model.Like;
import java.util.Optional;

@Repository
public interface LikeRepository  extends JpaRepository<Like, Integer> {
    boolean existsByFromUserIdAndToUserId(Integer fromUserId, Integer toUserId);

    //ユーザーへの「いいね」の数をカウント
    int countByToUserId(Integer toUserId);
}
