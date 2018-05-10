package info.ivicel.github.ipcwithmessenger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MessengerSerivce extends Service {
    private static final String TAG = "MessengerSerivce";

    private static class MessengerHandler extends  Handler {
        private WeakReference<Context> reference;

        MessengerHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (reference.get() == null) {
                return;
            }

            switch (msg.what) {
                case Constans.MSG_HELLO_FROM_CLIENT: {
                    Log.d(TAG, "handle Hello: " + msg.getData().getString("msg"));
                    break;
                }

                case Constans.MSG_CUSTOM_PARCELABLE_OBJECT: {
                    try {
                        Log.d(TAG, "handle Custom Object: " + ((MyData)msg.obj).getData());
                    } catch (Exception e) {
                        Log.d(TAG, "handle Custom Object: " + e.getMessage());
                    }
                    break;
                }

                case Constans.MSG_SYSTEM_PARCELABLE_OBJECT: {
                    try {
                        Log.d(TAG, "handle System Object: " + ((Bundle) msg.obj).getString("msg"));
                    } catch (Exception e) {
                        Log.d(TAG, "handle System Object: " + e.getMessage());
                    }
                    break;
                }
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler(this));

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
