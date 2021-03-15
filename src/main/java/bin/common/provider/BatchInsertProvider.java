/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.provider;

import bin.common.vo.BatchInsertVo;
import org.apache.ibatis.jdbc.SQL;

/**
 * description:this for BatchInsertProvider Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public class BatchInsertProvider implements BaseProvider {
    public String getTableName() {
        return "t_test_batch";
    }

    public String getCreateColumns() {
        return BatchInsertVo.createColumns();
    }

    public String add(BatchInsertVo insertVo) {
        return new SQL() {{
            INSERT_INTO(getTableName());
            VALUES("id", "#{id}");
            VALUES("data", "#{data}");
        }}.toString();
    }

    public String maxId() {
        return new SQL() {{
            SELECT("case when max(id) is null then 0 else max(id)+1 end as maxId");
            FROM(getTableName());
        }}.toString();
    }
}
