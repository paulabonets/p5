package edu.comillas.icai.gitt.pat.spring.p5.repository;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class RepositoryIntegrationTest {
    @Autowired TokenRepository tokenRepository;
    @Autowired AppUserRepository appUserRepository;

    /**
     * TODO#9
     * Completa este test de integración para que verifique
     * que los repositorios TokenRepository y AppUserRepository guardan
     * los datos correctamente, y las consultas por AppToken y por email
     * definidas respectivamente en ellos retornan el token y usuario guardados.
     */
    @Test
    void saveTest() {
        // Given
        AppUser user = new AppUser();
        user.setEmail("test@correo.com");
        user.setPassword("Password123");
        user.setName("Test User");
        user.setRole(Role.USER);
        appUserRepository.save(user);

        Token token = new Token();
        token.setId("tokentest123");
        token.setAppUser(user);
        tokenRepository.save(token);

        // When
        Optional<AppUser> userByEmail = appUserRepository.findByEmail("test@correo.com");
        Optional<Token> tokenByUser = tokenRepository.findByAppUser(user);

        // Then
        assertTrue(userByEmail.isPresent());
        assertEquals("Test User", userByEmail.get().getName());

        assertTrue(tokenByUser.isPresent());
        assertEquals("tokentest123", tokenByUser.get().getId());
    }


    /**
     * TODO#10
     * Completa este test de integración para que verifique que
     * cuando se borra un usuario, automáticamente se borran sus tokens asociados.
     */
    @Test
    void deleteCascadeTest() {
        // Given
        AppUser user = new AppUser();
        user.setEmail("cascade@correo.com");
        user.setPassword("Password123");
        user.setName("Cascada");
        user.setRole(Role.USER);
        appUserRepository.save(user);

        Token token = new Token();
        token.setId("cascadeToken");
        token.setAppUser(user);
        tokenRepository.save(token);

        // Confirmar que ambos están guardados
        Assertions.assertEquals(1, appUserRepository.count());
        Assertions.assertEquals(1, tokenRepository.count());

        // When: se borra el usuario
        appUserRepository.delete(user);

        // Then: también se borra el token
        Assertions.assertEquals(0, appUserRepository.count());
        Assertions.assertEquals(0, tokenRepository.count());
    }
}