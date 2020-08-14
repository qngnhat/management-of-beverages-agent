package TestNhanVien;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestNGAddAccount {
	protected Connection con;
	protected Properties p;
	String username = "sa";
	String password = "songlong";
	String url = "jdbc:sqlserver://localhost:1433;databaseName=QLNUOCGIAIKHAT";
	String drive = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	@BeforeClass
	public void getConnection() {
		try {
			Class.forName(drive);
			con = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Lỗi kết nối");
			e.printStackTrace();
		}
	}
	
	@Test
    public void AddAccountsFromDataBase() {
        boolean check = false;
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập tên nhân viên: ");
        String a = sc.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String b = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String c = sc.nextLine();
        System.out.print("Nhập số CMND: ");
        String d = sc.nextLine();
        System.out.print("Nhập vai trò 0/1: ");
        String e = sc.next();
        try {
            getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT NHANVIEN (TENNV, MATKHAU, SDT, CMND, VAITRO) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.setString(4, d);
            ps.setString(5, e);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                check = true;
            }
            getClose();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
	
	@AfterClass
    public void getClose() throws SQLException {
        con.close();
    } 
}
