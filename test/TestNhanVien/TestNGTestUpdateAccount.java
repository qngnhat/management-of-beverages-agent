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




public class TestNGTestUpdateAccount {
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
	public void UpdateAccountFromDataBase() {
		boolean check = false;
		Scanner sc = new Scanner(System.in);
		System.out.print("Nhập mã tài khoản: ");
        String a = sc.next();
        System.out.print("Nhập Tên: ");
        String b = sc.next();
        System.out.print("Nhập mật khẩu: ");
        String c = sc.next();
        System.out.print("Nhập Vai trò 0/1: ");
        String d = sc.next();
        
        try {
            getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE NHANVIEN SET TENNV = ?, MATKHAU = ?, VAITRO = ? WHERE MANV = ?");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setString(3, c);
            ps.setString(4, d);
            int rs = ps.executeUpdate();
            if (rs > 0) {
                check = true;
            }
            getClose();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	}
	
	@AfterClass
	public void getClose() throws SQLException {
		con.close();
	}
}
