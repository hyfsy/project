package exam.model;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class TrunkEvent {
    private Station source;
    private Station target;
    private int     bikeNum;

    public TrunkEvent(Station source, Station target, int bikeNum) {
        this.source = source;
        this.target = target;
        this.bikeNum = bikeNum;
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
}
