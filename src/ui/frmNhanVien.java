/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import dao.NhanVienDAO;
import helper.DialogHelper;
import helper.GlobalData;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.regex.Pattern;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import model.NhanVien;

/**
 *
 * @author qng_nhat
 */
public class frmNhanVien extends javax.swing.JFrame {

    /**
     * Creates new form frmNhanVienNew
     */
    public frmNhanVien() {
        initComponents();
        loadTableNhanVien();
        setLocationRelativeTo(null);
        setActiveButton(lblList);
        setInactiveButton(lblDetails);
    }

    int index = -1;

    NhanVienDAO dao = new NhanVienDAO();

    public void setActiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 112, 206));
    }

    public void setInactiveButton(JLabel lbl) {
        lbl.setBackground(new Color(0, 140, 206));
    }

    private boolean checkEmpty() {
        if (txtHoTen.getText().equals("")) {
            txtHoTen.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập tên");
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
        if (txtMatKhau.getText().equals("")) {
            txtMatKhau.requestFocus();
            DialogHelper.alert(this, "Vui lòng mật khẩu");
            return false;
        }
        if (txtMatKhau.getText().length() < 6 && txtMatKhau.getText() != null) {
            txtMatKhau.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập mật khẩu trên 6 ký tự");
            return false;
        }
        if (!txtMatKhau.getText().equals(txtXacNhanMK.getText())) {
            txtXacNhanMK.requestFocus();
            DialogHelper.alert(this, "Vui lòng xác nhận mật khẩu");
            return false;
        }
        if (txtCMND.getText().equals("")) {
            txtCMND.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập chứng minh nhân dân");
            return false;
        }
        String cmndPattern = "[0-9]{9}";
        boolean checkCmnd = Pattern.matches(cmndPattern, txtCMND.getText());
        if (!checkCmnd) {
            txtCMND.requestFocus();
            DialogHelper.alert(this, "Vui lòng nhập đúng chứng minh nhân dân");
            return false;
        }
        if (!rdoNhanVien.isSelected() && !rdoQuanLy.isSelected()) {
            DialogHelper.alert(this, "Vui lòng chọn vai trò");
            return false;
        }
        return true;
    }

    private boolean checkRole() {
        int checkMaNhanVien = (int) tblGridView.getValueAt(this.index, 0);
        if (GlobalData.getLoggedRole() == false) {
            editNhanVien();
            return true;
        }
        if (GlobalData.getLoggedMaNhanVien() == checkMaNhanVien) {
            lblRole.setVisible(false);
            rdoNhanVien.setVisible(false);
            rdoQuanLy.setVisible(false);
            btnThem.setVisible(false);
            btnXoa.setVisible(false);
            editNhanVien();
            return true;
        }
        if (GlobalData.getLoggedMaNhanVien() != checkMaNhanVien) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        if (GlobalData.getLoggedRole()) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        return true;
    }

    private boolean protectTabFromStaff() {
        if (GlobalData.getLoggedRole()) {
            DialogHelper.alert(this, "Bạn không có quyền truy cập");
            return false;
        }
        return true;
    }

    void setModel(NhanVien model) {
        txtHoTen.setText(model.getTenNV());
        txtMatKhau.setText(model.getMatKhau());
        txtSdt.setText((String.valueOf(model.getSdt())));
        txtCMND.setText(String.valueOf(model.getCmnd()));
        rdoNhanVien.setSelected(model.isVaiTro());
        rdoQuanLy.setSelected(!model.isVaiTro());
    }

    NhanVien getModel() {
        NhanVien model = new NhanVien();
        model.setTenNV(txtHoTen.getText());
        model.setMatKhau(txtMatKhau.getText());
        model.setSdt(txtSdt.getText());
        model.setCmnd(txtCMND.getText());
        model.setVaiTro(rdoNhanVien.isSelected());
        return model;
    }

    void clear() {
        txtHoTen.setText("");
        txtMatKhau.setText("");
        txtSdt.setText("");
        txtCMND.setText("");
        txtXacNhanMK.setText("");
        bgrVaiTro.clearSelection();
    }

    void loadTableNhanVien() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            List<NhanVien> list = dao.select();
            for (NhanVien nv : list) {
                Object[] row = {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getCmnd(),
                    nv.isVaiTro() ? "Nhân Viên" : "Quản Lý"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void searchNhanVien() {
        DefaultTableModel model = (DefaultTableModel) tblGridView.getModel();
        model.setRowCount(0);
        try {
            String keyword = txtTimKiem.getText();
            List<NhanVien> list = dao.findByKeyword(keyword);
            for (NhanVien nv : list) {
                Object[] row = {
                    nv.getMaNV(),
                    nv.getTenNV(),
                    nv.getSdt(),
                    nv.getCmnd(),
                    nv.isVaiTro() ? "Nhân Viên" : "Quản Lý"
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            DialogHelper.alert(this, "Lỗi dữ liệu");
            e.printStackTrace();
        }
    }

    void insertNhanVien() {
        NhanVien model = getModel();
        if (checkEmpty()) {
            try {
                dao.insert(model);
                this.loadTableNhanVien();
                DialogHelper.alert(this, "Thêm thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi dữ liệu");
                e.printStackTrace();
            }
        }
    }

    void updateNhanVien() {
        NhanVien model = getModel();
        int maNV = (int) tblGridView.getValueAt(this.index, 0);
        if (checkEmpty()) {
            try {
                dao.update(model, maNV);
                this.loadTableNhanVien();
                DialogHelper.alert(this, "Cập nhật thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi dữ liệu");
                e.printStackTrace();
            }
        }
    }

    void deleteNhanVien() {
        int maNV = (int) tblGridView.getValueAt(this.index, 0);
        if (maNV == -1) {
            DialogHelper.alert(this, "Vui lòng chọn nhân viên cần xóa");
        } else {
            try {
                dao.delete(maNV);
                this.loadTableNhanVien();
                this.clear();
                DialogHelper.alert(this, "Xóa nhân viên thành công");
            } catch (Exception e) {
                DialogHelper.alert(this, "Lỗi");
                e.printStackTrace();
            }
        }
    }

    void editNhanVien() {
        try {
            int maNhanVien = (int) tblGridView.getValueAt(this.index, 0);
            NhanVien model = dao.findById(maNhanVien);
            if (model != null) {
                this.setModel(model);
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

        bgrVaiTro = new javax.swing.ButtonGroup();
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
        txtTimKiem = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JLabel();
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
        txtMatKhau = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        txtHoTen = new javax.swing.JTextField();
        jSeparator5 = new javax.swing.JSeparator();
        txtSdt = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        txtXacNhanMK = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        txtCMND = new javax.swing.JTextField();
        lblRole = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel11 = new javax.swing.JLabel();
        rdoNhanVien = new javax.swing.JRadioButton();
        rdoQuanLy = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("staffs");
        setResizable(false);

        pnlTop.setBackground(new java.awt.Color(0, 140, 206));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36)); // NOI18N
        jLabel1.setText("staffs");

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_staff_96px.png"))); // NOI18N

        javax.swing.GroupLayout pnlTopLayout = new javax.swing.GroupLayout(pnlTop);
        pnlTop.setLayout(pnlTopLayout);
        pnlTopLayout.setHorizontalGroup(
            pnlTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTopLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addContainerGap(688, Short.MAX_VALUE))
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
                .addContainerGap(632, Short.MAX_VALUE))
        );
        pnlCenterLayout.setVerticalGroup(
            pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlCenterLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(pnlCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblList, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlBottom.setLayout(new java.awt.CardLayout());

        pnlList.setBackground(new java.awt.Color(0, 112, 206));

        tblGridView.setFont(new java.awt.Font("Segoe UI", 0, 12)); // NOI18N
        tblGridView.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "MÃ NV", "HỌ VÀ TÊN", "SỐ ĐIỆN THOẠI", "CMND", "VAI TRÒ"
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

        txtTimKiem.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Nhập tên nhân viên cần tìm:");

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_search_32px.png"))); // NOI18N
        btnTimKiem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTimKiemMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlListLayout = new javax.swing.GroupLayout(pnlList);
        pnlList.setLayout(pnlListLayout);
        pnlListLayout.setHorizontalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTimKiem)
                        .addGap(0, 273, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        pnlListLayout.setVerticalGroup(
            pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlListLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(pnlListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jLabel6.setText("Họ và Tên");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("Số Điện Thoại");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Mật khẩu");

        txtMatKhau.setBackground(new java.awt.Color(0, 112, 206));
        txtMatKhau.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtMatKhau.setForeground(new java.awt.Color(204, 204, 204));
        txtMatKhau.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtHoTen.setBackground(new java.awt.Color(0, 112, 206));
        txtHoTen.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtHoTen.setForeground(new java.awt.Color(204, 204, 204));
        txtHoTen.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtSdt.setBackground(new java.awt.Color(0, 112, 206));
        txtSdt.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtSdt.setForeground(new java.awt.Color(204, 204, 204));
        txtSdt.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        txtXacNhanMK.setBackground(new java.awt.Color(0, 112, 206));
        txtXacNhanMK.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtXacNhanMK.setForeground(new java.awt.Color(204, 204, 204));
        txtXacNhanMK.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("Xác Nhận Mật khẩu");

        txtCMND.setBackground(new java.awt.Color(0, 112, 206));
        txtCMND.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        txtCMND.setForeground(new java.awt.Color(204, 204, 204));
        txtCMND.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        lblRole.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        lblRole.setText("Vai Trò");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel11.setText("CMND");

        rdoNhanVien.setBackground(new java.awt.Color(0, 112, 206));
        bgrVaiTro.add(rdoNhanVien);
        rdoNhanVien.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdoNhanVien.setText("Nhân Viên");

        rdoQuanLy.setBackground(new java.awt.Color(0, 112, 206));
        bgrVaiTro.add(rdoQuanLy);
        rdoQuanLy.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        rdoQuanLy.setText("Quản Lý");

        javax.swing.GroupLayout pnlDetailLayout = new javax.swing.GroupLayout(pnlDetail);
        pnlDetail.setLayout(pnlDetailLayout);
        pnlDetailLayout.setHorizontalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel11)
                    .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtCMND, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(42, 42, 42)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addComponent(rdoNhanVien)
                        .addGap(18, 18, 18)
                        .addComponent(rdoQuanLy))
                    .addComponent(lblRole)
                    .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel9)
                        .addComponent(jLabel5)
                        .addComponent(txtXacNhanMK)
                        .addComponent(txtSdt)
                        .addComponent(jSeparator6)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        pnlDetailLayout.setVerticalGroup(
            pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDetailLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(txtHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDetailLayout.createSequentialGroup()
                        .addComponent(txtSdt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(6, 6, 6)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDetailLayout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtXacNhanMK, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRole)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCMND, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(rdoNhanVien)
                    .addComponent(rdoQuanLy))
                .addGap(4, 4, 4)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73))
        );

        pnlDetails.add(pnlDetail, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 360));

        pnlBottom.add(pnlDetails, "card2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pnlCenter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(pnlBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        clear();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        insertNhanVien();
        loadTableNhanVien();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        updateNhanVien();
        loadTableNhanVien();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        if (DialogHelper.confirm(this, "Xóa nhân viên phẩm này?")) {
            deleteNhanVien();
            loadTableNhanVien();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void tblGridViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGridViewMouseClicked
        if (evt.getClickCount() == 2) {
            this.index = tblGridView.rowAtPoint(evt.getPoint());
            if (this.index >= 0) {
                if (checkRole()) {
                    pnlList.setVisible(false);
                    pnlDetails.setVisible(true);
                    setActiveButton(lblDetails);
                    setInactiveButton(lblList);
                }
            }
        }
    }//GEN-LAST:event_tblGridViewMouseClicked

    private void btnTimKiemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTimKiemMouseClicked
        searchNhanVien();
    }//GEN-LAST:event_btnTimKiemMouseClicked

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            searchNhanVien();
        }
    }//GEN-LAST:event_txtTimKiemKeyPressed

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
            java.util.logging.Logger.getLogger(frmNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmNhanVien.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmNhanVien().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgrVaiTro;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel lblDetails;
    private javax.swing.JLabel lblList;
    private javax.swing.JLabel lblRole;
    private javax.swing.JPanel pnlBottom;
    private javax.swing.JPanel pnlButton;
    private javax.swing.JPanel pnlCenter;
    private javax.swing.JPanel pnlDetail;
    private javax.swing.JPanel pnlDetails;
    private javax.swing.JPanel pnlList;
    private javax.swing.JPanel pnlTop;
    private javax.swing.JRadioButton rdoNhanVien;
    private javax.swing.JRadioButton rdoQuanLy;
    private javax.swing.JTable tblGridView;
    private javax.swing.JTextField txtCMND;
    private javax.swing.JTextField txtHoTen;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSdt;
    private javax.swing.JTextField txtTimKiem;
    private javax.swing.JTextField txtXacNhanMK;
    // End of variables declaration//GEN-END:variables
}
