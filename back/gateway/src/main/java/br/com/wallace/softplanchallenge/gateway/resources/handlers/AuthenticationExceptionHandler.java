package br.com.wallace.softplanchallenge.gateway.resources.handlers;

import br.com.wallace.softplanchallenge.gateway.data.base.enums.ExceptionHandlerStatusEnum;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidCredentialsException;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.InvalidEmailException;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserAlreadyExistsException;
import br.com.wallace.softplanchallenge.gateway.data.exceptions.UserNotFoundException;
import br.com.wallace.softplanchallenge.gateway.data.model.ApiExceptionResponseModel;
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
public class AuthenticationExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiExceptionResponseModel> invalidCredentialsException(
            InvalidCredentialsException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("Email e/ou senha inválidos")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.INVALID_CREDENTIALS.name())
                        .timestamp(new Date().getTime())
                        .path(request.getServletPath())
                        .build());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiExceptionResponseModel> userAlreadyExists(
            UserAlreadyExistsException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ApiExceptionResponseModel
                        .builder()
                        .message("O usuário já esta cadastrado")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.USER_ALREADY_EXISTS.name())
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
                        .message("O usuário não foi encontrado")
                        .error(e.toString())
                        .status(ExceptionHandlerStatusEnum.USER_NOT_FOUND.name())
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
