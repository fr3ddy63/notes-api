package de.home.micronaut.rest.notes.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.JsonExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Replaces(JsonExceptionHandler.class)
@Requires(classes = {JsonProcessingException.class, ExceptionHandler.class})
public class JsonProcessingExceptionHandler implements ExceptionHandler<JsonProcessingException, HttpResponse<String>> {

    @Override
    public HttpResponse<String> handle(HttpRequest request, JsonProcessingException exception) {
        return HttpResponse.serverError();
    }
}
