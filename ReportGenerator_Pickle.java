import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

public class ReportGenerator_Pickle {
    public static void exportReport(String type, List<Device_Pickle> devices) {
        if (devices == null || devices.isEmpty()) {
            System.out.println("No devices found. Cannot generate report.");
            return;
        }

        String filename = "LocalNetworkReport_" + LocalDate.now() + "." + type;

        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("IP, Hostname, MAC, Vendor, Type");
            for (Device_Pickle d : devices) {
                writer.printf("%s, %s, %s, %s, %s%n",
                        d.getIpSafe(),
                        d.getHostname() != null ? d.getHostname() : "Unknown",
                        d.getMac() != null ? d.getMac() : "Unknown",
                        d.getVendor() != null ? d.getVendor() : "Unknown",
                        d.getType() != null ? d.getType() : "Unknown");
            }
            System.out.println("Report saved as: " + filename);
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}
