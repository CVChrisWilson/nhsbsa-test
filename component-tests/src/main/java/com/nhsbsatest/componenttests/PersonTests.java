package com.nhsbsatest.componenttests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhsbsatest.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonTests {

    @Test
    public void givenPersonDoesNotExists_whenPersonInfoIsRetrieved_then404IsReceived()
            throws IOException {

        // Given

        HttpUriRequest request = new HttpGet( "http://localhost:8080/person/" + PlatformIdentifier.create());

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void givenPersonIsCreated_whenPersonInfoIsRetrieved_then200AndPersonIsReceived()
            throws IOException {

        // Given
        Person existingPerson = saveNewPerson();
        HttpUriRequest request = new HttpGet( "http://localhost:8080/person/" + existingPerson.getPlatformPersonIdentifier());

        // When
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

        Person foundPerson = toObj(EntityUtils.toString(httpResponse.getEntity()), Person.class);

        assertEquals(existingPerson.getEmail(), foundPerson.getEmail());
        assertEquals(existingPerson.getFirstName(), foundPerson.getFirstName());
        assertEquals(existingPerson.getLastName(), foundPerson.getLastName());
        assertEquals(existingPerson.getPlatformPersonIdentifier(), foundPerson.getPlatformPersonIdentifier());
        assertTrue(foundPerson.getSkillList().containsAll(existingPerson.getSkillList()));
    }

    @Test
    public void givenPersonIsCreated_whenPersonInfoIsUpdated_then200AndPersonIsUpdated()
            throws IOException {

        // Given
        Person existingPerson = saveNewPerson();
        existingPerson.setFirstName("Bob");
        existingPerson.setLastName("Watters");
        existingPerson.setEmail("updated@mail.net");
        existingPerson.setSkillList(createSkills(1));

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut request = new HttpPut( "http://localhost:8080/person/");

        StringEntity entity = new StringEntity(toJsonString(existingPerson));
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        HttpUriRequest getUpdatedPersonRequest = new HttpGet( "http://localhost:8080/person/" + existingPerson.getPlatformPersonIdentifier());

        // When
        CloseableHttpResponse response = client.execute(request);
        Person updatedPerson = toObj(EntityUtils.toString(response.getEntity()), Person.class);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getUpdatedPersonRequest);

        // Then
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_OK));

        Person foundPerson = toObj(EntityUtils.toString(httpResponse.getEntity()), Person.class);

        assertEquals(updatedPerson.getEmail(), foundPerson.getEmail());
        assertEquals(updatedPerson.getFirstName(), foundPerson.getFirstName());
        assertEquals(updatedPerson.getLastName(), foundPerson.getLastName());
        assertEquals(updatedPerson.getPlatformPersonIdentifier(), foundPerson.getPlatformPersonIdentifier());
        assertTrue(foundPerson.getSkillList().containsAll(updatedPerson.getSkillList()));
    }

    @Test
    public void givenPersonIsCreated_whenPersonInfoIsDeleted_then404IsReceived()
            throws IOException {

        // Given
        Person existingPerson = saveNewPerson();
        HttpUriRequest deleteRequest = new HttpDelete( "http://localhost:8080/person/" + existingPerson.getPlatformPersonIdentifier());
        HttpUriRequest getRequest = new HttpGet( "http://localhost:8080/person/" + existingPerson.getPlatformPersonIdentifier());

        // When
        HttpClientBuilder.create().build().execute(deleteRequest);

        // Then
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(getRequest);
        assertThat(
                httpResponse.getStatusLine().getStatusCode(),
                equalTo(HttpStatus.SC_NOT_FOUND));
    }

    private Person saveNewPerson() throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost( "http://localhost:8080/person/");
        StringEntity entity = new StringEntity(toJsonString(createNewPerson()));
        request.setEntity(entity);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        CloseableHttpResponse response = client.execute(request);

        return toObj(EntityUtils.toString(response.getEntity()), Person.class);
    }

    private <T> T toObj(String jsonBody, Class<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.readValue(jsonBody, type);
    }

    private String toJsonString(Object o) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(o);
    }

    private NewPerson createNewPerson() {
        NewPerson newPerson = new NewPerson();
        newPerson.setEmail(RandomStringUtils.randomAlphanumeric(12) + "@" + RandomStringUtils.randomAlphabetic(4) + ".com");
        newPerson.setFirstName(RandomStringUtils.randomAlphabetic(16));
        newPerson.setLastName(RandomStringUtils.randomAlphabetic(16));
        newPerson.setSkillList(createSkills(5));

        return newPerson;
    }

    private List<Skill> createSkills(int count) {
        List<Skill> skillList = new ArrayList<>();

        Stream.generate(() -> {
            Skill skill = new Skill();
            skill.setSkillLevel(SkillLevel.PRACTITIONER);
            skill.setSkillType(RandomStringUtils.randomAlphanumeric(12));
            return skill;
        }).limit(count)
                .forEach(skillList::add);

        return skillList;
    }
}
