/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author btpha
 */
public class ThongKe {
    String tenNCC;
    String tenSP;
    String tenLoai;
    int SoLuong;
    int doanhThu;

    public ThongKe() {
    }

    public ThongKe(String tenNCC, String tenSP, String tenLoai, int SoLuong, int doanhThu) {
        this.tenNCC = tenNCC;
        this.tenSP = tenSP;
        this.tenLoai = tenLoai;
        this.SoLuong = SoLuong;
        this.doanhThu = doanhThu;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public int getSoLuong() {
        return SoLuong;
    }

    public void setSoLuong(int SoLuong) {
        this.SoLuong = SoLuong;
    }

    public int getDoanhThu() {
        return doanhThu;
    }

    public void setDoanhThu(int doanhThu) {
        this.doanhThu = doanhThu;
    }
    
    
}
