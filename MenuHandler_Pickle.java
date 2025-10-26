import java.util.Scanner;

public class MenuHandler_Pickle {
    private Scanner sc;

    public MenuHandler_Pickle(Scanner sc) {
        this.sc = sc;
    }

    // Submenu for local subnet scanning
    public void localSubnetMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\t----- Local Subnet Menu -----");
            System.out.println("\t1. Identify devices");
            System.out.println("\t2. Separate devices by type");
            System.out.println("\t3. Locate all printers");
            System.out.println("\t4. Name all printers by name");
            System.out.println("\t5. Return to Main Menu");
            System.out.print("\tEnter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    ScannerUtils_Pickle.identifyDevices();
                    break;
                case "2":
                    ScannerUtils_Pickle.separateDevices();
                    break;
                case "3":
                    ScannerUtils_Pickle.locatePrinters();
                    break;
                case "4":
                    ScannerUtils_Pickle.namePrinters();
                    break;
                case "5":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Submenu for whole network scanning
    public void wholeNetworkMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\t----- Whole Network Menu -----");
            System.out.println("\t1. Generate report");
            System.out.println("\t2. Show active and non-active devices");
            System.out.println("\t3. Return to Main Menu");
            System.out.print("\tEnter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    reportMenu();
                    break;
                case "2":
                    ScannerUtils_Pickle.showActiveDevices();
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("\tInvalid option.");
            }
        }
    }

    // Submenu for report exporting
    private void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\t----- Generate Report Menu -----");
            System.out.println("\t1. Export report as LocalNetwork.TXT");
            System.out.println("\t2. Export report as LocalNetwork.CSV");
            System.out.println("\t3. Return to Whole Network Menu");
            System.out.print("\tEnter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    ReportGenerator_Pickle.exportReport("txt", ScannerUtils_Pickle.getDevices());
                    break;
                case "2":
                    ReportGenerator_Pickle.exportReport("csv", ScannerUtils_Pickle.getDevices());
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("\tInvalid option.");
            }
        }
    }
}
