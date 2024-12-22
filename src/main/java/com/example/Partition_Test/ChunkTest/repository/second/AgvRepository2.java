package com.example.Partition_Test.ChunkTest.repository.second;

import com.example.Partition_Test.ChunkTest.dto.AgvAgvSumDto;
import com.example.Partition_Test.ChunkTest.entity.second.Agv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgvRepository2 extends JpaRepository<Agv, Long> {
    //@Query("select new com.zzangmin.gesipan.layer.basiccrud.dto.post.PostSimpleQueryDTO(p.postId, p.postSubject, p.baseTime.createdAt, p.hitCount, p.user.userId, p.user.userNickname) from Post p join fetch p.user where p.postId in :postIds")

    String AgvAgvSumDtoAddress = "new com.example.Partition_Test.ChunkTest.dto.AgvAgvSumDto(a.time, b.sum)";
    @Query(value = "select " + AgvAgvSumDtoAddress +
            " from Agv a inner" +
            " join a.agvSum b" +
            " where time in :times")
    List<AgvAgvSumDto> findAgvAndSum(@Param("times") List<Integer> timeList);
}
