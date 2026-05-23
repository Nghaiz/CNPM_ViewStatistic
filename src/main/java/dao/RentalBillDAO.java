package dao;

import model.Client;
import model.Collateral;
import model.RentalBill;
import model.RentalCollateral;
import model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RentalBillDAO extends DAO {

    public RentalBillDAO() {
        super();
    }

    public List<RentalBill> getBillDetailByCostume(int costumeId, java.util.Date startDate, java.util.Date endDate) {
        List<RentalBill> result = new ArrayList<>();
        String sql = """
                SELECT rb.id, rb.createdAt, rb.saleoff, rb.note,
                    c.id AS clientId, c.fullname AS clientName,
                    u.id AS sellerId, u.fullname AS sellerName,
                    SUM(CASE WHEN charged.rentalDays > 0 THEN charged.quantity ELSE 0 END) AS totalQuantity,
                    SUM(charged.rentalDays * charged.rentalPrice * charged.quantity) AS totalRevenue
                FROM tblRentalBill rb
                INNER JOIN tblClient c ON c.id = rb.clientId
                INNER JOIN tblUser u ON u.id = rb.userId
                INNER JOIN (
                    SELECT rc.rentalBillId,
                        ret.returnedQuantity AS quantity,
                        rc.rentalPrice,
                        GREATEST(DATEDIFF(
                            LEAST(DATE(rtb.returnedAt), DATE(?)),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays
                    FROM tblRentedCostume rc
                    INNER JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    INNER JOIN tblReturnBill rtb ON rtb.id = ret.returnBillId
                    WHERE rc.costumeId = ?
                        AND DATE(rc.rentedAt) <= DATE(?)
                        AND DATE(rtb.returnedAt) >= DATE(?)

                    UNION ALL

                    SELECT rc.rentalBillId,
                        rc.rentalQuantity - IFNULL(SUM(ret.returnedQuantity), 0) AS quantity,
                        rc.rentalPrice,
                        GREATEST(DATEDIFF(
                            DATE(?),
                            GREATEST(DATE(rc.rentedAt), DATE(?))
                        ) + 1, 0) AS rentalDays
                    FROM tblRentedCostume rc
                    LEFT JOIN tblReturnedCostume ret ON ret.rentedCostumeId = rc.id
                    WHERE rc.costumeId = ?
                        AND DATE(rc.rentedAt) <= DATE(?)
                    GROUP BY rc.id, rc.rentalBillId, rc.rentalQuantity, rc.rentalPrice, rc.rentedAt
                    HAVING quantity > 0
                ) charged ON charged.rentalBillId = rb.id
                WHERE charged.rentalDays > 0
                    AND charged.quantity > 0
                GROUP BY rb.id, rb.createdAt, rb.saleoff, rb.note, c.id, c.fullname, u.id, u.fullname
                ORDER BY totalRevenue DESC, rb.createdAt ASC, rb.id ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            Date startSqlDate = new Date(startDate.getTime());
            Date endSqlDate = new Date(endDate.getTime());

            ps.setDate(1, endSqlDate);
            ps.setDate(2, startSqlDate);
            ps.setInt(3, costumeId);
            ps.setDate(4, endSqlDate);
            ps.setDate(5, startSqlDate);
            ps.setDate(6, endSqlDate);
            ps.setDate(7, startSqlDate);
            ps.setInt(8, costumeId);
            ps.setDate(9, endSqlDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RentalBill rentalBill = mapRentalBillSummary(rs);
                rentalBill.setTotalQuantity(rs.getInt("totalQuantity"));
                rentalBill.setTotalRevenue(rs.getFloat("totalRevenue"));
                rentalBill.setListRentalCollateral(getRentalCollaterals(rentalBill.getId()));
                result.add(rentalBill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<RentalCollateral> getRentalCollaterals(int rentalBillId) {
        List<RentalCollateral> result = new ArrayList<>();
        String sql = """
                SELECT rc.id, rc.value, rc.note,
                    c.id AS collateralId, c.name AS collateralName, c.type AS collateralType, c.note AS collateralNote
                FROM tblRentalCollateral rc
                INNER JOIN tblCollateral c ON c.id = rc.collateralId
                WHERE rc.rentalBillId = ?
                ORDER BY rc.id ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, rentalBillId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Collateral collateral = new Collateral();
                collateral.setId(rs.getInt("collateralId"));
                collateral.setName(rs.getString("collateralName"));
                collateral.setType(String.valueOf(rs.getInt("collateralType")));
                collateral.setNote(rs.getString("collateralNote"));

                RentalCollateral rentalCollateral = new RentalCollateral();
                rentalCollateral.setId(rs.getInt("id"));
                rentalCollateral.setValue(rs.getFloat("value"));
                rentalCollateral.setNote(rs.getString("note"));
                rentalCollateral.setCollateral(collateral);
                result.add(rentalCollateral);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private RentalBill mapRentalBillSummary(ResultSet rs) throws java.sql.SQLException {
        RentalBill rentalBill = new RentalBill();
        rentalBill.setId(rs.getInt("id"));
        rentalBill.setCreatedAt(toLocalDateTime(rs.getTimestamp("createdAt")));
        rentalBill.setSaleoff(rs.getFloat("saleoff"));
        rentalBill.setNote(rs.getString("note"));

        Client client = new Client();
        client.setId(rs.getInt("clientId"));
        client.setFullname(rs.getString("clientName"));
        rentalBill.setClient(client);

        User seller = new User();
        seller.setId(rs.getInt("sellerId"));
        seller.setFullName(rs.getString("sellerName"));
        rentalBill.setSeller(seller);
        return rentalBill;
    }

    private java.time.LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}
