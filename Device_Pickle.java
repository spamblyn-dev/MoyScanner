public class Device_Pickle {
    private String ip;
    private String hostname;
    private String mac;
    private String vendor;
    private String type;

    public Device_Pickle(String ip) {
        this.ip = ip;
    }

    public Device_Pickle(String ip, String hostname, String mac, String vendor, String type) {
        this.ip = ip;
        this.hostname = hostname;
        this.mac = mac;
        this.vendor = vendor;
        this.type = type;
    }

    public String getIpSafe() {
        return (ip != null) ? ip : "Unknown";
    }

    public String getHostname() { return hostname; }
    public String getMac() { return mac; }
    public String getVendor() { return vendor; }
    public String getType() { return type; }

    public void setHostname(String hostname) { this.hostname = hostname; }
    public void setMac(String mac) { this.mac = mac; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    public void setType(String type) { this.type = type; }

    public String toString() {
        return getIpSafe() + " | " +
               (hostname != null ? hostname : "Unknown") + " | " +
               (mac != null ? mac : "Unknown") + " | " +
               (vendor != null ? vendor : "Unknown") + " | " +
               (type != null ? type : "Unknown");
    }
}
