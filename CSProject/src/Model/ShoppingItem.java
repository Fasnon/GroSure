package Model;


public class ShoppingItem {
    private double price;
    private String name;
    private ItemCategory category;
    private String quantityName;
    private PriceEffects priceEffects;

    public ShoppingItem (String name, double price, String quantityName){
        this.name = name;
        this.price = price;
        this.quantityName = quantityName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemCategory getCategory() {
        return category;
    }
    public String getPriceEffectsString(){
        if (priceEffects == null){
            return "-";
        }
        else{
            return priceEffects.priceEffectsStr();
        }
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public String getQuantityName() {
        return quantityName;
    }

    public String getPriceString(){
        return "$"+String.format("%.2f",price);
    }

    public void setQuantityName(String quantityName) {
        this.quantityName = quantityName;
    }

    public PriceEffects getPriceEffects() {
        return priceEffects;
    }

    public void setPriceEffects(PriceEffects priceEffects) {
        this.priceEffects = priceEffects;
    }

    public String toString(){
        return name + " " + quantityName;
    }
}
