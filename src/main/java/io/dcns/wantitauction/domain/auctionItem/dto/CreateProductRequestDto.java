package io.dcns.wantitauction.domain.auctionItem.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItemEnum;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CreateProductRequestDto {

    private String itemName;
    private String itemDescription;
    private Long minPrice;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private final AuctionItemEnum status = AuctionItemEnum.READY;

    public CreateProductRequestDto(String itemName, String itemDescription, Long minPrice,
        LocalDateTime startDate, LocalDateTime endDate) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.minPrice = minPrice;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
