package TestSanPham;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;




public class TestNGSanPham {
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
	public void ExportAccountsFromDatabase() {
		try {
			getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT * FROM NUOCGIAIKHAT");
			ResultSet rs = ps.executeQuery();
			System.out.println("ID" +"\t" + "Tên"+ "\t" + "\t" + "\t" + "\t" + "Loại" + "\t" + "Nhà Cung cấp" + "\t" + "Số lượng" + "\t" + "Giá Bán" );
			while(rs.next()) {
				String maSP = rs.getString("MaSP");
				String tenSP = rs.getString("TenSP");
				String loai = rs.getString("MaLoai");
				String nhaCC = rs.getString("MaNCC");
				String soLuong = rs.getString("SoLuong");
				String giaBan = rs.getString("GiaBan");
				System.out.println(maSP + "\t" + tenSP + "\t" + "\t" + loai + "\t" + nhaCC + "\t" + "\t" + soLuong + "\t" + "\t" + giaBan);
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
