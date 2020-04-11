// IMessageReceiverListener.aidl
package com.chiclaim.ipc;

import com.chiclaim.ipc.bean.Message;

interface IMessageReceiverListener {

    void onReceiveMessage(in Message message);
}
