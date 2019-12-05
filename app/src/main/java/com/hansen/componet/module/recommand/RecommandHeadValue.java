package com.hansen.componet.module.recommand;

import com.hansen.componet.module.BaseModel;

import java.util.ArrayList;

/**
 * @author HanN on 2019/12/5 13:25
 * @email: 1356548475@qq.com
 * @project componet
 * @description:
 * @updateuser:
 * @updatedata: 2019/12/5 13:25
 * @updateremark:
 * @version: 2.1.67
 */
public class RecommandHeadValue extends BaseModel {

    public ArrayList<String> ads;
    public ArrayList<String> middle;
    public ArrayList<RecommandFooterValue> footer;
}
