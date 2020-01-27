package de.home.micronaut.rest.notes.common;

import de.home.micronaut.rest.notes.exception.NotFoundException;
import de.home.micronaut.rest.notes.user.UserEntity;
import de.home.micronaut.rest.notes.user.UserService;
import io.micronaut.security.authentication.*;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import javax.inject.Singleton;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

@Singleton
public class AuthenticationProviderUserPassword implements AuthenticationProvider {

    private final UserService userService;
    private final PasswordHash passwordHash;

    public AuthenticationProviderUserPassword(UserService userService, PasswordHash passwordHash) {
        this.userService = userService;
        this.passwordHash = passwordHash;
    }

    @Override
    public Publisher<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
        UserEntity user = this.userService
                .find(authenticationRequest.getIdentity().toString())
                .orElseThrow(NotFoundException::new);

        try {
            if (this.passwordHash.validate(authenticationRequest.getSecret().toString(), user.getPassword())) {
                return Flowable.just(new UserDetails(
                        user.getName(),
                        user.getRoles()));
            } else {
                return Flowable.just(new AuthenticationFailed());
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            return Flowable.just(new AuthenticationFailed());
        }
    }
}
