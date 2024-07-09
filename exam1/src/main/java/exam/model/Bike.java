package exam.model;

import exam.StationManager;

import java.util.Random;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Bike {

    public static final int INIT_SPEED = 1;

    private final Random         r             = new Random();
    // 速度
    private final int            speed;
    private final StationManager stationManager;
    // 移动前的格子
    private       Grid           prev;
    // 当前所处的格子
    private       Grid           pos;
    private       boolean        arriveStation = false;

    public Bike(StationManager stationManager) {
        this(INIT_SPEED, stationManager);
    }

    public Bike(int speed, StationManager stationManager) {
        this.speed = speed;
        this.stationManager = stationManager;
        this.pos = stationManager.borrow(1).getGrid();
    }

    public void move() {
        if (arriveStation()) {
            return;
        }
        for (int i = 0; i < speed; i++) {
            Grid[] surroundGridExclude = pos.getSurroundGridExclude(prev);
            Grid nowPos = surroundGridExclude[r.nextInt(surroundGridExclude.length)];
            prev = pos;
            pos = nowPos;

            // 到车站
            if (stationManager.isStation(pos)) {
                stationManager.rtnBorrow(pos, 1);
                arriveStation = true;
                break;
            }
        }
    }

    public Grid getPos() {
        return pos;
    }

    public boolean arriveStation() {
        return arriveStation;
    }
}
