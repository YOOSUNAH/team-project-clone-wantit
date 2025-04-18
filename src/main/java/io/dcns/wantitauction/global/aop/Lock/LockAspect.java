package io.dcns.wantitauction.global.aop.Lock;

import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
@Slf4j(topic = "LockAspect")
public class LockAspect {
    private final RedissonClient redissonClient;

    @Around("@annotation(lockable)")
    public Object applyLock(ProceedingJoinPoint joinPoint,
                            Lockable lockable) throws Throwable {
        RLock lock = redissonClient.getFairLock(lockable.value());
        try {
            // 락 획득 시도
            if (lock.tryLock(lockable.waitTime(), lockable.leaseTime(), TimeUnit.MILLISECONDS)) {
                try {
                    log.info("get lock");
                    return joinPoint.proceed();
                } finally {
                    log.info("unlock");
                    lock.unlock();
                }
            } else {
                // 락 획득에 실패했을 때의 처리
                throw new RuntimeException("락 획득 실패");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupt 발생");
        }
    }
}
