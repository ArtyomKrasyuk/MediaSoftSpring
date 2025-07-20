package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.*;
import com.example.MediaSoftSpring.services.RatingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RatingService ratingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void saveRating_success() throws Exception {
        RatingPostRequestDTO dto = new RatingPostRequestDTO(1L, 1L, 5, "Отлично!");

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(ratingService).save(dto);
    }

    @Test
    void saveRating_invalidRating() throws Exception {
        RatingPostRequestDTO dto = new RatingPostRequestDTO(1L, 1L, 0, ""); // Невалидная оценка

        mockMvc.perform(post("/api/reviews")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_success() throws Exception {
        RatingResponseDTO dto = new RatingResponseDTO(1L, 1L, 5, "Отлично");
        when(ratingService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/reviews"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1))
                .andExpect(jsonPath("$[0].rating").value(5));
    }

    @Test
    void findById_success() throws Exception {
        RatingResponseDTO dto = new RatingResponseDTO(1L, 1L, 4, "Нормально");
        when(ratingService.findById(1L, 1L)).thenReturn(dto);

        mockMvc.perform(get("/api/reviews/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    void findById_invalidId() throws Exception {
        mockMvc.perform(get("/api/reviews/-1/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_success() throws Exception {
        RatingPutRequestDTO dto = new RatingPutRequestDTO(4, "Обновлённый текст");

        mockMvc.perform(put("/api/reviews/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(ratingService).update(1L, 1L, dto);
    }

    @Test
    void update_invalidRating() throws Exception {
        RatingPutRequestDTO dto = new RatingPutRequestDTO(10, ""); // Невалидная оценка

        mockMvc.perform(put("/api/reviews/1/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void delete_success() throws Exception {
        mockMvc.perform(delete("/api/reviews/1/1"))
                .andExpect(status().isOk());

        verify(ratingService).removeById(1L, 1L);
    }

    @Test
    void delete_invalidId() throws Exception {
        mockMvc.perform(delete("/api/reviews/-1/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pageableWithSort() throws Exception {
        RatingResponseDTO dto = new RatingResponseDTO(1L, 1L, 5, "Отлично");
        when(ratingService.findAllPageableAndSorting(0, 5)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/reviews/pageable?page=0&size=5&sort=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1));

        verify(ratingService).findAllPageableAndSorting(0, 5);
    }

    @Test
    void findAllSorted() throws Exception {
        RatingResponseDTO dto = new RatingResponseDTO(1L, 1L, 5, "Отлично");
        when(ratingService.findAllSorting()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/reviews/sorting"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visitorId").value(1));

        verify(ratingService).findAllSorting();
    }
}
