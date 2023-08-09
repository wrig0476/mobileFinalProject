package algonquin.cst2335.mobilefinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.mobilefinalproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.mobilefinalproject.databinding.ConvertionResultsLayoutBinding;

public class CurrencyConverter extends AppCompatActivity {

    private ImageButton imageButton;
    private Spinner fromSpinner;
    private Spinner toSpinner;
    private EditText amountEditText;
    private EditText convertAmount;
    private Button convertButton;
    private RecyclerView.Adapter convertedAmountAdapter;

    ActivityCurrencyConverterBinding binding;
    ArrayList<String> convertedAmounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCurrencyConverterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imageButton = binding.imgButton;
        fromSpinner = binding.fromSpinner;
        toSpinner = binding.toSpinner;
        amountEditText = binding.amountEditText;
        convertAmount = binding.convertAmount;
        convertButton = binding.convertButton;


        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(convertedAmountAdapter = new RecyclerView.Adapter<ConvertedAmountAdapter>() {
            @NonNull
            @Override
            public ConvertedAmountAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ConvertionResultsLayoutBinding binding = ConvertionResultsLayoutBinding.inflate(getLayoutInflater());
                return new ConvertedAmountAdapter(binding.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull ConvertedAmountAdapter holder, int position) {
                holder.conversionResult.setText("");
                String obj = convertedAmounts.get(position);
                holder.conversionResult.setText(obj);

            }

            @Override
            public int getItemCount() {
                return convertedAmounts.size();
            }
            public int getItemViewType(int position){
                return 0;
            }
        });


        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(CurrencyConverter.this, MainActivity.class);
            startActivity(intent);
        });

        // Define the currency options
        String[] currencyOptions = {"AUD", "CAD", "USD"};

        // Create ArrayAdapter instances with the currency options
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencyOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter instances to the Spinners
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);


        convertButton.setOnClickListener(v -> performCurrencyConversion());

    }

    private void performCurrencyConversion() {
        String fromCurrency = fromSpinner.getSelectedItem().toString();
        String toCurrency = toSpinner.getSelectedItem().toString();
        String amountString = amountEditText.getText().toString().trim(); // Get the entered amount as a string and remove leading/trailing whitespaces

        if (amountString.isEmpty()) {
            // If the amountEditText is empty, show an error dialog
            showErrorDialog("Please enter a valid amount.");
            return;
        }

        double currencyAmount = 0.0; // Initialize amount to a default value

        try {
            currencyAmount = Double.parseDouble(amountString);
        } catch (NumberFormatException e) {
            // If the entered amount is not a valid number, show an error dialog
            showErrorDialog("Invalid amount entered. Please enter a valid number.");
            return;
        }

        // Construct the API URL with the selected currencies and amount
        String apiUrl = "https://api.getgeoapi.com/v2/currency/convert?format=json"
                + "&from=" + fromCurrency
                + "&to=" + toCurrency
                + "&amount=" + currencyAmount
                + "&api_key=2011c0442fd66c69fb987e9e3a20f85e05d0a37a"
                + "&format=json";

        // Make the API request using Volley
        // Handle API request errors here
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    // Handle the API response here
                    try {
                        // Parse the JSON response and extract the converted amount
                        JSONObject rates = response.getJSONObject("rates");

                        if (toCurrency.equals(fromCurrency)) {
                            showErrorDialog("Cannot convert " + fromCurrency + " to " + toCurrency);
                        } else if (toCurrency.equals("AUD") || toCurrency.equals("CAD") || toCurrency.equals("USD")) {
                            JSONObject countryArray = rates.getJSONObject(toCurrency);
                            double rateForAmountString = Double.parseDouble(countryArray.getString("rate_for_amount"));
                            String finalAmount = String.format("$%.2f", rateForAmountString);

                            convertAmount.setText(finalAmount);


                            convertedAmounts.add(binding.amountEditText.getText().toString());
                            convertedAmountAdapter.notifyItemInserted(convertedAmounts.size()-1);
                            binding.amountEditText.setText("");





                            Toast.makeText(CurrencyConverter.this, "Converted amount: " + finalAmount, Toast.LENGTH_LONG).show();

                            convertedAmounts.add(finalAmount);

                            // Notify the adapter of the change
                            convertedAmountAdapter.notifyDataSetChanged();

                        } else {
                            showErrorDialog("Invalid currency selected.");
                        }

                    } catch (JSONException e) {
                        showErrorDialog("Error parsing API response.");
                        e.printStackTrace();
                    }
                },
                error -> {
                    showErrorDialog("Error occurred during API request.");
                    error.printStackTrace();
                });

        // Add the request to the RequestQueue (Volley handles it automatically)
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .show();
    }

    class ConvertedAmountAdapter extends RecyclerView.ViewHolder {
        TextView conversionResult;
         public ConvertedAmountAdapter(@NonNull View itemView) {
            super(itemView);

            conversionResult = itemView.findViewById(R.id.recyclerResults);
        }
    }

}
