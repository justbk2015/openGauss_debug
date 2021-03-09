package common;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * author: z00588921
 * date: 2020/9/14
 * descript:
 */
public class DriverInfoManager {
    public static final String PROP_FILE_NAME = "database.properties";
    public static final String PROP_ALL_KEY = "all_keys";
    public static final String PROP_ACTIVITY = "activity_key";
    private Map<String, DriverInfo> infos = new HashMap<>();

    public static final DriverInfoManager instance = new DriverInfoManager();
    public static final String DEFAULT_DRIVER = "org.postgresql.Driver";

    private String activityKey = "db";
    private DriverInfoManager() {
        Properties prop = loadProperties();
        List<String> keys = Arrays.stream(prop.getProperty(PROP_ALL_KEY).split(","))
                .filter(a -> !"".equals(a))
                .collect(Collectors.toList());
        for (String key : keys) {
            DriverInfo driverInfo =
                    new DriverInfo(prop.getProperty(key + ".jdbc.ip"),
                            Integer.parseInt(prop.getProperty(key + ".jdbc.port")),
                            prop.getProperty(key + ".jdbc.database"),
                            prop.getProperty(key + ".jdbc.user"),
                            prop.getProperty(key + ".jdbc.password"));
            driverInfo.parseJdbcProperties(key, prop);
            driverInfo.setLoggerLevel(prop.getProperty(key + ".jdbc.loggerLevel", "OFF"));
            this.infos.put(key, driverInfo);
        }
        activityKey = prop.getProperty(PROP_ACTIVITY);
    }

    public String getActivity() {
        return activityKey;
    }

    public static DriverInfo getInfo(String key) {
        return instance.infos.get(key);
    }

    public static DriverInfo getInfo() {
        return instance.infos.get(instance.getActivity());
    }

    public static boolean registerDriver() {
        return registerDriver(DEFAULT_DRIVER);
    }

    public static boolean registerDriver(String driverName) {
        try {
            Class.forName(driverName);
            return true;
        } catch (ClassNotFoundException e) {
            Logger.info("register driver failed!" + e.toString());
        }
        return false;
    }

    public static Connection getConnection(DriverInfo info) throws SQLException {
        return DriverManager.getConnection(info.getFormatConnectionInfo(),
                info.getUser(),
                info.getPassword());
    }

    public static Properties loadProperties() {
        Properties prop = new Properties();

        try (InputStream is = DriverInfoManager.class.getClassLoader().getResourceAsStream(PROP_FILE_NAME)) {
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
