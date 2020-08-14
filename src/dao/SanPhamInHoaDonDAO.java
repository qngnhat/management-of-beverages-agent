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
import model.SanPham;

/**
 *
 * @author qng_nhat
 */
public class SanPhamInHoaDonDAO {

    private List<SanPham> selectName(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SanPham model = readFromResultSetName(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public SanPham findByName(String tenSP) {
        String sql = "SELECT * FROM NUOCGIAIKHAT WHERE TenSP like ?";
        List<SanPham> list = selectName(sql, tenSP);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<SanPham> select() {
        String sql = "Select * from NUOCGIAIKHAT";
        return select(sql);
    }

    private List<SanPham> select(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SanPham model = readFromResultSet(rs);
                    list.add(model);
                }
            } finally {
                rs.getStatement().getConnection().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private SanPham readFromResultSet(ResultSet rs) throws SQLException {
        SanPham model = new SanPham();
        model.setMaSP(rs.getInt("MaSP"));
        model.setTenSP(rs.getString("TenSP"));
        model.setMaLoai(rs.getInt("MaLoai"));
        model.setMaNcc(rs.getInt("MaNcc"));
        model.setSoLuong(rs.getInt("SoLuong"));
        model.setGiaBan(rs.getInt("GiaBan"));
        model.setMoTa(rs.getString("MoTa"));
        model.setHinh(rs.getBytes("Hinh"));
        return model;
    }

    private SanPham readFromResultSetName(ResultSet rs) throws SQLException {
        SanPham model = new SanPham();
        model.setMaSP(rs.getInt("MaSP"));
        model.setTenSP(rs.getString("TenSP"));
        return model;
    }
}
