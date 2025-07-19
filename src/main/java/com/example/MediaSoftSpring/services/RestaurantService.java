package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.mapstruct.RestaurantMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final RestaurantMapper mapper;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, RatingRepository ratingRepository, RestaurantMapper mapper) {
        this.restaurantRepository = restaurantRepository;
        this.ratingRepository = ratingRepository;
        this.mapper = mapper;
    }

    public void save(RestaurantRequestDTO dto){
        restaurantRepository.save(mapper.toEntity(dto));
    }

    public RestaurantResponseDTO findById(Long id){
        return mapper.toDTO(restaurantRepository.findById(id).orElse(null));
    }

    public List<RestaurantResponseDTO> findAll(){
        return restaurantRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public List<RestaurantResponseDTO> findByRating(BigDecimal rating){
        return restaurantRepository.findByRating(rating).stream().map(mapper::toDTO).toList();
    }

    // При удалении ресторана удаляются все связанные с ним оценки
    public void removeById(Long id){
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        restaurant.getRatings().clear();
        restaurantRepository.deleteById(id);
    }

    public void update(Long id, RestaurantRequestDTO dto){
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow();
        mapper.update(dto, restaurant);
        restaurantRepository.save(restaurant);
    }
}
