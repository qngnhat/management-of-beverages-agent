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

public class TestNGAddSanPham {
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
        System.out.print("Nhập tên sản phẩm: ");
        String a = sc.nextLine();
        System.out.print("Nhập mã loại: ");
        int b = sc.nextInt();
        System.out.print("Nhập mã ncc: ");
        int c = sc.nextInt();
        System.out.print("Nhập số lượng: ");
        String d = sc.next();
        System.out.print("Nhập giá bán: ");
        String e = sc.next();
        try {
            getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT NUOCGIAIKHAT (TenSP, MaLoai, MaNCC, SoLuong, GiaBan) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, a);
            ps.setInt(2, b);
            ps.setInt(3, c);
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
