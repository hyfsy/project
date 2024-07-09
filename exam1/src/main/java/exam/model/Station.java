package exam.model;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class Station {

    private final Grid   grid;
    private final String stationName;
    private int    bikeNum;

    public Station(Grid grid, String stationName, int initBikeNum) {
        this.grid = grid;
        this.stationName = stationName;
        this.bikeNum = initBikeNum;
    }

    public Grid getGrid() {
        return grid;
    }

    public String getStationName() {
        return stationName;
    }

    public int getBikeNum() {
        return bikeNum;
    }

    public void decr(int bikeNum) {
        this.bikeNum -= bikeNum;
    }

    public void incr(int bikeNum) {
        this.bikeNum += bikeNum;
    }
}
