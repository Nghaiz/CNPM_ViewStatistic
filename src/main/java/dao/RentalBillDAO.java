package dao;

import model.Client;
import model.Collateral;
import model.Costume;
import model.RentalBill;
import model.RentalCollateral;
import model.RentedCostume;
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
                    u.id AS sellerId, u.fullname AS sellerName
                FROM tblRentalBill rb
                INNER JOIN tblClient c ON c.id = rb.clientId
                INNER JOIN tblUser u ON u.id = rb.userId
                WHERE EXISTS (
                    SELECT 1
                    FROM tblRentedCostume rc
                    WHERE rc.rentalBillId = rb.id
                        AND rc.costumeId = ?
                        AND DATE(rc.rentedAt) <= DATE(?)
                        AND (
                            EXISTS (
                                SELECT 1
                                FROM tblReturnedCostume ret
                                INNER JOIN tblReturnBill rtb ON rtb.id = ret.returnBillId
                                WHERE ret.rentedCostumeId = rc.id
                                    AND DATE(rtb.returnedAt) >= DATE(?)
                            )
                            OR rc.rentalQuantity > (
                                SELECT IFNULL(SUM(ret2.returnedQuantity), 0)
                                FROM tblReturnedCostume ret2
                                WHERE ret2.rentedCostumeId = rc.id
                            )
                        )
                )
                ORDER BY rb.createdAt ASC, rb.id ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            Date startSqlDate = new Date(startDate.getTime());
            Date endSqlDate = new Date(endDate.getTime());

            ps.setInt(1, costumeId);
            ps.setDate(2, endSqlDate);
            ps.setDate(3, startSqlDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RentalBill rentalBill = mapRentalBillSummary(rs);
                rentalBill.setListRentedCostume(getRentedCostumes(rentalBill.getId()));
                rentalBill.setListRentalCollateral(getRentalCollaterals(rentalBill.getId()));
                result.add(rentalBill);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<RentedCostume> getRentedCostumes(int rentalBillId) {
        List<RentedCostume> result = new ArrayList<>();
        String sql = """
                SELECT rc.id, rc.rentalPrice, rc.rentalQuantity, rc.rentedAt, rc.dateToReturn,
                    c.id AS costumeId, c.name AS costumeName, c.type AS costumeType,
                    c.category AS costumeCategory, c.description AS costumeDescription, c.price AS costumePrice
                FROM tblRentedCostume rc
                INNER JOIN tblCostume c ON c.id = rc.costumeId
                WHERE rc.rentalBillId = ?
                ORDER BY rc.id ASC
                """;

        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, rentalBillId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Costume costume = new Costume();
                costume.setId(rs.getInt("costumeId"));
                costume.setName(rs.getString("costumeName"));
                costume.setType(rs.getString("costumeType"));
                costume.setCategory(rs.getString("costumeCategory"));
                costume.setDescription(rs.getString("costumeDescription"));
                costume.setPrice(rs.getFloat("costumePrice"));

                RentedCostume rentedCostume = new RentedCostume();
                rentedCostume.setId(rs.getInt("id"));
                rentedCostume.setRentalPrice(rs.getFloat("rentalPrice"));
                rentedCostume.setRentalQuantity(rs.getInt("rentalQuantity"));
                rentedCostume.setRentedAt(toLocalDateTime(rs.getTimestamp("rentedAt")));
                rentedCostume.setDateToReturn(toLocalDateTime(rs.getTimestamp("dateToReturn")));
                rentedCostume.setCostume(costume);
                result.add(rentedCostume);
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
