package net.karmacoder.osc;

import net.karmacoder.osc.model.Info;
import net.karmacoder.osc.model.State;
import net.karmacoder.osc.model.command.Command;
import net.karmacoder.osc.model.command.Status;
import net.karmacoder.osc.model.command.ListFiles;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CameraService {

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
  Call<ListFiles.Response> listFiles(@Body ListFiles command);

  @Headers({
      "X-XSRF-Protected: 1",
      "Content-Type: application/json;charset=utf-8"
  })
  @POST("osc/commands/status")
  Call<Status> commandStatus(@Body Command.CommandId id);
}
