package lv.notes.ims.controller;

import lombok.extern.log4j.Log4j2;
import lv.notes.ims.dto.BadRequestDto;
import lv.notes.ims.excpetion.EntityNotFoundException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static java.lang.String.valueOf;

@Log4j2
@ControllerAdvice(basePackages = {"lv.notes.ims.controller"})
public class GenericExceptionHandler {
    private static final String ABSENT_PARAMETER_NOT_VALID_MESSAGE = "Absent of mandatory request parameters or invalid parameter value";

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestDto> handleValidationFailedException(MethodArgumentNotValidException ex) {
        var errorFields = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(this::getErrorField)
                .toList();

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getBadRequestDto(errorFields));
    }

    private BadRequestDto.Field getErrorField(FieldError fieldError) {
        return new BadRequestDto.Field()
                .setValue(valueOf(fieldError.getRejectedValue()))
                .setDetails(fieldError.getDefaultMessage())
                .setName(fieldError.getField());
    }

    private BadRequestDto getBadRequestDto(List<BadRequestDto.Field> errorFields) {
        var responseBody = new BadRequestDto();
        responseBody.setMessage(ABSENT_PARAMETER_NOT_VALID_MESSAGE);
        responseBody.setFields(errorFields);
        return responseBody;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public void handleUnexpectedException(Exception ex) {
        log.error("Internal unexpected error: {} ", ex.getMessage(), ex);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleEntityNotFoundException(EntityNotFoundException ex) {
        log.debug("Entity not found: entity [{}], message [{}]",
                ex.getEntityName(), ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleOptimisticLockException(OptimisticLockingFailureException ex) {
        log.debug("Optimistic locking failure: message [{}]", ex.getMessage());
    }

}
