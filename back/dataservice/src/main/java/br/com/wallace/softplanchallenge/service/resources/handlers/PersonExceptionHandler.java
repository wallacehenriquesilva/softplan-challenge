package br.com.wallace.softplanchallenge.service.resources.handlers;


import br.com.wallace.softplanchallenge.service.data.base.enums.ExceptionHandlerStatusEnum;
import br.com.wallace.softplanchallenge.service.data.exceptions.*;
import br.com.wallace.softplanchallenge.service.data.model.ApiExceptionResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * Classe de handler de exception, onde são tratadas as excetpions do tipo RuntimeException para que seja
 * enviado ao client a resposta adenquata para aquele tipo de exception.
 *
 * @author Wallace Silva
 * @version 1.0
 * @since 2019-11-19
 */
@ControllerAdvice
public class PersonExceptionHandler {
    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ApiExceptionResponseModel> personNotFoundException(
            PersonNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Pessoa não encontrado para o id")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.PERSON_NOT_FOUND.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(SexInvalidException.class)
    public ResponseEntity<ApiExceptionResponseModel> sexInvalidException(
            SexInvalidException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Sexo inválido para a pessoa")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.INVALID_SEX_ERROR.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiExceptionResponseModel> userNotFoundException(
            UserNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Usuário não encontrado para a operação")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.USER_NOT_FOUND.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(PersonNotPermitedForUserException.class)
    public ResponseEntity<ApiExceptionResponseModel> personNotPermitedForUserException(
            PersonNotPermitedForUserException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("O usuário não possui permissão para realizar operações com essa pessoa.")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.USER_NOT_PERMITED.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(UnformatedCpfException.class)
    public ResponseEntity<ApiExceptionResponseModel> unformatedCpfException(
            UnformatedCpfException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("O cpf do usuário não se encontra no padrão de formatação.")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.UNFORMATED_CPF.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(DuplicatedCpfException.class)
    public ResponseEntity<ApiExceptionResponseModel> duplicatedCpfException(
            DuplicatedCpfException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Esse cpf já esta em uso por outra pessoa.")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.DUPLICATED_CPF.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(DuplicatedEmailException.class)
    public ResponseEntity<ApiExceptionResponseModel> duplicatedEmailException(
            DuplicatedEmailException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Esse e-mail já esta em uso por outra pessoa.")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.DUPLICATED_EMAIL.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiExceptionResponseModel> invalidEmailException(
            InvalidEmailException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("O email informado não é valido.")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.INVALID_EMAIL.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }
}