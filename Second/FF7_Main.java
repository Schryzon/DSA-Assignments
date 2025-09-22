/**
 * Interactive FFVII linked-list manager.
 *
 * Provides:
 *  - Party (Doubly linked list) CRUD
 *  - Inventory (Singly linked list) CRUD
 *  - Materia ring (Circular linked list) CRUD + rotate simulation
 *
 * Compile:
 *   javac Nodes\*.java Lists\*.java FF7_Types.java FF7_Main.java
 * Run:
 *   java -cp . FF7_MainInteractive
 */
import java.util.Scanner;
import Nodes.*;
import Lists.*;

public class FF7_Main{
    private static final Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("=== FINAL FANTASY VII ===");

        // Lists (use your concrete list implementations)
        Doubly_Linked_List<FF7_Types.Character> party = new Doubly_Linked_List<>();
        Singly_Linked_List<String> inventory = new Singly_Linked_List<>();
        Circular_Linked_List<FF7_Types.Materia> materia_ring = new Circular_Linked_List<>();

        // seed some entries (optional)
        party.add(new FF7_Types.Character("Cloud", 1200, 50));
        party.add(new FF7_Types.Character("Tifa", 1100, 48));
        inventory.add("Potion");
        inventory.add("Ether");
        materia_ring.add(new FF7_Types.Materia("Fire", 3));
        materia_ring.add(new FF7_Types.Materia("Cure", 2));

        main_loop:
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println(" 1) Party (manage characters)");
            System.out.println(" 2) Inventory (items)");
            System.out.println(" 3) Materia Ring");
            System.out.println(" 4) Show summary");
            System.out.println(" 0) Exit");
            System.out.print("> ");

