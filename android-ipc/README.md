
# Android IPC


## Android 多进程：

- Application onCreate 方法会被执行多次
- 数据共享失败（对象、单例、回调）
- 进程间通信（文件、Intent、AIDL）


## Linux 进程间通信方式

- 管道：管道分为匿名管道和有名管道。匿名管道只能用于父子进程，或者兄弟进程（亲缘进程）之间。管道都是基于内存的缓冲区 来实现，大小为1页4KB，管道读写时还需要确保对方的存在，读只能从头开始读， 从末尾开始写。管道方向的数据只能从一个方向流动，如果需要双方进行通信的话，需要建立两个管道。一般管道用于轻量级的进程间通信。
- 消息队列：消息队列是存放在内核消息链表中，消息队列只会在内核重启或者特定删除的情况下才会消失。消息队列的读取支持随机读取。
- 信号：信号可以在任何时候发送给某个进程，而无需知道另外一个进程的状态。如果另一个进程不存在，信号就会被内存保存起来，如果进程启动了，内核就会把存储的信号发送给它。
- 共享内存：这是效率最高的一种进程通信机制。多个进程读取同一块内存空间，需要访问的进程将其映射到自己的私有地址空间，进程就可以读取这块内存，而不需要数据的拷贝，大大提高通信效率。但是由于多个进程共享一块内存，因此需要某种同步机制，这个同步机制就是下面信号量。
- 信号量：信号量的作用在于进程间的同步，信号量的增减是原子操作，是由内核来实现的。PV操作就是基于信号量的。
- 套接字：C/S 架构来通信。


## Android 跨进程通信核心


- Binder：
    - C/S架构，稳定性好，由于共享内存的方式。
    - 底层基于内存映射，性能较好，数据拷贝只有1次优于管道、消息队列、Socket
    - 安全性高，UID、PID可见


- Android 跨进程通信方式：
    - AIDL（基于Binder）
    - Intent、Messenger、ContentProvider（3者都是基于 AIDL）
    - 文件共享


## AIDL实战


### in、out、inout 关键字的作用

主要用于 IPC 通信是传递实体参数给 Binder 进程，例如 `IMessageService.aidl`：

```
// 实体类作为参数需要 in、out、inout 关键字，基本类型和接口则不需要
void sendMessage(inout Message message);
```

RemoteService.java:

```
private IMessageService messageService = new IMessageService.Stub() {
    @Override
    public void sendMessage(final Message message) throws RemoteException {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RemoteService.this, String.valueOf(message.getContent()), Toast.LENGTH_SHORT).show();
            }
        });
        // 将消息设置为发送成功状态
        message.setSendSuccess(isConnected);
    }
};
```

MainActivity.java:

```
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

```

### oneway 关键字作用

`oneway` 关键字用于发起 IPC 方法调用的时候是否阻塞主线程：

IConnectionService.aidl:

```
interface IConnectionService {
    oneway void connect();
}
```

RemoteService.java:

```
private IConnectionService connectionService = new IConnectionService.Stub() {
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
};
```

MainActivity.java 中发起 IPC 调用:

```
public void connect(View view) throws RemoteException {
    // 该方法是阻塞的，如果耗时的话，会阻塞主线程
    // 可以在 aidl 文件中将该方法声明为 oneway，oneway 修饰的方法不能有返回值
    connectionServiceProxy.connect();
}

```

### 关于 Binder 线程

从 Demo 例子中我们可以看出，在进行 IPC 调用的时候，目标方法会在 Binder 进程中的子线程中执行，例如在 RemoteService.connectionService.connect() 等方法输出线程名称：

```
会在 Binder 所在进程中的子线程中执行（28320 表示进程的id，3 表示线程编号）：
D/RemoteService: connect in Binder:28320_3
D/RemoteService: disconnect in Binder:28320_3
D/RemoteService: isConnected in Binder:28320_3
```

所以，如果要在主线程执行，需要用 Handler 进行通信。

### 关于对象拷贝

在 IPC 调用的时候，如果传递参数是复杂对象，那么这个对象到了 Binder 进程，其实已经不是当时传递的那个对象了，对象的地址是不一样的。

例如 Demo 中的 IMessageService.aidl:

```
void registerReceiveListener(IMessageReceiverListener listener);
void unRegisterReceiveListener(IMessageReceiverListener listener);
```

