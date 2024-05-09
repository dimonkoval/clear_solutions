package org.example.util;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class SortingTest {
    private static final String PARAMETER_ID = "id";
    private static final String PARAMETER_ASC = "ASC";
    private static final String PARAMETER_ID_ASC = "id:ASC";

    @Test
    void getSortFromRequestParam_Ok() {
        Sort actual = Sorting.getSortFromRequestParam(PARAMETER_ID_ASC);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertEquals(PARAMETER_ASC, Objects.requireNonNull(actual.getOrderFor(PARAMETER_ID)).getDirection().name());
    }
}