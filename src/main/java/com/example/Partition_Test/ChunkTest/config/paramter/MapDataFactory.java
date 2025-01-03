package com.example.Partition_Test.ChunkTest.config.paramter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapDataFactory {

    private final MapParam mapParam;

    private final DbParam dbParam;

    public MapData getMapData(String schema){
        if (schema.equals(dbParam.getMIXING_USERNAME())){
            return mapParam.getMixing();
        }
        else if (schema.equals(dbParam.getELECTRODE_USERNAME())){
            return mapParam.getElectrode();
        }

        return null;

    }


}
