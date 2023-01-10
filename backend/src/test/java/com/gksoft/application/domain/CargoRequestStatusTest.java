package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CargoRequestStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CargoRequestStatus.class);
        CargoRequestStatus cargoRequestStatus1 = new CargoRequestStatus();
        cargoRequestStatus1.setId(1L);
        CargoRequestStatus cargoRequestStatus2 = new CargoRequestStatus();
        cargoRequestStatus2.setId(cargoRequestStatus1.getId());
        assertThat(cargoRequestStatus1).isEqualTo(cargoRequestStatus2);
        cargoRequestStatus2.setId(2L);
        assertThat(cargoRequestStatus1).isNotEqualTo(cargoRequestStatus2);
        cargoRequestStatus1.setId(null);
        assertThat(cargoRequestStatus1).isNotEqualTo(cargoRequestStatus2);
    }
}
