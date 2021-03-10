/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.mapper;

import bin.common.vo.JsonbVo;

import java.util.List;
import java.util.Map;

/**
 * description:this for JsonbMapper Interface
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/9]
 * @since 2021/3/9
 */
public interface JsonbMapper {
    void createTable();
    void dropTable();
    int insertOne(JsonbVo vo);
    int selectMaxId();
    JsonbVo selectJsonb(int id);
    List<JsonbVo> selectJsonbAll();
}
