/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package common.testcase;

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description:this for BaseJdbcTestCase Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/16]
 * @since 2021/3/16
 */
public abstract class BaseJdbcTestCase {
    static {
        DriverInfoManager.registerDriver();
    }
    protected AtomicInteger autoId = null;
    protected int[] autoIdLock = new int[0];
    public abstract Connection getConn();
    public abstract String getTableName();
    public abstract String getTableColumns4Create();

    protected void createTable() throws SQLException {
        String sql = String.format(Locale.ENGLISH, "create table if not exists %s %s",
                getTableName(),
                getTableColumns4Create());
        executeSql(sql);
    }

    protected void dropTable() throws SQLException {
        String sql = String.format(Locale.ENGLISH, "drop table if exists %s",
                getTableName());
        executeSql(sql);
    }

    public void initAutoId() throws SQLException {
        synchronized (autoIdLock) {
            int maxId = executeGetMaxId();
            autoId = new AtomicInteger(maxId);
        }
    }

    public int getNextId() {
        return autoId.getAndIncrement();
    }

    protected int executeGetMaxId() throws SQLException {
        String sql = String.format(Locale.ENGLISH,
                "select case when max(id) is null then 0 else max(id)+1 end as maxId from %s",
                getTableName());
        List <Object> maxIds = executeAndGetList(sql, new SimpleParseRs());
        return new IntConvert().convert(maxIds.get(0));
    }

    public int count() throws SQLException {
        String sql = String.format(Locale.ENGLISH,
                "select count(*) from %s",
                getTableName());
        return new IntConvert().convert(executeAndGetOne(sql, new SimpleParseRs()));
    }
    public <T> T executeAndGetOne(String sql, ParseResultSet<T> parse) throws SQLException {
        return executeAndGetOne(getConn(), sql, parse);
    }

    public <T> T executeAndGetOne(Connection conn, String sql, ParseResultSet<T> parse) throws SQLException {
        return executeAndGetList(conn, sql, parse).get(0);
    }
    public <T> List<T> executeAndGetList(String sql, ParseResultSet<T> parse) throws SQLException {
        return executeAndGetList(getConn(), sql, parse);
    }

    public <T> List<T> executeAndGetList(Connection conn, String sql, ParseResultSet<T> parse) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                return parse.parseList(rs);
            }
        }
    }


    protected void executeSql(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    protected void executeSql(String sql) throws SQLException {
        executeSql(getConn(), sql);
    }

    public static interface ParseResultSet<T> {
        T parse(ResultSet rs) throws SQLException;
        default List<T> parseList(ResultSet rs) throws SQLException {
            List<T> results = new ArrayList<>(1);
            while (rs.next()) {
                results.add(parse(rs));
            }
            return results;
        }
    }

    public static class SimpleParseRs implements ParseResultSet<Object> {
        private Object parseResult = null;
        private boolean isParse = false;
        @Override
        public Object parse(ResultSet rs) throws SQLException {
            if (isParse) {
                return parseResult;
            }
            isParse = true;
            parseResult = rs.getObject(1);
            return parseResult;
        }
    }

    public static class IntConvert {
        public Integer convert(Object obj) throws SQLException {
            if (obj instanceof Integer) {
                return (Integer) obj;
            }
            if (obj instanceof Long) {
                return ((Long) obj).intValue();
            }
            throw new SQLException("type not match!");
        }
    }
}
