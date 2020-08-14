/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.LoaiSanPhamDAO;
import dao.NhaCungCapDAO;
import helper.DialogHelper;
import helper.GlobalData;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import model.LoaiSanPham;
import model.NhaCungCap;

/**
 *
 * @author qng_nhat
 */
public class frmNhaCungCap extends javax.swing.JFrame {

    /**
     * Creates new form frmNhaCungCapNew
     */
    public frmNhaCungCap() {
        initComponents();
        loadTableNhaCungCap();
        loadLNGK();
        loadComboboxTenNhaCungCap();
        setLocationRelativeTo(null);
        setActiveButton(lblListNhaCungCap);
        setInactiveButton(lblDetailsNhaCungCap);
        setInactiveButton(lblType);
    }

    int indexNCC = 0;
    int indexLNGK = 0;

    NhaCungCapDAO nhaCungCapDAO = new NhaCungCapDAO();
    LoaiSanPhamDAO loaiNGKDAO = new LoaiSanPhamDAO();

    private boolean checkNcc() {
        if (txtTenNhaCC.getText().equals("")) {
            txtTenNhaCC.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập tên nhà cung cấp");
            return false;
        }
        if (txtSDT.getText().equals("")) {
            txtSDT.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập số điện thoại");
            return false;
        }
        String sdtPattern = "^(0\\d{9,10})$";
        boolean checkSDT = Pattern.matches(sdtPattern, txtSDT.getText());
        if (!checkSDT) {
            txtSDT.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập đúng số điện thoại");
            return false;
        }
        if (txtDiaChi.getText().equals("")) {
            txtDiaChi.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập địa chỉ");
            return false;
        }
        return true;
    }

