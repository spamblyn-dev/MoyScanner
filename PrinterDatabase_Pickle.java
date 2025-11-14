import java.util.*;

public class PrinterDatabase_Pickle {
    private static final Map<String, String> vendorMap = new HashMap<>();

    static {
        vendorMap.put("00:1A:4B", "HP");
        vendorMap.put("00:80:92", "Canon");
        vendorMap.put("00:26:AB", "Epson");
        vendorMap.put("00:19:BB", "Brother");
        vendorMap.put("00:40:84", "Lexmark");
        vendorMap.put("00:50:C2", "Kyocera");
        vendorMap.put("00:1B:44", "Xerox");
    }

    public static boolean isPrinterVendor(String mac) {
        if (mac == null) return false;
        String prefix = mac.length() >= 8 ? mac.substring(0, 8).toUpperCase() : mac.toUpperCase();
        for (String known : vendorMap.keySet()) {
            if (prefix.startsWith(known)) return true;
        }
        return false;
    }

    public static String identifyVendorByMac(String mac) {
        if (mac == null) return "Unknown Vendor";
        String prefix = mac.length() >= 8 ? mac.substring(0, 8).toUpperCase() : mac.toUpperCase();
        for (Map.Entry<String, String> entry : vendorMap.entrySet()) {
            if (prefix.startsWith(entry.getKey())) return entry.getValue();
        }
        return "Unknown Vendor";
    }
}
