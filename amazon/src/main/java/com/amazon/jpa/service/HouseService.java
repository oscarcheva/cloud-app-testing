package com.amazon.jpa.service;

import com.amazon.jpa.dto.HouseDTO;
import com.amazon.jpa.entity.HouseEntity;
import com.amazon.jpa.repository.HouseRepo;
import com.amazon.jpa.repository.PersonHouseRepo;
import com.amazon.jpa.repository.PersonRepo;
import com.amazon.error.AmazonException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepo houseRepo;

    private final PersonRepo personRepo;

    private final PersonHouseRepo personHouseRepo;

    private static final String NOT_FOUND = "Not found";

    ModelMapper modelMapper = new ModelMapper();

    public Mono<HouseDTO> getHouse(Long id)
    {
        return houseRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .flatMap(this::getHouseWithPerson)
                .onErrorResume(this::handleError);
    }

    public ParallelFlux<HouseDTO> getAllHouses(){
        return houseRepo.findAll()
                .parallel()
                .flatMap(this::getHouseWithPerson)
                .runOn(Schedulers.boundedElastic());
    }

    private Mono<HouseDTO>getHouseWithPerson(HouseEntity houseEntity){

        return Mono.just(houseEntity)
                .zipWhen(entity -> personHouseRepo.findAllByHouseId(houseEntity.getId()).collectList())
                .map(Tuple2::getT2)
                .flatMapMany(Flux::fromIterable)
                .flatMap(personHouseEntity ->
                        personRepo.findById(personHouseEntity.getPersonId()))
                .collectList()
                .map(personEntity -> {
                    houseEntity.setPersons(new ArrayList<>());
                    personEntity.forEach(x-> houseEntity.getPersons().add(x));
                    return houseEntity;
                })
                .map(v -> modelMapper.map(houseEntity,HouseDTO.class));
    }

    public Mono<HouseDTO> createHouse(HouseDTO houseDTO){
        return Mono.just(houseDTO)
                .flatMap( house ->{
                    var houseEntity = modelMapper.map(house, HouseEntity.class);
                    return houseRepo.save(houseEntity);
                })
                .map(v -> modelMapper.map(v, HouseDTO.class));
    }

    public Mono<HouseDTO> updateHouse(HouseDTO houseDTO){
        return houseRepo.findById(houseDTO.getId())
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .flatMap( house ->{
                    var houseEntity = modelMapper.map(house, HouseEntity.class);
                    return houseRepo.save(houseEntity);
                })
                .map(v -> modelMapper.map(v, HouseDTO.class));

    }

    public Mono<HouseDTO> deleteHouse(Long id){
        return houseRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .delayUntil( house ->{
                    var houseEntity = modelMapper.map(house, HouseEntity.class);
                    return houseRepo.delete(houseEntity);
                })
                .map(v -> modelMapper.map(v, HouseDTO.class));    }

    private <T> Mono <T>  handleError(Throwable throwable){
        return Mono.error(throwable);
    }

}
