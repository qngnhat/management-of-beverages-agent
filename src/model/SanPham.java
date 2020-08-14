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
public class SanPham {
    int maSP;
    String tenSP;
    int maLoai;
    int maNcc;
    int soLuong;
    int giaBan;
    String tenLoai;
    String tenNCC;
    String moTa;
    byte[] hinh;

//    @Override
//    public String toString() {
//        return this.tenSP;
//    }

    @Override
    public String toString() {
        return this.tenSP;
    }

    public SanPham(int maSP, String tenSP, int maLoai, int maNcc, int soLuong, int giaBan, String tenLoai, String tenNCC, String moTa, byte[] hinh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.maLoai = maLoai;
        this.maNcc = maNcc;
        this.soLuong = soLuong;
        this.giaBan = giaBan;
        this.tenLoai = tenLoai;
        this.tenNCC = tenNCC;
        this.moTa = moTa;
        this.hinh = hinh;
    }

    public SanPham() {
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getMaNcc() {
        return maNcc;
    }

    public void setMaNcc(int maNcc) {
        this.maNcc = maNcc;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(int giaBan) {
        this.giaBan = giaBan;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public byte[] getHinh() {
        return hinh;
    }

    public void setHinh(byte[] hinh) {
        this.hinh = hinh;
    }

    

    
}
