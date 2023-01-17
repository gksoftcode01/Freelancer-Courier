package com.gksoft.application.repository;

import com.gksoft.application.domain.UserRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the UserRate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRateRepository extends JpaRepository<UserRate, Long> {
    @Query(value = " select * from user_rate where (:cargoId is null or CARGO_REQUEST_ID = :cargoId) " +
        " and  (:userId is null or USER_ID = :userId) " +
        " and  (:isCourier is null or IS_COURIER = :isCourier) ",nativeQuery = true)
    Page<UserRate> srchRates(@Param("cargoId") Long cargoId,@Param("userId") Long userId,
                             @Param("isCourier")  Long isCourier, Pageable pageable);
    List<UserRate> findByUserIdAndIsCourier(Long userId,Integer isCourier);
    List<UserRate> findByCargoRequestId (Long cargoRequestId);
}
