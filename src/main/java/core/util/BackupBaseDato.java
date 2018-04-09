package core.util;

import core.controller.Ayuda;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.CodeSource;

/**
 * Created by Ivans on 8/13/2017.
 */
public class BackupBaseDato {

    private String yourDBName;
    private String yourUserName;
    private String yourUserPassword;
    private Class<?> cls;
    private String savePath;
    private String folderPath;

    public BackupBaseDato(String yourDBName, String yourUserName, String yourUserPassword, Class<?> cls) {
        this.yourDBName = yourDBName;
        this.yourUserName = yourUserName;
        this.yourUserPassword = yourUserPassword;
        this.cls = cls;
        try {
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            folderPath = jarDir + "\\backup";
        /*NOTE: Creating Path Constraints for backup saving*/
        /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
            savePath = jarDir + "\\backup\\" + "facturacion.sql";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BackupBaseDato backupBaseDato = new BackupBaseDato("facturacion", "root", "", Ayuda.class);
        // backupBaseDato.backupdbMsyql();
        // backupBaseDato.restoreDB("facturacion", "root", "", "facturacion.sql");
    }

    public void backupdbMsyql() {
        try {

        /*NOTE: Getting path to the Jar file being executed*/
        /*NOTE: YourImplementingClass-> replace with the class executing the code*/


        /*NOTE: Creating Database Constraints*/
            String dbName = yourDBName;
            String dbUser = yourUserName;
            String dbPass = yourUserPassword;

        /*NOTE: Creating Folder if it does not exist*/
            File f1 = new File(folderPath);
            f1.mkdir();

            // String savePath = "facturacion.sql";

        /*NOTE: Used to create a cmd command*/
            String executeCmd = "C:\\xampp\\mysql\\bin\\mysqldump -u" + dbUser + " --database " + dbName + " -r " + savePath;

        /*NOTE: Executing the command here*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

        /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0) {
                System.out.println("Backup Complete");
            } else {
                System.out.println("Backup Failure");
            }

        } catch (IOException | InterruptedException ex) {
            System.out.println("Error at Backuprestore" + ex.getMessage());
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public boolean restoreDB(String dbName, String dbUserName, String dbPassword, String source) {
        /* CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
        File jarFile = null;
        String restorePath = "";
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();
            restorePath = jarDir + "\\backup" + "\\" + source;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } */

        String[] executeCmd = new String[]{"C:\\xampp\\mysql\\bin\\mysql", "--user=" + dbUserName, "--password=" + dbPassword, dbName, "-e", " source " + source};
        Process runtimeProcess;
        try {
            runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();
            if (processComplete == 0) {
                System.out.println("Backup restored successfully");
                return true;
            } else {
                System.out.println("Could not restore the backup");
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
