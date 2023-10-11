package edu.school21.exceptions;

public class AlreadyAuthenticatedException extends Throwable{
    public AlreadyAuthenticatedException(String m){
        super(m);
    }
}
