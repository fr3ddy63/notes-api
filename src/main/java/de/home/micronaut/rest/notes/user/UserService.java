package de.home.micronaut.rest.notes.user;

import de.home.micronaut.rest.notes.common.BaseService;

import javax.inject.Singleton;

@Singleton
public class UserService extends BaseService<String, UserEntity> {

    public UserService() {
        super(UserEntity.class);
    }
}
