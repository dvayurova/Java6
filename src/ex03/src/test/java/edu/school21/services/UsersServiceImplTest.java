package edu.school21.services;

import edu.school21.exceptions.AlreadyAuthenticatedException;
import edu.school21.exceptions.EntityNotFoundException;
import edu.school21.models.User;
import edu.school21.repositories.UsersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceImplTest {
    private static User user;
    private static UsersRepository mockRepository;
    private  static UsersServiceImpl usersService;

    @BeforeAll
    static void setUp() {
        mockRepository = Mockito.mock(UsersRepository.class);
        usersService = new UsersServiceImpl(mockRepository);

        user = new User();
        user.setLogin("testuser");
        user.setPassword("password");
        user.setAuthenticationStatus(false);
    }

    @AfterEach
   void tearDown(){
        reset(mockRepository);
    }

    @Test
    void testAuthenticateSuccess() throws EntityNotFoundException, AlreadyAuthenticatedException {
        when(mockRepository.findByLogin("testuser")).thenReturn(user);
        boolean result = usersService.
                authenticate("testuser", "password");
        verify(mockRepository, times(1)).update(user);
        assertTrue(result);
    }

    @Test
    void testAuthenticateIncorrectLogin() throws EntityNotFoundException, AlreadyAuthenticatedException {
        when(mockRepository.findByLogin("nonexistentuser")).thenThrow(EntityNotFoundException.class);
        boolean  result = usersService.authenticate("nonexistentuser", "password");
        verify(mockRepository, never()).update(any(User.class));
        assertFalse(result);
    }

    @Test
    void testAuthenticateIncorrectPassword() throws EntityNotFoundException, AlreadyAuthenticatedException {
        User tmp = new User(1L, "testuser2", "wrongpassword", false);
        when(mockRepository.findByLogin("testuser2")).thenReturn(tmp);
        boolean result = usersService.authenticate("testuser2", "password");
        verify(mockRepository, never()).update(any(User.class));
        assertFalse(result);
    }

    @Test
    void testAlreadyAuthenticatedException() throws EntityNotFoundException {
        when(mockRepository.findByLogin("testuser")).thenReturn(user);
        user.setAuthenticationStatus(true);
        assertThrows(AlreadyAuthenticatedException.class, () -> {
            boolean result = usersService.authenticate("testuser", "password");
        });
    }

    @Test
    void testUpdateException() throws AlreadyAuthenticatedException, EntityNotFoundException {
        user.setAuthenticationStatus(false);
        when(mockRepository.findByLogin("testuser")).thenReturn(user);
        doThrow(new EntityNotFoundException("error")).when(mockRepository).update(user);
        boolean  result = usersService.authenticate("testuser", "password");
        assertFalse(result);
    }

    @Test
    void testRepositoryNull() throws AlreadyAuthenticatedException, EntityNotFoundException {
        UsersServiceImpl nullRepositoryService = new UsersServiceImpl(null);
        boolean  result = nullRepositoryService.authenticate("testuser", "password");
        assertFalse(result);
    }

}
