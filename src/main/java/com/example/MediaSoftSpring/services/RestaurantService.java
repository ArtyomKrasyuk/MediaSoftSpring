package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.mapstruct.RestaurantMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean save(RestaurantRequestDTO dto){
        return restaurantRepository.save(mapper.toEntity(dto));
    }

    public boolean remove(Restaurant restaurant){
        boolean result = restaurantRepository.remove(restaurant);
        if(result){
            removeRatings(restaurant.getId());
            return true;
        }
        else return false;
    }

    public boolean removeById(Long id){
        boolean result = restaurantRepository.removeById(id);
        if(result){
            removeRatings(id);
            return true;
        }
        else return false;
    }

    public boolean update(Long id, RestaurantRequestDTO dto){
        Restaurant restaurant = restaurantRepository.findById(id);
        if(restaurant != null){
            restaurant.setTitle(dto.title());
            restaurant.setDescription(dto.description());
            restaurant.setCuisine(Restaurant.Cuisine.valueOf(dto.cuisine()));
            restaurant.setAverageBill(dto.averageBill());
            return true;
        }
        else return false;
    }

    public List<RestaurantResponseDTO> findAll(){
        return restaurantRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public RestaurantResponseDTO findById(Long id){
        return mapper.toDTO(restaurantRepository.findById(id));
    }

    // Метод для удаления всех оценок ресторана, который был удалён
    private void removeRatings(Long id){
       List<Rating> ratings = ratingRepository.findAll().stream().filter(elem -> elem.getRestaurantId().equals(id)).toList();
       ratings.forEach(ratingRepository::remove);
    }
}
