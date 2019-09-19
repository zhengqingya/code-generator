package com.zhengqing.test;

import lombok.Data;

@Data
public class FieldVo {

    private String title;

    private String filed;

    public FieldVo() {
    }

    public FieldVo(String title, String filed) {
        this.title = title;
        this.filed = filed;
    }
}
