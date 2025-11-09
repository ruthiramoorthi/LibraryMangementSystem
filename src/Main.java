import Model.Book;
import Model.Member;
import Service.LibraryService;
import java.util.Scanner;

public class Main {
    private final static LibraryService svc = new LibraryService();
    private final static Scanner sc = new Scanner(System.in);

    public static void main(String[]args){
        System.out.println("===Library Management System===");
        boolean running = true;
        while(running){
            showMenu();
            String choice = sc.nextLine().trim();
            switch (choice){
                case "1" : addBook(); break;
                case "2" : searchBook(); break;
                case "3" : registerMember(); break;
                case "4" : borrowBook(); break;
                case "5" : returnBook(); break;
                case "0" : running = false; break;
                default:System.out.print("Invalid option");
            }
        }
        System.out.print("Bye..");
        sc.close();
    }
    private static void showMenu(){
        System.out.println("\n 1) Add Book\n 2) Search Book by Title\n 3) Register Member\n 4) Borrow Book\n 5)Return Book\n 0) Exit\nChoose:");
    }
    private static void addBook(){
        System.out.println("Title: ");String title = sc.nextLine();
        System.out.println("author: ");String author = sc.nextLine();
        System.out.println("ISBN: ");String isbn = sc.nextLine();
        System.out.println("Quantity: ");int qty = Integer.parseInt(sc.nextLine());
        Book b = new Book(title, author, isbn, qty);
        svc.addBook(b);
    }
    private static void searchBook(){
        System.out.println("Enter title query:"); String q = sc.nextLine();
        svc.searchByTitle(q);
    }
    private static  void registerMember(){
        System.out.println("Name: "); String name = sc.nextLine();
        System.out.println("email: "); String email = sc.nextLine();
        Member m = new Member(name,email);
        int id = svc.registerMember(m);
        if(id>0) System.out.println("Memer registered with ID:"+id);
    }
    private static void borrowBook(){
        System.out.println("Book ID to borrow: "); int bid = Integer.parseInt(sc.nextLine());
        System.out.println("Member ID: "); int mid = Integer.parseInt(sc.nextLine());
        svc.borrowBook(bid,mid);
    }
    private static void returnBook() {
        System.out.println("Borrow ID to return (from borrows table):"); int
                rid = Integer.parseInt(sc.nextLine());
                svc.returnBook(rid);
    }
}
}
