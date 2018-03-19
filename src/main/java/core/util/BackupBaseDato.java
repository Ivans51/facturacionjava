package core.util;

import core.controller.Ayuda;

import java.awt.*;
import java.io.*;
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

    public BackupBaseDato(String yourDBName, String yourUserName, String yourUserPassword, Class<?> cls) {
        this.yourDBName = yourDBName;
        this.yourUserName = yourUserName;
        this.yourUserPassword = yourUserPassword;
        this.cls = cls;
    }

    public static void main(String[] args) {
        BackupBaseDato backupBaseDato = new BackupBaseDato("facturacion", "root", "", Ayuda.class);
        // backupBaseDato.backupdbMsyql();
        backupBaseDato.restoredbfromsql("facturacion.sql");
    }

    public void backupdbMsyql() {
        try {

        /*NOTE: Getting path to the Jar file being executed*/
        /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();


        /*NOTE: Creating Database Constraints*/
            String dbName = yourDBName;
            String dbUser = yourUserName;
            String dbPass = yourUserPassword;

        /*NOTE: Creating Path Constraints for folder saving*/
        /*NOTE: Here the backup folder is created for saving inside it*/
            String folderPath = jarDir + "\\backup";

        /*NOTE: Creating Folder if it does not exist*/
            File f1 = new File(folderPath);
            f1.mkdir();

        /*NOTE: Creating Path Constraints for backup saving*/
        /*NOTE: Here the backup is saved in a folder called backup with the name backup.sql*/
            savePath = "\"" + jarDir + "\\backup\\" + "facturacion.sql\"";
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

        } catch (URISyntaxException | IOException | InterruptedException ex) {
            System.out.println("Error at Backuprestore" + ex.getMessage());
        }
    }

    public String getSavePath() {
        return savePath;
    }

    public void restoredbfromsql(String s) {
        try {
            /*NOTE: String s is the mysql file name including the .sql in its name*/
            /*NOTE: Getting path to the Jar file being executed*/
            /*NOTE: YourImplementingClass-> replace with the class executing the code*/
            CodeSource codeSource = cls.getProtectionDomain().getCodeSource();
            File jarFile = new File(codeSource.getLocation().toURI().getPath());
            String jarDir = jarFile.getParentFile().getPath();

            /*NOTE: Creating Database Constraints*/
            String dbName = yourDBName;
            String dbUser = yourUserName;
            String dbPass = yourUserPassword;

            /*NOTE: Creating Path Constraints for restoring*/
            String restorePath = jarDir + "\\backup" + "\\" + s;

            /*NOTE: Used to create a cmd command*/
            /*NOTE: Do not create a single large string, this will cause buffer locking, use string array*/
            // String[] executeCmd = new String[]{"C:\\xampp\\mysql\\bin\\mysql", dbName, "-u " + dbUser, "-e", " source " + restorePath};
            String[] executeCmd = new String[]{"C:\\xampp\\mysql\\bin\\mysql", "-u "+dbUser, "-A", "-D " + dbName , "-e", "\"SOURCE "+restorePath+"\""};

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            Process runtimeProcess = Runtime.getRuntime().exec(executeCmd);
            int processComplete = runtimeProcess.waitFor();

            /*NOTE: processComplete=0 if correctly executed, will contain other values if not*/
            if (processComplete == 0)
                System.out.println("Successfully restored from SQL : " + s);
            else
                System.out.println("Error at restoring");
        } catch (URISyntaxException | IOException | InterruptedException | HeadlessException ex) {
            System.out.println("Error at restoredbfromsql" + ex.getMessage());
        }
    }
}
