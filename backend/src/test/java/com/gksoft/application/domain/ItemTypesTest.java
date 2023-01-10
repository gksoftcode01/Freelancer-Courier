package com.gksoft.application.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.gksoft.application.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemTypesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemTypes.class);
        ItemTypes itemTypes1 = new ItemTypes();
        itemTypes1.setId(1L);
        ItemTypes itemTypes2 = new ItemTypes();
        itemTypes2.setId(itemTypes1.getId());
        assertThat(itemTypes1).isEqualTo(itemTypes2);
        itemTypes2.setId(2L);
        assertThat(itemTypes1).isNotEqualTo(itemTypes2);
        itemTypes1.setId(null);
        assertThat(itemTypes1).isNotEqualTo(itemTypes2);
    }
}
