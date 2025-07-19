package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RatingPostRequestDTO;
import com.example.MediaSoftSpring.dto.RatingPutRequestDTO;
import com.example.MediaSoftSpring.dto.RatingResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.RatingId;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.mapstruct.RatingMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final RatingMapper mapper;

    @Autowired
    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository, RatingMapper mapper) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
        this.mapper = mapper;
    }

    public void save(RatingPostRequestDTO dto){
        if(ratingRepository.existsById(new RatingId(dto.visitorId(), dto.restaurantId()))) throw new EntityExistsException();
        Rating rating = ratingRepository.save(mapper.toEntity(dto));
        changeRestaurantRating(rating.getRestaurant());
    }

    public RatingResponseDTO findById(Long visitorId, Long restaurantId){
        return mapper.toDTO(ratingRepository.findById(new RatingId(visitorId, restaurantId)).orElse(null));
    }

    public List<RatingResponseDTO> findAll(){
        return ratingRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public void removeById(Long visitorId, Long restaurantId){
        ratingRepository.deleteById(new RatingId(visitorId, restaurantId));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        changeRestaurantRating(restaurant);
    }

    public void update(Long visitorId, Long restaurantId, RatingPutRequestDTO dto){
        Rating rating = ratingRepository.findById(new RatingId(visitorId, restaurantId)).orElseThrow();
        mapper.update(dto, rating);
        ratingRepository.save(rating);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        changeRestaurantRating(restaurant);
    }

    public List<RatingResponseDTO> findAllSorting(){
        List<Rating> ratings = ratingRepository.findAll(Sort.by("rating").descending());
        return ratings.stream().map(mapper::toDTO).toList();
    }

    public List<RatingResponseDTO> findAllPageable(int page, int size){
        List<Rating> ratings =  ratingRepository.findAll(PageRequest.of(page, size)).toList();
        return ratings.stream().map(mapper::toDTO).toList();
    }

    public List<RatingResponseDTO> findAllPageableAndSorting(int page, int size){
        List<Rating> ratings = ratingRepository.findAll(PageRequest.of(page, size, Sort.by("rating").descending())).toList();
        return ratings.stream().map(mapper::toDTO).toList();
    }

    // Метод для изменения рейтинга ресторана
    private void changeRestaurantRating(Restaurant restaurant){
        BigDecimal decimal = BigDecimal.ZERO;
        if(!restaurant.getRatings().isEmpty()){
            for(Rating rating: restaurant.getRatings()) decimal = decimal.add(BigDecimal.valueOf(rating.getRating()));
            decimal = decimal.divide(BigDecimal.valueOf(restaurant.getRatings().size()), 2, RoundingMode.HALF_UP);
        }
        restaurant.setRating(decimal);
        restaurantRepository.save(restaurant);
    }
}
