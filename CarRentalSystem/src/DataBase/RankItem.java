package DataBase;

public class RankItem {
    private String itemName;
    private int num;

    public RankItem(String itemName, int num) {
        this.itemName = itemName;
        this.num = num;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
