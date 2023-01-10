package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequestStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoRequestStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargoRequestStatusRepository extends JpaRepository<CargoRequestStatus, Long> {}
