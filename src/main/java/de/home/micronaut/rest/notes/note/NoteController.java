package de.home.micronaut.rest.notes.note;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.home.micronaut.rest.notes.common.JsonMapper;
import de.home.micronaut.rest.notes.exception.NotFoundException;
import de.home.micronaut.rest.notes.user.UserEntity;
import de.home.micronaut.rest.notes.user.UserService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.validation.Validated;

import javax.annotation.Nullable;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.UUID;

@Controller("/notes")
@Validated
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    private final JsonMapper jsonMapper;

    public NoteController(NoteService noteService, UserService userService, JsonMapper jsonMapper) {
        this.noteService = noteService;
        this.userService = userService;
        this.jsonMapper = jsonMapper;
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get("/id")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<String> getNote(UUID id) throws JsonProcessingException {
        return HttpResponse.ok(
                this.jsonMapper.getObjectMapper()
                        .writerWithView(NoteViews.Public.class)
                        .writeValueAsString(
                                this.noteService
                                        .find(id)
                                        .orElseThrow(NotFoundException::new)));
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Get()
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<String> getNotes() throws JsonProcessingException {
        return HttpResponse.ok(
                this.jsonMapper.getObjectMapper()
                        .writerWithView(NoteViews.Public.class)
                        .writeValueAsString(this.noteService.find()));
    }

    @Post()
    @Secured(SecurityRule.IS_AUTHENTICATED)
    HttpResponse<String> post(@Valid @Body NoteEntity note, @Nullable Principal principal) {
        UserEntity user = this.userService
                .find(principal.getName())
                .orElseThrow(NotFoundException::new);

        note.setAuthor(user);

        this.noteService.persist(note);

        return HttpResponse.created(UriBuilder
                .of("/notes/{id}")
                .expand(Collections.singletonMap("id", note.getId())));
    }

    @Delete("/id")
    @Secured({"ROLE_ADMIN"})
    HttpResponse<String> delete(UUID id) {
        this.noteService.remove(this.noteService.find(id).orElseThrow(NotFoundException::new));
        return HttpResponse.noContent();
    }
}
