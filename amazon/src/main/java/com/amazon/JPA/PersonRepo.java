package com.amazon.JPA;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface PersonRepo extends R2dbcRepository<PersonEntity, Long> {

}
