package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.mapstruct.VisitorMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class VisitorService {
    private final VisitorRepository visitorRepository;
    private final RestaurantRepository restaurantRepository;
    private final RatingRepository ratingRepository;
    private final VisitorMapper mapper;

    @Autowired
    public VisitorService(VisitorRepository visitorRepository, RestaurantRepository restaurantRepository, RatingRepository ratingRepository, VisitorMapper mapper) {
        this.visitorRepository = visitorRepository;
        this.restaurantRepository = restaurantRepository;
        this.ratingRepository = ratingRepository;
        this.mapper = mapper;
    }

    public void save(VisitorRequestDTO dto){
        visitorRepository.save(mapper.toEntity(dto));
    }

    public VisitorResponseDTO findById(Long id){
        return mapper.toDTO(visitorRepository.findById(id).orElse(null));
    }

    public List<VisitorResponseDTO> findAll(){
        return visitorRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    // При удалении пользователя происходит удаление всех его оценок и пересчёт средних оценок ресторанов
    public void removeById(Long id){
        Visitor visitor = visitorRepository.findById(id).orElseThrow();
        List<Long> restaurantIds = visitor.getRatings().stream().map(elem -> elem.getId().getRestaurantId()).sorted().toList();
        visitor.getRatings().clear();
        visitorRepository.deleteById(id);
        List<Restaurant> restaurants = restaurantRepository.findAllById(restaurantIds);
        restaurants.forEach(this::changeRestaurantRating);
    }

    public void update(Long id, VisitorRequestDTO dto){
        Visitor visitor = visitorRepository.findById(id).orElseThrow();
        mapper.update(dto, visitor);
        visitorRepository.save(visitor);
    }

    // Метод для изменения рейтинга ресторана
    private void changeRestaurantRating(Restaurant restaurant){
        BigDecimal decimal = BigDecimal.ZERO;
        List<Rating> ratings = ratingRepository.findByRestaurantId(restaurant.getId());
        if(!ratings.isEmpty()){
            for(Rating rating: ratings) decimal = decimal.add(BigDecimal.valueOf(rating.getRating()));
            decimal = decimal.divide(BigDecimal.valueOf(restaurant.getRatings().size()), 2, RoundingMode.HALF_UP);
        }
        restaurant.setRating(decimal);
        restaurantRepository.save(restaurant);
    }
}
