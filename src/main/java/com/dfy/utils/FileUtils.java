package com.dfy.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created in 15:14 2019/9/26
 * Description: []
 * Company: [尚德机构]
 *
 * @author [dufeiyang]
 */
public class FileUtils {
    private static String savefile = "/root/data/text.txt";
    public static void saveAsFile(String content) {
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(savefile,true);
            fwriter.write(System.getProperty("line.separator"));
            fwriter.write(content);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
