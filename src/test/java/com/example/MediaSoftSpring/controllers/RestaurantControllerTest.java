package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.services.RestaurantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllRestaurants() throws Exception {
        List<RestaurantResponseDTO> restaurants = List.of(
                new RestaurantResponseDTO(1L, "Итальянский ресторан", "Описание", Restaurant.Cuisine.ITALIAN, new BigDecimal("1000"), new BigDecimal("4.5"))
        );

        when(restaurantService.findAll()).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Итальянский ресторан")));
    }

    @Test
    void shouldReturnRestaurantById() throws Exception {
        RestaurantResponseDTO restaurant = new RestaurantResponseDTO(1L, "Итальянский ресторан", "Описание", Restaurant.Cuisine.ITALIAN, new BigDecimal("1000"), new BigDecimal("4.5"));

        when(restaurantService.findById(1L)).thenReturn(restaurant);

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Итальянский ресторан")));
    }

    @Test
    void shouldReturnRestaurantsByRating() throws Exception {
        List<RestaurantResponseDTO> restaurants = List.of(
                new RestaurantResponseDTO(1L, "Китайский ресторан", "Описание", Restaurant.Cuisine.CHINESE, new BigDecimal("1500"), new BigDecimal("4.8"))
        );

        when(restaurantService.findByRating(new BigDecimal("4.5"))).thenReturn(restaurants);

        mockMvc.perform(get("/api/restaurants/rating?rating=4.5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cuisine", is("CHINESE")));
    }

    @Test
    void shouldSaveRestaurant() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Название", "Описание", "ITALIAN", new BigDecimal("2000"));

        doNothing().when(restaurantService).save(any(RestaurantRequestDTO.class));

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForInvalidRestaurant() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("", "Описание", "RUSSIAN", new BigDecimal("-10"));
        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Обновлённое название", "Описание", "EUROPEAN", new BigDecimal("3000"));

        doNothing().when(restaurantService).update(eq(1L), any(RestaurantRequestDTO.class));

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenUpdateFails() throws Exception {
        RestaurantRequestDTO dto = new RestaurantRequestDTO("Название", "Описание", "ITALIAN", new BigDecimal("2000"));

        doThrow(new RuntimeException("Элемент не найден")).when(restaurantService).update(eq(1L), any(RestaurantRequestDTO.class));

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        doNothing().when(restaurantService).removeById(1L);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestWhenDeleteFails() throws Exception {
        doThrow(new RuntimeException("Элемент не найден")).when(restaurantService).removeById(1L);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailOnNegativeId() throws Exception {
        mockMvc.perform(get("/api/restaurants/-5"))
                .andExpect(status().isBadRequest());
    }
}