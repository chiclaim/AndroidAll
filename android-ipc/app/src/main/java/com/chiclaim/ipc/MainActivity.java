package com.chiclaim.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.ipc.bean.Message;
import com.chiclaim.ipc.service.RemoteService;

public class MainActivity extends AppCompatActivity {


    private ServiceConnection serviceConnection;

    private IConnectionService connectionServiceProxy;
    private IMessageService messageServiceProxy;


    private IMessageReceiverListener messageReceiverListener = new IMessageReceiverListener.Stub() {

        @Override
        public void onReceiveMessage(final Message message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, message.getContent(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IServiceManager serviceManager = IServiceManager.Stub.asInterface(service);
                try {
                    connectionServiceProxy = IConnectionService.Stub.asInterface(serviceManager.getService(IConnectionService.class.getSimpleName()));
                    messageServiceProxy = IMessageService.Stub.asInterface(serviceManager.getService(IMessageService.class.getSimpleName()));
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 一定要 unBindService
        // android.app.ServiceConnectionLeaked: Activity com.chiclaim.ipc.MainActivity has leaked ServiceConnection
        unbindService(serviceConnection);
    }

    public void connect(View view) throws RemoteException {
        // 该方法是阻塞的，如果耗时的话，会阻塞主线程
        // 可以在 aidl 文件中将该方法声明为 oneway，oneway 修饰的方法不能有返回值
        connectionServiceProxy.connect();
    }

    public void disconnect(View view) throws RemoteException {
        connectionServiceProxy.disconnect();
    }

    public void isConnected(View view) throws RemoteException {
        boolean c = connectionServiceProxy.isConnected();
        Toast.makeText(this, String.valueOf(c), Toast.LENGTH_SHORT).show();
    }

    public void sendMessage(View view) throws RemoteException {

        Message message = new Message();
        message.setContent("send message from main");
        messageServiceProxy.sendMessage(message);

    }

    public void registerListener(View view) throws RemoteException {
        // 此处打印的 messageReceiverListener 和 RemoteService 打印的不一样
        // 对象传递给另一个进程，messageReceiverListener 会序列化和反序列化

        Log.d("RemoteService", "MainActivity registerListener : " + messageReceiverListener.toString());
        messageServiceProxy.registerReceiveListener(messageReceiverListener);
    }

    public void unRegisterListener(View view) throws RemoteException {
        Log.d("RemoteService", "MainActivity unRegisterListener : " + messageReceiverListener.toString());
        messageServiceProxy.unRegisterReceiveListener(messageReceiverListener);
    }
}
