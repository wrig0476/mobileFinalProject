package algonquin.cst2335.mobilefinalproject.data;

public class FlightDataModel {
    private String flightNumber;

    private String destination= "Toronto";
    private String terminal = "1";
    private String gate = "A23";

    private String delay = "10 minutes";

    public FlightDataModel(String flightNumber, String destination, String terminal, String gate, String delay) {
        this.flightNumber = flightNumber;
        this.destination = destination;
        this.terminal = terminal;
        this.gate = gate;
        this.delay = delay;
    }

    public FlightDataModel(String flightNumber) {
        this(flightNumber, "Toronto", "1", "A23", "10 minutes");
    }

    public String getFlightNumber() {
        return this.flightNumber;
    }

    public String getDestination() {
        return this.destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
    public String getGate() {
        return this.gate;
    }

    public void setGate(String gate) {
        this.gate = gate;
    }
    public String getTerminal() {
        return this.terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
    public String getDelay() {
        return this.delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }
}
