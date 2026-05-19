package model;

public class CostumeStatistic extends Costume {
    private int totalRentalTimes;
    private float totalRevenue;

    public CostumeStatistic() {
        super();
    }

    public CostumeStatistic(int id, String name, String type, String category, String description, float price,
                            int totalRentalTimes, float totalRevenue) {
        super(id, name, type, category, description, price);
        this.totalRentalTimes = totalRentalTimes;
        this.totalRevenue = totalRevenue;
    }

    public int getTotalRentalTimes() {
        return totalRentalTimes;
    }

    public void setTotalRentalTimes(int totalRentalTimes) {
        this.totalRentalTimes = totalRentalTimes;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
