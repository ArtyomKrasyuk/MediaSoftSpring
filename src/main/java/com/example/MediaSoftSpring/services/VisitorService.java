package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Visitor;
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

    @Autowired
    public VisitorService(VisitorRepository visitorRepository, RestaurantRepository restaurantRepository, RatingRepository ratingRepository) {
        this.visitorRepository = visitorRepository;
        this.restaurantRepository = restaurantRepository;
        this.ratingRepository = ratingRepository;
    }

    public boolean save(Visitor visitor){
        return visitorRepository.save(visitor);
    }

    public boolean remove(Visitor visitor){
        boolean result = visitorRepository.remove(visitor);
        if(result){
            removeRatings(visitor.getId());
            return true;
        }
        else return false;
    }

    public List<Visitor> findAll(){
        return visitorRepository.findAll();
    }

    public Visitor findById(Long id){
        return visitorRepository.findById(id);
    }

    // Метод для удаления всех оценкок пользователя, который был удалён
    private void removeRatings(Long id){
        List<Rating> ratings = ratingRepository.findAll().stream().filter(elem -> elem.getVisitorId().equals(id)).toList();
        ratings.forEach(rating -> {
            ratingRepository.remove(rating);
            changeRestaurantRating(rating.getRestaurantId());
        });
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
        if(count == 0) restaurantRepository.changeRestaurantRating(id, BigDecimal.ZERO);
        else restaurantRepository.changeRestaurantRating(id, decimal.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP));
    }
}
