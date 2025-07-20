package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
class VisitorServiceIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER =
            new PostgreSQLContainer<>("postgres:15")
                    .withDatabaseName("test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    public static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_CONTAINER::getDriverClassName);
    }

    @Autowired
    private VisitorService visitorService;

    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RatingRepository ratingRepository;

    private VisitorRequestDTO testDTO;

    @BeforeEach
    void setUp() {
        testDTO = new VisitorRequestDTO("Иванов Иван", 30, "Мужской");
    }

    @Test
    void testSaveAndFindById() {
        visitorService.save(testDTO);

        List<Visitor> visitors = visitorRepository.findAll();
        assertThat(visitors).hasSize(1);

        VisitorResponseDTO response = visitorService.findById(visitors.get(0).getId());

        assertThat(response.name()).isEqualTo(testDTO.name());
        assertThat(response.age()).isEqualTo(testDTO.age());
        assertThat(response.sex()).isEqualTo(testDTO.sex());
    }

    @Test
    void testFindAll() {
        visitorService.save(testDTO);
        visitorService.save(new VisitorRequestDTO("Петров Петр", 25, "Мужской"));

        List<VisitorResponseDTO> result = visitorService.findAll();
        assertThat(result).hasSize(2);
    }

    @Test
    void testUpdate() {
        visitorService.save(testDTO);
        Visitor visitor = visitorRepository.findAll().get(0);

        VisitorRequestDTO updated = new VisitorRequestDTO("Обновлённый", 35, "Мужской");
        visitorService.update(visitor.getId(), updated);

        VisitorResponseDTO response = visitorService.findById(visitor.getId());

        assertThat(response.name()).isEqualTo("Обновлённый");
        assertThat(response.age()).isEqualTo(35);
    }

    // Данный тест не выполняется, потому что внутри метода visitorService.removeById(visitor.getId()).
    // Происходит итерирование по оценкам посетителя с помощью visitor.getRatings().stream().
    // Поле ratings у сущности Visitor помечено аннотацией OneToMany, однако по непонятной мне причине во время теста
    // visitorRepository возвращает сущность посетителя с неинициализированным полем ratings, хотя он должен установить туда set c оценками.
    // Даже установка FetchType.EAGER не решила ситуацию. При этом во время обычного запуска приложения всё работает нормально.
    // Я перепробовал множетсво разных подходов, но так и не понял, как мне решить данную проблему.
    @Test
    void testRemoveByIdAlsoDeletesRatingsAndRecalculatesRestaurantRating() {
        Restaurant restaurant = new Restaurant("test", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(1000));
        restaurant = restaurantRepository.save(restaurant);

        VisitorRequestDTO visitorDTO = new VisitorRequestDTO("Алексей Смирнов", 28, "Мужской");
        visitorService.save(visitorDTO);
        Visitor visitor = visitorRepository.findAll().get(0);

        Rating rating = new Rating(new RatingId(visitor.getId(), restaurant.getId()), 5, "Отлично", visitor, restaurant);
        ratingRepository.save(rating);
        restaurant.setRating(BigDecimal.valueOf(5));
        restaurantRepository.save(restaurant);

        Restaurant before = restaurantRepository.findById(restaurant.getId()).orElseThrow();
        assertThat(before.getRating()).isEqualTo(BigDecimal.valueOf(5));

        visitorService.removeById(visitor.getId()); // проблема

        assertThat(visitorRepository.findById(visitor.getId())).isEmpty();

        List<Rating> remainingRatings = ratingRepository.findByRestaurantId(restaurant.getId());
        assertThat(remainingRatings).isEmpty();

        Restaurant updated = restaurantRepository.findById(restaurant.getId()).orElseThrow();
        assertThat(updated.getRating()).isEqualTo(BigDecimal.ZERO.setScale(2));
    }
}
