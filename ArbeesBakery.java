import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

// ─────────────────────────────────────────────
//  User class
// ─────────────────────────────────────────────
class User {
    String name;
    String email;
    String password;
    String address;

    public User(String name, String email, String password, String address) {
        this.name     = name;
        this.email    = email;
        this.password = password;
        this.address  = address;
    }
}

// ─────────────────────────────────────────────
//  OrderItem class
// ─────────────────────────────────────────────
class OrderItem {
    String itemName;
    int    quantity;
    double price;
    double total;

    public OrderItem(String itemName, int quantity, double price) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.price    = price;
        this.total    = price * quantity;
    }
}

// ─────────────────────────────────────────────
//  OrderRecord — one full completed order
// ─────────────────────────────────────────────
class OrderRecord {
    String               orderRef;
    String               customerName;
    String               customerEmail;
    String               deliveryAddress;
    String               paymentMethod;
    String               timestamp;
    String               status;
    ArrayList<OrderItem> items;
    double               grandTotal;

    public OrderRecord(String orderRef, String customerName, String customerEmail,
                       String deliveryAddress, String paymentMethod,
                       String timestamp, ArrayList<OrderItem> items, double grandTotal) {
        this.orderRef        = orderRef;
        this.customerName    = customerName;
        this.customerEmail   = customerEmail;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod   = paymentMethod;
        this.timestamp       = timestamp;
        this.status          = "Pending";
        this.items           = new ArrayList<>(items);
        this.grandTotal      = grandTotal;
    }
}

// ─────────────────────────────────────────────
//  Main application
// ─────────────────────────────────────────────
public class ArbeesBakery {//main program

    static Scanner                sc           = new Scanner(System.in);
    static ArrayList<User>        users        = new ArrayList<>();
    static ArrayList<OrderItem>   cart         = new ArrayList<>();
    static ArrayList<OrderRecord> orderHistory = new ArrayList<>();
    static User                   loggedIn     = null;
    static int                    orderCounter = 1000;

    // ── Admin credentials ──────────────────────
    static final String ADMIN_USER = "admin";
    static final String ADMIN_PASS = "admin123";

    // ── Menu data ──────────────────────────────
    static ArrayList<String> itemNames  = new ArrayList<>();
    static ArrayList<Double> itemPrices = new ArrayList<>();

    static {
        String[] names = {
            "Pandesal", "Monay", "Ensaymada",
            "Spanish Bread", "Egg Bread", "Torta", "Star Bread",
            "Francis", "Ube Cheese Pandesal", "Choco German",
            "Siacoy", "Doughnut", "Banana Bread", "Slice Bread", "Chiffon Cake"
        };
        double[] prices = {
            5.00, 6.00, 6.00, 7.00, 10.00, 10.00, 6.00,
            6.00, 8.00, 6.00, 6.00, 6.00, 55.00, 65.00, 250.00
        };
        for (int i = 0; i < names.length; i++) {
            itemNames.add(names[i]);
            itemPrices.add(prices[i]);
        }
    }

    // ══════════════════════════════════════════
    //  ENTRY POINT
    // ══════════════════════════════════════════
    public static void main(String[] args) {
        printBanner();
        viewMenu();
        homeMenu();
        System.out.println("\n  Thank you for visiting ARBEE'S BAKERY! Goodbye!");
    }

