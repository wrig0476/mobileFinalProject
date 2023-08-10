package algonquin.cst2335.mobilefinalproject;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import algonquin.cst2335.mobilefinalproject.data.FlightDataModel;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    Logger log = Logger.getLogger(CustomAdapter.class.getName());
    private List<FlightDataModel> data;

    private OnClickListener onClickListener;

    public CustomAdapter( List<FlightDataModel> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View views = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flight_resource,parent,false);

        return new ViewHolder(views);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FlightDataModel singleData = this.data.get(position);
        holder.flightNumber.setText(singleData.getFlightNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                log.info("Following flight is clicked:" + singleData.getFlightNumber() + ", destination: " + singleData.getDestination());
                if(onClickListener != null) {
                    onClickListener.onClick(position, singleData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, FlightDataModel model);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView flightNumber;
        public ViewHolder(View view) {
            super(view);
            flightNumber = view.findViewById(R.id.someText);
        }

    }
}
