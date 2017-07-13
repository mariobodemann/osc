package net.karmacoder.osc;


import net.karmacoder.osc.model.command.ListFiles;
import net.karmacoder.osc.utils.BaseTest;

import org.junit.Before;
import org.junit.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static java.util.Collections.singletonList;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

public class AllCommandsTest extends BaseTest {
  private CameraClient client;
  private MockWebServer server;

  @Before
  public void setUp() throws Exception {
    server = new MockWebServer();
    server.start();

    final String mockedUrl = server
        .url("/")
        .toString();

    client = new CameraClient(mockedUrl);
  }

  /*Deletes an image given its URL.*/
  @Test public void testDelete() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Returns either an image or a video given its URL.*/
  @Test public void testGetFile() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Returns one shot of the current preview frame of the camera.*/
  @Test public void testGetLivePreview() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Returns file metadata given its URL.*/
  @Test public void testGetMetadata() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Returns current settings for requested properties.*/
  @Test public void testGetOptions() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Lists all images/all videos/all images and videos in the camera.*/
  @Test public void testListFiles() throws Exception {
    // SETUP
    final ListFiles c = new ListFiles()
        .setFileType(ListFiles.FileType.Video)
        .setEntryCount(3)
        .setMaxThumbSize(4)
        .setContinuationToken("FOO")
        .setUseGrouping(true)
        .setGroupingType(ListFiles.FileType.Image)
        .setGroupId("FOO")
        .setFileId("FOO")
        .setCaptureMode(singletonList("timelapse"));

    server.enqueue(new MockResponse()
        .setResponseCode(200)
        .setBody(loadFile("fixtures/listFilesResponse.json"))
    );

    // Test
    final Response<ListFiles.Response> response = client.listFiles(c).execute();

    // Validate
    final ListFiles.Response body = response.body();
    assertThat(body, not(equalTo(null)));
    assertThat(body.totalEntries, equalTo(3));
    assertThat(body.entries.size(), equalTo(3));
    assertThat(body.entries.get(0).fileUrl, equalTo("foobar"));
    assertThat(body.entries.get(0).is360, equalTo(true));

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getBody().readUtf8(), equalTo(loadFile("fixtures/listFilesRequest.json")));
  }

  /*Sets values for specified properties.*/
  @Test public void testSetOptions() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Starts video capture or interval image capture depending on value of captureMode.*/
  @Test public void testStartCapture() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Stops video capture or open-ended interval image capture, determined by capture mode in Options*/
  @Test public void testStopCapture() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Captures an equirectangular image, saving lat/long coordinates to EXIF.*/
  @Test public void testTakePicture() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Shows the availability of Friends Camera features.*/
  @Test public void testGetCapability() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Gets the recording status.*/
  @Test public void testGetRecordingStatus() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Captures an image during recording, saving lat/long coordinates to EXIF.*/
  @Test public void testLiveSnapshot() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Requests to the camera current settings of White Balance, Exposure Value, ISO, Shutter speed information.*/
  @Test public void testManualMetadata() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Pauses recording.*/
  @Test public void testPauseRecording() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Resumes recording.*/
  @Test public void testResumeRecording() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Obtains information on MPEG preview data from the camera.*/
  @Test public void testStartPreview() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Obtains information on JPEG preview data from the camera.*/
  @Test public void testStartStillPreview() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Stops transferring MPEG preview video stream from the camera.*/
  @Test public void testStopPreview() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

  /*Stops transferring JPEG preview video stream from the camera.*/
  @Test public void testStopStillPreview() throws Exception {
    assertThat("implemented", equalTo("true"));
  }

}
