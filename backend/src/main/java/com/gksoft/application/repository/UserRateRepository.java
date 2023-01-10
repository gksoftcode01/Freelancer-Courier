package com.gksoft.application.repository;

import com.gksoft.application.domain.UserRate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRateRepository extends JpaRepository<UserRate, Long> {}
