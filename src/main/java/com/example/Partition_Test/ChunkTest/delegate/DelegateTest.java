package com.example.Partition_Test.ChunkTest.delegate;

import org.springframework.batch.item.Chunk;

public interface DelegateTest<T> {

    Chunk<T> calculate(Chunk<T> chunk, DelegateEnum delegateEnum, boolean param);
}
