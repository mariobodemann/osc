package net.karmacoder.osc.model.command;

import java.util.HashMap;
import java.util.Map;

public class Command {
  public static class Response {
    public static class Error {
      public String code;
    }

    public String name;
    public Status.State state;
    public String id;
    public Map<String, Object> results;
    public Error error;
    public Map<String, Object> progress;

  }

  public static class CommandId {
    public final String id;

    public CommandId(String id) {
      this.id = id;
    }
  }

  public String name;
  public Map<String, Object> parameters;

  public Command(String name) {
    this.name = name;
    this.parameters = new HashMap<>();
  }
  @Override
  public String toString() {
    return name;
  }
}
