module com.example.blockchainvoting {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    // Spring Boot and related modules
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.web;
    requires spring.beans;
    requires spring.core;
    requires spring.security.config;
    requires spring.security.web;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens com.example.blockchainvoting to javafx.fxml, spring.core, spring.beans, spring.context;
    exports com.example.blockchainvoting;
    opens com.example.blockchainvoting.dto;
    opens com.example.blockchainvoting.controller;
}