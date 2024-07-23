package com.njdge.fastbuilder.arena;

import lombok.Data;
import lombok.Getter;

@Getter
public enum ArenaType {
    NORMAL(55, 6),
    SHORT(33,3),
    INCLINED_SHORT(23,3);

    private int length; //spawn point to finish line
    private int height; //spawn point's height to finish line's height

    ArenaType(int length, int height) {
        this.length = length;
        this.height = height;
    }
}
