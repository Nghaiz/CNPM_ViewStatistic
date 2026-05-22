package dao;

import model.CostumeStatistic;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class CostumeStatisticDAO extends DAO {
    public CostumeStatisticDAO() {
        super();
    }

    public List<CostumeStatistic> getCostumeStatistic(Date startDate, Date endDate){
        List<CostumeStatistic> result = new ArrayList<>();
        String sql = """
                SELECT c.id, c.name, c.type, c.category,
                    IFNULL(cs.totalRentalTimes, 0) AS totalRentalTimes,
                    IFNULL(cs.totalRevenue, 0) AS totalRevenue
                FROM tblCostume c
                INNER JOIN (
                    SELECT charged.costumeId,
                        SUM(charged.quantity) AS totalRentalTimes,
                        SUM(charged.amount) AS totalRevenue
                    FROM (
                        SELECT rc.costumeId,
                            ret.returnedQuantity AS quantity,
                            GREATEST(DATEDIFF(
                                LEAST(DATE(rtb.returnedAt), DATE(?)),
                                GREATEST(DATE(rc.rentedAt), DATE(?))
                            ) + 1, 0) * rc.rentalPrice * ret.returnedQuantity AS amount
                        FROM tblRentedCostume rc
                        INNER JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                        INNER JOIN tblReturnBill rtb ON rtb.id = ret.returnBillId
                        WHERE DATE(rc.rentedAt) <= DATE(?)
                            AND DATE(rtb.returnedAt) >= DATE(?)

                        UNION ALL

                        SELECT rc.costumeId,
                            rc.rentalQuantity - IFNULL(returned.totalReturnedQuantity, 0) AS quantity,
                            GREATEST(DATEDIFF(
                                DATE(?),
                                GREATEST(DATE(rc.rentedAt), DATE(?))
                            ) + 1, 0) * rc.rentalPrice * (rc.rentalQuantity - IFNULL(returned.totalReturnedQuantity, 0)) AS amount
                        FROM tblRentedCostume rc
                        LEFT JOIN (
                            SELECT rentedCostumeId, SUM(returnedQuantity) AS totalReturnedQuantity
                            FROM tblReturnedCostume
                            GROUP BY rentedCostumeId
                        ) returned ON returned.rentedCostumeId = rc.id
                        WHERE rc.rentalQuantity - IFNULL(returned.totalReturnedQuantity, 0) > 0
                            AND DATE(rc.rentedAt) <= DATE(?)
                    ) charged
                    WHERE charged.amount > 0
                    GROUP BY charged.costumeId
                ) cs ON cs.costumeId = c.id
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
                CostumeStatistic costumeStatistic = new CostumeStatistic();
                costumeStatistic.setId(rs.getInt("id"));
                costumeStatistic.setName(rs.getString("name"));
                costumeStatistic.setType(rs.getString("type"));
                costumeStatistic.setCategory(rs.getString("category"));
                costumeStatistic.setTotalRentalTimes(rs.getInt("totalRentalTimes"));
                costumeStatistic.setTotalRevenue(rs.getFloat("totalRevenue"));
                result.add(costumeStatistic);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
