package com.gcp.jpa.service;


import com.gcp.error.GoogleException;
import com.gcp.jpa.dto.HouseDTO;
import com.gcp.jpa.entity.HouseEntity;
import com.gcp.jpa.repository.HouseRepo;
import com.gcp.jpa.repository.PersonRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class HouseService {

    private static final String NOT_FOUND = "Not found";
    private final HouseRepo houseRepo;
    ModelMapper modelMapper = new ModelMapper();

    public Mono<HouseDTO> getHouse(Long id) {
        return Mono.fromCallable(() -> houseRepo.findById(id).orElse(new HouseEntity()))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new GoogleException(NOT_FOUND)))
                .map(houseEntity -> modelMapper.map(houseEntity, HouseDTO.class))
                .onErrorResume(this::handleError);
    }

    public ParallelFlux<HouseDTO> getAllHouses() {
        return Mono.fromCallable(houseRepo::findAll)
                .flatMapMany(Flux::fromIterable)
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map(houseEntity -> modelMapper.map(houseEntity, HouseDTO.class));
    }


    public Mono<HouseDTO> createHouse(HouseDTO houseDTO) {
        return Mono.just(houseDTO)
                .subscribeOn(Schedulers.boundedElastic())
                .map(dto -> modelMapper.map(dto, HouseEntity.class))
                .map(houseRepo::save)
                .map(v -> modelMapper.map(v, HouseDTO.class))
                .onErrorResume(this::handleError);

    }

    public Mono<HouseDTO> updateHouse(HouseDTO houseDTO) {
        return getHouse(houseDTO.getId())
                .subscribeOn(Schedulers.boundedElastic())
                .map(house -> {
                    var houseEntity = modelMapper.map(house, HouseEntity.class);
                    return houseRepo.save(houseEntity);
                })
                .map(v -> modelMapper.map(v, HouseDTO.class))
                .onErrorResume(this::handleError);


    }

    public Mono<HouseDTO> deleteHouse(Long id) {
        return getHouse(id)
                .subscribeOn(Schedulers.boundedElastic())
                .map(dto -> modelMapper.map(dto, HouseEntity.class))
                .map(entity -> {
                    houseRepo.delete(entity);
                    return entity;
                })
                .map(v -> modelMapper.map(v, HouseDTO.class))
                .onErrorResume(this::handleError);

    }

    private <T> Mono<T> handleError(Throwable throwable) {
        return Mono.error(throwable);
    }

}
