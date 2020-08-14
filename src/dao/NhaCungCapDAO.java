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
import model.NhaCungCap;

/**
 *
 * @author qng_nhat
 */
public class NhaCungCapDAO {

    public void insert(NhaCungCap model) {
        String sql = "INSERT NhaCungCap (TENNCC, SDT, DIACHI) VALUES (?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getTenNCC(),
                model.getSdt(),
                model.getDiaChi()
        );
    }

    public void update(NhaCungCap model, int maNCC) {
        String sql = "UPDATE NhaCungCap SET TENNCC = ?, SDT = ?, DIACHI = ? WHERE MANCC = ?";
        JdbcHelper.executeUpdate(sql,
                model.getTenNCC(),
                model.getSdt(),
                model.getDiaChi(),
                maNCC);
    }

    public void delete(int maNCC) {
        String sql = "DELETE FROM NHACUNGCAP WHERE MANCC = ?";
        JdbcHelper.executeUpdate(sql, maNCC);
    }

    private NhaCungCap readFromResultSet(ResultSet rs) throws SQLException {
        NhaCungCap model = new NhaCungCap();
        model.setMaNCC(rs.getInt("MaNcc"));
        model.setTenNCC(rs.getString("TenNcc"));
        model.setSdt(rs.getString("Sdt"));
        model.setDiaChi(rs.getString("DiaChi"));
        return model;
    }

    private List<NhaCungCap> select(String sql, Object... args) {
        List<NhaCungCap> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    NhaCungCap model = readFromResultSet(rs);
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

    public List<NhaCungCap> select() {
        String sql = "SELECT * FROM NHACUNGCAP";
        return select(sql);
    }

    public NhaCungCap findById(int maNCC) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE MANCC = ?";
        List<NhaCungCap> list = select(sql, maNCC);
        return list.size() > 0 ? list.get(0) : null;
    }

    public NhaCungCap findByName(String tenNCC) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE TenNCC like ?";
        List<NhaCungCap> list = select(sql, tenNCC);
        return list.size() > 0 ? list.get(0) : null;
    }

    public List<NhaCungCap> findByKeyword(String keyword) {
        String sql = "SELECT * FROM NHACUNGCAP WHERE TenNCC LIKE ?";
        return select(sql, "%" + keyword + "%");
    }
}
