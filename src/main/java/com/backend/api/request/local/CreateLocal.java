package com.backend.api.request.local;

import com.backend.api.entity.local.AreaCategory;
import com.backend.api.entity.util.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateLocal {

    @NotBlank(message = "장소명을 입력해 주세요")
    private String name;

    @NotBlank(message = "x좌표를 입력해 주세요")
    private String x;

    @NotBlank(message = "y좌표를 입력해 주세요")
    private String y;

    @NotNull(message = "주소를 입력해 주세요")
    private Address address;

    @NotNull(message = "지역 카테고리를 입력해 주세요")
    private AreaCategory areaCategory;

    @Builder
    public CreateLocal(String name, String x, String y, Address address, AreaCategory areaCategory) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.address = address;
        this.areaCategory = areaCategory;
    }
}
