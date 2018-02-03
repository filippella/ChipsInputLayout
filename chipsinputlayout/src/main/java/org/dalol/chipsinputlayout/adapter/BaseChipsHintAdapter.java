/*
 * Copyright (c) 2018 Filippo Engidashet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dalol.chipsinputlayout.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;


import org.dalol.chipsinputlayout.model.ChipInput;
import org.dalol.chipsinputlayout.holders.ChipsInputViewHolder;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Filippo Engidashet <filippo.eng@gmail.com>
 * @version 1.0.0
 * @since 03/02/2018.
 */

public abstract class BaseChipsHintAdapter<C extends ChipInput, V extends ChipsInputViewHolder> extends BaseAdapter implements Filterable {

    private final List<C> mAllChipsHintList = new LinkedList<>();
    private final List<C> mChipsHintList = new LinkedList<>();
    private final LayoutInflater mLayoutInflater;
    private final Filter mHintFilter = new ChipsHintFilter();

    public BaseChipsHintAdapter(Context context, @NonNull List<C> allChipsHintList) {
        mLayoutInflater = LayoutInflater.from(context);
        mAllChipsHintList.addAll(allChipsHintList);
        mChipsHintList.addAll(allChipsHintList);
    }

    @Override
    public int getCount() {
        return mChipsHintList.size();
    }

    @Override
    public C getItem(int position) {
        return mChipsHintList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if (convertView == null) {
            viewHolder = onCreateViewHolder(mLayoutInflater, parent, position);
            convertView = viewHolder.itemView;
            viewHolder.mPosition = position;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (V) convertView.getTag();
        }
        bindViewHolder(viewHolder, position);
        return convertView;
    }

    protected abstract V onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int position);

    protected abstract void bindViewHolder(V viewHolder, int position);

    @Override
    public Filter getFilter() {
        return mHintFilter;
    }

    public void updateChipsHint(List<? extends ChipInput> chipInputs) {
        List<C> filteredChipsList = new LinkedList<>(mAllChipsHintList);
        filteredChipsList.removeAll(chipInputs);
        mChipsHintList.clear();
        mChipsHintList.addAll(filteredChipsList);
        notifyDataSetChanged();
    }

    class ChipsHintFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<C> filteredChipsList = new LinkedList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredChipsList.addAll(mAllChipsHintList);
            } else {
                String constraint = charSequence.toString();
                for (int i = 0, size = mAllChipsHintList.size(); i < size; i++) {
                    C chipInput = mAllChipsHintList.get(i);
                    if (chipInput.toString().toLowerCase().contains(constraint.toLowerCase())) {
                        filteredChipsList.add(chipInput);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredChipsList;
            filterResults.count = filteredChipsList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mChipsHintList.clear();
            mChipsHintList.addAll((Collection<? extends C>) filterResults.values);
            notifyDataSetChanged();
        }
    }
}
