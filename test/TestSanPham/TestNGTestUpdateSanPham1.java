package TestSanPham;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;




public class TestNGTestUpdateSanPham {
	
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
		System.out.print("Nhập mã sản phẩm: ");
        String a = sc.nextLine();
		System.out.print("Nhập tên sản phẩm: ");
        String b = sc.nextLine();
        System.out.print("Nhập mã loại: ");
        int c = sc.nextInt();
        System.out.print("Nhập mã ncc: ");
        int d = sc.nextInt();
        System.out.print("Nhập số lượng: ");
        String e = sc.next();
        System.out.print("Nhập giá bán: ");
        String f = sc.next();
        
        try {
            getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE NUOCGIAIKHAT SET TenSP = ?, MaLoai = ?, MaNCC = ?, SoLuong = ?, GiaBan = ? WHERE MaSP = ?");
            ps.setString(1, a);
            ps.setString(2, b);
            ps.setInt(3, c);
            ps.setInt(4, d);
            ps.setString(5, e);
            ps.setString(6, f);
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
