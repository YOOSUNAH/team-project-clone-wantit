package io.dcns.wantitauction.domain.point.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointRequestDto {

    private Long changedPoint;

    private String details;
}
