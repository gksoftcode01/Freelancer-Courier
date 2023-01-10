package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CargoRequest entity.
 *
 * When extending this class, extend CargoRequestRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface CargoRequestRepository extends CargoRequestRepositoryWithBagRelationships, JpaRepository<CargoRequest, Long> {
    default Optional<CargoRequest> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<CargoRequest> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<CargoRequest> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.status left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState left join fetch cargoRequest.fromCity left join fetch cargoRequest.toCity",
        countQuery = "select count(distinct cargoRequest) from CargoRequest cargoRequest"
    )
    Page<CargoRequest> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.status left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState left join fetch cargoRequest.fromCity left join fetch cargoRequest.toCity"
    )
    List<CargoRequest> findAllWithToOneRelationships();

    @Query(
        "select cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.status left join fetch cargoRequest.fromCountry left join fetch cargoRequest.toCountry left join fetch cargoRequest.fromState left join fetch cargoRequest.toState left join fetch cargoRequest.fromCity left join fetch cargoRequest.toCity where cargoRequest.id =:id"
    )
    Optional<CargoRequest> findOneWithToOneRelationships(@Param("id") Long id);
}
