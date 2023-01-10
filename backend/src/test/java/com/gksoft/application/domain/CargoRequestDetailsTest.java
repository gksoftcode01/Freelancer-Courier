package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoRequestDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequestDetails.class);
        CargoRequestDetails cargoRequestDetails1 = new CargoRequestDetails();
        cargoRequestDetails1.setId(1L);
        CargoRequestDetails cargoRequestDetails2 = new CargoRequestDetails();
        cargoRequestDetails2.setId(cargoRequestDetails1.getId());
        assertThat(cargoRequestDetails1).isEqualTo(cargoRequestDetails2);
        cargoRequestDetails2.setId(2L);
        assertThat(cargoRequestDetails1).isNotEqualTo(cargoRequestDetails2);
        cargoRequestDetails1.setId(null);
        assertThat(cargoRequestDetails1).isNotEqualTo(cargoRequestDetails2);
    }
}
