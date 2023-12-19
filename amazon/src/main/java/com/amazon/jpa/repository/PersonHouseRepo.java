package com.amazon.jpa.repository;

import com.amazon.jpa.entity.PersonHouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonHouseRepo extends R2dbcRepository<PersonHouseEntity,Long> {

    Flux<PersonHouseEntity> findAllByHouseId(Long id);
}
