package com.example.module_homepage2.base


/**
 * 股票市场
 */
enum class Exch {
    UNKNOWN,

    //深证
    SZSE,

    //上证
    SSE,

    //港股
    HKEX,

    //板块
    BLOCK,

    //上证期权 (目前没有用到 )
    SSEQ,

    //股转（新三板）
    NEEQ,

    //沪深，只请求用
    SZ_SH
}

/**
 * 证券主类别
 */
enum class SecType {

    //未知
    UNKNOWN,

    //股票
    STOCK,

    //指数
    INDEX,

    //基金
    FUND,

    //期货
    FUTURE,

    //期权
    OPTION,

    //权证
    WARRANT_,

    //债券
    BOND,

    //信托
    TRUST,

    //集合资产管理计划
    CAMP,

    //板块
    BLOCK,

    //非交易类别
    NON_TRD,

    //债券质押式回购(国债逆回购)
    BOND_PLEDGE_STYLE_REPOS

}

/**
 * 证券子类别
 */
enum class SecSubType {
    //未知
    UNKONW,

    // 国债
    NATIONAL_BOND,

    //无息国债 (sh)
    INTEREST_FREE_NATIONAL_BOND,

    // 国债分销 (sh)
    NATIONAL_BOND_DISTRIBUTED,

    // 可转债
    CONVERTIBLE,

    //分离式可转债
    SEPRARATE_CONVERTIBLE,

    //    公司债
    COMPANY_BOND,

    //  企业债
    ENTERPRISE_BOND,

    //私募债 (sz)
    PRIVATE_BOND,

    // 可交换私募债(sz)
    EXCHANGE_PRIVATE_BOND,

    // 质押式回购
    PLEDGE_STYLE_REPO,

    // 买断式债券回购 (sh)
    OUTRIGHT_BOND_REPO,

    // 证券公司短期债(sh)
    SECURITY_COMPANY_SHORT_BOND,

    //可交换公司债 (sh)
    EXCHANGEABLE_CORPORATE_BOND,

    // 其他债券
    OTHER_BOND,

    //// 封闭式基金
    CLOSE_END_FUND,

    // 开放式基金
    OPEN_END_FUND,

    // 分级子基金 (sz)
    LEVEL_SUB_FUND,

    // (SH) 交易所交易基金
    EXCHANGE_TRADE_FUND,

    // SZ 仅申赎基金
    ONLY_PURCHASE_SALE_FUND,

    // SH
    OTHER_FUND,

    // 权证
    WARRANT,

    // (SH )//企业发行权证
    ENTERPRISE_WARRANT,

    // (SH)
    COVERED_WARRANT,

    // 个股期货 (sh)
    STOCK_FUTURE,

    // 个股期权 (sh)
    STOCK_OPTION,

    // sh ETF期权
    ETF_OPTION,

    // 主板A股
    CNY_STOCK,

    // 主板B股
    USD_STOCK,
    INTERNATIONAL_STOCK,

    //  (sz)中小板股票
    SME_BOARD,

    // 创业板股票
    GEM_BOARD,

    // 其他股票
    OTHER_STOCK,

    // 本市场股票 ETF
    MARKET_STOCK_ETF,

    // 跨市场股票 ETF
    CROSS_MARKET_STOCK_ETF,

    // 跨境 ETF
    CROSS_BOARD_ETF,

    //  本市场实物债权 ETF
    MARKET_REAL_BOND_ETF,

    // 现金债券
    CASH_BOND_ETF,

    // 黄金ETF
    GOLD_ETF,

    //  货币ETF
    CURRENCY_ETF,

    // 杠杆ETF
    LEVERAGED_ETF,

    // 商品期货
    COMMODITY_FUTURE_ETF,

    // 标准LOF
    STANDARD_LOF,

    // 优先股
    PREFERRED_STOCK,

    // 资产支持证券
    ABS,

    // 证券公司次级债
    SUBORDINATED_DEBT_OF_SECURITIES_COMPANIES,

    // 公司债分销 (sh)
    COMPANY_BOND_DISTRIBUTED,

    // 跨境/跨市场基金
    CROSS_MARKET_FUND,

    // 金融机构发行证券
    FINANCIAL_INSTITUTIONS_ISSUE_BOND,

    // 债券期货
    BOND_FUTURE,

    // 其他期货
    OTHER_FUTURE,

    // 集合资产管理计划
    COLLECTIVE_ASSET_MANAGEMENT_PLAN,

    // 国债预发行
    NATIONAL_BOND_PRE_RELEASE,

    // LOF_基金
    LOF_FUND,

    // 公开发行优先股
    PUBLIC_OFFER_PREFERRED_STOCK,

    // 非公开发行优先股
    NON_PUBLIC_OFFER_PREFERRED_STOCK,

    // 报价回购
    QUOTE_REPURCHASE,

    // 港股 债券
    HK_BOND,

    // 港股一揽子权证
    BWRT,

    // 港股 股本
    EQTY,

    // 港股 信托
    TRST,

    // 上网证券发行申购
    DETAIL_TYPE_IN,

    // 老股东证券发行申购
    DETAIL_TYPE_IS,

    // 行业板块
    BLOCK_INDUSTRY,

    // 概念板块
    BLOCK_CONCEPT,

