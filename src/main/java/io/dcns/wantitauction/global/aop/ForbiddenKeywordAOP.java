package io.dcns.wantitauction.global.aop;

import io.dcns.wantitauction.domain.auctionItem.dto.CreateProductRequestDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ForbiddenKeywordAOP {

    @Pointcut("@annotation(io.dcns.wantitauction.global.aop.ForbiddenKeyword)")
    private void forbiddenKeywordPoint() {
    }

    @Before("forbiddenKeywordPoint()")
    public void forbiddenKeywordAOP(JoinPoint joinPoint) {
        System.out.println("금지 품목 키워드 검색");
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg instanceof CreateProductRequestDto) {
                String itemDescription = ((CreateProductRequestDto) arg).getItemDescription()
                    .toLowerCase(); // 영문은 소문자로 변환하여 검열
                ForbiddenKeywords[] forbiddenKeywords = ForbiddenKeywords.values();
                for (ForbiddenKeywords forbiddenKeyword : forbiddenKeywords) {
                    if (itemDescription.contains(forbiddenKeyword.getKorean())
                        || itemDescription.contains(forbiddenKeyword.getEnglish())
                    ) {
                        throw new IllegalArgumentException("금지 품목 키워드가 포함되어 있습니다.");
                    }
                }
            }
        }
    }

}
