package edu.ucsb.cs156.spring.backenddemo.controllers;

import edu.ucsb.cs156.spring.backenddemo.services.UniversityQueryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "University info from universities.hipolabs.com")
@Slf4j
@RequestMapping("/api/university")
public class UniversityController {

    @Autowired
    private UniversityQueryService universityQueryService;

    @Operation(summary = "Get list of universities that match a given name", description = "Uses API documented here: http://universities.hipolabs.com/search")
    @GetMapping("/get")
    public ResponseEntity<String> getUniversity(
            @Parameter(name = "name", description = "name to search", example = "Harvard") @RequestParam String name) {
        log.info("getUniversity: name={}", name);
        try {
            String result = universityQueryService.getJSON(name);
            return ResponseEntity.ok().body(result);
        } catch (HttpClientErrorException e) {
            log.error("Error occurred while fetching data for university: {}", name, e);
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("Unhandled error occurred", e);
            return ResponseEntity.badRequest().body("An error occurred while processing the request.");
        }
    }
}
