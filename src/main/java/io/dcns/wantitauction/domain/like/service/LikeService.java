package io.dcns.wantitauction.domain.like.service;

import io.dcns.wantitauction.domain.auctionItem.entity.AuctionItem;
import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.like.dto.LikeAuctionItemResponseDto;
import io.dcns.wantitauction.domain.like.dto.LikeResponseDto;
import io.dcns.wantitauction.domain.like.entity.Like;
import io.dcns.wantitauction.domain.like.repository.LikeQueryRepository;
import io.dcns.wantitauction.domain.like.repository.LikeRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final LikeQueryRepository likeQueryRepository;

    @Transactional
    public LikeResponseDto likeAuctionItem(Long auctionItemId, User user) {
        validateAuctionItem(auctionItemId);
        Like like = findByAuctionItemIdAndUserId(auctionItemId, user.getUserId());

        if (like == null) {
            like = new Like(user, auctionItemId);
            likeRepository.save(like);
        } else {
            like.updateLikedStatus();
        }
        return new LikeResponseDto(like.getUserId(), like.getAuctionItemId(), like.isLiked());
    }

    public List<LikeAuctionItemResponseDto> getLikedAuctionItem(Long userId) {
        List<Long> likedAuctionItemIds = likeRepository.findByUserIdAndLikedTrue(userId).stream()
            .map(Like::getAuctionItemId).toList();
        List<AuctionItem> likedAuctionItems = likedAuctionItemIds.stream()
            .map(auctionItemRepository::findByAuctionItemId)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
        return likedAuctionItems.stream().map(this::mapToDto).toList();
    }

    private void validateAuctionItem(Long auctionItemId) {
        if (!auctionItemRepository.existsByAuctionItemId(auctionItemId)) {
            throw new NoSuchElementException("해당 auctionItemId이 존재하지 않습니다.");
        }
    }

    private Like findByAuctionItemIdAndUserId(Long auctionItemId, Long userId) {
        return likeRepository.findByAuctionItemIdAndUserId(auctionItemId, userId);
    }

    private LikeAuctionItemResponseDto mapToDto(AuctionItem auctionItem) {
        return new LikeAuctionItemResponseDto(
            auctionItem.getItemName(),
            auctionItem.getItemDescription(),
            auctionItem.getStartDate(),
            auctionItem.getEndDate()
        );
    }
}
