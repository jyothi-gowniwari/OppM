package com.genworks.oppm.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.genworks.oppm.model.TaskModel;
import com.genworks.oppm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private LayoutInflater inflater;

    private Context mContext;
    private boolean isedit=false;
    private ArrayList<TaskModel> tasklist;
    private ArrayList<TaskModel> mFilteredList;

    public boolean isClickable = true;
    private MyItemClickListener clickListener;
    private MyEditItemClickListner editItemClickListner;


    public TaskAdapter(Context context, ArrayList<TaskModel> tasklist, MyItemClickListener clickListener, MyEditItemClickListner editItemClickListner) {
        mContext=context;
        this.tasklist=tasklist;
        this.clickListener = clickListener;
        this.mFilteredList=tasklist;
        this.editItemClickListner=editItemClickListner;
    }

    @Override
    public TaskAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_task, parent, false);
        TaskAdapter.MyViewHolder holder = new TaskAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final TaskAdapter.MyViewHolder holder, final int position) {


        holder.subject.setText(tasklist.get(position).getSubject());
        holder.task_type.setText(tasklist.get(position).getTaskType());
        holder.assigned_to.setText(tasklist.get(position).getAssigned());
        holder.related_to.setText(tasklist.get(position).getParent_id());
        holder.outcome.setText(tasklist.get(position).getOutcome());
        holder.status.setText(tasklist.get(position).getStatus());
        holder.location.setText(tasklist.get(position).getLocation());
        holder.schedule_date.setText(tasklist.get(position).getModifiedtime());
        holder.schedule_date.setBackgroundResource(R.color.password);
        holder.opportunity.setText(tasklist.get(position).getOpportunityNo());
        // holder.createdtime.setText(tasklist.get(position).getCreatedtime());//okay where you need your api respnose from preferfernce.//ok
//        holder.subject.setText(subject);
//        holder.task_type.setText(task_type);
//        holder.assigned_to.setText(assigned_to);
//        holder.outcome.setText(outcome);
//        holder.status.setText(status1);
//        holder.location.setText(location);
//        holder.schedule_date.setText(schedule_date);
//        holder.opportunity.setText(opportunity);
        final String status = tasklist.get(position).getStatus();
        final String outcomes=tasklist.get(position).getOutcome();
        Log.d("outcome",outcomes);


        if(outcomes.equals(" Follow-up ")){
            holder.image.setImageResource(R.drawable.followup);
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.linecolor));
        }else if(outcomes.equals(" Order Closed ")){
            holder.image.setImageResource(R.drawable.orderclosed);
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.linecolor));
        }else if(outcomes.equals(" Interested ")){
            holder.image.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        }else if(outcomes.equals(" Done ")){
            holder.image.setImageResource(R.drawable.done);
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.linecolor));
        }else if(outcomes.equals(" Collected ")){
            holder.image.setImageResource(R.drawable.collected);
            holder.image.setColorFilter(ContextCompat.getColor(mContext, R.color.linecolor));
        }else if(outcomes.equals(" Not Interested ")){
            holder.image.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }

        if(status.equals("Scheduled")){
            holder.status.setBackgroundResource(R.color.blue);
        } else if(status.equals("Completed")){
            holder.status.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        } else if(status.equals("In Complete")){
            holder.status.setBackgroundResource(R.color.red);
        }
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    // showPopupMenu(holder.buttonViewOption);
                    PopupMenu popup = new PopupMenu(mContext, view);
                    MenuInflater inflater = popup.getMenuInflater();
                    inflater.inflate(R.menu.menu_card, popup.getMenu());
                    // popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            isedit=true;
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    editItemClickListner.myEditItemClick(position);
                                    return true;
                                case R.id.share:
                                    Intent intent=new Intent(Intent.ACTION_SEND);
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Task Details : ");
                                    sb.append('\n');
                                    sb.append("Subject : ");
                                    sb.append(tasklist.get(position).getSubject());                                    sb.append('\n');
                                    sb.append("Related To : ");
                                    sb.append(tasklist.get(position).getParent_id());
                                    sb.append('\n');
                                    sb.append("Task Type : ");
                                    sb.append(tasklist.get(position).getTaskType());
                                    sb.append('\n');
                                    sb.append("Assigned To : ");
                                    sb.append(tasklist.get(position).getAssigned());
                                    sb.append('\n');
                                    sb.append("Schedule Date : ");
                                    sb.append(tasklist.get(position).getScheduleDate());
                                    sb.append('\n');
                                    sb.append("Location : ");
                                    sb.append(tasklist.get(position).getLocation());
                                    sb.append('\n');
                                    sb.append("Status : ");
                                    sb.append(tasklist.get(position).getStatus());
                                    sb.append('\n');
                                    sb.append("Outcome : ");
                                    sb.append(tasklist.get(position).getOutcome());
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

                    clickListener.myItemClick(position);


            }
        });


    }


    public void onScreenshot(View view) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            // create bitmap screen capture
            CardView cardView = (CardView) view.findViewById(R.id.cardView);;
            cardView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(cardView.getDrawingCache());
            cardView.setDrawingCacheEnabled(false);
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
            File imageFile = new File(mPath);
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        Log.d("Tasksize",String.valueOf(tasklist.size()));
        return tasklist.size();
    }


