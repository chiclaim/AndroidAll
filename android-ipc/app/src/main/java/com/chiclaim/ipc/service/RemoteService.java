package com.chiclaim.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chiclaim.ipc.IConnectionService;


public class RemoteService extends Service {


    private boolean isConnected = false;
    private Handler handler = new Handler(Looper.getMainLooper());

    private IConnectionService connectionService = new IConnectionService.Stub() {

        /*

            会在 Binder 所在进程中的子线程中执行（28320 表示进程的id）：
            D/RemoteService: connect in Binder:28320_3
            D/RemoteService: disconnect in Binder:28320_3
            D/RemoteService: isConnected in Binder:28320_3

         */

        @Override
        public void connect() throws RemoteException {
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
        }

        @Override
        public boolean isConnected() throws RemoteException {
            Log.d("RemoteService", "isConnected in " + Thread.currentThread().getName());
            return isConnected;
        }

    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return connectionService.asBinder();
    }
}