    // ══════════════════════════════════════════
    //  BANNER
    // ══════════════════════════════════════════
    static void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║                                          ║");
        System.out.println("  ║          ARBEE'S BAKERY                  ║");
        System.out.println("  ║     Freshly Baked Goodness Every Day     ║");
        System.out.println("  ║                                          ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    // ══════════════════════════════════════════
    //  VIEW MENU
    // ══════════════════════════════════════════
    static void viewMenu() {
        System.out.println("\n╔═════════════════════════════════════════════╗");
        System.out.println("║          ARBEE'S BAKERY MENU                ║");
        System.out.println("╠═════╦═══════════════════════╦═══════════════╣");
        System.out.println("║ No. ║ Item                  ║ Price (PHP)   ║");
        System.out.println("╠═════╬═══════════════════════╬═══════════════╣");
        for (int i = 0; i < itemNames.size(); i++) {
            System.out.printf("║  %-2d ║ %-21s ║ %13.2f ║%n",
                i + 1, itemNames.get(i), itemPrices.get(i));
        }
        System.out.println("╚═════╩═══════════════════════╩═══════════════╝");
    }

    // ══════════════════════════════════════════
    //  HOME MENU
    // ══════════════════════════════════════════
    static void homeMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║      HOW CAN WE SERVE YOU?       ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  [1] Sign Up                     ║");
            System.out.println("║  [2] Customer Login              ║");
            System.out.println("║  [3] Staff Login                 ║");
            System.out.println("║  [4] Exit                        ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = validateInt(1, 4);
            switch (choice) {
                case 1 -> signUp();
                case 2 -> login();
                case 3 -> adminLogin();
                case 4 -> running = false;
            }
        }
    }

    // ══════════════════════════════════════════
    //  SIGN UP
    // ══════════════════════════════════════════
    static void signUp() {
        System.out.println("\n  ── SIGN UP ──────────────────────────");
        System.out.print("  Full Name    : ");
        String name  = sc.nextLine().trim();

        System.out.print("  Email        : ");
        String email = sc.nextLine().trim().toLowerCase();

        for (User u : users) {
            if (u.email.equals(email)) {
                System.out.println("  Email already registered. Please log in.");
                return;
            }
        }

        System.out.print("  Password     : ");
        String pass = sc.nextLine().trim();

        System.out.print("  Address      : ");
        String address = sc.nextLine().trim();

        users.add(new User(name, email, pass, address));
        System.out.println("  Account created successfully! You may now log in.");
    }

    // ══════════════════════════════════════════
    //  CUSTOMER LOGIN
    // ══════════════════════════════════════════
    static void login() {
        System.out.println("\n  ── CUSTOMER LOGIN ──────────────────");
        System.out.print("  Email    : ");
        String email = sc.nextLine().trim().toLowerCase();
        System.out.print("  Password : ");
        String pass  = sc.nextLine().trim();

        for (User u : users) {
            if (u.email.equals(email) && u.password.equals(pass)) {
                loggedIn = u;
                customerMenu();
                return;
            }
        }
        System.out.println("  Invalid email or password. Please try again.");
    }

    // ══════════════════════════════════════════
    //  CUSTOMER MENU
    // ══════════════════════════════════════════
    static void customerMenu() {
        boolean active = true;
        while (active) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.printf( "║  Hello, %-24s║%n", loggedIn.name + "!");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  [1] Place an Order              ║");
            System.out.println("║  [2] My Order History            ║");
            System.out.println("║  [3] Edit Profile                ║");
            System.out.println("║  [4] Logout                      ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = validateInt(1, 4);
            switch (choice) {
                case 1 -> orderingSystem();
                case 2 -> viewMyOrders();
                case 3 -> editProfile();
                case 4 -> {
                    System.out.println("\n  Logged out. See you again, " + loggedIn.name + "!");
                    loggedIn = null;
                    active   = false;
                    printBanner();
                    viewMenu();
                }
            }
        }
    }

