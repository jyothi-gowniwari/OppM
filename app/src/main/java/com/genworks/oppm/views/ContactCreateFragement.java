package com.genworks.oppm.views;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.genworks.oppm.model.ContactFormModel;
import com.genworks.oppm.model.DescribeDetails;
import com.genworks.oppm.model.Fields;
import com.genworks.oppm.model.PickListValues;
import com.genworks.oppm.model.Records;
import com.genworks.oppm.model.RecordsAccounts;
import com.genworks.oppm.model.Results_Account;
import com.genworks.oppm.model.Results_AccountQ;
import com.genworks.oppm.model.SaveContactModule;
import com.genworks.oppm.model.TypeDetails;
import com.genworks.oppm.model.UserAccountQuery;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactCreateFragement extends Fragment {


    private ProgressDialog progressDialog = null;
    private Object values;
    TinyDB tinydb;
    private boolean isonetimelogin=false;
    private ArrayList<String> assignedlist;
    CustomAdapter adapter;
    AppCompatAutoCompleteTextView autoCompleteTextView;
    private Fragment fragment;
    private LinearLayout linearLayout;
    private ArrayList<PickListValues> salutionname_list=new ArrayList<>();
    private Button closebtn;
    private String contact_id,isDuplicate,firstname,lastname,edit_assigned_tos,sessionId,ids,account_ids,module_id,speclization_names,names,jobtiles,contact_type,contact_source,id,salution_names;
    private AppCompatButton save,cancel;
    private String edit_salutation_names,edit_jobtiles,edit_contacttype,edit_contactsource,edit_speclizations;
    private MaterialSpinner spinnersalutation;
    private PopupWindow popupWindow;
    JsonElement usernames;

    private MaterialSpinner spinnerSpecialization,spinneraccountManager,spinneraccountname;
    private MaterialSpinner spinnerJobTitle;
    private MaterialSpinner spinnerContactType;
    private ArrayList<Records> recordsList=new ArrayList<>();
    private ArrayList<RecordsAccounts> recordsListAccount=new ArrayList<>();
    private MaterialSpinner spinnerContactSource;
    private ArrayList<ContactFormModel> contactFormModels;
    private ArrayList<String> Salutationnames;
    private ArrayList<Users> usersListAccount=new ArrayList<>();
    private ArrayList<String> Specializationnames;
    private ArrayList<PickListValues> jobtitle_list=new ArrayList<>();
    private ArrayList<PickListValues> contacttype_list=new ArrayList<>();
    private ArrayList<PickListValues> contactsource_list=new ArrayList<>();
    private ArrayList<PickListValues> speclization_list=new ArrayList<>();
    private ArrayList<String> Jobtitlenames;
    private ArrayList<Records> records;
    RecyclerView recyclerView;
    ImageView Add_account;
    private String assigned_id,speclizations,salutation_names,contacttype,userids;
    // ArrayList for Listview
    ArrayList<HashMap<String, String>> productList;
    private ArrayList<RecordsAccounts> recordsAccounts;
    private String accountnames,account_id;
    private ArrayList<String> account_manger,account_name;
    private ArrayList<String> ContactTypenames;
    private ArrayList<String> ContactSourcenames;
    private TextInputEditText first_name,last_name,email_id,mobile;
    private final Handler handler = new Handler ();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment
        ((MainActivity)getActivity()).setActionBarTitle("CREATE NEW CONTACT");
        View rootView = inflater.inflate(R.layout.contact_form, container, false);
        setHasOptionsMenu(true);
        spinnersalutation=rootView.findViewById(R.id.firstname);
        first_name=rootView.findViewById(R.id.editFirstname);
        first_name.setHint(Html.fromHtml ( first_name.getHint()+""));

        recyclerView = rootView.findViewById(R.id.recyclerView);
        autoCompleteTextView = (AppCompatAutoCompleteTextView) rootView.findViewById(R.id.account_name);

        // editTextSearch = rootView.findViewById(R.id.editTextSearch);
       // spinneraccountname=rootView.findViewById(R.id.account_name);
        //editTextSearch.setHint(Html.fromHtml ( editTextSearch.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        last_name=rootView.findViewById(R.id.editLastname);
        last_name.setHint(Html.fromHtml ( last_name.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        email_id=rootView.findViewById(R.id.editEmailid);
        email_id.setHint(Html.fromHtml ( email_id.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        mobile=rootView.findViewById(R.id.editMobilephone);
        mobile.setHint(Html.fromHtml ( mobile.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        spinnerSpecialization=rootView.findViewById(R.id.specilization);
        spinnerJobTitle=rootView.findViewById(R.id.job_title);
        spinnerContactType=rootView.findViewById(R.id.contact_type);
        spinnerContactSource=rootView.findViewById(R.id.contact_source);
        save=rootView.findViewById(R.id.save);
        spinneraccountManager=rootView.findViewById(R.id.assigned_to);
        Salutationnames=new ArrayList<>();
        Specializationnames=new ArrayList<>();
        Jobtitlenames=new ArrayList<>();
        account_name=new ArrayList<>();
        ContactTypenames =new ArrayList<>();
        ContactSourcenames=new ArrayList<>();
        account_manger=new ArrayList<String>();
        assignedlist=new ArrayList<>();
        account_name=new ArrayList<String>();
        tinydb=new TinyDB(getContext());

        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
            Log.d("session_IdMAin",sessionId);
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }
        Add_account=rootView.findViewById(R.id.add_account);

        Add_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDuplicate="false";
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Account");
                fragment = new AccountCreateFragement();
                fragment.setArguments(args);
                loadFragment(fragment);
            }

        });
        @SuppressLint("ResourceAsColor")
        ColorStateList colorStateList = ColorStateList.valueOf(R.color.black);
        ViewCompat.setBackgroundTintList(autoCompleteTextView, colorStateList);
        ViewCompat.setBackgroundTintList(first_name, colorStateList);
        ViewCompat.setBackgroundTintList(last_name, colorStateList);
        ViewCompat.setBackgroundTintList(spinneraccountManager, colorStateList);
        ViewCompat.setBackgroundTintList(spinnerContactSource, colorStateList);
        ViewCompat.setBackgroundTintList(spinnerContactType, colorStateList);
        ViewCompat.setBackgroundTintList(spinnersalutation, colorStateList);
        ViewCompat.setBackgroundTintList(spinnerJobTitle, colorStateList);
        ViewCompat.setBackgroundTintList(spinnerSpecialization, colorStateList);
        ViewCompat.setBackgroundTintList(mobile, colorStateList);
        ViewCompat.setBackgroundTintList(email_id, colorStateList);
        spinnerContactSource.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerContactSource.getWindowToken(), 0);
                return false;
            }
        }) ;

        spinnersalutation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalutation.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerContactType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerContactType.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerContactSource.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerContactSource.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerJobTitle.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerJobTitle.getWindowToken(), 0);
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
        spinnerSpecialization.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerSpecialization.getWindowToken(), 0);
                return false;
            }
        }) ;
        isDuplicate = getArguments().getString("isDuplicate");
        firstname=getActivity().getIntent().getStringExtra("firstname");
        lastname=getActivity().getIntent().getStringExtra("lastname");
        if (isDuplicate.equals("true")) {
            edit_salutation_names = getArguments().getString("salutations");
            String firstnames = getArguments().getString("firstnames");
            first_name.setText(firstnames);
            String account_names = getArguments().getString("account_names");
            autoCompleteTextView.setText(account_names);
            String lastnames = getArguments().getString("lastnames");
            last_name.setText(lastnames);
            edit_speclizations = getArguments().getString("speclization_name");
            String emails = getArguments().getString("emails");
            email_id.setText(emails);
            String mobiles = getArguments().getString("mobiles");
            mobile.setText(mobiles);
            contact_id=getArguments().getString("contact_id");
            Log.d("contact_id",contact_id);
            edit_jobtiles = getArguments().getString("jobtile");
            edit_contacttype = getArguments().getString("contacttype");
            edit_contactsource = getArguments().getString("contactsource");
            Log.d("edit_contactsource",edit_contactsource);
            edit_assigned_tos= getArguments().getString("assigned_tos");
            Log.d("contactsource",edit_contactsource);
        }
        String apiResponseAccount=tinydb.getString("jsonAccount");
        String apiResponseAll=tinydb.getString("jsonAllContacts");
        if (!apiResponseAccount.equals("")){
            Gson gson = new Gson();
            UserAccountQuery userAccountQuery = gson.fromJson(apiResponseAccount, UserAccountQuery.class);
            workingOnResponseAccount(userAccountQuery);
        }if (!apiResponseAll.equals("")){
            Gson gson = new Gson();
            AccountDescribe accountDescribe = gson.fromJson(apiResponseAll, AccountDescribe.class);
            workingOnResponseAll(accountDescribe);
        }else {
            fetchJSON();
            fetchAccountJSON();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contact_id!=null){
                    updaterecords();
                }else {
                    saverecords();
                }
            }
        });
        cancel=rootView.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new ContactFragment();
                loadFragment(fragment);
            }
        });

        return rootView;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void fetchJSON(){
       // Salutationnames.add(("Saltutation"+" ");
        spinnerSpecialization.setHint(((Html.fromHtml ( "Specialization"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"))));
        spinnersalutation.setHint(((Html.fromHtml ( "Saltutation"+"  "+"<font color=\"#ff0000\">" + "* " + "</font>"))));
        //Specializationnames.add("Specialization"+" ");
      //  account_manger.add("Created By"+" "+Html.fromHtml(getResources().getString(R.string.asteriskred)));
        spinneraccountManager.setHint(((Html.fromHtml ( "Created By"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));
       // Jobtitlenames.add("Job Title"+" " + Html.fromHtml(getResources().getString(R.string.asteriskred)));
        spinnerJobTitle.setHint(((Html.fromHtml ( "Job Title"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));

       // ContactTypenames.add("Contact Type"+" " + Html.fromHtml(getResources().getString(R.string.asteriskred)));
        spinnerContactType.setHint(((Html.fromHtml ( "Contact Type"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));
        spinnerContactSource.setHint(((Html.fromHtml ( "Contact Source"+" "))));


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setIndeterminate(true);
//                progressDialog.setMessage("Loading...");
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.setCancelable(false);
//                progressDialog.show();

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "describe";
                String module = "Contacts";
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
                            String jsonAllContacts = g.toJson(accountDescribe);
                            tinydb.putString("jsonAllContacts",jsonAllContacts);
                            workingOnResponseAll(accountDescribe);




                            }
                         //   progressDialog.dismiss();
                        }


                    @Override
                    public void onFailure(Call<AccountDescribe> call, Throwable t) {
                        Log.d("error", t.getMessage());
                       // progressDialog.dismiss();
                    }


                });

            }



        }, 0);
        return ;
    }

    private void workingOnResponseAll(AccountDescribe accountDescribe) {
        spinnerSpecialization.setHint(((Html.fromHtml ( "Specialization"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"))));
        spinnersalutation.setHint(((Html.fromHtml ( "Saltutation"+"  "+"<font color=\"#ff0000\">" + "* " + "</font>"))));
        spinneraccountManager.setHint(((Html.fromHtml ( "Created By"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));
        spinnerJobTitle.setHint(((Html.fromHtml ( "Job Title"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));
        spinnerContactType.setHint(((Html.fromHtml ( "Contact Type"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"+" "))));
        spinnerContactSource.setHint(((Html.fromHtml ( "Contact Source"+" "))));
        String success = accountDescribe.getSuccess();

        if (success.equals("true")) {
            Results_Account results = accountDescribe.getResult();

            DescribeDetails describeDetails = results.getDescribe();

            String value = "";

            String label = describeDetails.getLabel();
            if (label.equals("Contacts")) {
                ArrayList<Fields> desFields = describeDetails.getFields();

                for (Fields desFields1 : desFields) {
                    String name = desFields1.getName();
                    //salutation type
                    if (name.equals("salutationtype")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            Salutationnames.add(value);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            salutionname_list.add(pickListValues2);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Salutationnames);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnersalutation.setAdapter(dataAdapter);

                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnersalutation.getAdapter();
                            int position=adapter.getPosition(edit_salutation_names);
                            spinnersalutation.setSelection(position+1);

                            spinnersalutation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = salutionname_list.get(position);
                                        salutation_names = pickListValues3.getValue();
                                        Log.e("salutation_names", salutation_names);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });


                        }


                    }
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

                        JsonParser parser = new JsonParser();
                        JsonElement element = parser.parse(result);
                        JsonObject obj = element.getAsJsonObject();
                        final Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();

                        for(final Map.Entry<String, JsonElement> entry: entries) {
                            userids = entry.getKey();
                            usernames = entry.getValue();
                            assignedlist.add(usernames.getAsString().toString());
                            Users users = new Users(userids, usernames);
                            usersListAccount.add(users);
                        }
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(),R.layout.spinner_item,assignedlist){// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
                            //spinnerlocation.setAdapter(dataAdapter);


                            @Override
                            public boolean isEnabled(int position){
                                if(position == 0)
                                {
                                    // Disable the first item from Spinner
                                    // First item will be use for hint
                                    // spinnerlocation.setPrompt("Select");

                                    return false;
                                }
                                else
                                {
                                    return true;
                                }
                            }
                            @Override
                            public View getDropDownView(int position, View convertView,
                                                        ViewGroup parent) {
                                View view = super.getDropDownView(position, convertView, parent);
                                TextView tv = (TextView) view;

                                if(position+1 == 0){
                                    // Set the hint text color gray

                                    tv.setTextColor(Color.GRAY);
                                }
                                else {
                                    tv.setTextColor(Color.BLACK);
                                }
                                return view;
                            }
                        };


                        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.borderdashboard.simple_spinner_item, taskTypes);
                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinneraccountManager.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter=(ArrayAdapter<String>)spinneraccountManager.getAdapter();
                        if(isDuplicate.equals("true")) {
                            int position = adapter.getPosition(edit_assigned_tos);
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
                                if(position+1>0){
                                    Users users1 = usersListAccount.get(position);
                                    assigned_id = users1.getUserid();
                                   // Toast.makeText(getContext(),assigned_id,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }

                    //specialization

                    else if (name.equals("cf_905")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            Specializationnames.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            speclization_list.add(pickListValues2);
                        }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Specializationnames);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerSpecialization.setAdapter(dataAdapter);
                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerSpecialization.getAdapter();
                            int position=adapter.getPosition(edit_speclizations);
                            spinnerSpecialization.setSelection(position+1);
                            spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = speclization_list.get(position);
                                        speclizations = String.valueOf(pickListValues3.getValue());
                                        Log.e("speclizations", speclizations);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                    }

                    //job title
                    else if (name.equals("cf_907")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            Jobtitlenames.add(value);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, Jobtitlenames);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerJobTitle.setAdapter(dataAdapter);
                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerJobTitle.getAdapter();
                            int position=adapter.getPosition(edit_jobtiles);
                            spinnerJobTitle.setSelection(position+1);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            jobtitle_list.add(pickListValues2);
                            spinnerJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = jobtitle_list.get(position);
                                        jobtiles = String.valueOf(pickListValues3.getValue());
                                        Log.e("states", jobtiles);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        }
                    }

                    //Contact type

                    else if (name.equals("cf_909")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            ContactTypenames.add(value);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            contacttype_list.add(pickListValues2);

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ContactTypenames);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerContactType.setAdapter(dataAdapter);

                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerContactType.getAdapter();
                            int position=adapter.getPosition(edit_contacttype);
                            spinnerContactType.setSelection(position+1);

                            spinnerContactType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = contacttype_list.get(position);
                                        contact_type = String.valueOf(pickListValues3.getValue());
                                        Log.e("states", contact_type);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //Conatct Source
                    else if (name.equals("cf_979")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
                        for (PickListValues pickListValues1 : pickListValues) {
                            value = pickListValues1.getValue();
                            ContactSourcenames.add(value);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            contactsource_list.add(pickListValues2);
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, ContactSourcenames);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerContactSource.setAdapter(dataAdapter);
                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerContactSource.getAdapter();
                            int position=adapter.getPosition(edit_contactsource);
                            spinnerContactSource.setSelection(position+1);

                            spinnerContactSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = contactsource_list.get(position);
                                        contact_source = String.valueOf(pickListValues3.getValue());
                                        Log.e("contact_source", contact_source);
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
    }

    private void fetchAccountJSON(){
        autoCompleteTextView.setHint(Html.fromHtml ( "Account Name"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
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
            autoCompleteTextView.setAdapter(adapter);
            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    private void filter(String text) {
        //new array list that will hold the filtered data
        ArrayList<String> filterdNames = new ArrayList<>();

        //looping through existing elements
        for (String s : account_name) {
            //if the existing elements contains the search input
            if (s.toLowerCase().contains(text.toLowerCase())) {
                //adding the element to filtered list
                filterdNames.add(s);
              //  editTextSearch.setText(text);
            }
           // editTextSearch.setText(s);
        }

        //calling a method of the adapter class and passing the filtered list


        adapter.filterList(filterdNames);
    }



    private void saverecords(){

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                if (last_name.getText().toString().isEmpty()) {
                    last_name.setError("Please Enter Last Name");
                } else if (spinneraccountManager.getSelectedItem() == null) {
                    spinneraccountManager.setError("Please Select an option");
                } else if (spinnerSpecialization.getSelectedItem() == null) {
                    spinnerSpecialization.setError("Please Select an option");
                }else if (spinnerJobTitle.getSelectedItem() == null) {
                    spinnerJobTitle.setError("Please Select an option");
                }else if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Please Enter Mobile Phone");
                }else if (email_id.getText().toString().isEmpty()) {
                    email_id.setError("Please Enter EmailId");
                }else if (spinnerContactType.getSelectedItem()==null) {
                    spinnerContactType.setError("Please Select an option");
                }
                else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                 //   sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "saveRecord";
                    String module = "Contacts";
                    String firstname = first_name.getText().toString();
                    String lastname = last_name.getText().toString();
                    String accountname = account_id;

                    spinnerSpecialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getSelectedItem().equals(0);

                            speclizations = item.toString();

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    spinnersalutation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getSelectedItem().equals(0);

                            salutation_names = item.toString();

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    spinnerJobTitle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getSelectedItem().equals(0);

                            jobtiles = item.toString();

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    spinnerContactType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                            Object item = parent.getSelectedItem().equals(0);

                            salutation_names = item.toString();

                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    // String speclization=spinnerSpecialization.getSelectedItem().toString();
                    //  String salutionnames=spinnersalutation.getSelectedItem().toString();
                    // String jobtile=spinnerJobTitle.getSelectedItem().toString();
                    //  String contacttype=spinnerContactType.getSelectedItem().toString();
                    String contactsource = contact_source;
                    String email = email_id.getText().toString();
                    String mobiles = mobile.getText().toString();
                    String record = "";
                    JSONObject postdata = new JSONObject();

                    try {
                        postdata.put("salutationtype", salutation_names);
                        Log.d("salutationtype", salutation_names);
                        postdata.put("firstname", firstname);
                        Log.d("firstname", firstname);
                        postdata.put("lastname", lastname);
                        Log.d("lastname", lastname);
                        postdata.put("account_id", account_id);
                        Log.d("account_id", accountname);
                        postdata.put("cf_907", jobtiles);
                        Log.d("cf_907", jobtiles);
                        postdata.put("assigned_user_id",assigned_id );
                        Log.d("assigned_user_id", assigned_id);
                        postdata.put("cf_909", contact_type);
                        Log.d("cf_909", contact_type);
                        postdata.put("cf_905", speclizations);
                        Log.d("cf_905", speclizations);
                        postdata.put("mobile", mobiles);
                        Log.d("mobile", mobiles);
                        postdata.put("email", email);
                        Log.d("email", email);
                        postdata.put("cf_979", contact_source);
                        Log.d("cf_979", contact_source);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //String values= postdata.getString(firstname,lastname,jobtile,contacttype,speclization,mobiles,email);
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

                                SaveContactModule saveContactModule = response.body();


                                String success = saveContactModule.getSuccess();


                                if (success.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Successfully saved", Toast.LENGTH_LONG).show();
                                    fragment = new ContactFragment();
                                    loadFragment(fragment);

                                }else if(success.equals("false")){

                                    String message="Duplicates detected";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Invalid details", Toast.LENGTH_LONG).show();
                                }
                            }
                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<SaveContactModule> call, Throwable t) {
                            Log.d("error", t.getMessage());
                            progressDialog.dismiss();
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

                if (last_name.getText().toString().isEmpty()) {
                    last_name.setError("Please Enter Last Name");
                } else if (spinneraccountManager.getSelectedItem() == null) {
                    spinneraccountManager.setError("Please Select an option");
                } else if (spinnerSpecialization.getSelectedItem() == null) {
                    spinnerSpecialization.setError("Please Select an option");
                }else if (spinnerJobTitle.getSelectedItem() == null) {
                    spinnerJobTitle.setError("Please Select an option");
                }else if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Please Enter Mobile Phone");
                }else if (email_id.getText().toString().isEmpty()) {
                    email_id.setError("Please Enter EmailId");
                }else if (spinnerContactType.getSelectedItem()==null) {
                    spinnerContactType.setError("Please Select an option");
                }
                else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                  //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "saveRecord";
                    String module = "Contacts";
                    String record = contact_id;
                    Log.d("record",record);
                    String firstname = first_name.getText().toString();
                    String lastname = last_name.getText().toString();
                    String accountname = account_id;

                    // String speclization=spinnerSpecialization.getSelectedItem().toString();
                    //  String salutionnames=spinnersalutation.getSelectedItem().toString();
                    // String jobtile=spinnerJobTitle.getSelectedItem().toString();
                    //  String contacttype=spinnerContactType.getSelectedItem().toString();
                    String contactsource = contact_source;
                    String accountmanger = ids;
                    String email = email_id.getText().toString();
                    String mobiles = mobile.getText().toString();
                    JSONObject postdata = new JSONObject();

                    try {
                        postdata.put("salutationtype", salutation_names);
                        Log.d("salutationtype", salutation_names);
                        postdata.put("firstname", firstname);
                        Log.d("firstname", firstname);
                        postdata.put("lastname", lastname);
                        Log.d("lastname", lastname);
                        postdata.put("account_id", account_id);
                        Log.d("account_id", account_id);
                        postdata.put("cf_907", jobtiles);
                        Log.d("cf_907", jobtiles);
                        postdata.put("assigned_user_id", assigned_id);
                        Log.d("assigned_user_id", assigned_id);
                        postdata.put("cf_909", contact_type);
                        Log.d("cf_909", contact_type);
                        postdata.put("cf_905", speclizations);
                        Log.d("cf_905", speclizations);
                        postdata.put("mobile", mobiles);
                        Log.d("mobile", mobiles);
                        postdata.put("email", email);
                        Log.d("email", email);
                        postdata.put("cf_979", contact_source);
                        Log.d("cf_979", contact_source);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    //String values= postdata.getString(firstname,lastname,jobtile,contacttype,speclization,mobiles,email);
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

                                SaveContactModule saveContactModule = response.body();
                                String success = saveContactModule.getSuccess();
                                if (success.equals("true")) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                    fragment = new ContactFragment();
                                    loadFragment(fragment);
                                }else if(success.equals("false")){
                                    String message="Duplicates detected";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext(), "Invalid details", Toast.LENGTH_LONG).show();
                                }
                            }
                            progressDialog.dismiss();
                        }


                        @Override
                        public void onFailure(Call<SaveContactModule> call, Throwable t) {
                            Log.d("error", t.getMessage());
                            progressDialog.dismiss();
                        }
                    });

                }


            }
        }, 0);
        return ;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ((AppCompatActivity)getActivity()).getBaseContext().setActionBarTitle("Create New Task");
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Toast.makeText(getContext(),"Refersh",Toast.LENGTH_LONG).show();
                fetchJSON();
                fetchAccountJSON();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }
    

}

