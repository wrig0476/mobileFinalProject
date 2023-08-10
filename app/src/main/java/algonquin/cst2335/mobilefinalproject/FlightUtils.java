package algonquin.cst2335.mobilefinalproject;

import algonquin.cst2335.mobilefinalproject.data.FlightDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Logger;

public class FlightUtils {
    static Logger flightUtilsLog = Logger.getLogger(FlightUtils.class.getName());
    public static ArrayList<FlightDataModel> parseFlights(JSONArray allFlightsArr) {
        int flightCount = allFlightsArr.length();
        ArrayList<FlightDataModel> allFlightsList = new ArrayList<>();
        flightUtilsLog.info("Parsing " + flightCount + "flights.");
        for(int index=0; index < flightCount; index++) {
            try {
                JSONObject flightInfoAll = (JSONObject) allFlightsArr.get(index);
                JSONObject flightData = (JSONObject) flightInfoAll.get("flight");
                JSONObject flightDepartureData = (JSONObject) flightInfoAll.get("departure");
                JSONObject flightArrivalData = (JSONObject) flightInfoAll.get("arrival");

                // Construct structure;
                String flightNumber = flightData.getString("iata");
                String destination = flightArrivalData.getString("airport");
                String terminal = flightDepartureData.getString("terminal");
                String departureGate = flightDepartureData.getString("gate");
                Integer flightDelay;

                if("null".equals(flightNumber)) {
                    continue;
                }
                try {
                    flightDelay = flightDepartureData.getInt("delay");
                } catch (Exception ex) {
                    flightDelay = 0;
                }

                if (null != flightNumber) {
                    FlightDataModel flightDetailsModel = new FlightDataModel(flightNumber);
                    flightDetailsModel.setDestination(null != destination ? destination : "Destination not found");
                    flightDetailsModel.setTerminal(null != terminal ? terminal : "Terminal not found");
                    flightDetailsModel.setGate(null != departureGate ? departureGate : "Departure gate is not announced yet");
                    flightDetailsModel.setDelay(null != flightDelay ? flightDelay.intValue() + " minutes" : "No delay");
                    allFlightsList.add(flightDetailsModel);
                }
            } catch (JSONException ex) {
                flightUtilsLog.warning("Failed to parse a flight");
                continue;
            }
        }
        flightUtilsLog.info("Total " + allFlightsList.size() + " flights were parsed.");
        return allFlightsList;
    }

}
