package com.gcp.jpa.service;

import com.gcp.error.GoogleException;
import com.gcp.jpa.dto.PersonDTO;
import com.gcp.jpa.entity.PersonEntity;
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
public class PersonService {
    private static final String NOT_FOUND = "Not found";
    private final PersonRepo personRepo;
    private final HouseRepo houseRepo;
    ModelMapper modelMapper = new ModelMapper();

    public ParallelFlux<PersonDTO> getAllPersons() {
        return Mono.fromCallable(personRepo::findAll)
                .flatMapMany(Flux::fromIterable)
                .subscribeOn(Schedulers.boundedElastic())
                .parallel()
                .map(v -> modelMapper.map(v, PersonDTO.class))
                .runOn(Schedulers.boundedElastic())
                .doOnError(this::handleError);

    }

    public Mono<PersonDTO> getPerson(Long id) {
        return Mono.fromCallable(() -> personRepo.findById(id).orElse(new PersonEntity()))
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new GoogleException(NOT_FOUND)))
                .map(v -> modelMapper.map(v, PersonDTO.class))
                .onErrorResume(this::handleError);
    }


    public Mono<PersonDTO> createPerson(PersonDTO personDTO) {
        return Mono.just(personDTO)
                .subscribeOn(Schedulers.boundedElastic())
                .map(personDto -> modelMapper.map(personDto, PersonEntity.class))
                .map(personRepo::save)
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);

    }

    public Mono<PersonDTO> updatePerson(long id, PersonDTO personDTO) {
        return getPerson(id)
                .switchIfEmpty(Mono.error(new GoogleException(NOT_FOUND)))
                .map(dto -> {
                    dto.setHouse(personDTO.getHouse());
                    dto.setName(personDTO.getName());
                    dto.setLastName(personDTO.getLastName());
                    var entity = modelMapper.map(dto, PersonEntity.class);
                    entity.setId(id);
                    return entity;
                })
                .map(personRepo::save)
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    public Mono<PersonDTO> deletePerson(long id) {
        return getPerson(id)
                .subscribeOn(Schedulers.boundedElastic())
                .switchIfEmpty(Mono.error(new GoogleException(NOT_FOUND)))
                .map(personDTO -> modelMapper.map(personDTO, PersonEntity.class))
                .map(entity-> {
                    personRepo.delete(entity);
                    return  entity;
                })
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable throwable) {
        return Mono.error(throwable);
    }

}
