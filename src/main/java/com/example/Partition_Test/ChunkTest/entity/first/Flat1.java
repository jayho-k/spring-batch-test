package com.example.Partition_Test.ChunkTest.entity.first;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flat1 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int flat1;
}
