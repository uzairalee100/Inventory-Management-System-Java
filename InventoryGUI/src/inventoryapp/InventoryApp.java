package inventoryapp;

import java.util.Scanner;

public class InventoryApp {

    // ---------- Product Node ----------
    public static class Product {
        public  int id;
        public String name;
        public int quantity;
        public double price;
        public Product next;

        Product(int id, String name, int quantity, double price) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.next = null;
        }

        @Override
        public String toString() {
            return String.format("ID: %d | Name: %s | Qty: %d | Price: %.2f",
                    id, name, quantity, price);
        }
    }

    // ---------- Inventory (Linked List) ----------
    public static class Inventory {
        private Product head;
        private static int nextId = 1005; // Auto ID starts from 1001

        public Inventory() {
            head = null;
        }

        // Add product (auto ID)
        public Product addProduct(String name, int quantity, double price) {
            Product newProd = new Product(nextId++, name, quantity, price);
            if (head == null) {
                head = newProd;
            } else {
                Product curr = head;
                while (curr.next != null)
                    curr = curr.next;
                curr.next = newProd;
            }
            return newProd;
        }

        // Read: Display all products
        public void displayInventory() {
            if (head == null) {
                System.out.println("Inventory is empty.");
                return;
            }
            System.out.println("Current Inventory:");
            Product curr = head;
            while (curr != null) {
                System.out.println(curr);
                curr = curr.next;
            }
        }

        // Search: Find product by ID
        public Product searchById(int id) {
            Product curr = head;
            while (curr != null) {
                if (curr.id == id)
                    return curr;
                curr = curr.next;
            }
            return null;
        }

        // Update: Modify product info by ID
        public boolean updateProduct(int id, String newName, Integer newQuantity, Double newPrice) {
            Product node = searchById(id);
            if (node == null)
                return false;
            if (newName != null)
                node.name = newName;
            if (newQuantity != null)
                node.quantity = newQuantity;
            if (newPrice != null)
                node.price = newPrice;
            return true;
        }

        // Delete: Remove product by ID
        public boolean deleteProduct(int id) {
            if (head == null)
                return false;
            if (head.id == id) {
                head = head.next;
                return true;
            }
            Product prev = head;
            Product curr = head.next;
            while (curr != null) {
                if (curr.id == id) {
                    prev.next = curr.next;
                    return true;
                }
                prev = curr;
                curr = curr.next;
            }
            return false;
        }

        // Count: Number of products
        public int size() {
            int count = 0;
            Product curr = head;
            while (curr != null) {
                count++;
                curr = curr.next;
            }
            return count;
        }
        
        // Return all products as an array (for JTable display)
public Product[] getAllProducts() {
    int count = size();
    Product[] arr = new Product[count];
    Product curr = head;
    int i = 0;
    while (curr != null) {
        arr[i++] = curr;
        curr = curr.next;
    }
    return arr;
}

public int getNextId() {
    return nextId;
}

    }

    // ---------- Main Menu ----------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Inventory inventory = new Inventory();

        while (true) {
            printMenu();
            System.out.print("Enter choice: ");
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1": // Add product
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine().trim();
                    System.out.print("Enter quantity: ");
                    int qty = Integer.parseInt(sc.nextLine().trim());
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(sc.nextLine().trim());

                    Product added = inventory.addProduct(name, qty, price);
                    System.out.println("Product added successfully ");//with ID: + added.id
                    break;

                case "2": // Display
                    inventory.displayInventory();
                    break;

                case "3": // Update
                    System.out.print("Enter product ID to update: ");
                    int uid = Integer.parseInt(sc.nextLine().trim());
                    Product found = inventory.searchById(uid);
                    if (found == null) {
                        System.out.println("Product not found with ID " + uid);
                        break;
                    }
                    System.out.println("Found: " + found);
                    System.out.println("Leave field empty to keep it unchanged.");

                    System.out.print("New name: ");
                    String newName = sc.nextLine().trim();
                    if (newName.isEmpty())
                        newName = null;

                    System.out.print("New quantity: ");
                    String qStr = sc.nextLine().trim();
                    Integer newQty = qStr.isEmpty() ? null : Integer.parseInt(qStr);

                    System.out.print("New price: ");
                    String pStr = sc.nextLine().trim();
                    Double newPrice = pStr.isEmpty() ? null : Double.parseDouble(pStr);

                    boolean updated = inventory.updateProduct(uid, newName, newQty, newPrice);
                    if (updated)
                        System.out.println("Product updated successfully.");
                    else
                        System.out.println("Update failed.");
                    break;

                case "4": // Delete
                    System.out.print("Enter product ID to delete: ");
                    int did = Integer.parseInt(sc.nextLine().trim());
                    boolean deleted = inventory.deleteProduct(did);
                    if (deleted)
                        System.out.println("Product deleted successfully.");
                    else
                        System.out.println("Product not found.");
                    break;

                case "5": // Search
                    System.out.print("Enter product ID to search: ");
                    int sid = Integer.parseInt(sc.nextLine().trim());
                    Product p = inventory.searchById(sid);
                    if (p == null)
                        System.out.println("Product not found.");
                    else
                        System.out.println("Found: " + p);
                    break;

                case "6": // Size
                    System.out.println("Total products in inventory: " + inventory.size());
                    break;

                case "0": // Exit
                    System.out.println("Exiting program. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }

            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("===== Inventory Management Menu =====");
        System.out.println("1. Add product (Create)");
        System.out.println("2. Display inventory (Read)");
        System.out.println("3. Update product (Update)");
        System.out.println("4. Delete product (Delete)");
        System.out.println("5. Search product by ID");
        System.out.println("6. Inventory size");
        System.out.println("0. Exit");
        System.out.println("=====================================");
    }
}
