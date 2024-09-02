package com.example.Partition_Test.ChunkTest.mapper;

import com.example.Partition_Test.ChunkTest.entity.first.Flat1;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class flat1Mapper implements FieldSetMapper<Flat1> {

    @Override
    public Flat1 mapFieldSet(FieldSet fs) throws BindException {
        if (fs == null) {
            return null;
        }
        Flat1 flat1 = new Flat1();
        flat1.setFlat1(fs.readInt(0));
        return flat1;
    }

}