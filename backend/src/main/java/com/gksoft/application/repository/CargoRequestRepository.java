package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequest;
import java.util.List;
import java.util.Optional;

import com.gksoft.application.domain.UserRate;
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
public interface CargoRequestRepository extends CargoRequestRepositoryWithBagRelationships,
    JpaRepository<CargoRequest, Long> {

    @Query(value = " select * from cargo_request   where " +
        "(:fromCountry is null or FROM_COUNTRY_ID = :fromCountry) " +
        " and (:toCountry is null or TO_COUNTRY_ID = :toCountry) " +
        " and(:fromState is null or FROM_STATE_ID = :fromState) " +
        " and (:toState is null or TO_STATE_ID = :toState) " +
        " and  (:createBy is null or CREATE_BY_ID = :createBy) " +
        " and  (:statusId is null or STATUS_ID = :statusId) ",nativeQuery = true)
    Page<CargoRequest> srchCargoRequest(@Param("fromCountry") Long fromCountry, @Param("toCountry") Long toCountry,
                                        @Param("fromState")  Long fromState, @Param("toState") Long toState,
                                        @Param("createBy") Long createBy,
                                        @Param("statusId")  Long statusId, Pageable pageable);

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
