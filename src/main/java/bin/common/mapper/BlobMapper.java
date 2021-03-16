/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.mapper;

import bin.common.vo.BlobVo;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Blob;
import java.util.List;

/**
 * description:this for BlobMapper interface
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/11]
 * @since 2021/3/11
 */
public interface BlobMapper {
    @Select("select case when max(id) is null then 0 else max(id)+1 end as maxId  from t_blob")
    int selectMaxId();

    @Update("create table if not exists t_blob(id int primary key, descdata varchar, data blob)")
    void createTable();

    @Update("drop table if exists t_blob")
    void dropTable();

    int insertBlobOne(BlobVo blobVo);
    BlobVo selectBlobById(int id);
    List<BlobVo> selectBlobAll();
}
