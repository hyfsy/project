package exam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Maze {

    // 迷宫格子
    private final Grid[][] grids;

    public Maze(int width, int height) {
        grids = new Grid[width][height];
        init();
    }

    private void init() {

        // init
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = new Grid(i, j);
            }
        }

        // relation
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j].setSurround(this.getSurround(i, j));
            }
        }
    }

    public Grid getGrid(int x, int y) {
        return grids[x][y];
    }

    public Grid[][] getGrids() {
        return grids;
    }

    private Grid[] getSurround(int x, int y) {

        List<Grid> surrounds = new ArrayList<>();

        int xf = x + 1;
        int yf = y - 1;
        int xnf = x - 1;
        int ynf = y + 1;

        if (xf < grids.length) {
            surrounds.add(grids[xf][y]);
        }
        if (yf >= 0) {
            surrounds.add(grids[x][yf]);
        }
        if (xnf >= 0) {
            surrounds.add(grids[xnf][y]);
        }
        if (ynf < grids[x].length) {
            surrounds.add(grids[x][ynf]);
        }

        return surrounds.toArray(new Grid[0]);
    }
}
