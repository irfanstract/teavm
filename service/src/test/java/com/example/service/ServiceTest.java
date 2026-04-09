package com.example.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    @Test
    void getServiceMessage() {
        Service s = new Service();
        assertEquals("Hello from Core -> Service", s.getServiceMessage());
    }
}
