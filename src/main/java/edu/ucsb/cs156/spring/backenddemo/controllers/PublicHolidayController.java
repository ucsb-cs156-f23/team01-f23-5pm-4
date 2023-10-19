package edu.ucsb.cs156.spring.backenddemo.controllers;

import org.springframework.web.bind.annotation.RestController;

import edu.ucsb.cs156.spring.backenddemo.services.PublicHolidayQueryService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;


@Tag(name="public-holiday-controller")
@Slf4j
@RestController
@RequestMapping("/api/publicholiday")
public class PublicHolidayController {
    
  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  PublicHolidayQueryService publicHolidayQueryService;

  @Operation(summary = "Get public holidays from year and country code", description = "JSON return format documented here: https://date.nager.at/api/v2/publicholidays/2023/us")
  @GetMapping("/get")
  public ResponseEntity<String> getPublicHoliday(
      @Parameter(name="year", description="year, e.g. 2012", example="2003") @RequestParam String year,
      @Parameter(name="countryCode", description="2 letter country code, e.g. US, MX, CN", example="US") @RequestParam String countryCode
  ) throws JsonProcessingException {
      log.info("getPublicHoliday: year={} countryCode={}", year, countryCode);
      String result = publicHolidayQueryService.getJSON(year, countryCode);
      return ResponseEntity.ok().body(result);
  }
}
