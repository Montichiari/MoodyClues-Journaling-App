package com.moodyclues;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.moodyclues.dto.RegisterRequestDto;
import com.moodyclues.model.JournalUser;
import com.moodyclues.repository.JournalUserRepository;
import com.moodyclues.serviceimpl.JournalUserServiceImpl;

import jakarta.persistence.EntityNotFoundException;

class JournalUserServiceImplTest {

    @InjectMocks
    private JournalUserServiceImpl journalUserService;

    @Mock
    private JournalUserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // === findJournalUserByEmail ===
    @Test
    void testFindJournalUserByEmail_success() {
        String email = "test@example.com";
        JournalUser mockUser = new JournalUser();
        mockUser.setEmail(email);

        when(userRepo.findJournalUserByEmail(email)).thenReturn(Optional.of(mockUser));

        JournalUser result = journalUserService.findJournalUserByEmail(email);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindJournalUserByEmail_notFound() {
        when(userRepo.findJournalUserByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
            journalUserService.findJournalUserByEmail("missing@example.com"));
    }

    // === loginAttempt ===
    @Test
    void testLoginAttempt_success() {
        String email = "user@example.com";
        String rawPassword = "Password123!";
        String hashedPassword = "hashed";

        JournalUser user = new JournalUser();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userRepo.findJournalUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(true);

        boolean result = journalUserService.loginAttempt(email, rawPassword);
        assertTrue(result);
    }

    @Test
    void testLoginAttempt_wrongPassword() {
        String email = "user@example.com";
        String rawPassword = "wrong";
        String hashedPassword = "hashed";

        JournalUser user = new JournalUser();
        user.setEmail(email);
        user.setPassword(hashedPassword);

        when(userRepo.findJournalUserByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(rawPassword, hashedPassword)).thenReturn(false);

        boolean result = journalUserService.loginAttempt(email, rawPassword);
        assertFalse(result);
    }

    // === registerUser ===
    @Test
    void testRegisterUser_success() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("new@example.com");
        dto.setPassword("Password123!");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        when(userRepo.findJournalUserByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");

        journalUserService.registerUser(dto);

        ArgumentCaptor<JournalUser> userCaptor = ArgumentCaptor.forClass(JournalUser.class);
        verify(userRepo).save(userCaptor.capture());

        JournalUser savedUser = userCaptor.getValue();
        assertEquals("new@example.com", savedUser.getEmail());
        assertEquals("John", savedUser.getFirstName());
        assertEquals("Doe", savedUser.getLastName());
        assertEquals("hashedPassword", savedUser.getPassword());
    }

    @Test
    void testRegisterUser_duplicateEmail() {
        RegisterRequestDto dto = new RegisterRequestDto();
        dto.setEmail("existing@example.com");

        when(userRepo.findJournalUserByEmail(dto.getEmail())).thenReturn(Optional.of(new JournalUser()));

        assertThrows(ResponseStatusException.class, () -> journalUserService.registerUser(dto));
    }

    // === deleteUser (by email) ===
    @Test
    void testDeleteUserByEmail() {
        String email = "user@example.com";
        JournalUser user = new JournalUser();
        user.setArchived(false);

        when(userRepo.findJournalUserByEmail(email)).thenReturn(Optional.of(user));

        journalUserService.deleteUser(email, "somePassword");
        assertTrue(user.isArchived());
    }

    // === deleteUser (by id) ===
    @Test
    void testDeleteUserById() {
        String id = "some-id";
        JournalUser user = new JournalUser();
        user.setArchived(false);

        when(userRepo.findJournalUserById(id)).thenReturn(Optional.of(user));

        journalUserService.deleteUser(id);
        assertTrue(user.isArchived());
    }
    
    @Test
    void testFindJournalUserById_success() {
        String id = "user-123";
        JournalUser mockUser = new JournalUser();
        mockUser.setId(id);

        when(userRepo.findJournalUserById(id)).thenReturn(Optional.of(mockUser));

        JournalUser result = journalUserService.findJournalUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testFindJournalUserById_notFound() {
        String id = "nonexistent-id";

        when(userRepo.findJournalUserById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            journalUserService.findJournalUserById(id);
        });
    }
    
}