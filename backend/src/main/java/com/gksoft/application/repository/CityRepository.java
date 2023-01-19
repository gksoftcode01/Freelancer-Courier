package com.gksoft.application.repository;

import com.gksoft.application.domain.City;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the City entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findAllByStateId(Long stateId);
}
