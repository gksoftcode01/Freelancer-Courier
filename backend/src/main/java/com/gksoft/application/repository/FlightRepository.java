package com.gksoft.application.repository;

import com.gksoft.application.domain.Flight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Flight entity.
 *
 * When extending this class, extend FlightRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface FlightRepository extends FlightRepositoryWithBagRelationships, JpaRepository<Flight, Long> {
    default Optional<Flight> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Flight> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Flight> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct flight from Flight flight left join fetch flight.fromCountry left join fetch flight.toCountry left join fetch flight.fromState left join fetch flight.toState left join fetch flight.fromCity left join fetch flight.toCity",
        countQuery = "select count(distinct flight) from Flight flight"
    )
    Page<Flight> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct flight from Flight flight left join fetch flight.fromCountry left join fetch flight.toCountry left join fetch flight.fromState left join fetch flight.toState left join fetch flight.fromCity left join fetch flight.toCity"
    )
    List<Flight> findAllWithToOneRelationships();

    @Query(
        "select flight from Flight flight left join fetch flight.fromCountry left join fetch flight.toCountry left join fetch flight.fromState left join fetch flight.toState left join fetch flight.fromCity left join fetch flight.toCity where flight.id =:id"
    )
    Optional<Flight> findOneWithToOneRelationships(@Param("id") Long id);
}
