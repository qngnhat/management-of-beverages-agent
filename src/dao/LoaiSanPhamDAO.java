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
import model.LoaiSanPham;

/**
 *
 * @author qng_nhat
 */
public class LoaiSanPhamDAO {

    public void insert(LoaiSanPham model) {
        String sql = "INSERT LoaiNGK (TENLOAI, MANCC) VALUES (?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getTenLoai(),
                model.getMaNCC()
        );
    }

    public void update(LoaiSanPham model, int maLoai) {
        String sql = "UPDATE LoaiNGK SET TENLOAI = ?, MANCC = ? WHERE MALOAI = ?";
        JdbcHelper.executeUpdate(sql,
                model.getTenLoai(),
                model.getMaNCC(),
                maLoai);
    }

    public void delete(int maLoai) {
        String sql = "DELETE FROM LOAINGK WHERE MALOAI = ?";
        JdbcHelper.executeUpdate(sql, maLoai);
    }

    private LoaiSanPham readFromResultSet(ResultSet rs) throws SQLException {
        LoaiSanPham model = new LoaiSanPham();
        model.setMaLoai(rs.getInt("MaLoai"));
        model.setTenLoai(rs.getString("TenLoai"));
        model.setMaNCC(rs.getInt("MaNCC"));
        return model;
    }

    private List<LoaiSanPham> select(String sql, Object... args) {
        List<LoaiSanPham> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    LoaiSanPham model = readFromResultSet(rs);
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

    public List<LoaiSanPham> select() {
        String sql = "SELECT * FROM LoaiNGK";
        return select(sql);
    }

    public LoaiSanPham findById(int maLoai) {
        String sql = "SELECT * FROM LoaiNGK WHERE MALOAI = ?";
        List<LoaiSanPham> list = select(sql, maLoai);
        return list.size() > 0 ? list.get(0) : null;
    }

    public LoaiSanPham findByName(String tenSP) {
        String sql = "SELECT * FROM LoaiNGK WHERE TenLoai like ?";
        List<LoaiSanPham> listloai = select(sql, tenSP);
        return listloai.size() > 0 ? listloai.get(0) : null;
    }

    public List<LoaiSanPham> findByMaNhaCungCap(int maNCC) {
        String sql = "SELECT * FROM LoaiNGK WHERE MaNCC = ?";
        List<LoaiSanPham> list = select(sql, maNCC);
        return list;
    }
}
