package com.triple.homework.controller;

import com.triple.homework.mileage.MileageRepository;
import com.triple.homework.mileage.MileageService;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(MileageController.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MileageControllerTest {

    @Autowired
    public MockMvc mockMvc;

    @Test
    @DisplayName("사용자 마일리지 조회")
    void getMileages() throws Exception{
        // 현재 DB tbl_mileage 에 있는 마일리지 값 입력
        // 2는 첫 리뷰 추가 후, 업로드 사진 제거로 인한 1점 회수로 인해 2점이 된 상태
        Long result = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/mileages?id=3ede0ef2-92b7-4817-a5f3-0c575361f745")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(Long.toString(result)))
                .andDo(print());

    }
}