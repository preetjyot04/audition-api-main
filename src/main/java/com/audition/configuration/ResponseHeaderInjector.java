package com.audition.configuration;


import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ResponseHeaderInjector extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
        final FilterChain filterChain)
        throws ServletException, IOException {

        final SpanContext spanContext = Span.current().getSpanContext();
        final String traceId = spanContext.getTraceId();
        final String spanId = spanContext.getSpanId();
        response.addHeader("X-Trace-Id", traceId);
        response.addHeader("X-Span-Id", spanId);

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }
}



