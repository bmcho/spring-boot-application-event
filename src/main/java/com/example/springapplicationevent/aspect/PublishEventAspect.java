package com.example.springapplicationevent.aspect;

import com.example.springapplicationevent.annotation.PublishEvent;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Aspect
public class PublishEventAspect implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;
    private final Executor executor;

    @Override
    public void setApplicationEventPublisher(@Nullable ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public PublishEventAspect(Executor executor) {
        this.executor = executor;
    }

    @AfterReturning(pointcut = "@annotation(publishEvent)", returning = "retValue", argNames = "joinPoint,publishEvent,retValue")
    public void afterSettlementEventReturning(JoinPoint joinPoint, PublishEvent publishEvent, Object retValue) {
        log.info("[@AspectEvent] {}", joinPoint.getSignature());

        if (retValue instanceof List) {
            log.info("[Not Allowed Type] type: {}", retValue.getClass());
            return;
        }

        Object event;
        String errorMsg = "";

        if (retValue == null) {
            errorMsg = "retValue is null";
            log.info("[@AspectEvent] PublishEventAspect.PublishSettlementEvent Error: {}", errorMsg);
            return;
        }

        try {
            event = publishEvent.eventClass()
                .getConstructor(retValue.getClass())
                .newInstance(retValue);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            //나중에 외부 로그서버처리 필요
            errorMsg = e.getMessage();
            log.error("[@AspectEvent] PublishEventAspect.PublishSettlementEvent Error: {}", errorMsg);
            return;
        }

        executor.execute(() -> applicationEventPublisher.publishEvent(event));
    }

}
