module application.wisefile {
    requires javafx.controls;
    requires javafx.fxml;
    requires juniversalchardet;
    requires org.apache.commons.lang3;
    requires log4j;
    requires poi;
    requires poi.ooxml;
    requires opencsv;
    requires com.ibm.icu;
    requires java.sql;
    requires java.desktop;
    requires controlsfx;
    
    requires static lombok;
    
    requires spring.context;
    requires spring.beans; 
    requires spring.core;
    requires spring.context.support;
    requires spring.expression;
    requires spring.jdbc;
    requires spring.test;
    requires spring.tx;
    requires spring.aop;
    requires javafx.graphics;
	requires javafx.base;
	
    opens application.wisefile to javafx.fxml;
    opens application.wisefile.util to spring.core;
    opens application.wisefile.service to spring.core;

    exports application.wisefile;
    exports application.wisefile.vo;
    exports application.wisefile.service;
    exports application.wisefile.util;
    exports application.wisefile.controller;
    opens application.wisefile.controller to javafx.fxml;
    exports application.wisefile.util.address;
    opens application.wisefile.util.address to spring.core;
    exports application.wisefile.service.impl;
    opens application.wisefile.service.impl to spring.core;
    exports application.wisefile.common;
    opens application.wisefile.common to javafx.fxml;
    exports application.wisefile.chk;
    opens application.wisefile.chk to javafx.fxml;
    opens application.wisefile.vo to javafx.fxml;
}