package com.karolina.check.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.karolina.check.R;
import com.karolina.check.databinding.TemplateVaccineItemBinding;
import com.karolina.check.models.Vaccine;

import java.util.List;

/**
 * Created by RicardoAndrés on 26/04/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public interface OnItemClickAdapter{
        void onIClick(View v);
    }

    Context context;
    List<Vaccine> vaccineList;
    OnItemClickAdapter onItemClickAdapter;
    View conView;

    public ItemAdapter(Context context, List<Vaccine> vaccineList, OnItemClickAdapter onItemClickAdapter) {
        this.context = context;
        this.vaccineList = vaccineList;
        this.onItemClickAdapter = onItemClickAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = View.inflate(context, R.layout.template_vaccine_item, null);
        RecyclerView.ViewHolder viewHolder = new VaccineHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Vaccine vaccine = vaccineList.get(position);
        VaccineHolder viewHolder = (VaccineHolder) holder;
        viewHolder.binding.setVaccine(vaccine);

        conView = viewHolder.binding.getRoot();
        conView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onItemClickAdapter.onIClick(v);
    }

    @Override
    public int getItemCount() {
        return vaccineList.size();
    }

    static class VaccineHolder extends RecyclerView.ViewHolder{

        TemplateVaccineItemBinding binding;

        public VaccineHolder(View itemView) {
            super(itemView);
            binding = TemplateVaccineItemBinding.bind(itemView);
        }
    }

}
