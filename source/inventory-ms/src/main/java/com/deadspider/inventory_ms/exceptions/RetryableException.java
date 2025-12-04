package com.deadspider.inventory_ms.exceptions;

public class RetryableException extends RuntimeException{
 
    public RetryableException(String message){
        super(message);
    } 

    public RetryableException(Throwable cause){
        super(cause);
    } 

}
