package com.example.MediaSoftSpring.controllers;

import com.example.MediaSoftSpring.dto.VisitorRequestDTO;
import com.example.MediaSoftSpring.dto.VisitorResponseDTO;
import com.example.MediaSoftSpring.services.VisitorService;
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
@RequestMapping("/api/users")
@Tag(name = "VisitorController", description = "Контроллер для посетителей")
@Validated
public class VisitorController {
    private final VisitorService service;

    @Autowired
    public VisitorController(VisitorService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Вывод всех посетителей", description = "Возвращает список всех посетителей")
    public ResponseEntity<List<VisitorResponseDTO>> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Поиск посетителя по id", description = "Возвращает информацию о посетителе с конкретным id")
    public ResponseEntity<VisitorResponseDTO> findById(@PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
           @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Добавление посетителя", description = "Создаёт нового посетителя, возвращает true при успешном создании, иначе false")
    public ResponseEntity<?> save(@RequestBody @Valid @Parameter(description = "DTO с данными посетителя для ввода", required = true) VisitorRequestDTO dto){
        service.save(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Изменение данных посетителя", description = "Изменяет объект посетителя, возвращает true при успешном изменении, иначе false")
    public ResponseEntity<?> update(@PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
              @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id,
              @RequestBody @Valid @Parameter(description = "DTO с данными посетителя для ввода", required = true) VisitorRequestDTO dto){
        service.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление посетителя", description = "Удаляет объект посетителя, возвращает true при успешном удалении, иначе false")
    public ResponseEntity<?> delete(@PathVariable @Parameter(description = "Идентификатор посетителя", required = true)
           @Positive(message = "Id должен быть больше 0") @NotNull(message = "Id не может быть null") Long id){
        service.removeById(id);
        return ResponseEntity.ok().build();
    }
}
