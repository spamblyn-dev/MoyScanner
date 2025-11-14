import java.net.Socket;

public class PortScan_Pickle {
    public static void scanPorts(String ip) {
        System.out.println("Scanning ports on " + ip);
        for (int port = 10; port <= 20; port++) {
            try (Socket socket = new Socket(ip, port)) {
                System.out.println("Port " + port + " is OPEN");
            } catch (Exception e) {
                System.out.println("Port " + port + " is CLOSED");
            }
        }
    }
}
