package uk.singular.dfs.provider.sandbox.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidMessageException extends Exception{

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public InvalidMessageException(String message){
        super(message);
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
