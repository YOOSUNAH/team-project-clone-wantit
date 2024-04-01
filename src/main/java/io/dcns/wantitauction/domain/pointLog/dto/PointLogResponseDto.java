package io.dcns.wantitauction.domain.pointLog.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointLogResponseDto {

    private Long userId;

    private Long pointLogId;

    private Long changedPoint;

    private String details;

    private LocalDateTime changedDate;
}
