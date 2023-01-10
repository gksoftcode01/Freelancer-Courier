package com.gksoft.application.repository;

import com.gksoft.application.domain.Flight;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface FlightRepositoryWithBagRelationships {
    Optional<Flight> fetchBagRelationships(Optional<Flight> flight);

    List<Flight> fetchBagRelationships(List<Flight> flights);

    Page<Flight> fetchBagRelationships(Page<Flight> flights);
}
