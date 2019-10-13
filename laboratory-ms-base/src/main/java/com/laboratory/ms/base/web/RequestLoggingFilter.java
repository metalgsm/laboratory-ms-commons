package com.laboratory.ms.base.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        HttpServletRequest requestToUse = request;

        boolean shouldLog = LOGGER.isDebugEnabled() && (boolean) request.getAttribute(ShouldLogFilter.SHOULD_LOG);

        if (shouldLog && !isAsyncDispatch(request) && !(request instanceof ContentCachingRequestWrapper)) {
            requestToUse = new ContentCachingRequestWrapper(request);
        }

        try {
            filterChain.doFilter(requestToUse, response);
        } finally {
            if (shouldLog && !isAsyncStarted(requestToUse)) {
                ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(requestToUse, ContentCachingRequestWrapper.class);
                LOGGER.debug(getMessage(wrapper));
            }
        }
    }

    private String getMessage(ContentCachingRequestWrapper wrapper) {
        StringBuilder msg = new StringBuilder();
        msg.append("uri=").append(wrapper.getRequestURI());

        String queryString = wrapper.getQueryString();
        if (queryString != null) {
            msg.append('?').append(queryString);
        }

        String client = wrapper.getRemoteAddr();
        if (StringUtils.hasLength(client)) {
            msg.append(";client=").append(client);
        }
        HttpSession session = wrapper.getSession(false);
        if (session != null) {
            msg.append(";session=").append(session.getId());
        }
        String user = wrapper.getRemoteUser();
        if (user != null) {
            msg.append(";user=").append(user);
        }

        msg.append(";headers=").append(new ServletServerHttpRequest(wrapper).getHeaders());

        byte[] buf = wrapper.getContentAsByteArray();
        if (buf.length > 0) {
            int length = buf.length;
            String payload;
            try {
                payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
            msg.append(";payload=").append(payload);
        }

        return msg.toString();
    }

}
