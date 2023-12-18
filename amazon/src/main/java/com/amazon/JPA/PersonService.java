package com.amazon.JPA;

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
    ModelMapper modelMapper = new ModelMapper();

    public ParallelFlux<PersonDTO> getAllPersons() {
        return personRepo.findAll()
                .parallel()
                .runOn(Schedulers.boundedElastic())
                .map(v -> modelMapper.map(v, PersonDTO.class))
                .doOnError(this::handleError);

    }

    public Mono<PersonDTO> getPerson(Long id) {
        return personRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
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
        return personRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .flatMap(entity-> {
                    entity = modelMapper.map(personDTO, PersonEntity.class);
                    entity.setId(id);
                    return personRepo.save(entity);
                })
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    public Mono<PersonDTO> deletePerson(long id) {
        return personRepo.findById(id)
                .switchIfEmpty(Mono.error(new AmazonException(NOT_FOUND)))
                .delayUntil(personRepo::delete)
                .map(entity -> modelMapper.map(entity, PersonDTO.class))
                .onErrorResume(this::handleError);
    }

    private <T> Mono<T> handleError(Throwable throwable) {
        return Mono.error(throwable);
    }

}
