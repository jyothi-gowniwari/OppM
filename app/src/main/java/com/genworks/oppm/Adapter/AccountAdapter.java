package com.genworks.oppm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.genworks.oppm.model.AccountModel;
import com.genworks.oppm.R;


import java.util.ArrayList;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private Context mContext;
    private boolean isedit=false;
    private ArrayList<AccountModel> accountlist;
    private AccountAdapter.MyItemClickListener clickListener;
    private AccountAdapter.MyEditItemClickListner editItemClickListner;


    public AccountAdapter(Context context, ArrayList<AccountModel> accountlist, AccountAdapter.MyItemClickListener clickListener, AccountAdapter.MyEditItemClickListner editItemClickListner) {
        mContext = context;
        this.accountlist = accountlist;
        this.clickListener = clickListener;
        this.editItemClickListner = editItemClickListner;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_account, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.country.setText(accountlist.get(position).getAccountName());
        holder.name.setText(accountlist.get(position).getOwnershipType());
        holder.facility_type.setText(accountlist.get(position).getFacilityType());
        holder.bill_street.setText(accountlist.get(position).getBill_street());
        holder.bill_city.setText(accountlist.get(position).getBill_city());
        holder.bill_district.setText(accountlist.get(position).getBillingDistrict());
        holder.bill_state.setText(accountlist.get(position).getBillingState());
        holder.email.setText(accountlist.get(position).getEmail());
        holder.mobile.setText(accountlist.get(position).getPhone());
        holder.account_manager.setText(accountlist.get(position).getAssigned());

        final String mobile_number = accountlist.get(position).getPhone();
        holder.mobile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + mobile_number));
                mContext.startActivity(callIntent);
            }

        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isedit=true;

               // showPopupMenu(holder.buttonViewOption);
                PopupMenu popup = new PopupMenu(mContext, view);
                final MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_card, popup.getMenu());
               // popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.edit:
                                    editItemClickListner.myEditItemClick(position);
                                    return true;
                                case R.id.share:
                                    Intent intent=new Intent(Intent.ACTION_SEND);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Account Details : ");
                                    sb.append('\n');
                                    sb.append("Account Name : ");
                                    sb.append(accountlist.get(position).getAccountName());
                                    sb.append('\n');
                                    sb.append("Ownership Type : ");
                                    sb.append(accountlist.get(position).getOwnershipType());
                                    sb.append('\n');
                                    sb.append("Facility Type : ");
                                    sb.append(accountlist.get(position).getFacilityType());
                                    sb.append('\n');
                                    sb.append("Street : ");
                                    sb.append(accountlist.get(position).getBill_street());
                                    sb.append('\n');
                                    sb.append("City : ");
                                    sb.append(accountlist.get(position).getBill_city());
                                    sb.append('\n');
                                    sb.append("District : ");
                                    sb.append(accountlist.get(position).getBillingDistrict());
                                    sb.append('\n');
                                    sb.append("State : ");
                                    sb.append(accountlist.get(position).getBillingState());
                                    sb.append('\n');
                                    sb.append("Email : ");
                                    sb.append(accountlist.get(position).getEmail());
                                    sb.append('\n');
                                    sb.append("Mobile No. : ");
                                    sb.append(accountlist.get(position).getPhone());

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





        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isedit=false;
                Log.d("TASK ADAPTER", "onClick: Cardview");
                clickListener.myItemClick(position);
            }
        });


       // holder.sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
       // holder.sample1.setDragEdge(SwipeLayout.DragEdge.Right);
//        sample2.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.sample1.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editItemClickListner.myEditItemClick(position);
//            }
//        });

//        holder.sample1.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
////                Intent sendIntent = new Intent();
////                sendIntent.setAction(Intent.ACTION_SEND);
////                sendIntent.putExtra(Intent.EXTRA_TEXT,true);
////                sendIntent.setType("text/plain");
////                Intent.createChooser(sendIntent,"Share via");
////                mContext.startActivity(sendIntent);
//
//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                whatsappIntent.setType("text/plain");
//                whatsappIntent.setPackage("com.whatsapp");
//
//                Intent email = new Intent(Intent.ACTION_SEND);
//                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
//                email.putExtra(Intent.EXTRA_TEXT, "text");
//                Uri uri = Uri.parse("file://" + email.getAction());
//                email.putExtra(Intent.EXTRA_STREAM, uri);
//                email.setType("message/rfc822");
//                mContext.startActivity(email);
//                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
//                try {
//                    mContext.startActivity(whatsappIntent);
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(mContext,"Whatsapp have not been installed.",Toast.LENGTH_LONG).show();
//                }
//            }
//        });
}
    @Override
    public int getItemCount() {
        Log.d("accountsize",String.valueOf(accountlist.size()));
        return accountlist.size();


    }

//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.sample1;
//    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView buttonViewOption,account_manager,country, name, city, facility_type, bill_street, bill_city, bill_district, bill_state, bill_country, bill_pin, textViewOptions;
        SwipeLayout sample1;
        Button email, mobile;
        CardView cardView;
        ImageView share, tvEdit;
        LinearLayout cards;

        public MyViewHolder(View itemView) {
            super(itemView);
          //  sample1 = (SwipeLayout) itemView.findViewById(R.id.sample1);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);
            country = (TextView) itemView.findViewById(R.id.headingText);
            name = (TextView) itemView.findViewById(R.id.subHeaderText);
            city = (TextView) itemView.findViewById(R.id.subHeadingText);
            facility_type = (TextView) itemView.findViewById(R.id.facility_type);
            bill_street = (TextView) itemView.findViewById(R.id.bill_street);
            bill_city = (TextView) itemView.findViewById(R.id.bill_city);
            bill_district = (TextView) itemView.findViewById(R.id.bill_district);
            bill_state = (TextView) itemView.findViewById(R.id.bill_states);
            bill_pin = (TextView) itemView.findViewById(R.id.bill_pin);
            account_manager=(TextView)itemView.findViewById(R.id.account_manger);
            textViewOptions = (TextView) itemView.findViewById(R.id.textViewOptions);
            email = (Button) itemView.findViewById(R.id.email);
            mobile = (Button) itemView.findViewById(R.id.mobile);
            cardView = itemView.findViewById(R.id.cardView);

        }

    }
        public interface MyItemClickListener {
            void myItemClick(int position);
        }

        public interface MyEditItemClickListner {
            void myEditItemClick(int position);
        }



}


