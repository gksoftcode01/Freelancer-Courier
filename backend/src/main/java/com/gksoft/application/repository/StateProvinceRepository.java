package com.gksoft.application.repository;

import com.gksoft.application.domain.StateProvince;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the StateProvince entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StateProvinceRepository extends JpaRepository<StateProvince, Long> {}
