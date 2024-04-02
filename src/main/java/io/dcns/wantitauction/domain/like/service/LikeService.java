package io.dcns.wantitauction.domain.like.service;

import io.dcns.wantitauction.domain.auctionItem.repository.AuctionItemRepository;
import io.dcns.wantitauction.domain.like.dto.LikeResponseDto;
import io.dcns.wantitauction.domain.like.entity.Like;
import io.dcns.wantitauction.domain.like.repository.LikeQueryRepository;
import io.dcns.wantitauction.domain.like.repository.LikeRepository;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final LikeQueryRepository likeQueryRepositor;

    public LikeResponseDto likeAuctionItem(Long auctionItemId, User user) {
        validateAuctionItem(auctionItemId);
        Like liked = findByAuctionItemIdAndUserId(auctionItemId, user.getUserId());

        if (liked != null) {
            likeRepository.delete(liked);
            return new LikeResponseDto(user.getUserId(), auctionItemId, false);
        } else {
            likeRepository.save(new Like(user, auctionItemId));
            return new LikeResponseDto(user.getUserId(), auctionItemId, true);
        }
    }

    public List<LikeResponseDto> getLikedAuctionItem(Long userId) {
        List<Like> likedAuctionItemList = likeQueryRepositor.findAllByUserId(userId);
        return likedAuctionItemList.stream().map(
            like -> new LikeResponseDto(userId, like.getAuctionItemId(), true)).toList();
    }

    private void validateAuctionItem(Long auctionItemId) {
        if (!auctionItemRepository.existsByAuctionItemId(auctionItemId)) {
            throw new NoSuchElementException("해당 auctionItemId이 존재하지 않습니다.");
        }
    }

    private Like findByAuctionItemIdAndUserId(Long auctionItemId, Long userId) {
        return likeRepository.findByAuctionItemIdAndUserId(auctionItemId, userId);
    }
}
