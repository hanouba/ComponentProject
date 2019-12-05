package com.hansen.componet.module.recommand;

import com.hansen.componet.module.BaseModel;

import java.util.ArrayList;

/**
 * @author HanN on 2019/12/5 13:24
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 产品实体  data里面的数据 第二层
 * @updateuser:
 * @updatedata: 2019/12/5 13:24
 * @updateremark:
 * @version: 2.1.67
 */
public class RecommandModel extends BaseModel {

    public ArrayList<RecommandBodyValue> list;
    public RecommandHeadValue head;
}
