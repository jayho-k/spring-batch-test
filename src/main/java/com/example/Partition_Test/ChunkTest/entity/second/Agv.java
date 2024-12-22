package com.example.Partition_Test.ChunkTest.entity.second;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Agv {
    @Id
    private int id;
    private int time;
    private int even;

    @OneToOne(mappedBy = "agv")
    private AgvSum agvSum;

}
