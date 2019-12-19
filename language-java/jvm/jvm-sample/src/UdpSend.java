import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSend {

    public static void main(String[] args) throws Exception {
        // 1 ,创建UDP服务,创建DatagramSocket对象
        DatagramSocket Socket = new DatagramSocket(9999);
        // 2,确定数据并封装成数据报包
        byte[] buffer = "I'm zhangsan".getBytes();
        //往端口13200,主机为"192.168.1.102"发送buffer字节数组里面的数据
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length,
                InetAddress.getByName("10.1.81.240"), 13200);
        // 通过服务发送数据
        Socket.send(packet);
        // 关闭资源
        Socket.close();
    }
}