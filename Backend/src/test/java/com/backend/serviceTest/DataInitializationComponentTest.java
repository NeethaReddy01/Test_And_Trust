package com.backend.serviceTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.modal.User;
import com.backend.repository.UserRepository;
import com.backend.service.CartService;
import com.backend.service.DataInitializationComponent;
import com.backend.user.domain.UserRole;

class DataInitializationComponentTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartService cartService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializationComponent dataInitializationComponent;

    private User adminUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setEmail("admin@gmail.com");
        adminUser.setFirstName("admin");
        adminUser.setLastName("n");
        adminUser.setPassword("encodedPassword");
        adminUser.setRole(UserRole.ROLE_ADMIN.toString());
    }

    @Test
    void testInitializeAdminUser_WhenUserNotExists() {
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(null);
        when(passwordEncoder.encode("admin")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(adminUser);

        dataInitializationComponent.run();

        verify(userRepository).findByEmail("admin@gmail.com");
        verify(passwordEncoder).encode("admin");
        verify(userRepository).save(any(User.class));
        verify(cartService).createCart(adminUser);
    }

    @Test
    void testInitializeAdminUser_WhenUserAlreadyExists() {
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(adminUser);

        dataInitializationComponent.run();

        verify(userRepository).findByEmail("admin@gmail.com");
        verifyNoMoreInteractions(passwordEncoder, userRepository, cartService);
    }
}
