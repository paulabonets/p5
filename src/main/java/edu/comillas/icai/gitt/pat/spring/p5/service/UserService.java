package edu.comillas.icai.gitt.pat.spring.p5.service;

import edu.comillas.icai.gitt.pat.spring.p5.entity.AppUser;
import edu.comillas.icai.gitt.pat.spring.p5.entity.Token;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.ProfileResponse;
import edu.comillas.icai.gitt.pat.spring.p5.model.RegisterRequest;
import edu.comillas.icai.gitt.pat.spring.p5.model.Role;
import edu.comillas.icai.gitt.pat.spring.p5.repository.TokenRepository;
import edu.comillas.icai.gitt.pat.spring.p5.repository.AppUserRepository;
import edu.comillas.icai.gitt.pat.spring.p5.util.Hashing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * TODO#6
 * Completa los métodos del servicio para que cumplan con el contrato
 * especificado en el interface UserServiceInterface, utilizando
 * los repositorios y entidades creados anteriormente
 */

@Service
public class UserService implements UserServiceInterface {
    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private Hashing hashing;


    @Override
    public Token login(String email, String password) {
        Optional<AppUser> userOpt = appUserRepository.findByEmail(email);
        if (userOpt.isEmpty()) return null;

        AppUser user = userOpt.get();

        if (!hashing.check(password, user.getPassword())) return null;

        Token token = new Token();
        token.setId(UUID.randomUUID().toString());
        token.setAppUser(user);
        tokenRepository.save(token);

        return token;
    }


    @Override
    public AppUser authentication(String tokenId) {
        return tokenRepository.findById(tokenId)
                .map(Token::getAppUser)
                .orElse(null);
    }


    @Override
    public ProfileResponse profile(AppUser appUser) {
        return new ProfileResponse(
                appUser.getEmail(),
                appUser.getName(),
                appUser.getRole()
        );
    }

    @Override
    public ProfileResponse profile(AppUser appUser, ProfileRequest profile) {
        if (StringUtils.hasText(profile.name())) {
            appUser.setName(profile.name());
        }
        if (StringUtils.hasText(profile.password())) {
            appUser.setPassword(profile.password());
        }

        appUserRepository.save(appUser);

        return profile(appUser);
    }

    @Override
    public ProfileResponse profile(RegisterRequest register) {
        AppUser appUser = new AppUser();
        appUser.setEmail(register.email());
        appUser.setPassword(hashing.hash(register.password()));
        appUser.setName(register.name());
        appUser.setRole(Role.USER);

        appUserRepository.save(appUser);

        return profile(appUser);
    }

    @Override
    public void logout(String tokenId) {
        tokenRepository.deleteById(tokenId);
    }

    @Override
    public void delete(AppUser appUser) {
        appUserRepository.delete(appUser);
    }

}
