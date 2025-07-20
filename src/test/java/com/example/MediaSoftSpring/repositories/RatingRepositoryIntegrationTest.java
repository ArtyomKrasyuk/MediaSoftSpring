package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
class RatingRepositoryIntegrationTest {

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
    private RatingRepository ratingRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private VisitorRepository visitorRepository;

    private Restaurant restaurant1;

    @BeforeEach
    void setUp() {
        restaurant1 = restaurantRepository.save(new Restaurant("title", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(1000)));
        Restaurant restaurant2 = restaurantRepository.save(new Restaurant("title", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(1000)));
        Visitor visitor1 = visitorRepository.save(new Visitor("Артём", 21, "Мужской"));
        Visitor visitor2 = visitorRepository.save(new Visitor("Артём", 21, "Мужской"));
        Visitor visitor3 = visitorRepository.save(new Visitor("Артём", 21, "Мужской"));
        Rating rating1 = new Rating(new RatingId(visitor1.getId(), restaurant1.getId()), 5, "Отлично", visitor1, restaurant1);
        Rating rating2 = new Rating(new RatingId(visitor2.getId(), restaurant1.getId()), 4, "Хорошо", visitor2, restaurant1);
        Rating rating3 = new Rating(new RatingId(visitor3.getId(), restaurant2.getId()), 3, "Нормально", visitor3, restaurant2);

        ratingRepository.saveAll(List.of(rating1, rating2, rating3));
    }

    @Test
    void shouldReturnRatingsByRestaurantId() {
        List<Rating> ratings = ratingRepository.findByRestaurantId(restaurant1.getId());

        assertThat(ratings).hasSize(2);
    }

    @Test
    void shouldReturnEmptyListWhenRestaurantIdNotFound() {
        List<Rating> ratings = ratingRepository.findByRestaurantId(999999999999L);

        assertThat(ratings).isEmpty();
    }
}