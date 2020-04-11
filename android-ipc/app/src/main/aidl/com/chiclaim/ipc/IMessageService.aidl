// IMessageService.aidl
package com.chiclaim.ipc;

import com.chiclaim.ipc.bean.Message;
import com.chiclaim.ipc.IMessageReceiverListener;

interface IMessageService {

  // 实体类作为参数需要 in 关键字，基本类型和接口则不需要
  void sendMessage(in Message message);

  void registerReceiveListener(IMessageReceiverListener listener);
  void unRegisterReceiveListener(IMessageReceiverListener listener);



}
