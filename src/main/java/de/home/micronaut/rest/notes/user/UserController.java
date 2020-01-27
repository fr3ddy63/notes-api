package de.home.micronaut.rest.notes.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.home.micronaut.rest.notes.common.JsonMapper;
import de.home.micronaut.rest.notes.common.PasswordHash;
import de.home.micronaut.rest.notes.exception.NotFoundException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collections;

@Controller("/users")
@Validated
public class UserController {

    private final UserService userService;
    private final JsonMapper jsonMapper;
    private final PasswordHash passwordHash;

    public UserController(UserService userService, JsonMapper jsonMapper, PasswordHash passwordHash) {
        this.userService = userService;
        this.jsonMapper = jsonMapper;
        this.passwordHash = passwordHash;
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get("/{name}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    HttpResponse<String> getUser(String name) throws JsonProcessingException {
        return HttpResponse.ok(
                this.jsonMapper.getObjectMapper()
                        .writerWithView(UserViews.Public.class)
                        .writeValueAsString(
                                this.userService
                                        .find(name)
                                        .orElseThrow(NotFoundException::new)));
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get()
    @Secured(SecurityRule.IS_ANONYMOUS)
    HttpResponse<String> getUsers() throws JsonProcessingException {
        return HttpResponse.ok(
                this.jsonMapper.getObjectMapper()
                        .writerWithView(UserViews.Public.class)
                        .writeValueAsString(this.userService.find()));
    }

    @Post()
    @Secured(SecurityRule.IS_ANONYMOUS)
    HttpResponse<String> post(@Valid @Body UserEntity user)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        user.setPassword(this.passwordHash.generate(user.getPassword()));
        this.userService.persist(user);
        // todo: check exceptions from persisting
        return HttpResponse.created(UriBuilder
                .of("/users/{name}")
                .expand(Collections.singletonMap("name", user.getName())));
    }

    @Delete("/{name}")
    @Secured({"ROLE_ADMIN"})
    HttpResponse<String> delete(String name) {
        this.userService.remove(this.userService.find(name).orElseThrow(NotFoundException::new));
        return HttpResponse.noContent();
    }
}
