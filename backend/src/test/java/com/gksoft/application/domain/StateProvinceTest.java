package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateProvinceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateProvince.class);
        StateProvince stateProvince1 = new StateProvince();
        stateProvince1.setId(1L);
        StateProvince stateProvince2 = new StateProvince();
        stateProvince2.setId(stateProvince1.getId());
        assertThat(stateProvince1).isEqualTo(stateProvince2);
        stateProvince2.setId(2L);
        assertThat(stateProvince1).isNotEqualTo(stateProvince2);
        stateProvince1.setId(null);
        assertThat(stateProvince1).isNotEqualTo(stateProvince2);
    }
}
