package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class CargoRequestRepositoryWithBagRelationshipsImpl implements CargoRequestRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<CargoRequest> fetchBagRelationships(Optional<CargoRequest> cargoRequest) {
        return cargoRequest.map(this::fetchReqItemTypes);
    }

    @Override
    public Page<CargoRequest> fetchBagRelationships(Page<CargoRequest> cargoRequests) {
        return new PageImpl<>(
            fetchBagRelationships(cargoRequests.getContent()),
            cargoRequests.getPageable(),
            cargoRequests.getTotalElements()
        );
    }

    @Override
    public List<CargoRequest> fetchBagRelationships(List<CargoRequest> cargoRequests) {
        return Optional.of(cargoRequests).map(this::fetchReqItemTypes).orElse(Collections.emptyList());
    }

    CargoRequest fetchReqItemTypes(CargoRequest result) {
        return entityManager
            .createQuery(
                "select cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.reqItemTypes where cargoRequest is :cargoRequest",
                CargoRequest.class
            )
            .setParameter("cargoRequest", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<CargoRequest> fetchReqItemTypes(List<CargoRequest> cargoRequests) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, cargoRequests.size()).forEach(index -> order.put(cargoRequests.get(index).getId(), index));
        List<CargoRequest> result = entityManager
            .createQuery(
                "select distinct cargoRequest from CargoRequest cargoRequest left join fetch cargoRequest.reqItemTypes where cargoRequest in :cargoRequests",
                CargoRequest.class
            )
            .setParameter("cargoRequests", cargoRequests)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
