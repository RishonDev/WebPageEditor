package wpe.core;

/**
 * @author Notification
 * @version 1.0.0
 * @date 22/01/23
 * @project IntelliJ IDEA
 **/
public class Notification {
    static String os= SoftwareInfo.getPlatform();

    public static void systemNotification(String message, String title){
        if (os.contains("Linux")) {
            ProcessBuilder builder = new ProcessBuilder(
                    "zenity",
                    "--notification",
                    "--title=" + title,
                    "--text=" + message);
            try {
                builder.inheritIO().start();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        } else if (os.contains("mac")) {
            ProcessBuilder builder = new ProcessBuilder(
                    "osascript", "-e",
                    "display notification \"" + message + "\""
                            + " with title \"" + title + "\"");
            try {
                builder.inheritIO().start();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        } else if (java.awt.SystemTray.isSupported()) {
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();

            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(java.awt.Toolkit.getDefaultToolkit().getImage(""), "Tray Demo");
            trayIcon.setImageAutoSize(true);
            try {
                tray.add(trayIcon);
            } catch (java.awt.AWTException e) {
                throw new RuntimeException(e);
            }

            trayIcon.displayMessage(title, message, java.awt.TrayIcon.MessageType.INFO);
        }
    }
}
