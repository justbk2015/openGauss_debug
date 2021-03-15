/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.provider;

import org.apache.ibatis.jdbc.SQL;

import java.util.Locale;

/**
 * description:this for BatchProvider Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public interface BaseProvider {
    String getTableName();
    String getCreateColumns();

    default String createTable() {
        return createTableByFlag(getIgnoreExist());
    }

    default String createTableByFlag(boolean ignoreExist) {
        return String.format(Locale.ENGLISH,
                "CREATE TABLE %s%s %s",
                appendCreateIgnoreExist(ignoreExist),
                getTableName(),
                getCreateColumns()
        );
    }

    default String dropTable() {
        return dropTableByFlag(getIgnoreExist());
    }

    default String dropTableByFlag(boolean ignoreExist) {
        return String.format(Locale.ENGLISH,
                "DROP TABLE %s%s",
                appendDropIgnoreExist(ignoreExist),
                getTableName()
        );
    }

    default boolean getIgnoreExist() {
        return true;
    }

    default String appendCreateIgnoreExist(boolean ignoreExist) {
        return ignoreExist ? "IF NOT EXISTS " : "";
    }

    default String appendDropIgnoreExist(boolean ignoreExist) {
        return ignoreExist ? "IF EXISTS " : "";
    }
}
