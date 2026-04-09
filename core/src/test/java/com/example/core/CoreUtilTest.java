package com.example.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreUtilTest {
    @Test
    void getMessage() {
        assertEquals("Hello from Core", CoreUtil.getMessage());
    }
}
