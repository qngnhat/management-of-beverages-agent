/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Date;

/**
 *
 * @author qng_nhat
 */
public class HoaDon {
    private int maHD;
    private Date datengayLapHD, datengayGiaoHang;
    private String tenKH;
    private int maKH;
    private int maNV;

    public HoaDon() {
    }

    public HoaDon(int maHD, Date datengayLapHD, Date datengayGiaoHang, String tenKH, int maKH, int maNV) {
        this.maHD = maHD;
        this.datengayLapHD = datengayLapHD;
        this.datengayGiaoHang = datengayGiaoHang;
        this.tenKH = tenKH;
        this.maKH = maKH;
        this.maNV = maNV;
    }

    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public Date getDatengayLapHD() {
        return datengayLapHD;
    }

    public void setDatengayLapHD(Date datengayLapHD) {
        this.datengayLapHD = datengayLapHD;
    }

    public Date getDatengayGiaoHang() {
        return datengayGiaoHang;
    }

    public void setDatengayGiaoHang(Date datengayGiaoHang) {
        this.datengayGiaoHang = datengayGiaoHang;
    }

    public String getTenKH() {
        return tenKH;
    }

    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

}