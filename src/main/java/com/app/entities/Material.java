package main.java.com.app.entities;

public class Material extends Component {

    @Override
    public double calculateCost() {
        return (getUnitCost() * getQuantity() * getQualityCoefficient()) + getTransportCost();
    }
}
