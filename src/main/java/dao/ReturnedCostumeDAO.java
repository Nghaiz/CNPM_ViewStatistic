package dao;

import model.RentalBill;
import model.RentedCostume;
import model.ReturnedCostume;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ReturnedCostumeDAO extends DAO {

    public ReturnedCostumeDAO() {
        super();
    }

    public List<List<ReturnedCostume>> getReturnedCostume(
            List<RentalBill> listRentalBill,
            java.util.Date startDate,
            java.util.Date endDate
    ) {
        List<List<ReturnedCostume>> result = new ArrayList<>();
        if (listRentalBill == null) {
            return result;
        }

        for (RentalBill rentalBill : listRentalBill) {
            result.add(rentalBill == null
                    ? new ArrayList<>()
                    : getReturnedCostumeByRentalBill(rentalBill, startDate, endDate));
        }
        return result;
    }

    private List<ReturnedCostume> getReturnedCostumeByRentalBill(
            RentalBill rentalBill,
            java.util.Date startDate,
            java.util.Date endDate
    ) {
        List<ReturnedCostume> result = new ArrayList<>();
        for (RentedCostume rentedCostume : rentalBill.getListRentedCostume()) {
            result.addAll(getReturnedCostumesByRentedCostume(rentedCostume, startDate, endDate));
        }
        return result;
    }

    private List<ReturnedCostume> getReturnedCostumesByRentedCostume(
            RentedCostume rentedCostume,
            java.util.Date startDate,
            java.util.Date endDate
    ) {
        List<ReturnedCostume> result = new ArrayList<>();
        int totalReturnedQuantity = 0;
        String sql = """
                SELECT ret.id, ret.returnedQuantity, rtb.returnedAt,
                    CASE WHEN DATE(?) <= DATE(?) AND DATE(rtb.returnedAt) >= DATE(?) THEN 1 ELSE 0 END AS isCharged
                FROM tblReturnedCostume ret
                INNER JOIN tblReturnBill rtb ON rtb.id = ret.returnBillId
                WHERE ret.rentedCostumeId = ?
                ORDER BY rtb.returnedAt ASC, ret.id ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, toSqlDate(rentedCostume.getRentedAt()));
            ps.setDate(2, toSqlDate(endDate));
            ps.setDate(3, toSqlDate(startDate));
            ps.setInt(4, rentedCostume.getId());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int returnedQuantity = rs.getInt("returnedQuantity");
                totalReturnedQuantity += returnedQuantity;
                if (rs.getBoolean("isCharged")) {
                    result.add(mapReturnedCostume(rs, rentedCostume, returnedQuantity));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        int remainingQuantity = rentedCostume.getRentalQuantity() - totalReturnedQuantity;
        if (remainingQuantity > 0 && !toSqlDate(rentedCostume.getRentedAt()).after(toSqlDate(endDate))) {
            result.add(createUnreturnedCostume(rentedCostume, remainingQuantity));
        }
        return result;
    }

    private ReturnedCostume mapReturnedCostume(
            ResultSet rs,
            RentedCostume rentedCostume,
            int returnedQuantity
    ) throws java.sql.SQLException {
        ReturnedCostume returnedCostume = createReturnedCostume(rentedCostume, returnedQuantity);
        returnedCostume.setId(rs.getInt("id"));
        returnedCostume.setReturnedAt(toLocalDateTime(rs.getTimestamp("returnedAt")));
        return returnedCostume;
    }

    private ReturnedCostume createUnreturnedCostume(RentedCostume rentedCostume, int quantity) {
        ReturnedCostume returnedCostume = createReturnedCostume(rentedCostume, quantity);
        returnedCostume.setId(0);
        returnedCostume.setReturnedAt(null);
        return returnedCostume;
    }

    private ReturnedCostume createReturnedCostume(RentedCostume rentedCostume, int quantity) {
        ReturnedCostume returnedCostume = new ReturnedCostume();
        returnedCostume.setReturnedQuantity(quantity);
        returnedCostume.setRentalPrice(rentedCostume.getRentalPrice());
        returnedCostume.setRentalQuantity(rentedCostume.getRentalQuantity());
        returnedCostume.setRentedAt(rentedCostume.getRentedAt());
        returnedCostume.setDateToReturn(rentedCostume.getDateToReturn());
        returnedCostume.setCostume(rentedCostume.getCostume());
        returnedCostume.setListRentedDamage(rentedCostume.getListRentedDamage());
        return returnedCostume;
    }

    private java.sql.Date toSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    private java.sql.Date toSqlDate(java.time.LocalDateTime dateTime) {
        return java.sql.Date.valueOf(dateTime.toLocalDate());
    }

    private java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
