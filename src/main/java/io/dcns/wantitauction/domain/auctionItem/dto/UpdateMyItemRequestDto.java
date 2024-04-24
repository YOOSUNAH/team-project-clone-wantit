package io.dcns.wantitauction.domain.auctionItem.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateMyItemRequestDto {

    private String itemName;
    private String itemDescription;
    private Long minPrice;
    private String category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