    private boolean checkLoai() {
        if (txtTenLoai.getText().equals("")) {
            txtTenLoai.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập tên loại sản phẩm");
            return false;
        }
        if (txtTenLoai.getText().length() < 6) {
            txtTenLoai.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập đúng tên nhà cung cấp");
            return false;
        }
        if (txtMaNCCLNGK.getText().equals("")) {
            txtMaNCCLNGK.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập mã nhà cung cấp sản phẩm");
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
        txtTenNhaCC.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtKeyword.setText("");
    }

    NhaCungCap getModelNhaCungCap() {
        NhaCungCap model = new NhaCungCap();
        model.setTenNCC(txtTenNhaCC.getText());
        model.setSdt(txtSDT.getText());
        model.setDiaChi(txtDiaChi.getText());
        return model;
    }

    void setModelNhaCungCap(NhaCungCap model) {
        txtTenNhaCC.setText(model.getTenNCC());
        txtSDT.setText(model.getSdt());
        txtDiaChi.setText(String.valueOf(model.getDiaChi()));
    }

    LoaiSanPham getModelLNGK() {
        LoaiSanPham model = new LoaiSanPham();
        model.setTenLoai(txtTenLoai.getText());
        model.setMaNCC(Integer.parseInt(txtMaNCCLNGK.getText()));
        return model;
    }

    void setModelLNGK(LoaiSanPham model) {
        txtTenLoai.setText(model.getTenLoai());
        txtMaNCCLNGK.setText(String.valueOf(model.getMaNCC()));

    }

    private boolean protectTabFromStaff() {
        if (GlobalData.getLoggedRole()) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        return true;
    }
    
    private boolean accessIntoLoaiNGK(){
        if(GlobalData.getLoggedRole()){
            btnThemLNGK.setVisible(false);
            btnXoaLNGK.setVisible(false);
            btnSuaLNGK.setVisible(false);
            btnLamMoiLNGK.setVisible(false);
        }
        return true;
    }

    void loadComboboxTenNhaCungCap(){
        DefaultComboBoxModel model = (DefaultComboBoxModel) cboTenNhaCungCap.getModel();
        model.removeAllElements();
        try {
            List<NhaCungCap> list = nhaCungCapDAO.select();
            for(NhaCungCap nhaCungCap : list)
                model.addElement(nhaCungCap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void loadMaNhaCungCap(){
        String TenNCC = String.valueOf(cboTenNhaCungCap.getSelectedItem());
        NhaCungCap model = nhaCungCapDAO.findByName(TenNCC);
        txtMaNCCLNGK.setText(String.valueOf(model.getMaNCC()));
    }
    void loadTableNhaCungCap() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<NhaCungCap> list = nhaCungCapDAO.select();
            for (NhaCungCap ncc : list) {
                Object[] row = {
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSdt(),
                    ncc.getDiaChi(),};
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void searchNhaCungCap() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtKeyword.getText();
            List<NhaCungCap> list = nhaCungCapDAO.findByKeyword(keyword);
            for (NhaCungCap ncc : list) {
                Object[] row = {
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSdt(),
                    ncc.getDiaChi()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void insertNhaCungCap() {
        NhaCungCap model = getModelNhaCungCap();
        if (checkNcc()) {
            try {
                nhaCungCapDAO.insert(model);
                this.loadTableNhaCungCap();
                DialogHelper.alert(this, "Thêm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Vui lòng nhập đúng thông tin");
                e.printStackTrace();
            }
        }
    }

    void updateNhaCungCap() {
        NhaCungCap model = getModelNhaCungCap();
        int maNCC = (int) tblGridView.getValueAt(this.indexNCC, 0);
        if (checkNcc()) {
            try {
                nhaCungCapDAO.update(model, maNCC);
                this.loadTableNhaCungCap();
                DialogHelper.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Vui lòng nhập đúng thông tin");
                e.printStackTrace();
            }
        }
    }

    void deleteNhaCungCap() {
        int maNCC = (int) tblGridView.getValueAt(this.indexNCC, 0);
        try {
            nhaCungCapDAO.delete(maNCC);
            this.loadTableNhaCungCap();
            this.clear();
            DialogHelper.alert(this, "Xóa nhà cung cấp thành công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Xóa không thành công");
            e.printStackTrace();
        }
    }

    void editNhaCungCap() {
        try {
            int maNCC = (int) tblGridView.getValueAt(this.indexNCC, 0);
            NhaCungCap model = nhaCungCapDAO.findById(maNCC);
            if (model != null) {
                this.setModelNhaCungCap(model);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadLNGK() {
        DefaultTableModel model = (DefaultTableModel) tblGridView2.getModel();
        model.setRowCount(0);
        try {
            List<LoaiSanPham> list = loaiNGKDAO.select();
            for (LoaiSanPham loaingk : list) {
                Object[] row = {
                    loaingk.getMaLoai(),
                    loaingk.getTenLoai(),
                    loaingk.getMaNCC()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void clearLNGK() {
        txtMaNCCLNGK.setText("");
        txtTenLoai.setText("");
    }

    void insertLNGK() {
        LoaiSanPham model = getModelLNGK();
        if (checkLoai()) {
            try {
                loaiNGKDAO.insert(model);
                this.loadLNGK();
                DialogHelper.alert(this, "Thêm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Vui lòng nhập đúng thông tin");
                e.printStackTrace();
            }
        }
    }

    void updateLNGK() {
        LoaiSanPham model = getModelLNGK();
        int maLoaiNGK = (int) tblGridView2.getValueAt(this.indexLNGK, 0);
        if (checkLoai()) {
            try {
                loaiNGKDAO.update(model, maLoaiNGK);
                this.loadLNGK();
                DialogHelper.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Vui lòng nhập đúng thông tin");
                e.printStackTrace();
            }
        }
    }

    void deleteLNGK() {
        int maLoaiNGK = (int) tblGridView2.getValueAt(this.indexLNGK, 0);
        try {
            loaiNGKDAO.delete(maLoaiNGK);
            this.loadLNGK();
            this.clearLNGK();
            DialogHelper.alert(this, "Xóa nhà loại sản phẩm thành công");
        } catch (Exception e) {
            DialogHelper.alert(this, "Xóa không thành công");
            e.printStackTrace();
        }

    }

    void editLNGK() {
        try {
            int maLoaiNGK = (int) tblGridView2.getValueAt(this.indexLNGK, 0);
            LoaiSanPham model = loaiNGKDAO.findById(maLoaiNGK);
            if (model != null) {
                this.setModelLNGK(model);
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

        pnlTop = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        pnlCenter = new javax.swing.JPanel();
        lblListNhaCungCap = new javax.swing.JLabel();
        lblDetailsNhaCungCap = new javax.swing.JLabel();
        lblType = new javax.swing.JLabel();
        pnlBottom = new javax.swing.JPanel();
        pnlList = new javax.swing.JPanel();
        txtKeyword = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGridView = new javax.swing.JTable();
        pnlDetails = new javax.swing.JPanel();
        pnlButton = new javax.swing.JPanel();
        btnLamMoi = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        pnlDetail = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtDiaChi = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        txtTenNhaCC = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        txtSDT = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        pnlType = new javax.swing.JPanel();
        pnlDetailType = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtTenLoai = new javax.swing.JTextField();
        jSeparator7 = new javax.swing.JSeparator();
        txtMaNCCLNGK = new javax.swing.JTextField();
        jSeparator8 = new javax.swing.JSeparator();
        btnThemLNGK = new javax.swing.JButton();
        btnXoaLNGK = new javax.swing.JButton();
        btnSuaLNGK = new javax.swing.JButton();
        btnLamMoiLNGK = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        cboTenNhaCungCap = new javax.swing.JComboBox<>();
        pnlListOfType = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblGridView2 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("suppliers");
        setResizable(false);

        pnlTop.setBackground(new java.awt.Color(0, 140, 206));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("supplier");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_supplier_96px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTopLayout.setVerticalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addGap(0, 4, Short.MAX_VALUE))
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlCenter.setBackground(new java.awt.Color(0, 140, 206));

        lblListNhaCungCap.setBackground(new java.awt.Color(0, 112, 206));
        lblListNhaCungCap.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblListNhaCungCap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblListNhaCungCap.setText("supplier list");
        lblListNhaCungCap.setOpaque(true);
        lblListNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblListNhaCungCapMouseClicked(evt);
            }
        });

        lblDetailsNhaCungCap.setBackground(new java.awt.Color(0, 112, 206));
        lblDetailsNhaCungCap.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblDetailsNhaCungCap.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDetailsNhaCungCap.setText("supplier details");
        lblDetailsNhaCungCap.setOpaque(true);
        lblDetailsNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblDetailsNhaCungCapMouseClicked(evt);
            }
        });

        lblType.setBackground(new java.awt.Color(0, 112, 206));
        lblType.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblType.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblType.setText("type of product");
        lblType.setOpaque(true);
        lblType.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTypeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlCenterLayout = new javax.swing.GroupLayout(pnlCenter);
        pnlCenter.setLayout(pnlCenterLayout);
        pnlCenterLayout.setHorizontalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlCenterLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lblListNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblDetailsNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblListNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDetailsNhaCungCap, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblType, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlBottom.setLayout(new java.awt.CardLayout());

        pnlList.setBackground(new java.awt.Color(0, 112, 206));

        txtKeyword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Nhập tên nhà cung cấp:");

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_32px.png"))); // NOI18N
        btnTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTimKiemMouseClicked(evt);
            }
        });

        tblGridView.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "MÃ NCC", "TÊN NCC", "SĐT", "ĐỊA CHỈ"
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

        javax.swing.GroupLayout pnlListLayout = new javax.swing.GroupLayout(pnlList);
        pnlList.setLayout(pnlListLayout);
        pnlListLayout.setHorizontalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pnlListLayout.createSequentialGroup()
                .addGap(222, 222, 222)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTimKiem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17))
        );

        pnlBottom.add(pnlList, "card3");

        pnlDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlButton.setBackground(new java.awt.Color(0, 99, 194));

        btnLamMoi.setBackground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_refresh_32px.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorderPainted(false);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(255, 255, 255));
        btnSua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_32px.png"))); // NOI18N
        btnSua.setText("Cập nhật");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(255, 255, 255));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_trash_32px_1.png"))); // NOI18N
        btnXoa.setText("Xóa");
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
                .addContainerGap(70, Short.MAX_VALUE)
                .addGroup(pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65))
        );
        pnlButtonLayout.setVerticalGroup(
            pnlButtonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlButtonLayout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        pnlDetails.add(pnlButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 0, 260, 360));

