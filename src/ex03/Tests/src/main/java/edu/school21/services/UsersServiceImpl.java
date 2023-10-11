package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;

public class UsersServiceImpl {
    private UsersRepository repository;

    public UsersServiceImpl(UsersRepository repository) {
        this.repository = repository;
    }

    public boolean authenticate(String login, String password) throws AlreadyAuthenticatedException {
        if (repository == null) {
            System.err.println("Repository is null");
            return false;
        }
        User user = getUser(login);
        if (user == null) {
            return false;
        }
        if (user.isAuthenticationStatus()) {
            throw new AlreadyAuthenticatedException("User is already authenticated");
        }
        if (!user.getPassword().equals(password)) {
            user.setAuthenticationStatus(false);
            return false;
        }
        user.setAuthenticationStatus(true);
        return tryToUpdate(user);
    }

    private User getUser(String login) {
        User user;
        try {
            user = repository.findByLogin(login);
            return user;
        } catch (EntityNotFoundException e) {
            System.err.println("Entity Not Found");
            return null;
        }
    }

    private boolean tryToUpdate(User user) {
        try {
            repository.update(user);
            return true;
        } catch (EntityNotFoundException e) {
            System.err.println("Entity Not Found");
            return false;
        }
    }
}
