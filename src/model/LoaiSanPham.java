/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author qng_nhat
 */
public class LoaiSanPham {
    int maLoai;
    String tenLoai;
    int maNCC;

    @Override
    public String toString() {
        return this.tenLoai;
    }

    public LoaiSanPham() {
    }

    public LoaiSanPham(int maLoai, String tenLoai, int maNCC) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.maNCC = maNCC;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }
    
    
}
