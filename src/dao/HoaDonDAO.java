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
import model.HoaDon;

/**
 *
 * @author qng_nhat
 */
public class HoaDonDAO {

    public void insert(HoaDon model) {
        String sql = "INSERT INTO HOADON VALUES(?,?,?,?,?)";
        JdbcHelper.executeUpdate(sql,
                model.getDatengayLapHD(),
                model.getDatengayGiaoHang(),
                model.getTenKH(),
                model.getMaKH(),
                model.getMaNV());
    }

    public void delete(int MaHD) {
        String sql = "DELETE FROM HOADON WHERE MaHD=?";
        JdbcHelper.executeUpdate(sql, MaHD);
    }

    public List<HoaDon> select() {
        String sql = "SELECT * FROM HOADON";
        return select(sql);
    }

    public List<HoaDon> xuatMaHD() {
        String sql = "SELECT MAX(MaHD) AS MaHD FROM HOADON";
        return select(sql);
    }

    public HoaDon findById(int mahd) {
        String sql = "SELECT * FROM HOADON WHERE MaHD=?";
        List<HoaDon> list = select(sql, mahd);
        return list.size() > 0 ? list.get(0) : null;
    }

    private List<HoaDon> select(String sql, Object... args) {
        List<HoaDon> list = new ArrayList<>();
        try {
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while (rs.next()) {
                    HoaDon model = readFromResultSet(rs);
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

    private HoaDon readFromResultSet(ResultSet rs) throws SQLException {
        HoaDon model = new HoaDon();
        model.setMaHD(rs.getInt("MaHD"));
        model.setDatengayLapHD(rs.getDate("NgayLapHD"));
        model.setDatengayGiaoHang(rs.getDate("NgayGiaoHang"));
        model.setTenKH(rs.getString("TenKH"));
        model.setMaKH(rs.getInt("MaKH"));
        model.setMaNV(rs.getInt("MaNV"));
        return model;
    }
}
