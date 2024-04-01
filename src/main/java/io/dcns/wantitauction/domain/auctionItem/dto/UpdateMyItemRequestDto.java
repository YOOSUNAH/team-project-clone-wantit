package io.dcns.wantitauction.domain.auctionItem.dto;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UpdateMyItemRequestDto {

    private String itemName;
    private String productDescription;
    private Long minPrice;
    private Long maxPrice;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
