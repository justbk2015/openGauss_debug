package common;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

/**
 * author: z00588921
 * date: 2020/9/14
 * descript:
 */
public class DriverInfo {
    public static enum LoggerLevel {
        OFF,
        TRACE,
        DEBUG
    }
    public static final int DEFAULT_PORT = 5432;
    public static final String DEFAULT_DATABASE = "postgres";
    public static final String DEFAULT_CONNECT_FORMAT = "jdbc:postgresql://%s:%s/%s";
    private String ip;
    private int port;
    private String database;
    private String user;
    private String password;
    private LoggerLevel logLevel = LoggerLevel.OFF;
    private Properties additionProp = new Properties();
    public DriverInfo(String ip, String user, String password) {
        this(ip, DEFAULT_PORT, DEFAULT_DATABASE, user, password);
    }
    public DriverInfo(String ip, int port, String database, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getUser() {
        return this.user;
    }

    public String getPassword() {
        return this.password;
    }

    public void setLoggerLevel(String level) {
        this.logLevel = LoggerLevel.valueOf(level.toUpperCase(Locale.ENGLISH));
    }

    public String getFormatConnectionInfo() {
        StringBuilder sb = new StringBuilder(128);
        sb.append(String.format(DEFAULT_CONNECT_FORMAT, this.ip, this.port, this.database));
        sb.append("?loggerLevel=" + logLevel.name());
        return sb.toString();
    }

    public Properties getProperties() {
        Properties properties = new Properties(this.additionProp);
        properties.setProperty("passwd", getPassword());
        properties.setProperty("user", getUser());
        return properties;
    }

    public void parseJdbcProperties(String key, Properties prop) {
        String prefix = key + ".jdbc.";
        Enumeration<?> names = prop.propertyNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement().toString();
            if (name.startsWith(prefix)) {
                this.additionProp.put(name.substring(prefix.length()), prop.getProperty(name));
            }
        }
    }
}
