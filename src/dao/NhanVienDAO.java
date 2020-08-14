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
import model.NhanVien;

/**
 *
 * @author qng_nhat
 */
public class NhanVienDAO {

    public void insert(NhanVien model) {
        String sql = "INSERT NHANVIEN (TENNV, MATKHAU, SDT, CMND, VAITRO) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getTenNV(),
                model.getMatKhau(),
                model.getSdt(),
                model.getCmnd(),
                model.isVaiTro());
    }

    public void update(NhanVien model, int maNV) {
        String sql = "UPDATE NHANVIEN SET TENNV = ?, MATKHAU = ?, SDT = ?, CMND = ?, VAITRO = ? WHERE MANV = ?";
        JdbcHelper.executeUpdate(sql,
                model.getTenNV(),
                model.getMatKhau(),
                model.getSdt(),
                model.getCmnd(),
                model.isVaiTro(),
                maNV);
    }

    public void delete(int maNV) {
        String sql = "DELETE FROM NHANVIEN WHERE MANV = ?";
        JdbcHelper.executeUpdate(sql, maNV);
    }

    private NhanVien readFromResultSet(ResultSet rs) throws SQLException {
        NhanVien model = new NhanVien();
        model.setMaNV(rs.getInt("MaNV"));
        model.setTenNV(rs.getString("TenNV"));
        model.setMatKhau(rs.getString("MatKhau"));
        model.setSdt(rs.getString("Sdt"));
        model.setCmnd(rs.getString("CMND"));
        model.setVaiTro(rs.getBoolean("VaiTro"));
        return model;
    }

    private List<NhanVien> select(String sql, Object... args) {
        List<NhanVien> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhanVien model = readFromResultSet(rs);
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

    public List<NhanVien> select() {
        String sql = "SELECT * FROM NHANVIEN";
        return select(sql);
    }

    public NhanVien findByName(String tenNV) {
        String sql = "SELECT * FROM NHANVIEN WHERE TENNV = ?";
        List<NhanVien> list = select(sql, tenNV);
        return list.size() > 0 ? list.get(0) : null;
    }

    public NhanVien findById(int maNV) {
        String sql = "SELECT * FROM NHANVIEN WHERE MANV = ?";
        List<NhanVien> list = select(sql, maNV);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<NhanVien> findByKeyword(String keyword) {
        String sql = "SELECT * FROM NHANVIEN WHERE TenNV LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

}
