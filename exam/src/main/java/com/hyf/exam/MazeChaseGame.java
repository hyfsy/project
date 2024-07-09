package com.hyf.exam;

import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/03/31
 */
public class MazeChaseGame
{

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {

        Maze maze = new Maze(8, 8);

        boolean[][] bbs = new boolean[][] { //
                new boolean[] {true, true, true, true, true, true, true, true }, //
                new boolean[] {true, false, false, true, false, false, false, true }, //
                new boolean[] {true, false, true, true, true, true, false, true }, //
                new boolean[] {true, false, true, false, false, true, true, true }, //
                new boolean[] {true, true, true, false, false, true, false, true }, //
                new boolean[] {true, false, true, true, true, true, false, true }, //
                new boolean[] {true, false, false, false, true, false, false, true }, //
                new boolean[] {true, true, true, true, true, true, true, true }, //
        };

        // block
        for (int i = 0; i < bbs.length; i++) {
            for (int j = 0; j < bbs[i].length; j++) {
                if (!bbs[i][j]) {
                    maze.getGrid(i, j).setBlock(true);
                }
            }
        }

        // orange
        maze.getGrid(0, 2).setOrange(true);
        maze.getGrid(5, 4).setOrange(true);

        // cat
        Cat a = new Cat("A猫", maze.getGrid(2, 0), CatRole.HUNTED, 2);
        Cat b = new Cat("B猫", maze.getGrid(2, 5), CatRole.HUNTER, 2);
        Cat c = new Cat("C猫", maze.getGrid(6, 7), CatRole.HUNTER, 2);

        // start
        startMove(100, Duration.ofSeconds(1), a, b, c);
    }

    /**
     * 开始移动cats
     * 
     * @param loopCount
     *            循环次数
     * @param duration
     *            每次循环的周期
     * @param cats
     *            cats
     */
    public static void startMove(int loopCount, Duration duration, Cat... cats) {

        LocalTime recordTime = LocalTime.of(0, 0);

        while (loopCount-- > 0) {

            recordTime = recordTime.plus(duration.getSeconds(), duration.getUnits().get(0));

            for (Cat cat : cats) {
                cat.move();
            }

            // 移动后决定角色的改变
            changeRoleIfNecessary(cats);

            System.out.println(
                    recordTime + " " + Arrays.stream(cats).map(Cat::getMoveState).collect(Collectors.joining(" ")));

            try {
                Thread.sleep(duration.toMillis());
            }
            catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * 移动后，根据cat的移动状况改变cat的角色和掉头
     * 
     * @param cats
     *            cats
     */
    private static void changeRoleIfNecessary(Cat[] cats) {

        Map<CatRole, List<Cat>> catMap = Arrays.stream(cats).collect(Collectors.groupingBy(Cat::getRole));

        List<Cat> hunted = catMap.get(CatRole.HUNTED);
        List<Cat> hunters = catMap.get(CatRole.HUNTER);

        // 哪些格子有哪些被抓捕的猫走过
        Map<Grid, List<Cat>> grids = new HashMap<>();

        // get cats route
        for (Cat cat : hunted) {
            Grid[] prevMovedGrids = cat.getPrevMovedGrids();
            for (Grid prevMovedGrid : prevMovedGrids) {
                grids.compute(prevMovedGrid, (g, l) -> {
                    if (l == null) {
                        l = new ArrayList<>();
                    }

                    l.add(cat);
                    return l;
                });
            }
        }

        // 只掉头一个猫
        AtomicBoolean changeHunted = new AtomicBoolean(false);
        // 掉头过的被抓捕猫
        Set<Cat> dealCat = new HashSet<>();

        // change role
        for (Cat cat : hunters) {
            Grid[] prevMovedGrids = cat.getPrevMovedGrids();
            for (Grid prevMovedGrid : prevMovedGrids) {
                AtomicBoolean meet = new AtomicBoolean(false);
                // 当前格子有被追捕的猫走过
                grids.computeIfPresent(prevMovedGrid, (g, catList) -> {
                    meet.set(true);

                    for (Cat c : catList) {
                        if (dealCat.add(c)) {
                            // 切换角色
                            c.setRole(CatRole.HUNTER);
                            // 掉头
                            c.turnround();
                        }
                    }

                    if (changeHunted.compareAndSet(false, true)) {
                        // 切换角色
                        cat.setRole(CatRole.HUNTED);
                        // 校验是否要掉头
                        if (catList.get(0).getPrev() != cat.getPrev()) {
                            cat.turnround();
                        }
                    }

                    return catList;
                });

                if (meet.get()) {
                    break;
                }
            }
        }
    }
}
