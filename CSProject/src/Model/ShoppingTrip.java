package Model;

import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShoppingTrip {
    private Date date;
    private ArrayList<ShoppingList> listsUsed = new ArrayList<ShoppingList>();
    private int ID;
    private String name;
    private User userBelongTo;

    public ShoppingTrip(String name, Date d, ArrayList<ShoppingList> listsUsed, int ID, User userBelongTo){
        date = d;
        this.name = name;
        this.listsUsed = listsUsed;
        this.ID = ID;
        this.userBelongTo = userBelongTo;
    }

    public Date getDate() {
        return date;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public ArrayList<ShoppingList> getListsUsed() {
        return listsUsed;
    }

    public User getUserBelongTo() {
        return userBelongTo;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getID() {
        return ID;
    }
    public String getStringDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(date);
    }
    public double getPrice(){
        double d = 0;
        for (ShoppingList l: listsUsed){
            d += l.getNetPrice();
        }
        System.out.println(name + " " + d);
       return d;
    }

    public String getStringPrice(){
        return "$" + String.format("%.2f", getPrice());
    }
}
