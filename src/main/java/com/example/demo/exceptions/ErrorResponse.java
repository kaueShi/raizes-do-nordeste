package com.example.demo.exceptions;

public record ErrorResponse(String error, String message, java.util.List<ErrorDetail> details,
                            java.time.Instant timestamp, String path, String traceId) {}
