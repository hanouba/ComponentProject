package com.hansen.componet.module.recommand;

import com.hansen.componet.module.BaseModel;
import com.hansen.hansensdk.module.monitor.Monitor;
import com.hansen.hansensdk.module.monitor.emevent.EMEvent;

import java.util.ArrayList;

/**
 * @author HanN on 2019/12/5 13:21
 * @email: 1356548475@qq.com
 * @project componet
 * @description: 搜索实体
 * @updateuser:
 * @updatedata: 2019/12/5 13:21
 * @updateremark:
 * @version: 2.1.67
 */
public class RecommandBodyValue extends BaseModel {
    public int type;
    public String logo;
    public String title;
    public String info;
    public String price;
    public String text;
    public String site;
    public String from;
    public String zan;
    public ArrayList<String> url;

    //视频专用
    public String thumb;
    public String resource;
    public String resourceID;
    public String adid;
    public ArrayList<Monitor> startMonitor;
    public ArrayList<Monitor> middleMonitor;
    public ArrayList<Monitor> endMonitor;
    public String clickUrl;
    public ArrayList<Monitor> clickMonitor;
    public EMEvent event;
}
