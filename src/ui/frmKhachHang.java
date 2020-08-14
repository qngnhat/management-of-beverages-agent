/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.KhachHangDAO;
import helper.DialogHelper;
import helper.GlobalData;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import model.KhachHang;

/**
 *
 * @author qng_nhat
 */
public class frmKhachHang extends javax.swing.JFrame {

    int index = 0;
    KhachHangDAO dao = new KhachHangDAO();
    
    public frmKhachHang() {
        initComponents();
        init();
    }

    private void init(){
        loadTableKhachHang();
        setActiveButton(lblList);
        setInactiveButton(lblDetails);
        setLocationRelativeTo(null);
    }
    public void setActiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 112, 206));
    }

    public void setInactiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 140, 206));
    }

    private boolean checkEmpty() {
        if (txtTenKH.getText().equals("")) {
            txtTenKH.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập tên khách hàng");
            return false;
        }

        if (txtTenKH.getText().length() < 6) {
            txtTenKH.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập đầy đủ tên");
            return false;
        }

        if (txtDiaChi.getText().equals("")) {
            txtDiaChi.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập địa chỉ");
            return false;
        }

        if (txtSdt.getText().equals("")) {
            txtSdt.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập số điện thoại");
            return false;
        }
        String phonePattern = "^(0\\d{9,10})$";
        boolean checkPhone = Pattern.matches(phonePattern, txtSdt.getText());
        if (!checkPhone) {
            txtSdt.requestFocus();
            DialogHelper.alert(this, "Số điện thoại không hợp lệ");
            return false;
        }
        return true;
    }

    KhachHang getModel() {
        KhachHang model = new KhachHang();
        model.setTenKH(txtTenKH.getText());
        model.setSdt(txtSdt.getText());
        model.setDiaChi(txtDiaChi.getText());
        return model;
    }

    void setModel(KhachHang model) {
        txtTenKH.setText(model.getTenKH());
        txtSdt.setText(model.getSdt());
        txtDiaChi.setText(model.getDiaChi());
    }

    void loadTableKhachHang() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<KhachHang> list = dao.select();
            for (KhachHang kh : list) {
                Object[] row = {
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSdt(),
                    kh.getDiaChi()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
        }
    }

    void searchKhachHang() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtKeyword.getText();
            List<KhachHang> list = dao.findByKeyword(keyword);
            for (KhachHang kh : list) {
                Object[] row = {
                    kh.getMaKH(),
                    kh.getTenKH(),
                    kh.getSdt(),
                    kh.getDiaChi()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void clear() {
        setModel(new KhachHang());
    }

    private boolean protectTabFromStaff() {
        if (GlobalData.getLoggedRole()) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        return true;
    }
    
    void insertKhachHang() {
        KhachHang model = getModel();
        if (checkEmpty()) {
            try {
                dao.insert(model);
                this.loadTableKhachHang();
                DialogHelper.alert(this, "Thêm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi dữ liệu");
            }
        }
    }

    void updateKhachHang() {
        KhachHang model = getModel();
        int maKH = (int) tblGridView.getValueAt(this.index, 0);
        if (checkEmpty()) {
            try {
                dao.update(model, maKH);
                this.loadTableKhachHang();
                DialogHelper.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi dữ liệu");
            }
        }
    }

    void delete() {
        int maKH = (int) tblGridView.getValueAt(this.index, 0);
        Boolean confirm = DialogHelper.confirm(this, "Xóa khách hàng " + tblGridView.getValueAt(this.index, 1) + "?");
        if (confirm == true) {
            try {
                dao.delete(maKH);
                this.loadTableKhachHang();
                this.clear();
                DialogHelper.alert(this, "Xóa khách hàng thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi");
            }
        }
    }

    void edit() {
        try {
            int maKH = (int) tblGridView.getValueAt(this.index, 0);
            KhachHang model = dao.findById(maKH);
            if (model != null) {
                this.setModel(model);
            }
        } catch (Exception e) {
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
        lblDetails = new javax.swing.JLabel();
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
        txtTenKH = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        txtSdt = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("customer");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlTop.setBackground(new java.awt.Color(0, 140, 206));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("customer");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_member_96px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(622, Short.MAX_VALUE))
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

        getContentPane().add(pnlTop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 910, 100));

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
                .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(lblDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(649, Short.MAX_VALUE))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        getContentPane().add(pnlCenter, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 910, 30));

        pnlBottom.setLayout(new java.awt.CardLayout());

        pnlList.setBackground(new java.awt.Color(0, 112, 206));

        txtKeyword.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtKeyword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKeywordActionPerformed(evt);
            }
        });
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Nhập tên khách hàng:");

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
                "MÃ KH", "TÊN KH", "SĐT", "ĐỊA CHỈ"
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
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(208, 208, 208)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiem)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)))
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pnlBottom.add(pnlList, "card3");

        pnlDetails.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        pnlButton.setBackground(new java.awt.Color(0, 99, 194));

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

        btnThem.setBackground(new java.awt.Color(255, 255, 255));
        btnThem.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_add_32px.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setBorder(null);
        btnThem.setBorderPainted(false);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
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
        jLabel6.setText("Tên Khách Hàng");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Số Điện Thoại");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Địa Chỉ");

        txtDiaChi.setBackground(new java.awt.Color(0, 112, 206));
        txtDiaChi.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtDiaChi.setForeground(new java.awt.Color(204, 204, 204));
        txtDiaChi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtTenKH.setBackground(new java.awt.Color(0, 112, 206));
        txtTenKH.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtTenKH.setForeground(new java.awt.Color(204, 204, 204));
        txtTenKH.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtSdt.setBackground(new java.awt.Color(0, 112, 206));
        txtSdt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSdt.setForeground(new java.awt.Color(204, 204, 204));
        txtSdt.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

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
                                .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addGap(42, 42, 42)
                        .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5)
                            .addComponent(txtSdt)
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
                        .addComponent(txtTenKH, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        getContentPane().add(pnlBottom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 130, 910, 360));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseClicked
        searchKhachHang();
    }//GEN-LAST:event_btnTimKiemMouseClicked

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clear();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        insertKhachHang();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        if (DialogHelper.confirm(this, "Cập nhật nhà cung cấp này?")) {
            updateKhachHang();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (DialogHelper.confirm(this, "Xóa nhà cung cấp này?")) {
            delete();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

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
        }
    }//GEN-LAST:event_lblDetailsMouseClicked

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tblGridView.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                this.edit();
                pnlList.setVisible(false);
                pnlDetails.setVisible(true);
                setActiveButton(lblDetails);
                setInactiveButton(lblList);
            }
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void txtKeywordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKeywordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKeywordActionPerformed

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) { 
            searchKhachHang();
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
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new frmKhachHang().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lblDetails;
    private javax.swing.JLabel lblList;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtDiaChi;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTenKH;
    // End of variables declaration//GEN-END:variables
}
