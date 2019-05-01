package com.xzq.eg.exercise3.Bean;

/**
 * created by xzq on 2019/4/26
 */
public class CircleRect {
    private int code;
    private int x;
    private int y;
    private int state;

    public CircleRect() {
    }

    public CircleRect(int code, int x, int y, int state) {
        this.code = code;
        this.x = x;
        this.y = y;
        this.state = state;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
