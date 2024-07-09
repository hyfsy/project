package exam.model;

import java.util.Random;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Truck {

    private final Random r = new Random();

    private int  speed;
    private Grid pos;
    private Grid prev;

    private Station source;
    private Station target;
    private int  bikeNum;

    public Truck(Grid pos, int speed) {
        this.pos = pos;
        this.speed = speed;
    }

    public void moveOne() {
        Grid[] surroundGridExclude = pos.getSurroundGridExclude(prev);
        Grid nowPos = surroundGridExclude[r.nextInt(surroundGridExclude.length)];
        prev = pos;
        pos = nowPos;
    }

    public int getSpeed() {
        return speed;
    }

    public Station getSource() {
        return source;
    }

    public void setSource(Station source) {
        this.source = source;
    }

    public Station getTarget() {
        return target;
    }

    public void setTarget(Station target) {
        this.target = target;
    }

    public int getBikeNum() {
        return bikeNum;
    }

    public void setBikeNum(int bikeNum) {
        this.bikeNum = bikeNum;
    }

    public Grid getPos() {
        return pos;
    }
}
