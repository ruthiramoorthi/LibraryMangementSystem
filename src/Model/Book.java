package Model;

public class Book {
    private int id;
    private String title;
    private String author;
    private String isbn;
    private int totalQty;
    private int availQty;

    public Book(){}
    public Book(int id,String title,String author,String isbn,int totalQty,int availQty){
        this.id=id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalQty = totalQty;
        this.availQty = availQty;
    }
    public Book(String title,String author,String isbn,int totalQty){
        this.id=id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.totalQty = totalQty;
        this.availQty = availQty;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getAuthor() {return author;}
    public void setAuthor(String author) {this.author = author;}
    public String getIsbn() {return isbn;}
    public void setIsbn(String isbn) {this.isbn = isbn;}
    public int getTotalQty() {return totalQty;}
    public void setTotalQty(int totalQty) {this.totalQty = totalQty;}
    public int getAvailQty() {return availQty;}
    public void setAvailQty(int availQty) {this.availQty = availQty;}

    @Override
    public String toString(){
        return String.format("[%d] %s by %s (ISBN: %s) - Available: %d/%d",id,title,author,isbn,availQty,totalQty);
    }
}
