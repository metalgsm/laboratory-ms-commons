package com.laboratory.ms.base.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class ShouldLogFilter implements Filter {

    static final String SHOULD_LOG = ShouldLogFilter.class.getName() + ".SHOULD_LOG";

    private List<String> ignoredPathPrefixes;

    public ShouldLogFilter(LoggingProperties loggingProperties) {
        ignoredPathPrefixes = Optional.ofNullable(loggingProperties.getIgnoredPathPrefixes()).orElse(Collections.emptyList());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(SHOULD_LOG, shouldLog(request));
        try {
            chain.doFilter(request, response);
        } finally {
            request.removeAttribute(SHOULD_LOG);
        }
    }

    private boolean shouldLog(ServletRequest request) {
        final String path = ((HttpServletRequest) request).getServletPath();
        for (String ignoredPathPrefix : ignoredPathPrefixes) {
            if (path.startsWith(ignoredPathPrefix)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void destroy() {
    }

}
