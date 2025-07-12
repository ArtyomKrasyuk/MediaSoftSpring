package com.example.MediaSoftSpring.runner;

import com.example.MediaSoftSpring.services.RatingService;
import com.example.MediaSoftSpring.services.RestaurantService;
import com.example.MediaSoftSpring.services.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final VisitorService visitorService;
    private final RestaurantService restaurantService;
    private final RatingService ratingService;

    @Autowired
    public CommandLineRunnerImpl(VisitorService visitorService, RestaurantService restaurantService, RatingService ratingService) {
        this.visitorService = visitorService;
        this.restaurantService = restaurantService;
        this.ratingService = ratingService;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Посетители:");
        visitorService.findAll().forEach(System.out::println);
        System.out.println("Рестораны:");
        restaurantService.findAll().forEach(System.out::println);
        System.out.println("Оценки:");
        ratingService.findAll().forEach(System.out::println);
        System.out.println("Удаляем оценку у ресторана с id=2 и проверяем, что рейтинг ресторана изменился");
        ratingService.remove(ratingService.findById(1L, 2L));
        System.out.println("Рестораны:");
        restaurantService.findAll().forEach(System.out::println);
        System.out.println("Оценки:");
        ratingService.findAll().forEach(System.out::println);
        System.out.println("Удаляем посетителя с id=2 и проверяем, что его оценки были удалены и рейтинги ресторанов изменились");
        visitorService.remove(visitorService.findById(2L));
        System.out.println("Посетители:");
        visitorService.findAll().forEach(System.out::println);
        System.out.println("Рестораны:");
        restaurantService.findAll().forEach(System.out::println);
        System.out.println("Оценки:");
        ratingService.findAll().forEach(System.out::println);
        System.out.println("Удаляем ресторан с id=3 и проверяем, что его оценки были удалены");
        restaurantService.remove(restaurantService.findById(3L));
        System.out.println("Рестораны:");
        restaurantService.findAll().forEach(System.out::println);
        System.out.println("Оценки:");
        ratingService.findAll().forEach(System.out::println);
    }
}