    // 地域板块
    BLOCK_REGION,

    // 行业板块 证监会
    INDUSTRY_PLATE_CSRC,

    // 指数板块
    INDEX_PLATE,

    // 详细类别中的非交易类别
    NON_TRD_DETAIL_TAYPE,

    //招商龙头板块
    CMB_LEADING_PLATE,

    //基础层
    BASIC_LEVEL,

    //创新层
    INNOVATION_LEVEL,

    //两网及退市
    TWONETS_DELISTED,

    //科创板股票
    TECHNOLOGY_INNOVATION_BOARD_STOCK
}



/**
 * 币种 HKD-港币, CNY-人民币, USD-美元
 */
enum class Currency {
    UNKNOWN, HKD, CNY, USD
}

/**
 * 买卖方向
 */
enum class Direction {
    UNKNOWN, BUY, SELL
}

/**
 * 证券状态
 */
enum class SecTag {
    //未知
    UNKNOWN_SECURITY_STATUS,

    //停牌
    SUSPENSION,

    //除权 SZ&SH
    XR,

    //除息 SZ&SH
    DR,
    ST,

    //*ST
    STPLUS,

    //上市首日.SZ&SH
    LISTED_ON_THE_FIRST_DAY,

    //公司再融资
    COMPANY_REFINANCING,

    //恢复上市首日
    RESUME_ON_THE_FIRST_DAY_OF_LISTING,

    //网络投票
    NETWORK_VOTING,

    //退市整理期,退市整理产品
    DELISTING_FINISHING_PERIOD,

    //增发股份上市
    ADDITIONAL_SHARES_LISTED,

    //合约调整
    CONTRACT_ADJUSTMENT,

    //暂停上市后协议转让,退市转让产品
    SUSPENSION_OF_POST_LISTING_AGREEMENT_TRANSFER,

    //国内主板正常交易产品
    DOMESTIC_MOTHERBOARD_NORMAL_TRADING_PRODUCTS,

    //股票风险警示产品
    STOCK_RISK_WARNING_PRODUCTS,

    //优先股产品
    PREFERRED_STOCK_PRODUCTS,

    //债券投资者适当性要求类
    BOND_INVESTOR_SUITABILITY_REQUIREMENTS,

    //非停牌
    NON_SUSPENSION,

    //参与市场波动调节机制
    PARTICIPATE_IN_MARKET_REGULATION_MECHANISMS,

    //参与收市竞价交易时段
    PARTICIPATE_IN_THE_CLOSING_AUCTION_SESSION,

    //不参与
    NOT_PARTICIPATE,

    //退市
    DELIST_STATUS,

    //可开仓
    CAN_OPEN_POSITION,

    //限制卖出开仓和买入开仓
    RESTRICTIONS_ON_SHORT_LONG,

    //未临近到期日
    NOT_NEAR_EXPIRATION_DATE,

    //临近到期日不足5个交易日
    NEAR_EXPIRATION_DATE,

    //近期未做调整
    NOT_ADJUSTED_NEAR_TERM,

    //最近5个交易日内合约发生过调整
    RECENTLY_ADJUSTED,

    //存续的合约
    SURVIVING_CONTRACT,

    //正常转让
    NORMAL_EXCHANGE,

    //停牌，不接受转让申报
    SUSPENSION_EXCHANGE,

    //停牌，接受转让申报
    SUSPENSION_NON_EXCHANGE,

    //未盈利(科创板)
    UNPROFITABLE,

    //表决权差异安排(科创板)
    VOTING_DIFFERENCE_ARRANGEMENT,

    //中国存托凭证 CDR
    CHINESE_DEPOSITORY_RECEIPT,

    //全球存托凭证 GDR
    GLOBAL_DEPOSITORY_RECEIPT,

    //注册制
    REGISTRATION,

    //协议控制架构
    VIE
}

/**
 * 推送数据定义
 */
enum class DataLevel {
    //普通行情
    QUOTE_NORMAL,

    //简略行情
    QUOTE_SIMPLE,

    // 详细行情
    QUOTE_DETAIL
}

/**
 * k线复权
 */
enum class FqMode {
    NONE, FRONT, BACK
}

/**
 * k线周期
 */
enum class KlinePeriod {
    DAY, WEEK, MONTH, SEASON, YEAR,
    MINUTE_1, MINUTE_5, MINUTE_15, MINUTE_30, MINUTE_60,MINUTE_120

}

enum class Phase{

    //未知
    UNKNOWN,
    //未开盘 9:00 ~ 9:14:59
    NOT_OPEN,
    //盘前竞价（集合竞价）
    AUCTION,
    //等待开盘  9:25:00 - 9:29:59
    START,
    //交易中
    TRADING,
    //早盘  交易中-上午 (板块、指数)
    TRADING_AM,
    //盘中休市  午休
    NOONTIME,
    //午盘 交易中-下午 （板块、指数）
    TRADING_PM,
    //盘后固定
    AFTER_TRADE,
    //港股 盘前竞价
    HK_AUCTION,
    //港股收盘竞价
    HK_AFTER,
    //停牌
    HALT,
    //临时停牌
    HALT_TEMP,
    //退市
    EXITED,
    //暂停上市
    SUSPENDED,
    //等待上市
    COMING,
    //已收盘
     CLOSED


}