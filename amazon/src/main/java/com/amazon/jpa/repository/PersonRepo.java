package com.amazon.jpa.repository;

import com.amazon.jpa.entity.PersonEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepo extends R2dbcRepository<PersonEntity, Long> {

}
