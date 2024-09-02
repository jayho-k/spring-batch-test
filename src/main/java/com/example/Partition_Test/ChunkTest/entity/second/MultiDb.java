package com.example.Partition_Test.ChunkTest.entity.second;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "multidb")
public class MultiDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int multidb;

}
