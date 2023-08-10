package algonquin.cst2335.mobilefinalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Logger;

import algonquin.cst2335.mobilefinalproject.data.FlightDataModel;


public class AviationTracker extends AppCompatActivity implements View.OnClickListener {
    Logger log = Logger.getLogger(AviationTracker.class.getName());
    private RequestQueue queue;
    Button submitButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);// takes a layout resource file
        setContentView(R.layout.activity_aviation_tracker); // this inflates the UI
        submitButton = findViewById(R.id.submitButton);
        editText = findViewById(R.id.searchString);

        queue = Volley.newRequestQueue(this);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(submitButton!=null && view.equals(submitButton)) {
            //data to populate the data for recyclerView
            String airportCode = editText.getText().toString();
            ArrayList<FlightDataModel> data=new ArrayList<>();

            // mock data
//            for(int i = 0; i< 10;i ++){
//                data.add(new FlightDataModel(airportCode + i));
//            }
//
//            RecyclerView recyclerView = findViewById(R.id.tan_view);
//            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            final CustomAdapter adapter = new CustomAdapter(data);
//            //adapter.setClickListener(this);
//            recyclerView.setAdapter(adapter);
//
//            adapter.setOnClickListener(new CustomAdapter.OnClickListener() {
//                @Override
//                public void onClick(int position, FlightDataModel model) {
//                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//                    intent.putExtra("flight_number", model.getFlightNumber());
//                    intent.putExtra("destination", model.getDestination());
//                    intent.putExtra("terminal", model.getTerminal());
//                    intent.putExtra("gate", model.getGate());
//                    intent.putExtra("delay", model.getDelay());
//                    startActivity(intent);
//                }
//            });

            String apiKey = "304438ec9f1b5d2c7193b13844d9e060";
            String stringURL = "http://api.aviationstack.com/v1/flights?limit=20&access_key="+ apiKey+ "&dep_iata=" + URLEncoder.encode(airportCode);

            log.info("Request URL" + stringURL);
            Context context = this;
            // get real data:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            JSONArray allData = (JSONArray) response.getJSONArray("data");
                            final ArrayList <FlightDataModel> flightData = FlightUtils.parseFlights(allData);

                            runOnUiThread(() -> {
                                RecyclerView recyclerView = findViewById(R.id.tan_view);
                                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                                final CustomAdapter adapter = new CustomAdapter(flightData);
                                recyclerView.setAdapter(adapter);

                                adapter.setOnClickListener(new CustomAdapter.OnClickListener() {
                                    @Override
                                    public void onClick(int position, FlightDataModel model) {
                                        Intent intent = new Intent(AviationTracker.this, DetailActivity.class);
                                        intent.putExtra("flight_number", model.getFlightNumber());
                                        intent.putExtra("destination", model.getDestination());
                                        intent.putExtra("terminal", model.getTerminal());
                                        intent.putExtra("gate", model.getGate());
                                        intent.putExtra("delay", model.getDelay());
                                        startActivity(intent);
                                    }
                                });
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                        error.printStackTrace();
                    });
            request.setRetryPolicy(new DefaultRetryPolicy(30000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);
        }
    }
}