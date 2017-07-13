package net.karmacoder.osc.model;

import java.util.List;
import java.util.Map;

public class Info {
  public static class Endpoints {
    public int httpPort;
    public int httpUpdatesPort;
  }

  public String manufacturer;
  public String model;
  public String serialNumber;
  public String firmwareVersion;
  public String supportUrl;
  public Endpoints endpoints;
  public Boolean gps;
  public Boolean gyro;
  public Integer uptime;
  public List<String> apiLevel;
  public List<String> api;

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("manufacturer: ").append(manufacturer).append(System.lineSeparator());
    builder.append("model: ").append(model).append(System.lineSeparator());
    builder.append("serialNumber: ").append(serialNumber).append(System.lineSeparator());
    builder.append("firmeareVersion: ").append(firmwareVersion).append(System.lineSeparator());
    builder.append("supportUrl: ").append(supportUrl).append(System.lineSeparator());
    builder.append("endpoints: ").append(endpoints).append(System.lineSeparator());
    builder.append("gps: ").append(gps).append(System.lineSeparator());
    builder.append("gyro: ").append(gyro).append(System.lineSeparator());
    builder.append("uptime: ").append(uptime).append(System.lineSeparator());
    builder.append("apiLevel: ").append(apiLevel).append(System.lineSeparator());
    builder.append("api: ").append(api).append(System.lineSeparator());

    return builder.toString();
  }
}
