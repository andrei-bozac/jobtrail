package ro.jobtrail.service;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ValidareException extends RuntimeException{
    public ValidareException(String mesaj){
        super(mesaj);
    }

}
