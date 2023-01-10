package com.gksoft.application.repository;

import com.gksoft.application.domain.Ask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AskRepository extends JpaRepository<Ask, Long> {}
