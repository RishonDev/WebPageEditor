package wpe.core;

@SuppressWarnings("ALL")
public class SoftwareInfo {
    private static final String buildMode = "Alpha";
    private static final String About = "A Text Editor Designed in java";
    private static final String versionNumber = "1.0";

    public static String getVersionNumber() {
        return versionNumber;
    }

    public static String getVersion() {
        return versionNumber + "-" + buildMode;
    }

    public static String getAbout() {
        return About;
    }

    public static String getBuildMode() {
        return buildMode;
    }

    public static String getPlatform() {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows"))
            return "Windows";
        else if (osName.equals("Mac OS X"))
            return System.getProperty("os.name");
        else return "Linux";
    }

    public static String getOSVersion() {
        return System.getProperty("os.version");
    }

    public static String getHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String getAppDataDirectory() {
        switch (getPlatform()) {
            case "Mac OS X", "Linux" -> {
                return getHomeDirectory() + "/.WaveTextEditor";
            }
            case "Windows" -> {
                return getHomeDirectory() + "\\.WaveTextEditor";
            }
        }
        return null;
    }


}