当我们调用了反注册 unRegisterReceiveListener 方法后，消息还是会收到，这是因为监听对象已经发生了变化，导致从集合中移除失败。

这个时候可以使用 RemoteCallbackList，将监听交给 RemoteCallbackList 来管理。RemoteCallbackList 是通过 IBinder 作为 key 来定位之前的监听对象，虽然监听对象发生变化，但是 IBinder 是唯一的。


### Messenger

Messenger 内部也是使用 AIDL + Handler 来实现 IPC 通信：

```
private Handler messengerHandler = new Handler(Looper.getMainLooper()) {
    @Override
    public void handleMessage(@NonNull android.os.Message msg) {
        super.handleMessage(msg);
    }
};
private Messenger messenger = new Messenger(messengerHandler);
```

通过 replyTo 实现反向通信：

```
// 发送端：
public void sendByMessenger(View view) throws RemoteException {
    Message raw = new Message();
    raw.setContent("send message in main by messenger");
    android.os.Message message = new android.os.Message();
    // 发送端设置 replyTo，接收端可以拿到该对象反向发送消息给发送端
    message.replyTo = messenger;
    Bundle bundle = new Bundle();
    bundle.putParcelable("message", raw);
    message.setData(bundle);
    messengerProxy.send(message);
}

// 接收端：
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
```

### AIDL 如何实现 IPC

Android 实现 IPC 的核心是 Binder，开发中一般是 Binder 结合 Service 一起使用。

Service 中有一个 onBind 方法，如果 Service 需要 IPC ，那么我们覆写该方法（以本 Demo 为例）：

```
public class MyService extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceManager.asBinder();
    }
}
```

上面的 onBind 方法返回的 IBinder 对象，会包装成 BinderProxy（用于对应 native 层的 IBinder 对象） 对象返回给客户端 bindService 时 ServiceConnection 的回调中：

```
bindService(intent, serviceConnection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // service object is BinderProxy
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

```

上面的 serviceManager 其实就是根据我们新建的 IServiceManager.AIDL 文件生成的类。生成的类有几个关键内部类我们来介绍下：

**Stub**

Stub 类是一个抽象内部类，它实现了 IServiceManager 接口，该类不会实现 IServiceManager 接口里面的方法（生成的类也不知道如何实现），而是由服务端来实现（Service）。

Stub 中有几个重要的方法：

- asInterface

    该方法主要用于将 IBinder 对象转换成对应的接口。该方法中会判断客户端和服务端的进程，如果进程一样，没有必要 IPC 那么该方法返回服务端的 Stub，否则返回 Stub.Proxy 内部类对象。

- asBinder

    该方法用于获取对应的 Binder 对象。


- onTransact(int code,android.os.Parcel data, android.os.Parcel reply, int flags)

    该方法运行在服务端的 Binder 线程池中，通过 code 参数来确定客户端要调用哪个方法，然后从 data 对象中获取执行目标方法所需要的参数，执行完方法后，如果该方法有返回值，就向 reply 中写入返回值。
    如果 onTransact 方法返回 false，那么客户端的请求会失败，因此我们可以利用这个特性来做权限验证，毕竟我们也不希望随便一个进程都能远程调用我们的服务。
    ```
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags)
            throws android.os.RemoteException
        {
          java.lang.String descriptor = DESCRIPTOR;
          switch (code)
          {
            // 省略其他 case
            case TRANSACTION_getService:
            {
              data.enforceInterface(descriptor);
              java.lang.String _arg0;
              _arg0 = data.readString();
              // 实际上调用的是 RemoteService.serviceManager.getService(name)
              android.os.IBinder _result = this.getService(_arg0);
              reply.writeNoException();
              reply.writeStrongBinder(_result);
              return true;
            }
            default:
            {
              return super.onTransact(code, data, reply, flags);
            }
          }
    ```


**Stub.Proxy**

Proxy 类同样也实现了 IServiceManager 接口，Proxy 类中实现了 IServiceManager 接口的方法，然后在方法中会触发 Stub.onTransact 方法。在 MinActivity 中执行 getService 方法的执行流程如下所示：

```
MainActivity.serviceManager.getService
    -> IServiceManager.Stub.Proxy.mRemote.transact() // mRemote 就是 BinderProxy
        -> IServiceManager.Stub.onTransact()
            -> RemoteService.serviceManager.getService()
```

这就是 AIDL 的基本执行流程。
