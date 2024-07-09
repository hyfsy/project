package exam;

import exam.model.Maze;
import exam.model.Station;
import exam.model.Truck;
import exam.model.TrunkEvent;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Game {

    public static void main(String[] args) {
        startGame();
    }

    public static void startGame() {

        Maze maze = prepareMaze();

        Truck a = new Truck(maze.getGrid(0, 0), 3);
        Truck b = new Truck(maze.getGrid(0, 0), 3);

        // start
        startMove(200, Duration.ofSeconds(1), maze, a, b);
    }

    private static void startMove(int loopCount, Duration duration, Maze maze, Truck... trucks) {

        LocalTime recordTime = LocalTime.of(0, 0);
        StationManager stationManager = new StationManager(getInitStations(maze));
        TrunkScheduler trunkScheduler = new TrunkScheduler(stationManager, trucks);
        BikeScheduler bikeScheduler = new BikeScheduler(stationManager);

        while (loopCount-- > 0 || Thread.interrupted()) {

            recordTime = recordTime.plus(duration.getSeconds(), duration.getUnits().get(0));

            trunkScheduler.schedule();
            bikeScheduler.schedule();

            printState(recordTime, stationManager, trunkScheduler, bikeScheduler);

            try {
                Thread.sleep(duration.toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static Maze prepareMaze() {

        Maze maze = new Maze(8, 8);

        boolean[][] bbs = new boolean[][]{ //
                new boolean[]{true, true, true, true, false, false, false, false}, //
                new boolean[]{true, false, false, true, false, false, false, false}, //
                new boolean[]{true, false, false, true, true, true, true, true}, //
                new boolean[]{true, false, false, false, false, false, false, true}, //
                new boolean[]{true, true, true, true, true, true, false, true}, //
                new boolean[]{false, false, false, false, false, true, false, true}, //
                new boolean[]{false, false, false, false, false, true, true, true}, //
                new boolean[]{false, false, false, false, false, false, false, true}, //
        };

        // block
        for (int i = 0; i < bbs.length; i++) {
            for (int j = 0; j < bbs[i].length; j++) {
                if (!bbs[i][j]) {
                    maze.getGrid(i, j).setBlock(true);
                }
            }
        }

        return maze;
    }

    private static Station[] getInitStations(Maze maze) {
        Station stationA = new Station(maze.getGrid(0, 0), "A", 30);
        Station stationB = new Station(maze.getGrid(3, 2), "B", 40);
        Station stationC = new Station(maze.getGrid(7, 7), "C", 30);
        return new Station[]{stationA, stationB, stationC};
    }

    private static void printState(LocalTime recordTime, StationManager stationManager, TrunkScheduler trunkScheduler, BikeScheduler bikeScheduler) {

        StringBuilder sb = new StringBuilder();
        sb.append(recordTime).append(" ");
        String stationState = Arrays.stream(stationManager.getStations())
                .map(s -> s.getStationName() + "站车" + s.getBikeNum()).collect(Collectors.joining(", "));
        sb.append(stationState);
        sb.append(", 路上车").append(bikeScheduler.getBikeSize());

        List<TrunkEvent> trunkEvents = trunkScheduler.getAndClearArriveEvents();
        if (trunkEvents != null && trunkEvents.size() > 0) {
            for (TrunkEvent trunkEvent : trunkEvents) {
                Station source = trunkEvent.getSource();
                Station target = trunkEvent.getTarget();
                sb.append(", ")
                        .append(source.getStationName())
                        .append("到")
                        .append(target.getStationName())
                        .append("运输了").append(trunkEvent.getBikeNum()).append("辆车");
            }
        }

        System.out.println(sb);
    }
}
