import java.util.Scanner;
import java.util.*;
import java.io.File;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

class FoodClass {
    File NewFile = new File("orderdetails.csv");
    static Scanner sc1 = new Scanner(System.in);
    static ArrayList<ArrayList<String>> ItemsMenu = new ArrayList<ArrayList<String>>();
    static ArrayList<ArrayList<String>> FoodPlan = new ArrayList<ArrayList<String>>();

    static void Readingfile(String fileName, int fun) {
        try {
            String line = "";
            Scanner br = new Scanner(new FileReader(fileName));
            while ((br.hasNext())) {
                line = br.nextLine();
                String[] FoodItems1 = line.split(",");
                List<String> fixedLenghtList = Arrays.asList(FoodItems1);
                ArrayList<String> Fooditems = new ArrayList<String>(fixedLenghtList);
                if (fun == 0)
                    ItemsMenu.add(Fooditems);
                else
                    FoodPlan.add(Fooditems);
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Runtime Error");
        }
    }

    static double CalculateAmount(int id, int q) {
        double v = 0;
        for (int i = 0; i < ItemsMenu.size(); i++) {
            ArrayList<String> dump = ItemsMenu.get(i);
            if (id == Integer.parseInt(dump.get(0))) {
                v += (q * Integer.parseInt(dump.get(2)));
            }
        }
        return v;
    }

    static String ConfirmOrder(String m[]) {
        int Amount = 0;
        System.out.println();
        System.out.println("Ordered Items");
        for (int k = 0; k < m.length; k += 2) {
            for (int j = 0; j < ItemsMenu.size(); j++) {
                ArrayList<String> dump = ItemsMenu.get(j);
                if (Integer.parseInt(m[k]) == Integer.parseInt(dump.get(0))) {
                    System.out.println("[ " + dump.get(1) + "    ---->  Cost  :  Rs."
                            + Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2)) + " ---->  Qty : "
                            + Integer.parseInt(m[k + 1]) + " ]");
                    Amount += Integer.parseInt(m[k + 1]) * Integer.parseInt(dump.get(2));
                }
            }
        }
        System.out.println();
        System.out.println("Total Bill : Rs." + Amount);
        System.out.println();
        System.out.println("Enter 'y' to Confirm order");
        char c = sc1.next().charAt(0);
        if (c == 'y')
             return ", Approved";
            //System.out.println(",Approved");
        return ", Cancelled";
        //System.out.println("Canceled");
    }

    static void newOrder() {
        String st = "\n", Food = "";
        ArrayList<String> dummy = FoodPlan.get(FoodPlan.size() - 1);
        int lastIndex = Integer.parseInt(dummy.get(0));
        double totalAmount = 0;
        int i = 1, it, count;
        while (true) {
            System.out.println();
            System.out.println("Enter Order Details");
            System.out.print("Item:" + i + "  Enter ItemId : ");
            it = sc1.nextInt();
            System.out.print("Item:" + i + "  Quantity : ");
            count = sc1.nextInt();
            i++;
            totalAmount += CalculateAmount(it, count);
            Food += String.valueOf(it) + " " + String.valueOf(count) + " ";
            System.out.println();
            System.out.println("Press y to palce Another order");
            char c = sc1.next().charAt(0);
            if (c != 'y')
                break;
        }
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        st += String.valueOf((lastIndex + 1)) + "," + formattedDate + "," + String.valueOf(totalAmount) + ',' + Food;
        String[] m = Food.split(" ");
        st += ConfirmOrder(m);
        sc1.nextLine();


        try {
            byte[] ByteInput = st.getBytes();
            FileOutputStream FileWrite = new FileOutputStream("orderdetails.csv", true);
            FileWrite.write(ByteInput);
            FoodPlan.clear();
            Readingfile("orderdetails.csv", 1);
            FileWrite.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void Add() {
        try {
            NewFile.delete();
            if (NewFile.createNewFile()) {
                System.out.println("Current bill status updated");
            }
            for (ArrayList<String> arrayList : FoodPlan) {
                String listString = String.join(",", arrayList);
                listString += "\n";
                byte[] ByteInput = listString.getBytes();
                FileOutputStream FileWrite = new FileOutputStream("orderdetails.csv",true);
                FileWrite.write(ByteInput);
                FileWrite.close();
            }
        } catch (Exception e) {
            System.out.println("");
        }
    }

    void statusofthebill() {
        System.out.print("Enter order Id : ");
        int n = sc1.nextInt();
        for (int j = 0; j < FoodPlan.size(); j++) {
            ArrayList<String> dump = FoodPlan.get(j);
            if (n == Integer.parseInt(dump.get(0))) {
                System.out.println(dump);
                System.out.println("press 'y' to place Order");
                char c = sc1.next().charAt(0);
                if (c == 'y')
                    FoodPlan.get(j).set(4, "Approved");
                else
                    FoodPlan.get(j).set(4, "Cancelled");
            }
        }
        sc1.nextLine();
        Add();
    }

    void totalbillcollectedonDay() {
        System.out.print("Enter the date in Format day-month-year :");
        String date = sc1.nextLine();
        double Col = 0.0;
        for (ArrayList<String> dump : FoodPlan) {
            if ((dump.get(1)).equals(date)) {
                Col += (Double.parseDouble(dump.get(2)));
            }
        }
        System.out.println(date + " Total Collection on the day is  Rs." + Col);
    }


    FoodClass() {
        Readingfile("menulist.csv", 0);
        Readingfile("orderdetails.csv", 1);
    }
}

// main class
class RestaurantApplication1 {
    static Scanner sc = new Scanner(System.in);
    static String menuItems[] = { "Placing an Order", "Updating Bill Status", "Collection of the day" };
    static FoodClass orderA = new FoodClass();

    static void Menulist() {
        System.out.println();
        System.out.println("Welcome to 'WestIndies' , Rajahmundry");
        System.out.println("------------------------------------");
        for (int i = 0; i < 3; i++)
            System.out.println((i + 1) + "-" + menuItems[i]);
    }

    static void Menu() {
        System.out.println();
        System.out.print("Enter your Choice(Enter number in terms of 1 ----> 001): ");
        int n = sc.nextInt();
        if (n == 1)
            orderA.newOrder();
        else if (n == 2)
            orderA.statusofthebill();
        else if (n == 3) {
            orderA.totalbillcollectedonDay();
        } else
            System.out.println("Selected food item is not in list");
    }

    public static void main(String[] args) {
        while (true) {
            Menulist();
            Menu();
            System.out.println("Press 'y' to return 'Main_Menu'");
            char ch = sc.next().charAt(0);
            if (ch != 'y')
                break;
        }
    }
}