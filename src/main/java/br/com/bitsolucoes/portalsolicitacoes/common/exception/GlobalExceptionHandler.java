package br.com.bitsolucoes.portalsolicitacoes.common.exception;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception,
                                                   HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception,
                                                     HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> fields.put(error.getField(), error.getDefaultMessage()));
        return build(HttpStatus.BAD_REQUEST, "Dados inválidos.", fields, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException exception,
                                                          HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, exception.getMessage(), Map.of(), request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableMessage(HttpMessageNotReadableException exception,
                                                            HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "O corpo da requisição é inválido ou possui valores incompatíveis.",
                Map.of(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException exception,
                                                       HttpServletRequest request) {
        String message = "O parâmetro '" + exception.getName() + "' possui um formato inválido.";
        return build(HttpStatus.BAD_REQUEST, message, Map.of(exception.getName(), "formato inválido"), request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException exception,
                                                           HttpServletRequest request) {
        String message = "O parâmetro '" + exception.getParameterName() + "' é obrigatório.";
        return build(HttpStatus.BAD_REQUEST, message,
                Map.of(exception.getParameterName(), "obrigatório"), request);
    }

    private ResponseEntity<ApiError> build(HttpStatus status, String message, Map<String, String> fields,
                                           HttpServletRequest request) {
        ApiError error = new ApiError(Instant.now(), status.value(), status.getReasonPhrase(), message,
                request.getMethod(), request.getRequestURI(), fields);
        return ResponseEntity.status(status).body(error);
    }
}
