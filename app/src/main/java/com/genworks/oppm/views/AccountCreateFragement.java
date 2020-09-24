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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.genworks.oppm.model.AccountDescribe;
import com.genworks.oppm.model.DescribeDetails;
import com.genworks.oppm.model.Fields;
import com.genworks.oppm.model.LocationModule;
import com.genworks.oppm.model.LocationRecords;
import com.genworks.oppm.model.PickListValues;
import com.genworks.oppm.model.Records;
import com.genworks.oppm.model.Results_Account;
import com.genworks.oppm.model.Results_Locations;
import com.genworks.oppm.model.SaveContactModule;
import com.genworks.oppm.model.TypeDetails;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountCreateFragement extends Fragment {


    private ProgressDialog progressDialog = null;
    private Object values;
    private ArrayList<String> location_list;
    private Fragment fragment;
    private ArrayList<Users> responseList;
    ArrayList<JsonElement> users=new ArrayList<>();
    HashMap<Integer,String> spinnerMap;
    private String username,firstname,lastname,edit_ship_districts,edit_bill_districts,ownership,ownership_type,facility_Type,locationids,locationdistrict,locationcircle,locationidss;
    private String assigned_id,sessionId,module_id,ids,facility_type,states,ownershipTypes;
    private TextInputEditText ship_country,mobile,edit_bill_city,edit_ship_pin,edit_ship_city,bill_country,edit_bill_district,edit_bill_pin,email,country,website,bill_address,ship_address;
    private AppCompatButton save,cancel;
    private boolean isonetimelogin=false;
    private String isDuplicate,edit_ownershiptype,edit_facilityType,edit_states,edit_accountmanger,account_id;
    private EditText edit_account_name;
    private MaterialSpinner spinnerfacilityType,spinnerOwnershipType,spinnerState,spinneraccountManager,spinnerStateship;
    private ArrayList<String> facilityTypes;
    private ArrayList<String> ownershipType;
    private ArrayList<PickListValues> facilityTypeList=new ArrayList<>();
    private ArrayList<PickListValues> stattes_list=new ArrayList<>();
    private ArrayList<String> state,shipstate;
    TinyDB tinydb;
    private ArrayList<Records> recordsList=new ArrayList<>();
    private ArrayList<String> assignedlist;
    private ArrayList<PickListValues> ownershipTypelist=new ArrayList<>();
    private ArrayList<String> account_manger;
    private ArrayList<Users> account_mangerusers=new ArrayList<>();
    private final Handler handler = new Handler ( );
    private ArrayList<Records> records;
    private ArrayList<LocationRecords> locationRecords;
    private ArrayList<LocationRecords> locationList=new ArrayList<>();
    private String owner_name,userids;
    LinkedHashMap<String,String> linkedHashMap;
    private MaterialSpinner autoDistrict,autoDistrictship;
    List<View> fieldsToBeValidated;
    JsonElement usernames;
    private ArrayList<Users> usersListAccount=new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment

        ((MainActivity)getActivity()).setActionBarTitle("CREATE NEW ACCOUNT");


        View rootView = inflater.inflate(R.layout.account_form, container, false);
       // setHasOptionsMenu(true);
        spinnerfacilityType=rootView.findViewById(R.id.facility_type);
        email=rootView.findViewById(R.id.email);
        email.setHint( email.getHint());
        spinneraccountManager=rootView.findViewById(R.id.account_manger);
        spinnerOwnershipType=rootView.findViewById(R.id.ownership_type);
        website=rootView.findViewById(R.id.website);
        website.setHint(Html.fromHtml ( website.getHint()+""));

        bill_address=rootView.findViewById(R.id.bill_add);
        ship_address=rootView.findViewById(R.id.ship_add);
        bill_address.setHint(Html.fromHtml ( bill_address.getHint()+""));
        ship_address.setHint(Html.fromHtml ( ship_address.getHint()+""));

        edit_account_name=rootView.findViewById(R.id.accountnames);
        edit_account_name.setHint(Html.fromHtml ( edit_account_name.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        mobile=rootView.findViewById(R.id.editMobilephone);
        mobile.setHint(Html.fromHtml ( mobile.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        bill_country=rootView.findViewById(R.id.bill_country);
        bill_country.setHint(Html.fromHtml ( bill_country.getHint()+""));

        ship_country=rootView.findViewById(R.id.ship_country);
        ship_country.setHint(Html.fromHtml ( ship_country.getHint()+""));

        edit_bill_city =rootView.findViewById(R.id.bill_city);
        edit_bill_city.setHint(Html.fromHtml ( edit_bill_city.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        edit_ship_city =rootView.findViewById(R.id.ship_city);
        edit_ship_city.setHint(Html.fromHtml ( edit_ship_city.getHint()+""));
        autoDistrict=rootView.findViewById(R.id.bill_dist);
//        autoDistrict.setHint(Html.fromHtml ( "Billing District"+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        autoDistrictship=rootView.findViewById(R.id.ship_dist);
        autoDistrictship.setHint(Html.fromHtml ( autoDistrict.getHint()+""));

        edit_bill_pin=rootView.findViewById(R.id.bill_pin);
        edit_bill_pin.setHint(Html.fromHtml ( edit_bill_pin.getHint()+""));

        edit_ship_pin=rootView.findViewById(R.id.ship_pin);
        edit_ship_pin.setHint(Html.fromHtml ( edit_ship_pin.getHint()+""));


        cancel=rootView.findViewById(R.id.cancel);

        spinnerState=rootView.findViewById(R.id.state);
        spinnerStateship=rootView.findViewById(R.id.ship_state);
        save=rootView.findViewById(R.id.save);
        facilityTypes=new ArrayList<>();
        ownershipType=new ArrayList<>();
        state=new ArrayList<>();
        shipstate=new ArrayList<>();
        account_manger=new ArrayList<String>();
        location_list=new ArrayList<>();
        assignedlist=new ArrayList<>();
        responseList=new ArrayList<Users>();
        tinydb = new TinyDB(getContext());
        username = getActivity().getIntent().getStringExtra("username");
        firstname=getActivity().getIntent().getStringExtra("firstname");
        lastname=getActivity().getIntent().getStringExtra("lastname");
        Log.d("username",username);
        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }

        spinnerfacilityType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerfacilityType.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerOwnershipType.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerOwnershipType.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerStateship.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerStateship.getWindowToken(), 0);
                return false;
            }
        }) ;
        spinnerState.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerState.getWindowToken(), 0);
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
        isDuplicate = getArguments().getString("isDuplicate");
        if (isDuplicate.equals("true")) {

            String accountNames = getArguments().getString("account_name");
            edit_account_name.setText(accountNames);
            edit_facilityType = getArguments().getString("facilityType");
            edit_ownershiptype = getArguments().getString("ownership_types");
            edit_states = getArguments().getString("bill_states");
            String pin_code = getArguments().getString("pin_code");
            edit_bill_pin.setText(pin_code);
            String mobiles = getArguments().getString("mobiles");
            mobile.setText(mobiles);
            String emails = getArguments().getString("email");
            email.setText(emails);
            String bill_citys = getArguments().getString("bill_citys");
            edit_bill_city.setText(bill_citys);
            account_id=getArguments().getString("account_id");
            edit_bill_districts = getArguments().getString("bill_districts");
            edit_ship_districts = getArguments().getString("ship_districts");

            edit_accountmanger = getArguments().getString("assigned_tos");
            Log.d("edit_accountmanger",edit_accountmanger);
        }
        String apiResponseLocation=tinydb.getString("jsonLocation");
        String apiResponseAll=tinydb.getString("jsonAll");
        if (!apiResponseLocation.equals("")){
            Gson gson = new Gson();
            LocationModule locationModule = gson.fromJson(apiResponseLocation, LocationModule.class);
            workingOnResponseLocation(locationModule);
        }if (!apiResponseAll.equals("")){
            Gson gson = new Gson();
            AccountDescribe accountDescribe = gson.fromJson(apiResponseAll, AccountDescribe.class);
            workingOnResponseAll(accountDescribe);
        }else {
            fetchLocationJSON();
            fetchJSON();
        }



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(account_id!=null){
                    updaterecords();
                }else {
                    saverecords();
                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new AccountFragement();
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
        spinnerfacilityType.setHint(Html.fromHtml ( spinnerfacilityType.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
       spinnerOwnershipType.setHint(Html.fromHtml ( spinnerOwnershipType.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerState.setHint(Html.fromHtml ( spinnerState.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerStateship.setHint(Html.fromHtml ( spinnerStateship.getHint()+""));
        spinneraccountManager.setHint(Html.fromHtml ( spinneraccountManager.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.setIcon(R.drawable.loader);
                progressDialog.show();
              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "describe";
                String module = "Accounts";
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
                            String jsonAll = g.toJson(accountDescribe);
                            tinydb.putString("jsonAll",jsonAll);
                            workingOnResponseAll(accountDescribe);

                        }

                    }


                    @Override
                    public void onFailure(Call<AccountDescribe> call, Throwable t) {
                        Log.d("error", t.getMessage());
                     //   progressDialog.dismiss();
                    }


                });

            }



        }, 0);
        return ;
    }

    private void workingOnResponseAll(AccountDescribe accountDescribe) {

        spinnerfacilityType.setHint(Html.fromHtml ( spinnerfacilityType.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerOwnershipType.setHint(Html.fromHtml ( spinnerOwnershipType.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerState.setHint(Html.fromHtml ( spinnerState.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerStateship.setHint(Html.fromHtml ( spinnerStateship.getHint()+""));
        spinneraccountManager.setHint(Html.fromHtml ( spinneraccountManager.getHint()+" "+"<font color=\"#ff0000\">" + "* " + "</font>"));

        String success = accountDescribe.getSuccess();
        if (success.equals("true")) {
            Results_Account results = accountDescribe.getResult();

            DescribeDetails describeDetails = results.getDescribe();

            String value = "";

            String label = describeDetails.getLabel();
            if (label.equals("Accounts")) {
                ArrayList<Fields> desFields = describeDetails.getFields();

                for (Fields desFields1 : desFields) {
                    String name = desFields1.getName();
                    //facility type
                    if (name.equals("rating")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            facilityTypes.add(value);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            facilityTypeList.add(pickListValues2);
                        }

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(),R.layout.spinner_item,facilityTypes){// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                        spinnerfacilityType.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerfacilityType.getAdapter();
                        int position=adapter.getPosition(edit_facilityType);
                        spinnerfacilityType.setSelection(position+1);
                        spinnerfacilityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position + 1 > 0) {

                                    PickListValues pickListValues3 = facilityTypeList.get(position);
                                    facility_Type = pickListValues3.getValue();
                                    Log.e("facility_Type", facility_Type);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });




                    }

                    //ownership type

                    else if (name.equals("industry")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            ownershipType.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            ownershipTypelist.add(pickListValues2);
                        }

                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(),R.layout.spinner_item,ownershipType){// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                        spinnerOwnershipType.setAdapter(spinnerArrayAdapter);

                        ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerOwnershipType.getAdapter();
                        int position=adapter.getPosition(edit_ownershiptype);
                        spinnerOwnershipType.setSelection(position+1);
                        spinnerOwnershipType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                if (position + 1 > 0) {

                                    PickListValues pickListValues3 = ownershipTypelist.get(position);
                                    owner_name = String.valueOf(pickListValues3.getValue());
                                    Log.e("owner_name", owner_name);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                    //assigned user
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
                            int position = adapter.getPosition(edit_accountmanger);
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
                                    Toast.makeText(getContext(),assigned_id,Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }
                    //job title
                    else if (name.equals("cf_887")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            state.add(value);
                            PickListValues pickListValues2 = new PickListValues(label,value);
                            stattes_list.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(),R.layout.spinner_item,state){// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnerState.setAdapter(spinnerArrayAdapter);
                            spinnerStateship.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter=(ArrayAdapter<String>) spinnerState.getAdapter();
                            int position=adapter.getPosition(edit_states);
                            spinnerState.setSelection(position+1);
                            ArrayAdapter<String> adapter1=(ArrayAdapter<String>) spinnerStateship.getAdapter();
                            int position1=adapter1.getPosition(edit_states);
                            spinnerStateship.setSelection(position1+1);
                            spinnerStateship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = stattes_list.get(position);
                                        states = String.valueOf(pickListValues3.getValue());
                                        Log.e("states", states);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = stattes_list.get(position);
                                        states = String.valueOf(pickListValues3.getValue());
                                        Log.e("states", states);
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

    //district list
    private void fetchLocationJSON(){
        autoDistrict.setHint(Html.fromHtml("Billing District" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
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
        autoDistrict.setHint(Html.fromHtml(autoDistrict.getHint() + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
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
            autoDistrict.setAdapter(spinnerArrayAdapter);
            ArrayAdapter<String> adapter = (ArrayAdapter<String>) autoDistrict.getAdapter();
            int position = adapter.getPosition(edit_bill_districts);
            spinnerArrayAdapter.notifyDataSetChanged();
            autoDistrict.setSelection(position + 1);
            autoDistrict.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position+1>0) {
                        //    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        //  ((TextView) parent.getChildAt(0)).setText("Assigned To");
                        LocationRecords locationRecords = locationList.get(position);

                        locationids = locationRecords.getId();
                        String[] moduleid = locationids.split("x");
                        String moduleids = moduleid[0];
                        locationidss = moduleid[1];
                        Log.e("locationids", locationids);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            autoDistrictship.setAdapter(spinnerArrayAdapter);
            ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) autoDistrictship.getAdapter();
            int position1 = adapter.getPosition(edit_ship_districts);
            spinnerArrayAdapter.notifyDataSetChanged();
            autoDistrictship.setSelection(position + 1);
            autoDistrictship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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


    //save records

    private void saverecords(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                String account_name = edit_account_name.getText().toString();
                String accountmanger = assigned_id;
                //   Log.d("acountmanger",accountmanger);
                String billcity = edit_bill_city.getText().toString();
                Log.d("billcity", billcity);
                //String state=spinnerState.getSelectedItem().toString();
                String district = locationids;
                String primary_email = email.getText().toString();
                Log.d("primary_email", primary_email);
                String mobiles = mobile.getText().toString();
                Log.d("mobiles", mobiles);
                String bill_pin = edit_bill_pin.getText().toString();
                Log.d("bill_pin", bill_pin);

                String countrys = bill_country.getText().toString();
                Log.d("countrys", countrys);

                if (edit_account_name.getText().toString().isEmpty()) {
                    edit_account_name.setError("Please Enter Account Name");
                }else if (spinnerOwnershipType.getSelectedItem() == null) {
                    spinnerOwnershipType.setError("Please Select an option");
                } else if (spinnerfacilityType.getSelectedItem() == null) {
                    spinnerfacilityType.setError("Please Select an option");
                }else if (spinneraccountManager.getSelectedItem() == null) {
                    spinneraccountManager.setError("Please Select an option");
                }else if (spinnerState.getSelectedItem() == null) {
                    spinnerState.setError("Please Select an option");
                }else if (mobile.getText().toString().isEmpty()) {
                    mobile.setError("Please Enter Mobile Number");
                }else if (edit_bill_city.getText().toString().isEmpty()) {
                    edit_bill_city.setError("Please Enter City Name");
                }else if (edit_bill_city.getText().toString().isEmpty()) {
                    edit_bill_city.setError("Please Enter City Name");
                }
                else {
                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                   // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "saveRecord";
                    String module = "Accounts";
                    String record = "";


                    JSONObject postdata = new JSONObject();
                    try {
                        postdata.put("accountname", account_name);
                        Log.d("accountname", account_name);
                        postdata.put("industry", owner_name);
                        Log.d("industry", owner_name);
                        postdata.put("rating", facility_Type);
                        Log.d("rating", facility_Type);
                        postdata.put("assigned_user_id", accountmanger);
                        Log.d("assigned_user_id", accountmanger);
                        postdata.put("bill_city", billcity);
                        Log.d("bill_city", billcity);
                        postdata.put("cf_887", states);
                        Log.d("cf_887", states);
                        postdata.put("bill_pobox", locationids);
                        Log.d("bill_pobox", locationids);
                        postdata.put("bill_code", bill_pin);
                        Log.d("bill_code", bill_pin);
                        postdata.put("phone", mobiles);
                        Log.d("phone", mobiles);
                        postdata.put("email1", primary_email);
                        Log.d("email1", primary_email);
                        postdata.put("bill_country", countrys);
                        Log.d("bill_country", countrys);

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
                                    fragment = new AccountFragement();
                                    loadFragment(fragment);
                                }else if(success.equals("false")){

                                   String message="Duplicates detected";
                                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();

                                }

                                else {
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

    private void updaterecords() {

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
                    String module = "Accounts";
                    String record = account_id;
                    String account_name = edit_account_name.getText().toString();
                    String accountmanger = assigned_id;
                    //   Log.d("acountmanger",accountmanger);
                    String billcity = edit_bill_city.getText().toString();
                    Log.d("billcity", billcity);
                    //String state=spinnerState.getSelectedItem().toString();
                    String district = locationids;
                    String primary_email = email.getText().toString();
                    Log.d("primary_email", primary_email);
                    String mobiles = mobile.getText().toString();
                    Log.d("mobiles", mobiles);
                    String bill_pin = edit_bill_pin.getText().toString();
                    Log.d("bill_pin", bill_pin);

                    String countrys = bill_country.getText().toString();
                    Log.d("countrys", countrys);
                    JSONObject postdata = new JSONObject();
                    try {
                        postdata.put("accountname", account_name);
                        Log.d("accountname", account_name);
                        postdata.put("industry", owner_name);
                        Log.d("industry", owner_name);
                        postdata.put("rating", facility_Type);
                        Log.d("rating", facility_Type);
                        postdata.put("assigned_user_id", accountmanger);
                        Log.d("assigned_user_id", accountmanger);
                        postdata.put("bill_city", billcity);
                        Log.d("bill_city", billcity);
                        postdata.put("cf_887", states);
                        Log.d("cf_887", states);
                        postdata.put("bill_pobox", locationids);
                        Log.d("bill_pobox", locationids);
                        postdata.put("bill_code", bill_pin);
                        Log.d("bill_code", bill_pin);
                        postdata.put("phone", mobiles);
                        Log.d("phone", mobiles);
                        postdata.put("email1", primary_email);
                        Log.d("email1", primary_email);
                        postdata.put("bill_country", countrys);
                        Log.d("bill_country", countrys);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                    /** Call the method with parameter in the interface to get the notice data*/
                    Call<SaveContactModule> call = service.UpdateDetails(operation, sessionId, module, record, postdata);

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
                                    Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                    fragment = new AccountFragement();
                                    loadFragment(fragment);
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
                            // progressDialog.dismiss();
                        }
                    });
                }
            }, 0);
            return;
        }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                fetchLocationJSON();
                fetchJSON();
                return true;
        }
        return true;

    }
}

