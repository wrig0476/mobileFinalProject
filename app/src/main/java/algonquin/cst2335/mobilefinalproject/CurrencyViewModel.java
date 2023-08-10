package algonquin.cst2335.mobilefinalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

/**
 * this class represents a ViewModel for the currency conversion, and allows the data to survive
 * configuration changes.
 */
public class CurrencyViewModel extends ViewModel {
    /**
     * an observable list of converted currency amounts.
     */
    public MutableLiveData<ArrayList<String>> convertedAmounts = new MutableLiveData< >();
    }

