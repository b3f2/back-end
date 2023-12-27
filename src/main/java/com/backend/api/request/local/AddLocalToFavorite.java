package com.backend.api.request.local;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddLocalToFavorite {

    @NotNull(message = "장소 Id를 입력해 주세요")
    private Long localId;

    @Builder
    public AddLocalToFavorite(Long localId) {
        this.localId = localId;
    }
}
