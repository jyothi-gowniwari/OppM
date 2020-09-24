package com.genworks.oppm.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.genworks.oppm.model.ModelTest;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.FileCompressor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;


public class OpportunityAdapter extends RecyclerView.Adapter<OpportunityAdapter.MyViewHolder> {

    LayoutInflater inflater;
    Context mContext;
    List<ModelTest> list;
    Listener mlistener;
    private boolean iseditopportunity=false;
    private Uri realUri;
    private PopupWindow popupWindow;
    private Button close;
    private TextView no_file;
    FileCompressor mCompressor;
    private File mPhotoFile;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    private LinearLayout linearLayout;
    private Context context;
    private Object values;
    private ArrayList<ModelTest> lost_reasons;
    private MaterialSpinner spinnerlostreason;
    String mobie;
    private OpportunityAdapter.MyItemClickListener clickListener;
    private OpportunityAdapter.MyEditItemClickListner editItemClickListner;
    private OpportunityAdapter.MyPDfItemClickListner pdfItemClickListner;
    ArrayList<String> lostreasons=new ArrayList<>();


    public OpportunityAdapter(Context context, List<ModelTest> list,Listener listener,MyItemClickListener clickListener, MyEditItemClickListner editItemClickListner, MyPDfItemClickListner pdfItemClickListner) {
        this.mContext=context;
        this.list = list;
        this.mlistener=listener;
        this.clickListener = clickListener;
        this.editItemClickListner = editItemClickListner;
        this.pdfItemClickListner=pdfItemClickListner;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_opp, parent, false);
        OpportunityAdapter.MyViewHolder holder = new OpportunityAdapter.MyViewHolder(view);

        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ModelTest opportunity=list.get(position);
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


        holder.cardView.setTag(position);
//        });
//        holder.sample1.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editItemClickListner.myEditItemClick(position);
//            }
//        });
//        holder.sample1.setShowMode(SwipeLayout.ShowMode.PullOut);
//        holder.sample1.setDragEdges(SwipeLayout.DragEdge.Right);
//        holder.cardView.getParent().requestDisallowInterceptTouchEvent(true);
//        holder.cardView.setOnGenericMotionListener(new View.OnGenericMotionListener() {
//
//            @Override
//            public boolean onGenericMotion(View v, MotionEvent event) {
//                if(event.getAction()==MotionEvent.AXIS_TOUCH_MINOR) {
//
//                    return true;
//                }
//
//                return false;
//            }
//
//        });

        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                iseditopportunity=true;

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
                                sb.append("Opportunity Details : ");
                                sb.append('\n');
                                sb.append("Sales Stage : ");
                                sb.append(list.get(position).getSalesstageValue());
                                sb.append('\n');
                                sb.append("Account Name : ");
                                sb.append(list.get(position).getRelated());
                                sb.append('\n');
                                sb.append("Assigned To : ");
                                sb.append(list.get(position).getAssigned());
                                sb.append('\n');
                                sb.append("Contact : ");
                                sb.append(list.get(position).getContact());
                                sb.append('\n');
                                sb.append("Location : ");
                                sb.append(list.get(position).getLocation());
                                sb.append('\n');
                                sb.append("Product Value: ");
                                sb.append(list.get(position).getProductValue().concat("(")+list.get(position).getQty());
                                sb.append('\n');
                                sb.append("Potential No. : ");
                                sb.append(list.get(position).getPotentialNo());
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


        //  holder.cardView.setOnDragListener(new DragListener(mlistener));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TASK ADAPTER", "onClick: Cardview");
                iseditopportunity=false;
                clickListener.myItemClick(position);

            }
        });


        String modality_name = opportunity.getModality();
        if (modality_name.equals("LCS")||modality_name.equals("Ultrasound")||modality_name.equals("DI")) {
            // Toast.makeText(mContext, modality_name, Toast.LENGTH_LONG).show();
            holder.pdf.setVisibility(View.GONE);

        }else {
            holder.pdf.setVisibility(View.VISIBLE);
            holder.pdf.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pdfItemClickListner.myPDfItemClick(position);
                }
            });
        }


    }


//    public DragListener getDragInstance() {
//        if (mlistener != null) {
//            return new DragListener(mlistener);
//        } else {
//            Log.e("Route Adapter: ", "Initialize listener first!");
//            return null;
//        }
//    }

    @Override
    public int getItemCount() {
        Log.d("oppsize",String.valueOf(list.size()));
        return list.size();

    }
//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.sample1;
//    }
    public List<ModelTest> getCustomList() {
        return list;
    }

    public void updateCustomList(List<ModelTest> customList) {
        this.list = list;
    }

    public interface Listener {
        void setEmptyList(boolean visibility);
    }




    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView buttonViewOption,potential_name, account_name, contact_name,item_name,potential_no,amount,qty,location,win_prob,assigned_to,textoptions;
        Button email,pdf;
        CardView cardView;
        LinearLayout cards;
        ImageView share, tvEdit;
        SwipeLayout sample1;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);

            account_name = (TextView) itemView.findViewById(R.id.account_name);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);
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
            cardView = itemView.findViewById(R.id.cardView);
           // sample1 = (SwipeLayout) itemView.findViewById(R.id.sample1);
            image=(ImageView)itemView.findViewById(R.id.image);
            //share = (ImageView) itemView.findViewById(R.id.share);
            //tvEdit = (ImageView) itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardView);


        }


    }
    public interface MyItemClickListener {
        void myItemClick(int position);
    }

    public interface MyEditItemClickListner {
        void myEditItemClick(int position);
    }
    public interface MyPDfItemClickListner {
        void myPDfItemClick(int position);
    }



    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    }





