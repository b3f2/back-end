package com.backend.api.response.local;

import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.local.Local;
import com.backend.api.entity.util.Address;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LocalResponse {

    private final String name;

    private final Address address;

    private final AreaCategory areaCategory;

    private final String x;

    private final String y;

    @Builder
    public LocalResponse(String name, Address address, AreaCategory areaCategory, String x, String y) {
        this.name = name;
        this.address = address;
        this.areaCategory = areaCategory;
        this.x = x;
        this.y = y;
    }

    public static LocalResponse of(Local local) {
        return LocalResponse.builder()
                .name(local.getName())
                .address(local.getAddress())
                .areaCategory(local.getAreaCategory())
                .x(local.getX())
                .y(local.getY())
                .build();
    }
}
