package com.audition.configuration;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class ResponseHeaderInjectorTest {

    @Mock
    private SpanContext spanContext;

    @Mock
    private Span span;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private ResponseHeaderInjector injector;

  /*  @Test
    void testDoFilter() throws ServletException, IOException {
        // Given
        MockitoAnnotations.openMocks(this); // Initialize mocks
        String traceId = "1234567890abcdef";
        String spanId = "abcdef1234567890";
        when(span.getSpanContext()).thenReturn(spanContext);
        when(spanContext.getTraceId()).thenReturn(traceId);
        when(spanContext.getSpanId()).thenReturn(spanId);
        // when(Span.current()).thenReturn(span);

        // When
        injector.doFilter(request, response, filterChain);

        // Then
        verify(response, times(1)).addHeader("X-Trace-Id", traceId);
        verify(response, times(1)).addHeader("X-Span-Id", spanId);
        verify(filterChain, times(1)).doFilter(request, response);
    }*/
}
