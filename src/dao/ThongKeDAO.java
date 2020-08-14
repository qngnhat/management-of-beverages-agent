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
import model.ThongKe;

/**
 *
 * @author qng_nhat
 */
public class ThongKeDAO {
    private ThongKe readFromResultSet(ResultSet rs) throws SQLException{
        ThongKe model = new ThongKe();
        model.setTenNCC(rs.getString(1));
        model.setTenSP(rs.getString(2));
        model.setTenLoai(rs.getString(3));
        model.setSoLuong(rs.getInt(4));
        model.setDoanhThu(rs.getInt(5));
        return model;
    }
    
    private List<ThongKe> select(String sql, Object...args){
        List<ThongKe> list = new ArrayList<>();
        try{
            ResultSet rs = null;
            try{
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    ThongKe model = readFromResultSet(rs);
                    list.add(model);
                }
            }finally{
                rs.getStatement().getConnection().close();
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return list;
    }
    
    public List<ThongKe> select(){
        String sql = "SELECT * FROM THONGKE";
        return select(sql);
    }
}
