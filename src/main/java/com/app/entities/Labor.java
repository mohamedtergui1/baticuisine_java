package main.java.com.app.entities;

public class Labor extends Component {
    private double hourlyRate;
    private double hoursWorked;
    private double workerProductivity;



    
    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public double getWorkerProductivity() { return workerProductivity; }
    public void setWorkerProductivity(double workerProductivity) { this.workerProductivity = workerProductivity; }

    @Override
    public double calculateCost() {
        return getHourlyRate() * getHoursWorked() * getWorkerProductivity();
    }
    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"name\": \"%s\", \"unitCost\": %.2f, \"quantity\": %.2f, \"componentType\": \"%s\", \"vatRate\": %.2f, \"transportCost\": %.2f, \"qualityCoefficient\": %.2f, \"hourlyRate\": %.2f, \"hoursWorked\": %.2f, \"workerProductivity\": %.2f, \"project\": %s}",
                getId(),
                getName(),
                getUnitCost(),
                getQuantity(),
                getComponentType(),
                getVatRate(),
                getTransportCost(),
                getQualityCoefficient(),
                hourlyRate,
                hoursWorked,
                workerProductivity,
                getProject() != null ? getProject() : null
        );
    }

}
