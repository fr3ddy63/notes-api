package de.home.micronaut.rest.notes.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.home.micronaut.rest.notes.common.JsonMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
public class UserControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Inject
    JsonMapper jsonMapper;

    @Test
    public void testGetUserNotFound() {
        HttpRequest<String> request = HttpRequest.GET("/users/Unknown");

        try {
            HttpResponse<String> response = client.toBlocking().exchange(request, String.class);
        } catch (HttpClientResponseException exception) {
            assertEquals(exception.getResponse().getBody(), Optional.empty());
            assertEquals(exception.getStatus(), HttpStatus.NOT_FOUND);
        }
    }

    @Test
    public void testGetUserAdmin() throws IOException {
        HttpRequest<String> request = HttpRequest.GET("/users/Admin");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(response.getStatus(), HttpStatus.OK);
        assertEquals(
                this.jsonMapper.getObjectMapper()
                        .writerWithView(UserViews.Public.class)
                        .writeValueAsString(new UserEntity("Admin", "dummy")),
                response.body()
        );
    }

    @Test
    public void testPostUser() throws IOException {
        HttpRequest<String> request = HttpRequest.POST("/users", "{\"name\": \"Test\", \"password\": \"TestTest\"}");
        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);

        assertEquals(response.getBody(), Optional.empty());
        assertEquals(response.getStatus(), HttpStatus.CREATED);
        assertEquals(response.header("location"), "/users/Test");

        HttpRequest<String> request2 = HttpRequest.POST("/login", "{\"username\": \"Test\", \"password\": \"TestTest\"}");
        HttpResponse<String> response2 = client.toBlocking().exchange(request2, String.class);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = mapper.readValue(response2.body(), new TypeReference<Map<String, Object>>() {});

        assertTrue(jsonMap.containsKey("access_token"));
        assertEquals(jsonMap.get("token_type"), "Bearer");

        // body: {"username":"Test","access_token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0IiwibmJmIjoxNTgwMDgxOTMyLCJyb2xlcyI6W10sImlzcyI6Im5vdGVzLWFwaSIsImV4cCI6MTU4MDA4NTUzMiwiaWF0IjoxNTgwMDgxOTMyfQ.NWslEk7bQbblzoksJAbVMjk88GlIJC7sPvrGGiKabIw","refresh_token":"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJUZXN0IiwibmJmIjoxNTgwMDgxOTMyLCJyb2xlcyI6W10sImlzcyI6Im5vdGVzLWFwaSIsImlhdCI6MTU4MDA4MTkzMn0.w514eNUB_wiH5mVzuACagCOAT0dTfBWVoLrk9WFMAnA","token_type":"Bearer","expires_in":3600}
    }

//    @Test
//    public void testLogin() {
//        HttpRequest<String> request = HttpRequest.POST("/login", "{\"username\": \"Test\", \"password\": \"TestTest\"}");
//        HttpResponse<String> response = client.toBlocking().exchange(request, String.class);
//
//        int x = 0;
//    }
}
