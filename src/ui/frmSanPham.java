/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoaiSanPhamDAO;
import dao.NhaCungCapDAO;
import dao.LoadListSPDAO;
import dao.SanPhamDAO;
import helper.DialogHelper;
import helper.GlobalData;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import model.LoaiSanPham;
import model.NhaCungCap;
import model.SanPham;
import javax.swing.JLabel;

/**
 *
 * @author qng_nhat
 */
public class frmSanPham extends javax.swing.JFrame {

    /**
     * Creates new form frmSanPham
     */
    public frmSanPham() {
        initComponents();
        loadTableSanPham();
        loadComboboxNhaCungCapAtStart();
        setLocationRelativeTo(null);
        setActiveButton(lblList);
        setInactiveButton(lblDetails);
    }

    int index = 0;

    LoadListSPDAO listSanPhamDAO = new LoadListSPDAO();
    SanPhamDAO sanPhamDAO = new SanPhamDAO();
    NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    LoaiSanPhamDAO loaiSanPhamDAO = new LoaiSanPhamDAO();

    private boolean checkEmpty() {
        if (txtTenSanPham.getText().equals("")) {
            txtTenSanPham.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập tên nước giải khát");
            return false;
        }
        if (txtTenSanPham.getText().length() < 6) {
            txtTenSanPham.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập đúng tên nước giải khát");
            return false;
        }
        if (txtGiaBanSanPham.getText().equals("")) {
            txtGiaBanSanPham.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập giá sản phẩm");
            return false;
        }
        if (txtSoLuongSanPham.getText().equals("")) {
            txtSoLuongSanPham.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập số lượng sản phẩm");
            return false;
        }
        return true;
    }

