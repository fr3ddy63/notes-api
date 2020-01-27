package de.home.micronaut.rest.notes.note;

import de.home.micronaut.rest.notes.common.BaseService;

import javax.inject.Singleton;
import java.util.UUID;

@Singleton
public class NoteService extends BaseService<UUID, NoteEntity> {

    public NoteService() {
        super(NoteEntity.class);
    }
}
