package main.java.com.baticuisine;


import java.util.*;

public class Main {
    public static void main(String[] args)  {

        System.out.println("HOTELS Reservation Management");
        Scanner scanner = new Scanner(System.in);
        int i;
           System.out.println("menu");

           do {
               System.out.println("1. mange Reservations");
               System.out.println("2. mange Rooms");
               System.out.println("3. mange Hotels");
               System.out.println("4. mange users");
               System.out.println("5. exit");
               System.out.print("Enter your choice: ");
               i=scanner.nextInt();
               scanner.nextLine();
               switch (i)
               {
                   case 1:

                       break;
                   case  2:
                       break;
                   case  3:
                       break;
                   case  4:

                       break;
               }
           } while (5 != i);
    }
}