    public void setActiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 112, 206));
    }

    public void setInactiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 140, 206));
    }

    void clear() {
        txtTenSanPham.setText("");
        txtGiaBanSanPham.setText("");
        txtKeyword.setText("");
        cboLoaiSanPham.setSelectedIndex(0);
        cboTenNhaCungCap.setSelectedIndex(0);
        txtSoLuongSanPham.setText("");
    }

    private boolean protectTabFromStaff() {
        if (GlobalData.getLoggedRole()) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        return true;
    }

    void setModelSanPham(SanPham model) {
        txtMaSanPham.setText(String.valueOf(model.getMaSP()));
        txtTenSanPham.setText(model.getTenSP());
        txtMaLoaiSanPham.setText(String.valueOf(model.getMaLoai()));
        txtMaNhaCungCap.setText(String.valueOf(model.getMaNcc()));
        txtGiaBanSanPham.setText(String.valueOf(model.getGiaBan()));
        txtSoLuongSanPham.setText(String.valueOf(model.getSoLuong()));
    }

    SanPham getModelSanPham() {
        SanPham model = new SanPham();
        model.setTenSP(String.valueOf(txtTenSanPham.getText()));
        model.setMaLoai(Integer.parseInt(txtMaLoaiSanPham.getText()));
        model.setMaNcc(Integer.parseInt(txtMaNhaCungCap.getText()));
        model.setTenLoai(String.valueOf(cboLoaiSanPham.getSelectedItem()));
        model.setTenNCC((String.valueOf(cboTenNhaCungCap.getSelectedItem())));
        model.setSoLuong(Integer.parseInt(txtSoLuongSanPham.getText()));
        model.setGiaBan(Integer.parseInt(txtGiaBanSanPham.getText()));
        return model;
    }

    void loadTableSanPham() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<SanPham> list = listSanPhamDAO.select();
            for (SanPham sanPham : list) {
                Object[] row = {
                    sanPham.getMaSP(),
                    sanPham.getTenSP(),
                    sanPham.getTenLoai(),
                    sanPham.getTenNCC(),
                    sanPham.getGiaBan(),
                    sanPham.getSoLuong()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void searchSanPhamByName() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            String tenSanPham = txtKeyword.getText();
            List<SanPham> list = listSanPhamDAO.findByKeyword(tenSanPham);
            for (SanPham sanPham : list) {
                Object[] row = {
                    sanPham.getMaSP(),
                    sanPham.getTenSP(),
                    sanPham.getMaLoai(),
                    sanPham.getMaNcc(),
                    sanPham.getSoLuong(),
                    sanPham.getGiaBan(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void loadComboboxNhaCungCapAtStart() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenNhaCungCap.getModel();
        model.removeAllElements();
        try {
            List<NhaCungCap> list = nhaCungCapDAO.select();
            for (NhaCungCap ncc : list) {
                model.addElement(ncc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadCboLoaiSanPham() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLoaiSanPham.getModel();
        model.removeAllElements();
        try {
            List<LoaiSanPham> list = loaiSanPhamDAO.select();
            for (LoaiSanPham loaiSanPham : list) {
                model.addElement(loaiSanPham);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadCboLoaiSanPhamAtCboNhaCungCapClicked() {
        String TenNcc = String.valueOf(cboTenNhaCungCap.getSelectedItem());
        NhaCungCap nhaCCModel = nhaCungCapDAO.findByName(TenNcc);
        txtMaNhaCungCap.setText(String.valueOf(nhaCCModel.getMaNCC()));
        int maNhaCungCap = Integer.parseInt(txtMaNhaCungCap.getText());

        DefaultComboBoxModel model = (DefaultComboBoxModel) cboLoaiSanPham.getModel();
        model.removeAllElements();
        try {
            List<LoaiSanPham> list = loaiSanPhamDAO.findByMaNhaCungCap(maNhaCungCap);
            for (LoaiSanPham loaiSanPham : list) {
                model.addElement(loaiSanPham);
            }
        } catch (NullPointerException e) {
        }
    }

    void exportMaLoaiSanPhamAtCboLoaiSanPhamClicked() {
        try {
            String tenLoai = String.valueOf(cboLoaiSanPham.getSelectedItem());
            LoaiSanPham model = loaiSanPhamDAO.findByName(tenLoai);
            txtMaLoaiSanPham.setText(String.valueOf(model.getMaLoai()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void insertSanPham() {
        SanPham model = getModelSanPham();
        if (checkEmpty()) {
            try {
                listSanPhamDAO.insert(model);
                this.loadTableSanPham();
                DialogHelper.alert(this, "Thêm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Đã có sản phẩm này");
            }
        }
    }

    void updateSanPham() {
        SanPham model = getModelSanPham();
        int maSP = Integer.parseInt(txtMaSanPham.getText());
        if (checkEmpty()) {
            try {
                listSanPhamDAO.update(model, maSP);
                this.loadTableSanPham();
                DialogHelper.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Vui lòng nhập đúng thông tin");
                e.printStackTrace();
            }
        }
    }

    void deleteSanPham() {
        int maSP = (int) tblGridView.getValueAt(this.index, 0);
        try {
            listSanPhamDAO.delete(maSP);
            this.loadTableSanPham();
            this.clear();
            DialogHelper.alert(this, "Xóa sản phẩm thành công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi");
            e.printStackTrace();
        }
    }

    void editSanPham() {
        try {
            int maSP = (int) tblGridView.getValueAt(this.index, 0);
            String tenLoai = (String) tblGridView.getValueAt(this.index, 2);
            String tenNhaCC = (String) tblGridView.getValueAt(this.index, 3);
            cboLoaiSanPham.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{String.valueOf(tenLoai)}));
            cboTenNhaCungCap.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{String.valueOf(tenNhaCC)}));
            SanPham model = sanPhamDAO.findById(maSP);
            if (model != null) {
                this.setModelSanPham(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        jPanel1 = new javax.swing.JPanel();
        pnlTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlCenter = new javax.swing.JPanel();
        lblList = new javax.swing.JLabel();
        lblDetails = new javax.swing.JLabel();
        pnlBottom = new javax.swing.JPanel();
        pnlList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnTimKiem = new javax.swing.JButton();
        pnlDetails = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnLamMoi = new javax.swing.JButton();
        btnInsertNGK = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        pnlDetail = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtGiaBanSanPham = new javax.swing.JTextField();
        cboTenNhaCungCap = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        cboLoaiSanPham = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        txtSoLuongSanPham = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        pnlHidden = new javax.swing.JPanel();
        txtMaNhaCungCap = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtMaSanPham = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtMaLoaiSanPham = new javax.swing.JTextField();

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("product");
        setResizable(false);

        pnlTop.setBackground(new java.awt.Color(0, 140, 206));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("product");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_box_96px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTopLayout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addComponent(jLabel3))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTopLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        pnlCenter.setBackground(new java.awt.Color(0, 140, 206));

        lblList.setBackground(new java.awt.Color(0, 112, 206));
        lblList.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblList.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblList.setText("list");
        lblList.setOpaque(true);
        lblList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblListMouseClicked(evt);
            }
        });

        lblDetails.setBackground(new java.awt.Color(0, 112, 206));
        lblDetails.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDetails.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetails.setText("details");
        lblDetails.setOpaque(true);
        lblDetails.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDetailsMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(lblDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlBottom.setBackground(new java.awt.Color(0, 112, 206));
        pnlBottom.setLayout(new java.awt.CardLayout());

        pnlList.setBackground(new java.awt.Color(0, 112, 206));

        jScrollPane1.setBackground(new java.awt.Color(204, 204, 204));
        jScrollPane1.setBorder(null);
        jScrollPane1.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N

        tblGridView.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Loại sản phẩm", "Tên nhà Cung Cấp", "Giá Bán", "Số Lượng"
            }
        ));
        tblGridView.setEnabled(false);
        tblGridView.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView.setRowHeight(22);
        tblGridView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridViewMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGridView);

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        jLabel8.setText("Nhập tên sản phẩm");

        txtKeyword.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });

        btnTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnTimKiem.setText("Tìm Kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlListLayout = new javax.swing.GroupLayout(pnlList);
        pnlList.setLayout(pnlListLayout);
        pnlListLayout.setHorizontalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addGap(20, 229, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnTimKiem)
                .addGap(207, 207, 207))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                .addGap(14, 14, 14)
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTimKiem)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        pnlBottom.add(pnlList, "card3");

        pnlDetails.setBackground(new java.awt.Color(0, 112, 206));

        pnlButton.setBackground(new java.awt.Color(0, 98, 196));

        btnLamMoi.setBackground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_refresh_32px.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setBorder(null);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnInsertNGK.setBackground(new java.awt.Color(255, 255, 255));
        btnInsertNGK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnInsertNGK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnInsertNGK.setText("Thêm");
        btnInsertNGK.setBorder(null);
        btnInsertNGK.setBorderPainted(false);
        btnInsertNGK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertNGKActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 255, 255));
        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_32px.png"))); // NOI18N
        btnSua.setText("Cập nhật");
        btnSua.setBorder(null);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_trash_32px_1.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setBorder(null);
        btnXoa.setPreferredSize(new java.awt.Dimension(71, 33));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlButtonLayout = new javax.swing.GroupLayout(pnlButton);
        pnlButton.setLayout(pnlButtonLayout);
        pnlButtonLayout.setHorizontalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInsertNGK, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(71, 71, 71))
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnInsertNGK, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        pnlDetail.setBackground(new java.awt.Color(0, 112, 206));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Nhà cung cấp");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("Giá bán");

        txtGiaBanSanPham.setBackground(new java.awt.Color(0, 112, 206));
        txtGiaBanSanPham.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtGiaBanSanPham.setForeground(new java.awt.Color(204, 204, 204));
        txtGiaBanSanPham.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtGiaBanSanPham.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        cboTenNhaCungCap.setBackground(new java.awt.Color(5, 120, 216));
        cboTenNhaCungCap.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cboTenNhaCungCap.setForeground(new java.awt.Color(204, 204, 204));
        cboTenNhaCungCap.setBorder(null);
        cboTenNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenNhaCungCapActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Loại sản phẩm");

        cboLoaiSanPham.setBackground(new java.awt.Color(5, 120, 216));
        cboLoaiSanPham.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        cboLoaiSanPham.setForeground(new java.awt.Color(204, 204, 204));
        cboLoaiSanPham.setBorder(null);
        cboLoaiSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiSanPhamActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Tên sản phẩm");

        txtTenSanPham.setBackground(new java.awt.Color(0, 112, 206));
        txtTenSanPham.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenSanPham.setForeground(new java.awt.Color(204, 204, 204));
        txtTenSanPham.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtSoLuongSanPham.setBackground(new java.awt.Color(0, 112, 206));
        txtSoLuongSanPham.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSoLuongSanPham.setForeground(new java.awt.Color(204, 204, 204));
        txtSoLuongSanPham.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txtSoLuongSanPham.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Số lượng");

        javax.swing.GroupLayout pnlDetailLayout = new javax.swing.GroupLayout(pnlDetail);
        pnlDetail.setLayout(pnlDetailLayout);
        pnlDetailLayout.setHorizontalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlDetailLayout.createSequentialGroup()
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlDetailLayout.createSequentialGroup()
                                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel4)
                                    .addComponent(txtSoLuongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7)
                            .addComponent(txtGiaBanSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(cboTenNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(49, 49, 49))
        );
        pnlDetailLayout.setVerticalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                            .addComponent(jLabel6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(cboTenNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel5))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(cboLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(pnlDetailLayout.createSequentialGroup()
                            .addComponent(jLabel7)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtGiaBanSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtSoLuongSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(89, 89, 89))
        );

        javax.swing.GroupLayout pnlDetailsLayout = new javax.swing.GroupLayout(pnlDetails);
        pnlDetails.setLayout(pnlDetailsLayout);
        pnlDetailsLayout.setHorizontalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addComponent(pnlDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(pnlButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlDetailsLayout.setVerticalGroup(
            pnlDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailsLayout.createSequentialGroup()
                .addComponent(pnlDetail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 14, Short.MAX_VALUE))
            .addComponent(pnlButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pnlBottom.add(pnlDetails, "card2");

        pnlHidden.setBackground(new java.awt.Color(0, 112, 206));

        jLabel9.setText("Mã Nhà Cung cấp");

        jLabel11.setText("Mã sản phẩm");

        jLabel10.setText("Mã Loại Sản Phẩm");

        javax.swing.GroupLayout pnlHiddenLayout = new javax.swing.GroupLayout(pnlHidden);
        pnlHidden.setLayout(pnlHiddenLayout);
        pnlHiddenLayout.setHorizontalGroup(
            pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHiddenLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(56, 56, 56)
                .addGroup(pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtMaLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(549, Short.MAX_VALUE))
        );
        pnlHiddenLayout.setVerticalGroup(
            pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHiddenLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlHiddenLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMaLoaiSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlHiddenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHiddenLayout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMaSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pnlHiddenLayout.createSequentialGroup()
                            .addComponent(jLabel9)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txtMaNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(282, Short.MAX_VALUE))
        );

        pnlBottom.add(pnlHidden, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlBottom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlCenter, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (DialogHelper.confirm(this, "Xóa sản phẩm này?")) {
            deleteSanPham();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        updateSanPham();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnInsertNGKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertNGKActionPerformed
        insertSanPham();

    }//GEN-LAST:event_btnInsertNGKActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clear();
        loadCboLoaiSanPham();
        loadComboboxNhaCungCapAtStart();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        searchSanPhamByName();
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tblGridView.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.editSanPham();
                pnlList.setVisible(false);
                pnlDetails.setVisible(true);
                setActiveButton(lblDetails);
                setInactiveButton(lblList);
            }
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void lblListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblListMouseClicked
        setActiveButton(lblList);
        setInactiveButton(lblDetails);
        pnlList.setVisible(true);
        pnlDetails.setVisible(false);
    }//GEN-LAST:event_lblListMouseClicked

    private void lblDetailsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDetailsMouseClicked
        if (protectTabFromStaff()) {
            setActiveButton(lblDetails);
            setInactiveButton(lblList);
            pnlList.setVisible(false);
            pnlDetails.setVisible(true);
            txtMaLoaiSanPham.setText("");
        }
    }//GEN-LAST:event_lblDetailsMouseClicked

    private void cboTenNhaCungCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenNhaCungCapActionPerformed
        loadCboLoaiSanPhamAtCboNhaCungCapClicked();
    }//GEN-LAST:event_cboTenNhaCungCapActionPerformed

    private void cboLoaiSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiSanPhamActionPerformed
        exportMaLoaiSanPhamAtCboLoaiSanPhamClicked();
    }//GEN-LAST:event_cboLoaiSanPhamActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchSanPhamByName();
        }
    }//GEN-LAST:event_txtKeywordKeyPressed

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
            java.util.logging.Logger.getLogger(frmSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmSanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmSanPham().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnInsertNGK;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiSanPham;
    private javax.swing.JComboBox<String> cboTenNhaCungCap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JLabel lblDetails;
    private javax.swing.JLabel lblList;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlHidden;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtGiaBanSanPham;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtMaLoaiSanPham;
    private javax.swing.JTextField txtMaNhaCungCap;
    private javax.swing.JTextField txtMaSanPham;
    private javax.swing.JTextField txtSoLuongSanPham;
    private javax.swing.JTextField txtTenSanPham;
    // End of variables declaration//GEN-END:variables
}
