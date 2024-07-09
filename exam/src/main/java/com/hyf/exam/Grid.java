package com.hyf.exam;

import java.util.Arrays;
import java.util.Objects;

/**
 * 每个格子
 * 
 * @author baB_hyf
 * @date 2022/03/31
 */
public class Grid
{

    private final int x;
    private final int y;

    // 当前格子四周的格子
    private Grid[] surround;

    private boolean block;
    private boolean orange;

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Grid[] getSurroundGridExclude(/* @Nullable */ Grid grid) {
        return Arrays.stream(surround).filter(g -> !g.block).filter(g -> !g.equals(grid)).toArray(Grid[]::new);
    }

    public void setSurround(Grid... surround) {
        this.surround = surround;
    }

    public void setOrange(boolean orange) {
        this.orange = orange;
    }

    public boolean isOrange() {
        return orange;
    }

    public void setBlock(boolean block) {
        this.block = block;
    }

    public boolean isBlock() {
        return block;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Grid grid = (Grid) o;
        return x == grid.x && y == grid.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
