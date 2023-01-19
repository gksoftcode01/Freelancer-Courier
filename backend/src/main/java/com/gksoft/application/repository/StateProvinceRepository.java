package com.gksoft.application.repository;

import com.gksoft.application.domain.StateProvince;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for the StateProvince entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateProvinceRepository extends JpaRepository<StateProvince, Long> {
     List<StateProvince> findAllByCountryId( Long countryId);
}
