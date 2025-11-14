import java.io.*;
import java.util.*;

public class DeviceDatabase_Pickle {
    private static final Map<String, String[]> deviceMap = new HashMap<>();

    static {
        loadDevicesFromFile("device_vendors.txt");
    }

    private static void loadDevicesFromFile(String filename) {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line;
        int count = 0;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue; // skip empty/comment lines
            String[] parts = line.split(",");
            if (parts.length >= 3) {
                deviceMap.put(parts[0].trim().toUpperCase(), new String[]{parts[1].trim(), parts[2].trim()});
                count++;
            }
        }
        System.out.println("\nDevice vendor file '" + filename + "' successfully read.\n " + count + " entries loaded.");
    } catch (IOException e) {
        System.out.println("Could not read device vendor file: " + filename);
    }
}

    public static boolean isKnownDevice(String mac) {
        if (mac == null) return false;
        String prefix = mac.length() >= 8 ? mac.substring(0, 8).toUpperCase() : mac.toUpperCase();
        return deviceMap.containsKey(prefix);
    }

    public static String[] getVendorAndType(String mac) {
        if (mac == null) return new String[]{"Unknown Vendor", "Unknown Type"};
        String prefix = mac.length() >= 8 ? mac.substring(0, 8).toUpperCase() : mac.toUpperCase();
        return deviceMap.getOrDefault(prefix, new String[]{"Unknown Vendor", "Unknown Type"});
    }
}
