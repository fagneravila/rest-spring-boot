package br.com.avila.integrationtests.controllers.withjson;


import br.com.avila.config.TestConfigs;
import br.com.avila.integrationtests.dto.AccountCredentialsDTO;
import br.com.avila.integrationtests.dto.PersonDTO;
import br.com.avila.integrationtests.dto.TokenDTO;
import br.com.avila.integrationtests.dto.wrappers.WrapperPersonDTO;
import br.com.avila.integrationtests.testcontainers.AbstractIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonDTO person;
    private static TokenDTO tokenDto;

    @BeforeAll
    static void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();
        tokenDto = new TokenDTO();
    }

    @Test
    @Order(0)
    void signin() {
        AccountCredentialsDTO credentials =
                new AccountCredentialsDTO("leandro", "admin123");

        tokenDto = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDTO.class);


        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCAL)
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + tokenDto.getAccessToken())
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @Order(1)
    void createTest() throws JsonProcessingException {
        mockPerson();

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(2)
    void updateTest() throws JsonProcessingException {
        person.setLastName("Benedict Torvalds");

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
                .when()
                .put()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());

    }

    @Test
    @Order(3)
    void findByIdTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(4)
    void disableTest() throws JsonProcessingException {

        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Linus", createdPerson.getFirstName());
        assertEquals("Benedict Torvalds", createdPerson.getLastName());
        assertEquals("Helsinki - Finland", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertFalse(createdPerson.getEnabled());
    }

    @Test
    @Order(5)
    void deleteTest() throws JsonProcessingException {

        given(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }


    @Test
    @Order(6)
    void findAllTest() throws JsonProcessingException {

        var content = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO personOne = people.get(0);

        assertNotNull(personOne.getId());
        assertTrue(personOne.getId() > 0);

        assertEquals("Aluino", personOne.getFirstName());
        assertEquals("Mongan", personOne.getLastName());
        assertEquals("Apt 1137", personOne.getAddress());
        assertEquals("Male", personOne.getGender());
        assertTrue(personOne.getEnabled());

        PersonDTO personFour = people.get(4);

        assertNotNull(personFour.getId());
        assertTrue(personFour.getId() > 0);

        assertEquals("Amaleta", personFour.getFirstName());
        assertEquals("Thripp", personFour.getLastName());
        assertEquals("15th Floor", personFour.getAddress());
        assertEquals("Female", personFour.getGender());
        assertTrue(personFour.getEnabled());
    }

    @Test
    @Order(7)
    void findByNameTest() throws JsonProcessingException {

        // {{baseUrl}}/api/person/v1/findPeopleByName/and?page=0&size=12&direction=asc

            var content = given(specification)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("firstName", "and") // nome que não existe
                    .queryParams("page", 0, "size", 12, "direction", "asc")
                    .when()
                    .get("findPeopleByName/{firstName}")
                    .then()
                    .statusCode(404)
                    .contentType("application/problem+json")
                    .extract()
                    .body()
                    .asString();

            JsonNode error = objectMapper.readTree(content);

            assertEquals("Not Found", error.get("title").asText());
            assertEquals(404, error.get("status").asInt());
            assertTrue(error.get("detail").asText().contains("No static resource"));
        }


    @Test
    @Order(8)
    void hateoasAndHalTest() throws JsonProcessingException {
        Response response = given(specification)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .queryParams("page", 3, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .extract()
                .response();

        List<Map<String, Object>> people = response.jsonPath().getList("_embedded.people");
        Map<String, Object> pageLinks = response.jsonPath().getMap("_links");
        Map<String, Object> pageAttributes = response.jsonPath().getMap("page");

        // Verificação dos links da página
        assertThat("Page link 'self' is missing", pageLinks, hasKey("self"));
        assertThat("Page link 'first' is missing", pageLinks, hasKey("first"));
        assertThat("Page link 'prev' is missing", pageLinks, hasKey("prev"));
        assertThat("Page link 'next' is missing", pageLinks, hasKey("next"));
        assertThat("Page link 'last' is missing", pageLinks, hasKey("last"));

        // Verificação dos atributos da página
        assertThat("Page size mismatch", pageAttributes.get("size"), is(12));
        assertThat("Page number mismatch", pageAttributes.get("number"), is(3));
        assertTrue("totalElements should be greater than 0", ((Integer) pageAttributes.get("totalElements")) > 0);
        assertTrue("totalPages should be greater than 0", ((Integer) pageAttributes.get("totalPages")) > 0);

        // Verificação dos links HATEOAS de cada pessoa
        for (Map<String, Object> person : people) {
            Map<String, Object> links = (Map<String, Object>) person.get("_links");

            // Links esperados em cada pessoa
            List<String> expectedLinks = Arrays.asList(
                    "self", "findAll", "findPersonByName",
                    "create", "update", "delete",
                    "disable", "massCreation", "exportPage"
            );

            for (String link : expectedLinks) {
                assertThat("HATEOAS/HAL link '" + link + "' is missing", links, hasKey(link));
            }

            // Verificação do formato e existência do href e do tipo HTTP
            links.forEach((key, value) -> {
                Map<String, String> linkData = (Map<String, String>) value;

                assertThat("HATEOAS/HAL link '" + key + "' has an invalid URL",
                        linkData.get("href"), matchesPattern("https?://.+/api/person/v1.*"));

                assertThat("HATEOAS/HAL link '" + key + "' has an invalid HTTP method",
                        linkData.get("type"), notNullValue());
            });
        }
    }




    private void mockPerson() {
        person.setFirstName("Linus");
        person.setLastName("Torvalds");
        person.setAddress("Helsinki - Finland");
        person.setGender("Male");
        person.setEnabled(true);
        person.setProfileUrl("https://pub.erudio.com.br/meus-cursos");
        person.setPhotoUrl("https://pub.erudio.com.br/meus-cursos");
    }
}