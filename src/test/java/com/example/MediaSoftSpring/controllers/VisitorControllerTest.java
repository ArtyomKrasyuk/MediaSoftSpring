package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.services.VisitorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VisitorController.class)
class VisitorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitorService visitorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllVisitors() throws Exception {
        List<VisitorResponseDTO> visitors = List.of(
                new VisitorResponseDTO(1L, "Иванов Иван", 30, "Мужской"),
                new VisitorResponseDTO(2L, "Петрова Анна", 25, "Женский")
        );

        when(visitorService.findAll()).thenReturn(visitors);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].name", is("Иванов Иван")));
    }

    @Test
    void shouldReturnVisitorById() throws Exception {
        VisitorResponseDTO visitor = new VisitorResponseDTO(1L, "Иванов Иван", 30, "Мужской");

        when(visitorService.findById(1L)).thenReturn(visitor);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Иванов Иван")))
                .andExpect(jsonPath("$.age", is(30)))
                .andExpect(jsonPath("$.sex", is("Мужской")));
    }

    @Test
    void shouldReturnBadRequestOnVisitorById() throws Exception {
        when(visitorService.findById(1L)).thenThrow(NoSuchElementException.class);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldSaveVisitor() throws Exception {
        VisitorRequestDTO request = new VisitorRequestDTO("Иванов Иван", 30, "Мужской");

        doNothing().when(visitorService).save(any(VisitorRequestDTO.class));

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateVisitor() throws Exception {
        VisitorRequestDTO request = new VisitorRequestDTO("Иванов Иван", 31, "Мужской");

        doNothing().when(visitorService).update(eq(1L), any(VisitorRequestDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateFails() throws Exception {
        VisitorRequestDTO dto = new VisitorRequestDTO("Иванов Иван", 30, "Мужской");

        // Посетитель не найден
        doThrow(new RuntimeException("Посетитель не найден"))
                .when(visitorService).update(eq(1L), any(VisitorRequestDTO.class));

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteVisitor() throws Exception {
        doNothing().when(visitorService).removeById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("Посетитель не найден"))
                .when(visitorService).removeById(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestOnInvalidInput() throws Exception {
        VisitorRequestDTO invalidRequest = new VisitorRequestDTO("Иванов Иван", -1, "Пол");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}