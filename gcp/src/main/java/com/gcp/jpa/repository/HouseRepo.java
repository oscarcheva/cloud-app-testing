package com.gcp.jpa.repository;

import com.gcp.jpa.entity.HouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseRepo extends JpaRepository<HouseEntity, Long> {
}
