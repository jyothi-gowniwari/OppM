package com.genworks.oppm.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.genworks.oppm.Adapter.TaskAdapter;
import com.genworks.oppm.Helper.SQLiteDB;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.model.TaskModel;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.PreferenceManagerTask;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskTodayFragement extends Fragment implements TaskAdapter.MyItemClickListener, TaskAdapter.MyEditItemClickListner {

    public TaskTodayFragement() {
        // Required empty public constructor
    }
    private TaskAdapter taskAdapter;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private RecyclerView recyclerViewTask;
    Fragment fragment;
    String sessionId;
    private boolean mSearchViewAdded = false;
    private int searchItem;
    private boolean searchActive = false;
    private WindowManager mWindowManager;
    private Button closebtn;
    private PopupWindow popupWindow;
    String mobie,name;
    boolean isall=false;
    boolean isincomplte=false;
    boolean isdate=false;
    Calendar calendar;
    Menu search_menu;
    MenuItem item_search;
    int year;
    private androidx.appcompat.widget.Toolbar searchtollbar;
    int month;
    int dayOfMonth;
    private boolean tabsSelected;
    private TaskModel taskModel;
    private ImageView image;
    private TextView resultView;
    private ArrayList<TaskModel> listTask;
    TextView subject, outcome, status_task, task_type, assigned_to, related_to, schedule_date, location, opportunity, bill_pin, createdtime, modifiedtime, modifiedby, remark, activitytype;
    Gson gson;
    private ProgressDialog progressDialog = null;
    private Object values;
    private Toolbar mToolbar;
    private LinearLayout linearLayout;
    private LinearLayout task_all,task_incomplete,task_schedueld,task_weekwise,task_today;
    private final Handler handler = new Handler();
    ProgressDialog progressDoalog;
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean isDuplicate;
    private boolean isFilter = false;
    private TextView incomplete,all,today,weekwise,scheduled;
    private String status,task_id;
    SQLiteDB sqlite_obj;
    private String ismenutoggle;
    private ViewPager viewPager;
    private TabLayout tabLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment
        //    ismenutoggle = getArguments().getString("ismenutoggle");
        // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Opportunity");
        View rootView = inflater.inflate(R.layout.task, container, false);
        setHasOptionsMenu(true);
        setSearchtollbar();


//        FloatingActionButton fb = rootView.findViewById(R.id.fab);
//        fb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isDuplicate = false;
//                Bundle args = new Bundle();
//                args.putString("isDuplicate", String.valueOf(isDuplicate));
//                fragment = new TaskCreateFragement();
//                fragment.setArguments(args);
//                loadFragment(fragment);
//            }
//        });

        //sqlite_obj = new SQLiteDB(getActivity());
        //Linearlayouts for tabs

//        task_all.setBackgroundColor(Color.rgb(31,104,120));
        recyclerViewTask = rootView.findViewById(R.id.recyclerOpportunity);
        listTask = new ArrayList<>();
        taskAdapter = new TaskAdapter(requireContext(), listTask, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewTask.setLayoutManager(linearLayoutManager);
        recyclerViewTask.setAdapter(taskAdapter);



        //cocnition means directly i will call this and i have a refresh menu item that i where your refresh butt
        String apiResponseStr = PreferenceManagerTask.getInstance(requireContext()).getAPIResponseTask(); // so you have to press your refresh buttonok

        // String apiResponseStr = PreferenceManager.getInstance(requireContext()).getAPIResponseAccount(); // so you have to press your refresh buttonok
        if (!apiResponseStr.equals("")){
            Gson gson = new Gson();
            SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
            listTask.clear();
            workingOnResponsedatewise(obj);
        }else {
            fetchJSON();
        }
        taskAdapter.notifyDataSetChanged();
        return rootView;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void fetchJSON() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.drawable.loader);
                progressDialog.show();

                sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "syncModuleRecords";
                String module = "Calendar";
                String syncToken = "";
                String mode = "";
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                /** Call the method with parameter in the interface to get the notice data*/
                Call<SyncModule> call = service.GetSyncModuleList(operation, sessionId, module, syncToken, mode);

                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");

                call.enqueue(new Callback<SyncModule>() {
                    @Override
                    public void onResponse(Call<SyncModule> call, Response<SyncModule> response) {


                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));

                            SyncModule syncModule = response.body();
                            Gson g = new Gson();
                            String json = g.toJson(syncModule);
                            PreferenceManagerTask.getInstance(requireContext()).setAPIResponseTask(json);

                            workingOnResponsedatewise(syncModule);
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<SyncModule> call, Throwable t) {
                        Log.d("error", t.getMessage());
                        progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;
    }

    @SuppressLint("ResourceAsColor")
    private void workingOnResponsedatewise(SyncModule syncModule){
        isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String value = "";
                String subject = "";
                String taskType = "";
                String assigned = "";
                String scheduleDates = "";
                String modifydatetime="";
                String scheduleDate = "";
                String location = "";
                String opportunityNo = "";
                status = "";
                String outcome = "";
                String createdtime = "";
                String modifiedtime = "";
                String modifiedby = "";
                String remark = "";
                String activitytype = "";
                String parent_id = "";

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Task Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();
                            if (name.equals("subject")) {
                                subject = String.valueOf(values);
                            } else if (name.equals("cf_956")) {
                                taskType = String.valueOf(values);
                            } else if (name.equals("parent_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//
                                    parent_id = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("assigned_user_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//
                                    assigned = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("potential")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    opportunityNo = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("modifiedby")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    modifiedby = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("date_start")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate =outputFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }


                            } else if (name.equals("taskstatus")) {
                                status = String.valueOf(values);

                            } else if (name.equals("location")) {
                                location = String.valueOf(values);


                            } else if (name.equals("cf_1015")) {
                                outcome = String.valueOf(values);

                            } else if (name.equals("cf_1013")) {
                                remark = String.valueOf(values);
                            } else if (name.equals("activitytype")) {
                                activitytype = String.valueOf(values);
                            } else if (name.equals("createdtime")) {
                                createdtime = String.valueOf(values);
                            } else if (name.equals("modifiedtime")) {
                                modifydatetime = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                Date date = null;
                                try {
                                    date = inputFormat.parse(modifydatetime);
                                    modifiedtime = outputFormat.format(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }
                    PreferenceManagerTask.getInstance(requireContext()).setMultipleData(subject, taskType, assigned, scheduleDate, location, opportunityNo, status, outcome);
                }

                TaskModel taskModel = new TaskModel(subject, taskType, assigned, scheduleDate, location, opportunityNo, status, " " + outcome + " ", createdtime, modifiedtime, activitytype, modifiedby, remark, parent_id,task_id);
//
                String date = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
                Log.d("date",date);
                if(modifiedtime.equals(date)) {
                    listTask.add(taskModel);
                    taskAdapter.notifyDataSetChanged();
                }
            }

        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ((AppCompatActivity)getActivity()).getBaseContext().setActionBarTitle("Create New Task");
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.search:

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    ((MainActivity)  getActivity()).circleReveal(R.id.searchtoolbar,1,true,true);
                else
                    searchtollbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
                return true;
            case R.id.refresh:
                listTask.clear();
                fetchJSON();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void myEditItemClick(int position) {

        isDuplicate = true;

        String subject_name = listTask.get(position).getSubject();
        String task_types = listTask.get(position).getTaskType();
        String assigneds = listTask.get(position).getAssigned();
        String scheduledates = listTask.get(position).getScheduleDate();
        String locations = listTask.get(position).getLocation();
        String opportunityNos = listTask.get(position).getOpportunityNo();
        String statuss = listTask.get(position).getStatus();
        String outcomes = listTask.get(position).getOutcome();
        String outcome=outcomes.trim();
        String created_time = listTask.get(position).getCreatedtime();
        String modified_time = listTask.get(position).getModifiedtime();
        String activity_type = listTask.get(position).getActivitytype();
        String modified_by = listTask.get(position).getModifiedby();
        String remarks = listTask.get(position).getRemark();
        String parent_id=listTask.get(position).getParent_id();
        String task_id=listTask.get(position).getTask_id();

        Fragment fragment = new TaskCreateFragement();
        Bundle args = new Bundle();
        args.putString("subject_name", subject_name);
        args.putString("task_types", task_types);
        args.putString("assigneds", assigneds);
        args.putString("parent_id", parent_id);
        args.putString("scheduledates", scheduledates);
        args.putString("locations", locations);
        Log.d("locations",locations);
        args.putString("outcomeedit",outcome);
        Log.d("outcomeedit",outcome);
        args.putString("task_id",task_id);
        Log.d("task_id",task_id);
        args.putString("statuss", statuss);
        args.putString("remarks", remarks);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);

    }


    @Override
    public void myItemClick(int position) {

        String subject_name = listTask.get(position).getSubject();
        String task_types = listTask.get(position).getTaskType();
        String assigneds = listTask.get(position).getAssigned();
        String scheduledates = listTask.get(position).getScheduleDate();
        String locations = listTask.get(position).getLocation();
        String opportunityNos = listTask.get(position).getOpportunityNo();
        String statuss = listTask.get(position).getStatus();
        String outcomes = listTask.get(position).getOutcome();
        String created_time = listTask.get(position).getCreatedtime();
        String modified_time = listTask.get(position).getModifiedtime();
        String activity_type = listTask.get(position).getActivitytype();
        String modified_by = listTask.get(position).getModifiedby();
        String remarks = listTask.get(position).getRemark();
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_task, null);

        linearLayout = customView.findViewById(R.id.card_details);
        popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        subject = (TextView) customView.findViewById(R.id.subject);
        outcome = (TextView) customView.findViewById(R.id.outcome);
        status_task = (TextView) customView.findViewById(R.id.status);
        task_type = (TextView) customView.findViewById(R.id.task_type);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        related_to = (TextView) customView.findViewById(R.id.related_to);
        schedule_date = (TextView) customView.findViewById(R.id.schedule_date);
        location = (TextView) customView.findViewById(R.id.location);
        opportunity = (TextView) customView.findViewById(R.id.opp_no);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        activitytype = (TextView) customView.findViewById(R.id.activitytype);
        remark = (TextView) customView.findViewById(R.id.remark);
        image = (ImageView) customView.findViewById(R.id.image);
        closebtn = (Button) customView.findViewById(R.id.close);
        subject.setText(subject_name);
        outcome.setText(outcomes);
        status_task.setText(statuss);
        task_type.setText(task_types);
        assigned_to.setText(assigneds);
        schedule_date.setText(scheduledates);
        location.setText(locations);
        opportunity.setText(opportunityNos);
        createdtime.setText(created_time);
        modifiedtime.setText(modified_time);
        modifiedby.setText(modified_by);
        activitytype.setText(activity_type);
        remark.setText(remarks);


        if (statuss.equals("Scheduled")) {
            status_task.setBackgroundResource(R.color.orange);
        } else if (statuss.equals("Completed")) {
            status_task.setBackgroundResource(R.color.green);
        } else if (statuss.equals("In Complete")) {
            status_task.setBackgroundResource(R.color.red);
        }
        if (outcomes.equals(" Follow-up ")) {
            image.setImageResource(R.drawable.followup);
            image.setColorFilter(ContextCompat.getColor(getContext(), R.color.linecolor));
        } else if (outcomes.equals(" Order Closed ")) {
            image.setImageResource(R.drawable.orderclosed);
            image.setColorFilter(ContextCompat.getColor(getContext(), R.color.linecolor));
        } else if (outcomes.equals(" Interested ")) {
            image.setImageResource(R.drawable.ic_thumb_up_black_24dp);
        } else if (outcomes.equals(" Done ")) {
            image.setImageResource(R.drawable.done);
            image.setColorFilter(ContextCompat.getColor(getContext(), R.color.linecolor));
        } else if (outcomes.equals(" Collected ")) {
            image.setImageResource(R.drawable.collected);
            image.setColorFilter(ContextCompat.getColor(getContext(), R.color.linecolor));
        } else if (outcomes.equals(" Not Interested ")) {
            image.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();  //merge 3 into single listitem all the details are same but only product changed/ actually i don't understand what you say.
                //see my opportunity list in the window first 3 list item names are chnadra mouli r u there?yes then want to display one time only
                //product want to chnage Falco 202,cs 30,Bs 200 based on GW no now what you want? 3 into single list if the GW no is same means yes ok. when you add you data from api means json to arraylist. that time you have to do that.

            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }

    // < ---- Implementing Search begins here ---- > //
    public void onSearch(String query){
        Log.d("Query From Activity",query);
        ArrayList<TaskModel> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listTask.size(); i++) {

            final String account_name = listTask.get(i).getAssigned().toLowerCase();
            final String contact_name = listTask.get(i).getParent_id().toLowerCase();

            if (account_name.contains(query)||contact_name.contains(query) ) {
                // make them also bold

                filteredList.add(listTask.get(i));
                //highlightText(query,jTitle);
                //  resultView.setVisibility(View.GONE);
                // listTask.get(i).setText(sb);
            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewTask.setLayoutManager(mLayoutManager);
        taskAdapter = new TaskAdapter(getContext(),filteredList,this,this);
        recyclerViewTask.setAdapter(taskAdapter);
        taskAdapter.notifyDataSetChanged();



    }


    public void setSearchtollbar()
    {
        searchtollbar = (androidx.appcompat.widget.Toolbar) getActivity().findViewById(R.id.searchtoolbar);
        if (searchtollbar != null) {
            searchtollbar.inflateMenu(R.menu.menu_search);
            search_menu=searchtollbar.getMenu();

            searchtollbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        circleReveal(R.id.searchtoolbar,1,true,false);
                    else
                        searchtollbar.setVisibility(View.GONE);
                }
            });

            item_search = search_menu.findItem(R.id.action_filter_search);

            MenuItemCompat.setOnActionExpandListener(item_search, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    // Do something when collapsed
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        circleReveal(R.id.searchtoolbar,1,true,false);
                    }
                    else
                        searchtollbar.setVisibility(View.GONE);
                    return true;
                }

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    // Do something when expanded
                    return true;
                }
            });

            initSearchView();


        } else
            Log.d("toolbar", "setSearchtollbar: NULL");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(true);
    }
    public void initSearchView()
    {
        final SearchView searchView =
                (SearchView) search_menu.findItem(R.id.action_filter_search).getActionView();

        searchView.setSubmitButtonEnabled(false);
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        closeButton.setImageResource(R.drawable.ic_close_black_24dp);

        // set hint and the text colors
        EditText txtSearch = ((EditText) searchView.findViewById(R.id.search_src_text));
        txtSearch.setHint("Search..");
        txtSearch.setHintTextColor(Color.DKGRAY);
        txtSearch.setTextColor(getResources().getColor(R.color.colorPrimary));

        // set the cursor
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        } catch (Exception e) {
            e.printStackTrace();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                callSearch(newText);
                return true;
            }

            public void callSearch(String query) {
                query = query.toLowerCase();
                Log.i("query", "" + query);
                onSearch(query);
            }

        });
    }
    // < ---- Implementing Search ends here ---- > //
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, boolean containsOverflow, final boolean isShow)
    {
        final View myView = getActivity().findViewById(viewID);

        int width=myView.getWidth();

        if(posFromRight>0)
            width-=(posFromRight*getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material))-(getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)/ 2);
        if(containsOverflow)
            width-=getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx=width;
        int cy=myView.getHeight()/2;

        Animator anim;
        if(isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0,(float)width);
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float)width, 0);

        anim.setDuration((long)220);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if(!isShow)
                {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if(isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        anim.start();


    }

    public static CharSequence highlightText(String search, String originalText) {
        if (search != null && !search.equalsIgnoreCase("")) {
            String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();
            int start = normalizedText.indexOf(search);
            if (start < 0) {
                return originalText;
            } else {
                Spannable highlighted = new SpannableString(originalText);
                while (start >= 0) {
                    int spanStart = Math.min(start, originalText.length());
                    int spanEnd = Math.min(start + search.length(), originalText.length());
                    highlighted.setSpan(new ForegroundColorSpan(Color.BLUE), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = normalizedText.indexOf(search, spanEnd);
                }
                return highlighted;
            }
        }
        return originalText;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 4) {
//
//            listTask = repository.consDataBase(this);
//            taskAdapter.notifyDataSetChanged();
//        }
//    }
}


