package net.karmacoder.osc;

import net.karmacoder.osc.model.Info;
import net.karmacoder.osc.model.State;
import net.karmacoder.osc.model.command.Command;
import net.karmacoder.osc.model.command.Status;
import net.karmacoder.osc.utils.BaseTest;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

import static net.karmacoder.osc.model.State.ConnectionType.SoftAp;
import static net.karmacoder.osc.model.State.NetworkType.Lte;
import static net.karmacoder.osc.model.command.Status.State.Done;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

public class CameraClientTest extends BaseTest {
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

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void state() throws Exception {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody(loadFile("fixtures/cameraStateResponse.json"));
    server.enqueue(mockResponse);

    final Response<State> response = client.state().execute();

    assertThat(response.code(), equalTo(200));

    final State state = response.body();
    assertThat(state, notNullValue());
    assertThat(state.fingerprint, equalTo("FINGERPRINT"));
    assertThat(state.state.batteryLevel, equalTo(0.95));
    assertThat(state.state.storageChanged, CoreMatchers.is(true));
    assertThat(state.state.cameraId, equalTo("full-rear-camera"));
    assertThat(state.state.captureState, equalTo(State.CaptureState.Idle));
    assertThat(state.state.chargeState, equalTo("charging(USB)"));
    assertThat(state.state.connectionType, equalTo(SoftAp));
    assertThat(state.state.networkType, equalTo(Lte));
    assertThat(state.state.fileChanged, equalTo("X"));
    assertThat(state.state.reachData, nullValue());
    assertThat(state.state.sdCardState, equalTo(true));
    assertThat(state.state.signalLevel, equalTo(4));
    assertThat(state.state.smsCount, equalTo(0));
    assertThat(state.state.thermalWarningNotification, equalTo(false));
    assertThat(state.state.wifiLevel, equalTo(0));
  }

  @Test
  public void info() throws Exception {
    final MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setBody(loadFile("fixtures/cameraInfoResponse.json"));

    server.enqueue(mockResponse);

    final retrofit2.Response<Info> response = client.info().execute();

    final Info info = response.body();

    assertThat(info, notNullValue());
    assertThat(info.manufacturer, equalTo("LGE"));
    assertThat(info.model, equalTo("LG-R105"));
    assertThat(info.serialNumber, equalTo("LGR105cf99f269"));
    assertThat(info.firmwareVersion, equalTo("R105160302"));
    assertThat(info.supportUrl, equalTo("developer.lge.com/friends"));
    assertThat(info.endpoints.httpPort, equalTo(6624));
    assertThat(info.endpoints.httpUpdatesPort, equalTo(6624));
    assertThat(info.gps, equalTo(true));
    assertThat(info.gyro, equalTo(true));
    assertThat(info.uptime, equalTo(48));
    assertThat(info.apiLevel.get(0), equalTo("v2"));
    assertThat(info.api.toString(), equalTo("[" +
        "/osc/checkForUpdates, "
        + "/osc/commands/execute, "
        + "/osc/commands/status, "
        + "/osc/info, "
        + "/osc/state]"
    ));
  }

  @Test
  public void commandStatus() throws Exception {
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .setBody(loadFile("fixtures/cameraStatusResponse.json"))
    );

    final Response<Status> statusResponse = client.commandStatus(
        new Command.CommandId("fancyCommandId")
    ).execute();

    final RecordedRequest recordedRequest = server.takeRequest();
    assertThat(recordedRequest.getPath(), equalTo("/osc/commands/status"));
    final String recordedRequestBody = recordedRequest.getBody().readString(Charset.defaultCharset());
    assertThat(recordedRequestBody, containsString("fancyCommandId"));

    final Status status = statusResponse.body();
    assertThat(status, notNullValue());
    assertThat(status.state, equalTo(Done));
    assertThat((String) status.results.get("fileUri"), equalTo("ABC.JPG"));
  }
}