package com.amazon.jpa;

import com.amazon.jpa.dto.PersonDTO;
import com.amazon.jpa.entity.PersonEntity;
import com.amazon.jpa.repository.PersonRepo;
import com.amazon.jpa.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private final String GROOT = "I AM GROOT";

    @Mock
    PersonRepo personRepo;

    @InjectMocks
    PersonService personService;

    PersonEntity personEntity = PersonEntity.builder()
            .id(1L)
            .name(GROOT)
            .lastName(GROOT + 1)
            .build();

    PersonEntity personEntity2 = PersonEntity.builder()
            .id(2L)
            .name(GROOT)
            .lastName(GROOT + 1)
            .build();

    PersonEntity personEntity3 = PersonEntity.builder()
            .id(3L)
            .name(GROOT)
            .lastName(GROOT + 1)
            .build();

    PersonDTO personDTO1 = PersonDTO.builder()
            .name(GROOT)
            .lastName(GROOT + 1)
            .build();


    @Test
    void getPersonTest() {

        when(personRepo.findById(anyLong())).thenReturn(Mono.just(personEntity));

        StepVerifier.create(personService.getPerson(10L))
                .consumeNextWith(currentDTO -> {
                    Assertions.assertEquals(currentDTO.getId(), personEntity.getId());
                    Assertions.assertEquals(currentDTO.getName(), personDTO1.getName());
                    Assertions.assertEquals(currentDTO.getLastName(), personDTO1.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void getAllPersonsTest() {

        when(personRepo.findAll()).thenReturn(Flux.fromIterable(List.of(personEntity, personEntity2, personEntity3)));

        StepVerifier.create(personService.getAllPersons())
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void createPersonTest() {

        when(personRepo.save(any())).thenReturn(Mono.just(personEntity));

        StepVerifier.create(personService.createPerson(personDTO1))
                .consumeNextWith(currentDTO -> {
                    Assertions.assertEquals(currentDTO.getId(), personEntity.getId());
                    Assertions.assertEquals(currentDTO.getName(), personDTO1.getName());
                    Assertions.assertEquals(currentDTO.getLastName(), personDTO1.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void updatePersonTest() {

        when(personRepo.findById(anyLong())).thenReturn(Mono.just(personEntity));
        when(personRepo.save(any())).thenReturn(Mono.just(personEntity));

        StepVerifier.create(personService.updatePerson(1L, personDTO1))
                .consumeNextWith(currentDTO -> {
                    Assertions.assertEquals(currentDTO.getId(), personEntity.getId());
                    Assertions.assertEquals(currentDTO.getName(), personDTO1.getName());
                    Assertions.assertEquals(currentDTO.getLastName(), personDTO1.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void deletePersonTest() {

        when(personRepo.findById(anyLong())).thenReturn(Mono.just(personEntity));
        when(personRepo.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(personService.deletePerson(1L))
                .consumeNextWith(currentDTO -> {
                    Assertions.assertEquals(currentDTO.getId(), personEntity.getId());
                    Assertions.assertEquals(currentDTO.getName(), personDTO1.getName());
                    Assertions.assertEquals(currentDTO.getLastName(), personDTO1.getLastName());
                })
                .verifyComplete();
    }


}
