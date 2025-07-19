package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.RestaurantRequestDTO;
import com.example.MediaSoftSpring.dto.RestaurantResponseDTO;
import com.example.MediaSoftSpring.services.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "RestaurantController", description = "Контроллер для ресторанов")
@Validated
public class RestaurantController {
    private final RestaurantService service;

    @Autowired
    public RestaurantController(RestaurantService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Вывод всех ресторанов", description = "Возвращает список всех ресторанов")
    public ResponseEntity<List<RestaurantResponseDTO>> findAll(){
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск ресторана по id", description = "Возвращает информацию о ресторане с конкретным id")
    public ResponseEntity<RestaurantResponseDTO> findById(@PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
           @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/rating")
    @Operation(summary = "Поиск ресторанов с оценкой, не ниже заданной",
            description = "Возвращает информацию о ресторанах, у которых оценка не меньше заданной в параметре запроса")
    public ResponseEntity<List<RestaurantResponseDTO>> findByRating(@RequestParam(defaultValue = "0")
                             @Parameter(description = "Оценка ресторана") BigDecimal rating){
        try{
            return ResponseEntity.ok(service.findByRating(rating));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @Operation(summary = "Добавление ресторана", description = "Создаёт новый ресторан, возвращает true при успешном создании, иначе false")
    public ResponseEntity<?> save(@RequestBody @Valid @Parameter(description = "DTO с данными ресторана для ввода", required = true) RestaurantRequestDTO dto){
        try {
            service.save(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение данных ресторана", description = "Изменяет объект ресторана, возвращает true при успешном изменении, иначе false")
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
           @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id,
           @RequestBody @Valid @Parameter(description = "DTO с данными ресторана для ввода", required = true) RestaurantRequestDTO dto){
        try {
            service.update(id, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление ресторана", description = "Удаляет объект ресторана, возвращает true при успешном удалении, иначе false")
    public ResponseEntity<?> delete(@PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
           @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        try {
            service.removeById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
