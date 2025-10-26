import java.io.*;
import java.util.regex.*;

public class ArpUtil_Pickle {
    // Returns MAC in format "00:11:22:33:44:55" or null
    public static String getMacForIp(String ip) {
        try {
            ProcessBuilder pb = new ProcessBuilder("arp", "-a");
            pb.redirectErrorStream(true);
            Process p = pb.start();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.contains(ip)) {
                        String mac = extractMac(line);
                        if (mac != null) return mac.toUpperCase();
                    }
                }
            }
            p.waitFor();
        } catch (Exception e) {
            // ignore, return null if any error occurs
        }
        return null;
    }

    private static String extractMac(String line) {
        // simple regex to find mac (00:11:22:33:44:55 or 00-11-22-33-44-55)
        Matcher m = Pattern.compile("([0-9a-fA-F]{2}[:\\-]){5}[0-9a-fA-F]{2}").matcher(line);
        if (m.find()) {
            return m.group().replace('-', ':');
        }
        // optional: match dot format 0011.2233.4455
        Matcher m2 = Pattern.compile("([0-9A-Fa-f]{4}\\.){2}[0-9A-Fa-f]{4}").matcher(line);
        if (m2.find()) {
            String s = m2.group().replace(".", "");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i += 2) {
                if (i > 0) sb.append(':');
                sb.append(s.substring(i, i + 2));
            }
            return sb.toString();
        }
        return null;
    }
}
