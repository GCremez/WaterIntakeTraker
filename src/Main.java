
import service.WaterTrackerService;
import ui.Cli;

import java.util.ArrayList;


public class Main {
    public static void main(String[] args) {

        WaterTrackerService service = new WaterTrackerService(new ArrayList<>());
        Cli cli = new Cli(service);
        cli.start();

    }
}