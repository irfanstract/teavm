package com.example.service;

import com.example.core.CoreUtil;

public class Service {
    public String getServiceMessage() {
        return CoreUtil.getMessage() + " -> Service";
    }
}
