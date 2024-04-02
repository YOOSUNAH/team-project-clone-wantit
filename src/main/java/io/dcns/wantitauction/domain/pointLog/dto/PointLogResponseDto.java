package io.dcns.wantitauction.domain.pointLog.dto;

import io.dcns.wantitauction.domain.pointLog.entity.PointLogStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PointLogResponseDto {

    private Long userId;

    private Long pointLogId;

    private Long changedPoint;

    private PointLogStatus status;

    private String details;

    private Long auctionItemId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
