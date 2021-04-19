package uk.singular.dfs.provider.sandbox.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFound extends Exception{

    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

    public ResourceNotFound(String message){
        super(message);
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

}
