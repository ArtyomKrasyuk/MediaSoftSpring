package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.RatingPostRequestDTO;
import com.example.MediaSoftSpring.dto.RatingPutRequestDTO;
import com.example.MediaSoftSpring.dto.RatingResponseDTO;
import com.example.MediaSoftSpring.services.RatingService;
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

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "RatingController", description = "Контроллер для оценок")
@Validated
public class RatingController {
    private final RatingService service;

    @Autowired
    public RatingController(RatingService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Вывод всех оценок", description = "Возвращает список всех оценок")
    public ResponseEntity<List<RatingResponseDTO>> findAll(){
        try {
            return ResponseEntity.ok(service.findAll());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Поиск оценки по id", description = "Возвращает информацию об оценке с конкретным id")
    public ResponseEntity<RatingResponseDTO> findById(@PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
          @Positive(message = "Id посетителя должен быть больше 0") @NotNull(message = "Id посетителя не может быть null") Long visitorId,
          @PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
          @Positive(message = "Id ресторана должен быть больше 0") @NotNull(message = "Id ресторана не может быть null") Long restaurantId){
        try {
            return ResponseEntity.ok(service.findById(visitorId, restaurantId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/sorting")
    @Operation(summary = "Поиск оценок с сортировкой", description = "Сортировка оценок по убыванию")
    public ResponseEntity<List<RatingResponseDTO>> findAllSorting(){
        try{
            return ResponseEntity.ok(service.findAllSorting());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/pageable")
    @Operation(summary = "Постраничный поииск оценок",
            description = "Постраничный поиск и сортировка")
    public ResponseEntity<List<RatingResponseDTO>> findAllPageable(
            @RequestParam(defaultValue = "0") @Parameter(description = "Номер страницы") int page,
            @RequestParam(defaultValue = "5") @Parameter(description = "Количество элементов") int size,
            @RequestParam(defaultValue = "false") @Parameter(description = "Нужна ли сортировка") boolean sort
    )
    {
        try{
            if(sort) return ResponseEntity.ok(service.findAllPageableAndSorting(page, size));
            else return ResponseEntity.ok(service.findAllPageable(page, size));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping
    @Operation(summary = "Добавление оценки", description = "Создаёт новую оценку, возвращает true при успешном создании, иначе false")
    public ResponseEntity<?> save(@RequestBody @Valid @Parameter(description = "DTO с данными оценки для ввода", required = true) RatingPostRequestDTO dto){
        try {
            service.save(dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Изменение данных оценки", description = "Изменяет объект оценки, возвращает true при успешном изменении, иначе false")
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
               @Positive(message = "Id посетителя должен быть больше 0") @NotNull(message = "Id посетителя не может быть null") Long visitorId,
               @PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
               @Positive(message = "Id ресторана должен быть больше 0") @NotNull(message = "Id ресторана не может быть null") Long restaurantId,
               @RequestBody @Valid @Parameter(description = "DTO с данными оценки для ввода", required = true) RatingPutRequestDTO dto){
        try {
            service.update(visitorId, restaurantId, dto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{visitorId}/{restaurantId}")
    @Operation(summary = "Удаление оценки", description = "Удаляет объект оценки, возвращает true при успешном удалении, иначе false")
    public ResponseEntity<?> delete(
            @PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
            @Positive(message = "Id посетителя должен быть больше 0") @NotNull(message = "Id посетителя не может быть null") Long visitorId,
            @PathVariable @Parameter(description = "Идентификатор ресторана", required = true)
            @Positive(message = "Id ресторана должен быть больше 0") @NotNull(message = "Id ресторана не может быть null") Long restaurantId
    )
    {
        try {
            service.removeById(visitorId, restaurantId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
