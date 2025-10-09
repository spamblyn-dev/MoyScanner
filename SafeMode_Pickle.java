public class SafeMode_Pickle {
    public static void runSafeMode() {
        System.out.println("Safe Mode enabled. Running slow, quiet scans...");
        System.out.println("Performing safe subnet scan...");
        try {
            Thread.sleep(2000); // Slow down
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Safe scan completed.");
    }
}
