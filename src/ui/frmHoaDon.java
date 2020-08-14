/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.ChiTietHDDAO;
import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.SanPhamDAO;
import dao.TongTienDAO;
import helper.DialogHelper;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.ChiTietHD;
import model.HoaDon;
import model.KhachHang;
import model.SanPham;
import model.TongTien;

/**
 *
 * @author qng_nhat
 */
public class frmHoaDon extends javax.swing.JFrame {

    int index = 0;
    int indexOfTongTien = 0;
    int indexChiTietHoaDon = 0;
    
    HoaDonDAO hoaDonDAO = new HoaDonDAO();
    ChiTietHDDAO chiTietHoaDonDAO = new ChiTietHDDAO();
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    KhachHangDAO khachHangDAO = new KhachHangDAO();
    TongTienDAO tongTienDAO = new TongTienDAO();
    
    public frmHoaDon() {
        initComponents();
        init();
    }

    private void init() {
        setLocationRelativeTo(null);
        loadTableHoaDon();
        loadComboboxTenSanPham();
        loadComboboxTenKhachHang();
        setActiveButton(lblList);
        setInactiveButton(lblNew);
        setInactiveButton(lblDetail);
    }

    
    
    void setActiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 112, 206));
    }

    void setInactiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 140, 206));
    }
    
    private boolean checkEmpty() {
        if (txtMaKhachHang.getText().equals("")) {
            txtMaKhachHang.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập mã khách hàng đặt hóa đơn");
            return false;
        }

        if (txtMaNhanVien.getText().equals("")) {
            txtMaNhanVien.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập mã nhân viên của bạn");
            return false;
        }
        
        if (txtSoLuong.getText().equals("")) {
            txtSoLuong.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập số lượng sản phẩm");
            return false;
        }
        return true;
    }

    void setModel(HoaDon model) {
        cboTenSP.setToolTipText(String.valueOf(model.getMaHD()));
        cboTenSP.setSelectedItem(hoaDonDAO.findById(model.getMaHD()));
        txtMaHD.setText(String.valueOf(model.getMaHD()));
        dateNgayLap.setDate(model.getDatengayLapHD());
        DateNgayGiaoHang.setDate(model.getDatengayGiaoHang());
        cboTenKH.setToolTipText(String.valueOf(model.getMaKH()));
        cboTenKH.setSelectedItem(hoaDonDAO.findById(model.getMaHD()));
        txtMaKhachHang.setText(String.valueOf(model.getMaKH()));
        txtMaNhanVien.setText(String.valueOf(model.getMaNV()));
    }

    void setModelTongTien(TongTien modelTongTien) {
        txtTongTien.setText(String.valueOf(modelTongTien.getTongTien()) + " VND");
    }

    HoaDon getModel() {
        HoaDon hoaDon = new HoaDon();
        SanPham sanPham = (SanPham) cboTenSP.getSelectedItem();
        hoaDon.setDatengayLapHD((dateNgayLap.getDate()));
        hoaDon.setDatengayGiaoHang((DateNgayGiaoHang.getDate()));
        KhachHang khachHang = (KhachHang) cboTenKH.getSelectedItem();
        hoaDon.setMaKH(Integer.parseInt(txtMaKhachHang.getText()));
        hoaDon.setMaNV(Integer.parseInt(txtMaNhanVien.getText()));
        hoaDon.setTenKH(String.valueOf(cboTenKH.getSelectedItem()));
        return hoaDon;
    }

    ChiTietHD getModelChiTietHoaDon() {
        ChiTietHD ChiTietHoaDonModel = new ChiTietHD();
        ChiTietHoaDonModel.setMaHD(Integer.parseInt(txtMaHD.getText()));
        ChiTietHoaDonModel.setMaSP(Integer.parseInt(txtMaSP.getText()));
        ChiTietHoaDonModel.setTenSP(String.valueOf(cboTenSP.getSelectedItem()));
        ChiTietHoaDonModel.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
        return ChiTietHoaDonModel;
    }
    
    void loadTableHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<HoaDon> list = hoaDonDAO.select();
            for (HoaDon hoaDon : list) {
                Object[] row = {
                    hoaDon.getMaHD(),
                    hoaDon.getDatengayLapHD(),
                    hoaDon.getDatengayGiaoHang(),
                    hoaDon.getTenKH(),
                    hoaDon.getMaKH(),
                    hoaDon.getMaNV()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!!!");
        }
    }

    void editHoaDon() {
        try {
            int hd = (int) tblGridView.getValueAt(this.index, 0);
            HoaDon model = hoaDonDAO.findById(hd);
            if (model != null) {
                this.setModel(model);
            }
            DefaultTableModel tbmodel = (DefaultTableModel) tblChiTietHD.getModel();
            tbmodel.setRowCount(0);
            List<Object[]> list = chiTietHoaDonDAO.findById(hd);
            for (Object[] row : list) {
                tbmodel.addRow(row);
            }
            txtMaHDFake.setText(txtMaHD.getText());
            txtMaHDReal.setText(txtMaHD.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi truy vấn dữ liệu!");
        }
        exportTongTien();
    }
    
    void loadTableChiTietHoaDon() {
        DefaultTableModel model = (DefaultTableModel) tblChiTietHD.getModel();
        model.setRowCount(0);
        try {
            int maHoaDon = Integer.parseInt(txtMaHD.getText());
            DefaultTableModel tbmodel = (DefaultTableModel) tblChiTietHD.getModel();
            tbmodel.setRowCount(0);
            List<Object[]> list = chiTietHoaDonDAO.findById(maHoaDon);
            for (Object[] row : list) {
                tbmodel.addRow(row);
            }
        } catch (NumberFormatException e) {
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!!!");
        }
    }
    
    void loadComboboxTenSanPham() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenSP.getModel();
        model.removeAllElements();
        try {
            List<SanPham> list = sanPhamDAO.select();
            for (SanPham sanPham : list) {
                model.addElement(sanPham);
            }
        } catch (Exception ex) {
        }
    }

    void loadComboboxTenKhachHang() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenKH.getModel();
        model.removeAllElements();
        try {
            List<KhachHang> list = khachHangDAO.select();
            for (KhachHang khachHang : list) {
                model.addElement(khachHang);
            }
        } catch (Exception e) {
        }
    }

    void loadMaSanPham() {
        String tenSP = String.valueOf(cboTenSP.getSelectedItem());
        SanPham model = sanPhamDAO.findByName(tenSP);
        txtMaSP.setText(String.valueOf(model.getMaSP()));
    }

    void loadMaKhachHang() {
        String tenKH = String.valueOf(cboTenKH.getSelectedItem());
        KhachHang model = khachHangDAO.findByName(tenKH);
        txtMaKhachHang.setText(String.valueOf(model.getMaKH()));
    }
    
    //asdasdasd
    
    void clear() {
        txtMaHD.setText("");
        txtMaKhachHang.setText("");
        txtMaNhanVien.setText("");
        dateNgayLap.setDate(null);
        DateNgayGiaoHang.setDate(null);
        txtSoLuong.setText("");
        txtTongTien.setText("");
        txtMaSP.setText("");
        txtMaHDFake.setText("");
        txtMaHDReal.setText("");
    }
    
    void insertHoaDon() {
        HoaDon hoaDon = getModel();
        if (checkEmpty()) {
            if (DialogHelper.confirm(this, "Xác nhận?")) {
                try {
                    hoaDonDAO.insert(hoaDon);
                    this.loadTableHoaDon();
                    exportHoaDonID();
                    DialogHelper.alert(this, "Thêm mới thành công");

                } catch (Exception e) {
                    DialogHelper.alert(this, "Thêm mới thất bại!!!");
                }
            }
        }
    }

    void deleteHoaDon() {
        if (DialogHelper.confirm(this, "Bạn muốn xoá hoá đơn này?")) {
            int mahd = Integer.parseInt(txtMaHD.getText());
            try {
                hoaDonDAO.delete(mahd);
                this.loadTableHoaDon();
                DialogHelper.alert(this, "Xoá thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Xoá thất bại");
            }
        }
    }

    void insertSanPhamToHoaDon() {
        ChiTietHD ct = getModelChiTietHoaDon();
        if (checkEmpty()) {
            try {
                chiTietHoaDonDAO.insert(ct);
                this.loadTableChiTietHoaDon();
                exportTongTien();
                DialogHelper.alert(this, "Thêm sản phẩm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Thêm sản phẩm thất bại!!!");
            }
        }
    }
    
    private void exportTongTien() {
        try {
            int hd = (int) tblGridView.getValueAt(this.index, 0);
            TongTien modeltt = tongTienDAO.TongTien(hd);
            if (modeltt != null) {
                this.setModelTongTien(modeltt);
            }
        } catch (Exception e) {
        }
    }
    
    

    void removeSanPhamFromHoaDon() {
        if (DialogHelper.confirm(this, "Loại sản phẩm này khỏi hoá đơn?")) {
            int maHoaDon = Integer.parseInt(txtMaHD.getText());
            String tenSanPham = (String) tblChiTietHD.getValueAt(this.indexChiTietHoaDon, 0);
            try {
                chiTietHoaDonDAO.delete(maHoaDon, tenSanPham);
                this.loadTableChiTietHoaDon();
                exportTongTien();
                DialogHelper.alert(this, "Thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Thất bại!!");
            }
        }
    }

    public void exportHoaDonID() {
        try {
            String userName = "sa";
            String password = "songlong";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=QLNUOCGIAIKHAT";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            try (Connection con = DriverManager.getConnection(url, userName, password); Statement st = con.createStatement()) {
                String sql = "SELECT MAX(MaHD) AS MaHD FROM HOADON";
                try (ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        int maHD = rs.getInt(1);
                        txtMaHD.setText(String.valueOf(maHD));
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlCenter = new javax.swing.JPanel();
        lblList = new javax.swing.JLabel();
        lblNew = new javax.swing.JLabel();
        lblDetail = new javax.swing.JLabel();
        pnlBottom = new javax.swing.JPanel();
        pnlList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        pnlNewBill = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnXoaHD = new javax.swing.JButton();
        pnlDetail = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtMaNhanVien = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        txtMaHDReal = new javax.swing.JTextField();
        txtMaKhachHang = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel8 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        dateNgayLap = new com.toedter.calendar.JDateChooser();
        DateNgayGiaoHang = new com.toedter.calendar.JDateChooser();
        cboTenKH = new javax.swing.JComboBox<>();
        pnlDetails = new javax.swing.JPanel();
        pnlDetailBill = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        btnAddSP = new javax.swing.JButton();
        btnXoaSP = new javax.swing.JButton();
        cboTenSP = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        txtMaHDFake = new javax.swing.JTextField();
        jSeparator13 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        pnlListOfDetailBill = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblChiTietHD = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        txtTongTien = new javax.swing.JTextField();
        hiden = new javax.swing.JPanel();
        txtMaHD = new javax.swing.JTextField();
        lblMaHD = new javax.swing.JLabel();
        txtMaSP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMaKH = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("bill");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTop.setBackground(new java.awt.Color(0, 140, 206));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("bill");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_bill_96px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(753, Short.MAX_VALUE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel3)
                .addGap(0, 4, Short.MAX_VALUE))
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(pnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 930, -1));

        pnlCenter.setBackground(new java.awt.Color(0, 140, 206));

        lblList.setBackground(new java.awt.Color(0, 112, 206));
        lblList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblList.setText("bill list");
        lblList.setOpaque(true);
        lblList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblListMouseClicked(evt);
            }
        });

        lblNew.setBackground(new java.awt.Color(0, 112, 206));
        lblNew.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblNew.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNew.setText("new bill");
        lblNew.setOpaque(true);
        lblNew.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblNewMouseClicked(evt);
            }
        });

        lblDetail.setBackground(new java.awt.Color(0, 112, 206));
        lblDetail.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDetail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetail.setText("bill details");
        lblDetail.setOpaque(true);
        lblDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDetailMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblNew, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNew, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDetail, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(pnlCenter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 104, 930, -1));

        pnlBottom.setLayout(new java.awt.CardLayout());

        pnlList.setBackground(new java.awt.Color(0, 112, 206));

        tblGridView.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "MÃ HOÁ ĐƠN", "NGÀY LẬP HOÁ ĐƠN", "NGÀY GIAO HÀNG", "TÊN KHÁCH HÀNG", "MÃ KHÁCH HÀNG", "MÃ NHÂN VIÊN"
            }
        ));
        tblGridView.setEnabled(false);
        tblGridView.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView.setRowHeight(22);
        tblGridView.setRowMargin(0);
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);

        javax.swing.GroupLayout pnlListLayout = new javax.swing.GroupLayout(pnlList);
        pnlList.setLayout(pnlListLayout);
        pnlListLayout.setHorizontalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 910, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlBottom.add(pnlList, "card3");

        pnlNewBill.setBackground(new java.awt.Color(0, 0, 0));

        pnlButton.setBackground(new java.awt.Color(0, 99, 194));

        btnClear.setBackground(new java.awt.Color(255, 255, 255));
        btnClear.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_refresh_32px.png"))); // NOI18N
        btnClear.setText("Làm mới");
        btnClear.setBorder(null);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(255, 255, 255));
        btnSave.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnSave.setText("Thêm");
        btnSave.setBorder(null);
        btnSave.setBorderPainted(false);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnXoaHD.setBackground(new java.awt.Color(255, 255, 255));
        btnXoaHD.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoaHD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_trash_32px_1.png"))); // NOI18N
        btnXoaHD.setText("Xóa");
        btnXoaHD.setBorder(null);
        btnXoaHD.setPreferredSize(new java.awt.Dimension(71, 33));
        btnXoaHD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaHDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlButtonLayout.createSequentialGroup()
                .addGap(116, 116, 116)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDetail.setBackground(new java.awt.Color(0, 112, 206));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Ngày Lập");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Mã Nhân Viên");

        txtMaNhanVien.setBackground(new java.awt.Color(0, 112, 206));
        txtMaNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaNhanVien.setForeground(new java.awt.Color(229, 229, 229));
        txtMaNhanVien.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel10.setText("Ngày Giao Hàng");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("Tên Khách Hàng");

        txtMaHDReal.setBackground(new java.awt.Color(0, 112, 206));
        txtMaHDReal.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaHDReal.setForeground(new java.awt.Color(229, 229, 229));
        txtMaHDReal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtMaHDReal.setRequestFocusEnabled(false);
        txtMaHDReal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMaHDRealActionPerformed(evt);
            }
        });

        txtMaKhachHang.setBackground(new java.awt.Color(0, 112, 206));
        txtMaKhachHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaKhachHang.setForeground(new java.awt.Color(229, 229, 229));
        txtMaKhachHang.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMaKhachHang.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtMaKhachHang.setFocusable(false);

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setText("Mã Khách Hàng");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Mã Hóa Đơn");

        dateNgayLap.setBackground(new java.awt.Color(0, 112, 206));
        dateNgayLap.setDateFormatString("dd/MM/yyyy");
        dateNgayLap.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        DateNgayGiaoHang.setBackground(new java.awt.Color(0, 112, 206));
        DateNgayGiaoHang.setDateFormatString("dd/MM/yyyy");
        DateNgayGiaoHang.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        cboTenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cboTenKH.setBorder(null);
        cboTenKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenKHActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDetailLayout = new javax.swing.GroupLayout(pnlDetail);
        pnlDetail.setLayout(pnlDetailLayout);
        pnlDetailLayout.setHorizontalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(348, 348, 348)
                        .addComponent(jLabel13))
                    .addComponent(jLabel2)
                    .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jSeparator10, javax.swing.GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
                            .addComponent(cboTenKH, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(46, 46, 46)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtMaKhachHang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator12, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtMaHDReal, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(dateNgayLap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(27, 27, 27)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel10)
                            .addComponent(jSeparator9, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)
                            .addComponent(DateNgayGiaoHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(17, 17, 17))
        );
        pnlDetailLayout.setVerticalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                .addContainerGap(67, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel6)
                    .addComponent(jLabel10))
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtMaHDReal, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(DateNgayGiaoHang, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateNgayLap, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel13))
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(txtMaKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cboTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addComponent(jLabel2)
                .addGap(5, 5, 5)
                .addComponent(txtMaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(62, 62, 62))
        );

        javax.swing.GroupLayout pnlNewBillLayout = new javax.swing.GroupLayout(pnlNewBill);
        pnlNewBill.setLayout(pnlNewBillLayout);
        pnlNewBillLayout.setHorizontalGroup(
            pnlNewBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNewBillLayout.createSequentialGroup()
                .addComponent(pnlDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlNewBillLayout.setVerticalGroup(
            pnlNewBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDetail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlBottom.add(pnlNewBill, "card2");

        pnlDetails.setBackground(new java.awt.Color(0, 112, 206));

        pnlDetailBill.setBackground(new java.awt.Color(0, 112, 206));
        pnlDetailBill.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Tên Sản Phẩm");
        pnlDetailBill.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));
        pnlDetailBill.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 340, 10));
        pnlDetailBill.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 280, 80, 10));

        btnAddSP.setBackground(new java.awt.Color(255, 255, 255));
        btnAddSP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnAddSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnAddSP.setText("Thêm");
        btnAddSP.setBorder(null);
        btnAddSP.setBorderPainted(false);
        btnAddSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddSPActionPerformed(evt);
            }
        });
        pnlDetailBill.add(btnAddSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 320, 125, 50));

        btnXoaSP.setBackground(new java.awt.Color(255, 255, 255));
        btnXoaSP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoaSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_trash_32px_1.png"))); // NOI18N
        btnXoaSP.setText("Xóa");
        btnXoaSP.setBorder(null);
        btnXoaSP.setPreferredSize(new java.awt.Dimension(71, 33));
        btnXoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaSPActionPerformed(evt);
            }
        });
        pnlDetailBill.add(btnXoaSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 320, 125, 50));

        cboTenSP.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cboTenSP.setBorder(null);
        cboTenSP.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cboTenSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenSPActionPerformed(evt);
            }
        });
        pnlDetailBill.add(cboTenSP, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 340, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("Số Lượng");
        pnlDetailBill.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, -1, -1));

        txtSoLuong.setBackground(new java.awt.Color(0, 112, 206));
        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoLuong.setForeground(new java.awt.Color(242, 242, 242));
        txtSoLuong.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlDetailBill.add(txtSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 80, 42));

        txtMaHDFake.setBackground(new java.awt.Color(0, 112, 206));
        txtMaHDFake.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMaHDFake.setForeground(new java.awt.Color(242, 242, 242));
        txtMaHDFake.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtMaHDFake.setRequestFocusEnabled(false);
        pnlDetailBill.add(txtMaHDFake, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 150, 42));
        pnlDetailBill.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 150, 10));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Mã Hóa Đơn");
        pnlDetailBill.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, -1, -1));

        pnlListOfDetailBill.setBackground(new java.awt.Color(0, 99, 194));

        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        tblChiTietHD.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblChiTietHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tên SP", "Số Lượng", "Đơn Giá"
            }
        ));
        tblChiTietHD.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblChiTietHD.setGridColor(new java.awt.Color(255, 255, 255));
        tblChiTietHD.setRowHeight(22);
        tblChiTietHD.setRowMargin(0);
        tblChiTietHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietHDMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblChiTietHD);

        javax.swing.GroupLayout pnlListOfDetailBillLayout = new javax.swing.GroupLayout(pnlListOfDetailBill);
        pnlListOfDetailBill.setLayout(pnlListOfDetailBillLayout);
        pnlListOfDetailBillLayout.setHorizontalGroup(
            pnlListOfDetailBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListOfDetailBillLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlListOfDetailBillLayout.setVerticalGroup(
            pnlListOfDetailBillLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListOfDetailBillLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Tổng Tiền");

        txtTongTien.setBackground(new java.awt.Color(0, 112, 206));
        txtTongTien.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        txtTongTien.setForeground(new java.awt.Color(255, 102, 0));
        txtTongTien.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtTongTien.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout pnlDetailsLayout = new javax.swing.GroupLayout(pnlDetails);
        pnlDetails.setLayout(pnlDetailsLayout);
        pnlDetailsLayout.setHorizontalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(pnlDetailBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailsLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(pnlListOfDetailBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(22, Short.MAX_VALUE))
                    .addGroup(pnlDetailsLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))))
        );
        pnlDetailsLayout.setVerticalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlDetailBill, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDetailsLayout.createSequentialGroup()
                        .addComponent(pnlListOfDetailBill, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pnlBottom.add(pnlDetails, "card4");

        txtMaHD.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtMaHD.setEnabled(false);

        lblMaHD.setText("Mã hoá đơn");

        txtMaSP.setEditable(false);
        txtMaSP.setEnabled(false);

        jLabel4.setText("Mã Sản Phẩm");

        txtMaKH.setEditable(false);
        txtMaKH.setEnabled(false);

        jLabel5.setText("Mã Khách Hàng");

        javax.swing.GroupLayout hidenLayout = new javax.swing.GroupLayout(hiden);
        hiden.setLayout(hidenLayout);
        hidenLayout.setHorizontalGroup(
            hidenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidenLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hidenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(823, Short.MAX_VALUE))
        );
        hidenLayout.setVerticalGroup(
            hidenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hidenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblMaHD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMaKH, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(281, Short.MAX_VALUE))
        );

        pnlBottom.add(hiden, "card5");

        getContentPane().add(pnlBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 134, 930, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblListMouseClicked
        setActiveButton(lblList);
        setInactiveButton(lblNew);
        setInactiveButton(lblDetail);
        pnlList.setVisible(true);
        pnlNewBill.setVisible(false);
        pnlDetails.setVisible(false);
    }//GEN-LAST:event_lblListMouseClicked

    private void lblNewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblNewMouseClicked
        setActiveButton(lblNew);
        setInactiveButton(lblList);
        setInactiveButton(lblDetail);
        pnlList.setVisible(false);
        pnlNewBill.setVisible(true);
        pnlDetails.setVisible(false);
    }//GEN-LAST:event_lblNewMouseClicked

    private void lblDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDetailMouseClicked
        setActiveButton(lblDetail);
        setInactiveButton(lblNew);
        setInactiveButton(lblList);
        pnlDetails.setVisible(true);
        pnlList.setVisible(false);
        pnlNewBill.setVisible(false);
    }//GEN-LAST:event_lblDetailMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clear();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        insertHoaDon();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnXoaHDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaHDActionPerformed
        deleteHoaDon();
        clear();
    }//GEN-LAST:event_btnXoaHDActionPerformed

    private void btnAddSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddSPActionPerformed
        insertSanPhamToHoaDon();
    }//GEN-LAST:event_btnAddSPActionPerformed

    private void btnXoaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaSPActionPerformed
        removeSanPhamFromHoaDon();
    }//GEN-LAST:event_btnXoaSPActionPerformed

    private void tblChiTietHDMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietHDMouseClicked
        if (evt.getClickCount() == 2) {
            this.indexChiTietHoaDon = tblGridView.rowAtPoint(evt.getPoint());
            if (this.indexChiTietHoaDon >= 0) {
                this.editHoaDon();
            }
        }
    }//GEN-LAST:event_tblChiTietHDMouseClicked

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        if (evt.getClickCount() == 2) {
            txtTongTien.setText("");
            txtMaHDFake.setText("");
            txtSoLuong.setText("");
            this.index = tblGridView.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editHoaDon();
                try {
                    TableModel tbmodel = tblGridView.getModel();
                    int srow = tblGridView.getSelectedRow();
                    Date dateNgayLapHd = new SimpleDateFormat("dd/MM/yyyy").parse((String) tbmodel.getValueAt(srow, 1));
                    Date dateNgayGiaoHang = new SimpleDateFormat("dd/MM/yyyy").parse((String) tbmodel.getValueAt(srow, 2));
                    this.dateNgayLap.setDate(dateNgayLapHd);
                    this.DateNgayGiaoHang.setDate(dateNgayGiaoHang);
                } catch (Exception e) {
                }

                pnlList.setVisible(false);
                pnlNewBill.setVisible(true);
                pnlDetails.setVisible(false);
                setActiveButton(lblNew);
                setInactiveButton(lblList);
                setInactiveButton(lblDetail);
            }
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void cboTenSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenSPActionPerformed
        loadMaSanPham();
    }//GEN-LAST:event_cboTenSPActionPerformed

    private void cboTenKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenKHActionPerformed
        loadMaKhachHang();
    }//GEN-LAST:event_cboTenKHActionPerformed

    private void txtMaHDRealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMaHDRealActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMaHDRealActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new frmHoaDon().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser DateNgayGiaoHang;
    private javax.swing.JButton btnAddSP;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnXoaHD;
    private javax.swing.JButton btnXoaSP;
    private javax.swing.JComboBox<String> cboTenKH;
    private javax.swing.JComboBox<String> cboTenSP;
    private com.toedter.calendar.JDateChooser dateNgayLap;
    private javax.swing.JPanel hiden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblDetail;
    private javax.swing.JLabel lblList;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblNew;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlDetailBill;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlListOfDetailBill;
    private javax.swing.JPanel pnlNewBill;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblChiTietHD;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtMaHD;
    private javax.swing.JTextField txtMaHDFake;
    private javax.swing.JTextField txtMaHDReal;
    private javax.swing.JTextField txtMaKH;
    private javax.swing.JTextField txtMaKhachHang;
    private javax.swing.JTextField txtMaNhanVien;
    private javax.swing.JTextField txtMaSP;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTongTien;
    // End of variables declaration//GEN-END:variables
}
