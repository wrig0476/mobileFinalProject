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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;

import algonquin.cst2335.mobilefinalproject.data.CurrencyViewModel;
import algonquin.cst2335.mobilefinalproject.databinding.ActivityCurrencyConverterBinding;
import algonquin.cst2335.mobilefinalproject.databinding.ConvertionResultsLayoutBinding;

/**
 * This a currency converter app that can convert from one national currency to another desired currency.
 */
public class CurrencyConverter extends AppCompatActivity {

    //buttons for the home button and the menu button.
    private ImageButton imageButton, menuButton;

    //spinner for selecting the currencies to convert from and to.
    private Spinner fromSpinner, toSpinner;

    //edit text for the user to input the amount they want to convert and then display the converted amount results.
    private EditText amountEditText, convertAmount;

    //button to convert currency by performing the performCurrencyConversion() method.
    private Button convertButton;

    //adapter for the recycler view that displays the list of converted amounts.
    private RecyclerView.Adapter convertedAmountAdapter;

    //binding object for this activities layout.
    ActivityCurrencyConverterBinding binding;

    //viewModel that holds the live mutable data.
    CurrencyViewModel currencyViewModel;

    //list that holds the converted amounts.
    ArrayList<String> convertedAmounts;

    /**
     * called when activity is starting.
     * initializes UI components, and sets up event listeners.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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
        menuButton = binding.menuBtn;

        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);
        convertedAmounts = currencyViewModel.convertedAmounts.getValue();

        if(convertedAmounts == null)
        {
            currencyViewModel.convertedAmounts.postValue( convertedAmounts = new ArrayList<String>() );
        }
        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        /**
         * sets an adapter for the recycler view to manage the items in the list.
         * this adapter is responsible for creating, binding, and counting the items
         * related to converted currency amounts.
         */
        binding.recycleView.setAdapter(convertedAmountAdapter = new RecyclerView.Adapter<ConvertedAmountAdapter>() {

            /**
             * creates a new ViewHolder used to represent individual list items.
             * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
             * @param viewType The view type of the new View.
             * @return the newly created view holder
             */
            @NonNull
            @Override
            public ConvertedAmountAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ConvertionResultsLayoutBinding binding = ConvertionResultsLayoutBinding.inflate(getLayoutInflater());
                return new ConvertedAmountAdapter(binding.getRoot());
            }

            /**
             * binds the contents for the ViewHolder to data from the converted amounts list
             * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void onBindViewHolder(@NonNull ConvertedAmountAdapter holder, int position) {
                holder.conversionResult.setText("");
                String obj = convertedAmounts.get(position);
                holder.conversionResult.setText(obj);

            }

            /**
             * gets the number of items available in the dataset.
             * @return the number of items in the converted amounts list.
             */
            @Override
            public int getItemCount() {

                return convertedAmounts.size();
            }
        });

        /**
         * click listener for the menu image button shows alert with app instructions.
         */
        menuButton.setOnClickListener(v -> {
            showMenuAlert();
        });


        /**
         * click listener for the home image button starts the main activity which is the home page.
         */
        imageButton.setOnClickListener(v -> {
            Intent intent = new Intent(CurrencyConverter.this, MainActivity.class);
            startActivity(intent);
        });

        /**
         * defining currency options
         */
        String[] currencyOptions = {"AUD", "CAD", "USD", "NZ"};

        /**
         * creating arrayAdapter instances with the currency option.
         */
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, currencyOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the ArrayAdapter instances to the Spinners
        /**
         * setting the arrayAdapter instances to the spinners.
         */
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);

        /**
         * on click listener for the convert button returns the performCurrencyConversion() method.
         */
        convertButton.setOnClickListener(v -> performCurrencyConversion());

    }

    /**
     * this method is used to perform the currency conversion by communicating with and external conversion API.
     * it retrieves the conversion rates and computes the converted amount.
     */
    private void performCurrencyConversion() {
        String fromCurrency = fromSpinner.getSelectedItem().toString();
        String toCurrency = toSpinner.getSelectedItem().toString();
        String amountString = amountEditText.getText().toString().trim(); // Get the entered amount as a string and remove leading/trailing whitespaces

        //if there is not initial value entered and alert dialog will be triggered.
        if (amountString.isEmpty()) {
            emptyStringAlert();
            return;
        }

        // initializing to a default value
        double currencyAmount = 0.0;

        //converts the string of the entered value into a double
        currencyAmount = Double.parseDouble(amountString);



        //constructs the API url with the selected currencies and amount.
        String apiUrl = "https://api.getgeoapi.com/v2/currency/convert?format=json"
                + "&from=" + fromCurrency
                + "&to=" + toCurrency
                + "&amount=" + currencyAmount
                + "&api_key=2011c0442fd66c69fb987e9e3a20f85e05d0a37a"
                + "&format=json";


        //making the API request using Volley.
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    // Handling the API response here.
                    try {

                        //parse JSON response and extracting the converted amount.
                        JSONObject rates = response.getJSONObject("rates");

                        //check if the to currency is the same as the from currency
                        if (toCurrency.equals(fromCurrency)) {
                            // show alert dialog about an invalid conversion attempt
                            invalidConversionAlert();

                            //check if the to currency is one of the specified valid currencies
                        } else if (toCurrency.equals("AUD") || toCurrency.equals("CAD") || toCurrency.equals("USD")) {
                            //fetch the conversion rate for the selected to currency
                            JSONObject countryArray = rates.getJSONObject(toCurrency);
                            double rateForAmountString = Double.parseDouble(countryArray.getString("rate_for_amount"));
                            //format the converted amount to two decimal places along with a $ symbol
                            String finalAmount = String.format("$%.2f", rateForAmountString);

                            //display the converted amount in the ui
                            convertAmount.setText(finalAmount);

                            //notify the adapter about the newly inserted item in the converted amounts list
                            convertedAmountAdapter.notifyItemInserted(convertedAmounts.size()-1);

                            // show a toast message for the converted amount
                            Toast.makeText(CurrencyConverter.this, "Converted amount: " + finalAmount, Toast.LENGTH_LONG).show();

                            //formatting the string for the final converted value that will be shown on the recycler view
                            String formattedConversion = fromCurrency+" $"+binding.amountEditText.getText().toString()+" -> "+toCurrency+" "+finalAmount;
                            convertedAmounts.add(formattedConversion);

                            // Notify the adapter of the change
                            convertedAmountAdapter.notifyDataSetChanged();

                        }
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                },
                error -> {
                    //if JSON error occurs it will show and API alert dialog
                    showAPIAlert();
                    error.printStackTrace();
                });

        // Add the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /**
     * this method creates a snack bar for when you have not input a value to convert.
     */
    private void emptyStringAlert() {
        Snackbar.make(binding.getRoot(), R.string.emptyStringError, Snackbar.LENGTH_LONG).show();
    }

    /**
     * this method creates an alert dialog for when you are trying to convert the same currency.
     */
    private void invalidConversionAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
                .setMessage(R.string.invalidConversionAlert)
                .setPositiveButton("OK", null)
                .show();
    }

    /**
     * this method creates an alert dialog for app instructions shown when pressing the menu button.
     */
    private void showMenuAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alertMenuTitle)
                .setMessage(R.string.menuInstructions)
                .setPositiveButton("ok", null)
                .show();
    }

    /**
     * this creates and alert dialog for errors during an API request.
     */
    private void showAPIAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error)
                .setMessage(R.string.APIAlert)
                .setPositiveButton("ok", null)
                .show();
    }

    /**
     * ViewHolder class for displaying converted amounts in the recycler view.
     */
    class ConvertedAmountAdapter extends RecyclerView.ViewHolder {
        /**
         * text view variable for defining the recyclerResults textview.
         */
        TextView conversionResult;

        /**
         * Constructor for the ViewHolder.
         * @param itemView the view associated with this ViewHolder.
         */
         public ConvertedAmountAdapter(@NonNull View itemView) {
            super(itemView);

            conversionResult = itemView.findViewById(R.id.recyclerResults);
        }
    }

}
