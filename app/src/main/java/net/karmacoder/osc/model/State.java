package net.karmacoder.osc.model;

import com.squareup.moshi.Json;

public class State {
  public enum ConnectionType {
    @Json(name = "softAP")SoftAp,
    @Json(name = "overAP")OverAp
  }

  public static class InnerState {
    public Double batteryLevel;
    public Boolean storageChanged;

    @Json(name = "_cameraId") public String cameraId;
    @Json(name = "_captureState") public State.CaptureState captureState;
    @Json(name = "_chargeState") public String chargeState;
    @Json(name = "_connectionType") public State.ConnectionType connectionType;
    @Json(name = "_networkType") public State.NetworkType networkType;
    @Json(name = "_fileChanged") public String fileChanged;
    @Json(name = "_reachData") public String reachData;
    @Json(name = "_sdCardState") public Boolean sdCardState;
    @Json(name = "_signalLevel") public Integer signalLevel;
    @Json(name = "_smsCount") public Integer smsCount;
    @Json(name = "_thermalWarningNoti") public Boolean thermalWarningNotification;
    @Json(name = "_wifiLevel") public Integer wifiLevel;
  }

  public enum CaptureState {
    @Json(name = "idle")Idle,
    @Json(name = "recording")Recording,
    @Json(name = "shooting")Shooting,
    @Json(name = "timer_counting")TimerCounting,
    @Json(name = "recording_no_preview")RecordingNoPreview,
    @Json(name = "idle_by_temperature")IdleByTemperature,
    @Json(name = "idle_by_network_state")IdleByNetworkState,
    @Json(name = "interval")Interval,
    @Json(name = "burstshot")Burstshot,
    @Json(name = "max_file_size_reached")MaxFileSizeReached,
    @Json(name = "storage_full")StorageFull,
    @Json(name = "liveStreaming_start_fail")LiveStreamingStartFail,
    @Json(name = "timelapse")Timelapse,
    @Json(name = "looping")Looping,
    @Json(name = "recording_by_customkey")RecordingByCustomkey,
    @Json(name = "timelapse_by_customkey")TimelapseByCustomkey,
    @Json(name = "looping_by_customkey")LoopingByCustomkey,
    @Json(name = "liveStreaming")LiveStreaming,
    @Json(name = "liveStreaming_WithRecording")LiveStreamingWithRecording,
    @Json(name = "liveStreaming_error_delay")LiveStreamingErrorDelay,
    @Json(name = "liveStreaming_error_server_reject")LiveStreamingErrorServerRSeject
  }

  public enum NetworkType {
    @Json(name = "noSim")NoSim,
    @Json(name = "dataOff")DataOff,
    @Json(name = "unknown")Unknown,
    @Json(name = "2G")TwoG,
    @Json(name = "3G")ThreeG,
    @Json(name = "LTE")Lte;
  }

  public String fingerprint;
  public InnerState state;

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append("fingerprint: ").append(fingerprint).append("\n");

    builder.append("state.batteryLevel: ").append(state.batteryLevel).append("\n");
    builder.append("state.storageChanged: ").append(state.storageChanged).append("\n");
    builder.append("state.cameraId: ").append(state.cameraId).append("\n");
    builder.append("state.captureState: ").append(state.captureState).append("\n");
    builder.append("state.chargeState: ").append(state.chargeState).append("\n");
    builder.append("state.connectionType: ").append(state.connectionType).append("\n");
    builder.append("state.networkType: ").append(state.networkType).append("\n");
    builder.append("state.fileChanged: ").append(state.fileChanged).append("\n");
    builder.append("state.sdCardState: ").append(state.sdCardState).append("\n");
    builder.append("state.signalLevel: ").append(state.signalLevel).append("\n");
    builder.append("state.smsCount: ").append(state.smsCount).append("\n");
    builder.append("state.thermalWarningNoti: ").append(state.thermalWarningNotification).append("\n");
    builder.append("state.wifiLevel: ").append(state.wifiLevel).append("\n");
    builder.append("state.reachData: ").append(state.reachData).append("\n");

    return builder.toString();
  }
}
