package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.CarDto;
import com.api.parkingcontrol.models.CarModel;
import com.api.parkingcontrol.repositories.CarRepository;
import com.api.parkingcontrol.services.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarController carController;

    @MockBean
    private CarRepository carRepository;

    @MockBean
    private CarService carService;

    @MockBean
    private CarModel carModel;

    @Test
    void create() throws Exception{
        var newCar = new CarDto();
        newCar.setBrand("x");
        newCar.setColor("white");
        newCar.setModel("xx");
        newCar.setLicensePlate("2445FGR");

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var body = new JSONObject(ow.writeValueAsString(newCar));

        RequestBuilder request = MockMvcRequestBuilders.post("/cars").contentType(MediaType.APPLICATION_JSON).content(body.toString());
        var response = mockMvc.perform(request).andReturn();
        assertEquals(400, response.getResponse().getContentAsString());
    }

    @Test
    void getAll() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/cars").contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(status().isOk()).andReturn();
    }

    @Test
    void getById() {
    }

    @Test
    void delete() {
    }

    @Test
    void update() {
    }
}