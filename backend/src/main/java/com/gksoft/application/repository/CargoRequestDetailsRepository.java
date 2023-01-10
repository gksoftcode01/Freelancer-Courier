package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequestDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoRequestDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CargoRequestDetailsRepository extends JpaRepository<CargoRequestDetails, Long> {}
