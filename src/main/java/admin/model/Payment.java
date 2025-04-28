package admin.model;

import java.time.LocalDateTime;
import java.util.List;

public class Payment {
    private String customerName;
    private String customerNote;
    private String instaAccount;
    private String phoneNumber;
    private double totalAmount;
    private LocalDateTime dateTime;
    private List<BookOrder> books;

    public Payment(String customerName, String customerNote, String instaAccount,
                   String phoneNumber, List<BookOrder> books, double totalAmount) {
        this.customerName = customerName;
        this.customerNote = customerNote;
        this.instaAccount = instaAccount;
        this.phoneNumber = phoneNumber;
        this.books = books;
        this.totalAmount = totalAmount;
        this.dateTime = LocalDateTime.now(); // Set current date-time
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNote() {
        return customerNote;
    }

    public void setCustomerNote(String customerNote) {
        this.customerNote = customerNote;
    }

    public String getInstaAccount() {
        return instaAccount;
    }

    public void setInstaAccount(String instaAccount) {
        this.instaAccount = instaAccount;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public List<BookOrder> getBooks() {
        return books;
    }

    public void setBooks(List<BookOrder> books) {
        this.books = books;
    }
}
