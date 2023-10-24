package edu.ucsb.cs156.spring.backenddemo.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import edu.ucsb.cs156.spring.backenddemo.services.UniversityQueryService;

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
    public void test_getCountryCodes_validResponse() throws Exception {
        String fakeJsonResult = "{ \"fake\" : \"result\" }";
        String name = "Harvard";
        when(mockUniversityQueryService.getJSON(eq(name))).thenReturn(fakeJsonResult);

        String url = String.format("/api/university/get?name=%s", name);

        MvcResult response = mockMvc
                .perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseString = response.getResponse().getContentAsString();
        assertEquals(fakeJsonResult, responseString);
    }
    
    @Test
    public void test_getCountryCodes_invalidName() throws Exception {
        String name = "";
        String expectedMessage = "Invalid university name";

        String url = String.format("/api/university/get?name=%s", name);

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedMessage));
    }

    @Test
    public void test_getCountryCodes_serviceError() throws Exception {
        String name = "Harvard";
        when(mockUniversityQueryService.getJSON(eq(name))).thenThrow(new RuntimeException("Service error"));

        String url = String.format("/api/university/get?name=%s", name);

        mockMvc.perform(get(url).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Service error"));
    }

}
