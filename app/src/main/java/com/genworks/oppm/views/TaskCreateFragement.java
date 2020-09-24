package com.genworks.oppm.views;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.genworks.oppm.Adapter.CustomAdapter;
import com.genworks.oppm.model.AccountDescribe;
import com.genworks.oppm.model.DescribeDetails;
import com.genworks.oppm.model.Fields;
import com.genworks.oppm.model.LocationModule;
import com.genworks.oppm.model.LocationRecords;
import com.genworks.oppm.model.PickListValues;
import com.genworks.oppm.model.Records;
import com.genworks.oppm.model.RecordsAccounts;
import com.genworks.oppm.model.RecordsContacts;
import com.genworks.oppm.model.RecordsOpportunity;
import com.genworks.oppm.model.Results_Account;
import com.genworks.oppm.model.Results_AccountQ;
import com.genworks.oppm.model.Results_ContactQ;
import com.genworks.oppm.model.Results_Locations;
import com.genworks.oppm.model.Results_OpportunityQ;
import com.genworks.oppm.model.SaveContactModule;
import com.genworks.oppm.model.TaskModel;
import com.genworks.oppm.model.TypeDetails;
import com.genworks.oppm.model.UserAccountQuery;
import com.genworks.oppm.model.UserContactQuery;
import com.genworks.oppm.model.UserOpportunityQuery;
import com.genworks.oppm.model.Users;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.TinyDB;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TaskCreateFragement extends Fragment{

    private TextInputEditText subject,remark,schedule_date,email;
    private Button cancel;
    private boolean isonetimelogin=false;
    private String username,isDuplicate,contactnames;
    private SharedPreferences sharedPreferences;
    private AppCompatAutoCompleteTextView autoaccount_name,autoconatct_name,autoopportunity_name;
    private ProgressDialog progressDialog = null;
    private ArrayList<String> location_list,outcome_list,task_typelist,status_list,schedule_datelist,assigned_list;
    private MaterialSpinner assigned_to;
    private Fragment fragment;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    TinyDB tinydb,tinystatus;
    EditText editTextUserName;
    private String firstname,lastname,task_id,strDate,time_start,locationids,account_id,contact_id,contactfirstnames,contactlastnames,opportunityids,opportunitynames;
    private Object values;
    private ArrayList<PickListValues> outcomelist=new ArrayList<>();
    private ArrayList<PickListValues> statuslist=new ArrayList<>();
    private CustomAdapter adapter;
    private AppCompatButton save;
    private PopupWindow popupWindow;
    private LinearLayout linearLayout;
    private ArrayList<String> account_manger,account_name,contact_name,opportunity_name;
    private ArrayList<RecordsAccounts> recordsListAccount=new ArrayList<>();
    private ArrayList<RecordsOpportunity> recordsListOpportunity=new ArrayList<>();
    private ArrayList<RecordsContacts> recordsListContact=new ArrayList<>();
    private ArrayList<PickListValues> pickListTask=new ArrayList<>();
    private ArrayList<Records> records;
    private ArrayList<LocationRecords> locationRecords;
    private MaterialSpinner spinnertaskType,spinneraccountManager,spinnerstatus,spinnerlocation,spinneroutcome;
    private ArrayList<String> taskTypes,status,location,outcome;
    private ListView lv;
    private ArrayList<RecordsAccounts> recordsAccounts;
    private ArrayList<RecordsContacts> recordsContacts;
    private ArrayList<RecordsOpportunity> recordsOpportunities;
    // Listview Adapter

    TextInputEditText inputSearch;
    RecyclerView recyclerViewAccount,recyclerViewContact,recyclerViewOpportunity;
    EditText searchContact,searchAccount,searchOpportunity;
    ImageView Add_account;
    Calendar calendar;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    private ArrayList<Records> recordsList=new ArrayList<>();
    private ArrayList<LocationRecords> locationList=new ArrayList<>();
    private String userids,assigned_id,edit_parentid,sessionId,ids,task_type_value,accountnames,outcome_name,status_name,assigned,edit_location,edit_outcome,edit_status,edit_scheduledate;
    private Button closebtn;
    private ImageView calendar1;
    DatePickerDialog datePickerDialog;
    int year;
    private String task_types;
    int month;
    private ImageView add_account,add_contact,add_opportunity;
    int dayOfMonth;
    private ArrayList<TaskModel> tasklist;
    private LinearLayout account,contact,opportunity,outcomes;
    private final Handler handler = new Handler ( );
    private ArrayList<Users> usersListAccount=new ArrayList<>();






    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment
        isDuplicate = getArguments().getString("isDuplicate");
        if(isDuplicate.equals("false")) {
            ((MainActivity)getActivity()).setActionBarTitle("CREATE NEW TASK");
        }
        final View rootView = inflater.inflate(R.layout.task_form, container, false);
        sharedPrefs = getActivity().getSharedPreferences(LoginActivity.MyPREFERENCES,
                Context.MODE_PRIVATE);
        subject=rootView.findViewById(R.id.subject);
        subject.setHint(Html.fromHtml ( subject.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinneraccountManager=rootView.findViewById(R.id.assigned_to);
        autoaccount_name=rootView.findViewById(R.id.account_name);
        autoconatct_name=rootView.findViewById(R.id.contact_name);
        autoopportunity_name=rootView.findViewById(R.id.opportunity_name);
        spinnertaskType=rootView.findViewById(R.id.task_type);
        remark=rootView.findViewById(R.id.remark);
        add_account=rootView.findViewById(R.id.add_account);
        add_contact=rootView.findViewById(R.id.add_contact);
        add_opportunity=rootView.findViewById(R.id.add_opportunity);
        remark.setHint(remark.getHint());
        spinnerlocation=rootView.findViewById(R.id.location);
        spinnerstatus=rootView.findViewById(R.id.status);
        username = getActivity().getIntent().getStringExtra("username");
        schedule_date=rootView.findViewById(R.id.schedule_date);
        schedule_date.setHint(Html.fromHtml ( schedule_date.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        cancel=rootView.findViewById(R.id.cancel);
        account=rootView.findViewById(R.id.account);
        contact=rootView.findViewById(R.id.contact);
        opportunity=rootView.findViewById(R.id.opportunity);
        outcomes=rootView.findViewById(R.id.outcome);
        spinneroutcome=rootView.findViewById(R.id.outcomes);
        calendar1=rootView.findViewById(R.id.calendarGridView);
        save=rootView.findViewById(R.id.save);

        spinnertaskType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnertaskType.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerlocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerlocation.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerstatus.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerstatus.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinneroutcome.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinneroutcome.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinneraccountManager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinneraccountManager.getWindowToken(), 0);
                return false;
            }
        }) ;

        save.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(save.getWindowToken(), 0);
                return false;
            }
        }) ;
        cancel.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(cancel.getWindowToken(), 0);
                return false;
            }
        }) ;

        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
            Log.d("session_IdMAin",sessionId);
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }

        add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Account");
                fragment = new AccountCreateFragement();
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });
        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Contact");
                fragment = new ContactCreateFragement();
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });

        add_opportunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Opportunity");
                fragment = new OpportunityCreateFragment();
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });
        calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        strDate = mdformat.format(calendar.getTime());

       // Log.d("strdadte",strDate);

        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        schedule_date.setText(date);
        calendar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


                                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                schedule_date.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        @SuppressLint("ResourceAsColor")
        ColorStateList colorStateList = ColorStateList.valueOf(R.color.black);
        ViewCompat.setBackgroundTintList(subject, colorStateList);
        ViewCompat.setBackgroundTintList(remark, colorStateList);
        ViewCompat.setBackgroundTintList(schedule_date, colorStateList);
        ViewCompat.setBackgroundTintList(autoaccount_name,colorStateList);
        ViewCompat.setBackgroundTintList(autoconatct_name,colorStateList);
        ViewCompat.setBackgroundTintList(autoopportunity_name, colorStateList);


        tinydb = new TinyDB(getContext());

        taskTypes=new ArrayList<>();

        status=new ArrayList<>();

        location_list=new ArrayList<>();
        outcome_list=new ArrayList<>();
        account_manger=new ArrayList<String>();

        tinystatus=new TinyDB(getContext());
        account_name=new ArrayList<>();
        contact_name=new ArrayList<>();
        opportunity_name=new ArrayList<>();

