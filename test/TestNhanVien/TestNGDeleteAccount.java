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

public class TestNGDeleteAccount {
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
    public void DeleteAccountsFromDataBase() {
        boolean check = false;
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập mã số nhân viên cần xóa(số): ");
        String a = sc.next();
        try {
            getConnection();
            PreparedStatement ps = con.prepareStatement("DELETE FROM NHANVIEN WHERE MANV = ?");
            ps.setString(1, a);
            int rs = ps.executeUpdate();
            getClose();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
	
	@AfterClass
    public void getClose() throws SQLException {
        con.close();
    }  
}
