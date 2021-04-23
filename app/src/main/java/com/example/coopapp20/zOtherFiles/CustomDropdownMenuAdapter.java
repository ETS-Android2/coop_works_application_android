package com.example.coopapp20.zOtherFiles;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CustomDropdownMenuAdapter extends ArrayAdapter {

    private Filter filter = new NoFilter();
    private List<String> items;

    public CustomDropdownMenuAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        items = objects;
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return items.get(position);
    }



    @Override
    public int getPosition(@Nullable Object item) {
        return items.indexOf(item);
    }

    private class NoFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence arg0) {
            FilterResults result = new FilterResults();
            if(items != null) {
                result.values = items;
                result.count = items.size();
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence arg0, FilterResults arg1) {
            notifyDataSetChanged();
        }
    }
}
