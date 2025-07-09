package com.apgroup.auth.http;

import com.apgroup.auth.dto.StringErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author Louis Figes (W21017657)
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Standard error response for generic exceptions.
     * @param ex the exception
     * @return a response entity with  500 internal server error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StringErrorResponse> handleGenericException(Exception ex) {
        StringErrorResponse errorDTO = new StringErrorResponse("Internal Server Error");
        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Override the default handler for HttpMessageNotReadableException
     * provided by ResponseEntityExceptionHandler.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        StringErrorResponse errorDTO = new StringErrorResponse("Invalid request, please check API documentation");
        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }
}
