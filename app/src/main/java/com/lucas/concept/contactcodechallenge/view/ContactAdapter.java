package com.lucas.concept.contactcodechallenge.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucas.concept.contactcodechallenge.R;
import com.lucas.concept.contactcodechallenge.controller.ContactController;
import com.lucas.concept.contactcodechallenge.controller.IItemClickListener;
import com.lucas.concept.contactcodechallenge.model.Contact;
import com.squareup.picasso.Picasso;
import com.volcaniccoder.volxfastscroll.IVolxAdapter;

import java.util.ArrayList;
import java.util.List;

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> implements Filterable, IVolxAdapter<Contact> {
    private final Context mContext;
    private ArrayList<Contact> mData;
    private ArrayList<Contact> mDataFiltered;
    private IItemClickListener mClickListener;

    public ContactAdapter(Context context) {
        mContext = context;
        mData = ContactController.getInstance(mContext).getData();
        mDataFiltered = mData;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mDataFiltered = mData;
                } else {
                    ArrayList<Contact> filteredList = new ArrayList<>();
                    for (Contact row : mData) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstName().toLowerCase().contains(charString.toLowerCase()) || row.getLastName().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    mDataFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mDataFiltered = (ArrayList<Contact>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public List<Contact> getList() {
        return mDataFiltered;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mFirstName;
        public TextView mLastName;
        public ImageView mContactThumbnail;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mFirstName = itemView.findViewById(R.id.contact_first_name);
            mLastName = itemView.findViewById(R.id.contact_last_name);
            mContactThumbnail = itemView.findViewById(R.id.contact_image);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onClickDevice(v, mData.indexOf(mDataFiltered.get(getAdapterPosition())));
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.contact_cell, viewGroup, false);
        ContactAdapter.MyViewHolder vh = new ContactAdapter.MyViewHolder(itemView);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.mFirstName.setText(mDataFiltered.get(i).getFirstName());
        myViewHolder.mLastName.setText(mDataFiltered.get(i).getLastName());
        Picasso.with(mContext)
                .load(mDataFiltered.get(i).getThumbUrl())
                .resize((int) mContext.getResources().getDimension(R.dimen.width_image_thumb), (int) mContext.getResources().getDimension(R.dimen.height_image_thumb))
                .error(android.R.drawable.ic_menu_camera)
                .placeholder(android.R.drawable.ic_menu_camera)
                .into(myViewHolder.mContactThumbnail);
    }

    @Override
    public int getItemCount() {
        return mDataFiltered.size();
    }

    public void setClickListener(IItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }
}
