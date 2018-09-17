package com.nazran.filtering;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactListAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<ModelContact> modelContacts;
    private ArrayList<ModelContact> modelContactsFiltered;
    private NumberFilter numberFilter;

    public ContactListAdapter(Context context, ArrayList<ModelContact> modelContacts) {
        this.context = context;
        this.modelContacts = modelContacts;
        this.modelContactsFiltered = modelContacts;
        getFilter();
    }

    @Override
    public int getCount() {
        return modelContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return modelContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.contact_list_item, parent, false);

        TextView name = view.findViewById(R.id.name);
        TextView number = view.findViewById(R.id.number);

        ModelContact modelContact = modelContacts.get(position);
        name.setText(modelContact.getName());
        number.setText(modelContact.getNumber());

        return view;
    }

    @Override
    public Filter getFilter() {
        if (numberFilter == null)
            numberFilter = new NumberFilter();

        return numberFilter;
    }

    private class NumberFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();

            if (constraint != null && constraint.length() > 0) {

                ArrayList<ModelContact> filterContacts = new ArrayList<>();

                for (int i = 0; i < modelContactsFiltered.size(); i++) {
                    if (modelContactsFiltered.get(i).getNumber()
                            .contains(constraint.toString())
                            || modelContactsFiltered.get(i).getName().toLowerCase()
                            .contains(constraint.toString().toLowerCase())) {

                        filterContacts.add(modelContactsFiltered.get(i));
                    }
                }
                filterResults.count = filterContacts.size();
                filterResults.values = filterContacts;
            } else {
                filterResults.count = modelContactsFiltered.size();
                filterResults.values = modelContactsFiltered;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelContacts = (ArrayList<ModelContact>) results.values;
            notifyDataSetChanged();
        }
    }
}
