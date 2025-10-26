import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.*;

public class ScannerUtils_Pickle {
    private static List<Device_Pickle> devices = Collections.synchronizedList(new ArrayList<>());

    /*// Scan the local subnet for live devices
    public static void identifyDevices() {
        System.out.println("Scanning local subnet for devices...");
        devices.clear();

        String prefix = getLocalIPv4Prefix();
        if (prefix == null) {
            System.out.println("Unable to determine local subnet. Aborting scan.");
            return;
        }

        ExecutorService pool = Executors.newFixedThreadPool(50); // adjust threads as needed
        List<Future<Device_Pickle>> futures = new ArrayList<>();

        for (int i = 1; i <= 254; i++) {
            final String ip = prefix + "." + i;
            futures.add(pool.submit(() -> scanHost(ip)));
        }

        // Collect results
        for (Future<Device_Pickle> f : futures) {
            try {
                Device_Pickle d = f.get();
                if (d != null) devices.add(d);
            } catch (Exception e) {
                // ignore individual scan errors
            }
        }

        pool.shutdown();

        System.out.println("Devices found: " + devices.size());
        if (devices.isEmpty()) {
            System.out.println("No live hosts detected.");
        }
    } */
   public static void identifyDevices() {
    System.out.println("Scanning local subnet for devices...");
    devices.clear();

    String prefix = getLocalIPv4Prefix();
    if (prefix == null) {
        System.out.println("Unable to determine local subnet. Aborting scan.");
        return;
    }

    ExecutorService pool = Executors.newFixedThreadPool(50);
    List<Future<Device_Pickle>> futures = new ArrayList<>();

    for (int i = 1; i <= 254; i++) {
        final String ip = prefix + "." + i;
        futures.add(pool.submit(() -> scanHost(ip)));
    }

    for (Future<Device_Pickle> f : futures) {
        try {
            Device_Pickle d = f.get();
            if (d != null) devices.add(d);
        } catch (Exception e) { }
    }
    pool.shutdown();

    System.out.println("Devices found: " + devices.size());
    if (!devices.isEmpty()) {
        System.out.println("----- Device List -----");
        for (Device_Pickle d : devices) {
            System.out.println(d);
        }
    }
}


    // Scan a single host
    private static Device_Pickle scanHost(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            if (addr.isReachable(500)) { // 500ms timeout
                Device_Pickle d = new Device_Pickle(ip);
                d.setHostname(addr.getCanonicalHostName());

                // MAC lookup
                String mac = ArpUtil_Pickle.getMacForIp(ip);
                d.setMac(mac);

                // Printer detection (simple heuristic)
                if (mac != null && PrinterDatabase_Pickle.isPrinterVendor(mac)) {
                    d.setType("Printer");
                    d.setVendor(PrinterDatabase_Pickle.identifyVendorByMac(mac));
                } else if (addr.getCanonicalHostName().toLowerCase().contains("printer")) {
                    d.setType("Printer");
                    d.setVendor("Unknown Printer");
                } else {
                    d.setType("Unknown");
                    d.setVendor("Unknown Vendor");
                }

                return d;
            }
        } catch (IOException e) {
            // Ignore unreachable hosts
        }
        return null;
    }

    public static void separateDevices() {
        System.out.println("Separating devices by type...");
        Map<String, List<Device_Pickle>> map = new HashMap<>();
        for (Device_Pickle d : devices) {
            map.computeIfAbsent(d.getType(), k -> new ArrayList<>()).add(d);
        }
        for (String type : map.keySet()) {
            System.out.println("---- " + type + " ----");
            for (Device_Pickle d : map.get(type)) {
                System.out.println(d);
            }
        }
    }

    public static void locatePrinters() {
        System.out.println("Locating printers...");
        for (Device_Pickle d : devices) {
            if ("Printer".equalsIgnoreCase(d.getType())) {
                System.out.println(d);
            }
        }
    }

    public static void namePrinters() {
        System.out.println("Naming printers...");
        for (Device_Pickle d : devices) {
            if ("Printer".equalsIgnoreCase(d.getType())) {
                System.out.println("Printer named: " + d);
            }
        }
    }

    public static void showActiveDevices() {
        System.out.println("Showing active devices...");
        for (Device_Pickle d : devices) {
            System.out.println("[ACTIVE] " + d);
        }
    }

    public static boolean pingDevice(String ip) {
        try {
            InetAddress addr = InetAddress.getByName(ip);
            return addr.isReachable(500);
        } catch (IOException e) {
            return false;
        }
    }

    public static List<Device_Pickle> getDevices() {
        return devices;
    }

    // Get local subnet prefix (e.g., 192.168.1)
    private static String getLocalIPv4Prefix() {
        try {
            InetAddress local = InetAddress.getLocalHost();
            String ip = local.getHostAddress();
            int lastDot = ip.lastIndexOf(".");
            if (lastDot > 0) return ip.substring(0, lastDot);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
