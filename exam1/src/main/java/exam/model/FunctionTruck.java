package exam.model;

import exam.StationManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class FunctionTruck {

    private final List<TrunkEvent> events = new LinkedList<>();

    private StationManager stationManager;
    private Truck          truck;

    public FunctionTruck(StationManager stationManager, Truck truck) {
        this.stationManager = stationManager;
        this.truck = truck;
        updatePlan();
    }

    public void move() {
        int speed = truck.getSpeed();
        for (int i = 0; i < speed; i++) {
            truck.moveOne();

            Grid pos = truck.getPos();

            // 到车站
            if (stationManager.isStation(pos)) {
                addArriveEvent(truck);
                stationManager.rtnBorrow(pos, truck.getBikeNum());
                updatePlan();
                break;
            }

        }
    }

    private void updatePlan() {

        Station[] stations = stationManager.getStations();


        int maxBikeNum = 0;
        for (Station station : stations) {
            maxBikeNum += station.getBikeNum();
        }


        int minStationNum = Integer.MAX_VALUE;
        Station minStation = null;
        for (Station station : stations) {
            minStationNum = Math.min(station.getBikeNum(), minStationNum);
            minStation = station;
        }

        boolean set = false;

        int i = maxBikeNum / stations.length;
        for (Station station : stations) {
            if (station.getBikeNum() > i) {
                int borrowNum = station.getBikeNum() - i;
                station.decr(borrowNum);
                truck.setBikeNum(borrowNum);
                truck.setSource(station);
                truck.setTarget(minStation);
                set = true;
                break;
            }
        }

        if (!set) {
            for (Station station : stations) {
                if (station.getBikeNum() > 0) {
                    station.decr(1);
                    truck.setBikeNum(1);
                    truck.setSource(station);
                    truck.setTarget(minStation);
                    break;
                }
            }
        }
    }

    private void addArriveEvent(Truck truck) {
        Station source = truck.getSource();
        Station target = truck.getTarget();
        TrunkEvent trunkEvent = new TrunkEvent(source, target, truck.getBikeNum());
        events.add(trunkEvent);
    }

    public List<TrunkEvent> getAndClearArriveEvents() {
        List<TrunkEvent> trunkEvents = new ArrayList<>(events);
        events.clear();
        return trunkEvents;
    }
}
