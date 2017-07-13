package net.karmacoder.osc;

import net.karmacoder.osc.model.Info;
import net.karmacoder.osc.model.State;
import net.karmacoder.osc.model.command.Command;
import net.karmacoder.osc.model.command.ListFiles;
import net.karmacoder.osc.model.command.Status;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class CameraClient {
  private final CameraService service;

  public CameraClient() {
    this("http://192.168.43.1:6624");
  }

  CameraClient(String basePath) {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(basePath)
        .addConverterFactory(MoshiConverterFactory.create())
        .build();

    service = retrofit.create(CameraService.class);
  }

  public Call<State> state() {
    return service.state();
  }

  public Call<Info> info() {
    return service.info();
  }

  public Call<ListFiles.Response> listFiles(ListFiles listFilesCommand) {
    return service.listFiles(listFilesCommand);
  }

  public Call<Status> commandStatus(Command.CommandId id) {
    return service.commandStatus(id);
  }

}
