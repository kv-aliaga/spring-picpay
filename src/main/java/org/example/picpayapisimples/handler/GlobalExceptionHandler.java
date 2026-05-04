package org.example.picpayapisimples.handler;

import org.example.picpayapisimples.handler.exception.BusinessException;
import org.example.picpayapisimples.handler.exception.model.*;
import org.example.picpayapisimples.handler.exception.patch.*;
import org.example.picpayapisimples.handler.exception.transaction.*;
import org.example.picpayapisimples.handler.exception.validation.ValidationException;
import org.example.picpayapisimples.handler.exception.validation.age.*;
import org.example.picpayapisimples.handler.exception.validation.input.*;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.TransactionSerializationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
//    Exceções personalizadas
    @ExceptionHandler(UnderageException.class)
    public ResponseEntity<ErroResponse> handleAge(UnderageException ue){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(ue.getMessage()), 422));
    }

    @ExceptionHandler(UnrealisticAgeException.class)
    public ResponseEntity<ErroResponse> handleAge(UnrealisticAgeException uae){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(uae.getMessage()), 422));
    }

    @ExceptionHandler(InvalidAgeException.class)
    public ResponseEntity<ErroResponse> handleAge(InvalidAgeException iae){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(iae.getMessage()), 422));
    }

    @ExceptionHandler(InvalidCPFException.class)
    public ResponseEntity<ErroResponse> handleInput(InvalidCPFException icpfe){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(icpfe.getMessage()), 422));
    }

    @ExceptionHandler(InvalidDDDException.class)
    public ResponseEntity<ErroResponse> handleInput(InvalidDDDException iddde){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(iddde.getMessage()), 422));
    }

    @ExceptionHandler(InavlidEmailException.class)
    public ResponseEntity<ErroResponse> handleInput(InavlidEmailException iee){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(iee.getMessage()), 422));
    }

    @ExceptionHandler(InvalidPhoneException.class)
    public ResponseEntity<ErroResponse> handleInput(InvalidPhoneException ipe){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(ipe.getMessage()), 422));
    }

    @ExceptionHandler(InvalidAccountTypeException.class)
    public ResponseEntity<ErroResponse> handleInput(InvalidAccountTypeException iate){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(iate.getMessage()), 422));
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErroResponse> handleInput(InvalidInputException iie){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(iie.getMessage()), 422));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErroResponse> handleInput(ValidationException ve){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(ve.getMessage()), 422));
    }

    @ExceptionHandler(ImmutableFieldException.class)
    public ResponseEntity<ErroResponse> handlePatch(ImmutableFieldException ife){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of(ife.getMessage()), 409));
    }

    @ExceptionHandler(InexistentFieldPatchException.class)
    public ResponseEntity<ErroResponse> handlePatch(InexistentFieldPatchException ifpe){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of(ifpe.getMessage()), 400));
    }

    @ExceptionHandler(UniqueViolationException.class)
    public ResponseEntity<ErroResponse> handlePatch(UniqueViolationException uve){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of(uve.getMessage()), 409));
    }

    @ExceptionHandler(PatchException.class)
    public ResponseEntity<ErroResponse> handlePatch(PatchException pe){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of(pe.getMessage()), 400));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErroResponse> handleTransaction(InsufficientBalanceException ibe){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(ibe.getMessage()), 422));
    }

    @ExceptionHandler(NegativeOrZeroTransferException.class)
    public ResponseEntity<ErroResponse> handleTransaction(NegativeOrZeroTransferException nozte){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(nozte.getMessage()), 422));
    }

    @ExceptionHandler(SameAccountTransferException.class)
    public ResponseEntity<ErroResponse> handleTransaction(SameAccountTransferException sate){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(sate.getMessage()), 422));
    }

    @ExceptionHandler(ZeroAmountException.class)
    public ResponseEntity<ErroResponse> handleTransaction(ZeroAmountException zae){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(zae.getMessage()), 422));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErroResponse> handleTransaction(TransactionException te){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(te.getMessage()), 422));
    }

    @ExceptionHandler(ActiveModelDeleteException.class)
    public ResponseEntity<ErroResponse> handleModel(ActiveModelDeleteException amde){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of(amde.getMessage()), 409));
    }

    @ExceptionHandler(InactiveResourceException.class)
    public ResponseEntity<ErroResponse> handleModel(InactiveResourceException ire){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of(ire.getMessage()), 409));
    }

    @ExceptionHandler(MinimumInactivityPeriodException.class)
    public ResponseEntity<ErroResponse> handleModel(MinimumInactivityPeriodException mipe){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of(mipe.getMessage()), 409));
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ErroResponse> handleModel(ModelNotFoundException mnfe){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(List.of(mnfe.getMessage()), 404));
    }

    @ExceptionHandler(ModelException.class)
    public ResponseEntity<ErroResponse> handleModel(ModelException me){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of(me.getMessage()), 400));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResponse> handleBusiness(BusinessException be){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of(be.getMessage()), 400));
    }

//    Exceções do SDK
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException manve){
        List<String> mensagens = manve.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagens, 400));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraint(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Algum dos parâmetros informados é inválido"), 400));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleValidation(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("O corpo da requisição está inválido ou mal formado"), 400));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> handleMissingParam(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Um parâmetro obrigatório não foi informado"), 400));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleMethodMismatch(){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Um parâmetro foi informado com o tipo inválido"), 400));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> handleNoResourceFound(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(List.of("O recurso solicitado não foi encontrado"), 404));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpRequest(){
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErroResponse(List.of("O método HTTP utilizado não é permitido para esta rota"), 405));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpMedia(){
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErroResponse(List.of("O tipo de conteúdo enviado é suportado"), 415));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrity(){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of("Não foi possível realizar a operação por conflito com os dados existentes"), 409));
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErroResponse> handleOptimisticLocking(){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of("Este registro foi alterado por outra operação, tente novamente"), 409));
    }

    @ExceptionHandler(TransactionSerializationException.class)
    public ResponseEntity<ErroResponse> handleTransaction(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(List.of("Não foi possível concluir a transação, tente novamente"), 500));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleAccessDenied(){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroResponse(List.of("Você não tem permissão de acesso para este recurso"), 403));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroResponse> handleAuthentication(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErroResponse(List.of("É necessário estar autenticado para o acesso a este recurso"), 401));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(List.of("Ocorreu um erro inesperado durante a execução"), 500));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(List.of("Ocorreu um erro interno inesperado"), 500));
    }
}