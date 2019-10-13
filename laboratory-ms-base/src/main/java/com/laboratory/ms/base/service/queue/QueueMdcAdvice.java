package com.laboratory.ms.base.service.queue;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.joor.Reflect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;

import com.laboratory.ms.base.service.HeaderKey;
import com.laboratory.ms.base.service.MdcKey;

public class QueueMdcAdvice implements MethodInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueMdcAdvice.class);

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Instant start = Instant.now();
        try {
            Message message = (Message) invocation.getArguments()[1];
            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            MDC.put(MdcKey.IP, (String) headers.get(HeaderKey.IP));

            try {
                Object messagingMessageListenerAdapter = Reflect.on(invocation.getThis()).field("this$0").field("messageListener").get();
                Method method = Reflect.on(messagingMessageListenerAdapter).field("handlerMethod").field("invokerHandlerMethod")
                        .field("bridgedMethod").get();
                MDC.put(MdcKey.METHOD, method.toString());
            } catch (Exception e) {
                // ignore
            }

            MDC.put(MdcKey.URL, message.getMessageProperties().getConsumerQueue());
            MDC.put(MdcKey.EVENT_LOG, Boolean.TRUE.toString());
            LOGGER.info("Starting call");
            MDC.put(MdcKey.EVENT_LOG, Boolean.FALSE.toString());
            return invocation.proceed();
        } finally {
            MDC.put(MdcKey.EVENT_LOG, Boolean.TRUE.toString());
            MDC.put(MdcKey.DURATION_MILLIS, String.valueOf(ChronoUnit.MILLIS.between(start, Instant.now())));
            LOGGER.info("Ending call");
            MDC.remove(MdcKey.IP);
            MDC.remove(MdcKey.METHOD);
            MDC.remove(MdcKey.URL);
            MDC.remove(MdcKey.EVENT_LOG);
            MDC.remove(MdcKey.DURATION_MILLIS);
        }
    }

}
