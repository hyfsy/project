package exam;

import exam.model.Grid;
import exam.model.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class StationManager {
    private final Random r = new Random();

    private final Station[] stations;

    public StationManager(Station[] stations) {
        if (stations == null) {
            throw new IllegalArgumentException();
        }
        this.stations = stations;
    }

    public Station[] getStations() {
        Station[] stations = new Station[this.stations.length];
        System.arraycopy(this.stations, 0, stations, 0, this.stations.length);
        return stations;
    }

    public boolean isStation(Grid grid) {
        for (Station station : stations) {
            if (station.getGrid() == grid) {
                return true;
            }
        }
        return false;
    }

    public Station getStation(Grid grid) {
        for (Station station : stations) {
            if (station.getGrid() == grid) {
                return station;
            }
        }
        throw new RuntimeException("Not station");
    }

    public Station borrow(int num) {
        List<Station> stations = new ArrayList<>(Arrays.asList(getStations()));
        while (stations.size() > 0) {
            Station station = stations.get(r.nextInt(stations.size()));
            if (station.getBikeNum() > 0) {
                station.decr(num);
                return station;
            }
            stations.remove(station);
        }
        throw new RuntimeException("Station has no bike");
    }

    public void rtnBorrow(Grid grid, int num) {
        Station station = getStation(grid);
        station.incr(num);
    }
}
