package com.gksoft.application.repository;

import com.gksoft.application.domain.Bid;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Bid entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {}
