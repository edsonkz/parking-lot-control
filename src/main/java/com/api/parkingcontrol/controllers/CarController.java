package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.CarDto;
import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.services.CarService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/cars")
public class CarController {

    final CarService carService;

    public CarController(CarService carService){
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid CarDto carDto){
        var carModel = new CarModel();
        BeanUtils.copyProperties(carDto, carModel);
        carModel.setRegistrationDate(LocalDate.now(ZoneId.of("UTC")));
        carService.save(carModel);

        var json = convertToJSON(carModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }

    @GetMapping
    public ResponseEntity<Page<CarModel>> getAll(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pages){
        return ResponseEntity.status(HttpStatus.OK).body(carService.findAll(pages));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable(value = "id") UUID id){
        Optional<CarModel> carModelOptional = carService.findById(id);
        if(!carModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        return ResponseEntity.status(HttpStatus.OK).body(carModelOptional.get());
    }

    @DeleteMapping
    public ResponseEntity<Object> delete(@PathVariable(value = "id") UUID id){
        Optional<CarModel> carModelOptional = carService.findById(id);
        if(!carModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Car not found.");
        }

        carService.delete(carModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(carModelOptional.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") UUID id,
                                         @RequestBody @Valid CarDto carDto) {
        Optional<CarModel> carModelOptional = carService.findById(id);
        if (!carModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
        }
        var carModel = new CarModel();
        BeanUtils.copyProperties(carDto, carModel);
        carModel.setId(carModelOptional.get().getId());
        carModel.setRegistrationDate(carModelOptional.get().getRegistrationDate());
        return ResponseEntity.status(HttpStatus.OK).body(carService.save(carModel));
    }

    public String convertToJSON(CarModel object){
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        try {
            String json = mapper.writeValueAsString(object);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
