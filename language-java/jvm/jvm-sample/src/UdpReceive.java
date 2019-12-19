import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpReceive {

    public static void main(String[] args) throws Exception {
        DatagramSocket Socket = new DatagramSocket(13200);
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        Socket.receive(packet);
        //通过数据包获取其中的数据
        String ip = packet.getAddress().getHostAddress();
        String data = new String(packet.getData(), 0, packet.getLength());
        //获取是从哪个端口发过来的
        int port = packet.getPort();
        System.out.println(ip + "--" + data + "--" + port);
        Socket.close();
    }
}