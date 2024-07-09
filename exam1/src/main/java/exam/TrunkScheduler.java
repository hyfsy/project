package exam;

import exam.model.FunctionTruck;
import exam.model.Truck;
import exam.model.TrunkEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/09/07
 */
public class TrunkScheduler {

    private final List<TrunkEvent>    events = new LinkedList<>();
    private final List<FunctionTruck> trucks;

    public TrunkScheduler(StationManager stationManager, Truck... trucks) {
        this.trucks = new ArrayList<>(Arrays.asList(trucks)).stream().map(t -> new FunctionTruck(stationManager, t)).collect(Collectors.toList());
    }

    public void schedule() {
        for (FunctionTruck truck : trucks) {
            truck.move();
            events.addAll(truck.getAndClearArriveEvents());
        }
    }

    public List<TrunkEvent> getAndClearArriveEvents() {
        List<TrunkEvent> trunkEvents = new ArrayList<>(events);
        events.clear();
        return trunkEvents;
    }
}
