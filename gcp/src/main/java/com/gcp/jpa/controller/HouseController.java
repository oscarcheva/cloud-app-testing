package com.gcp.jpa.controller;


import com.gcp.jpa.dto.HouseDTO;
import com.gcp.jpa.service.HouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

@RestController
@RequestMapping("/v1/house")
@RequiredArgsConstructor

public class HouseController {

    private final HouseService houseService;

    @GetMapping("/{id}")
    public Mono<HouseDTO> getHouse(@PathVariable Long id){
        return houseService.getHouse(id);
    }

    @GetMapping("/all")
    public ParallelFlux<HouseDTO>getAllHouses(){
        return houseService.getAllHouses();
    }

    @PostMapping()
    public Mono<HouseDTO> createHouse(@Validated @RequestBody HouseDTO houseDTO){
        return houseService.createHouse(houseDTO);
    }

    @PutMapping()
    public Mono<HouseDTO> updateHouse(@Validated @RequestBody HouseDTO houseDTO){
        return houseService.updateHouse(houseDTO);
    }

    @DeleteMapping("/{id}")
    public Mono<HouseDTO> updateHouse(@PathVariable Long id){
        return houseService.deleteHouse(id);
    }

}
