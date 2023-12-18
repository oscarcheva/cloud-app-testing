package com.amazon.JPA;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;
    @GetMapping("/person/{id}")
    public Mono<PersonDTO>getPerson(@PathVariable Long id){
        return personService.getPerson(id);
    }

    @GetMapping("/person/all")
    public ParallelFlux<PersonDTO> getAllPersons(){
        return personService.getAllPersons();
    }

    @PostMapping("/person")
    public Mono<PersonDTO>createPerson(@Validated @RequestBody PersonDTO personDTO){
        return personService.createPerson(personDTO);
    }

    @PutMapping("/person/{id}")
    public Mono<PersonDTO>updatePerson(@PathVariable long id, @Validated @RequestBody PersonDTO personDTO){
        return personService.updatePerson(id, personDTO);
    }

    @DeleteMapping("/person/{id}")
    public Mono<PersonDTO>deletePerson(@PathVariable Long id){
        return personService.deletePerson(id);
    }}
