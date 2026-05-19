package dao;

import model.CostumeStatistic;

import java.sql.*;
import java.util.*;
import java.text.*;
import java.util.Date;

public class CostumeStatisticDAO extends DAO {
    public CostumeStatisticDAO() {
        super();
    }

    public List<CostumeStatistic> getCostumeStatistic(Date startDate, Date endDate){
        List<CostumeStatistic> result = new ArrayList<>();
        String sql = "";

        try {
            PreparedStatement ps = con.prepareStatement(sql);


            ResultSet rs = ps.executeQuery();


        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
