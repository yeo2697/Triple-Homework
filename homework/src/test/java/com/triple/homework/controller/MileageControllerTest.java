package com.triple.homework.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        
        // A 사용자
        String user = "3ede0ef2-92b7-4817-a5f3-0c575361f745";
        // B 사용자
//        String user = "8eof0ef2-26p9-4817-a5f3-1t575361d236";
        
        Long result = 3L;

        mockMvc.perform(MockMvcRequestBuilders.get("/mileages?id=" + user)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.ALL))
                .andExpect(status().isOk())
                .andExpect(content().string(Long.toString(result)))
                .andDo(print());

    }
}