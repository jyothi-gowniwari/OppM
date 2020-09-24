package com.genworks.oppm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.genworks.oppm.model.ContactModel;
import com.genworks.oppm.R;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    String searchString="";
    private Context mContext;
    private boolean iseditcontact=false;
    private ArrayList<ContactModel> contactlist;

    public boolean isClickable = true;
    private ContactAdapter.MyItemClickListener clickListener;
    private ContactAdapter.MyEditItemClickListner editItemClickListner;

    public ContactAdapter(Context context, ArrayList<ContactModel> contactlist, ContactAdapter.MyItemClickListener clickListener, ContactAdapter.MyEditItemClickListner editItemClickListner) {
        mContext=context;
        this.contactlist=contactlist;
        this.clickListener = clickListener;
        this.editItemClickListner=editItemClickListner;
    }

    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_contact, parent, false);
        ContactAdapter.MyViewHolder holder = new ContactAdapter.MyViewHolder(view);
        return holder;
    }
    public void setFilter( ArrayList<ContactModel> contactlist,String searchString) {
        this.searchString=searchString;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(ContactAdapter.MyViewHolder holder, final int position) {

        holder.account_name.setText(contactlist.get(position).getAccounName());
        holder.specilization.setText(contactlist.get(position).getSpecialization());
        holder.job_title.setText(contactlist.get(position).getJobTitle());
        holder.contact_type.setText(contactlist.get(position).getContactType());
        holder.email.setText(contactlist.get(position).getEmailID());
        holder.mobile.setText(contactlist.get(position).getMobilePhone());
        holder.salutationtype.setText(contactlist.get(position).getSalutationtype());
        holder.firstname.setText(contactlist.get(position).getFirstname());
        holder.lastname.setText(contactlist.get(position).getLastname());

        final String mobile_number = contactlist.get(position).getMobilePhone();// always remember one thing you can create  and it's not the better way to create instance of activity or fragment to pass value only so

        holder.mobile.setOnClickListener(new OnClickListener() {

           @Override public void onClick(View v) {
               Intent callIntent = new Intent(Intent.ACTION_DIAL);
               callIntent.setData(Uri.parse("tel:" + mobile_number));
               mContext.startActivity(callIntent);
           }

       });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TASK ADAPTER", "onClick: Cardview");
                iseditcontact=false;
                clickListener.myItemClick(position);
            }
        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iseditcontact=true;

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
                                editItemClickListner.myEditItemClick(position);
                                return true;
                            case R.id.share:
                                Intent intent=new Intent(Intent.ACTION_SEND);
                                StringBuilder sb = new StringBuilder();
                                sb.append("Contact Details : ");
                                sb.append('\n');
                                sb.append("Contact Name : ");
                                sb.append(contactlist.get(position).getSalutationtype()+" "+contactlist.get(position).getFirstname()+" "+contactlist.get(position).getLastname());
                                sb.append('\n');
                                sb.append("Account Name : ");
                                sb.append(contactlist.get(position).getAccounName());
                                sb.append('\n');
                                sb.append("Specialization : ");
                                sb.append(contactlist.get(position).getSpecialization());
                                sb.append('\n');
                                sb.append("Job Title : ");
                                sb.append(contactlist.get(position).getJobTitle());
                                sb.append('\n');
                                sb.append("Contact Type : ");
                                sb.append(contactlist.get(position).getContactType());
                                sb.append('\n');
                                sb.append("Email : ");
                                sb.append(contactlist.get(position).getEmailID());
                                sb.append('\n');
                                sb.append("Mobile No. : ");
                                sb.append(contactlist.get(position).getMobilePhone());

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


//
//
//        holder.sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.sample1.setDragEdge(SwipeLayout.DragEdge.Right);
////        sample2.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.sample1.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editItemClickListner.myEditItemClick(position);
//            }
//        });
//
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
//                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
//                whatsappIntent.setType("text/plain");
//                whatsappIntent.setPackage("com.whatsapp");
//
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
        Log.d("contactsize",String.valueOf(contactlist.size()));
        return contactlist.size();
    }


//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.sample1;
//    }




    class MyViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout sample1;
        TextView buttonViewOption,country, account_name, job_title,specilization,contact_type,lastname,salutationtype,firstname,bill_country,bill_pin;
        Button email,mobile;
        CardView cardView;
        ImageView share,tvEdit;

        public MyViewHolder(View itemView) {
            super(itemView);
           // sample1 = (SwipeLayout) itemView.findViewById(R.id.sample1);
            account_name = (TextView) itemView.findViewById(R.id.account_name);
//            name = (TextView) itemView.findViewById(R.id.subHeaderText);
//            city = (TextView) itemView.findViewById(R.id.subHeadingText);
            specilization = (TextView) itemView.findViewById(R.id.specilization);
            job_title = (TextView) itemView.findViewById(R.id.job_title);
            contact_type = (TextView) itemView.findViewById(R.id.contact_type);
            email = (Button) itemView.findViewById(R.id.email);
            mobile = (Button) itemView.findViewById(R.id.mobile);
            salutationtype = (TextView) itemView.findViewById(R.id.text_states);
            firstname = (TextView) itemView.findViewById(R.id.firstname);
            lastname = (TextView) itemView.findViewById(R.id.lastname);
            share = (ImageView) itemView.findViewById(R.id.share);
            tvEdit = (ImageView) itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardView);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);


        }


    }


    public interface MyItemClickListener{
        void myItemClick(int position);
    }

    public interface MyEditItemClickListner{
        void myEditItemClick(int position);
    }
}
