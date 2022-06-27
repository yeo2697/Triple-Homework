package com.triple.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ReviewControllerTest {
    @Autowired
    public MockMvc mockMvc;

    @Test
    @DisplayName("ADD CREATED -> ADD CONFLICT")
    void addReview() throws Exception{
        // 실제 DB review 저장
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
        jsonArray.put("afb0cef2-851d-4a50-bb07-9cc15cbdc332");

        String jsonString = new JSONObject()
                .put("type", "REVIEW")
                .put("action", "ADD")
                .put("reviewId", "240a0658-dc5f-4878-9381-ebb7b2667772")
                .put("content", "좋아요!")
                .put("attachedPhotoIds", jsonArray)
                .put("userId", "3ede0ef2-92b7-4817-a5f3-0c575361f745")
                .put("placeId", "2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .toString();

        // 생성 성공
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("CREATED"));

        // 연이은 요청으로 인하여 중복발생
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().string("CONFLICT"));
    }

    @Test
    @DisplayName("MOD CREATED")
    void modReview() throws Exception{
        // 실제 DB review 업데이트
        // jsonArray 주석 해제로 내용 변경 가능
        JSONArray jsonArray = new JSONArray();
//        jsonArray.put("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
//        jsonArray.put("afb0cef2-851d-4a50-bb07-9cc15cbdc332");

        String jsonString = new JSONObject()
                .put("type", "REVIEW")
                .put("action", "MOD")
                .put("reviewId", "240a0658-dc5f-4878-9381-ebb7b2667772")
                .put("content", "좋아요!")
                .put("attachedPhotoIds", jsonArray)
                .put("userId", "3ede0ef2-92b7-4817-a5f3-0c575361f745")
                .put("placeId", "2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("CREATED"));
    }

    @Test
    @DisplayName("DELETE CREATED -> DELETE NOT FOUND")
    void deleteReview() throws Exception{
        // 실제 DB review 삭제
        // review_id로 review 및 mileage 조회
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
        jsonArray.put("afb0cef2-851d-4a50-bb07-9cc15cbdc332");

        String jsonString = new JSONObject()
            .put("type", "REVIEW")
            .put("action", "DEL")
            .put("reviewId", "240a0658-dc5f-4878-9381-ebb7b2667772")
            .put("content", "좋아요!")
            .put("attachedPhotoIds", jsonArray)
            .put("userId", "3ede0ef2-92b7-4817-a5f3-0c575361f745")
            .put("placeId", "2e4baf1c-5acb-4efb-a1af-eddada31b00f")
                .toString();

        // 삭제 성공
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("CREATED"));

        // 이미 삭제된 리뷰 삭제 요청시 NOT FOUND
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .content(jsonString)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("NOT_FOUND"));
    }
}