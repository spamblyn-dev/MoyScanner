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
            System.out.println("----- Local Subnet Menu -----");
            System.out.println("1. Identify devices");
            System.out.println("2. Separate devices by type");
            System.out.println("3. Locate all printers");
            System.out.println("4. Name all printers by name");
            System.out.println("5. Return to Main Menu");
            System.out.print("Enter choice: ");
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
            System.out.println("----- Whole Network Menu -----");
            System.out.println("1. Generate report");
            System.out.println("2. Show active and non-active devices");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter choice: ");
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
                    System.out.println("Invalid option.");
            }
        }
    }

    // Submenu for report exporting
    private void reportMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("----- Generate Report Menu -----");
            System.out.println("1. Export report as LocalNetwork.TXT");
            System.out.println("2. Export report as LocalNetwork.CSV");
            System.out.println("3. Return to Whole Network Menu");
            System.out.print("Enter choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    ReportGenerator_Pickle.exportReport("txt");
                    break;
                case "2":
                    ReportGenerator_Pickle.exportReport("csv");
                    break;
                case "3":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
