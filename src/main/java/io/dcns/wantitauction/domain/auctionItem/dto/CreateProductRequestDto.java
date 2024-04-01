package io.dcns.wantitauction.domain.auctionItem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateProductRequestDto {

    private String itemName;
    private String productDescription;
    private Long minPrice;
    private Long maxPrice;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private final AuctionItemEnum status = AuctionItemEnum.READY;

}
