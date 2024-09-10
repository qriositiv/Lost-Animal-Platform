package com.straysafe.backend.config;

import ch.qos.logback.classic.LoggerContext;
import com.straysafe.backend.util.DatabaseLogAppender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class LogAppenderInitializer implements ApplicationListener<ApplicationReadyEvent> {

  private static final Logger logger = LoggerFactory.getLogger(LogAppenderInitializer.class);

  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {
    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
    DatabaseLogAppender databaseLogAppender = new DatabaseLogAppender();
    databaseLogAppender.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
    databaseLogAppender.setContext(context);
    databaseLogAppender.start();

    context.getLogger("ROOT").addAppender(databaseLogAppender);
  }
}
