package com.example.Partition_Test.ChunkTest.entity.first;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.batch.item.Chunk;

@Entity
@Getter
@Setter
@Table(name = "agvsum")
@NoArgsConstructor
@AllArgsConstructor
public class AgvSum extends Chunk<AgvSum> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int sum;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Agv agv;

}
