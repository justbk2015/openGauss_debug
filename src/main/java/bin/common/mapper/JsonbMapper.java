/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.mapper;

import bin.common.vo.JsonbVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    @Update("create table if not exists t_jsonb(id int primary key, data jsonb)")
    void createTable();

    @Update("drop table if exists t_jsonb")
    void dropTable();

    @Select("select case when max(id) is null then 0 else max(id)+1 end as maxId  from t_jsonb")
    int selectMaxId();

    int insertOne(JsonbVo vo);
    JsonbVo selectJsonb(int id);
    List<JsonbVo> selectJsonbAll();
}
