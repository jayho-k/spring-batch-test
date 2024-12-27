package com.example.Partition_Test.ChunkTest.domain.repository;

import com.example.Partition_Test.ChunkTest.domain.entity.AddTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddTableRepository extends JpaRepository<AddTable, Long> {

}
