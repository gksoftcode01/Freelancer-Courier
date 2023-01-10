package com.gksoft.application.repository;

import com.gksoft.application.domain.CargoRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CargoRequestRepositoryWithBagRelationships {
    Optional<CargoRequest> fetchBagRelationships(Optional<CargoRequest> cargoRequest);

    List<CargoRequest> fetchBagRelationships(List<CargoRequest> cargoRequests);

    Page<CargoRequest> fetchBagRelationships(Page<CargoRequest> cargoRequests);
}
