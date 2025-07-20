package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.RatingRequestDTO;
import com.example.MediaSoftSpring.dto.RatingResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
import com.example.MediaSoftSpring.entities.Restaurant;
import com.example.MediaSoftSpring.entities.Visitor;
import com.example.MediaSoftSpring.mapstruct.RatingMapper;
import com.example.MediaSoftSpring.repositories.RatingRepository;
import com.example.MediaSoftSpring.repositories.RestaurantRepository;
import com.example.MediaSoftSpring.repositories.VisitorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final RestaurantRepository restaurantRepository;
    private final VisitorRepository visitorRepository;
    private final RatingMapper mapper;

    @Autowired
    public RatingService(RatingRepository ratingRepository, RestaurantRepository restaurantRepository, VisitorRepository visitorRepository, RatingMapper mapper) {
        this.ratingRepository = ratingRepository;
        this.restaurantRepository = restaurantRepository;
        this.visitorRepository = visitorRepository;
        this.mapper = mapper;
    }

    public boolean save(RatingRequestDTO dto){
        if(visitorExists(dto.visitorId()) && restaurantExists(dto.restaurantId())){
            boolean added = ratingRepository.save(mapper.toEntity(dto));
            if(added){
                changeRestaurantRating(dto.restaurantId());
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

    public boolean removeById(Long id){
        Rating rating = ratingRepository.findById(id);
        if(ratingRepository.removeById(id)){
            changeRestaurantRating(rating.getRestaurantId());
            return true;
        }
        else return false;
    }

    public boolean update(Long id, RatingRequestDTO dto){
        Rating rating = ratingRepository.findById(id);
        Long oldRestaurant = 0L;
        boolean restaurantChanged = false;
        boolean ratingChanged = false;
        if(rating != null){
            // Проверка, что ресторан и посетитель с указанными id существуют
            if(!visitorExists(dto.visitorId()) || !restaurantExists(dto.restaurantId())) return false;
            // Проверка, что оценки с такими id ресторана и посетителя ещё нет
            for(Rating elem: ratingRepository.findAll()){
                if(elem.getVisitorId().equals(dto.visitorId()) && elem.getRestaurantId().equals(dto.restaurantId())) return false;
            }
            rating.setVisitorId(dto.visitorId());
            if(!rating.getRestaurantId().equals(dto.restaurantId())){
                oldRestaurant = rating.getRestaurantId();
                restaurantChanged = true;
                rating.setRestaurantId(dto.restaurantId());
            }
            if(rating.getRating() != dto.rating()){
                ratingChanged = true;
                rating.setRating(dto.rating());
            }
            rating.setText(dto.text());
            if(restaurantChanged){
                changeRestaurantRating(oldRestaurant);
                changeRestaurantRating(dto.restaurantId());
            }
            else if(ratingChanged) changeRestaurantRating(dto.restaurantId());
            return true;
        }
        else return false;
    }

    public List<RatingResponseDTO> findAll(){
        return ratingRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public RatingResponseDTO findById(Long id){
        return mapper.toDTO(ratingRepository.findById(id));
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
        save(new RatingRequestDTO(1L, 1L, 4, "Текст"));
        save(new RatingRequestDTO(2L, 1L, 3, null));
        save(new RatingRequestDTO(2L, 2L, 5, null));
        save(new RatingRequestDTO(3L, 2L, 4, null));
        save(new RatingRequestDTO(1L, 2L, 4, null));
        save(new RatingRequestDTO(1L, 3L, 5, null));
        save(new RatingRequestDTO(3L, 3L, 3, null));
    }
}
