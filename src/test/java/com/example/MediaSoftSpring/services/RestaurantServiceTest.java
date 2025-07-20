package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.mapstruct.RestaurantMapper;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RestaurantMapper mapper;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;
    private RestaurantRequestDTO requestDTO;
    private RestaurantResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        Visitor visitor = new Visitor("Артём", 21, "Мужской");
        restaurant = new Restaurant("title1", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000));
        restaurant.setId(1L);
        Set<Rating> ratings = new HashSet<>();
        ratings.add(new Rating(new RatingId(1L, 1L), 5, "Отлично", visitor, restaurant));
        restaurant.setRatings(ratings);
        requestDTO = new RestaurantRequestDTO("title1", null, "EUROPEAN", BigDecimal.valueOf(10000));
        responseDTO = new RestaurantResponseDTO(1L, "title1", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000), BigDecimal.valueOf(5));
    }

    @Test
    void findById_shouldReturnRestaurant_whenIdIsCorrect(){
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));
        when(mapper.toDTO(restaurant)).thenReturn(responseDTO);

        RestaurantResponseDTO dto = restaurantService.findById(1L);

        Assertions.assertEquals(1L, dto.id());
        Assertions.assertEquals(responseDTO.title(), dto.title());
        Assertions.assertEquals(responseDTO.description(), dto.description());
        Assertions.assertEquals(responseDTO.cuisine(), dto.cuisine());
        Assertions.assertEquals(responseDTO.averageBill(), dto.averageBill());
        Assertions.assertEquals(responseDTO.rating(), dto.rating());
    }

    @Test
    void findByRating_shouldReturnRestaurantsWithNeededRating(){
        restaurant.setRating(BigDecimal.valueOf(5));
        when(restaurantRepository.findByRating(BigDecimal.valueOf(4))).thenReturn(List.of(restaurant));
        when(mapper.toDTO(restaurant)).thenReturn(responseDTO);

        List<RestaurantResponseDTO> res = restaurantService.findByRating(BigDecimal.valueOf(4));

        Assertions.assertEquals(BigDecimal.valueOf(5), responseDTO.rating());
    }

    @Test
    void findById_shouldReturnNull_whenRestaurantNotFound(){
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());

        RestaurantResponseDTO dto = restaurantService.findById(1L);

        Assertions.assertNull(dto);
    }

    @Test
    void save_shouldMapAndSaveRestaurant() {
        when(mapper.toEntity(requestDTO)).thenReturn(restaurant);

        restaurantService.save(requestDTO);

        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        when(restaurantRepository.findAll()).thenReturn(List.of(restaurant));
        when(mapper.toDTO(restaurant)).thenReturn(responseDTO);

        List<RestaurantResponseDTO> result = restaurantService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.getFirst().id());
    }

    @Test
    void update_shouldMapAndSaveUpdatedRestaurant_whenRestaurantExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.update(1L, requestDTO);

        verify(mapper).update(requestDTO, restaurant);
        verify(restaurantRepository).save(restaurant);
    }

    @Test
    void update_shouldThrow_whenRestaurantNotFound(){
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> restaurantService.update(1L, requestDTO));
    }

    @Test
    void removeById_shouldDeleteRestaurantAndRatings_whenRestaurantExists() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        restaurantService.removeById(1L);

        verify(restaurantRepository).deleteById(1L);
        Assertions.assertTrue(restaurant.getRatings().isEmpty());
    }

    @Test
    void removeById_shouldThrow_whenRestaurantNotFound() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> restaurantService.removeById(1L));
    }
}
