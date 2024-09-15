package main.java.com.baticuisine.entities;

public class Material extends Component {

    @Override
    public double calculateCost() {
        return (getUnitCost() * getQuantity() * getQualityCoefficient()) + getTransportCost();
    }
}
