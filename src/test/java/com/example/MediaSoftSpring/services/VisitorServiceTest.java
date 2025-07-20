package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.mapstruct.VisitorMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VisitorServiceTest {
    @Mock
    private VisitorRepository visitorRepository;
    @Mock
    private RestaurantRepository restaurantRepository;
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private VisitorMapper mapper;

    @InjectMocks
    private VisitorService visitorService;

    private Visitor visitor;
    private Restaurant restaurant1;
    private Restaurant restaurant2;
    private VisitorRequestDTO requestDTO;
    private VisitorResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        visitor = new Visitor("Артём", 21, "Мужской");
        restaurant1 = new Restaurant("title1", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000));
        restaurant1.setId(1L);
        restaurant2 = new Restaurant("title2", null, Restaurant.Cuisine.ITALIAN, BigDecimal.valueOf(10000));
        restaurant2.setId(2L);
        Set<Rating> ratings = new HashSet<>();
        ratings.add(new Rating(new RatingId(1L, 1L), 5, "Отлично", visitor, restaurant1));
        ratings.add(new Rating(new RatingId(1L, 2L), 4, "Хорошо", visitor, restaurant2));
        visitor.setRatings(ratings);

        requestDTO = new VisitorRequestDTO("Артём", 21, "Мужской");
        responseDTO = new VisitorResponseDTO(1L, "Артём", 21, "Мужской");
    }

    @Test
    void findById_shouldReturnVisitor_whenIdIsCorrect(){
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(mapper.toDTO(visitor)).thenReturn(responseDTO);

        VisitorResponseDTO dto = visitorService.findById(1L);

        Assertions.assertEquals(1L, dto.id());
        Assertions.assertEquals(responseDTO.name(), dto.name());
        Assertions.assertEquals(responseDTO.age(), dto.age());
        Assertions.assertEquals(responseDTO.sex(), dto.sex());
    }

    @Test
    void findById_shouldReturnNull_whenVisitorNotFound(){
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());

        VisitorResponseDTO dto = visitorService.findById(1L);

        Assertions.assertNull(dto);
    }

    @Test
    void save_shouldMapAndSaveVisitor() {
        when(mapper.toEntity(requestDTO)).thenReturn(visitor);

        visitorService.save(requestDTO);

        verify(visitorRepository).save(visitor);
    }

    @Test
    void findAll_shouldReturnListOfDTOs() {
        when(visitorRepository.findAll()).thenReturn(List.of(visitor));
        when(mapper.toDTO(visitor)).thenReturn(responseDTO);

        List<VisitorResponseDTO> result = visitorService.findAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(1L, result.getFirst().id());
    }

    @Test
    void update_shouldMapAndSaveUpdatedVisitor_whenVisitorExists() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));

        visitorService.update(1L, requestDTO);

        verify(mapper).update(requestDTO, visitor);
        verify(visitorRepository).save(visitor);
    }

    @Test
    void update_shouldThrow_whenVisitorNotFound(){
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> visitorService.update(1L, requestDTO));
    }

    @Test
    void removeById_shouldDeleteVisitorAndUpdateRatings_whenVisitorExists() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.of(visitor));
        when(restaurantRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(restaurant1, restaurant2));
        when(ratingRepository.findByRestaurantId(1L)).thenReturn(List.of()); // Не осталось оценок
        when(ratingRepository.findByRestaurantId(2L)).thenReturn(List.of()); // Не осталось оценок

        visitorService.removeById(1L);

        verify(visitorRepository).deleteById(1L);
        verify(restaurantRepository).save(restaurant1);
        verify(restaurantRepository).save(restaurant2);
        Assertions.assertEquals(BigDecimal.ZERO, restaurant1.getRating());
        Assertions.assertEquals(BigDecimal.ZERO, restaurant2.getRating());
    }

    @Test
    void removeById_shouldThrow_whenVisitorNotFound() {
        when(visitorRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> visitorService.removeById(1L));
    }
}
