package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.repositories.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    private CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Transactional
    public CarModel save(CarModel carModel) {
        return carRepository.save(carModel);
    }

    public Page<CarModel> findAll(Pageable pages) {
        return carRepository.findAll(pages);
    }

    public Optional<CarModel> findById(UUID id) {
        return carRepository.findById(id);
    }

    @Transactional
    public void delete(CarModel carModel) {
        carRepository.delete(carModel);
    }
}
