import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class ScannerUtils_Pickle {

    private static final String REPORT_FILE = "network_report.txt";
    private static List<Device_Pickle> devices = Collections.synchronizedList(new ArrayList<>());

    public static List<Device_Pickle> getDevices() { return devices; }

   public static void identifyDevices() {
    System.out.println("\n[ Local Subnet Scan Started ]");
    devices.clear();

    String prefix = getLocalIPv4Prefix();
    if (prefix == null) {
        System.out.println("Cannot detect local subnet.");
        return;
    }

    System.out.println("Scanning all hosts on subnet: " + prefix + "0/24");
    ExecutorService pool = Executors.newFixedThreadPool(50);
    List<Future<Device_Pickle>> futures = new ArrayList<>();

    // 1–254 range
    for (int i = 1; i <= 254; i++) {
        final String ip = prefix + i;
        futures.add(pool.submit(() -> scanHost(ip)));
    }

    // Collect scan results
    for (Future<Device_Pickle> f : futures) {
        try {
            Device_Pickle d = f.get();
            if (d != null) devices.add(d);
        } catch (Exception e) { }
    }
    pool.shutdown();

    //Refresh ARP cache to grab MACs for all discovered IPs
    System.out.println("\nRefreshing MAC addresses from ARP table...");
    for (Device_Pickle d : devices) {
        if (d.getMac() == null || d.getMac().isEmpty()) {
            String mac = ArpUtil_Pickle.getMacForIp(d.getIpSafe());
            d.setMac(mac);

            if (DeviceDatabase_Pickle.isKnownDevice(mac)) {
                String[] info = DeviceDatabase_Pickle.getVendorAndType(mac);
                d.setVendor(info[0]);
                d.setType(info[1]);
            }
        }
    }

    System.out.println("\nDevices found: " + devices.size());
    for (Device_Pickle d : devices) {
        System.out.println(d);
    }
}

    // Scan all local network prefixes (/24)
    public static void identifyWholeNetwork() {
        System.out.println("\n[ Whole Network Scan Started ]");
        devices.clear();
        List<String> prefixes = getAllLocalIPv4Prefixes();
        ExecutorService pool = Executors.newFixedThreadPool(50);
        List<Future<Device_Pickle>> futures = new ArrayList<>();

        for (String prefix : prefixes) {
            for (int i = 1; i <= 254; i++) {
                final String ip = prefix + i;
                futures.add(pool.submit(() -> scanHost(ip)));
            }
        }

        for (Future<Device_Pickle> f : futures) {
            try {
                Device_Pickle d = f.get();
                if (d != null) devices.add(d);
            } catch (Exception e) { }
        }
        pool.shutdown();

        System.out.println("Devices found: " + devices.size());
        for (Device_Pickle d : devices) {
            System.out.println(d);
        }
    }

    // Scan single host
    private static Device_Pickle scanHost(String ip) {
        try {
            if (!isHostOnline(ip)) return null; // TCP check for active host
            InetAddress addr = InetAddress.getByName(ip);

            Device_Pickle d = new Device_Pickle(ip);
            d.setHostname(addr.getCanonicalHostName());

            // Get MAC address
            String mac = ArpUtil_Pickle.getMacForIp(ip);
            d.setMac(mac);

            // Check if printer
            if (mac != null && PrinterDatabase_Pickle.isPrinterVendor(mac)) {
                d.setType("Printer");
                d.setVendor(PrinterDatabase_Pickle.identifyVendorByMac(mac));
            } 
            // Check known devices from device_vendors.txt
            else if (DeviceDatabase_Pickle.isKnownDevice(mac)) {
                String[] vendorInfo = DeviceDatabase_Pickle.getVendorAndType(mac);
                d.setVendor(vendorInfo[0]);
                d.setType(vendorInfo[1]);
            } 
            else if (addr.getCanonicalHostName().toLowerCase().contains("printer")) {
                d.setType("Printer");
                d.setVendor("Unknown Printer");
            } 
            else {
                d.setType("Unknown");
                d.setVendor("Unknown Vendor");
            }

            return d;
        } catch (Exception e) { 
            return null; 
        }
    }

    // TCP-based host check
    private static boolean isHostOnline(String ip) {
     try {
        // Cross-platform ping command
        String os = System.getProperty("os.name").toLowerCase();
        Process p;
        if (os.contains("win")) {
            p = Runtime.getRuntime().exec("ping -n 1 -w 300 " + ip);
        } else {
            p = Runtime.getRuntime().exec("ping -c 1 -W 1 " + ip);
        }

        int status = p.waitFor();
        return (status == 0);
    } catch (Exception e) {
        return false;
    }
}

    // List only printers
    public static void locatePrinters() {
        System.out.println("\n[ Locating Printers ]");
        boolean found = false;
        for (Device_Pickle d : devices) {
            if ("Printer".equalsIgnoreCase(d.getType())) {
                System.out.println(d);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No printers on this subnet!");
        }
    }

    // Name printers
    public static void namePrinters() {
        System.out.println("\n[ Naming Printers ]");
        boolean found = false;
        for (Device_Pickle d : devices) {
            if ("Printer".equalsIgnoreCase(d.getType())) {
                System.out.println("Printer named: " + d);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No printers on this subnet!");
        }
    }

    // Separate devices by type
    public static void separateDevices() {
        System.out.println("\n[ Separating devices by type ]");
        if (devices.isEmpty()) {
            System.out.println("No scan data. Run a scan first (option 1).");
            return;
        }

        Map<String, List<Device_Pickle>> map = new HashMap<>();
        for (Device_Pickle d : devices) {
            String type = (d.getType() != null) ? d.getType() : "Unknown";
            map.computeIfAbsent(type, k -> new ArrayList<>()).add(d);
        }

        for (String type : map.keySet()) {
            System.out.println("---- " + type + " ----");
            for (Device_Pickle d : map.get(type)) {
                System.out.println(d);
            }
        }
    }

    // Show active devices
    public static void showActiveDevices() {
        System.out.println("\n[ Active Devices ]");
        for (Device_Pickle d : devices) {
            try {
                if (isHostOnline(d.getIpSafe())) System.out.println("[ACTIVE] " + d);
            } catch (Exception e) { }
        }
    }

    // Ping a single device
    public static boolean pingDevice(String ip) {
        return isHostOnline(ip);
    }

    // Get local subnet prefix (e.g., 192.168.1)
    private static String getLocalIPv4Prefix() {
        try {
            InetAddress local = InetAddress.getLocalHost();
            String ip = local.getHostAddress();
            int lastDot = ip.lastIndexOf(".");
            if (lastDot > 0) return ip.substring(0, lastDot) + ".";
        } catch (Exception e) { }
        return null;
    }

    // Get all local IPv4 prefixes
    private static List<String> getAllLocalIPv4Prefixes() {
        List<String> prefixes = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
            while (nets.hasMoreElements()) {
                NetworkInterface net = nets.nextElement();
                Enumeration<InetAddress> addrs = net.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    String ip = addr.getHostAddress();
                    if (ip.contains(".") && !ip.startsWith("127.")) {
                        String prefix = ip.substring(0, ip.lastIndexOf(".") + 1);
                        if (!prefixes.contains(prefix)) prefixes.add(prefix);
                    }
                }
            }
        } catch (Exception e) { }
        return prefixes;
    }
}
