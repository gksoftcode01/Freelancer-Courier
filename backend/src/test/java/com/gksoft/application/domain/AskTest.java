package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ask.class);
        Ask ask1 = new Ask();
        ask1.setId(1L);
        Ask ask2 = new Ask();
        ask2.setId(ask1.getId());
        assertThat(ask1).isEqualTo(ask2);
        ask2.setId(2L);
        assertThat(ask1).isNotEqualTo(ask2);
        ask1.setId(null);
        assertThat(ask1).isNotEqualTo(ask2);
    }
}
