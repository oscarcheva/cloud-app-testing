package com.amazon.jpa.service;

import com.amazon.jpa.dto.PersonDTO;
import com.amazon.jpa.entity.PersonEntity;
import com.amazon.jpa.repository.HouseRepo;
import com.amazon.jpa.repository.PersonRepo;
import com.amazon.error.AmazonException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
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
        return personRepo.findAll()
                .parallel()
                .flatMap(this::getPersonWithHouse)
                .runOn(Schedulers.boundedElastic())
                .doOnError(this::handleError);

    }

    public Mono<PersonDTO> getPerson(Long id) {
        return personRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .flatMap(this::getPersonWithHouse)
                .onErrorResume(this::handleError);
    }

    private Mono<PersonDTO> getPersonWithHouse(PersonEntity personEntity){
        return Mono.just(personEntity)
                .zipWhen(entity -> houseRepo.findById(entity.getHouse_id()))
                .map(personAndHouse ->{
                    personEntity.setHouse(personAndHouse.getT2());
                    return personEntity;
                })
                .map(v -> modelMapper.map(v, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    public Mono<PersonDTO> createPerson(PersonDTO personDTO) {
        return Mono.just(personDTO)
                .map(personDto -> modelMapper.map(personDto, PersonEntity.class))
                .flatMap(personRepo::save)
                .map(entity -> modelMapper.map(entity, PersonDTO.class));

    }

    public Mono<PersonDTO> updatePerson(long id, PersonDTO personDTO) {
        return getPerson(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .flatMap(dto-> {
                    dto.setHouse(personDTO.getHouse());
                    dto.setName(personDTO.getName());
                    dto.setLastName(personDTO.getLastName());
                    var entity = modelMapper.map(dto, PersonEntity.class);
                    entity.setId(id);
                    return personRepo.save(entity);
                })
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    public Mono<PersonDTO> deletePerson(long id) {
        return getPerson(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .map(personDTO -> modelMapper.map(personDTO, PersonEntity.class))
                .delayUntil(personRepo::delete)
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable throwable) {
        return Mono.error(throwable);
    }

}
