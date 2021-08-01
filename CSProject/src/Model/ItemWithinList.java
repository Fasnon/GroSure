package Model;

import java.util.ArrayList;

public class ItemWithinList{
    private ShoppingItem item;
    private int quantity;
    public ItemWithinList(ShoppingItem i, int quantity){
        item = i;
        this.quantity = quantity;
    }

    public ShoppingItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice(){
        return item.getPrice();
    }

    public String getName(){
        return item.getName();
    }

    public String toString(){
        return item.toString();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        if (item.getPriceEffects() == null)
            return quantity * item.getPrice();

        if (item.getPriceEffects() instanceof BuyXPriceEffects) {
                BuyXPriceEffects bpe = (BuyXPriceEffects) item.getPriceEffects();
                int t = (int) bpe.getEffectRequirement();
                int discountsApplied = quantity / t;
                int remaining = quantity % t;
                return discountsApplied * bpe.getEffectMagnitude() + remaining * item.getPrice();

            }
            else {
                return quantity * item.getPrice();
            }
    }

    public String getStringTotalPrice(){
        return "$" + String.format("%.2f", getTotalPrice());
    }
}
