package exam;

import exam.model.Bike;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class BikeScheduler {

    private final List<Bike> bikes = new LinkedList<>();

    private final StationManager stationManager;

    public BikeScheduler(StationManager stationManager) {
        this.stationManager = stationManager;
    }

    public void schedule() {
        useBike();
        doSchedule();
    }

    private void useBike() {
        Bike bike = new Bike(stationManager);
        bikes.add(bike);
    }

    private void doSchedule() {
        Iterator<Bike> it = bikes.iterator();
        while (it.hasNext()) {
            Bike bike = it.next();
            bike.move();
            if (bike.arriveStation()) {
                it.remove();
            }
        }
    }

    public int getBikeSize() {
        return bikes.size();
    }
}
