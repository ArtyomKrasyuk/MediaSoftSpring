package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.RatingRequestDTO;
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
    public List<RatingResponseDTO> findAll(){
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск оценки по id", description = "Возвращает информацию об оценке с конкретным id")
    public RatingResponseDTO findById(@PathVariable @Parameter(description = "Идентификатор оценки", required = true)
          @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        return service.findById(id);
    }

    @PostMapping
    @Operation(summary = "Добавление оценки", description = "Создаёт новую оценку, возвращает true при успешном создании, иначе false")
    public ResponseEntity<Boolean> save(@RequestBody @Valid @Parameter(description = "DTO с данными оценки для ввода", required = true) RatingRequestDTO dto){
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение данных оценки", description = "Изменяет объект оценки, возвращает true при успешном изменении, иначе false")
    public ResponseEntity<Boolean> update(@PathVariable @Parameter(description = "Идентификатор оценки", required = true)
               @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id,
               @RequestBody @Valid @Parameter(description = "DTO с данными оценки для ввода", required = true) RatingRequestDTO dto){
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление оценки", description = "Удаляет объект оценки, возвращает true при успешном удалении, иначе false")
    public ResponseEntity<Boolean> delete(@PathVariable @Parameter(description = "Идентификатор оценки", required = true)
        @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        return ResponseEntity.ok(service.removeById(id));
    }
}
