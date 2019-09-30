package class_bytecode.instruction.sync;

public class Client {


    // 字节码会生成 monitorenter 指令
    public void add() {
        synchronized (this) {
        }
    }

    // 方法签名上添加同步，不会再字节码上生成 monitorenter 指令
    public synchronized void add2() {
    }

}