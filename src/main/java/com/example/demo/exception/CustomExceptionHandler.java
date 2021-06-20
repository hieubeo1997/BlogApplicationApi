package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
//package này dùng để bắt những ngoại lệ thường gặp khi các controller nhận request từ người dùng
//Thường được kết hợp với @ExceptionHandler để cắt ngang quá trình xử lý của các controller trong trường hợp có ngoại lệ xảy ra
public class CustomExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handlerNotFoundException(NotFoundException ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateRecordException.class)
    public ResponseEntity<?> handlerDuplicateRecord(DuplicateRecordException ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handlerBadRequestException(BadRequestException ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<?> handlerInternalServerException(InternalServerException ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<?> handlerUnAuthenticatedException(UnAuthorizeException ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.FORBIDDEN);
    }
    //Xử lý tất cả các ngoại lệ chưa được khai báo
    //Cho hết vào xử lý dạng Internal_Server_Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerOtherException(Exception ex, WebRequest req){
        //Log error
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
