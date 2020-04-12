
## Android IPC


### Android 多进程：

- Application onCreate 方法会被执行多次
- 数据共享失败（对象、单例、回调）
- 进程间通信（文件、Intent、AIDL）


### Linux 进程间通信方式：

- 管道：管道分为匿名管道和有名管道。匿名管道只能用于父子进程，或者兄弟进程（亲缘进程）之间。管道都是基于内存的缓冲区 来实现，大小为1页4KB，管道读写时还需要确保对方的存在，读只能从头开始读， 从末尾开始写。管道方向的数据只能从一个方向流动，如果需要双方进行通信的话，需要建立两个管道。一般管道用于轻量级的进程间通信。
- 消息队列：消息队列是存放在内核消息链表中，消息队列只会在内核重启或者特定删除的情况下才会消失。消息队列的读取支持随机读取。
- 信号：信号可以在任何时候发送给某个进程，而无需知道另外一个进程的状态。如果另一个进程不存在，信号就会被内存保存起来，如果进程启动了，内核就会把存储的信号发送给它。
- 共享内存：这是效率最高的一种进程通信机制。多个进程读取同一块内存空间，需要访问的进程将其映射到自己的私有地址空间，进程就可以读取这块内存，而不需要数据的拷贝，大大提高通信效率。但是由于多个进程共享一块内存，因此需要某种同步机制，这个同步机制就是下面信号量。
- 信号量：信号量的作用在于进程间的同步，信号量的增减是原子操作，是由内核来实现的。PV操作就是基于信号量的。
- 套接字：C/S 架构来通信。


### Android 跨进程通信核心：


- Binder：
    - C/S架构，稳定性好，由于共享内存的方式。
    - 底层基于内存映射，性能较好，数据拷贝只有1次优于管道、消息队列、Socket
    - 安全性高，UID、PID可见


- Android 跨进程通信方式：
    - AIDL（基于Binder）
    - Intent、Messenger、ContentProvider（3者都是基于 AIDL）
    - 文件共享


### AIDL实战


#### in、out、inout 关键字的作用

主要用于 IPC 通信是传递实体参数给 Binder 进程，例如 `IMessageService.aidl`：

```
// 实体类作为参数需要 in、out、inout 关键字，基本类型和接口则不需要
void sendMessage(inout Message message);
```

`RemoteService.java:`

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

`MainActivity.java:`
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

#### oneway 关键字作用

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

#### 如何通过 AIDL 实现 callback

#### AIDL 如何实现 IPC