            String choice = input.nextLine().trim();
            clear_screen();
            switch (choice) {
                case "1":
                    party_menu(party);
                    break;
                case "2":
                    inventory_menu(inventory);
                    break;
                case "3":
                    materia_menu(materia_ring);
                    break;
                case "4":
                    show_summary(party, inventory, materia_ring);
                    break;
                case "0":
                    System.out.println("Program exited, until then!");
                    break main_loop;
                default:
                    System.out.println("Invalid option, try again.");
            }
            wait_for_enter();
            clear_screen();
        }
        input.close();
    }

    /**
     * Party submenu: add, remove, list, find, edit hp/level.
     */
    private static void party_menu(Doubly_Linked_List<FF7_Types.Character> party) {
        while (true) {
            System.out.println("\nParty Menu:");
            System.out.println(" 1) List party members");
            System.out.println(" 2) Add member");
            System.out.println(" 3) Remove member (by name)");
            System.out.println(" 4) Find member (by name)");
            System.out.println(" 5) Edit member (hp / level)");
            System.out.println(" 0) Back");
            System.out.print("> ");

            String c = input.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.println("\n-- Party members --");
                    party.for_each(ch -> System.out.println(" - " + ch));
                    System.out.println("-- END --");
                    break;
                case "2":
                    System.out.print("Name: ");
                    String name = input.nextLine().trim();
                    int hp = prompt_int("HP: ", 1, Integer.MAX_VALUE);
                    int level = prompt_int("Level: ", 1, 999);
                    party.add(new FF7_Types.Character(name, hp, level));
                    System.out.println("Added " + name);
                    break;
                case "3":
                    System.out.print("Name to remove: ");
                    String rem_name = input.nextLine().trim();
                    boolean removed = party.remove(new FF7_Types.Character(rem_name, 0, 0));
                    System.out.println("Removed? " + removed);
                    break;
                case "4":
                    System.out.print("Name to find: ");
                    String find_name = input.nextLine().trim();
                    Node<FF7_Types.Character> found = party.find(new FF7_Types.Character(find_name, 0, 0));
                    if (found != null) {
                        System.out.println("Found -> " + found.get_data());
                    } else {
                        System.out.println("Not found.");
                    }
                    break;
                case "5":
                    System.out.print("Name to edit: ");
                    String edit_name = input.nextLine().trim();
                    Node<FF7_Types.Character> node = party.find(new FF7_Types.Character(edit_name, 0, 0));
                    if (node == null) {
                        System.out.println("No such member.");
                        break;
                    }
                    FF7_Types.Character ch = node.get_data();
                    System.out.println("Editing " + ch);
                    int new_hp = prompt_int("New HP (" + ch.hp + "): ", 1, Integer.MAX_VALUE);
                    int new_level = prompt_int("New Level (" + ch.level + "): ", 1, 999);
                    ch.hp = new_hp;
                    ch.level = new_level;
                    System.out.println("Updated -> " + ch);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            wait_for_enter();
            clear_screen();
        }
    }

    /**
     * Inventory submenu: add, remove, list.
     */
    private static void inventory_menu(Singly_Linked_List<String> inventory) {
        while (true) {
            System.out.println("\nInventory Menu:");
            System.out.println(" 1) List items");
            System.out.println(" 2) Add item");
            System.out.println(" 3) Remove item (by name)");
            System.out.println(" 0) Back");
            System.out.print("> ");

            String c = input.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.println("\n-- Inventory --");
                    inventory.for_each(it -> System.out.println(" * " + it));
                    System.out.println("-- END --");
                    break;
                case "2":
                    System.out.print("Item name: ");
                    String item = input.nextLine().trim();
                    inventory.add(item);
                    System.out.println("Added " + item);
                    break;
                case "3":
                    System.out.print("Item to remove: ");
                    String rem = input.nextLine().trim();
                    boolean ok = inventory.remove(rem);
                    System.out.println("Removed? " + ok);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            wait_for_enter();
            clear_screen();
        }
    }

    /**
     * Materia submenu: add, remove, list, rotate simulation.
     */
    private static void materia_menu(Circular_Linked_List<FF7_Types.Materia> ring) {
        while (true) {
            System.out.println("\nMateria Menu:");
            System.out.println(" 1) List materia");
            System.out.println(" 2) Add materia");
            System.out.println(" 3) Remove materia (by name+grade)");
            System.out.println(" 4) Rotate (simulate advancing slots)");
            System.out.println(" 0) Back");
            System.out.print("> ");

            String c = input.nextLine().trim();
            switch (c) {
                case "1":
                    System.out.println("\n-- Materia ring --");
                    ring.for_each(m -> System.out.println(" ~ " + m));
                    System.out.println("-- END --");
                    break;
                case "2":
                    System.out.print("Materia name: ");
                    String name = input.nextLine().trim();
                    int grade = prompt_int("Grade: ", 1, 9);
                    ring.add(new FF7_Types.Materia(name, grade));
                    System.out.println("Added " + name);
                    break;
                case "3":
                    System.out.print("Materia name to remove: ");
                    String rname = input.nextLine().trim();
                    int rgrade = prompt_int("Grade: ", 1, 9);
                    boolean r = ring.remove(new FF7_Types.Materia(rname, rgrade));
                    System.out.println("Removed? " + r);
                    break;
                case "4":
                    rotate_materia_sim(ring);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Invalid option.");
            }
            wait_for_enter();
            clear_screen();
        }
    }

    /**
     * Simulate rotating the circular materia ring starting from
     * either a named materia or the first present entry.
     *
     * Implementation note: cannot directly access protected head,
     * so use find() and for_each() to obtain a starting Node reference.
     */
    private static void rotate_materia_sim(Circular_Linked_List<FF7_Types.Materia> ring) {
        int size = ring.get_size();
        if (size == 0) {
            System.out.println("Ring is empty.");
            return;
        }

        System.out.print("Start from materia name (leave empty to use first): ");
        String start_name = input.nextLine().trim();

        Node<FF7_Types.Materia> start_node = null;

        if (start_name.length() > 0) {
            // Build a query materia with same name (grade unknown -> try several)
            // Attempt to find by name by scanning and comparing name field.
            // Because find() compares objects via equals, and FF7_Types.Materia.equals requires exact grade,
            // Fallback to scanning with a for_each to capture Node if name matches.
            final NodeContainer first_match = new NodeContainer();
            ring.for_each(m -> {
                if (first_match.node == null && m.name.equals(start_name)) {
                    // ??? We want the Node, but for_each only gives data object.
                    // So capture the data; then find the node with exact object.
                    first_match.captured_data = m;
                }
            });
            if (first_match.captured_data != null) {
                start_node = ring.find(first_match.captured_data);
            } else {
                System.out.println("Named materia not found. Using first element instead.");
            }
        }

        // If still no start_node, pick the first element captured by for_each
        if (start_node == null) {
            final NodeContainer any = new NodeContainer();
            ring.for_each(m -> {
                if (any.captured_data == null) {
                    any.captured_data = m;
                }
            });
            start_node = ring.find(any.captured_data);
        }

        if (start_node == null) {
            System.out.println("Could not obtain starting node (unexpected).");
            return;
        }

        int steps = prompt_int("How many steps to simulate? ", 1, 1000);
        System.out.println("Rotating starting at -> " + start_node.get_data());
        Node<FF7_Types.Materia> cur = start_node;
        int step = 0;
        while (step < steps && cur != null) {
            System.out.println(" Step " + step + " -> " + cur.get_data());
            cur = cur.get_next();
            step++;
            // Safety: break if looped more than size*2 (prevent infinite)
            if (step > size * 2) {
                System.out.println("Safety break triggered!");
                break;
            }
        }
    }

    /**
     * Helper: prompt integer with min/max validation.
     */
    private static int prompt_int(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                    continue;
                }
                return v;
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    /**
     * Print quick sizes and short lists.
     */
    private static void show_summary(Doubly_Linked_List<FF7_Types.Character> party,
                                     Singly_Linked_List<String> inventory,
                                     Circular_Linked_List<FF7_Types.Materia> ring) {
        System.out.println("\n=== Summary ===");
        System.out.println(" Party size: " + party.get_size());
        System.out.println(" Inventory size: " + inventory.get_size());
        System.out.println(" Materia size: " + ring.get_size());
        System.out.println("\nParty:");
        party.for_each(ch -> System.out.println(" - " + ch));
        System.out.println("\nInventory:");
        inventory.for_each(it -> System.out.println(" * " + it));
        System.out.println("\nMateria:");
        ring.for_each(m -> System.out.println(" ~ " + m));
    }

    private static void clear_screen(){
        try{
            if(System.getProperty("os.name").contains("Windows")){
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            }else{
                System.out.print("\033[H\033[2J");  
                System.out.flush();
            }
        }catch(Exception e){
            System.out.println("Could not clear screen.");
        }
    }

    private static void wait_for_enter() {
        System.out.println("\nPress ENTER to continue...");
        input.nextLine();
    }

    /**
     * Small mutable container so lambda can write a Node-like result.
     * Only store the captured data object then call find() to get Node.
     */
    private static class NodeContainer {
        public FF7_Types.Materia captured_data = null;
        public Node<FF7_Types.Materia> node = null;
    }
}
