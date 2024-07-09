package exam.model;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Grid {

    private final int x;
    private final int y;

    // 当前格子四周的格子
    private Grid[] surround;

    private boolean block;

    public Grid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Grid[] getSurroundGridExclude(/* @Nullable */ Grid prev) {
        if (surround == null) {
            throw new IllegalStateException();
        }
        Grid[] grids = Arrays.stream(surround).filter(g -> !g.block).filter(g -> !g.equals(prev)).toArray(Grid[]::new);
        if (grids.length != 0) {
            return grids;
        }
        return new Grid[]{prev};
    }

    public void setSurround(Grid... surround) {
        if (surround == null) {
            throw new IllegalStateException();
        }
        this.surround = surround;
    }

    public boolean isBlock() {
        return block;
    }

    public void setBlock(boolean block) {
        this.block = block;
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
