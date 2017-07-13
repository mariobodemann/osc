package net.karmacoder.osc;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;
import retrofit2.http.Headers;
import com.squareup.moshi.*;
import java.util.Map;
import java.util.List;
import java.util.*;

public interface CameraService {
  public class State {
    public static class State {
      public static enum CaptureState {
        idle,
        recording,
        shooting,
        timer_counting,
        recording_no_preview,
        idle_by_temperature,
        idle_by_network_state,
        interval,
        burstshot,
        max_file_size_reached,
        storage_full,
        liveStreaming_start_fail,
        timelapse,
        looping,
        recording_by_customkey,
        timelapse_by_customkey,
        looping_by_customkey,
        liveStreaming,
        liveStreaming_WithRecording,
        liveStreaming_error_delay,
        liveStreaming_error_server_reject
        }

      public Double batteryLevel;
      public Boolean storageChanged;

      @Json(name = "_cameraId") public String cameraId;
      @Json(name = "_captureState") public CaptureState captureState;
      @Json(name = "_chargeState") public String chargeState;
      @Json(name = "_connectionType") public String connectionType;
      @Json(name = "_networkType")  public String networkType;
      @Json(name = "_fileChanged")  public String fileChanged;
      @Json(name = "_reachData" ) public String reachData;
      @Json(name = "_sdCardState")  public Boolean sdCardState;
      @Json(name = "_signalLevel")  public Integer signalLevel;
      @Json(name = "_smsCount")  public Integer smsCount;
      @Json(name = "_thermalWarningNoti")  public Boolean thermalWarningNoti;
      @Json(name = "_wifiLevel")  public Integer wifiLevel;
    }

    public String fingerprint;
    public State state;

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();

      builder.append("fingerprint: ").append(fingerprint).append(System.lineSeparator());

      builder.append("state.batteryLevel: ").append(state.batteryLevel).append(System.lineSeparator());
      builder.append("state.storageChanged: ").append(state.storageChanged).append(System.lineSeparator());
      builder.append("state.cameraId: ").append(state.cameraId).append(System.lineSeparator());
      builder.append("state.captureState: ").append(state.captureState).append(System.lineSeparator());
      builder.append("state.chargeState: ").append(state.chargeState).append(System.lineSeparator());
      builder.append("state.connectionType: ").append(state.connectionType).append(System.lineSeparator());
      builder.append("state.networkType: ").append(state.networkType).append(System.lineSeparator());
      builder.append("state.fileChanged: ").append(state.fileChanged).append(System.lineSeparator());
      builder.append("state.sdCardState: ").append(state.sdCardState).append(System.lineSeparator());
      builder.append("state.signalLevel: ").append(state.signalLevel).append(System.lineSeparator());
      builder.append("state.smsCount: ").append(state.smsCount).append(System.lineSeparator());
      builder.append("state.thermalWarningNoti: ").append(state.thermalWarningNoti).append(System.lineSeparator());
      builder.append("state.wifiLevel: ").append(state.wifiLevel).append(System.lineSeparator());
      builder.append("state.reachData: ").append(state.reachData).append(System.lineSeparator());

