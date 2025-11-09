package Service;

import DAO.LibraryDAO;
import Model.Book;
import Model.Member;
import java.sql.SQLException;
import java.util.List;

public class LibraryService {
    private final LibraryDAO dao = new LibraryDAO();

    public void addBook(Book b) {
        try {
            dao.addBook(b);
            System.out.println("Book Added.");
        } catch (SQLException e) {
            System.err.println("Error Adding book: " + e.getMessage());
        }
    }

    public void searchByTitle(String q) {
        try {
            List<Book> list = dao.searchBooksByTitle(q);
            if (list.isEmpty()) System.out.println("No books found..");
           /* else for(Book b : list) {
                System.out.println(b);
            }*/
            else list.forEach(System.out::println);

        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
    }

    public int registerMember(Member m){
        try { return dao.addMember(m);}
        catch (SQLException e){ System.err.println("Member add error: "+e.getMessage());
        return -1;}
    }

    public void borrowBook(int bookId,int memberId){
        try{
            boolean ok = dao.borrowBook(bookId,memberId);
            System.out.println(ok?"Borrowed successfully." : "Borrow failed (not available or error).");
        } catch (SQLException e) { System.err.println("Borrow error: "+e.getMessage());}
    }
    public void returnBook(int borrowId) {
        try {
            boolean ok = dao.returnBook(borrowId);
            System.out.println(ok ? "Returned successfully." : "Return failed invalid id or already returned).");
        } catch (SQLException e) { System.err.println("Return error: " +
                e.getMessage()); }
    }
}