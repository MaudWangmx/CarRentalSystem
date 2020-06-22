package DataBase.TableView;

import javafx.beans.property.*;

public class CarTable {
    private final StringProperty licensePlate;
    private final StringProperty brand;
    private final StringProperty condition;
    private final StringProperty rent;
    private final StringProperty pledge;
    private final StringProperty status;

    public CarTable(String licensePlate, String brand, String condition, int rent, int pledge, boolean status) {
        this.licensePlate = new SimpleStringProperty(licensePlate);
        this.brand = new SimpleStringProperty(brand);
        switch (Integer.valueOf(condition)){
            case 5:
                this.condition = new SimpleStringProperty("Perfect");
                break;
            case 4:
                this.condition = new SimpleStringProperty("Good");
                break;
            case 3:
                this.condition = new SimpleStringProperty("Slightly Damaged");
                break;
            case 2:
                this.condition = new SimpleStringProperty("Bad");
                break;
            case 1:
            default:
                this.condition = new SimpleStringProperty("Unusable");
                break;
        }
        this.rent = new SimpleStringProperty(Integer.toString(rent, 10));
        this.pledge = new SimpleStringProperty(Integer.toString(pledge, 10));
        this.status = new SimpleStringProperty(status ? "Leasable" : "Leased");
    }


    public String getLicensePlate() {
        return licensePlate.get();
    }

    public StringProperty licensePlateProperty() {
        return licensePlate;
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public String getCondition() {
        return condition.get();
    }

    public StringProperty conditionProperty() {
        return condition;
    }

    public String getRent() {
        return rent.get();
    }

    public StringProperty rentProperty() {
        return rent;
    }

    public String getPledge() {
        return pledge.get();
    }

    public StringProperty pledgeProperty() {
        return pledge;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    @Override
    public String toString() {
        return "CarTable{" +
                "licensePlate=" + licensePlate +
                ", brand=" + brand +
                ", condition=" + condition +
                ", rent=" + rent +
                ", pledge=" + pledge +
                ", status=" + status +
                '}';
    }
}
