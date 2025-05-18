package com.example.reservation.global.aop;

import com.example.reservation.global.lock.LockService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LockAopAspect {

  private final LockService lockService;

  @Around("@annotation(com.example.reservation.global.aop.ReservationLock) && args(request)")
  public Object aroundMethod(ProceedingJoinPoint joinPoint, ReservationLockIdInterface request)
      throws Throwable {

    List<String> lockKeys = request.getLockKeys();

    try {
      for (String lockKey : lockKeys) {
        lockService.lock(lockKey);
      }
      return joinPoint.proceed();

    } finally {
      // 락 해제는 역순으로 해줘햐 함!! (데드락 방지)
      for (int i = lockKeys.size() - 1; i >= 0; i--) {
        lockService.unlock(lockKeys.get(i));
      }
    }
  }
}
