package Model;


import java.util.ArrayList;
import java.util.Date;

public class ShoppingList {
    private ArrayList<ItemWithinList> itemList;
    private String name;
    private int ID;


    public ShoppingList(String name, ArrayList<ItemWithinList> itemList, int ID) {
        this.name = name;
        this.itemList = itemList;
        this.ID = ID;
    }
    public ArrayList<ItemWithinList> getItemList() {
        return itemList;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }
    public double getNetPrice(){
        double d = 0.0;
        for (ItemWithinList i: itemList){
            d += i.getTotalPrice();
        }
        return d;
    }

    public String getStringNetPrice(){
        return "$" + String.format("%.2f", getNetPrice());
    }
    public String toString(){
        return ID + " " + name;
    }

}
