/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

/**
 *
 * @author qng_nhat
 */
public class GlobalData {
    
    private static boolean loggedRole;
    private static int loggedMaNhanVien;
    private static String loggedUser;
    
    public static Boolean getLoggedRole(){
        return loggedRole;
    }
    
    public static void setLoggedRole(boolean loggedRole){
        GlobalData.loggedRole = loggedRole;
    }

    public static int getLoggedMaNhanVien() {
        return loggedMaNhanVien;
    }

    public static void setLoggedMaNhanVien(int loggedMaNhanVien) {
        GlobalData.loggedMaNhanVien = loggedMaNhanVien;
    }    

    public static String getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(String loggedUser) {
        GlobalData.loggedUser = loggedUser;
    }
    
    
}
