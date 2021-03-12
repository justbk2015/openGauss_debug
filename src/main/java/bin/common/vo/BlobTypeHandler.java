/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.vo;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description:this for BlobTypeHandler Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/12]
 * @since 2021/3/12
 */
public class BlobTypeHandler extends BaseTypeHandler {
    public static AtomicInteger blobFileId = new AtomicInteger(0);
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
        if (o instanceof BlobVo.MyBlob) {
            BlobVo.MyBlob myBlob = (BlobVo.MyBlob) o;
            try (InputStream in = myBlob.getBinaryStream()) {
                preparedStatement.setBlob(i, in);
            } catch (IOException e) {
                throw new SQLException("use stream with error!");
            }
        } else {
            throw new SQLException("not support convert!");
        }
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return blobConvert(resultSet.getBlob(s));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return blobConvert(resultSet.getBlob(i));
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return blobConvert(callableStatement.getBlob(i));
    }

    private BlobVo.MyBlob blobConvert(Blob blob) {
        String fileName = null;
        try (InputStream inputStream = blob.getBinaryStream()) {
            String filePath = String.format(Locale.ENGLISH,"blob-%s.properties", blobFileId.getAndIncrement());
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
            try (FileOutputStream outputStream = new FileOutputStream(file)) {
                byte[] readBuf = new byte[1024];
                while (true) {
                    int readSize = inputStream.read(readBuf);
                    if (readSize <= 0) {
                        break;
                    }
                    outputStream.write(readBuf, 0, readSize);
                }
                outputStream.flush();
            }
            fileName = file.getAbsolutePath();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName == null ? null : new BlobVo.MyBlob(fileName);
    }
}
