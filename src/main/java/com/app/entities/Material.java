package main.java.com.app.entities;

public class Material extends Component {

    @Override
    public double calculateCost() {
        return (getUnitCost() * getQuantity() * getQualityCoefficient()) + getTransportCost();
    }
    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"name\": \"%s\", \"unitCost\": %.2f, \"quantity\": %.2f, \"componentType\": \"%s\", \"vatRate\": %.2f, \"transportCost\": %.2f, \"qualityCoefficient\": %.2f, \"project\": %s}",
                id,
                name != null ? name : "",
                unitCost,
                quantity,
                componentType != null ? componentType : "",
                vatRate,
                transportCost,
                qualityCoefficient,
                getProject() != null ? getProject() : null

        );
    }

}
