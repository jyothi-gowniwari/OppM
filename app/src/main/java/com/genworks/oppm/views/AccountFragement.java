package com.genworks.oppm.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genworks.oppm.Adapter.AccountAdapter;
import com.genworks.oppm.LoadingDialog;
import com.genworks.oppm.model.AccountModel;
import com.genworks.oppm.model.ContactModel;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.PreferenceManager;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class AccountFragement extends Fragment implements AccountAdapter.MyItemClickListener,AccountAdapter.MyEditItemClickListner {

    public AccountFragement() {
        // Required empty public constructor
    }
    private AccountAdapter accountAdapter;
    private RecyclerView recyclerViewAccount;
    Fragment fragment;
    private boolean isonetimelogin=false;
    private String currenttime;
    public static String currentFragment=null;
    private LoadingDialog loadingDialog;
    private androidx.appcompat.widget.Toolbar searchtollbar;
    private String sessionId,account_id,firstname,lastname;
    private PopupWindow popupWindow;
    private boolean isDuplicate = false;
    LinearLayout linearLayout1;
    private final Handler handler = new Handler();
    private String account_names,facilityType;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String mobie,username;
    Menu search_menu;
    TextView mTitle;
    MenuItem item_search;
    private LinearLayout linearLayout;
    private Button closebtn;
    private ArrayList<AccountModel> listAccount;
    private ArrayList<ContactModel> listContact;
    Gson gson;
    RelativeLayout relativeLayout;
    private ProgressDialog progressDialog = null;
    private Object values;

    private ProgressDialog progressDoalog;
    private boolean isaccount=true;
    public static final String ARG_PAGE = "ARG_PAGE";
    private TextView account_name,facility_type,ownership_type,assigned_to,createdtime,modifiedtime,modifiedby,bill_country,account_no,bill_street,bill_city,bill_district,bill_state,bill_pin;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.account, container, false);

        setSearchtollbar();
        setHasOptionsMenu(true);
        FloatingActionButton fb= rootView.findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDuplicate = false;
                Fragment fragment = new AccountCreateFragement();
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });
        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }
        recyclerViewAccount = rootView.findViewById(R.id.recyclerOpportunity);
        listAccount = new ArrayList<>();
        listContact=new ArrayList<>();
        username = getActivity().getIntent().getStringExtra("username");
        accountAdapter = new AccountAdapter(requireContext(),listAccount,this,this);
        recyclerViewAccount.setAdapter(accountAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        loadingDialog = new LoadingDialog(getContext());
        recyclerViewAccount.setLayoutManager(linearLayoutManager);
        String apiResponseStr = PreferenceManager.getInstance(requireContext()).getAPIResponseAccount(); // so you have to press your refresh buttonok
        java.time.LocalDate today = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            currenttime = mdformat.format(calendar.getTime());
        }
        if (!apiResponseStr.equals("")){
            Gson gson = new Gson();
            SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
            listAccount.clear();
            workingOnResponse(obj);
            accountAdapter.notifyDataSetChanged();
        }

        else {
            listAccount.clear();
            fetchJSON();
            accountAdapter.notifyDataSetChanged();
        }
       // fetchJSON();
        accountAdapter.notifyDataSetChanged();
        return rootView;
    }
    private void loadFragment(Fragment fragment) {
        // load fragment
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.account);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void workingOnResponse(final SyncModule syncModule){

                String success = syncModule.getSuccess();

                if (success.equals("true")) {
                    SyncResults results = syncModule.getResult();

                    Sync sync = results.getSync();

                    ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

                    for (SyncUpdated syncUpdated : syncUpdateds) {
                        account_id = syncUpdated.getId();

                        ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                        String value = "";
                        String accountName = "";
                        String ownershipType = "";
                        String facilityType = "";
                        String email = "";
                        String phone = "";
                        String assigned = "";
                        String bill_street = "";
                        String bill_city = "";
                        String billingDistrict = "";
                        String billingState = "";
                        String billingPIN = "";
                        String createdtime = "";
                        String modifiedtime = "";
                        String account_no = "";
                        String modifiedby = "";
                        String bill_country = "";
                        for (SyncBlocks syncBlocks1 : syncBlocks) {

                            String label = syncBlocks1.getLabel();
                            if (label.equals("Account Details")) {
                                ArrayList<SynFields> synFields = syncBlocks1.getFields();

                                for (SynFields synFields1 : synFields) {
                                    String name = synFields1.getName();
                                    values = synFields1.getValue();
                                    if (name.equals("accountname")) {
                                        accountName = String.valueOf(values);
                                    } else if (name.equals("industry")) {
                                        ownershipType = String.valueOf(values);
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
                                    } else if (name.equals("rating")) {
                                        facilityType = String.valueOf(values);
                                    } else if (name.equals("email1")) {
                                        email = String.valueOf(values);
                                    } else if (name.equals("phone")) {
                                        phone = String.valueOf(values);
                                    } else if (name.equals("createdtime")) {
                                        createdtime = String.valueOf(values);
                                    } else if (name.equals("modifiedtime")) {
                                        modifiedtime = String.valueOf(values);
                                    } else if (name.equals("account_no")) {
                                        account_no = String.valueOf(values);
                                    } else if (name.equals("modifiedby")) {
                                        try {
                                            Gson gson = new Gson();
                                            String strJson = gson.toJson(values);
                                            JSONObject jsonObject = new JSONObject(strJson);
                                            String v = jsonObject.getString("label");
//
                                            modifiedby = v;
                                        } catch (Exception ex) {
                                            Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                        }
                                    }
                                }
                            } else if (label.equals("Address Details")) {
                                ArrayList<SynFields> synFields = syncBlocks1.getFields();


                                for (SynFields synFields1 : synFields) {

                                    String name = synFields1.getName();
                                    values = synFields1.getValue();

                                    if (name.equals("bill_pobox")) {
                                        try {
                                            Gson gson = new Gson();
                                            String strJson = gson.toJson(values);
                                            JSONObject jsonObject = new JSONObject(strJson);
                                            String v = jsonObject.getString("label");
                                            billingDistrict = v;
                                        } catch (Exception ex) {
                                            Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                        }
                                    } else if (name.equals("bill_street")) {
                                        bill_street = String.valueOf(values);
                                    } else if (name.equals("bill_city")) {
                                        bill_city = String.valueOf(values);
                                    } else if (name.equals("cf_887")) {
                                        billingState = String.valueOf(values);
                                    } else if (name.equals("bill_code")) {
                                        billingPIN = String.valueOf(values);
                                    } else if (name.equals("bill_country")) {
                                        bill_country = String.valueOf(values);
                                    }
                                }
                            }
                           // loadingDialog.dismiss();
                            PreferenceManager.getInstance(requireContext()).setMultipleAccountData(accountName, ownershipType, facilityType, email, phone, assigned, bill_street, bill_city, billingDistrict, billingState, account_no, createdtime, modifiedtime, modifiedby, bill_country, billingPIN, account_id);
                        }
                        //
                        AccountModel accountModel = new AccountModel(accountName, ownershipType, facilityType, email, phone, assigned, bill_street, bill_city, billingDistrict, billingState.concat("  ").concat(billingPIN), account_no, createdtime, modifiedtime, modifiedby, bill_country, billingPIN, account_id);
                        listAccount.add(accountModel);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setReverseLayout(true);
                        linearLayoutManager.setStackFromEnd(true);
                        recyclerViewAccount.setLayoutManager(linearLayoutManager);
                        recyclerViewAccount.setAdapter(accountAdapter);
                        accountAdapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(getContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }


    private void fetchJSON(){

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
                String operation = "syncModuleRecords";
                String module = "Accounts";
                String syncToken = "";
                String mode = "public";
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
                            PreferenceManager.getInstance(requireContext()).setAPIResponseAccount(json);

                            workingOnResponse(syncModule);

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
        return ;
    }
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                listAccount.clear();
                fetchJSON();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public void myEditItemClick(int position) {


        if(username.equals("admin")) {
            isDuplicate = true;

            String account_name = listAccount.get(position).getAccountName();
            String facilityType = listAccount.get(position).getFacilityType();
            String ownership_types = listAccount.get(position).getOwnershipType();
            String pin_code = listAccount.get(position).getBillingPIN();
            String bill_streets = listAccount.get(position).getBill_street();
            String bill_citys = listAccount.get(position).getBill_city();
            String bill_districts = listAccount.get(position).getBillingDistrict();
            String bill_states = listAccount.get(position).getBillingState();
            String account_nos = listAccount.get(position).getAccount_no();
            String createdtimes = listAccount.get(position).getCreatedtime();
            String modifiedtimes = listAccount.get(position).getModifiedtime();
            String modifiedbys = listAccount.get(position).getModifiedby();
            String bill_countrys = listAccount.get(position).getBill_country();
            String mobiles = listAccount.get(position).getPhone();
            String email = listAccount.get(position).getEmail();
            String assigned_tos = listAccount.get(position).getAssigned();
            String account_id = listAccount.get(position).getAccount_id();

            Fragment fragment = new AccountCreateFragement();
            Bundle args = new Bundle();
            args.putString("account_name", account_name);
            args.putString("facilityType", facilityType);
            args.putString("ownership_types", ownership_types);
            args.putString("pin_code", pin_code);
            args.putString("bill_streets", bill_streets);
            args.putString("bill_citys", bill_citys);
            args.putString("bill_districts", bill_districts);
            args.putString("assigned_tos", assigned_tos);
            Log.d("assigned_tos", assigned_tos);
            args.putString("email", email);
            args.putString("mobiles", mobiles);
            args.putString("account_id", account_id);
            args.putString("bill_states", bill_states);
            args.putString("isDuplicate", String.valueOf(isDuplicate));
            fragment.setArguments(args);
            loadFragment(fragment);
        }else {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.permission_denied, null);
            final TextView back;
            popupWindow = new PopupWindow(customView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true);
            popupWindow.setFocusable(true);
            back=customView.findViewById(R.id.back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
        }
    }

    @Override
    public void myItemClick(int position) {
                String account_names=listAccount.get(position).getAccountName();
                String facilityType=listAccount.get(position).getFacilityType();
                String ownership_types=listAccount.get(position).getOwnershipType();
                String pin_code=listAccount.get(position).getBillingPIN();
                String bill_streets=listAccount.get(position).getBill_street();
                String bill_citys=listAccount.get(position).getBill_city();
                String bill_districts=listAccount.get(position).getBillingDistrict();
                String bill_states=listAccount.get(position).getBillingState();
                String account_nos=listAccount.get(position).getAccount_no();
                String createdtimes=listAccount.get(position).getCreatedtime();
                String modifiedtimes=listAccount.get(position).getModifiedtime();
                String modifiedbys=listAccount.get(position).getModifiedby();
                String bill_countrys=listAccount.get(position).getBill_country();
                String assigned_tos=listAccount.get(position).getAssigned();

                LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.custom_popup_account,null);

                linearLayout=customView.findViewById(R.id.card_details);
                final PopupWindow popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setTouchable(true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));
                popupWindow.showAtLocation(customView,Gravity.CENTER ,0, 0);
                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                account_no=customView.findViewById(R.id.account_no);
                account_name = customView.findViewById(R.id.account_name);
                ownership_type = customView.findViewById(R.id.ownership_type);
                facility_type = customView.findViewById(R.id.facility_type);
                assigned_to = customView.findViewById(R.id.assigned_to);
                createdtime =  customView.findViewById(R.id.createdtime);
                modifiedtime = customView.findViewById(R.id.modifiedtime);
                account_no = customView.findViewById(R.id.account_no);
                modifiedby = customView.findViewById(R.id.modifiedby);
                bill_street = customView.findViewById(R.id.bill_street);
                bill_city = customView.findViewById(R.id.bill_city);
                bill_district =  customView.findViewById(R.id.bill_district);
                bill_state = customView.findViewById(R.id.bill_state);
                bill_country =  customView.findViewById(R.id.bill_country);
                bill_pin =  customView.findViewById(R.id.pincode);
                closebtn=customView.findViewById(R.id.close);
                account_name.setText(account_names);
                facility_type.setText(facilityType);
                ownership_type.setText(ownership_types);
                createdtime.setText(createdtimes);
                modifiedtime.setText(modifiedtimes);
                modifiedby.setText(modifiedbys);
                bill_pin.setText(pin_code);
                account_no.setText(account_nos);
                bill_city.setText(bill_citys);
                bill_country.setText(bill_countrys);
                bill_district.setText(bill_districts);
                bill_state.setText(bill_states);
                bill_street.setText(bill_streets);
                assigned_to.setText(assigned_tos);
                account_no.setText(account_nos);
                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }
    // < ---- Implementing Search begins here ---- > //
    public void onSearch(String query){
        Log.d("Query From Activity",query);
        ArrayList<AccountModel> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listAccount.size(); i++) {

            final String jTitle = listAccount.get(i).getAccountName().toLowerCase();
            //   final String jCompany = listTask.get(i).getJob_company().toLowerCase();
            //  final String jLoc = mDataList.get(i).getJob_location().toLowerCase();
            if (jTitle.contains(query) ) {
                // make them also bold

                filteredList.add(listAccount.get(i));
                //highlightText(query,jTitle);
                //  resultView.setVisibility(View.GONE);
                // listTask.get(i).setText(sb);
            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewAccount.setLayoutManager(mLayoutManager);
        accountAdapter = new AccountAdapter(getContext(),filteredList,this,this);
        recyclerViewAccount.setAdapter(accountAdapter);
        accountAdapter.notifyDataSetChanged();

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
        searchView.setIconified(true);
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
    public void onBackPressed()
    {
        displayPreviousFragment(currentFragment);
    }

    public void displayPreviousFragment(String currentFragment)
    {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (currentFragment)
        {

            case "Fragment_3"    :   fragment = new DashboardFragement();     break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.commit();
        }
    }
}

