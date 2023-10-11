package edu.school21.repositories;

public class NotSavedSubEntityException extends Throwable {
    public NotSavedSubEntityException(String s) {
        super(s);
    }
}
