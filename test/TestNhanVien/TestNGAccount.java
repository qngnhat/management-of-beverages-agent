package TestNhanVien;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.*;


public class TestNGAccount{
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
		}
	}
	
        
	@Test
	public void ExportAccountsFromDatabase() {
		try {
			getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM NHANVIEN");
			ResultSet rs = ps.executeQuery();
			System.out.println("ID" +"\t" + "Username"+ "\t" + "Password" + "\t" + "Role");
			while(rs.next()) {
				String MaNV = rs.getString("MaNV");
				String TenNV = rs.getString("TenNV");
				String MatKhau = rs.getString("MatKhau");
				String VaiTro = rs.getString("VaiTro");
				System.out.println(MaNV + "\t" + TenNV + "\t" + MatKhau + "\t" + "\t" + VaiTro);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@AfterClass
    public void getClose() throws SQLException {
        con.close();
    }  

	
}
