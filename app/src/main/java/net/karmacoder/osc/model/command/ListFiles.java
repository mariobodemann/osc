package net.karmacoder.osc.model.command;

import com.squareup.moshi.Json;

import java.util.List;

// Lists all images/all videos/all images and videos in the camera.
public class ListFiles extends Command {
  public static class Response extends Command.Response {
    public static class Entry {
      /**
       * Name of the image/video.
       **/
      public String name;

      /**
       * Absolute URL of the image/video, which can be used to download from the camera directly.
       */
      public String fileUrl;

      /**
       * Size in bytes of the image/video.
       */
      public String size;

      /**
       * Date, time, and time zone for the image, in the format: YYYY:MM:DD HH:MM:SS+(-)HH:MM. Use 24-hour format for the time. Date and time are separated by one blank character. Time zone is offset from UTC time.
       */
      public String dateTimeZone;

      /**
       * Latitude of the image capture location.
       */
      public Double lat;

      /**
       * Longitude of the image capture location.
       */
      public Double lng;

      /**
       * Width of the image or each video frame.
       */
      public Integer width;

      /**
       * Height of the image or each video frame.
       */
      public Integer height;

      /**
       * Base64 encoded string for thumbnail image (when maxThumbSize != null).
       */
      public String thumbnail;

      /**
       * A boolean value indicating if the file is processed (e.g. stitched) or it is only a preview. This should be true by default unless delayProcessing is set to true.
       */
      public Boolean isProcessed;

      /**
       * Orientation of the image or video, represented by rotated angle.
       * [0, 90, 180, 270]
       */
      @Json(name = "_orientation") public Integer orientation;

      /**
       * The length of the recorded video file.
       */
      @Json(name = "_recordTime") public Integer recordTime;

      /**
       * whether the file is sphere(360) image/video
       */
      @Json(name = "_is360") public Boolean is360;

      /**
       * GroupId of the image/video.
       */
      @Json(name = "_groupId") public String groupId;

      /**
       * Capture mode of the image/video.
       */
      @Json(name = "_captureMode") public String captureMode;
    }

    /**
     * A list of image properties. Each entry should contain the following fields except for latitude and longitude, which are optional
     */
    public List<Entry> entries;

    /**
     * Total number of entries in storage.
     */
    public int totalEntries;

    /**
     * (Optional) Set only if the result is incomplete (incomplete means any listing that does not include the last image).
     * <p>
     * To fetch remaining entries, the client should call listImages command again with the token.
     * The token is formatted in the following manner: “image_11_20” – the returned list is an image list, the next index of the list is 11, and the total number of image files in the storage is 20.
     */
    public String continuationToken;
  }


  public enum FileType {
    @Json(name = "image")Image,
    @Json(name = "video")Video,
    @Json(name = "all")All;
  }

  public ListFiles() {
    super("camera.listFiles");
  }

  /**
   * Type of the files to be listed, should be any of the three: “image”, ”video”, ”all”.
   **/
  public ListFiles setFileType(FileType fileType) {
    parameters.put("fileType", fileType);
    return this;
  }

  /**
   * Desired number of entries to return.
   **/
  public ListFiles setEntryCount(int entryCount) {
    parameters.put("entryCount", entryCount);
    return this;
  }

  /**
   * Maximum size of thumbnail images (either for images or for videos). It is set to null when the client wants to omit thumbnail images from the result.
   **/
  public ListFiles setMaxThumbSize(int maxThumbSize) {
    parameters.put("maxThumbSize", maxThumbSize);
    return this;
  }

  /**
   * An opaque continuation token of type String, returned by previous listFiles call, used to retrieve next files. Omit this parameter for the first listFiles call of the same type (“image”, “video” or “all”).
   */
  public ListFiles setContinuationToken(String continuationToken) {
    parameters.put("continuationToken", continuationToken);
    return this;
  }

  /**
   * If set to true, burst or interval capture files are grouped together.
   */
  public ListFiles setUseGrouping(boolean useGrouping) {
    parameters.put("_useGrouping", useGrouping);
    return this;
  }

  /**
   * Required if _useGrouping is set to true. Specify file type to be grouped.
   */
  public ListFiles setGroupingType(FileType groupingType) {
    parameters.put("_groupingType", groupingType);
    return this;
  }

  /**
   * The output entry list contains only the files whose _groupId is the same as this parameter. Must be used together with _fileId parameter.
   */
  public ListFiles setGroupId(String groupId) {
    parameters.put("_groupId", groupId);
    return this;
  }

  /**
   * The output entry list contains only the files whose _fileId is the same as this parameter. Must be used together with _groupdId parameter.
   */
  public ListFiles setFileId(String fileId) {
    parameters.put("_fileId", fileId);
    return this;
  }

  /**
   * The output entry list contains only the files whose captureMode is the same as this parameter. ["timelapse", "burst", "interval", "looping", "4gb"]
   */
  public ListFiles setCaptureMode(List<String> captureMode) {
    parameters.put("_captureMode", captureMode);
    return this;
  }
}
