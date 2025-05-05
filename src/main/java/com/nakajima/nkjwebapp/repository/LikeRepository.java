package com.nakajima.nkjwebapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nakajima.nkjwebapp.model.Like;

import java.util.List;
import java.util.Map;


@Repository
public interface LikeRepository  extends JpaRepository<Like, Integer> {
    boolean existsByFromUserIdAndToUserId(Integer fromUserId, Integer toUserId);

    //ユーザーへの「いいね」の数をカウント
    int countByToUserId(Integer toUserId);

    // いいね数を月単位でランキング
    @Query("SELECT l.toUserId AS userId, COUNT(l) AS likeCount " +
           "FROM Like l WHERE MONTH(l.createdAt) = MONTH(CURRENT_DATE) AND YEAR(l.createdAt) = YEAR(CURRENT_DATE) " +
           "GROUP BY l.toUserId ORDER BY COUNT(l) DESC")
    List<Map<String, Object>> findTopLikedUsersThisMonth();

}