    // ══════════════════════════════════════════
    //  CUSTOMER — View Own Order History
    // ══════════════════════════════════════════
    static void viewMyOrders() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    MY ORDER HISTORY                          ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        boolean found = false;
        for (OrderRecord rec : orderHistory) {
            if (rec.customerEmail.equals(loggedIn.email)) {
                found = true;
                System.out.println("\n  ┌──────────────────────────────────────────────────────────┐");
                System.out.println("  │  Order Ref    : " + rec.orderRef);
                System.out.println("  │  Date & Time  : " + rec.timestamp);
                System.out.println("  │  Status       : " + rec.status);
                System.out.println("  │  Address      : " + rec.deliveryAddress);
                System.out.println("  │  Payment      : " + rec.paymentMethod);
                System.out.println("  ├──────────────────────────────────────────────────────────┤");
                System.out.printf( "  │  %-24s  %4s  %12s  │%n", "Item", "Qty", "Amount (PHP)");
                System.out.println("  ├──────────────────────────────────────────────────────────┤");
                for (OrderItem oi : rec.items) {
                    System.out.printf("  │  %-24s  x%-3d  %12.2f  │%n",
                        oi.itemName, oi.quantity, oi.total);
                }
                System.out.println("  ├──────────────────────────────────────────────────────────┤");
                System.out.printf( "  │  %-24s        %12.2f  │%n", "GRAND TOTAL", rec.grandTotal);
                System.out.println("  └──────────────────────────────────────────────────────────┘");
            }
        }
        if (!found) {
            System.out.println("  You have no orders yet.");
        }
    }

    // ══════════════════════════════════════════
    //  CUSTOMER — Edit Profile
    // ══════════════════════════════════════════
    static void editProfile() {
        boolean editing = true;
        while (editing) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║           EDIT PROFILE           ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  [1] Update Address              ║");
            System.out.println("║  [2] Update Password             ║");
            System.out.println("║  [3] Back                        ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Enter choice: ");
            int choice = validateInt(1, 3);

            switch (choice) {
                case 1 -> {
                    System.out.println("  Current Address : " + loggedIn.address);
                    System.out.print("  New Address     : ");
                    String newAddr = sc.nextLine().trim();
                    loggedIn.address = newAddr;
                    System.out.println("   Address updated successfully!");
                }
                case 2 -> {
                    System.out.print("  Current Password : ");
                    String oldPass = sc.nextLine().trim();
                    if (!oldPass.equals(loggedIn.password)) {
                        System.out.println("   Incorrect current password.");
                    } else {
                        System.out.print("  New Password     : ");
                        String newPass = sc.nextLine().trim();
                        System.out.print("  Confirm Password : ");
                        String confirmPass = sc.nextLine().trim();
                        if (!newPass.equals(confirmPass)) {
                            System.out.println("   Passwords do not match.");
                        } else {
                            loggedIn.password = newPass;
                            System.out.println("   Password updated successfully!");
                        }
                    }
                }
                case 3 -> editing = false;
            }
        }
    }

    // ══════════════════════════════════════════
    //  ADMIN LOGIN
    // ══════════════════════════════════════════
    static void adminLogin() {
        System.out.println("\n  ── STAFF LOGIN ─────────────────────");
        System.out.print("  Username : ");
        String username = sc.nextLine().trim();
        System.out.print("  Password : ");
        String pass     = sc.nextLine().trim();

        if (username.equals(ADMIN_USER) && pass.equals(ADMIN_PASS)) {
            System.out.println("\n  Staff access granted. Welcome, " + ADMIN_USER + "!");
            adminDashboard();
        } else {
            System.out.println("  Invalid credentials. Access denied.");
        }
    }

    // ══════════════════════════════════════════
    //  ADMIN DASHBOARD
    // ══════════════════════════════════════════
    static void adminDashboard() {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          STAFF DASHBOARD             ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  [1] View All Registered Customers   ║");
            System.out.println("║  [2] View All Order History          ║");
            System.out.println("║  [3] View Sales Summary              ║");
            System.out.println("║  [4] View Current Menu & Prices      ║");
            System.out.println("║  [5] Manage Menu Items               ║");
            System.out.println("║  [6] Mark Order as Delivered         ║");
            System.out.println("║  [7] Logout Staff                    ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = validateInt(1, 7);
            switch (choice) {
                case 1 -> adminViewCustomers();
                case 2 -> adminViewOrders();
                case 3 -> adminViewSales();
                case 4 -> viewMenu();
                case 5 -> adminManageMenu();
                case 6 -> adminMarkDelivered();
                case 7 -> {
                    System.out.println("\n  Staff logged out. Returning to main menu...");
                    adminRunning = false;
                }
            }
        }
    }

    // ── Admin: View All Customers ──────────────
    static void adminViewCustomers() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       REGISTERED CUSTOMERS                                                          ║");
        System.out.println("╠══════╦════════════════════╦══════════════════════╦══════════════════════════════════════════════════╣");
        System.out.println("║  No. ║ Name               ║ Email                ║ Address                                          ║");
        System.out.println("╠══════╬════════════════════╬══════════════════════╬══════════════════════════════════════════════════╣");
        if (users.isEmpty()) {
            System.out.println("║                    No registered customers yet.                                                 ║");
        } else {
            for (int i = 0; i < users.size(); i++) {
                User u = users.get(i);
                System.out.printf("║  %-3d ║ %-18s ║ %-20s ║ %-22s ║%n",
                    i + 1, u.name, u.email, u.address);
            }
        }
        System.out.println("╚══════╩════════════════════╩══════════════════════╩═════════════════════════════════════════════════╝");
        System.out.printf("  Total registered customers: %d%n", users.size());
    }

    // ── Admin: View All Orders ─────────────────
    static void adminViewOrders() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     ORDER HISTORY                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        if (orderHistory.isEmpty()) {
            System.out.println("  No orders placed yet.");
            return;
        }
        for (int i = 0; i < orderHistory.size(); i++) {
            OrderRecord rec = orderHistory.get(i);
            System.out.println("\n  ── Order #" + (i + 1) + " ─────────────────────────────────────");
            System.out.println("  Order Ref      : " + rec.orderRef);
            System.out.println("  Date & Time    : " + rec.timestamp);
            System.out.println("  Status         : " + rec.status);
            System.out.println("  Customer       : " + rec.customerName);
            System.out.println("  Delivery Addr  : " + rec.deliveryAddress);
            System.out.println("  Payment Method : " + rec.paymentMethod);
            System.out.println("  ┌──────────────────────────────────────────────────┐");
            System.out.printf( "  │  %-24s  %4s  %12s  │%n", "Item", "Qty", "Amount (PHP)");
            System.out.println("  ├──────────────────────────────────────────────────┤");
            for (OrderItem oi : rec.items) {
                System.out.printf("  │  %-24s  x%-3d  %12.2f  │%n",
                    oi.itemName, oi.quantity, oi.total);
            }
            System.out.println("  ├──────────────────────────────────────────────────┤");
            System.out.printf( "  │  %-24s        %12.2f  │%n", "GRAND TOTAL", rec.grandTotal);
            System.out.println("  └──────────────────────────────────────────────────┘");
        }
        System.out.printf("%n  Total orders placed: %d%n", orderHistory.size());
    }

    // ── Admin: Sales Summary ───────────────────
    static void adminViewSales() {
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     SALES SUMMARY                            ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        if (orderHistory.isEmpty()) {
            System.out.println("  No sales recorded yet.");
            return;
        }

        double totalRevenue = 0;
        int    totalOrders  = orderHistory.size();

        int[]    soldQty = new int[itemNames.size()];
        double[] soldRev = new double[itemNames.size()];

        for (OrderRecord rec : orderHistory) {
            totalRevenue += rec.grandTotal;
            for (OrderItem oi : rec.items) {
                for (int i = 0; i < itemNames.size(); i++) {
                    if (itemNames.get(i).equals(oi.itemName)) {
                        soldQty[i] += oi.quantity;
                        soldRev[i] += oi.total;
                    }
                }
            }
        }

        System.out.println("\n  ── Per-Item Sales ─────────────────────────────────────────");
        System.out.printf( "  %-22s  %8s  %14s%n", "Item", "Qty Sold", "Revenue (PHP)");
        System.out.println("  ──────────────────────────────────────────────────────────");
        boolean anySales = false;
        for (int i = 0; i < itemNames.size(); i++) {
            if (soldQty[i] > 0) {
                anySales = true;
                System.out.printf("  %-22s  %8d  %14.2f%n",
                    itemNames.get(i), soldQty[i], soldRev[i]);
            }
        }
        if (!anySales) System.out.println("  No items sold yet.");
        System.out.println("  ──────────────────────────────────────────────────────────");
        System.out.printf( "  Total Orders : %d%n", totalOrders);
        System.out.printf( "  Total Revenue: PHP %.2f%n", totalRevenue);
    }

    // ── Admin: Manage Menu Items ───────────────
    static void adminManageMenu() {
        boolean managing = true;
        while (managing) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║         MANAGE MENU ITEMS        ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  [1] Add New Item                ║");
            System.out.println("║  [2] Remove an Item              ║");
            System.out.println("║  [3] Update Item Price           ║");
            System.out.println("║  [4] Update Item Name            ║");
            System.out.println("║  [5] Back to Dashboard           ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Enter choice: ");
            int choice = validateInt(1, 5);

            switch (choice) {
                case 1 -> {
                    System.out.print("\n  New Item Name  : ");
                    String newName = sc.nextLine().trim();

                    // ── Check if item already exists in the menu ──
                    boolean alreadyExists = false;
                    for (String existing : itemNames) {
                        if (existing.equalsIgnoreCase(newName)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                    if (alreadyExists) {
                        System.out.println("  The item already existed in the menu.");
                        break;
                    }

                    System.out.print("  Price (PHP)    : ");
                    double newPrice = validateDouble();
                    itemNames.add(newName);
                    itemPrices.add(newPrice);
                    System.out.println("  \"" + newName + "\" added to menu at PHP " + String.format("%.2f", newPrice));
                }
                case 2 -> {
                    viewMenu();
                    System.out.print("\n  Enter item number to remove: ");
                    int idx = validateInt(1, itemNames.size());
                    String removed = itemNames.get(idx - 1);
                    itemNames.remove(idx - 1);
                    itemPrices.remove(idx - 1);
                    System.out.println("  \"" + removed + "\" removed from menu.");
                }
                case 3 -> {
                    viewMenu();
                    System.out.print("\n  Enter item number to update price: ");
                    int idx = validateInt(1, itemNames.size());
                    System.out.printf("  Current Price of %s: PHP %.2f%n",
                        itemNames.get(idx - 1), itemPrices.get(idx - 1));
                    System.out.print("  New Price (PHP): ");
                    double newPrice = validateDouble();
                    itemPrices.set(idx - 1, newPrice);
                    System.out.println("  Price updated to PHP " + String.format("%.2f", newPrice));
                }
                case 4 -> {
                    viewMenu();
                    System.out.print("\n  Enter item number to rename: ");
                    int idx = validateInt(1, itemNames.size());
                    System.out.println("  Current Name: " + itemNames.get(idx - 1));
                    System.out.print("  New Name    : ");
                    String newName = sc.nextLine().trim();
                    itemNames.set(idx - 1, newName);
                    System.out.println("  Item renamed to \"" + newName + "\".");
                }
                case 5 -> managing = false;
            }
        }
    }

    // ── Admin: Mark Order as Delivered ─────────
    static void adminMarkDelivered() {
        if (orderHistory.isEmpty()) {
            System.out.println("\n  No orders to update.");
            return;
        }

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║               PENDING / IN-PROGRESS ORDERS                  ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");

        boolean anyPending = false;
        for (int i = 0; i < orderHistory.size(); i++) {
            OrderRecord rec = orderHistory.get(i);
            if (!rec.status.equals("Delivered")) {
                anyPending = true;
                System.out.printf("  [%d] Ref: %-12s | Customer: %-18s | Status: %s%n",
                    i + 1, rec.orderRef, rec.customerName, rec.status);
            }
        }

        if (!anyPending) {
            System.out.println("  All orders have been delivered.");
            return;
        }

        System.out.print("\n  Enter order number to mark as Delivered (0 to cancel): ");
        int idx = validateInt(0, orderHistory.size());
        if (idx == 0) return;

        OrderRecord selected = orderHistory.get(idx - 1);
        if (selected.status.equals("Delivered")) {
            System.out.println("  This order is already marked as Delivered.");
        } else {
            selected.status = "Delivered";
            System.out.println("  Order " + selected.orderRef + " marked as Delivered.");
        }
    }

    // ══════════════════════════════════════════
    //  ORDERING SYSTEM
    // ══════════════════════════════════════════
    static void orderingSystem() {
        cart.clear();
        boolean ordering = true;
        while (ordering) {
            viewMenu();

            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║         ORDERING SYSTEM          ║");
            System.out.println("╠══════════════════════════════════╣");
            System.out.println("║  [1] Add Item to Cart            ║");
            System.out.println("║  [2] View Cart & Checkout        ║");
            System.out.println("║  [3] Back to Customer Menu       ║");
            System.out.println("╚══════════════════════════════════╝");
            System.out.print("  Enter choice: ");

            int choice = validateInt(1, 3);
            switch (choice) {
                case 1 -> {
                    addToCart();
                    // After adding, ask if they want to add another or go to cart
                    boolean addMore = true;
                    while (addMore) {
                        System.out.println("\n╔══════════════════════════════════════╗");
                        System.out.println("║ Would you like to add another order? ║");
                        System.out.println("╠══════════════════════════════════════╣");
                        System.out.println("║  [1] Yes, add more items             ║");
                        System.out.println("║  [2] No, go to cart                  ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        System.out.print("  Enter choice: ");
                        int more = validateInt(1, 2);
                        if (more == 1) {
                            viewMenu();
                            addToCart();
                        } else {
                            // [2] No → go directly to View Cart & Checkout
                            addMore = false;
                            if (checkout()) ordering = false;
                        }
                    }
                }
                case 2 -> {
                    if (checkout()) ordering = false;
                }
                case 3 -> ordering = false;
            }
        }
    }

    // ══════════════════════════════════════════
    //  ADD TO CART
    // ══════════════════════════════════════════
    static void addToCart() {
        System.out.print("\n  Select item number (1-" + itemNames.size() + "): ");
        int itemNo = validateInt(1, itemNames.size());

        System.out.print("  Enter quantity: ");
        int qty = validateInt(1, 9999);

        String name  = itemNames.get(itemNo - 1);
        double price = itemPrices.get(itemNo - 1);

        for (OrderItem oi : cart) {
            if (oi.itemName.equals(name)) {
                oi.quantity += qty;
                oi.total     = oi.price * oi.quantity;
                System.out.printf("  Updated cart: %s x%d @ PHP %.2f each%n",
                    name, oi.quantity, price);
                return;
            }
        }

        cart.add(new OrderItem(name, qty, price));
        System.out.printf("  Added to cart: %s x%d @ PHP %.2f each%n",
            name, qty, price);
    }

    // ══════════════════════════════════════════
    //  REMOVE FROM CART
    // ══════════════════════════════════════════
    static void removeFromCart() {
        if (cart.isEmpty()) {
            System.out.println("  Your cart is already empty.");
            return;
        }
        System.out.println("\n  Enter the item number you want to cancel/remove:");
        System.out.print("  Item No.: ");
        int idx = validateInt(1, cart.size());
        OrderItem removed = cart.remove(idx - 1);
        System.out.printf("  Removed: %s x%d from your cart.%n",
            removed.itemName, removed.quantity);
        if (cart.isEmpty()) {
            System.out.println("  Your cart is now empty.");
        }
    }

    // ══════════════════════════════════════════
    //  CHECKOUT
    // ══════════════════════════════════════════
    static boolean checkout() {
        boolean inCheckout = true;
        while (inCheckout) {
            if (cart.isEmpty()) {
                System.out.println("  Your cart is empty. Add items first.");
                return false;
            }

            System.out.println("\n╔══════╦════════════════════════╦═══════╦════════════════════╗");
            System.out.println("║  No. ║ Item                   ║  Qty  ║ Total (PHP)        ║");
            System.out.println("╠══════╬════════════════════════╬═══════╬════════════════════╣");
            double grandTotal = 0;
            for (int i = 0; i < cart.size(); i++) {
                OrderItem oi = cart.get(i);
                System.out.printf("║  %-3d ║ %-22s ║  %-4d ║ %18.2f ║%n",
                    i + 1, oi.itemName, oi.quantity, oi.total);
                grandTotal += oi.total;
            }
            System.out.println("╠══════╩════════════════════════╩═══════╬════════════════════╣");
            System.out.printf( "║            GRAND TOTAL               ║ %18.2f ║%n", grandTotal);
            System.out.println("╚══════════════════════════════════════╩════════════════════╝");

            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║           CART OPTIONS               ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  [1] Confirm Order                   ║");
            System.out.println("║  [2] Add Quantity      [ + ]         ║");
            System.out.println("║  [3] Minus Quantity    [ - ]         ║");
            System.out.println("║  [4] Remove an Item                  ║");
            System.out.println("║  [5] Back to Ordering Menu           ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("  Enter choice: ");
            int cartChoice = validateInt(1, 5);

            switch (cartChoice) {
                case 1 -> {
                    System.out.print("\n  Confirm order? (Y/N): ");
                    String confirm = sc.nextLine().trim().toUpperCase();
                    if (!confirm.equals("Y")) {
                        System.out.println("  Order not confirmed. Returning to cart.");
                    } else {
                        // Delivery address confirmation
                        System.out.println("\n  ── DELIVERY ADDRESS CONFIRMATION ────────");
                        System.out.println("  Address on file: " + loggedIn.address);
                        System.out.print("  Is this your delivery address? (Y/N): ");
                        String sameAddr = sc.nextLine().trim().toUpperCase();
                        String deliveryAddress;
                        if (sameAddr.equals("Y")) {
                            deliveryAddress = loggedIn.address;
                        } else {
                            System.out.print("  Enter your current delivery address: ");
                            deliveryAddress = sc.nextLine().trim();
                        }
                        System.out.println("  Delivery Address: " + deliveryAddress);

                        // Add another order?
                        System.out.println("\n╔══════════════════════════════════════╗");
                        System.out.println("║ Would you like to add another order? ║");
                        System.out.println("╠══════════════════════════════════════╣");
                        System.out.println("║  [1] Yes, add more items             ║");
                        System.out.println("║  [2] No, proceed to payment          ║");
                        System.out.println("╚══════════════════════════════════════╝");
                        System.out.print("  Enter choice: ");
                        int addMore = validateInt(1, 2);
                        if (addMore == 1) {
                            boolean keepAdding = true;
                            while (keepAdding) {
                                viewMenu();
                                addToCart();
                                System.out.println("\n╔══════════════════════════════════════╗");
                                System.out.println("║ Would you like to add another order? ║");
                                System.out.println("╠══════════════════════════════════════╣");
                                System.out.println("║  [1] Yes, add more items             ║");
                                System.out.println("║  [2] No, proceed to payment          ║");
                                System.out.println("╚══════════════════════════════════════╝");
                                System.out.print("  Enter choice: ");
                                int cont = validateInt(1, 2);
                                if (cont == 2) keepAdding = false;
                            }
                        }

                        double finalTotal = 0;
                        for (OrderItem oi : cart) finalTotal += oi.total;
                        boolean paid = payment(finalTotal, deliveryAddress);
                        if (paid) {
                            cart.clear();
                            return true;
                        }
                    }
                }
                case 2 -> {
                    // Add quantity [ + ]
                    System.out.print("\n  Enter item number to increase quantity [ + ]: ");
                    int addIdx = validateInt(1, cart.size());
                    System.out.print("  How many to add: ");
                    int addQty = validateInt(1, 9999);
                    OrderItem addItem = cart.get(addIdx - 1);
                    addItem.quantity += addQty;
                    addItem.total     = addItem.price * addItem.quantity;
                    System.out.printf("  + %s updated to x%d  (PHP %.2f total)%n",
                        addItem.itemName, addItem.quantity, addItem.total);
                }
                case 3 -> {
                    // Minus quantity [ - ]
                    System.out.print("\n  Enter item number to decrease quantity [ - ]: ");
                    int minIdx = validateInt(1, cart.size());
                    OrderItem minItem = cart.get(minIdx - 1);
                    System.out.printf("  Current quantity of %s: %d%n",
                        minItem.itemName, minItem.quantity);
                    System.out.print("  How many to subtract: ");
                    int minQty = validateInt(1, minItem.quantity);
                    minItem.quantity -= minQty;
                    if (minItem.quantity <= 0) {
                        cart.remove(minIdx - 1);
                        System.out.printf("  - %s removed from cart (quantity reached 0).%n",
                            minItem.itemName);
                    } else {
                        minItem.total = minItem.price * minItem.quantity;
                        System.out.printf("  - %s updated to x%d  (PHP %.2f total)%n",
                            minItem.itemName, minItem.quantity, minItem.total);
                    }
                }
                case 4 -> removeFromCart();
                case 5 -> inCheckout = false;
            }
        }
        return false;
    }

    // ══════════════════════════════════════════
    //  PAYMENT
    // ══════════════════════════════════════════
    static boolean payment(double grandTotal, String deliveryAddress) {
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.println("║         SELECT PAYMENT METHOD        ║");
        System.out.println("╠══════════════════════════════════════╣");
        System.out.println("║  [1] Cash on Delivery                ║");
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print("  Choose payment method [1]: ");
        int method = validateInt(1, 1);

        String chosenMethod = "Cash on Delivery";

        // Generate order reference and timestamp
        orderCounter++;
        String orderRef   = "ARB-" + orderCounter;
        String timestamp  = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd  hh:mm:ss a"));

        // Print receipt
        System.out.println("\n  ══════════════════════════════════════");
        System.out.println("              ARBEE'S BAKERY");
        System.out.println("           -- Official Receipt --");
        System.out.println("  ══════════════════════════════════════");
        System.out.println("  Order Ref  : " + orderRef);
        System.out.println("  Date & Time: " + timestamp);
        System.out.println("  Customer   : " + loggedIn.name);
        System.out.println("  Address    : " + deliveryAddress);
        System.out.println("  ──────────────────────────────────────");
        System.out.printf( "  %-22s  %4s  %10s%n", "Item", "Qty", "Amount");
        System.out.println("  ──────────────────────────────────────");
        for (OrderItem oi : cart) {
            System.out.printf("  %-22s  x%-3d  PHP %6.2f%n",
                oi.itemName, oi.quantity, oi.total);
        }
        System.out.println("  ──────────────────────────────────────");
        System.out.printf( "  %-22s        PHP %6.2f%n", "GRAND TOTAL", grandTotal);
        System.out.println("  ══════════════════════════════════════");

        System.out.println("\n  Here's your receipt!");
        System.out.println("  Payment Method : Cash on Delivery");
        System.out.printf( "  Amount Due     : PHP %.2f%n", grandTotal);
        System.out.println("  Please pay the exact amount upon delivery.");

        System.out.println("\n  ╔══════════════════════════════════════╗");
        System.out.println("  ║    PAYMENT SUCCESSFUL! Thank you!    ║");
        System.out.println("  ║    Your order is being prepared.     ║");
        System.out.println("  ╚══════════════════════════════════════╝");

        // Save order to history
        orderHistory.add(new OrderRecord(
            orderRef, loggedIn.name, loggedIn.email,
            deliveryAddress, chosenMethod, timestamp, cart, grandTotal));

        System.out.println("\n  Returning you to your account menu...");

        return true;
    }

    // ══════════════════════════════════════════
    //  HELPER — validate integer in range
    // ══════════════════════════════════════════
    static int validateInt(int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.printf("  Invalid input!\n Please enter a number between %d and %d: ", min, max);
            } catch (NumberFormatException e) {
                System.out.printf("  Invalid input!\n Please enter a number between %d and %d: ", min, max);
            }
        }
    }

    // ── validate positive double ───────────────
    static double validateDouble() {
        while (true) {
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val > 0) return val;
                System.out.print("  Invalid input! Please enter a positive amount: ");
            } catch (NumberFormatException e) {
                System.out.print("  Invalid input! Please enter a valid number: ");
            }
        }
    }
}