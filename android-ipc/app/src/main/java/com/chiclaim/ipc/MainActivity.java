package com.chiclaim.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.ipc.bean.Message;
import com.chiclaim.ipc.service.RemoteService;

public class MainActivity extends AppCompatActivity {


    private ServiceConnection serviceConnection;

    private IConnectionService connectionServiceProxy;
    private IMessageService messageServiceProxy;
    private Messenger messengerProxy;


    private IMessageReceiverListener messageReceiverListener = new IMessageReceiverListener.Stub() {

        @Override
        public void onReceiveMessage(final Message message) throws RemoteException {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, String.valueOf(message.getContent()), Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull android.os.Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            bundle.setClassLoader(Message.class.getClassLoader());
            Message message = bundle.getParcelable("message");
            Toast.makeText(MainActivity.this, String.valueOf(message.getContent()), Toast.LENGTH_SHORT).show();

        }
    };
    private Messenger messenger = new Messenger(handler);

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
                    messengerProxy = new Messenger(serviceManager.getService(Messenger.class.getSimpleName()));
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

        // 会输出 false，因为在 aidl 定义的时候使用的是 in 关键字：void sendMessage(in Message message);
        // in 关键字表示 在主进程中给对象设置的属性会传递给 Binder 进程
        // out 关键字表示 Binder 进程设置的属性，会传递给主进程
        // inout 关键字表示双向
        // 因为 Message.isSendSuccess = true 是在 Binder 进程设置的，并不会影响到主进程，所以输出是 false
        Log.d("MainActivity", "message send status : " + message.isSendSuccess());

    }

    public void registerListener(View view) throws RemoteException {
        // 此处打印的 messageReceiverListener 和 MainActivity 打印的不一样
        // 对象传递给另一个进程，messageReceiverListener 会序列化和反序列化

        Log.d("MainActivity", "MainActivity registerListener : " + messageReceiverListener.toString());
        messageServiceProxy.registerReceiveListener(messageReceiverListener);
    }

    public void unRegisterListener(View view) throws RemoteException {
        Log.d("MainActivity", "MainActivity unRegisterListener : " + messageReceiverListener.toString());
        messageServiceProxy.unRegisterReceiveListener(messageReceiverListener);
    }

    public void sendByMessenger(View view) throws RemoteException {
        Message raw = new Message();
        raw.setContent("send message in main by messenger");
        android.os.Message message = new android.os.Message();
        message.replyTo = messenger;
        Bundle bundle = new Bundle();
        bundle.putParcelable("message", raw);
        message.setData(bundle);
        messengerProxy.send(message);
    }
}
