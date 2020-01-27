package de.home.micronaut.rest.notes.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import javax.inject.Singleton;

@Produces
@Singleton
@Requires(classes = {NotFoundException.class, ExceptionHandler.class})
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<String>> {

    @Override
    public HttpResponse<String> handle(HttpRequest request, NotFoundException exception) {
        return HttpResponse.notFound();
    }
}
