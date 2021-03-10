/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021. All rights reserved.
 */

package bin.common.vo;

import com.huawei.shade.com.alibaba.fastjson.JSON;
import org.postgresql.util.PGobject;

import java.sql.SQLException;

/**
 * description:this for JsonbTable Class
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2021.
 *
 * @author Administrator
 * @version [openGauss_debug 0.0.1 2021/3/9]
 * @since 2021/3/9
 */
public class JsonbVo {
    public Integer id;
    public PGobject data;

    @Override
    public String toString() {
        return "JsonbVo{" +
                "id=" + id +
                ", jsonStr='" + data.getValue() + '\'' +
                '}';
    }

    public static JsonbVo parse(int id, JSON json) {
        JsonbVo newVo = new JsonbVo();
        newVo.id = id;
        String result = json.toJSONString();
        PGobject pGobject = new PGobject();
        pGobject.setType("jsonb");
        try {
            pGobject.setValue(result);
        } catch (SQLException throwables) {
            return null;
        }
        newVo.data = pGobject;
        return newVo;
    }
}
