package dao;

import model.Costume;
import model.RentalBill;
import model.RentedCostume;
import model.ReturnedCostume;

import java.sql.Date;
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
            if (rentalBill == null) {
                result.add(new ArrayList<>());
            } else {
                result.add(getReturnedCostumeByRentalBill(rentalBill.getId(), startDate, endDate));
            }
        }
        return result;
    }

    private List<ReturnedCostume> getReturnedCostumeByRentalBill(
            int rentalBillId,
            java.util.Date startDate,
            java.util.Date endDate
    ) {
        List<ReturnedCostume> result = new ArrayList<>();
        String sql = """
                SELECT charged.*,
                    charged.rentalDays * charged.rentalPrice * charged.returnedQuantity AS amount
                FROM (
                    SELECT ret.id AS returnedCostumeId,
                        ret.returnedQuantity,
                        rtb.returnedAt,
                        rc.id AS rentedCostumeId,
                        rc.rentalPrice,
                        rc.rentalQuantity,
                        rc.rentedAt,
                        rc.dateToReturn,
                        c.id AS costumeId,
                        c.name AS costumeName,
                        c.type AS costumeType,
                        c.category AS costumeCategory,
                        c.description AS costumeDescription,
                        c.price AS costumePrice,
                        GREATEST(DATEDIFF(
                            LEAST(DATE(rtb.returnedAt), DATE(?)),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays,
                        0 AS sortOrder
                    FROM tblRentedCostume rc
                    INNER JOIN tblCostume c ON c.id = rc.costumeId
                    INNER JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    INNER JOIN tblReturnBill rtb ON rtb.id = ret.returnBillId
                    WHERE rc.rentalBillId = ?
                        AND DATE(rc.rentedAt) <= DATE(?)
                        AND DATE(rtb.returnedAt) >= DATE(?)

                    UNION ALL

                    SELECT 0 AS returnedCostumeId,
                        rc.rentalQuantity - IFNULL(SUM(ret.returnedQuantity), 0) AS returnedQuantity,
                        NULL AS returnedAt,
                        rc.id AS rentedCostumeId,
                        rc.rentalPrice,
                        rc.rentalQuantity,
                        rc.rentedAt,
                        rc.dateToReturn,
                        c.id AS costumeId,
                        c.name AS costumeName,
                        c.type AS costumeType,
                        c.category AS costumeCategory,
                        c.description AS costumeDescription,
                        c.price AS costumePrice,
                        GREATEST(DATEDIFF(
                            DATE(?),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays,
                        1 AS sortOrder
                    FROM tblRentedCostume rc
                    INNER JOIN tblCostume c ON c.id = rc.costumeId
                    LEFT JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    WHERE rc.rentalBillId = ?
                        AND DATE(rc.rentedAt) <= DATE(?)
                    GROUP BY rc.id, rc.rentalPrice, rc.rentalQuantity, rc.rentedAt, rc.dateToReturn,
                        c.id, c.name, c.type, c.category, c.description, c.price
                    HAVING returnedQuantity > 0
                ) charged
                WHERE charged.rentalDays > 0
                    AND charged.returnedQuantity > 0
                ORDER BY charged.rentedCostumeId ASC, charged.sortOrder ASC, charged.returnedAt ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            Date startSqlDate = new Date(startDate.getTime());
            Date endSqlDate = new Date(endDate.getTime());

            ps.setDate(1, endSqlDate);
            ps.setDate(2, startSqlDate);
            ps.setInt(3, rentalBillId);
            ps.setDate(4, endSqlDate);
            ps.setDate(5, startSqlDate);
            ps.setDate(6, endSqlDate);
            ps.setDate(7, startSqlDate);
            ps.setInt(8, rentalBillId);
            ps.setDate(9, endSqlDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapReturnedCostume(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private ReturnedCostume mapReturnedCostume(ResultSet rs) throws java.sql.SQLException {
        Costume costume = new Costume();
        costume.setId(rs.getInt("costumeId"));
        costume.setName(rs.getString("costumeName"));
        costume.setType(rs.getString("costumeType"));
        costume.setCategory(rs.getString("costumeCategory"));
        costume.setDescription(rs.getString("costumeDescription"));
        costume.setPrice(rs.getFloat("costumePrice"));

        RentedCostume rentedCostume = new RentedCostume();
        rentedCostume.setId(rs.getInt("rentedCostumeId"));
        rentedCostume.setRentalPrice(rs.getFloat("rentalPrice"));
        rentedCostume.setRentalQuantity(rs.getInt("rentalQuantity"));
        rentedCostume.setRentedAt(toLocalDateTime(rs.getTimestamp("rentedAt")));
        rentedCostume.setDateToReturn(toLocalDateTime(rs.getTimestamp("dateToReturn")));
        rentedCostume.setCostume(costume);

        ReturnedCostume returnedCostume = new ReturnedCostume();
        returnedCostume.setId(rs.getInt("returnedCostumeId"));
        returnedCostume.setReturnedQuantity(rs.getInt("returnedQuantity"));
        returnedCostume.setReturnedAt(toLocalDateTime(rs.getTimestamp("returnedAt")));
        returnedCostume.setRentedCostume(rentedCostume);
        returnedCostume.setTotalAmount(rs.getFloat("amount"));
        return returnedCostume;
    }

    private java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
