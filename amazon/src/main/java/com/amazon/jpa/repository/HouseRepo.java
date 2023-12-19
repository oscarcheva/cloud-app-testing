package com.amazon.jpa.repository;

import com.amazon.jpa.entity.HouseEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepo extends R2dbcRepository<HouseEntity, Long> {
}
