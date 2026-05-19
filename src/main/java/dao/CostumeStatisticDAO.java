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
                    SELECT rented.costumeId,
                        SUM(CASE WHEN rented.rentalDays > 0 THEN rented.rentalQuantity ELSE 0 END) AS totalRentalTimes,
                        SUM(rented.rentalDays * rented.rentalPrice * rented.rentalQuantity) AS totalRevenue
                    FROM (
                        SELECT rc.id, rc.costumeId, rc.rentalQuantity, rc.rentalPrice,
                            GREATEST(DATEDIFF(
                                LEAST(DATE(COALESCE(MAX(rb.returnedAt), ?)), DATE(?)),
                                GREATEST(DATE(rc.rentedAt), DATE(?))
                            ) + 1, 0) AS rentalDays
                        FROM tblRentedCostume rc
                        LEFT JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                        LEFT JOIN tblReturnBill rb ON rb.id = ret.returnBillId
                        WHERE DATE(rc.rentedAt) <= DATE(?)
                        GROUP BY rc.id, rc.costumeId, rc.rentalQuantity, rc.rentalPrice, rc.rentedAt
                        HAVING DATE(COALESCE(MAX(rb.returnedAt), ?)) >= DATE(?)
                    ) rented
                    GROUP BY rented.costumeId
                ) cs ON cs.costumeId = c.id
                ORDER BY totalRentalTimes DESC, totalRevenue DESC, c.name ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            java.sql.Date startSqlDate = new java.sql.Date(startDate.getTime());
            java.sql.Date endSqlDate = new java.sql.Date(endDate.getTime());

            ps.setDate(1, endSqlDate);
            ps.setDate(2, endSqlDate);
            ps.setDate(3, startSqlDate);
            ps.setDate(4, endSqlDate);
            ps.setDate(5, endSqlDate);
            ps.setDate(6, startSqlDate);

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
