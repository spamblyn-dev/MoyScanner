import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MenuHandler_Pickle menuHandler = new MenuHandler_Pickle(sc);

        boolean running = true;

        while (running) {
            System.out.println("===================================");
            System.out.println("   Welcome to Network Scanner");
            System.out.println("===================================");
            System.out.println("1. Scan local subnet");
            System.out.println("2. Scan whole local network");
            System.out.println("3. Ping device on the network");
            System.out.println("4. Port scanning");
            System.out.println("5. Safe mode");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    menuHandler.localSubnetMenu();
                    break;
                case "2":
                    menuHandler.wholeNetworkMenu();
                    break;
                case "3":
                    System.out.print("Enter IP to ping: ");
                    String ip = sc.nextLine();
                    boolean reachable = ScannerUtils_Pickle.pingDevice(ip);
                    if (reachable) {
                        System.out.println(ip + " is reachable.");
                    } else {
                        System.out.println(ip + " is not reachable.");
                    }
                    break;
                case "4":
                    System.out.print("Enter IP for port scan: ");
                    String portIp = sc.nextLine();
                    PortScan_Pickle.scanPorts(portIp);
                    break;
                case "5":
                    SafeMode_Pickle.runSafeMode();
                    break;
                case "6":
                    running = false;
                    System.out.println("Exiting program... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, try again.");
            }
        }
        sc.close();
    }
}
