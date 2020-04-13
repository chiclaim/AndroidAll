package com.chiclaim.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chiclaim.ipc.IConnectionService;
import com.chiclaim.ipc.IMessageReceiverListener;
import com.chiclaim.ipc.IMessageService;
import com.chiclaim.ipc.IServiceManager;
import com.chiclaim.ipc.bean.Message;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RemoteService extends Service {


    private boolean isConnected = false;

    //private List<IMessageReceiverListener> listeners = new ArrayList<>();
    // 如果使用ArrayList ，在unRegister后，其实并没有真正的移除成功，因为传递进来的对象，RemoteService 收到的对象不还一样
    // RemoteCallbackList 是通过 IBinder 作为 key 来定位之前的监听对象，虽然监听对象发生变化，但是 IBinder 是唯一的
    private RemoteCallbackList<IMessageReceiverListener> listeners = new RemoteCallbackList<>();

    private Handler handler = new Handler(Looper.getMainLooper());
    private ScheduledThreadPoolExecutor scheduledExecutorService;

    private IConnectionService connectionService = new IConnectionService.Stub() {

        /*

            会在 Binder 所在进程中的子线程中执行（28320 表示进程的id）：
            D/RemoteService: connect in Binder:28320_3
            D/RemoteService: disconnect in Binder:28320_3
            D/RemoteService: isConnected in Binder:28320_3

         */

        private ScheduledFuture scheduledFuture;

        @Override
        public void connect() throws RemoteException {
            if (scheduledExecutorService == null) {
                scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
            }
            try {
                Thread.sleep(4000);
                isConnected = true;
                Log.d("RemoteService", "connect in " + Thread.currentThread().getName());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(RemoteService.this, "connect", Toast.LENGTH_SHORT).show();
                    }
                });

                scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int size = listeners.beginBroadcast();
                            for (int i = 0; i < size; i++) {
                                IMessageReceiverListener listener = listeners.getBroadcastItem(i);
                                Message message = new Message();
                                message.setContent("message send from remote service");
                                listener.onReceiveMessage(message);
                            }
                            listeners.finishBroadcast();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }, 5000, 5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void disconnect() throws RemoteException {
            isConnected = false;
            Log.d("RemoteService", "disconnect in " + Thread.currentThread().getName());

            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, "disconnect", Toast.LENGTH_SHORT).show();
                }
            });

            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.d("RemoteService", "isConnected in " + Thread.currentThread().getName());
            return isConnected;
        }

    };

    private IMessageService messageService = new IMessageService.Stub() {
        @Override
        public void sendMessage(final Message message) throws RemoteException {
            Log.d("RemoteService", "sendMessage in " + Thread.currentThread().getName());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RemoteService.this, String.valueOf(message.getContent()), Toast.LENGTH_SHORT).show();
                }
            });
            message.setSendSuccess(isConnected);
        }

        @Override
        public void registerReceiveListener(IMessageReceiverListener listener) throws RemoteException {
            Log.d("RemoteService", "RemoteService registerReceiveListener : " + listener.toString());
            Log.d("RemoteService", "registerReceiveListener in " + Thread.currentThread().getName());
            listeners.register(listener);

        }

        @Override
        public void unRegisterReceiveListener(IMessageReceiverListener listener) throws RemoteException {
            Log.d("RemoteService", "unRegisterReceiveListener in " + Thread.currentThread().getName());
            Log.d("RemoteService", "RemoteService unRegisterListener : " + listener.toString());
            listeners.unregister(listener);
        }
    };

    private Handler messengerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            // Class not found when unmarshalling: com.chiclaim.ipc.bean.Message
            bundle.setClassLoader(Message.class.getClassLoader());
            Message data = bundle.getParcelable("message");
            Toast.makeText(getApplicationContext(), data.getContent(), Toast.LENGTH_SHORT).show();

            Messenger replyTo = msg.replyTo;
            Message raw = new Message();
            raw.setContent("I receive your message: " + data.getContent());
            android.os.Message message = new android.os.Message();
            Bundle replyBundle = new Bundle();
            replyBundle.putParcelable("message", raw);
            message.setData(replyBundle);
            try {
                replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
    private Messenger messenger = new Messenger(messengerHandler);

    private IServiceManager serviceManager = new IServiceManager.Stub() {
        @Override
        public IBinder getService(String name) throws RemoteException {
            if (IConnectionService.class.getSimpleName().equals(name)) {
                return connectionService.asBinder();
            } else if (IMessageService.class.getSimpleName().equals(name)) {
                return messageService.asBinder();
            } else if (Messenger.class.getSimpleName().equals(name)) {
                return messenger.getBinder();
            }
            return null;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //connectionService.asBinder();
        return serviceManager.asBinder();
    }
}
