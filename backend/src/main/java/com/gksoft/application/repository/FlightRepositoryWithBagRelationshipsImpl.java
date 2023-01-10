package com.gksoft.application.repository;

import com.gksoft.application.domain.Flight;
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
public class FlightRepositoryWithBagRelationshipsImpl implements FlightRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Flight> fetchBagRelationships(Optional<Flight> flight) {
        return flight.map(this::fetchAvailableItemTypes);
    }

    @Override
    public Page<Flight> fetchBagRelationships(Page<Flight> flights) {
        return new PageImpl<>(fetchBagRelationships(flights.getContent()), flights.getPageable(), flights.getTotalElements());
    }

    @Override
    public List<Flight> fetchBagRelationships(List<Flight> flights) {
        return Optional.of(flights).map(this::fetchAvailableItemTypes).orElse(Collections.emptyList());
    }

    Flight fetchAvailableItemTypes(Flight result) {
        return entityManager
            .createQuery("select flight from Flight flight left join fetch flight.availableItemTypes where flight is :flight", Flight.class)
            .setParameter("flight", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Flight> fetchAvailableItemTypes(List<Flight> flights) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, flights.size()).forEach(index -> order.put(flights.get(index).getId(), index));
        List<Flight> result = entityManager
            .createQuery(
                "select distinct flight from Flight flight left join fetch flight.availableItemTypes where flight in :flights",
                Flight.class
            )
            .setParameter("flights", flights)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
