package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository, VisitorRepository visitorRepository) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
    }

    public boolean save(Rating rating){
        if(visitorExists(rating.getVisitorId()) && restaurantExists(rating.getRestaurantId())){
            boolean added = ratingRepository.save(rating);
            if(added){
                changeRestaurantRating(rating.getRestaurantId());
                return true;
            }
            else return false;
        }
        else return false;
    }

    public boolean remove(Rating rating){
        if(ratingRepository.remove(rating)){
            changeRestaurantRating(rating.getRestaurantId());
            return true;
        }
        else return false;
    }

    public List<Rating> findAll(){
        return ratingRepository.findAll();
    }

    public Rating findById(Long visitorId, Long restaurantId){
        return ratingRepository.findById(visitorId, restaurantId);
    }

    // Метод для изменения рейтинга ресторана
    private void changeRestaurantRating(Long id){
        BigDecimal decimal = BigDecimal.ZERO;
        long count = 0;
        for(Rating rating: ratingRepository.findAll()){
            if(rating.getRestaurantId().equals(id)){
                decimal = decimal.add(BigDecimal.valueOf(rating.getRating()));
                count++;
            }
        }
        restaurantRepository.changeRestaurantRating(id, decimal.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
    }

    private boolean visitorExists(Long id){
        for(Visitor visitor: visitorRepository.findAll()){
            if(visitor.getId().equals(id)) return true;
        }
        return false;
    }

    private boolean restaurantExists(Long id){
        for(Restaurant restaurant: restaurantRepository.findAll()){
            if(restaurant.getId().equals(id)) return true;
        }
        return false;
    }

    @PostConstruct
    public void setData(){
        // Создание оценок через метод сервиса, чтобы пересчитывать оценку уже созданного ресторана
        save(new Rating(1L, 1L, 4, "Текст"));
        save(new Rating(2L, 1L, 3, null));
        save(new Rating(2L, 2L, 5, null));
        save(new Rating(3L, 2L, 4, null));
        save(new Rating(1L, 2L, 4, null));
        save(new Rating(1L, 3L, 5, null));
        save(new Rating(3L, 3L, 3, null));
    }
}
