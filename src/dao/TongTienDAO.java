/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JdbcHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.TongTien;

/**
 *
 * @author qng_nhat
 */
public class TongTienDAO {
    
    public TongTien TongTien(int mahd){
        String sql = "SELECT * FROM TongTien WHERE MaHD=?";
        List<TongTien> list = select(sql, mahd);
        return list.size() > 0 ? list.get(0) : null;
    }
    private List<TongTien> select(String sql, Object... args) {
        List<TongTien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    TongTien model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return list;
    }
    
    private TongTien readFromResultSet(ResultSet rs) throws SQLException {
        TongTien model = new TongTien();
        model.setTongTien(rs.getInt("TONG"));
        return model;
    }
}
