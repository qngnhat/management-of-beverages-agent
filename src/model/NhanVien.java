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
public class NhanVien {
    int maNV;
    String tenNV;
    String matKhau;
    String sdt;
    String cmnd;
    boolean vaiTro = false;
    
    public String toString(){
        return this.tenNV;
    }

    public NhanVien() {
    }

    public NhanVien(int maNV, String tenNV, String matKhau, String sdt, String cmnd, boolean vaiTro) {
        this.maNV = maNV;
        this.tenNV = tenNV;
        this.matKhau = matKhau;
        this.sdt = sdt;
        this.cmnd = cmnd;
        this.vaiTro = vaiTro;
    }

    public int getMaNV() {
        return maNV;
    }

    public String getTenNV() {
        return tenNV;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public String getSdt() {
        return sdt;
    }

    public String getCmnd() {
        return cmnd;
    }

    public boolean isVaiTro() {
        return vaiTro;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public void setTenNV(String tenNV) {
        this.tenNV = tenNV;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public void setCmnd(String cmnd) {
        this.cmnd = cmnd;
    }

    public void setVaiTro(boolean vaiTro) {
        this.vaiTro = vaiTro;
    }

    
}
