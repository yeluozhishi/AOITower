package org.whk;

import lombok.Getter;

@Getter
public enum VIEW_UPDATE {
    REMOVE("移除"),
    ADD_NEW("新增"),
    UPDATE("更新"),


    ;

    private final String desc;

    VIEW_UPDATE(String desc) {
        this.desc = desc;
    }

}
