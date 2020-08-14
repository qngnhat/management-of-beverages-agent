
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.awt.Image;
import javax.swing.ImageIcon;
import model.NhanVien;

/**
 *
 * @author qng_nhat
 */
public class ShareHelper {
    //user's info after login
    public static NhanVien USER = null;
    
    //remove user's info after logout
    public static void logoff(){
        ShareHelper.USER = null;
    }
    
    //check login
    public static boolean authenticated(){
        return ShareHelper.USER == null;
    }
    
}