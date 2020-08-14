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
import model.KhachHang;

/**
 *
 * @author qng_nhat
 */
public class KhachHangDAO {

    public void insert(KhachHang model) {
        String sql = "INSERT KhachHang (TENKH, SDT, DiaChi) VALUES (?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getTenKH(),
                model.getSdt(),
                model.getDiaChi());
    }

    public void update(KhachHang model, int maKH) {
        String sql = "UPDATE KhachHang SET TENKH = ?, SDT = ?, DIACHI = ? WHERE MAKH=?";
        JdbcHelper.executeUpdate(sql,
                model.getTenKH(),
                model.getSdt(),
                model.getDiaChi(),
                maKH);
    }

    public void delete(int maKH) {
        String sql = "DELETE FROM KhachHang WHERE MAKH = ?";
        JdbcHelper.executeUpdate(sql, maKH);
    }

    private KhachHang readFromResultSet(ResultSet rs) throws SQLException {
        KhachHang model = new KhachHang();
        model.setMaKH(rs.getInt("MaKH"));
        model.setTenKH(rs.getString("TENKH"));
        model.setSdt(rs.getString("Sdt"));
        model.setDiaChi(rs.getString("DiaChi"));
        return model;
    }

    private List<KhachHang> select(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    KhachHang model = readFromResultSet(rs);
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

    public List<KhachHang> select() {
        String sql = "SELECT * FROM KhachHang";
        return select(sql);
    }

    public KhachHang findById(int maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
        List<KhachHang> list = select(sql, maKH);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<KhachHang> findByKeyword(String keyword) {
        String sql = "SELECT * FROM KhachHang WHERE TenKH LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    public KhachHang findByName(String tenKH) {
        String sql = "SELECT * FROM KHACHHANG WHERE TenKH like ?";
        List<KhachHang> list = selectName(sql, tenKH);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<KhachHang> selectName(String sql, Object... args) {
        List<KhachHang> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    KhachHang model = readFromResultSetName(rs);
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

    private KhachHang readFromResultSetName(ResultSet rs) throws SQLException {
        KhachHang model = new KhachHang();
        model.setMaKH(rs.getInt("MaKH"));
        model.setTenKH(rs.getString("TenKH"));
        return model;
    }
}
