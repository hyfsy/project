package com.hyf.exam;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/03/31
 */
public class Cat
{

    public static final int MAX_SPEED = 4;
    public static final int MIN_SPEED = 1;

    private final Random r = new Random();
    // 名称
    private final String name;

    // 追捕角色
    private CatRole role;
    // 移动前的格子
    private Grid prev;
    // 当前所处的格子
    private Grid pos;
    // 速度
    private int speed;

    // 前一次移动的路径
    private Grid[] prevMovedGrids;
    // 移动的日志记录
    private String moveState = "";
    // 掉头的提示
    private Grid hint;

    public Cat(String name, Grid pos, CatRole role) {
        this(name, pos, role, MIN_SPEED);
    }

    public Cat(String name, Grid pos, CatRole role, int speed) {
        this.name = name;
        this.pos = pos;
        this.role = role;
        this.speed = speed;
    }

    public void slow() {
        speed = Math.max(MIN_SPEED, speed >> 1);
    }

    public void fast() {
        speed = Math.min(MAX_SPEED, speed << 1);
    }

    public void turnround() {
        hint = prev;
    }

    public void move() {
        Grid[] prevMovedGrids = new Grid[speed];

        boolean hasOrange = false;

        // 移动
        // TODO
        for (int i = 0; i < speed; i++) {
            Grid currentGrid = pos;

            Grid nextGrid = hint;
            hint = null; // 立马清除
            if (nextGrid == null) {
                Grid[] surroundGrids = currentGrid.getSurroundGridExclude(prev);
                nextGrid = surroundGrids[r.nextInt(surroundGrids.length)];
            }

            if (nextGrid.isOrange()) {
                hasOrange = true;
            }

            prevMovedGrids[i] = currentGrid;
            this.prev = currentGrid;
            this.pos = nextGrid;
        }

        // 是否改变速度
        int prevSpeed = speed;
        if (hasOrange) {
            if (r.nextInt(2) >= 1) {
                fast();
            }
            else {
                slow();
            }
        }

        // 移动后记录
        recordMoveState(prevMovedGrids, pos, prevSpeed != speed);

        this.prevMovedGrids = prevMovedGrids;
    }

    /**
     * 记录上次移动的信息
     *
     * @param prevMovedGrids 移动的路径
     * @param curGrid 当前位置
     * @param speedChange 速度是否改变
     */
    private void recordMoveState(Grid[] prevMovedGrids, Grid curGrid, boolean speedChange) {
        Grid[] grids = prevMovedGrids;
        if (curGrid != null) {
            grids = new Grid[prevMovedGrids.length + 1];
            System.arraycopy(prevMovedGrids, 0, grids, 0, prevMovedGrids.length);
            grids[prevMovedGrids.length] = curGrid;
        }
        String move = Arrays.stream(grids).map(g -> (char)(97 + g.getX()) + "" + g.getY()).collect(Collectors.joining("->"));
        if (speedChange) {
            this.moveState = name + "移动 " + move + ", 速度变为" + speed + "格/秒" + ";";
        }
        else {
            this.moveState = name + "移动 " + move + ";";
        }
    }

    public Grid getPrev() {
        return prev;
    }

    public Grid getPos() {
        return pos;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public CatRole getRole() {
        return role;
    }

    public void setRole(CatRole role) {
        this.role = role;
    }

    public String getMoveState() {
        return moveState;
    }

    public Grid[] getPrevMovedGrids() {
        return this.prevMovedGrids;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cat cat = (Cat) o;
        return speed == cat.speed && Objects.equals(r, cat.r) && Objects.equals(name, cat.name) && role == cat.role
                && Objects.equals(prev, cat.prev) && Objects.equals(pos, cat.pos)
                && Arrays.equals(prevMovedGrids, cat.prevMovedGrids) && Objects.equals(moveState, cat.moveState);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(r, name, role, prev, pos, speed, moveState);
        result = 31 * result + Arrays.hashCode(prevMovedGrids);
        return result;
    }
}
