package com.straysafe.backend.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.Timestamp;

public class DatabaseLogAppender extends AppenderBase<ILoggingEvent> {

  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate template) {
    this.namedParameterJdbcTemplate = template;
  }

  @Override
  protected void append(ILoggingEvent event) {
    if (namedParameterJdbcTemplate == null) {
      addError("No NamedParameterJdbcTemplate set for the appender named [" + name + "].");
      return;
    }

    String methodName = getMethodName(event);

    String query = "INSERT INTO Log (timestamp, log_level, class_name, method_name, message) VALUES (:timestamp, :log_level, :class_name, :method_name, :message)";

    SqlParameterSource params = new MapSqlParameterSource()
        .addValue("timestamp", new Timestamp(event.getTimeStamp()))
        .addValue("log_level", event.getLevel().toString())
        .addValue("class_name", event.getLoggerName())
        .addValue("method_name", methodName)
        .addValue("message", event.getFormattedMessage());

    namedParameterJdbcTemplate.update(query, params);
  }

  private String getMethodName(ILoggingEvent event) {
    StackTraceElement[] callerData = event.getCallerData();
    if (callerData != null && callerData.length > 0) {
      return callerData[0].getMethodName();
    } else {
      return "Unknown Method";
    }
  }
}
