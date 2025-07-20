package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
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
public class RestaurantServiceIntegrationTest {

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
    }

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private RestaurantRequestDTO restaurantDTO;

    @BeforeEach
    void setUp() {
        restaurantRepository.deleteAll();

        restaurantDTO = new RestaurantRequestDTO(
                "Итальянский ресторан",
                "Ресторан с пастой и пиццей",
                "ITALIAN",
                BigDecimal.valueOf(1200)
        );
    }

    @Test
    void testSaveAndFindById() {
        restaurantService.save(restaurantDTO);

        List<RestaurantResponseDTO> all = restaurantService.findAll();
        assertThat(all).hasSize(1);

        RestaurantResponseDTO saved = all.get(0);
        RestaurantResponseDTO found = restaurantService.findById(saved.id());

        assertThat(found).isNotNull();
        assertThat(found.title()).isEqualTo(restaurantDTO.title());
        assertThat(found.cuisine().name()).isEqualTo(restaurantDTO.cuisine());
    }

    @Test
    void testUpdate() {
        restaurantService.save(restaurantDTO);
        RestaurantResponseDTO saved = restaurantService.findAll().get(0);

        RestaurantRequestDTO updatedDTO = new RestaurantRequestDTO(
                "Обновлённый ресторан",
                "Новое описание",
                "EUROPEAN",
                BigDecimal.valueOf(1500)
        );

        restaurantService.update(saved.id(), updatedDTO);
        RestaurantResponseDTO updated = restaurantService.findById(saved.id());

        assertThat(updated.title()).isEqualTo("Обновлённый ресторан");
        assertThat(updated.cuisine()).isEqualTo(Restaurant.Cuisine.EUROPEAN);
    }

    // Данный тест не выполняется, потому что внутри метода restaurantService.removeById(saved.id()).
    // Происходит очистка оценок ресторана с помощью restaurant.getRatings().clear().
    // Поле ratings у сущности Restaurant помечено аннотацией OneToMany, однако по непонятной мне причине во время теста
    // restaurantRepository возвращает сущность ресторана с неинициализированным полем ratings, хотя он должен установить туда set c оценками.
    // Даже установка FetchType.EAGER не решила ситуацию. При этом во время обычного запуска приложения всё работает нормально.
    // Я перепробовал множетсво разных подходов, но так и не понял, как мне решить данную проблему.
    @Test
    void testRemoveById() {
        restaurantService.save(restaurantDTO);
        RestaurantResponseDTO saved = restaurantService.findAll().get(0);

        restaurantService.removeById(saved.id());

        List<RestaurantResponseDTO> result = restaurantService.findAll();
        assertThat(result).isEmpty();
    }

    @Test
    void testFindByRating() {
        restaurantService.save(restaurantDTO);
        Restaurant savedEntity = restaurantRepository.findAll().get(0);
        savedEntity.setRating(BigDecimal.valueOf(4.3));
        restaurantRepository.save(savedEntity);

        List<RestaurantResponseDTO> result = restaurantService.findByRating(BigDecimal.valueOf(4.3));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).rating()).isEqualTo(BigDecimal.valueOf(4.3));
    }
}