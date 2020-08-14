/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import helper.JdbcHelper;
import model.NhanVien;
import model.SanPham;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author qng_nhat
 */
public class SanPhamDAO {

    public void insert(SanPham model) {
        String sql = "INSERT INTO NUOCGIAIKHAT (TenSP, MaLoai, MaNCC, SoLuong, GiaBan) VALUES (?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getTenSP(),
                model.getMaLoai(),
                model.getMaNcc(),
                model.getSoLuong(),
                model.getGiaBan());
    }

    public void update(SanPham model, int maSP) {
        String sql = "UPDATE NUOCGIAIKHAT SET TenSP = ?, MaLoai = ?, MaNCC = ?, SoLuong = ?, GiaBan = ?, MoTa = ?, Hinh = ? WHERE MaSP = ?";
        JdbcHelper.executeUpdate(sql,
                model.getTenSP(),
                model.getMaLoai(),
                model.getMaNcc(),
                model.getSoLuong(),
                model.getGiaBan(),
                model.getMoTa(),
                model.getHinh(),
                maSP);
    }

    public void delete(int maSP) {
        String sql = "DELETE FROM NUOCGIAIKHAT WHERE MASP = ?";
        JdbcHelper.executeUpdate(sql, maSP);
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
        return selectName(sql);
    }

    public List<SanPham> selectToTable() {
        String sql = "SELECT MaSP, TenSP, TenLoai, TenNcc, SoLuong, GiaBan, MoTa, Hinh FROM NUOCGIAIKHAT, LOAINGK, NHACUNGCAP "
                + "WHERE NUOCGIAIKHAT.MaLoai = LOAINGK.MaLoai AND NUOCGIAIKHAT.MaNcc = NHACUNGCAP.MaNcc";
        return selectTable(sql);
    }

    public SanPham findById(int maSP) {
        String sql = "SELECT * FROM NUOCGIAIKHAT WHERE MASP = ?";
        List<SanPham> list = select(sql, maSP);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<SanPham> findByKeyword(String keyword) {
        String sql = "SELECT * FROM SanPham WHERE TenSP LIKE ?";
        return select(sql, "%" + keyword + "%");
    }

    private SanPham readFromResultSet(ResultSet rs) throws SQLException {
        SanPham model = new SanPham();
        model.setMaSP(rs.getInt("MaSP"));
        model.setTenSP(rs.getString("TenSP"));
        model.setMaLoai(rs.getInt("MaLoai"));
        model.setMaNcc(rs.getInt("MaNcc"));
        model.setSoLuong(rs.getInt("SoLuong"));
        model.setGiaBan(rs.getInt("GiaBan"));
        return model;
    }

    private SanPham readFromResultSetName(ResultSet rs) throws SQLException {
        SanPham model = new SanPham();
        model.setMaSP(rs.getInt("MaSP"));
        model.setTenSP(rs.getString("TenSP"));
        return model;
    }

    private SanPham readFromResultSetTable(ResultSet rs) throws SQLException {
        SanPham model = new SanPham();
        model.setMaSP(rs.getInt("MaSP"));
        model.setTenSP(rs.getString("TenSP"));
        model.setTenLoai(rs.getString("TenLoai"));
        model.setTenNCC(rs.getString("TenNcc"));
        model.setSoLuong(rs.getInt("SoLuong"));
        model.setGiaBan(rs.getInt("GiaBan"));
        model.setMoTa(rs.getString("MoTa"));
        model.setHinh(rs.getBytes("Hinh"));
        return model;
    }

    private List<SanPham> selectTable(String sql, Object... args) {
        List<SanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    SanPham model = readFromResultSetTable(rs);
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
}
