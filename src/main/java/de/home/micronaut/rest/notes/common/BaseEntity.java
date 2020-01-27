package de.home.micronaut.rest.notes.common;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseEntity<ID extends Serializable> {

    public BaseEntity() {
    }

    public abstract ID getId();

    public abstract void setId(ID id);
}
