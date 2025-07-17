package com.example.MediaSoftSpring.repositories;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.entities.Restaurant;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

@Repository
public class RestaurantRepository {
    private final List<Restaurant> restaurants;

    public RestaurantRepository(){
        restaurants = new LinkedList<>();
    }

    public boolean save(Restaurant restaurant){
        for(Restaurant elem: restaurants){
            if(elem.getId().equals(restaurant.getId())) return false;
        }
        restaurants.add(restaurant);
        return true;
    }

    public boolean remove(Restaurant restaurant){
        return restaurants.remove(restaurant);
    }

    public boolean removeById(Long id){
        return restaurants.removeIf(elem -> elem.getId().equals(id));
    }

    public List<Restaurant> findAll(){
        return restaurants;
    }

    public Restaurant findById(Long id){
        return restaurants.stream().filter(elem -> elem.getId().equals(id)).findFirst().orElse(null);
    }

    public boolean changeRestaurantRating(Long id, BigDecimal rating){
        for(Restaurant restaurant: restaurants){
            if(restaurant.getId().equals(id)) {
                restaurant.setRating(rating);
                return true;
            }
        }
        return false;
    }

    @PostConstruct
    public void setData(){
        save(new Restaurant(1L, "Европейский", null, Restaurant.Cuisine.EUROPEAN, BigDecimal.valueOf(10000)));
        save(new Restaurant(2L, "Китайский", "Китайский ресторан", Restaurant.Cuisine.CHINESE, BigDecimal.valueOf(5000)));
        save(new Restaurant(3L, "Итальянский", "Итальянский ресторан", Restaurant.Cuisine.ITALIAN, BigDecimal.valueOf(4000)));
    }
}
