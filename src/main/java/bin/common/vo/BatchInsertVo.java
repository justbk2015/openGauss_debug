/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.vo;

import java.util.Locale;

/**
 * description:this for BatchInsertVo Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public class BatchInsertVo {
    public Integer id;
    public String data;

    public String toInsertData() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("(");
        sb.append(String.format(Locale.ENGLISH, "%s::int", id));
        sb.append(",");
        sb.append(String.format(Locale.ENGLISH, "\'%s\'", data));
        sb.append(")");
        return sb.toString();
    }

    public static String toPrepareStmt() {
        return "(?, ?)";
    }
    public static String createColumns() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("(");
        sb.append("id int primary key,");
        sb.append("\n");
        sb.append("data VARCHAR(100)");
        sb.append(")");
        return sb.toString();
    }
}
