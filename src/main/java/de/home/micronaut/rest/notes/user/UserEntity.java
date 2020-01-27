package de.home.micronaut.rest.notes.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import de.home.micronaut.rest.notes.common.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;
import java.util.Set;

@Entity
@Table(name = "USER")
public class UserEntity extends BaseEntity<String> {

    @Id
    @Pattern(regexp = "^[A-Za-z0-9]{2,32}$")
    @JsonView(UserViews.Public.class)
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "password", nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String.class)
    @JsonView(UserViews.Public.class)
    @Column(name = "roles", nullable = false)
    private Set<String> roles;

    @JsonView(UserViews.Public.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public UserEntity() {
        super();
    }

    @JsonCreator
    public UserEntity(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "password", required = true) String password) {
        this();
        this.name = name;
        this.password = password;
        this.roles = Set.of("ROLE_USER");
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = Instant.now();
    }

    @Override
    public String getId() {
        return this.getName();
    }

    @Override
    public void setId(String id) {
        this.setName(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
