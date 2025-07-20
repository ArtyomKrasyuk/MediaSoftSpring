package com.example.MediaSoftSpring.services;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.entities.Rating;
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

    public boolean save(VisitorRequestDTO dto){
        return visitorRepository.save(mapper.toEntity(dto));
    }

    public boolean remove(Visitor visitor){
        boolean result = visitorRepository.remove(visitor);
        if(result){
            removeRatings(visitor.getId());
            return true;
        }
        else return false;
    }

    public boolean removeById(Long id){
        boolean result = visitorRepository.removeById(id);
        if(result){
            removeRatings(id);
            return true;
        }
        else return false;
    }

    public boolean update(Long id, VisitorRequestDTO dto){
        Visitor visitor = visitorRepository.findById(id);
        if(visitor != null){
            visitor.setName(dto.name());
            visitor.setAge(dto.age());
            visitor.setSex(dto.sex());
            return true;
        }
        else return false;
    }

    public List<VisitorResponseDTO> findAll(){
        return visitorRepository.findAll().stream().map(mapper::toDTO).toList();
    }

    public VisitorResponseDTO findById(Long id){
        return mapper.toDTO(visitorRepository.findById(id));
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
