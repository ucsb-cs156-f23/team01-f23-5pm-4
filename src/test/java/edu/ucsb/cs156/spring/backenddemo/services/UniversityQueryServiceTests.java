package edu.ucsb.cs156.spring.backenddemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import edu.ucsb.cs156.spring.backenddemo.beans.CollegeSubreddit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestClientTest(UniversityQueryService.class)
public class UniversityQueryServiceTests {

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private UniversityQueryService universityQueryService;

    @Test
    public void test_getLocations() throws URISyntaxException, UnsupportedEncodingException, JsonProcessingException {

        String nameQuery = "UC";
        String expectedURL = UniversityQueryService.ENDPOINT.replace("{name}", URLEncoder.encode(nameQuery, StandardCharsets.UTF_8));

        List<CollegeSubreddit> subreddits = new ArrayList<>();
        CollegeSubreddit ucsb = new CollegeSubreddit();
        ucsb.setName("University of California - Santa Barbara");
        ucsb.setLocation("Santa Barbara, California, United States");
        ucsb.setSubreddit("UCSantaBarbara");
        CollegeSubreddit ucsc = new CollegeSubreddit();
        ucsc.setName("University of California - Santa Cruz");
        ucsc.setLocation("Santa Cruz, California, United States");
        ucsc.setSubreddit("UCSC");
        subreddits.add(ucsb);
        subreddits.add(ucsc);

        ObjectMapper mapper = new ObjectMapper();
        String expectedJSON = mapper.writeValueAsString(subreddits);

        this.mockRestServiceServer.expect(requestTo(expectedURL))
                .andExpect(header("Accept", MediaType.APPLICATION_JSON.toString()))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON.toString()))
                .andRespond(withSuccess(expectedJSON, MediaType.APPLICATION_JSON));

        String actualResult = universityQueryService.getJSON(nameQuery);
        assertEquals(expectedJSON, actualResult);
    }
}

