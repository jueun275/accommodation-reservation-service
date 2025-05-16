package com.example.reservation.global.aop;

import com.example.reservation.global.lock.LockService;
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
  public Object aroundMethod(
      ProceedingJoinPoint joinPoint,
      ReservationLockIdInterface request
  ) throws Throwable {
    lockService.lock(request.getLockKey());

    try {
      return joinPoint.proceed();

    } finally {

      lockService.unlock(request.getLockKey());
    }
  }
}
