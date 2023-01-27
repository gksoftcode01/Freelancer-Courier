package com.gksoft.application.repository;

import com.gksoft.application.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findOneByActivationKey(String activationKey);
    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
    Optional<User> findOneByResetKey(String resetKey);
    Optional<User> findOneByEmailIgnoreCase(String email);
    Optional<User> findOneByLogin(String login);

    @EntityGraph(attributePaths = {"authorities","country"})
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByLogin(String login);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<User> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
    @Modifying
    @Query(value = "update JHI_USER set AVG_RATE_COURIER = (select avg(rate) from user_rate where USER_ID = :userId and " +
        " is_courier = 1 ) " +
        "where ID = :userId ",nativeQuery = true)
    void updateCourierAvgRate(@Param("userId") Long userId);

    @Modifying
    @Query(value = "update JHI_USER set total_rate_courier = (select count(rate) from user_rate where USER_ID = :userId and " +
        " is_courier = 1) " +
        "where ID = :userId ",nativeQuery = true)
    void updateCouriertotalRate(@Param("userId") Long userId);

    @Modifying
    @Query(value = "update JHI_USER set avg_rate_client = (select avg(rate) from user_rate where USER_ID = :userId and " +
        " is_courier = 0) " +
        "where ID = :userId ",nativeQuery = true)
    void updateClientAvgRate(@Param("userId") Long userId);
    @Modifying
    @Query(value = "update JHI_USER set total_rate_client = (select count(rate) from user_rate where USER_ID = :userId and " +
        " is_courier = 0) " +
        "where ID = :userId ",nativeQuery = true)
    void updateClienttotalRate(@Param("userId") Long userId);


}