      return builder.toString();
    }
  }

  public class Info {
    public String manufacturer;
    public String model;
    public String serialNumber;
    public String firmwareVersion;
    public String supportUrl;
    public Map<String, Integer> endpoints;
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

  public class Command {
    public String name;
    public Map<String, String> parameters;

    public Command(String name, Map<String, String> parameters) {
      this.name = name;
      this.parameters = parameters;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  // Lists all images/all videos/all images and videos in the camera.
  public class ListFileCommand extends Command {
    public static class Parameter {
      public enum FileType {
        image, video, all
        }

      // Type of the files to be listed, should be any of the three: “image”, ”video”, ”all”.
      public FileType fileType;

      // Desired number of entries to return.
      public int entryCount;

      // Maximum size of thumbnail images (either for images or for videos). It is set to null when the client wants to omit thumbnail images from the result.
      public int maxThumbSize;

      // An opaque continuation token of type String, returned by previous listFiles call, used to retrieve next files. Omit this parameter for the first listFiles call of the same type (“image”, “video” or “all”).
      public String continuationToken;

      // If set to true, burst or interval capture files are grouped together.
      @Json(name="_useGrouping") public Boolean useGrouping;

      // Required if _useGrouping is set to true. Specify file type to be grouped.
      @Json(name="_groupingType")public FileType groupingType;

      // The output entry list contains only the files whose _groupId is the same as this parameter. Must be used together with _fileId parameter.
      @Json(name="_groupId")public String groupId;

      // The output entry list contains only the files whose _fileId is the same as this parameter. Must be used together with _groupdId parameter.
      @Json(name="_fileId")public String fileId;

      // The output entry list contains only the files whose captureMode is the same as this parameter. ["timelapse", "burst", "interval", "looping", "4gb"]
      @Json(name="_captureMode") public List<String> captureMode;
    }

    public ListFileCommand(Parameter parameter) {
      super("camera.listFiles", parameterToMap(parameter));
    }

    public static Map<String, String> parameterToMap(Parameter parameter) {
      Map<String, String> map = new HashMap<String, String>();

      if (parameter != null) {
        map.put("fileType", parameter.fileType != null? parameter.fileType.name() : "");
        map.put("entryCount", parameter.entryCount + "");
        map.put("maxThumbSize", parameter.maxThumbSize + "");
        map.put("continuationToken", parameter.continuationToken != null ? parameter.continuationToken : "");
        map.put("_useGrouping", parameter.useGrouping != null ? parameter.useGrouping.toString() : "");
        map.put("_groupingType", parameter.groupingType != null ? parameter.groupingType.toString() : "");
        map.put("_groupId", parameter.groupId);
        map.put("_fileId", parameter.fileId);
        map.put("_captureMode", parameter.captureMode != null ? parameter.captureMode.toString() : "");
      }
      
      return map;
    }
  }

  /*  
   camera.getFile  Returns either an image or a video given its URL.
   camera.getMetadata  Returns file metadata given its URL.
   camera.takePicture  Captures an equirectangular image, saving lat/long coordinates to EXIF.
   camera.delete Deletes an image given its URL.
   camera._getCapability Shows the availability of Friends Camera features.

   camera.getLivePreview Returns one shot of the current preview frame of the camera.
   camera.getOptions Returns current settings for requested properties.
   camera.setOptions Sets values for specified properties.
   camera.startCapture Starts video capture or interval image capture depending on value of captureMode.
   camera.stopCapture  Stops video capture or open-ended interval image capture, determined by capture mode in Options
   camera._getRecordingStatus  Gets the recording status.
   camera._liveSnapshot  Captures an image during recording, saving lat/long coordinates to EXIF.
   camera._manualMetadata  Requests to the camera current settings of White Balance, Exposure Value, ISO, Shutter speed information.
   camera._pauseRecording  Pauses recording.
   camera._resumeRecording Resumes recording.
   camera._startPreview  Obtains information on MPEG preview data from the camera.
   camera._startStillPreview Obtains information on JPEG preview data from the camera.
   camera._stopPreview Stops transferring MPEG preview video stream from the camera.
   camera._stopStillPreview  Stops transferring JPEG preview video stream from the camera.
   */

  public class CommandResponse {
    public String name;
    public String state;
    public String id;
    public Map<String, Object> results;
    public Error error;
    public Map<String, Object> progress;
  }

  public class Error {
    public String code;
  }

  public class CommandId {
    public String id;
  }

  public class CommandStatus {
    public String name;
    public String state;
    public Map<String, Object> results;
  }

  @Headers({
      "X-XSRF-Protected: 1",
      "Content-Type: application/json;charset=utf-8"
    })
  @GET("osc/state")
  Call<State> state();

  @Headers({
      "X-XSRF-Protected: 1",
      "Content-Type: application/json;charset=utf-8"
    })
  @GET("osc/info")
  Call<Info> info();

  @Headers({
      "X-XSRF-Protected: 1",
      "Content-Type: application/json;charset=utf-8"
    })
  @POST("osc/commands/execute")
  Call<CommandResponse> commandExecute(@Body ListFileCommand command);

  @Headers({
      "X-XSRF-Protected: 1",
      "Content-Type: application/json;charset=utf-8"
    })
  @GET("osc/commands/status")
  Call<CommandStatus> commandStatus(@Body CommandId id);
}
