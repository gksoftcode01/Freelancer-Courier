package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequest.class);
        CargoRequest cargoRequest1 = new CargoRequest();
        cargoRequest1.setId(1L);
        CargoRequest cargoRequest2 = new CargoRequest();
        cargoRequest2.setId(cargoRequest1.getId());
        assertThat(cargoRequest1).isEqualTo(cargoRequest2);
        cargoRequest2.setId(2L);
        assertThat(cargoRequest1).isNotEqualTo(cargoRequest2);
        cargoRequest1.setId(null);
        assertThat(cargoRequest1).isNotEqualTo(cargoRequest2);
    }
}
