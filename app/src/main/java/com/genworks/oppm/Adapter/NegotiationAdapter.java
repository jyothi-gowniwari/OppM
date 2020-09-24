package com.genworks.oppm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.genworks.oppm.model.ModelTest;
import com.genworks.oppm.R;

import java.util.ArrayList;

public class NegotiationAdapter extends RecyclerView.Adapter<NegotiationAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<ModelTest> modelTests;
    private MyNegotiationItemClickListener clickListener;
    public boolean isproposal = false;
    private MyNegotiationEditItemClickListner editItemClickListner;


    public NegotiationAdapter(Context context, ArrayList<ModelTest> modelTests, NegotiationAdapter.MyNegotiationItemClickListener clickListener, NegotiationAdapter.MyNegotiationEditItemClickListner editItemClickListner) {
        mContext = context;
        this.modelTests = modelTests;
        this.clickListener = clickListener;
        this.editItemClickListner = editItemClickListner;

    }
    @Override
    public NegotiationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_nego, parent, false);
        NegotiationAdapter.MyViewHolder holder = new NegotiationAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NegotiationAdapter.MyViewHolder holder, final int position) {

        ModelTest opportunity=modelTests.get(position);
        holder.account_name.setText(opportunity.getRelated());
        holder.potential_name.setText(opportunity.getPotentialValue());
        holder.contact_name.setText(opportunity.getContact());
        holder.potential_no.setText(opportunity.getPotentialNo());
        holder.location.setText(opportunity.getLocation());
        holder.item_name.setText(opportunity.getProductValue());
        holder.amount.setText(opportunity.getValueAndQty());
        holder.qty.setText(opportunity.getQty());
        holder.win_prob.setText(opportunity.getWinprobValue());
        holder.assigned_to.setText(opportunity.getAssigned());
        String winProbePercent =opportunity.getWinprobValue();

        if(winProbePercent.equals("30 %")){
            holder.win_prob.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if(winProbePercent.equals("50 %")){
            holder.win_prob.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if(winProbePercent.equals("80 %")){
            holder.win_prob.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isproposal=true)

                    clickListener.myNegatiationItemClick(position);
            }
        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isproposal=false;

                // showPopupMenu(holder.buttonViewOption);
                PopupMenu popup = new PopupMenu(mContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_card, popup.getMenu());
                // popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.edit:
                                editItemClickListner.myEditNegationItemClick(position);
                                return true;
                            case R.id.share:
                                Intent intent=new Intent(Intent.ACTION_SEND);
                                StringBuilder sb = new StringBuilder();
                                sb.append("Negotiation or Review Details : ");
                                sb.append('\n');
                                sb.append("Sales Stage : ");
                                sb.append(modelTests.get(position).getSalesstageValue());
                                sb.append('\n');
                                sb.append("Account Name : ");
                                sb.append(modelTests.get(position).getRelated());
                                sb.append('\n');
                                sb.append("Assigned To : ");
                                sb.append(modelTests.get(position).getAssigned());
                                sb.append('\n');
                                sb.append("Contact : ");
                                sb.append(modelTests.get(position).getContact());
                                sb.append('\n');
                                sb.append("Location : ");
                                sb.append(modelTests.get(position).getLocation());
                                sb.append('\n');
                                sb.append("Product Value: ");
                                sb.append(modelTests.get(position).getProductValue().concat("(")+modelTests.get(position).getQty());
                                sb.append('\n');
                                sb.append("Potential No. : ");
                                sb.append(modelTests.get(position).getPotentialNo());
                                intent.putExtra(android.content.Intent.EXTRA_TEXT, (CharSequence) sb);
                                intent.setType("text/plain");
                                mContext.startActivity(Intent.createChooser(intent,"send to"));
                                return true;
                            default:
                        }
                        return false;

                    }
                });
                popup.show();

            }
        });


    }
    @Override
    public int getItemCount() {

        return modelTests.size();

    }
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView buttonViewOption,potential_name, account_name, contact_name,item_name,potential_no,amount,qty,location,win_prob,assigned_to;
        Button email,pdf;
        SwipeLayout sample1;
        CardView cardView;
        ImageView share,tvEdit;
        LinearLayout cards;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            // sample1 = (SwipeLayout) itemView.findViewById(R.id.sample1);
            image=(ImageView)itemView.findViewById(R.id.image);
            share = (ImageView) itemView.findViewById(R.id.share);
            tvEdit = (ImageView) itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardView);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);
            account_name = (TextView) itemView.findViewById(R.id.account_name);
            potential_name = (TextView) itemView.findViewById(R.id.potential_name);
            contact_name = (TextView) itemView.findViewById(R.id.contact_name);
            item_name = (TextView) itemView.findViewById(R.id.item_name);
            location = (TextView) itemView.findViewById(R.id.location);
            potential_no = (TextView) itemView.findViewById(R.id.potential_no);
            amount = (TextView) itemView.findViewById(R.id.amount);
            win_prob = (TextView) itemView.findViewById(R.id.win_prob);
            assigned_to= (TextView) itemView.findViewById(R.id.assigned_to);
            qty = (TextView) itemView.findViewById(R.id.text_qty);
            email = (Button) itemView.findViewById(R.id.email);
            pdf = (Button) itemView.findViewById(R.id.pdf);
        }
    }
    public interface MyNegotiationItemClickListener {
        void myNegatiationItemClick(int position);
    }
    public interface MyNegotiationEditItemClickListner {
        void myEditNegationItemClick(int position);
    }
}


