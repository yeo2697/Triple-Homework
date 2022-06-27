package com.triple.homework.controller;

import com.triple.homework.mileage.MileageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

@Slf4j
@RestController
public class MileageController {

    private final MileageService mileageService;

    @Autowired
    public MileageController(MileageService mileageService) {
        this.mileageService = mileageService;
    }

    // User Mileage 조회
    @GetMapping("/mileages")
    public ResponseEntity getUserMileage(@RequestParam(value = "id") String id){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));

        try{
            return new ResponseEntity(mileageService.getTotalMileages(id), headers, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity("INTERNAL_SERVER_ERROR", headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
