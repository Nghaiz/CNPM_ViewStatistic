package dao;

import model.CostumeStatistic;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CostumeStatisticDAO extends DAO {
    public CostumeStatisticDAO() {
        super();
    }

    public List<CostumeStatistic> getCostumeStatistic(java.util.Date startDate, java.util.Date endDate) {
        List<CostumeStatistic> result = new ArrayList<>();
        String sql = """
                SELECT c.id, c.name, c.type, c.category,
                    SUM(charged.quantity) AS totalRentalTimes,
                    SUM(charged.rentalDays * charged.rentalPrice * charged.quantity) AS totalRevenue
                FROM tblCostume c
                INNER JOIN (
                    SELECT rc.costumeId,
                        ret.returnedQuantity AS quantity,
                        rc.rentalPrice,
                        GREATEST(DATEDIFF(
                            LEAST(DATE(rb.returnedAt), DATE(?)),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays
                    FROM tblRentedCostume rc
                    INNER JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    INNER JOIN tblReturnBill rb ON rb.id = ret.returnBillId
                    WHERE DATE(rc.rentedAt) <= DATE(?)
                        AND DATE(rb.returnedAt) >= DATE(?)

                    UNION ALL

                    SELECT rc.costumeId,
                        rc.rentalQuantity - IFNULL(SUM(ret.returnedQuantity), 0) AS quantity,
                        rc.rentalPrice,
                        GREATEST(DATEDIFF(
                            DATE(?),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays
                    FROM tblRentedCostume rc
                    LEFT JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    WHERE DATE(rc.rentedAt) <= DATE(?)
                    GROUP BY rc.id, rc.costumeId, rc.rentalQuantity, rc.rentalPrice, rc.rentedAt
                    HAVING quantity > 0
                ) charged ON charged.costumeId = c.id
                WHERE charged.rentalDays > 0
                    AND charged.quantity > 0
                GROUP BY c.id, c.name, c.type, c.category
                ORDER BY totalRentalTimes DESC, totalRevenue DESC, c.name ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            java.sql.Date startSqlDate = new java.sql.Date(startDate.getTime());
            java.sql.Date endSqlDate = new java.sql.Date(endDate.getTime());

            ps.setDate(1, endSqlDate);
            ps.setDate(2, startSqlDate);
            ps.setDate(3, endSqlDate);
            ps.setDate(4, startSqlDate);
            ps.setDate(5, endSqlDate);
            ps.setDate(6, startSqlDate);
            ps.setDate(7, endSqlDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CostumeStatistic cs = new CostumeStatistic();
                cs.setId(rs.getInt("id"));
                cs.setName(rs.getString("name"));
                cs.setType(rs.getString("type"));
                cs.setCategory(rs.getString("category"));
                cs.setTotalRentalTimes(rs.getInt("totalRentalTimes"));
                cs.setTotalRevenue(rs.getFloat("totalRevenue"));
                result.add(cs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
