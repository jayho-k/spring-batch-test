package com.example.Partition_Test.ChunkTest.domain.repository;

import com.example.Partition_Test.ChunkTest.domain.entity.Agv;
import com.example.Partition_Test.ChunkTest.service.dto.AgvAgvSumDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgvRepository extends JpaRepository<Agv, Long> {
    String AgvAgvSumDtoAddress = "new com.example.Partition_Test.ChunkTest.service.dto.AgvAgvSumDto(a.time, b.sum)";
    @Query(value = "select " + AgvAgvSumDtoAddress +
            " from Agv a inner" +
            " join a.agvSum b" +
            " where time in :times")
    List<AgvAgvSumDto> findAgvAndSum(@Param("times") List<Integer> timeList);
}
