package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.entities.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
public class RestaurantRepositoryIntegrationTest {

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
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setUp(){
        Restaurant restaurant = new Restaurant("title", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(1000));
        restaurantRepository.save(restaurant);
        restaurant.setRating(BigDecimal.valueOf(4.5));
    }

    @Test
    void shouldReturnOneRestaurant(){
        List<Restaurant> restaurants = restaurantRepository.findByRating(BigDecimal.valueOf(4));

        assertThat(restaurants).hasSize(1);
    }

    @Test
    void shouldReturnEmptyList(){
        List<Restaurant> restaurants = restaurantRepository.findByRating(BigDecimal.valueOf(4.6));

        assertThat(restaurants).isEmpty();
    }
}
