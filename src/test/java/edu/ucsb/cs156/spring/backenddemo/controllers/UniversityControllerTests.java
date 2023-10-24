package edu.ucsb.cs156.spring.backenddemo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;

import edu.ucsb.cs156.spring.backenddemo.services.UniversityQueryService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UniversityController.class)
public class UniversityControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UniversityQueryService mockUniversityQueryService;

    @Test
    public void test_getCountryCodes() throws Exception {

        String fakeJsonResult = "{ \"fake\" : \"result\" }";
        String name = "Harvard";
        when(mockUniversityQueryService.getJSON(eq(name))).thenReturn(fakeJsonResult);

        String url = String.format("/api/university/get?name=%s", name);

        MvcResult response = mockMvc
                .perform(get(url).contentType("application/json"))
                .andExpect(status().isOk()).andReturn();

        String responseString = response.getResponse().getContentAsString();

        assertEquals(fakeJsonResult, responseString);
    }

    @Test
public void test_getUniversity_notFound() throws Exception {
    when(mockUniversityQueryService.getJSON(anyString())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

    String url = "/api/university/get?name=UnknownUniversity";
    mockMvc.perform(get(url).contentType("application/json"))
            .andExpect(status().isNotFound());
}

@Test
public void test_getUniversity_missingParam() throws Exception {
    String url = "/api/university/get";
    mockMvc.perform(get(url).contentType("application/json"))
            .andExpect(status().isBadRequest());
}

@Test
public void test_getUniversity_responseContent() throws Exception {
    String fakeJsonResult = "{ \"name\" : \"Harvard\", \"location\" : \"USA\" }";
    when(mockUniversityQueryService.getJSON(eq("Harvard"))).thenReturn(fakeJsonResult);

    String url = "/api/university/get?name=Harvard";
    mockMvc.perform(get(url).contentType("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Harvard"))
            .andExpect(jsonPath("$.location").value("USA"));
}





}