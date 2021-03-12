/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.vo;

import org.postgresql.core.BaseConnection;
import org.postgresql.jdbc.PgBlob;

import java.io.*;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Locale;

/**
 * description:this for BlobVo Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/11]
 * @since 2021/3/11
 */
public class BlobVo {
    public int id;
    public String descdata;
    public Blob data;
    public static class MyBlob implements Blob {
        public String fileName;
        public MyBlob(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public long length() throws SQLException {
            return 0;
        }

        @Override
        public byte[] getBytes(long pos, int length) throws SQLException {
            return new byte[0];
        }

        @Override
        public InputStream getBinaryStream() throws SQLException {
            try {
                return new FileInputStream(new File(this.fileName));
            } catch (FileNotFoundException e) {
                throw new SQLException(String.format(Locale.ENGLISH,"file:%s not found", fileName));
            }
        }

        @Override
        public long position(byte[] pattern, long start) throws SQLException {
            return 0;
        }

        @Override
        public long position(Blob pattern, long start) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long pos, byte[] bytes) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
            return 0;
        }

        @Override
        public OutputStream setBinaryStream(long pos) throws SQLException {
            return null;
        }

        @Override
        public void truncate(long len) throws SQLException {

        }

        @Override
        public void free() throws SQLException {

        }

        @Override
        public InputStream getBinaryStream(long pos, long length) throws SQLException {
            return null;
        }
    }
}
