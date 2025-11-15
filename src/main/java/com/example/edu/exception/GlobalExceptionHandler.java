package com.example.edu.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class GlobalExceptionHandler {

    private ProblemDetail problem(HttpStatus status, String title, String detail, HttpServletRequest req) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("path", req.getRequestURI());
        // если используете MDC с traceId — добавьте сюда
        return pd;
    }

    // 1) Валидация тела запроса (@Valid DTO)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fe -> fe.getField(),
                        fe -> fe.getDefaultMessage(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Validation failed", "Payload validation failed", req);
        pd.setProperty("errors", errors);
        return pd;
    }

    // 1b) Валидация параметров/путей (@Validated на контроллере)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        v -> v.getPropertyPath().toString(),
                        v -> v.getMessage(),
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
        ProblemDetail pd = problem(HttpStatus.BAD_REQUEST, "Validation failed", "Constraint violation", req);
        pd.setProperty("errors", errors);
        return pd;
    }

    // 2) Не найдено
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        return problem(HttpStatus.NOT_FOUND, "Not found", ex.getMessage(), req);
    }

    // 3) Бизнес-конфликт/конкуренция данных
    @ExceptionHandler({ IllegalStateException.class, DataIntegrityViolationException.class })
    public ProblemDetail handleConflict(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.CONFLICT, "Conflict", "Business rule violation", req);
    }

    // 4) Кривой JSON/тип параметра
    @ExceptionHandler({ HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class })
    public ProblemDetail handleBadRequest(Exception ex, HttpServletRequest req) {
        return problem(HttpStatus.BAD_REQUEST, "Bad request", "Malformed request", req);
    }

    // 5) Остальные
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unhandled exception", ex);
        return problem(HttpStatus.INTERNAL_SERVER_ERROR, "Internal error", "Unexpected error", req);
    }
}
