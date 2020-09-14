package com.example.testapp.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapp.R;
import com.example.testapp.model.DataModel;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 11/08/2020
 * Adapter class for Recyclerview item
 */
public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    private List<DataModel> dataModelList;
    private OnItemClickListener listener;

    public DataAdapter(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context=parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_data,(ViewGroup) null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final DataModel dataModel = dataModelList.get(position);
        holder.textViewName.setText(dataModel.getShortName());
        holder.textViewValue.setText(String.valueOf(dataModel.getLastTradePrice()));
        holder.textViewChange.setText(context.getString(R.string.str_change_value,dataModel.getChange()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onItemClick(dataModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.textview_view_name) TextView textViewName;
        @BindView(R.id.textview_view_value) TextView textViewValue;
        @BindView(R.id.textview_view_change) TextView textViewChange;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DataModel dataModel);
    }

}
