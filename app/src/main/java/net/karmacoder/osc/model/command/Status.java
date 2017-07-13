package net.karmacoder.osc.model.command;

import com.squareup.moshi.Json;

import java.util.Map;

public class Status {
  public enum State {
    @Json(name = "done")Done,
    @Json(name = "inProgress")InProgress,
    @Json(name = "error")Error,
  }

  public String name;
  public State state;
  public Map<String, Object> results;
}
