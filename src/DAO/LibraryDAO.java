package DAO;
import db.DBConnection;
import Model.Member;
import Model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryDAO {
    public void addBook(Book book) throws SQLException{
        String sql = "insert into books(title,author,isbn,total_qul,avail_qul)values(?,?,?,?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,book.getTitle());
            ps.setString(2,book.getAuthor());
            ps.setString(3,book.getIsbn());
            ps.setInt(4,book.getTotalQty());
            ps.setInt(5,book.getAvailQty());
            ps.executeUpdate();
        }
    }
    public List<Book> searchBooksByTitle(String q) throws SQLException{
        String sql = "select * from books where title like ?";
        List<Book> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
             ps.setString(1,"%"+q+"%");
             try(ResultSet rs = ps.executeQuery()){
                 while(rs.next()) list.add(mapBook(rs));
        }
    }
    return list;
    }
    private Book mapBook(ResultSet rs) throws SQLException{
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("isbn"),
                rs.getInt("total_qul"),
                rs.getInt("avail_qul")
        );
    }
    private Book getBookByID(int id) throws SQLException{
        String sql = "select * from books where id = ? ";
        try(Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) return mapBook(rs);
            }
        }
        return null;
    }

    public void updateBookAvail(int bookId,int newAvail) throws SQLException{
        String sql = "update books set avail_qul = ? where id = ? ";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,newAvail);
            ps.setInt(2,bookId);
            ps.executeUpdate();
        }
    }

    //-----Members----
    public int addMember(Member m) throws SQLException{
        String sql = "insert into members(name,email) values(?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,m.getName());
            ps.setString(2,m.getEmail());
            ps.executeUpdate();
            try(ResultSet rs =ps.getGeneratedKeys()){
                if(rs.next()) return rs.getInt(1);
            }
            /* ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                return generatedId;
                }*/
        }
        return -1;
    }

    //--Borrow / Return---
    public boolean borrowBook(int bookId,int memberId) throws SQLException{
        Connection c = DBConnection.getConnection();
        try {
            c.setAutoCommit(false);

            Book b = getBookByID(bookId);
            if (b == null || b.getAvailQty() <= 0) {
                c.rollback();
                return false;
            }
            String borrowSql = "insert into borrows (book_id,member_id,borrow_data,returned) values (?,?,?,?)";
            try (PreparedStatement ps = c.prepareStatement(borrowSql)) {
                ps.setInt(1, bookId);
                ps.setInt(2, memberId);
                ps.setDate(3, new Date(System.currentTimeMillis()));
                ps.setBoolean(4,false);
                ps.executeUpdate();
            }

            updateBookAvail(bookId, b.getAvailQty() - 1);

            c.commit();
            return true;
        }catch (SQLException ex){
            c.rollback();
            throw ex;
        } finally {
            c.setAutoCommit(true);
            c.close();
        }
    }
    public boolean returnBook(int borrowId) throws SQLException {
        String findSql = "SELECT book_id, returned FROM borrows WHERE id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(findSql)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return false;
                boolean returned = rs.getBoolean("returned");
                int bookId = rs.getInt("book_id");
                if (returned) return false; // already returned
                String updBorrow =
                        "UPDATE borrows SET returned = ?, return_date = ? WHERE id = ?";
                try (PreparedStatement ps2 = c.prepareStatement(updBorrow)) {
                    ps2.setBoolean(1, true);
                    ps2.setDate(2, new Date(System.currentTimeMillis()));
                    ps2.setInt(3, borrowId);
                    ps2.executeUpdate();
                }
                Book b = getBookByID(bookId);
                updateBookAvail(bookId, b.getAvailQty() + 1);
                return true;
            }
        }
    }
}