Log.d("isDuplicate",isDuplicate);
        firstname=getActivity().getIntent().getStringExtra("firstname");
        lastname=getActivity().getIntent().getStringExtra("lastname");
        if (isDuplicate.equals("true")) {

            String subject_name = getArguments().getString("subject_name");
            String remarks = getArguments().getString("remarks");
            task_types = getArguments().getString("task_types");
            edit_location = getArguments().getString("locations");
            Log.d("locations",edit_location);
            subject.setText(subject_name);
            edit_status = getArguments().getString("statuss");
            edit_outcome=getArguments().getString("outcomeedit");
            Log.d("edit_outcome",edit_outcome);
            assigned = getArguments().getString("assigneds");
            edit_scheduledate = getArguments().getString("scheduledates");
            schedule_date.setText(edit_scheduledate);
            remark.setText(remarks);
            edit_parentid=getArguments().getString("parent_id");
            task_id=getArguments().getString("task_id");
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new TaskFragement();
                loadFragment(fragment);

            }
        });

        String apiResponseStr = tinydb.getString("jsonTask");
        String apiResponseLocation=tinydb.getString("jsonLocation");
        String apiResponseAccount=tinydb.getString("jsonAccount");
        String apiResponseContact=tinydb.getString("jsonContact");
        String apiResponseOpportunity=tinydb.getString("jsonOpportunity");
        if (!apiResponseStr.equals("")){
            Gson gson = new Gson();
            AccountDescribe obj = gson.fromJson(apiResponseStr, AccountDescribe.class);
            workingOnResponsespinners(obj);
        }
        if (!apiResponseLocation.equals("")){
            Gson gson = new Gson();
            LocationModule locationModule = gson.fromJson(apiResponseLocation, LocationModule.class);
            workingOnResponseLocation(locationModule);
        }
        if (!apiResponseAccount.equals("")){
            Gson gson = new Gson();
            UserAccountQuery userAccountQuery = gson.fromJson(apiResponseAccount, UserAccountQuery.class);
            workingOnResponseAccount(userAccountQuery);
        }if (!apiResponseContact.equals("")){
            Gson gson = new Gson();
            UserContactQuery userContactQuery = gson.fromJson(apiResponseContact, UserContactQuery.class);
            workingOnResponseContact(userContactQuery);
        }if (!apiResponseOpportunity.equals("")){
            Gson gson = new Gson();
            UserOpportunityQuery userOpportunityQuery = gson.fromJson(apiResponseOpportunity, UserOpportunityQuery.class);
            workingOnResponseOpportunity(userOpportunityQuery);
        }
        else {
            fetchJSON();
            fetchLocationJSON();
            fetchAccountJSON();
            fetchContactJSON();
            fetchOpportunityJSON();
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(task_id!=null){
                    updaterecords();
                }else {
                    saverecords();
                }

            }
        });



        return rootView;
    }

    private void display(String num) {
       // TextView textView = (TextView) findViewById(R.id.current_time_view);
        //textView.setText(num);
        time_start=num;
        Log.d("num",num);
    }

    private void fetchAccountJSON(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

               // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "query";
                String query = "select  *  from Accounts";
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
                /** Call the method with parameter in the interface to get the notice data*/
                Call<UserAccountQuery> call = service.UserAccountRecordDetails(operation, sessionId, query);
                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");
                call.enqueue(new Callback<UserAccountQuery>() {
                    @Override
                    public void onResponse(Call<UserAccountQuery> call, Response<UserAccountQuery> response) {
                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));
                            UserAccountQuery userAccountQuery = response.body();

                            Gson g = new Gson();
                            String jsonAccount = g.toJson(userAccountQuery);
                            tinydb.putString("jsonAccount",jsonAccount);
                            workingOnResponseAccount(userAccountQuery);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserAccountQuery> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return ;
    }

    private void workingOnResponseAccount(UserAccountQuery userAccountQuery) {
        String success = userAccountQuery.getSuccess();
        if (success.equals("true")) {
            Results_AccountQ results = userAccountQuery.getResult();
            // records = results.getRecords();
            recordsAccounts=results.getRecordsAccounts();
            for (final RecordsAccounts recordsAccounts1 : recordsAccounts) {
                account_id = recordsAccounts1.getId();
                accountnames = recordsAccounts1.getAccountname();
                account_name.add(accountnames);
                RecordsAccounts recordsAccounts = new RecordsAccounts(account_id, accountnames);
                recordsListAccount.add(recordsAccounts);
            }

                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, account_name);
                autoaccount_name.setAdapter(adapter);
                  autoaccount_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                      @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                          accountnames=adapter.getItem(position);
                                for(RecordsAccounts recordsAccounts2:recordsListAccount) {
                                    if (accountnames.equals(recordsAccounts2.getAccountname())){
                                        account_id = recordsAccounts2.getId();
                                    Toast.makeText(getContext(), account_id, Toast.LENGTH_LONG).show();
                                }
                                }
                          }


                  });

        }
    }


    private void fetchContactJSON(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "query";
                String query = "select  *  from Contacts";
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
                /** Call the method with parameter in the interface to get the notice data*/
                Call<UserContactQuery> call = service.UserContactRecordDetails(operation, sessionId, query);
                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");
                call.enqueue(new Callback<UserContactQuery>() {
                    @Override
                    public void onResponse(Call<UserContactQuery> call, Response<UserContactQuery> response) {
                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));
                            UserContactQuery userContactQuery = response.body();

                            Gson g = new Gson();
                            String jsonContact = g.toJson(userContactQuery);
                            tinydb.putString("jsonContact",jsonContact);
                            workingOnResponseContact(userContactQuery);

                        }
                    }

                    @Override
                    public void onFailure(Call<UserContactQuery> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return ;
    }

    private void workingOnResponseContact(UserContactQuery userContactQuery) {
        String success = userContactQuery.getSuccess();
        if (success.equals("true")) {
            Results_ContactQ results = userContactQuery.getResult();
            // records = results.getRecords();
            recordsContacts=results.getRecordsContacts();
            for (final RecordsContacts recordsContacts1 : recordsContacts) {
                contact_id = recordsContacts1.getId();
                contactfirstnames = recordsContacts1.getFirstname();
                contactlastnames = recordsContacts1.getLastname();
                final RecordsContacts recordsContacts = new RecordsContacts(contact_id, contactfirstnames, contactlastnames);
                recordsListContact.add(recordsContacts);
                contact_name.add(contactfirstnames.concat("").concat(contactlastnames));
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, contact_name);
            autoconatct_name.setAdapter(adapter);
            autoconatct_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    contactnames=adapter.getItem(position);
                    for(RecordsContacts recordsContacts:recordsListContact) {
                        if (contactnames.equals(recordsContacts.getLastname().concat(recordsContacts.getFirstname()))){
                            contact_id = recordsContacts.getId();
                            Toast.makeText(getContext(), contact_id, Toast.LENGTH_LONG).show();
                        }
                    }
                }


            });
        }
    }



    private void fetchOpportunityJSON(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

               // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "query";
                String query = "select  *  from Potentials";
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
                /** Call the method with parameter in the interface to get the notice data*/
                Call<UserOpportunityQuery> call = service.UserOpportunityRecordDetails(operation, sessionId, query);
                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");
                call.enqueue(new Callback<UserOpportunityQuery>() {
                    @Override
                    public void onResponse(Call<UserOpportunityQuery> call, Response<UserOpportunityQuery> response) {
                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));
                            UserOpportunityQuery userOpportunityQuery = response.body();

                            Gson g = new Gson();
                            String jsonOpportunity = g.toJson(userOpportunityQuery);
                            tinydb.putString("jsonOpportunity",jsonOpportunity);
                            workingOnResponseOpportunity(userOpportunityQuery);

                        }
                    }

                    @Override
                    public void onFailure(Call<UserOpportunityQuery> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return ;
    }

    private void workingOnResponseOpportunity(UserOpportunityQuery userOpportunityQuery) {
        String success = userOpportunityQuery.getSuccess();
        if (success.equals("true")) {
            Results_OpportunityQ results = userOpportunityQuery.getResult();
            // records = results.getRecords();
            recordsOpportunities=results.getRecordsOpportunities();
            for (final RecordsOpportunity recordsOpportunity : recordsOpportunities) {

                opportunityids = recordsOpportunity.getId();
                opportunitynames = recordsOpportunity.getPotentialname();


                RecordsOpportunity recordsOpportunity1=new RecordsOpportunity(opportunityids, opportunitynames);
                recordsListOpportunity.add(recordsOpportunity1);
                opportunity_name.add(opportunitynames);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, opportunity_name);

                autoopportunity_name.setAdapter(adapter);
                autoopportunity_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(position+1 > 0) {


                            RecordsOpportunity recordsOpportunity1 = recordsListOpportunity.get(position);
                            opportunityids = recordsOpportunity1.getId();
//                            Toast.makeText
//                                    (getContext(), "Selected : " + opportunityids, Toast.LENGTH_SHORT)
//                                    .show();
                            Log.e("opportunityids", opportunityids);
                            opportunitynames = recordsOpportunity1.getPotentialname();

                            autoopportunity_name.setText(opportunitynames);
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(autoopportunity_name.getWindowToken(), 0);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




            }
        }
    }


    private void saverecords(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                if (subject.getText().toString().isEmpty()) {
                    subject.setError("Please Enter Subject");
                } else if (spinnertaskType.getSelectedItem() == null) {
                    spinnertaskType.setError("Please Select an option");
                }else if (spinneraccountManager.getSelectedItem() == null) {
                    spinneraccountManager.setError("Please Select an option");
                }else if (spinnerstatus.getSelectedItem() == null) {
                    spinnerstatus.setError("Please Select an option");
                }else if (spinnerlocation.getSelectedItem() == null) {
                    spinnerlocation.setError("Please Select an option");
                } else {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                  //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "saveRecord";
                    String module = "Calendar";
                    String subject_name = subject.getText().toString();
                    String account_manager = assigned_id;
                    String schedule_dates = schedule_date.getText().toString();
                    String status = status_name;
                    String location = locationids;
                    String outcome = outcome_name;
                    String due_date = schedule_date.getText().toString();
                    String task_type = task_type_value;
                    String opportunity_ids = opportunityids;
                    String contact_ids = contact_id;
                    String account_ids = account_id;

                    JSONObject postdata = new JSONObject();
                    try {
                        postdata.put("subject", subject_name);
                        Log.d("subject",subject_name);
                        postdata.put("assigned_user_id", account_manager);
                        Log.d("assigned_user_id", String.valueOf(account_manager));
                        postdata.put("date_start", schedule_dates);
                        Log.d("date_start", schedule_dates);
                        postdata.put("taskstatus", status);
                        Log.d("taskstatus", String.valueOf(status));
                        postdata.put("location", location);
                        Log.d("location", String.valueOf(location));
                        postdata.put("cf_1015", outcome);
                        Log.d("cf_1015", String.valueOf(outcome));
                        postdata.put("time_start", strDate);
                        Log.d("time_start", String.valueOf(strDate));
                        postdata.put("due_date", due_date);
                        Log.d("due_date", String.valueOf(due_date));
                        postdata.put("cf_956", task_type);
                        Log.d("cf_956", String.valueOf(task_type));
                        postdata.put("activitytype", "Task");
                        Log.d("activitytype", "Task");
                        postdata.put("visibility", "Private");
                        Log.d("visibility", "Private");
                        if(opportunity_ids!=null) {
                            postdata.put("potential", opportunity_ids);
                            Log.d("potential", String.valueOf(opportunity_ids));
                        }
                        if(contact_ids!=null) {
                            postdata.put("contactid", contact_ids);
                            Log.d("contactid", String.valueOf(contact_ids));
                        }
                        if(account_ids!=null) {
                            postdata.put("parent_id", account_ids);
                            Log.d("parent_id", String.valueOf(account_ids));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                    /** Call the method with parameter in the interface to get the notice data*/
                    Call<SaveContactModule> call = service.SaveContactDetails(operation, sessionId, module, postdata);

                    /**Log the URL called*/
                    Log.i("URL Called", call.request().url() + "");

                    call.enqueue(new Callback<SaveContactModule>() {
                        @Override
                        public void onResponse(Call<SaveContactModule> call, Response<SaveContactModule> response) {


                            Log.e("response", new Gson().toJson(response.body()));
                            if (response.isSuccessful()) {
                                Log.e("response", new Gson().toJson(response.body()));

                                SaveContactModule accountDescribe = response.body();


                                String success = accountDescribe.getSuccess();

                                if (success.equals("true")) {

                                    progressDialog.dismiss();

                                    Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_LONG).show();
                                    ((MainActivity)getActivity()).setActionBarTitle("TASK LIST");
                                    fragment = new TaskFragement();
                                    loadFragment(fragment);
                                }else if(success.equals("false")){

                                    String message="Duplicates detected";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Invalid details", Toast.LENGTH_LONG).show();
                                }
//                                        SharedPreferences pref = PreferenceManager
//                                                .getDefaultSharedPreferences(getContext());
//                                        SharedPreferences.Editor editor = pref.edit();
//                                        editor.putString("subject", subject);
//                                        editor.putString("taskType", taskType);
//                                        editor.putString("assigned", assigned);
//                                        editor.putString("scheduleDate", scheduleDate);
//                                        editor.putString("location", location);
//                                        editor.putString("opportunityNo", opportunityNo);
//                                        editor.putString("status", status);
//                                        editor.putString("outcome", outcome);
//                                        editor.commit();


                            }
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onFailure(Call<SaveContactModule> call, Throwable t) {
                            Log.d("error", t.getMessage());
                            // progressDialog.dismiss();
                        }
                    });
                }
            }
        }, 0);
        return ;
    }


    private void updaterecords(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "saveRecord";
                String module = "Calendar";
                String subject_name=subject.getText().toString();
                String account_manager=assigned_id;
                String record=task_id;
                String schedule_dates=schedule_date.getText().toString();
                String status=status_name;
                String location=locationids;
                String outcome=outcome_name;
                String due_date=schedule_date.getText().toString();
                String task_type=task_type_value;
                String opportunity_ids=opportunityids;
                String contact_ids=contact_id;
                String account_ids=account_id;

                JSONObject postdata=new JSONObject();
                try {
                    postdata.put("subject",subject_name);
                    Log.d("subject", String.valueOf(subject));
                    postdata.put("assigned_user_id",account_manager);
                    Log.d("assigned_user_id", String.valueOf(account_manager));
                    postdata.put("date_start",schedule_dates);
                    Log.d("date_start", schedule_dates);
                    postdata.put("taskstatus",status);
                    Log.d("taskstatus", String.valueOf(status));
                    postdata.put("location",location);
                    Log.d("location", String.valueOf(location));
                    postdata.put("cf_1015",outcome);
                    Log.d("cf_1015", String.valueOf(outcome));
                    postdata.put("time_start",strDate);
                    Log.d("time_start", String.valueOf(strDate));
                    postdata.put("due_date",due_date);
                    Log.d("due_date", String.valueOf(due_date));
                    postdata.put("cf_956",task_type);
                    Log.d("cf_956", String.valueOf(task_type));
                    postdata.put("activitytype","Task");
                    Log.d("activitytype", "Task");
                    postdata.put("visibility","Private");
                    Log.d("visibility", "Private");
                    postdata.put("potential",opportunity_ids);
                    Log.d("potential", String.valueOf(opportunity_ids));
                    postdata.put("contactid",contact_ids);
                    Log.d("contactid", String.valueOf(contact_ids));
                    postdata.put("parent_id",account_ids);
                    Log.d("parent_id", String.valueOf(account_ids));
                    postdata.put("parent_id",account_ids);
                    Log.d("parent_id", String.valueOf(account_ids));




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                /** Call the method with parameter in the interface to get the notice data*/
                Call<SaveContactModule> call = service.UpdateDetails(operation, sessionId, module,record,postdata);

                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");

                call.enqueue(new Callback<SaveContactModule>() {
                    @Override
                    public void onResponse(Call<SaveContactModule> call, Response<SaveContactModule> response) {
                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));

                            SaveContactModule accountDescribe = response.body();


                            String success = accountDescribe.getSuccess();

                            if (success.equals("true")) {

                                progressDialog.dismiss();

                                Toast.makeText(getContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                                fragment = new TaskFragement();
                                loadFragment(fragment);
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(),"Invalid details",Toast.LENGTH_LONG).show();
                            }
//                                        SharedPreferences pref = PreferenceManager
//                                                .getDefaultSharedPreferences(getContext());
//                                        SharedPreferences.Editor editor = pref.edit();
//                                        editor.putString("subject", subject);
//                                        editor.putString("taskType", taskType);
//                                        editor.putString("assigned", assigned);
//                                        editor.putString("scheduleDate", scheduleDate);
//                                        editor.putString("location", location);
//                                        editor.putString("opportunityNo", opportunityNo);
//                                        editor.putString("status", status);
//                                        editor.putString("outcome", outcome);
//                                        editor.commit();





                        }
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onFailure(Call<SaveContactModule> call, Throwable t) {
                        Log.d("error", t.getMessage());
                        // progressDialog.dismiss();
                    }
                });
            }
        }, 0);
        return ;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }



    private void fetchLocationJSON(){
        spinnerlocation.setHint(Html.fromHtml("Location" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             //   sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "query";
                String query = "select  *  from Location";
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
                /** Call the method with parameter in the interface to get the notice data*/
                Call<LocationModule> call = service.LocationDetails(operation, sessionId, query);
                /**Log the URL called*/
                Log.i("URL Called", call.request().url() + "");
                call.enqueue(new Callback<LocationModule>() {
                    @Override
                    public void onResponse(Call<LocationModule> call, Response<LocationModule> response) {
                        Log.e("response", new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            Log.e("response", new Gson().toJson(response.body()));
                            LocationModule locationModule = response.body();
                            Gson g = new Gson();
                            String jsonLocation = g.toJson(locationModule);
                            tinydb.putString("jsonLocation",jsonLocation);
                            workingOnResponseLocation(locationModule);
                        }
                    }

                    @Override
                    public void onFailure(Call<LocationModule> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return ;
    }

    private void workingOnResponseLocation(LocationModule locationModule) {
        spinnerlocation.setHint(Html.fromHtml("Location" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        String success = locationModule.getSuccess();
        if (success.equals("true")) {
            Results_Locations results = locationModule.getResult();
            locationRecords = results.getRecords();
            for (final LocationRecords locationRecords1 : locationRecords) {
                String circle = locationRecords1.getLocation_tks_circle();

                String id = locationRecords1.getId();
                String district = locationRecords1.getLocation_tks_district();
                String locationno = locationRecords1.getLocationno();
                Log.d("circle", String.valueOf(circle.length()));
                LocationRecords locationRecords = new LocationRecords(locationno, district, circle, id);
                locationList.add(locationRecords);
                location_list.add(district.concat(" ").concat(",").concat(circle));
            }

            tinydb.putListString("locations",location_list);

            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                    getContext(), R.layout.spinner_item, location_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                //spinnerlocation.setAdapter(dataAdapter);


                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {
                        // Disable the first item from Spinner
                        // First item will be use for hint
                        // spinnerlocation.setPrompt("Select");

                        return false;
                    } else {
                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, View convertView,
                                            ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;

                    if (position + 1 == 0) {
                        // Set the hint text color gray

                        tv.setTextColor(Color.GRAY);
                    } else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.borderdashboard.simple_spinner_item, taskTypes);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerlocation.setAdapter(spinnerArrayAdapter);
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerlocation.getAdapter();
            int position = adapter.getPosition(edit_location);
            spinnerArrayAdapter.notifyDataSetChanged();
            spinnerlocation.setSelection(position + 1);
            spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position+1>0) {
                        //    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        //  ((TextView) parent.getChildAt(0)).setText("Assigned To");
                        LocationRecords locationRecords = locationList.get(position);

                        locationids = locationRecords.getId();
                        String[] moduleid = locationids.split("x");
                        String moduleids = moduleid[0];
                        locationids = moduleid[1];
                        Log.e("locationids", locationids);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }


    private void fetchJSON() {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "describe";
                    String module = "Calendar";
                    final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                    /** Call the method with parameter in the interface to get the notice data*/
                    Call<AccountDescribe> call = service.GetFieldDetailsList(operation, sessionId, module);

                    /**Log the URL called*/
                    Log.i("URL Called", call.request().url() + "");


                    call.enqueue(new Callback<AccountDescribe>() {
                        @Override
                        public void onResponse(Call<AccountDescribe> call, Response<AccountDescribe> response) {
                            Log.e("response", new Gson().toJson(response.body()));
                            if (response.isSuccessful()) {
                                Log.e("response", new Gson().toJson(response.body()));
                                AccountDescribe accountDescribe = response.body();
                                Gson g = new Gson();
                                String jsonTask = g.toJson(accountDescribe);
                                tinydb.putString("jsonTask",jsonTask);
                                workingOnResponsespinners(accountDescribe);
                            }
                            //   progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<AccountDescribe> call, Throwable t) {
                            Log.d("error", t.getMessage());
                            //    progressDialog.dismiss();
                        }


                    });

                }
                //  }


            }, 0);
            return;
        }

    private void workingOnResponsespinners(AccountDescribe accountDescribe) {

        spinnertaskType.setHint(Html.fromHtml("Task Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerstatus.setHint(Html.fromHtml("Status" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinneroutcome.setHint(Html.fromHtml("Outcome" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinneraccountManager.setHint(Html.fromHtml("Assigned To" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        autoopportunity_name.setHint(Html.fromHtml("Opportunity Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        autoaccount_name.setHint(Html.fromHtml("Account Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        autoconatct_name.setHint(Html.fromHtml("Contact Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));


        String success = accountDescribe.getSuccess();

        if (success.equals("true")) {
            Results_Account results = accountDescribe.getResult();

            DescribeDetails describeDetails = results.getDescribe();

            String value = "";

            String label = describeDetails.getLabel();
            if (label.equals("Calendar")) {
                ArrayList<Fields> desFields = describeDetails.getFields();

                for (Fields desFields1 : desFields) {
                    String name = desFields1.getName();
                    //task type
                    if (name.equals("cf_956")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            label = pickListValues1.getLabel();

                            PickListValues pickListValues2 = new PickListValues(value, label);
                            pickListTask.add(pickListValues2);
                            taskTypes.add(value);
                        }
                        tinydb.putListString("taskTypeslist", taskTypes);
                        Log.d("taskTypeslist", tinydb.getString("taskTypeslist"));

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, taskTypes) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                            //spinnerlocation.setAdapter(dataAdapter);


                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;

                                if (position + 1 == 0) {
                                    // Set the hint text color gray

                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.borderdashboard.simple_spinner_item, taskTypes);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnertaskType.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnertaskType.getAdapter();
                        int position = adapter.getPosition(task_types);
                        spinnertaskType.setSelection(position + 1);

                        opportunity.setVisibility(View.VISIBLE);
                        spinnertaskType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                                        String selectedItemText = (String) parent.getItemAtPosition(position);
                                if (position + 1 > 0) {

                                    PickListValues pickListValues = pickListTask.get(position);
                                    task_type_value = pickListValues.getValue();

//                                    Toast.makeText
//                                            (getContext(), "Selected : " + task_type_value, Toast.LENGTH_SHORT)
//                                            .show();

                                    Log.e("ids", task_type_value);
                                    if (task_type_value.equals("Cold Call") || task_type_value.equals("Courtesy Call")) {
                                        account.setVisibility(View.VISIBLE);
                                        contact.setVisibility(View.VISIBLE);
                                        outcomes.setVisibility(View.VISIBLE);
                                        opportunity.setVisibility(View.GONE);

                                    } else if (task_type_value.equals("Conference") || task_type_value.equals("Execution DocumentFragements") || task_type_value.equals("Training") || task_type_value.equals("Office Meeting")) {
                                        account.setVisibility(View.GONE);
                                        contact.setVisibility(View.GONE);
                                        opportunity.setVisibility(View.GONE);
                                        outcomes.setVisibility(View.VISIBLE);
                                    }else if (task_type_value.equals("Followup Meeting")){                                        account.setVisibility(View.GONE);
                                        contact.setVisibility(View.GONE);
                                        opportunity.setVisibility(View.VISIBLE);
                                        outcomes.setVisibility(View.GONE);
                                        account.setVisibility(View.GONE);
                                    }
                                    else {
                                        opportunity.setVisibility(View.VISIBLE);
                                        account.setVisibility(View.GONE);
                                        contact.setVisibility(View.GONE);
                                        outcomes.setVisibility(View.VISIBLE);
                                    }
                                }
                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                        saveTasksToSharedPrefs(getContext());


                    }

                    //account manager
                    else if (name.equals("assigned_user_id")) {
                        String result = "{\"19x1\":\"Admin Administrator\",\"19x5\":\"Ganeshprasad S\",\"19x6\":\"Balaji RR\",\"19x7\":\"Kiran Thadimarri\",\"19x8\":\"Sridhar Balakrishnan\",\"19x9\":\"Shilpa MK\",\"19x10\":\"Velmurugan N\",\"19x11\":\"Aamir Khanna\",\"19x12\":\"Jamir Abbas Pinjari\",\"19x13\":\"Syed Shadab Ashraf\",\"19x14\":\"Shahul Hameed\",\"19x15\":\"Manjula C\"" +
                                ",\"19x16\":\"Keerthi Vasan L\",\"19x17\":\"Lochan Jyoti Borgohain\",\"19x18\":\"Rajkumar Sanatomba Singh\",\"19x19\":\"Krishna Pandey\",\"19x20\":\"Nabajit Pathak\"" +
                                ",\"19x21\":\"Manoranjan Ningthoujam\",\"19x22\":\"Pravin Karbhari Ahire\",\"19x23\":\"Pratap Kumar Choudhary\",\"19x24\":\"Bhupendra Kumar Sharma\",\"19x25\":\"Ravindra RK\",\"19x26\":\"Raghavendra TK\"" +
                                ",\"19x27\":\"Prajwal VP\",\"19x28\":\"Guhan R\",\"19x29\":\"Karthik R\",\"19x30\":\"Varudha Raju A\",\"19x31\":\"Manikandan V\",\"19x32\":\"Prakash Loganathan\",\"19x33\":\"Krishnakumar R\",\"19x34\":\"Grite Vasan M\",\"19x35\":\"Shivam Kumar\",\"19x36\":\"Pramod Verma\"" +
                                ",\"19x37\":\"Sandeep Singh\",\"19x38\":\"Satyaprakash Viswakarma\",\"19x39\":\"Ashish Nagdeve\",\"19x40\": \"Shashank Bele\",\"19x41\":\"Rohit Shivdas\",\"19x42\":\"Narayan Bhosale\",\"19x43\":\"Prabodha Kumar Mohanty\",\"19x44\":\"Chandrakant Naikwade\",\"19x45\": \"Archana Balaji\",\"19x49\": \"Mohajyoti Bhattacharjee\",\"19x50\": \"Abid Bashir Wani\",\"19x52\": \"Anuj Kumar\",\"19x53\": \"Amit Singh\",\"19x54\": \"Sanjana Vivek\",\"19x55\": \"Hari Babu L\",\"19x56\": \"Nishi Kant\",\"19x57\": \"Deepak Saxena\",\"19x58\": \"B Mangaiyarkarasi\",\"19x59\": \"Om Prakash Verma\",\"19x60\": \"D Ramachandra Rao\",\"19x61\": \"Srinivasan R\"," +
                                "\"19x62\": \"Zafrey Alam\"," +
                                "\"19x63\": \"Balachandra C\"," +
                                "\"19x64\": \"Vijay Gore\"," +
                                "\"19x65\": \"Asef Shekh\"," +
                                "\"19x66\": \"Vivek Pandey\"," +
                                "\"19x67\": \"Pandiyarajan A\"," +
                                "\"19x68\": \"Vijay Nagekar\"," +
                                "\"19x69\": \"Lalchhandama Chawngthu\"," +
                                "\"19x70\": \"Nishant Kumar\"," +
                                "\"19x71\": \"Geetanjali Roy Chowdhury\"," +
                                "\"19x72\": \"K Ganga Prasad\"," +
                                "\"19x73\": \"Denish Patel\"," +
                                "\"19x74\": \"Kavitha S\"," +
                                "\"19x75\": \"Gokulnathan Ekanathan\"," +
                                "\"19x76\": \"Dharmaraj K\"," +
                                "\"19x77\": \"Saroj Kumar\"," +
                                "\"19x78\": \"Amol Hanmant Chavan\"," +
                                "\"19x79\": \"Deepak C R\"," +
                                "\"19x80\": \"Vinay Praksh Mishra\"," +
                                "\"19x81\": \"Diwan Aditya S\"," +
                                "\"19x82\": \"Ravi Kumar\"," +
                                "\"19x83\": \"Naina Dixit\"," +
                                "\"19x84\": \"Chandra Mouli N\"," +
                                "\"19x85\": \"Sachin Kamale\"," +
                                "\"19x86\": \"Subheksha Rawat\"," +
                                "\"19x87\": \"Suman Biswas\"," +
                                "\"19x88\": \"Segu Chanakya Prasad\"," +
                                "\"19x89\": \"Kapil Goswami\"," +
                                "\"19x90\": \"Anil Ventrapati\"," +
                                "\"19x91\": \"Vinaya Natarajan\"," +
                                "\"19x92\": \"Santosh Kumar Jha\"," +
                                "\"19x93\": \"Rasi Kiran R\"," +
                                "\"19x94\": \"Yatendra Pal Singh\"," +
                                "\"19x95\": \"Vikash Kumar Pandey\"," +
                                "\"19x96\": \"Prasan Karekar\"," +
                                "\"19x97\": \"Mridul Kumar Bordoloi\"," +
                                "\"19x98\": \"Satish Kumar Sengamalam\"," +
                                "\"19x99\": \"Sai Prema G\"," +
                                "\"19x100\": \"Phani Shankar Katepalli\"," +
                                "\"19x101\": \"Palak Manwani\"," +
                                "\"19x102\": \"Nilesh Soneji\"," +
                                "\"19x103\": \"Dinesh Babu\"," +
                                "\"19x104\": \"Aditya Kapileshwar\"," +
                                "\"19x105\": \"Gaurav Taldar\"," +
                                "\"19x107\": \"Rajasekar G\"," +
                                "\"19x108\": \"Rahul Kumar\"," +
                                "\"19x109\": \"Vikrant Prakash Kale\"," +
                                "\"19x110\": \"Rakesh L K\"," +
                                "\"19x111\": \"Syed Ali Bhadusha\"," +
                                "\"19x113\": \"Kalavathi S\"," +
                                "\"19x114\": \"Deepthi Rao R\"," +
                                "\"19x115\": \"Sangamash Tripathi\"," +
                                "\"19x116\": \"Raghavendra Kharvi\"," +
                                "\"19x118\": \"Monisha Kothari\"," +
                                "\"19x119\": \"Muthukumar Nadukkarayan\"," +
                                "\"19x120\": \"Pranam M\"," +
                                "\"19x121\": \"Adlin Crasta\"," +
                                "\"19x123\": \"Shiv Joshi\"," +
                                "\"19x124\": \"Tripti Mehra\"," +
                                "\"19x125\": \"Abhishek Agarwal\"," +
                                "\"19x126\": \"Harsha Singh\"," +
                                "\"19x128\": \"Basabananda Goswami\"," +
                                "\"19x129\": \"Venkatesh S\"," +
                                "\"19x130\": \"Peter Highmoor\"," +
                                "\"19x131\": \"Sachin Kumar\"," +
                                "\"19x132\": \"Dhanaraj Karuppusamy\"," +
                                "\"19x133\": \"Sreebhu V\"," +
                                "\"19x134\": \"Vaibav S\"," +
                                "\"19x135\": \"Umesh Pisalkar\"," +
                                "\"19x136\": \"Nirmal Kumar\"," +
                                "\"19x137\": \"Dhirav Agarwal\"," +
                                "\"19x138\": \"Priyadarshini Deb\"," +
                                "\"19x139\": \"Pranjal Borah\"," +
                                "\"19x140\": \"Dharmendra Ghose\"," +
                                "\"19x141\": \"Jitendra Maharaj\"," +
                                "\"19x142\": \"Koteswaran S\"," +
                                "\"19x143\": \"Nikita Todi\"," +
                                "\"19x144\": \"Nageshwar Rao\"," +
                                "\"19x145\": \"Nakul Acharjee\"," +
                                "\"19x146\": \"Pavithran R\"," +
                                "\"19x147\": \"Jitumani Kalita\"," +
                                "\"19x148\": \"Santosh B\"," +
                                "\"19x149\": \"Venkatesha R\"," +
                                "\"19x150\": \"Sanjoy Saha\"," +
                                "\"19x151\": \"Rohit G Jethani\"," +
                                "\"19x152\": \"S Madan\"," +
                                "\"19x153\": \"Dharmendra Kumar\"," +
                                "\"19x154\": \"Syed Ahsan\"," +
                                "\"19x155\": \"Hari Sudhan\"," +
                                "\"19x156\": \"Dharmendra Kotangle\"," +
                                "\"19x157\": \"Hrishikesh Shinde\"," +
                                "\"19x158\": \"Balaji M\"," +
                                "\"19x159\": \"Abhimanyu Mishra\"," +
                                "\"19x160\": \"Pooja Parulekar\"," +
                                "\"19x161\": \"Shubham Dam\"," +
                                "\"19x162\": \"Srinivasan S\"," +
                                "\"19x164\": \"Punit Bardia\"," +
                                "\"19x165\": \"Avinash Galatage\"," +
                                "\"19x166\": \"Mohd Sadiq Wani\"," +
                                "\"19x167\": \"Susanta Chakraborty\"," +
                                "\"19x169\": \"David I\"," +
                                "\"19x170\": \"Urvashi Parashar\"," +
                                "\"19x171\": \"Himangshu Chetia\"," +
                                "\"19x172\": \"Pravin Fartade\"," +
                                "\"19x173\": \"Prakashaiah G\"," +
                                "\"19x174\": \"Prashant Kumar Srivastava\"," +
                                "\"19x175\": \"Rajesh Sinha\"," +
                                "\"19x177\": \"Sumith V\"," +
                                "\"19x178\": \"Vaishnavi K\"," +
                                "\"19x179\": \"Bharath B\"," +
                                "\"19x180\": \"Waseem Akram\"," +
                                "\"19x181\": \"Devika Abrol\"," +
                                "\"19x182\": \"Sanjay Srivastava\"," +
                                "\"19x183\": \"Shachi Aggarwal\"," +
                                "\"19x184\": \"Shalaka Jaiswal\"," +
                                "\"19x186\": \"Vaibhav Bhola\"," +
                                "\"19x187\": \"Pankush Gulbandhar\"," +
                                "\"19x188\": \"Thanjani M\"," +
                                "\"19x189\": \"Meet Acharya\"," +
                                "\"19x190\": \"Parag Shankar Pachpande\"," +
                                "\"19x191\": \"Arun Srivastava\"," +
                                "\"19x192\": \"Sujisha N\"," +
                                "\"19x193\": \"Sanjit Roy Chowdhury\"," +
                                "\"19x194\": \"Chidanand Cholli\"," +
                                "\"19x195\": \"Rohit Kumar\"," +
                                "\"19x196\": \"Abhijeet Dabhekar\"," +
                                "\"19x197\": \"Rahul Kumar\"," +
                                "\"19x198\": \"Shruti Pathak\"," +
                                "\"19x199\": \"Nitrishna Das Gupta\"," +
                                "\"19x200\": \"Arun V\"," +
                                "\"19x201\": \"Mohsin Khan\"," +
                                "\"19x202\": \"Manikandan T\"," +
                                "\"19x204\": \"Sitharaman S\"," +
                                "\"19x205\": \"Deepak Singh\"," +
                                "\"19x206\": \"Biswajit Pattanayak\"," +
                                "\"19x207\": \"Gunjan Saxena\"," +
                                "\"19x208\": \"Praveen Chauhan\"," +
                                "\"19x209\": \"Aakash Jaiswal\"," +
                                "\"19x210\": \"Borish Yurembam\"," +
                                "\"19x211\": \"Bopadev Chingangbam\"," +
                                "\"19x212\": \"Yadhu P\"," +
                                "\"19x213\": \"Ankit Singh\"," +
                                "\"19x214\": \"Ravindrakumar K\"," +
                                "\"19x215\": \"Dharmendra Singh\"," +
                                "\"19x216\": \"Archa S\"," +
                                "\"19x217\": \"Sandeep Srivastav\"," +
                                "\"19x218\": \"Sravya L\"," +
                                "\"19x219\": \"Touhidul Ittehad\"," +
                                "\"19x220\": \"Priyanka Manjunath\"," +
                                "\"19x221\": \"Mohith H\"," +
                                "\"19x222\": \"Kishan Singh\"," +
                                "\"19x223\": \"Nitesh Tiwari\"," +
                                "\"19x224\": \"Aniket Kumar\"," +
                                "\"19x225\": \"Chhagan Thakur\"," +
                                "\"19x226\": \"Arul B\"," +
                                "\"19x227\": \"Somnath Kori\"," +
                                "\"19x229\": \"Krishan Kumar\"," +
                                "\"19x230\": \"Hariharan G\"," +
                                "\"19x231\": \"Raghavendran CK\"," +
                                "\"19x233\": \"Nihal Kotian\"," +
                                "\"19x234\": \"Rohit Kumar\"," +
                                "\"19x235\": \"Senthil R\"," +
                                "\"19x236\": \"Pramod Tiwari\"," +
                                "\"19x237\": \"Harsha PS\"," +
                                "\"19x238\": \"Ravi Shankar Shukla\"," +
                                "\"19x240\": \"Ashher Laique\"," +
                                "\"19x241\": \"Arunkumar Madiwal\"," +
                                "\"19x242\": \"Segu Amala\"," +
                                "\"19x244\": \"Ravi Singh\"," +
                                "\"19x245\": \"Ruchi Tiwari\"," +
                                "\"19x246\": \"Ajith Subramanian\"," +
                                "\"19x247\": \"Kumar Anand\"," +
                                "\"19x248\": \"Amit Jaiswal\"," +
                                "\"19x249\": \"Sonu Sharma\"," +
                                "\"19x250\": \"Niloy Chakraborty\"," +
                                "\"19x251\": \"Sumedh Dhanorkar\"," +
                                "\"19x252\": \"Rajamani G\"," +
                                "\"19x253\": \"Vinayak Patil\"," +
                                "\"19x254\": \"Mary Delish\"," +
                                "\"19x255\": \"Manjunath Bandi\"," +
                                "\"19x256\": \"Rishabh Shah\"," +
                                "\"19x258\": \"Sintujit Das\"," +
                                "\"19x259\": \"Bhavdeep Anand\"," +
                                "\"19x260\": \"Majinur Rahman\"," +
                                "\"19x261\": \"Niraj Das\"," +
                                "\"19x262\": \"Rohan Singh\"," +
                                "\"19x263\": \"Vinoth M\"," +
                                "\"19x264\": \"Vishal Singh\"," +
                                "\"19x265\": \"Dibakar Kumar Roy\"," +
                                "\"19x266\": \"Rutul Ramesh Patel\"," +
                                "\"19x267\": \"Ashwin A\"," +
                                "\"19x268\": \"Neeraj Sharma\"," +
                                "\"19x269\": \"Mohammed Hussain\"," +
                                "\"19x270\": \"B Venkatramanan\"," +
                                "\"19x271\": \"Pradyumn Mishra\"," +
                                "\"19x272\": \"Manjunath V\"," +
                                "\"19x1448\": \"Kavya Balaji\"," +
                                "\"19x1465\": \"Raj Jivani\"," +
                                "\"19x1392\": \"Jayanta Kumar Das\"," +
                                "\"19x1381\": \"Sathiaseelan R\"," +
                                "\"19x1390\": \"Ajit Kumar Mahanta\"," +
                                "\"19x1395\": \"Sibabrata Hazarika\"," +
                                "\"19x1352\": \"Srinivas Banda\"," +
                                "\"19x1356\": \"Vipin V\"," +
                                "\"19x1344\": \"Shashank Kishore\"," +
                                "\"19x1343\": \"Tirumalay Singh\"," +
                                "\"19x1374\": \"Raja P\"," +
                                "\"19x1402\": \"Umesh Suryakant\"," +
                                "\"19x273\": \"Molla Mohammad Abbas\"," +
                                "\"19x1380\": \"Subramanian R\"," +
                                "\"19x1386\": \"Gobi A\"," +
                                "\"19x1375\": \"Samala Srivathsava\"," +
                                "\"19x1379\": \"Palaniraj D\"," +
                                "\"19x1368\": \"Analjit Singh\"," +
                                "\"19x1425\": \"Avanti Jawake\"," +
                                "\"19x1387\": \"Makarand Gulavani\"," +
                                "\"19x1398\": \"Shilpi Katare\"," +
                                "\"19x1460\": \"Mrunali Anil Patil\"," +
                                "\"19x1365\": \"Priscilla Ebenezer\"," +
                                "\"19x1383\": \"Siddharudha Sindagi\"," +
                                "\"19x1362\": \"Ramandeep Singh\"," +
                                "\"19x275\": \"Anil Patil\"," +
                                "\"19x1447\": \"Pranati Dash\"," +
                                "\"19x1473\": \"Pranati Dash 2\"," +
                                "\"19x1388\": \"Kiran Sugaonkar\"," +
                                "\"19x1394\": \"Chinmoy Sarkar\"," +
                                "\"19x276\": \"Ankit Malik\"," +
                                "\"19x1393\": \"Rishab Roy\"," +
                                "\"19x1405\": \"Jitendra Kumar\"," +
                                "\"19x1367\": \"Vishant Gharde\"," +
                                "\"19x1366\": \"L Ranjith\"," +
                                "\"19x1377\": \"Varun Saxena\"," +
                                "\"19x1446\": \"Jasmeen Attar\"," +
                                "\"19x1457\": \"Vaishali Subhash Patil\"," +
                                "\"19x1370\": \"Gopalan N\"," +
                                "\"19x1373\": \"Sripathi T G\"," +
                                "\"19x307\": \"Tarun Agarwal\"," +
                                "\"19x1378\": \"Kartikey Shukla\"," +
                                "\"19x1359\": \"Chandragouda Hiregoudra\"," +
                                "\"19x1382\": \"Vasantharajan N\"," +
                                "\"19x277\": \"Bhupendra Singh\"," +
                                "\"19x1372\": \"Dinesh Kumar\"," +
                                "\"19x1427\": \"Shashwat Swetkar\"," +
                                "\"19x1400\": \"Chandra Sekhar\"," +
                                "\"19x1376\": \"Srikanth G\"," +
                                "\"19x1462\": \"Ravindra Sharma\"," +
                                "\"19x1397\": \"Jinal Prajapati\"," +
                                "\"19x1434\": \"Sachin Parashari\"," +
                                "\"19x1471\": \"Debdoot Mukherjee\"," +
                                "\"19x1437\": \"Ajay Prabhakar Ponde\"," +
                                "\"19x1431\": \"Mohd Shakir\"," +
                                "\"19x1433\": \"Bhupinder Singh\"," +
                                "\"19x1426\": \"Saumil Patel\"," +
                                "\"19x278\": \"Dhakshnamoorthy D\"," +
                                "\"19x1430\": \"Sachin Mishra\"," +
                                "\"19x1439\": \"Vikram Singh Guleria\"," +
                                "\"19x1429\": \"Manjeet Singh\"," +
                                "\"19x279\": \"Desh Bhaduria\"," +
                                "\"19x1436\": \"Ajay Kumar Gupta\"," +
                                "\"19x1432\": \"Satendra Kumar Mishra\"," +
                                "\"19x1435\": \"NIshkrutha Jaykumar\"," +
                                "\"19x1440\": \"Alok Chhikara\"," +
                                "\"19x1455\": \"Shibotosh Mukherjee\"," +
                                "\"19x1454\": \"Anant Kulkarni\"," +
                                "\"19x1453\": \"Sumitabh Khare\"," +
                                "\"19x280\": \"Dinesh Dharankar\"," +
                                "\"19x1452\": \"Sudeepta Das\"," +
                                "\"19x1451\": \"Amit Chauhan\"," +
                                "\"19x1458\": \"Atul Kumar\"," +
                                "\"19x1450\": \"Vijay Nath\"," +
                                "\"19x281\": \"Feroz Sheikh\"," +
                                "\"19x1461\": \"Supriyo Palit\"," +
                                "\"19x1464\": \"Hari Hara Ganesh\"," +
                                "\"19x1469\": \"Amit Sarawgi\"," +
                                "\"19x1466\": \"Amol Sharad Potdar\"," +
                                "\"19x1467\": \"Abdul Hamid\"," +
                                "\"19x1468\": \"Jagadeesh K\"," +
                                "\"19x1470\": \"Sanket Manohar Gokhale\"," +
                                "\"19x1472\": \"Nitin Juyal\"," +
                                "\"19x282\": \"Manikandan C\"," +
                                "\"19x283\": \"Muthupandi R\"," +
                                "\"19x284\": \"Nagaraj K\"," +
                                "\"19x285\": \"Padmaja Rao\"," +
                                "\"19x286\": \"Prasad BVS\"," +
                                "\"19x287\": \"Rahul Rajora\"," +
                                "\"19x288\": \"Ratnadip Bhattacharjee\"," +
                                "\"19x289\": \"Ravi Parihar\"," +
                                "\"19x290\": \"Shyam Rao\"," +
                                "\"19x291\": \"Suman Kumar\"," +
                                "\"19x292\": \"Sunil Verma\"," +
                                "\"19x293\": \"Sunil Kulkarni\"," +
                                "\"19x294\": \"Varun Shrivastava\"," +
                                "\"19x295\": \"Sadanand Sinha\"," +
                                "\"19x296\": \"Shrikant Bele\"," +
                                "\"19x297\": \"Ajeet Tiwari\"," +
                                "\"19x298\": \"Raj Narayan\"," +
                                "\"19x299\": \"Ashutosh Shukla\"," +
                                "\"19x300\": \"Priya Narayan\"," +
                                "\"19x301\": \"Suman Kumari\"," +
                                "\"19x302\": \"S Vijay\"," +
                                "\"19x303\": \"Sunder Rajan J\"," +
                                "\"19x304\": \"K Panchal\"," +
                                "\"19x305\": \"Mohammed Mansoor Ahmed\"," +
                                "\"19x306\": \"Himanshu Bhatt\"," +
                                "\"19x308\": \"Dhavala Divyavani\"," +
                                "\"19x309\": \"KP Tiwari\"," +
                                "\"19x311\": \"Kalyan Kumar S\"," +
                                "\"19x312\": \"Vittaldas Pai\"," +
                                "\"19x313\": \"Rahul Kale\"," +
                                "\"19x314\": \"Dharshana Kankaria\"," +
                                "\"19x315\": \"Kalpana Praveen\"," +
                                "\"19x1449\": \"Narayana Sahu\"," +
                                "\"19x1474\": \"Medivision Marketing\"," +
                                "\"19x1475\": \"Nakamachi leasing\"," +
                                "\"19x1477\": \"DD Healthcare\"," +
                                "\"19x1478\": \"Helpline Medicals\"," +
                                "\"19x1441\": \"Sohan Srivastav\"," +
                                "\"19x1442\": \"Pankaj Kumar\"," +
                                "\"19x1443\": \"Mukesh Ranjan\"," +
                                "\"19x1444\": \"Manish Kumar\"," +
                                "\"19x1445\": \"Mukesh Kumar\"," +
                                "\"19x1459\": \"Rahul Mishra\"," +
                                "\"19x1463\": \"Dwipjyati Banik\"," +
                                "\"19x1476\": \"Hi-Tech Medical\"," +
                                "\"19x316\": \"Prakash S\"," +
                                "\"19x1209\": \"syam V\"," +
                                "\"19x1347\": \"test tester\"}";
                        Gson gson = new Gson();
                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(result);
                        JsonObject obj = element.getAsJsonObject();
                        final Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
                        for (final Map.Entry<String, JsonElement> entry : entries) {
                            userids = entry.getKey();
                            JsonElement usernames = entry.getValue();
                            account_manger.add(usernames.getAsString().toString());
                            Users users = new Users(userids, usernames);
                            usersListAccount.add(users);

                        }

                        // tinydb.clear();
                        tinydb.putListString("users", account_manger);
                        Log.d("users", tinydb.getString("users"));

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, account_manger) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                            //spinnerlocation.setAdapter(dataAdapter);


                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    // spinnerlocation.setPrompt("Select");

                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;

                                if (position + 1 == 0) {
                                    // Set the hint text color gray

                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };


                        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.borderdashboard.simple_spinner_item, taskTypes);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinneraccountManager.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinneraccountManager.getAdapter();
                        if(isDuplicate.equals("true")) {
                            int position = adapter.getPosition(assigned);
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinneraccountManager.setSelection(position + 1);
                        }else {
                            int position = adapter.getPosition(firstname.concat(" ").concat(lastname));
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinneraccountManager.setSelection(position + 1);
                        }


                        spinneraccountManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position + 1 > 0) {
                                    Users users1 = usersListAccount.get(position);
                                    assigned_id = users1.getUserid();
                                  //  Toast.makeText(getContext(), assigned_id, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }
//
                    //task status type

                    else if (name.equals("taskstatus")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            status.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            statuslist.add(pickListValues2);

                        }
                        tinydb.putListString("status", status);
                        Log.d("status", tinydb.getString("status"));

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, status) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                            //spinnerlocation.setAdapter(dataAdapter);


                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    // spinnerlocation.setPrompt("Select");

                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;

                                if (position + 1 == 0) {
                                    // Set the hint text color gray

                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerstatus.setAdapter(spinnerArrayAdapter);

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerstatus.getAdapter();
                        int position = adapter.getPosition(edit_status);
                        spinnerstatus.setSelection(position + 1);

                        spinnerstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                if (position + 1 > 0) {

                                    PickListValues pickListValues3 = statuslist.get(position);
                                    status_name = String.valueOf(pickListValues3.getValue());
                                    Log.e("outcome_name", status_name);
//                                    Toast.makeText
//                                            (getContext(), "Selected : " + status_name, Toast.LENGTH_SHORT)
//                                            .show();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    } else if (name.equals("cf_1015")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            outcome_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            outcomelist.add(pickListValues2);
                        }

                        tinydb.putListString("outcome_list", outcome_list);
                        Log.d("outcome", tinydb.getString("outcome_list"));
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, outcome_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                            //spinnerlocation.setAdapter(dataAdapter);


                            @Override
                            public boolean isEnabled(int position) {
                                if (position == 0) {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    // spinnerlocation.setPrompt("Select");

                                    return false;
                                } else {
                                    return true;
                                }
                            }

                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;

                                if (position + 1 == 0) {
                                    // Set the hint text color gray

                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };
                        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, outcome_list);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinneroutcome.setAdapter(spinnerArrayAdapter);

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinneroutcome.getAdapter();
                        int position = adapter.getPosition(edit_outcome);
                        spinneroutcome.setSelection(position+1);
                        spinneroutcome.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                if (position + 1 > 0) {

                                    PickListValues pickListValues3 = outcomelist.get(position);
                                    outcome_name = String.valueOf(pickListValues3.getValue());
                                    Log.e("outcome_name", outcome_name);
//                                    Toast.makeText
//                                            (getContext(), "Selected : " + outcome_name, Toast.LENGTH_SHORT)
//                                            .show();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                    }

                }
            }


        }
    }
    //  }

    public void saveTasksToSharedPrefs(Context context) {
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(task_typelist); //tasks is an ArrayList instance variable
        prefsEditor.putString("currentTasks", json);
        prefsEditor.commit();
    }



}
