/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.mapper;

import bin.common.provider.BatchInsertProvider;
import bin.common.vo.BatchInsertVo;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

/**
 * description:this for BatchMapper Interface
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/15]
 * @since 2021/3/15
 */
public interface BatchInsertMapper {
    @UpdateProvider(type = BatchInsertProvider.class, method = "createTable")
    void createTable();

    @UpdateProvider(type = BatchInsertProvider.class, method = "dropTable")
    void dropTable();

    @InsertProvider(type = BatchInsertProvider.class, method = "add")
    int add(BatchInsertVo insertVo);

    @SelectProvider(type = BatchInsertProvider.class, method = "maxId")
    int maxId();

}
