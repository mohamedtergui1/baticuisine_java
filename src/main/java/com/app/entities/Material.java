package main.java.com.app.entities;

public class Material extends Component {
    private double transportCost;
    private double qualityCoefficient;
    private double quantity;
    private double unitCost;

    public double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(double unitCost) {
        this.unitCost = unitCost;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setTransportCost(double transportCost) {
        this.transportCost = transportCost;
    }


    public void setQualityCoefficient(double qualityCoefficient) {
        this.qualityCoefficient = qualityCoefficient;
    }


    public double getQualityCoefficient() {
        return qualityCoefficient;
    }

    public double getTransportCost() {
        return transportCost;
    }

    @Override
    public double calculateCost() {
        return (getUnitCost() * getQuantity() * getQualityCoefficient()) + getTransportCost();
    }
    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"name\": \"%s\", \"quantity\": %.2f, \"componentType\": \"%s\", \"vatRate\": %.2f, \"transportCost\": %.2f, \"qualityCoefficient\": %.2f, \"project\": %s}",
                id,
                name != null ? name : "",
                quantity,
                vatRate,
                transportCost,
                qualityCoefficient,
                getProject() != null ? getProject() : null
        );
    }

}
