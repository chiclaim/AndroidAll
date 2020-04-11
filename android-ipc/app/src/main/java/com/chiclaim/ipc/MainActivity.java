package com.chiclaim.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiclaim.ipc.service.RemoteService;

public class MainActivity extends AppCompatActivity {


    private ServiceConnection serviceConnection;

    private IConnectionService connectionServiceProxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        Intent intent = new Intent(this, RemoteService.class);
        bindService(intent, serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                connectionServiceProxy = IConnectionService.Stub.asInterface(service);
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
}
