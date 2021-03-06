package net.karmacoder.osc;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BluetoothScanningAdapter extends RecyclerView.Adapter<BluetoothScanningAdapter.BluetoothDeviceViewHolder> {
  private final LayoutInflater inflater;
  private final Listener listener;
  private List<BluetoothDevice> list;
  public BluetoothScanningAdapter(Context context, Listener listener) {
    super();

    this.inflater = LayoutInflater.from(context);
    this.listener = listener;

    this.list = new ArrayList<BluetoothDevice>();
  }

  public void reset() {
    list.clear();
    notifyDataSetChanged();
  }

  public void addDevice(BluetoothDevice device) {
    for (final BluetoothDevice currentDevice : list) {
      final String current = currentDevice.getAddress();
      if (current.equals(device.getAddress())) {
        return;
      }
    }

    list.add(device);
    notifyItemChanged(list.size() - 1);
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  @Override
  public BluetoothDeviceViewHolder onCreateViewHolder(final ViewGroup group, final int position) {
    final View view = inflater.inflate(R.layout.bluetooth_item, group, false);
    final BluetoothDeviceViewHolder viewHolder = new BluetoothDeviceViewHolder(view);
    view.setOnClickListener(new OnClickListener() {
                              @Override
                              public void onClick(View view) {
                                if (listener != null) {
                                  listener.onDeviceClicked(
                                      list.get(viewHolder.getPosition())
                                  );
                                }
                              }
                            }
    );

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(BluetoothDeviceViewHolder holder, int position) {
    holder.update(list.get(position));
  }

  public interface Listener {
    public void onDeviceClicked(BluetoothDevice model);
  }

  public static class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {
    public TextView textView;

    public BluetoothDeviceViewHolder(View view) {
      super(view);
      this.textView = (TextView) view.findViewById(R.id.text);
    }

    public void update(BluetoothDevice device) {
      textView.setText(device.getName() + "@" + device.getAddress() + device.getBondState());
    }
  }
}
