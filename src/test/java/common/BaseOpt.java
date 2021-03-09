/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */

package common;

import java.sql.DriverManager;

/**
 * Title: the BaseOpt class
 * <p>
 * Description:
 * <p>
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019.
 *
 * @author z00588921
 * @version [DataStudio 1.0.0, 2021/1/28]
 * @since 2021/1/28
 */
public class BaseOpt {
    static {
        DriverInfoManager.registerDriver();
    }
}
