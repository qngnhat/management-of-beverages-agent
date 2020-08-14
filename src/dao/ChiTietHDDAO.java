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
import model.ChiTietHD;

/**
 *
 * @author qng_nhat
 */
public class ChiTietHDDAO {

    public void insert(ChiTietHD model) {
        String sql = "EXEC USP_THEM_SP_CTHOADON ?,?,?,?";
        JdbcHelper.executeUpdate(sql,
                model.getMaHD(),
                model.getMaSP(),
                model.getTenSP(),
                model.getSoLuong());
    }

    public void update(ChiTietHD model) {
        String sql = " UPDATE CHITIETHOADON SET MAHD=?, MASP=?, TENSP=?, "
                + "SOLUONG=?, TONGTIEN=?";
        JdbcHelper.executeUpdate(sql,
                model.getMaHD(),
                model.getMaSP(),
                model.getTenSP(),
                model.getSoLuong());
    }

    public void delete(int maHD, String tenSP) {
        String sql = "DELETE FROM CHITIETHOADON WHERE MaHD=? and TenSP=?";
        JdbcHelper.executeUpdate(sql, maHD, tenSP);
    }

    public List<Object[]> findById(int mahd) {
        List<Object[]> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                String sql = "SELECT * FROM LOADCT, phu WHERE MaHD=?";
                rs = JdbcHelper.executeQuery(sql, mahd);
                while (rs.next()) {
                    Object[] model = {
                        rs.getString("TENSP"),
                        rs.getInt("SOLUONG"),
                        rs.getInt("GIABAN")
                    };
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return list;
    }

    private List<ChiTietHD> select(String sql, Object... args) {
        List<ChiTietHD> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    ChiTietHD model = readFromResultSet(rs);
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

    private ChiTietHD readFromResultSet(ResultSet rs) throws SQLException {
        ChiTietHD model = new ChiTietHD();
        model.setMaHD(rs.getInt("MAHD"));
        model.setTenSP(rs.getString("TenSP"));
        model.setMaSP(rs.getInt("MASP"));
        model.setSoLuong(rs.getInt("SOLUONG"));
        model.setGiaBan(rs.getInt("GIABAN"));
        model.setTongTien(rs.getInt("TONG"));
        return model;
    }
}
