// IServiceManager.aidl
package com.chiclaim.ipc;


interface IServiceManager {

    IBinder getService(String name);
}
