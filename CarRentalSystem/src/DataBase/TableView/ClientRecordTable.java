package DataBase.TableView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

public class ClientRecordTable {
    StringProperty dateTime;
    StringProperty licsPlate;
    StringProperty brand;
    StringProperty RecordType;
    StringProperty OperatorID;
    StringProperty Cost;
    StringProperty WithDraw;

    public ClientRecordTable(Timestamp dateTime, String licsPlate, String brand, String recordType, String operatorID, int cost, int withDraw) {
        this.dateTime = new SimpleStringProperty(dateTime.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        this.licsPlate = new SimpleStringProperty(licsPlate);
        this.brand = new SimpleStringProperty(brand);
        String recordTypeString;
        switch (Integer.valueOf(recordType)){
            case 0:
                recordTypeString = "Lease";
                break;
            case 1:
                recordTypeString = "Return";
                break;
            case 2:
                recordTypeString = "Damage";
                break;
            case 3:
                recordTypeString = "Traffic Fine";
                break;
            default:
                recordTypeString = "";
                break;
        }
        RecordType = new SimpleStringProperty(recordTypeString);
        OperatorID = new SimpleStringProperty(operatorID);
        Cost = new SimpleStringProperty(Integer.toString(cost));
        WithDraw = new SimpleStringProperty(Integer.toString(withDraw));
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public StringProperty dateTimeProperty() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime.set(dateTime);
    }

    public String getLicsPlate() {
        return licsPlate.get();
    }

    public StringProperty licsPlateProperty() {
        return licsPlate;
    }

    public void setLicsPlate(String licsPlate) {
        this.licsPlate.set(licsPlate);
    }

    public String getBrand() {
        return brand.get();
    }

    public StringProperty brandProperty() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand.set(brand);
    }

    public String getRecordType() {
        return RecordType.get();
    }

    public StringProperty recordTypeProperty() {
        return RecordType;
    }

    public void setRecordType(String recordType) {
        this.RecordType.set(recordType);
    }

    public String getOperatorID() {
        return OperatorID.get();
    }

    public StringProperty operatorIDProperty() {
        return OperatorID;
    }

    public void setOperatorID(String operatorID) {
        this.OperatorID.set(operatorID);
    }

    public String getCost() {
        return Cost.get();
    }

    public StringProperty costProperty() {
        return Cost;
    }

    public void setCost(String cost) {
        this.Cost.set(cost);
    }

    public String getWithDraw() {
        return WithDraw.get();
    }

    public StringProperty withDrawProperty() {
        return WithDraw;
    }

    public void setWithDraw(int withDraw) {
        this.WithDraw.set(Integer.toString(withDraw));
    }
}