        pnlDetail.setBackground(new java.awt.Color(0, 112, 206));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tên Nhà Cung Cấp");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Số Điện Thoại");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Địa Chỉ");

        txtDiaChi.setBackground(new java.awt.Color(0, 112, 206));
        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDiaChi.setForeground(new java.awt.Color(204, 204, 204));
        txtDiaChi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtTenNhaCC.setBackground(new java.awt.Color(0, 112, 206));
        txtTenNhaCC.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenNhaCC.setForeground(new java.awt.Color(204, 204, 204));
        txtTenNhaCC.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtSDT.setBackground(new java.awt.Color(0, 112, 206));
        txtSDT.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSDT.setForeground(new java.awt.Color(204, 204, 204));
        txtSDT.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout pnlDetailLayout = new javax.swing.GroupLayout(pnlDetail);
        pnlDetail.setLayout(pnlDetailLayout);
        pnlDetailLayout.setHorizontalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                .addContainerGap(48, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtDiaChi)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(txtTenNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addGap(42, 42, 42)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(txtSDT)
                            .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(39, 39, 39))
        );
        pnlDetailLayout.setVerticalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(txtTenNhaCC, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(txtSDT, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtDiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(113, 113, 113))
        );

        pnlDetails.add(pnlDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 360));

        pnlBottom.add(pnlDetails, "card2");

        pnlType.setBackground(new java.awt.Color(0, 112, 206));

        pnlDetailType.setBackground(new java.awt.Color(0, 112, 206));
        pnlDetailType.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Tên Nhà Cung Cấp");
        pnlDetailType.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Mã Nhà Cung Cấp");
        pnlDetailType.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, -1, -1));

        txtTenLoai.setBackground(new java.awt.Color(0, 112, 206));
        txtTenLoai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTenLoai.setForeground(new java.awt.Color(204, 204, 204));
        txtTenLoai.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlDetailType.add(txtTenLoai, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 346, 30));
        pnlDetailType.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 346, 10));

        txtMaNCCLNGK.setEditable(false);
        txtMaNCCLNGK.setBackground(new java.awt.Color(0, 112, 206));
        txtMaNCCLNGK.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtMaNCCLNGK.setForeground(new java.awt.Color(204, 204, 204));
        txtMaNCCLNGK.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        pnlDetailType.add(txtMaNCCLNGK, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 182, 100, 30));
        pnlDetailType.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 220, 100, 10));

        btnThemLNGK.setBackground(new java.awt.Color(255, 255, 255));
        btnThemLNGK.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnThemLNGK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnThemLNGK.setText("Thêm");
        btnThemLNGK.setBorderPainted(false);
        btnThemLNGK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemLNGKActionPerformed(evt);
            }
        });
        pnlDetailType.add(btnThemLNGK, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 240, 125, 50));

        btnXoaLNGK.setBackground(new java.awt.Color(255, 255, 255));
        btnXoaLNGK.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnXoaLNGK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_trash_32px_1.png"))); // NOI18N
        btnXoaLNGK.setText("Xóa");
        btnXoaLNGK.setPreferredSize(new java.awt.Dimension(71, 33));
        btnXoaLNGK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLNGKActionPerformed(evt);
            }
        });
        pnlDetailType.add(btnXoaLNGK, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 240, 125, 50));

        btnSuaLNGK.setBackground(new java.awt.Color(255, 255, 255));
        btnSuaLNGK.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnSuaLNGK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_update_32px.png"))); // NOI18N
        btnSuaLNGK.setText("Cập nhật");
        btnSuaLNGK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaLNGKActionPerformed(evt);
            }
        });
        pnlDetailType.add(btnSuaLNGK, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 300, 125, 50));

        btnLamMoiLNGK.setBackground(new java.awt.Color(255, 255, 255));
        btnLamMoiLNGK.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        btnLamMoiLNGK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_refresh_32px.png"))); // NOI18N
        btnLamMoiLNGK.setText("Làm mới");
        btnLamMoiLNGK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiLNGKActionPerformed(evt);
            }
        });
        pnlDetailType.add(btnLamMoiLNGK, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, 125, 50));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Tên Sản Phẩm");
        pnlDetailType.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, -1, -1));

        cboTenNhaCungCap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboTenNhaCungCapActionPerformed(evt);
            }
        });
        pnlDetailType.add(cboTenNhaCungCap, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 350, 30));

        pnlListOfType.setBackground(new java.awt.Color(0, 99, 194));

        tblGridView2.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblGridView2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã Loại", "Tên Loại", "Tên Nhà Cung Cấp"
            }
        ));
        tblGridView2.setEnabled(false);
        tblGridView2.setGridColor(new java.awt.Color(255, 255, 255));
        tblGridView2.setRowHeight(22);
        tblGridView2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGridView2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                tblGridView2MouseEntered(evt);
            }
        });
        jScrollPane2.setViewportView(tblGridView2);

        javax.swing.GroupLayout pnlListOfTypeLayout = new javax.swing.GroupLayout(pnlListOfType);
        pnlListOfType.setLayout(pnlListOfTypeLayout);
        pnlListOfTypeLayout.setHorizontalGroup(
            pnlListOfTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListOfTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlListOfTypeLayout.setVerticalGroup(
            pnlListOfTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListOfTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlTypeLayout = new javax.swing.GroupLayout(pnlType);
        pnlType.setLayout(pnlTypeLayout);
        pnlTypeLayout.setHorizontalGroup(
            pnlTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTypeLayout.createSequentialGroup()
                .addComponent(pnlDetailType, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlListOfType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTypeLayout.setVerticalGroup(
            pnlTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTypeLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlDetailType, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlListOfType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(38, 38, 38))
        );

        pnlBottom.add(pnlType, "card4");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(pnlBottom, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 910, Short.MAX_VALUE)
                    .addComponent(pnlTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(pnlBottom, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseClicked
        searchNhaCungCap();
    }//GEN-LAST:event_btnTimKiemMouseClicked

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clear();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        insertNhaCungCap();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (DialogHelper.confirm(this, "Cập nhật nhà cung cấp này?")) {
            updateNhaCungCap();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (DialogHelper.confirm(this, "Xóa nhà cung cấp này?")) {
            deleteNhaCungCap();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void lblListNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblListNhaCungCapMouseClicked
        setActiveButton(lblListNhaCungCap);
        setInactiveButton(lblDetailsNhaCungCap);
        setInactiveButton(lblType);
        pnlList.setVisible(true);
        pnlDetails.setVisible(false);
        pnlType.setVisible(false);
    }//GEN-LAST:event_lblListNhaCungCapMouseClicked

    private void lblDetailsNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblDetailsNhaCungCapMouseClicked
        if (protectTabFromStaff()) {
            setActiveButton(lblDetailsNhaCungCap);
            setInactiveButton(lblListNhaCungCap);
            setInactiveButton(lblType);
            pnlList.setVisible(false);
            pnlDetails.setVisible(true);
            pnlType.setVisible(false);
        }
    }//GEN-LAST:event_lblDetailsNhaCungCapMouseClicked
    
    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        if (evt.getClickCount() == 2) {
            if (protectTabFromStaff()) {
                this.indexNCC = tblGridView.rowAtPoint(evt.getPoint());
                if (this.indexNCC >= 0) {
                    this.editNhaCungCap();
                    pnlList.setVisible(false);
                    pnlDetails.setVisible(true);
                    setActiveButton(lblDetailsNhaCungCap);
                    setInactiveButton(lblListNhaCungCap);
                }
            }
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void btnLamMoiLNGKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiLNGKActionPerformed
        clearLNGK();
    }//GEN-LAST:event_btnLamMoiLNGKActionPerformed

    private void btnThemLNGKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemLNGKActionPerformed
        insertLNGK();
    }//GEN-LAST:event_btnThemLNGKActionPerformed

    private void btnSuaLNGKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaLNGKActionPerformed
        if (DialogHelper.confirm(this, "Cập nhật loại sản phẩm này?")) {
            updateLNGK();
        }
    }//GEN-LAST:event_btnSuaLNGKActionPerformed

    private void btnXoaLNGKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLNGKActionPerformed
        if (DialogHelper.confirm(this, "Xóa loại sản phẩm này?")) {
            deleteLNGK();
        }
    }//GEN-LAST:event_btnXoaLNGKActionPerformed

    private void tblGridView2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseClicked
        if (evt.getClickCount() == 2) {
            this.indexLNGK = tblGridView2.rowAtPoint(evt.getPoint());
            if (this.indexLNGK >= 0) {
                this.editLNGK();
            }
        }
    }//GEN-LAST:event_tblGridView2MouseClicked

    private void lblTypeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTypeMouseClicked
        if (accessIntoLoaiNGK()) {
            setActiveButton(lblType);
            setInactiveButton(lblDetailsNhaCungCap);
            setInactiveButton(lblListNhaCungCap);
            pnlType.setVisible(true);
            pnlList.setVisible(false);
            pnlDetails.setVisible(false);
        }
    }//GEN-LAST:event_lblTypeMouseClicked

    private void cboTenNhaCungCapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboTenNhaCungCapActionPerformed
        loadMaNhaCungCap();
    }//GEN-LAST:event_cboTenNhaCungCapActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            searchNhaCungCap();
        }
        if(evt.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            loadTableNhaCungCap();
        }
    }//GEN-LAST:event_txtKeywordKeyPressed

    private void tblGridView2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridView2MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGridView2MouseEntered

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
            java.util.logging.Logger.getLogger(frmNhaCungCap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmNhaCungCap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmNhaCungCap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmNhaCungCap.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNhaCungCap().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLamMoiLNGK;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnSuaLNGK;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThemLNGK;
    private javax.swing.JLabel btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaLNGK;
    private javax.swing.JComboBox<String> cboTenNhaCungCap;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblDetailsNhaCungCap;
    private javax.swing.JLabel lblListNhaCungCap;
    private javax.swing.JLabel lblType;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlDetailType;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlListOfType;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JPanel pnlType;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTable tblGridView2;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtMaNCCLNGK;
    private javax.swing.JTextField txtSDT;
    private javax.swing.JTextField txtTenLoai;
    private javax.swing.JTextField txtTenNhaCC;
    // End of variables declaration//GEN-END:variables
}
