package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.*;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.mapstruct.RatingMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RatingMapper mapper;

    @InjectMocks
    private RatingService ratingService;

    private Restaurant restaurant1; // Ресторан с одной оценкой
    private Restaurant restaurant2; // Ресторан с двумя оценками
    private Rating rating1;
    private Rating rating2;
    private Rating rating3;
    private RatingPostRequestDTO postRequestDTO1;
    private RatingPostRequestDTO postRequestDTO2;
    private RatingPutRequestDTO putRequestDTO;
    private RatingResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        Visitor visitor1 = new Visitor("Артём", 21, "Мужской");
        Visitor visitor2 = new Visitor("Иван", 21, "Мужской");
        restaurant1 = new Restaurant("title1", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000));
        restaurant2 = new Restaurant("title1", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000));
        restaurant1.setId(1L);
        restaurant2.setId(1L);
        Set<Rating> ratings = new HashSet<>();
        rating1 = new Rating(new RatingId(1L, 1L), 5, "Отлично", visitor1, restaurant1);
        ratings.add(rating1);
        restaurant1.setRatings(ratings);
        Set<Rating> ratingSet = new HashSet<>();
        rating2 = new Rating(new RatingId(2L, 2L), 4, "Хорошо", visitor2, restaurant2);
        rating3 = new Rating(new RatingId(1L, 2L), 5, "Отлично", visitor1, restaurant2);
        ratingSet.add(rating2);
        ratingSet.add(rating3);
        restaurant2.setRatings(ratingSet);
        postRequestDTO1 = new RatingPostRequestDTO(1L, 1L, 5, "Отлично");
        postRequestDTO2 = new RatingPostRequestDTO(2L, 1L, 4, "Хорошо");
        putRequestDTO = new RatingPutRequestDTO(5, "Отлично");
        responseDTO = new RatingResponseDTO(1L, 1L, 5, "Отлично");
    }

    @Test
    void findById_shouldReturnRating_whenIdIsCorrect(){
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.of(rating1));
        when(mapper.toDTO(rating1)).thenReturn(responseDTO);

        RatingResponseDTO dto = ratingService.findById(1L, 1L);

        Assertions.assertEquals(1L, dto.visitorId());
        Assertions.assertEquals(1L, dto.restaurantId());
        Assertions.assertEquals(responseDTO.rating(), dto.rating());
        Assertions.assertEquals(responseDTO.text(), dto.text());
    }

    @Test
    void findById_shouldReturnNull_whenRatingNotFound(){
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.empty());

        RatingResponseDTO dto = ratingService.findById(1L, 1L);

        Assertions.assertNull(dto);
    }

    @Test
    void save_shouldMapAndSaveRating() {
        when(mapper.toEntity(postRequestDTO2)).thenReturn(rating2);
        when(ratingRepository.save(rating2)).thenReturn(rating2);

        ratingService.save(postRequestDTO2);

        verify(ratingRepository).save(rating2);
        Assertions.assertEquals(BigDecimal.valueOf(4.5).setScale(2, RoundingMode.HALF_UP), restaurant2.getRating()); // Проверка, что оценка ресторана изменилась после добавления
    }

    @Test
    void save_shouldThrow_whenIdExists(){
        when(ratingRepository.existsById(new RatingId(1L, 1L))).thenReturn(true);

        Assertions.assertThrows(EntityExistsException.class, () -> ratingService.save(postRequestDTO1));
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating1));
        when(mapper.toDTO(rating1)).thenReturn(responseDTO);

        List<RatingResponseDTO> result = ratingService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.getFirst().visitorId());
        Assertions.assertEquals(1L, result.getFirst().restaurantId());
    }

    @Test
    void update_shouldMapAndSaveUpdatedRating_whenRatingExists() {
        restaurant1.setRating(BigDecimal.ZERO); // Случайный рейтинг
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.of(rating1));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        ratingService.update(1L, 1L, putRequestDTO);

        verify(mapper).update(putRequestDTO, rating1);
        verify(ratingRepository).save(rating1);
        // Проверка, что оценка изменилась
        Assertions.assertEquals(BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP), restaurant1.getRating());
    }

    @Test
    void update_shouldThrow_whenRatingNotFound(){
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> ratingService.update(1L, 1L, putRequestDTO));
    }

    @Test
    void removeById_shouldDeleteRating_whenRatingExists() {
        restaurant1.setRating(BigDecimal.ZERO); // Случайный рейтинг
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.of(rating3));
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));

        ratingService.removeById(1L, 1L);

        verify(ratingRepository).delete(rating3);
        Assertions.assertEquals(BigDecimal.valueOf(5).setScale(2, RoundingMode.HALF_UP), restaurant1.getRating());
    }

    @Test
    void removeById_shouldThrow_whenRatingNotFound() {
        when(ratingRepository.findById(new RatingId(1L, 1L))).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> ratingService.removeById(1L, 1L));
    }

    @Test
    void findAllSorting_shouldReturnListOfRatings(){
        when(ratingRepository.findAll((Sort.by("rating")).descending())).thenReturn(List.of(rating1, rating3, rating2));
        when(mapper.toDTO(rating1)).thenReturn(responseDTO);
        when(mapper.toDTO(rating3)).thenReturn(new RatingResponseDTO(1L, 2L, 5, "Отлично"));
        when(mapper.toDTO(rating2)).thenReturn(new RatingResponseDTO(2L, 2L, 4, "Хорошо"));

        List<RatingResponseDTO> res = ratingService.findAllSorting();

        Assertions.assertEquals(5, res.get(0).rating());
        Assertions.assertEquals(5, res.get(1).rating());
        Assertions.assertEquals(4, res.get(2).rating());
    }

    @Test
    void findAllPageable_shouldReturnListOfElementsFromPage(){
        when(ratingRepository.findAll(PageRequest.of(0, 2))).thenReturn(new PageImpl<>(List.of(rating1, rating2)));
        when(mapper.toDTO(rating1)).thenReturn(responseDTO);
        when(mapper.toDTO(rating2)).thenReturn(new RatingResponseDTO(2L, 2L, 4, "Хорошо"));

        List<RatingResponseDTO> res = ratingService.findAllPageable(0, 2);

        Assertions.assertEquals(5, res.get(0).rating());
        Assertions.assertEquals(4, res.get(1).rating());
    }
}
