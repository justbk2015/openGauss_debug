/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

import bin.common.DriverInfo;
import bin.common.DriverInfoManager;
import bin.common.vo.BatchInsertVo;
import common.testcase.BaseJdbcTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;

/**
 * description:this for JdbcBatchTest Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public class JdbcBatchTest extends BaseJdbcTestCase {
    private static final String tableName = "t_batch_insert";
    DriverInfo info = DriverInfoManager.getInfo();
    Connection conn = null;
    @Before
    public void setUp() throws SQLException {
        conn = DriverInfoManager.getConnection(info);
        dropTable();
        createTable();
        initAutoId();
    }

    @After
    public void teardown() throws SQLException {
        dropTable();
        conn.close();
        conn = null;
    }

    @Test
    public void testInsert() throws SQLException {
        int insertNum = 10;
        for (int i = 0; i < insertNum; i++) {
            BatchInsertVo vo = new BatchInsertVo();
            vo.id = getNextId();
            vo.data = "this is test " + vo.id;
            executeSql(insertFormat(vo.toInsertData()));
        }
        assertEquals(insertNum, count());
    }

    @Test
    public void testManyBatchInsert() throws SQLException {
        int n = 10;
        int insertNum = 1000;
        AtomicInteger taskNumber = new AtomicInteger(n);
        for (int i = 0; i < n ; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        testBatchInsert(insertNum);
                    } catch (SQLException throwables) {
                         System.out.println("insert with error!");
                    } finally {
                        taskNumber.getAndDecrement();
                    }
                }
            }).start();
        }
        while (taskNumber.get() != 0) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        assertEquals(n * insertNum, count());

    }
//    @Test
    public void testBatchInsert(int insertNum) throws SQLException {
        try (Connection conn = DriverInfoManager.getConnection(info)) {
            try (PreparedStatement ps = conn.prepareStatement(insertFormat(BatchInsertVo.toPrepareStmt()))) {
                for (int i = 0; i < insertNum; i++) {
                    int id = getNextId();
                    ps.setInt(1, id);
                    ps.setString(2, "this is test = " + id);
                    ps.addBatch();
                }
                ps.executeBatch();
                ps.clearBatch();
            }
        }
//        assertEquals(insertNum, count());
    }

    private String insertFormat(String insertData) {
        return String.format(Locale.ENGLISH, "insert into %s values %s",
                getTableName(),
                insertData);
    }

    @Override
    public Connection getConn() {
        return conn;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public String getTableColumns4Create() {
        return BatchInsertVo.createColumns();
    }

    public static class BatchInsertVoParse implements ParseResultSet<BatchInsertVo> {
        @Override
        public BatchInsertVo parse(ResultSet rs) throws SQLException {
            BatchInsertVo vo = new BatchInsertVo();
            vo.id = rs.getInt(1);
            vo.data = rs.getString(2);
            return vo;
        }
    }
}