//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.sample1;
//    }

//    public void clear() {
//        int size = tasklist.size();
//        tasklist.clear();
//        notifyItemRangeRemoved(0, size);
//    }
//
//    @Override
//    public Filter getFilter() {
//
//        return new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence charSequence) {
//
//                String charString = charSequence.toString();
//
//                if (charString.isEmpty()) {
//
//                    mFilteredList = tasklist;
//                } else {
//
//                    ArrayList<TaskModel> filteredList = new ArrayList<>();
//
//                    for (TaskModel taskModel : tasklist) {
//
//                        if (taskModel.getAssigned().toLowerCase().contains(charString) || taskModel.getParent_id().toLowerCase().contains(charString)) {
//
//                            filteredList.add(taskModel);
//
//                        }
//                    }
//
//                    mFilteredList = filteredList;
//                }
//
//                FilterResults filterResults = new FilterResults();
//                filterResults.values = mFilteredList;
//                return filterResults;
//            }
//
//            @Override
//            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
//                mFilteredList = (ArrayList<TaskModel>) filterResults.values;
//                notifyDataSetChanged();
//            }
//        };
//    }



    class MyViewHolder extends RecyclerView.ViewHolder{

        SwipeLayout sample1;
        TextView buttonViewOption,subject,outcome, status,task_type,assigned_to,related_to,schedule_date,location,opportunity,bill_pin,textViewOptionscreatedtime,modifiedtime,modifiedby,remark,activitytype,textViewOptions,createdtime;
        ImageView image;
        CardView cardView;
        ImageView share,tvEdit;
        LinearLayout cards;


        public MyViewHolder(View itemView) {
            super(itemView);
           // sample1 = (SwipeLayout) itemView.findViewById(R.id.sample1);
            subject= (TextView) itemView.findViewById(R.id.subject);
            buttonViewOption=itemView.findViewById(R.id.textViewOptions);
            outcome = (TextView) itemView.findViewById(R.id.outcome);
            status = (TextView) itemView.findViewById(R.id.status);
            task_type = (TextView) itemView.findViewById(R.id.task_type);
            assigned_to = (TextView) itemView.findViewById(R.id.assigned_to);
            related_to = (TextView) itemView.findViewById(R.id.related_to);
            schedule_date = (TextView) itemView.findViewById(R.id.schedule_date);
            location = (TextView) itemView.findViewById(R.id.location);
            opportunity = (TextView) itemView.findViewById(R.id.opp_no);
            textViewOptions=(TextView)itemView.findViewById(R.id.textViewOptions);
            image=(ImageView)itemView.findViewById(R.id.image);
            share = (ImageView) itemView.findViewById(R.id.share);
            tvEdit = (ImageView) itemView.findViewById(R.id.edit);
            cardView = itemView.findViewById(R.id.cardView);
            // createdtime = (TextView) itemView.findViewById(R.id.createdtime);
//            modifiedtime = (TextView) itemView.findViewById(R.id.modifiedtime);
//            modifiedby = (TextView) itemView.findViewById(R.id.modifiedby);
//            activitytype=(TextView) itemView.findViewById(R.id.activitytype);
           // cards = (LinearLayout) itemView.findViewById(R.id.cards);


        }


    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        mContext.startActivity(intent);
    }

    public interface MyItemClickListener{
        void myItemClick(int position);
    }

    public interface MyEditItemClickListner{
        void myEditItemClick(int position);
    }
}

