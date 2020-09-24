package com.genworks.oppm.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.widget.TextView;


import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.genworks.oppm.Adapter.ContactAdapter;
import com.genworks.oppm.LoadingDialog;
import com.genworks.oppm.model.ContactModel;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.PreferenceManagerContact;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactFragment extends Fragment implements ContactAdapter.MyItemClickListener, ContactAdapter.MyEditItemClickListner {

    private ContactAdapter contactAdapter;
    private RecyclerView recyclerViewContact;
    Fragment fragment;
    private LoadingDialog loadingDialog;
    String contact_id;
    private boolean isonetimelogin=false;
    private androidx.appcompat.widget.Toolbar searchtollbar;
    String sessionId;
    private LinearLayout linearLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String mobie;
    Menu search_menu;
    MenuItem item_search;
    private ArrayList<ContactModel> listContact;
    Gson gson;
    private ProgressDialog progressDialog = null;
    private Object values;
    private TextView account_name, assigned_to, comment, salutation, firstname, lastname, location, specilization, job_title, contactid, contact_source, contact_type, email, mobile, createdtime, modifiedtime, modifiedby;
    private final Handler handler = new Handler();
    private Button closebtn;
    ProgressDialog progressDoalog;
    public static final String ARG_PAGE = "ARG_PAGE";
    private boolean isDuplicate = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment

       // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Contact List");

        View rootView = inflater.inflate(R.layout.account, container, false);
        setSearchtollbar();

        setHasOptionsMenu(true);
        FloatingActionButton fb = rootView.findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDuplicate = false;
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                fragment = new ContactCreateFragement();
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });
        loadingDialog = new LoadingDialog(getContext());
        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }
        recyclerViewContact = rootView.findViewById(R.id.recyclerOpportunity);
        listContact = new ArrayList<>();

        contactAdapter = new ContactAdapter(requireContext(), listContact,this,this);
        String apiResponseStr = PreferenceManagerContact.getInstance(requireContext()).getAPIResponseContact(); // so you have to press your refresh buttonok
        if (!apiResponseStr.equals("")) {
            Gson gson = new Gson();
            SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
            listContact.clear();
            workingOnResponse(obj);
            contactAdapter.notifyDataSetChanged();

        }else {
            listContact.clear();
            fetchJSON();
            contactAdapter.notifyDataSetChanged();
        }



        contactAdapter.notifyDataSetChanged();
        return rootView;
    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void workingOnResponse(final SyncModule syncModule) {
     loadingDialog.show();
     loadingDialog.setCanceledOnTouchOutside(false);
     String success = syncModule.getSuccess();

        if (success.equals("true")) {

            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                contact_id = syncUpdated.getId();

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String salutationtype = "";
                String firstname = "";
                String lastname = "";
                String accounName = "";
                String specialization = "";
                String jobTitle = "";
                String contactType = "";
                String emailID = "";
                String mobilePhone = "";
                String contactsource = "";
                String createdtime = "";
                String modifiedtime = "";
                String modifiedby = "";
                String comment = "";
                String assigned = "";
                String assigned_id = "";
                String contactid = "";
                String created_by = "";

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("salutationtype")) {
                                salutationtype = String.valueOf(values);
                            } else if (name.equals("firstname")) {
                                firstname = String.valueOf(values);
                            } else if (name.equals("lastname")) {
                                lastname = String.valueOf(values);
                            } else if (name.equals("account_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//
                                    accounName = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("assigned_user_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    assigned_id = jsonObject.getString("value");
                                    assigned = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }
                            } else if (name.equals("cf_905")) {
                                specialization = String.valueOf(values);

                            } else if (name.equals("cf_907")) {
                                jobTitle = String.valueOf(values);

                            } else if (name.equals("cf_909")) {
                                contactType = String.valueOf(values);

                            } else if (name.equals("mobile")) {
                                mobilePhone = String.valueOf(values);

                            } else if (name.equals("email")) {
                                emailID = String.valueOf(values);

                            } else if (name.equals("cf_979")) {
                                contactsource = String.valueOf(values);

                            } else if (name.equals("createdtime")) {
                                createdtime = String.valueOf(values);
                            } else if (name.equals("modifiedtime")) {
                                modifiedtime = String.valueOf(values);
                            } else if (name.equals("cf_985")) {
                                comment = String.valueOf(values);
                            } else if (name.equals("contact_no")) {
                                contactid = String.valueOf(values);
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
                    }


                    PreferenceManagerContact.getInstance(requireContext()).setMultipleContactData(salutationtype, firstname, lastname, accounName, specialization, jobTitle, contactType, emailID, mobilePhone, createdtime, contactsource, modifiedtime, modifiedby, comment, assigned, contactid, contact_id);
                }
               // loadingDialog.dismiss();
                ContactModel contactModel = new ContactModel(salutationtype, firstname, lastname, accounName, specialization, jobTitle, contactType, emailID, mobilePhone, createdtime, contactsource, modifiedtime, modifiedby, comment, assigned, contactid, created_by, contact_id);
                listContact.add(contactModel);
                recyclerViewContact.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                recyclerViewContact.setAdapter(contactAdapter);
                contactAdapter.notifyDataSetChanged();
            }
        }
        loadingDialog.dismiss();
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
                progressDialog.show();

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "syncModuleRecords";
                String module = "Contacts";
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
                            PreferenceManagerContact.getInstance(requireContext()).setAPIResponseContact(json);

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
        return;
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
                listContact.clear();
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

        String salutations = listContact.get(position).getSalutationtype();
        String firstnames = listContact.get(position).getFirstname();
        String lastnames = listContact.get(position).getLastname();
        String speclization_name = listContact.get(position).getSpecialization();
        String jobtile = listContact.get(position).getJobTitle();
        String assigned_tos = listContact.get(position).getAssigned();
        String contact_ids = listContact.get(position).getContactid();
        String emails = listContact.get(position).getEmailID();
        String mobiles = listContact.get(position).getMobilePhone();
        String contactsource = listContact.get(position).getContactsource();
        String contacttype = listContact.get(position).getContactType();
        String createdtimes = listContact.get(position).getCreatedtime();
        String modifiedtimes = listContact.get(position).getModifiedtime();
        String modifiedbys = listContact.get(position).getModifiedby();
        //String comments=listContact.get(position).getComment();
        String account_names = listContact.get(position).getAccounName();
        String contact_id = listContact.get(position).getContact_id();

        Fragment fragment = new ContactCreateFragement();
        Bundle args = new Bundle();
        args.putString("salutations", salutations);
        args.putString("firstnames", firstnames);
        args.putString("lastnames", lastnames);
        args.putString("speclization_name", speclization_name);
        args.putString("jobtile", jobtile);
        args.putString("assigned_tos", assigned_tos);
        args.putString("contact_ids", contact_ids);
        args.putString("emails", emails);
        args.putString("mobiles", mobiles);
        args.putString("contactsource", contactsource);
        Log.d("contactsource",contactsource);
        args.putString("contacttype", contacttype);
        args.putString("account_names", account_names);
        args.putString("contact_id",contact_id);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    @Override
    public void myItemClick(int position) {

        String salutations = listContact.get(position).getSalutationtype();
        String firstnames = listContact.get(position).getFirstname();
        String lastnames = listContact.get(position).getLastname();
        String speclizations = listContact.get(position).getSpecialization();
        String jobtile = listContact.get(position).getJobTitle();
        String assigned_tos = listContact.get(position).getAssigned();
        String contact_ids = listContact.get(position).getContactid();
        String emails = listContact.get(position).getEmailID();
        String mobiles = listContact.get(position).getMobilePhone();
        String contactsource = listContact.get(position).getContactsource();
        Log.d("contactsource",contactsource);
        String contacttype = listContact.get(position).getContactType();
        String createdtimes = listContact.get(position).getCreatedtime();
        String modifiedtimes = listContact.get(position).getModifiedtime();
        String modifiedbys = listContact.get(position).getModifiedby();
        //String comments=listContact.get(position).getComment();
        String account_names = listContact.get(position).getAccounName();


        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_contact, null);

        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.color.trans));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        salutation = (TextView) customView.findViewById(R.id.salutation);
        firstname = (TextView) customView.findViewById(R.id.firstname);
        lastname = (TextView) customView.findViewById(R.id.lastname);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        specilization = (TextView) customView.findViewById(R.id.specilization);
        job_title = (TextView) customView.findViewById(R.id.job_title);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contactid = (TextView) customView.findViewById(R.id.contact_id);
        email = (TextView) customView.findViewById(R.id.email);
        mobile = (TextView) customView.findViewById(R.id.mobile);
        contact_source = (TextView) customView.findViewById(R.id.contact_source);
        location = (TextView) customView.findViewById(R.id.location);
        contact_type = (TextView) customView.findViewById(R.id.contact_type);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        // comment = (TextView) customView.findViewById(R.id.comment);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        salutation.setText(salutations);
        firstname.setText(firstnames);
        lastname.setText(lastnames);
        account_name.setText(account_names);
        createdtime.setText(createdtimes);
        modifiedtime.setText(modifiedtimes);
        modifiedby.setText(modifiedbys);
        specilization.setText(speclizations);
        job_title.setText(jobtile);
        contactid.setText(contact_ids);
        contact_source.setText(contactsource);
        contact_type.setText(contacttype);
        //comment.setText(comments);
        email.setText(emails);
        mobile.setText(mobiles);
        assigned_to.setText(assigned_tos);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });



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
        ArrayList<ContactModel> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listContact.size(); i++) {

            final String first_name = listContact.get(i).getFirstname().toLowerCase();
            final String last_name = listContact.get(i).getLastname().toLowerCase();
            final String account_name = listContact.get(i).getAccounName().toLowerCase();
            //  final String jLoc = mDataList.get(i).getJob_location().toLowerCase();
            if (first_name.contains(query)||last_name.contains(query)||account_name.contains(query) ) {
                // make them also bold

                filteredList.add(listContact.get(i));
                //highlightText(query,jTitle);
                //  resultView.setVisibility(View.GONE);
                // listTask.get(i).setText(sb);
            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewContact.setLayoutManager(mLayoutManager);
        contactAdapter = new ContactAdapter(getContext(),filteredList,this,this);
        recyclerViewContact.setAdapter(contactAdapter);
        contactAdapter.notifyDataSetChanged();

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
}

