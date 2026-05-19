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
        String sql = "SELECT c.id, c.name, c.type, c.category, c.description, c.price, "
                + "IFNULL(cs.totalRentalTimes, 0) AS totalRentalTimes, "
                + "IFNULL(cs.totalRevenue, 0) AS totalRevenue "
                + "FROM tblCostume c "
                + "LEFT JOIN ( "
                + "    SELECT rented.costumeId, "
                + "        SUM(CASE WHEN rented.rentalDays > 0 THEN rented.rentalQuantity ELSE 0 END) AS totalRentalTimes, "
                + "        SUM(rented.rentalDays * rented.rentalPrice * rented.rentalQuantity) AS totalRevenue "
                + "    FROM ( "
                + "        SELECT rc.id, rc.costumeId, rc.rentalQuantity, rc.rentalPrice, "
                + "            GREATEST(DATEDIFF(LEAST(COALESCE(MAX(rb.returnedAt), ?), ?), "
                + "                GREATEST(rc.rentedAt, ?)), 0) AS rentalDays "
                + "        FROM tblRentedCostume rc "
                + "        LEFT JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id "
                + "        LEFT JOIN tblReturnBill rb ON rb.id = ret.returnBillId "
                + "        WHERE rc.rentedAt < ? "
                + "        GROUP BY rc.id, rc.costumeId, rc.rentalQuantity, rc.rentalPrice, rc.rentedAt "
                + "        HAVING COALESCE(MAX(rb.returnedAt), ?) > ? "
                + "    ) rented "
                + "    GROUP BY rented.costumeId "
                + ") cs ON cs.costumeId = c.id "
                + "ORDER BY totalRevenue DESC, totalRentalTimes DESC, c.name ASC";

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            Timestamp startTimestamp = new Timestamp(startDate.getTime());
            Timestamp endTimestamp = new Timestamp(endDate.getTime());

            ps.setTimestamp(1, endTimestamp);
            ps.setTimestamp(2, endTimestamp);
            ps.setTimestamp(3, startTimestamp);
            ps.setTimestamp(4, endTimestamp);
            ps.setTimestamp(5, endTimestamp);
            ps.setTimestamp(6, startTimestamp);

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
