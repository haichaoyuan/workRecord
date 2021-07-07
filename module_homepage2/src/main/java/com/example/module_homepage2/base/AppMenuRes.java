package com.example.module_homepage2.base;

/**
 * Des: 首页顶部菜单
 * Author: Luke
 * Data: 17/2/16
 */
public class AppMenuRes extends Entity {
    public static String MENU_TYPE_H5 = "0";
    public static String MENU_TYPE_NATIVE = "1";
    public static String MENU_TYPE_SCHEME = "2";


    public final static int TARGET_MYSTOCK = 1;// 自选股
    public final static int TARGET_STOCKINDEX = 2;//大盘指数
    public final static int TARGET_CHANGELIST = 3;// 涨跌排名
    public final static int TARGET_HOTBLOCK = 4;// 热门板块
    public final static int TARGET_IPO = 5;// 新股ipo
    public final static int TARGET_WINNERLIST = 6;// 龙虎榜
    public final static int TARGET_OA = 7;// 开户
    public final static int TARGET_TRADE = 8;// 交易
    public final static int TARGET_SIMULATE = 9;// 模拟组合
    public final static int TARGET_ZTINFO = 10;// 涨停财经
    public final static int TARGET_SIMULATE_CONTEST = 11; //炒股大赛
    public final static int TARGET_HKSTOCK = 12; //港股通
    public final static int TARGET_MORE = 13; // 更多
    public final static int TARGET_SD_TG = 14; // 思迪投顾
    public final static int TARGET_LOCK_ACT = 15; // 激活锁定
    public final static int TARGET_OPEN_SCIENCE_TECHNOLOGY = 16; // 科创板开通
    public final static int TARGET_OPEN_SECOND_BOARD = 17; // 创业板开通

    private String menuImageUrl;//图片地址
    private String menuName;//菜单名字
    private String menuType;//菜单类型
    private String targetUrl;//跳转路径
    private String ids;

    private boolean isSimulated;//是否模拟数据

    public String getMenuImageUrl() {
        return menuImageUrl;
    }

    public void setMenuImageUrl(String menuImageUrl) {
        this.menuImageUrl = menuImageUrl;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public boolean isSimulated() {
        return isSimulated;
    }

    public void setSimulated(boolean simulated) {
        isSimulated = simulated;
    }

    public String getId() {
        return ids;
    }

    public void setId(String id) {
        this.ids = id;
    }
}
