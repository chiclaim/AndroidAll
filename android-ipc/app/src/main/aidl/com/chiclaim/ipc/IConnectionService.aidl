// IConnectionService.aidl
package com.chiclaim.ipc;

interface IConnectionService {

    oneway void connect();
    void disconnect();
    boolean isConnected();
}
