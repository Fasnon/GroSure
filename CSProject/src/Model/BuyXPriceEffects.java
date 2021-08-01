package Model;

public class BuyXPriceEffects  extends PriceEffects{
    private double effectRequirement;


    public BuyXPriceEffects( double effectRequirement ,double effectMagnitude){
    this.effectMagnitude = effectMagnitude;
    this.effectRequirement = effectRequirement;
    }
    public String priceEffectsStr() {
        return "Buy " + (int) effectRequirement + " for $" + String.format("%.2f", effectMagnitude);
    }

    public double getEffectRequirement() {
        return effectRequirement;
    }

    public double getEffectMagnitude(){
        return effectMagnitude;
    }
}