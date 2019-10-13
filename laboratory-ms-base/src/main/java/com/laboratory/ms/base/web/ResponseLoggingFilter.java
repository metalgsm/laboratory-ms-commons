package com.laboratory.ms.base.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

public class ResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletResponse responseToUse = response;

        boolean shouldLog = LOGGER.isDebugEnabled() && (boolean) request.getAttribute(ShouldLogFilter.SHOULD_LOG);

        if (shouldLog && !isAsyncDispatch(request) && !(response instanceof ContentCachingResponseWrapper)) {
            responseToUse = new ContentCachingResponseWrapper(response);
        }

        try {
            filterChain.doFilter(request, responseToUse);
        } finally {
            if (shouldLog && !isAsyncStarted(request)) {
                ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(responseToUse, ContentCachingResponseWrapper.class);
                LOGGER.debug(getMessage(wrapper));
                wrapper.copyBodyToResponse();
            }
        }
    }

    private String getMessage(ContentCachingResponseWrapper wrapper) {
        StringBuilder msg = new StringBuilder();
        msg.append("status=").append(wrapper.getStatus());

        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = buf.length;
                String payload;
                if (MediaType.APPLICATION_OCTET_STREAM_VALUE.equals(wrapper.getContentType())) {
                    payload = "[octet-stream]";
                } else {
                    try {
                        payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
                    } catch (UnsupportedEncodingException ex) {
                        payload = "[unknown]";
                    }
                }
                msg.append(";payload=").append(payload);
            }
        }

        return msg.toString();
    }

}
