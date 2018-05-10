package info.ivicel.github.ipcwithmessenger;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean bound = false;
    private Messenger messenger;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bound = true;
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            messenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.send_hello).setOnClickListener(this);
        findViewById(R.id.send_system_object).setOnClickListener(this);
        findViewById(R.id.send_custom_object).setOnClickListener(this);

        Intent intent = new Intent(this, MessengerSerivce.class);
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_hello:
                sendHello();
                break;

            case R.id.send_custom_object:
                sendCustomObject();
                break;

            case R.id.send_system_object:
                sendSystemObject();
                break;
        }
    }

    private void sendSystemObject() {
        Message msg = Message.obtain();
        msg.what = Constans.MSG_SYSTEM_PARCELABLE_OBJECT;
        Bundle data = new Bundle();
        data.putString("msg", "data from client with bundle using Message.obj");
        msg.obj = data;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendCustomObject() {
        Message msg = Message.obtain();
        msg.what = Constans.MSG_CUSTOM_PARCELABLE_OBJECT;
        MyData data = new MyData("data from client with custom parcelalbe using Message.obj");;
        msg.obj = data;
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendHello() {
        Message msg = Message.obtain();
        msg.what = Constans.MSG_HELLO_FROM_CLIENT;
        Bundle data = new Bundle();
        data.putString("msg", "hello from client with bundle");
        msg.setData(data);
        try {
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
