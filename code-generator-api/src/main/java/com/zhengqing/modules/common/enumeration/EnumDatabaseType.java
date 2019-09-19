package com.zhengqing.modules.common.enumeration;

/**
 *  <p> 数据库类型 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:15
 */
public enum EnumDatabaseType {

    MySQL("com.mysql.jdbc.Driver", 1),
    Oracle("oracle.jdbc.driver.OracleDriver", 2);

    private String driver;
    private Integer type;

    EnumDatabaseType(String driver, Integer type) {
        this.driver = driver;
        this.type = type;
    }

    public static EnumDatabaseType getEnum(Integer type) {
        for (EnumDatabaseType enumDatabaseType : EnumDatabaseType.values()) {
            if (enumDatabaseType.getType().equals(type)) {
                return enumDatabaseType;
            }
        }
        return MySQL;
    }

    public String getDriver() {
        return driver;
    }

    public Integer getType() {
        return type;
    }

}
