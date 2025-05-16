package com.example.reservation.global.lock;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LockService {
  private final RedissonClient redissonClient;

  public void lock(String lockKey) {
    RLock lock = redissonClient.getLock(lockKey);
    log.debug("Trying to acquire lock: {}", lockKey);
    try {
      boolean acquired = lock.tryLock(1, 15, TimeUnit.SECONDS);
      if (!acquired) {
        log.error("Failed to acquire lock - key: {}", lockKey);
        throw new IllegalStateException("다른 사용자가 이미 해당 방에대한 예약을 진행 중입니다.");
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("락 획득 중 인터럽트 발생", e);
    }
  }
//  public void lockWithRetry(String lockKey) {
//    int retryCount = 3;
//    int waitMs = 200;
//
//    for (int i = 0; i < retryCount; i++) {
//      try {
//        RLock lock = redissonClient.getLock(lockKey);
//        boolean acquired = lock.tryLock(1, 15, TimeUnit.SECONDS);
//        if (acquired) {
//          return;
//        }
//        Thread.sleep(waitMs);
//      } catch (InterruptedException e) {
//        Thread.currentThread().interrupt();
//        throw new IllegalStateException("락 인터럽트", e);
//      }
//    }
//
//    throw new IllegalStateException("예약이 너무 많이 몰렸습니다. 잠시 후 다시 시도해주세요.");
//  }

  public void unlock(String lockKey) {
    log.debug("unlock");
    RLock lock = redissonClient.getLock(lockKey);
    if (lock.isHeldByCurrentThread()) {
      lock.unlock();
      log.debug("Lock released: {}", lockKey);
    }
  }
}
