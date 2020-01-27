package de.home.micronaut.rest.notes.note;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.home.micronaut.rest.notes.common.BaseEntity;
import de.home.micronaut.rest.notes.user.UserEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "note")
public class NoteEntity extends BaseEntity<UUID> {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(optional = false)
    private UserEntity author;

    public NoteEntity() {
        super();
    }

    @JsonCreator
    public NoteEntity(@JsonProperty(value = "content", required = true) String content) {
        this();
        this.content = content;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public void setId(UUID uuid) {
        this.id = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public UserEntity getAuthor() {
        return author;
    }

    public void setAuthor(UserEntity author) {
        this.author = author;
    }
}
