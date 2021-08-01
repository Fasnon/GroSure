package Model;

public class DiscountPriceEffects extends PriceEffects {

    public DiscountPriceEffects( double effectMagnitude) {
        this.effectMagnitude = effectMagnitude;
    }

    public String priceEffectsStr(){
        return "UP: $" + String.format("%.2f", effectMagnitude);
    }
}


