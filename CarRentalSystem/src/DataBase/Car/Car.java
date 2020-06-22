package DataBase.Car;

public class Car {
    private String carID;
    private String brand;
    private boolean status;
    private int rent;
    private int pledge;
    private String condition;

    public Car(String carID, String brand, boolean status, int rent, int pledge, String condition) {
        this.carID = carID;
        this.brand = brand;
        this.status = status;
        this.rent = rent;
        this.pledge = pledge;
        this.condition = condition;
    }

    public String getCarID() {
        return carID;
    }

    public void setCarID(String carID) {
        this.carID = carID;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getPledge() {
        return pledge;
    }

    public void setPledge(int pledge) {
        this.pledge = pledge;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }


    @Override
    public String toString() {
        return "Car{" +
                "carID='" + carID + '\'' +
                ", brand='" + brand + '\'' +
                ", status=" + status +
                ", rent=" + rent +
                ", pledge=" + pledge +
                ", condition='" + condition + '\'' +
                '}';
    }
}
