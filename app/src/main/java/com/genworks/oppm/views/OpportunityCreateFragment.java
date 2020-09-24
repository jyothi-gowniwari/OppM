package com.genworks.oppm.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.genworks.oppm.Adapter.CustomAdapter;
import com.genworks.oppm.Adapter.ProductAdapter;
import com.genworks.oppm.BuildConfig;
import com.genworks.oppm.model.AccountDescribe;
import com.genworks.oppm.model.DescribeDetails;
import com.genworks.oppm.model.Fields;
import com.genworks.oppm.model.LocationModule;
import com.genworks.oppm.model.LocationRecords;
import com.genworks.oppm.model.PickListValues;
import com.genworks.oppm.model.Records;
import com.genworks.oppm.model.RecordsAccounts;
import com.genworks.oppm.model.RecordsContacts;
import com.genworks.oppm.model.RecordsProducts;
import com.genworks.oppm.model.Results_Account;
import com.genworks.oppm.model.Results_AccountQ;
import com.genworks.oppm.model.Results_ContactQ;
import com.genworks.oppm.model.Results_Locations;
import com.genworks.oppm.model.SaveContactModule;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.model.TypeDetails;
import com.genworks.oppm.model.UserAccountQuery;
import com.genworks.oppm.model.UserContactQuery;
import com.genworks.oppm.model.Users;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.Constants;
import com.genworks.oppm.Utils.FileCompressor;
import com.genworks.oppm.Utils.TinyDB;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpportunityCreateFragment extends Fragment {

    private final Handler handler = new Handler();
    private ProgressDialog progressDialog = null;
    static final int REQUEST_TAKE_PHOTO = 1000;
    static final int REQUEST_GALLERY_PHOTO = 2000;
    private String termsconditions, quantity_product, unitprice_product, isDuplicate, firstname, lastname, opprtunity_id, locationids, pndt_name, multi_product, time_start, strDate, support_name, winprob_name, saletype_name, prospect_name, interest_name, rating_name, department_name, lead_name, sessionId, ids, sales_name_value, product_id, productname, account_id, contact_id, accountnames, contactfirstnames, contactlastnames;
    private ArrayList<PickListValues> pickListSalesStage = new ArrayList<>();
    private MaterialSpinner spinnerpndt, spinnermulti, spinnersupport, spinnerwinprob, spinnersaletype, spinnerprospect, spinnersalesstage, spinnerassignedto, spinnerlocation, spinnerleadsource, spinnerintrest, spinnersalesperson, spinnerrating, spinnerdepartment;
    private ArrayList<String> lost_reasonlist, pndt_list, multi_list, support_list, account_name, contact_name, product_name, assigned_names, location_list, lead_sorcelist, sales_nameslist, department_list, rating_list, interesttype_list, prospect_list, saletype_list, winprob_list;
    private TextInputEditText opportunity_name, remark_sales, expected_po_date, expected_delivery_date;
    private ArrayList<Records> records;
    private TinyDB tinydb;
    String sourceRecord,contactnames;
    private File file;
    private Uri selectedImage;
    private static final int IMAGE = 100;
    TextView pancard, no_file_pan, aadhar_card, pndt, purchase_order, cheque, first_account, close_deal;
    String termscondition = "";
    private ArrayList<String> assignedlist;
    private ImageView add_account, add_contact;
    private ArrayList<String> sales_stagenames;
    private Object values;
    private TableLayout stk;
    File mPhotoFile;
    int serverResponseCode = 0;

    String upLoadServerUri = null,locationidss;
    ProgressDialog dialog = null;
    private String notes_title, pan;
    FileCompressor mCompressor;
    private LinearLayout linearLayout;
    private PopupWindow popupWindow;
    private Uri realUri;
    public static final int GALLEY_REQUEST_CODE = 10;
    private Button close;
    private AppCompatCheckBox demodonecheck;
    private ArrayList<LocationRecords> locationList = new ArrayList<>();
    private AppCompatButton save, cancel;
    private ImageView calendarexdeliverydate, calendarexpodate, calendardemodate;
    private CustomAdapter adapter;
    private AppCompatAutoCompleteTextView autoaccount_name, autoconatct_name, autoopportunity_name, autoproduct_name;
    Calendar calendar;
    LinearLayout productd, grands, adjustments, deducatedtaxs, taxcharges, pretaxs, chargess, discounts, itemtotals, netprices, taxs;
    DatePickerDialog datePickerDialog;
    int year;
    private String productnames, remarks, competition_names, competition_prices, lostreason_name, edit_salesstage, edit_opportunityname, edit_contact, edit_account, edit_location, edit_leadsource, edit_remark, edit_terms, edit_total, edit_itemname, edit_descriptions, edit_pdflink, edit_multideal, edit_sitereads, edit_fundingreq, edit_quantity,
            userids, assigned_id, salesperson_id, edit_assigned, edit_employee, edit_si, edit_demodone, edit_pndts, edit_deldate, edit_exppodate, edit_supportrequired, edit_winprob, edit_salestype, edit_prospecttype;
    int month;
    int dayOfMonth;
    String filename;
    //Bitmap to get image from gallery
    private Bitmap bitmap;
    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    //Uri to store the image uri
    private Uri filePath;
    private ArrayList<LocationRecords> locationRecords;
    private ArrayList<PickListValues> leadsourceList = new ArrayList<>();
    private ArrayList<PickListValues> lostreasonList = new ArrayList<>();
    private ArrayList<PickListValues> departmentList = new ArrayList<>();
    private ArrayList<PickListValues> ratingList = new ArrayList<>();
    private ArrayList<PickListValues> interesttypeList = new ArrayList<>();
    private ArrayList<PickListValues> prospectList = new ArrayList<>();
    private ArrayList<PickListValues> saletypeList = new ArrayList<>();
    private ArrayList<PickListValues> winprobList = new ArrayList<>();
    private ArrayList<PickListValues> supportList = new ArrayList<>();
    private ArrayList<PickListValues> multiList = new ArrayList<>();
    private ArrayList<PickListValues> pndtList = new ArrayList<>();
    private Fragment fragment;
    private ArrayList<RecordsAccounts> recordsListAccount = new ArrayList<>();
    RecyclerView recyclerViewAccount, recyclerViewContact;
    EditText searchContact, searchAccount;
    JsonElement usernames;
    RequestBody requestFile;
    //Image request code
    private int PICK_IMAGE_REQUEST = 1;
    private String productValue = "", productID = "", grandtotal = "", pre_tax_total = "", quantity = "", unit_price = "", unitprice = "";
    ;
    private TextView listprice, total, product_details, tax, texttaxcha, textded;
    private ProductAdapter adapter1;
    private EditText searchProduct;
    private ArrayList<RecordsProducts> recordsProducts;
    private ArrayList<RecordsAccounts> recordsAccounts;
    private ArrayList<RecordsContacts> recordsContacts;
    private TableLayout tables;
    RecyclerView recyclerViewProduct;
    private TextView lead_details, grand_total, pretax, quantitys, unit_prices, textdis, textcharge;
    private ArrayList<SynFields> productList = new ArrayList<>();
    private TextInputEditText descrption, demo_date, terms_condition;
    private ArrayList<RecordsProducts> recordsListProduct = new ArrayList<>();
    private LinearLayout exp_po_dates, descrptions, site, funding_requireds, demo_dates, si, opportunity, demo_dones, exp_del_date;
    private TextInputLayout rating, intrest_types, multi_product_deals, sales_types, win_probs, support_requireds, pndts;
    private ArrayList<Records> recordsList = new ArrayList<>();
    private CheckBox s_i;
    private boolean isonetimelogin=false;
    private ArrayList<RecordsContacts> recordsListContact = new ArrayList<>();
    private ArrayList<RecordsProducts> productrecordsList = new ArrayList<>();
    private ArrayList<Users> usersListAccount = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment

        ((MainActivity) getActivity()).setActionBarTitle("CREATE NEW OPPORTUNITY");
        View rootView = inflater.inflate(R.layout.opportunity_form, container, false);
        setHasOptionsMenu(true);
        sales_stagenames = new ArrayList<String>();
        assigned_names = new ArrayList<>();
        sales_nameslist = new ArrayList<>();
        saletype_list = new ArrayList<>();
        winprob_list = new ArrayList<>();
        pndt_list = new ArrayList<>();
        multi_list = new ArrayList<>();
        sales_nameslist.add("Secondary Sales Person");
        department_list = new ArrayList<>();
        rating_list = new ArrayList<>();
        interesttype_list = new ArrayList<>();
        prospect_list = new ArrayList<>();
        support_list = new ArrayList<>();
        spinnersalesstage = rootView.findViewById(R.id.sales_stage);
        spinnerprospect = rootView.findViewById(R.id.prospect_type);
        productd = rootView.findViewById(R.id.productd);
        spinnersaletype = rootView.findViewById(R.id.sales_type);
        spinnerwinprob = rootView.findViewById(R.id.win_prob);
        exp_po_dates = rootView.findViewById(R.id.exp_po_dates);
        terms_condition = rootView.findViewById(R.id.terms_condition);
        add_account = rootView.findViewById(R.id.add_account);
        add_contact = rootView.findViewById(R.id.add_contact);
        stk = rootView.findViewById(R.id.tables);
        product_details = rootView.findViewById(R.id.product_details);
        expected_po_date = rootView.findViewById(R.id.exp_date);
        expected_po_date.setHint(Html.fromHtml(expected_po_date.getHint() + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        grand_total = rootView.findViewById(R.id.grand_total);
        pretax = rootView.findViewById(R.id.pretax);
        quantitys = rootView.findViewById(R.id.quantity);
        unit_prices = rootView.findViewById(R.id.listprice);
        autoaccount_name = rootView.findViewById(R.id.account_name);
        autoconatct_name = rootView.findViewById(R.id.contact_name);
        autoproduct_name = rootView.findViewById(R.id.product_name);
        expected_delivery_date = rootView.findViewById(R.id.exp_del_dates);
        expected_delivery_date.setHint(Html.fromHtml(expected_delivery_date.getHint() + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        grands = rootView.findViewById(R.id.grands);
        adjustments = rootView.findViewById(R.id.adjustments);
        deducatedtaxs = rootView.findViewById(R.id.deducatedtaxs);
        taxcharges = rootView.findViewById(R.id.taxcharges);
        textcharge = rootView.findViewById(R.id.textcharge);
        pretaxs = rootView.findViewById(R.id.pretaxs);
        chargess = rootView.findViewById(R.id.chargess);
        discounts = rootView.findViewById(R.id.discounts);
        itemtotals = rootView.findViewById(R.id.itemtotals);
        netprices = rootView.findViewById(R.id.netprices);
        taxs = rootView.findViewById(R.id.taxs);
        tax = rootView.findViewById(R.id.tax);
        textdis = rootView.findViewById(R.id.textdis);
        texttaxcha = rootView.findViewById(R.id.texttaxcha);
        textded = rootView.findViewById(R.id.textded);
        rating = rootView.findViewById(R.id.ratings);
        spinnersupport = rootView.findViewById(R.id.support_required);
        win_probs = rootView.findViewById(R.id.win_probs);
        support_requireds = rootView.findViewById(R.id.support_requireds);
        sales_types = rootView.findViewById(R.id.sales_types);
        lead_details = rootView.findViewById(R.id.lead_details);
        intrest_types = rootView.findViewById(R.id.intrest_types);
        spinnerassignedto = rootView.findViewById(R.id.assigned_to);
        save = rootView.findViewById(R.id.save);
        cancel = rootView.findViewById(R.id.cancel);
        opportunity_name = rootView.findViewById(R.id.editoppname);
        opportunity_name.setHint(Html.fromHtml(opportunity_name.getHint() + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnermulti = rootView.findViewById(R.id.multi_product_deal);
        spinnerpndt = rootView.findViewById(R.id.pndt);
        demodonecheck = rootView.findViewById(R.id.demodonecheck);
        demo_dones = rootView.findViewById(R.id.demo_dones);
        autoaccount_name.setHint(Html.fromHtml("Account Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        autoconatct_name.setHint(Html.fromHtml("Contact Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
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

        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
            Log.d("session_IdMAin",sessionId);
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }
        multi_product_deals = rootView.findViewById(R.id.multi_product_deals);
        si = rootView.findViewById(R.id.si);
        s_i = rootView.findViewById(R.id.s_i);
        listprice = rootView.findViewById(R.id.listprice);
        total = rootView.findViewById(R.id.totals);
        demo_dates = rootView.findViewById(R.id.demo_dates);
        demo_date = rootView.findViewById(R.id.demo_date);
        demo_date.setHint(Html.fromHtml(demo_date.getHint() + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        opportunity = rootView.findViewById(R.id.opportunity);
        funding_requireds = rootView.findViewById(R.id.funding_requireds);
        descrptions = rootView.findViewById(R.id.descrptions);
        site = rootView.findViewById(R.id.site);
        pndts = rootView.findViewById(R.id.pndts);
        exp_del_date = rootView.findViewById(R.id.exp_del_date);
        demo_dones = rootView.findViewById(R.id.demo_dones);
        spinnerlocation = rootView.findViewById(R.id.location);
        spinnerleadsource = rootView.findViewById(R.id.lead_source);
        remark_sales = rootView.findViewById(R.id.remark);
        remark_sales.setHint(remark_sales.getHint());
        spinnerintrest = rootView.findViewById(R.id.intrest_type);
        calendarexpodate = rootView.findViewById(R.id.calendarexpodate);
        calendarexdeliverydate = rootView.findViewById(R.id.calendarexdeliverydate);
        calendardemodate = rootView.findViewById(R.id.calendarDemoDate);
        descrption = rootView.findViewById(R.id.descrption);
        spinnerdepartment = rootView.findViewById(R.id.department);
        spinnerrating = rootView.findViewById(R.id.rating);
        spinnerintrest = rootView.findViewById(R.id.intrest_type);
        spinnersalesperson = rootView.findViewById(R.id.sales_person);
        //  tables=rootView.findViewById(R.id.tables);
        location_list = new ArrayList<>();
        assignedlist = new ArrayList<>();
        lead_sorcelist = new ArrayList<>();
        product_name = new ArrayList<>();
        account_name = new ArrayList<>();
        contact_name = new ArrayList<>();
        lost_reasonlist = new ArrayList<>();
        tinydb = new TinyDB(getContext());
        @SuppressLint("ResourceAsColor")
        ColorStateList colorStateList = ColorStateList.valueOf(R.color.black);
        ViewCompat.setBackgroundTintList(autoaccount_name, colorStateList);
        ViewCompat.setBackgroundTintList(opportunity_name, colorStateList);
        ViewCompat.setBackgroundTintList(autoconatct_name, colorStateList);
        ViewCompat.setBackgroundTintList(autoproduct_name, colorStateList);
        ViewCompat.setBackgroundTintList(remark_sales, colorStateList);
        ViewCompat.setBackgroundTintList(descrption, colorStateList);
        ViewCompat.setBackgroundTintList(expected_po_date, colorStateList);
        ViewCompat.setBackgroundTintList(expected_delivery_date, colorStateList);
        ViewCompat.setBackgroundTintList(demo_date, colorStateList);
        ViewCompat.setBackgroundTintList(terms_condition, colorStateList);

        firstname = getActivity().getIntent().getStringExtra("firstname");
        lastname = getActivity().getIntent().getStringExtra("lastname");
        isDuplicate = getArguments().getString("isDuplicate");

        Log.d("isDuplicate", isDuplicate);
        if (isDuplicate.equals("true")) {


            edit_salesstage = getArguments().getString("sales_stage");
            Log.d("edit_salesstage", edit_salesstage);
            edit_opportunityname = getArguments().getString("opp_names");
            opportunity_name.setText(edit_opportunityname);
            edit_contact = getArguments().getString("contact");
            autoconatct_name.setText(edit_contact);
            edit_account = getArguments().getString("account_names");
            autoaccount_name.setText(edit_account);
            edit_location = getArguments().getString("locations");
            edit_leadsource = getArguments().getString("leadsources");
            edit_remark = getArguments().getString("remark_sales_persons");
            remark_sales.setText(edit_remark);
            edit_prospecttype = getArguments().getString("prospect_types");
            edit_salestype = getArguments().getString("sale_types");
            edit_winprob = getArguments().getString("winprobValues");
            edit_supportrequired = getArguments().getString("support_requireds");
            edit_exppodate = getArguments().getString("exp_po_date");
            expected_po_date.setText(edit_exppodate);
            edit_deldate = getArguments().getString("exp_del_dates");
            expected_delivery_date.setText(edit_deldate);
            opprtunity_id = getArguments().getString("opportunity_ids");
           // Log.d("opprtunity_id",opprtunity_id);
            edit_pndts = getArguments().getString("pndtsopp");
            edit_demodone = getArguments().getString("demo_dones");
            demo_date.setText(edit_demodone);
            edit_si = getArguments().getString("s_is");
            edit_fundingreq = getArguments().getString("fun_requireds");
            edit_sitereads = getArguments().getString("site_reads");
            edit_multideal = getArguments().getString("multi_deals");
            edit_pdflink = getArguments().getString("pdf_link");
            edit_descriptions = getArguments().getString("descriptions");
            edit_itemname = getArguments().getString("item_list");
            // autoproduct_name.setText(edit_itemname);
            edit_quantity = getArguments().getString("quantitys");
            edit_total = getArguments().getString("totals");
            edit_terms = getArguments().getString("termsnconditions");
            edit_assigned = getArguments().getString("assignedto");
            Log.d("edit_assigned", edit_assigned);
            edit_employee = getArguments().getString("employee");
            String terms = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                terms = String.valueOf((Html.fromHtml(edit_terms, Html.FROM_HTML_MODE_LEGACY)));
            } else {
                terms = String.valueOf((Html.fromHtml(edit_terms)));
            }
            terms_condition.setText(terms);

        }
        String apiResponseLocation = tinydb.getString("jsonLocationopp");
        String apiResponseAccount = tinydb.getString("jsonAccount");
        String apiResponseContact = tinydb.getString("jsonContact");
        String apiResponseAllOpportunity = tinydb.getString("jsonAllOpportunity");
        String apiResponseProductsPotentials = tinydb.getString("jsonAllProductsPotentials");
        String apiResponseProducts = tinydb.getString("jsonAllProducts");
        if (!apiResponseLocation.equals("")) {
            Gson gson = new Gson();
            LocationModule locationModule = gson.fromJson(apiResponseLocation, LocationModule.class);
            workingOnResponseLocation(locationModule);
        }
        if (!apiResponseAccount.equals("")) {
            Gson gson = new Gson();
            UserAccountQuery userAccountQuery = gson.fromJson(apiResponseAccount, UserAccountQuery.class);
            workingOnResponseAccount(userAccountQuery);
        }
        if (!apiResponseContact.equals("")) {
            Gson gson = new Gson();
            UserContactQuery userContactQuery = gson.fromJson(apiResponseContact, UserContactQuery.class);
            workingOnResponseContact(userContactQuery);
        }
        if (!apiResponseAllOpportunity.equals("")) {
            Gson gson = new Gson();
            AccountDescribe accountDescribe = gson.fromJson(apiResponseAllOpportunity, AccountDescribe.class);
            workingOnResponseAll(accountDescribe);
        }
        if (!apiResponseProducts.equals("")) {
            Gson gson = new Gson();
            SyncModule syncModule = gson.fromJson(apiResponseProducts, SyncModule.class);
            workingOnResponseProduct(syncModule);
        }
        if (!apiResponseProductsPotentials.equals("")) {
            Gson gson = new Gson();
            SyncModule syncModule = gson.fromJson(apiResponseProductsPotentials, SyncModule.class);
            workingOnResponseProductPotential(syncModule);
        } else {
            fetchJSON();
            fetchLocationJSON();
            fetchProductnameJSON();
            fetchProductPotentialJSON();
            fetchAccountJSON();
            fetchContactJSON();
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (opprtunity_id != null) {
//                    updaterecords();
//                } else {
//                    saverecords();
//                }
                saverecords();
            }
        });
        //  saveRecord();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SalesStageFragment();
                loadFragment(fragment);
            }
        });

        tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.gst_tax, null);
                AppCompatButton save, cancel;
                TextInputEditText remark, competition_name, competition_price;
                AppCompatButton submit;
                MaterialSpinner lost_reason;
                close = customView.findViewById(R.id.close);
                linearLayout = customView.findViewById(R.id.card_details);

                popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);


            }
        });
        textcharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.charges, null);
                AppCompatButton save, cancel;
                TextInputEditText remark, competition_name, competition_price;
                AppCompatButton submit;
                MaterialSpinner lost_reason;
                close = customView.findViewById(R.id.close);
                linearLayout = customView.findViewById(R.id.card_details);

                popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);


            }
        });

        textdis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.overall_discount, null);
                AppCompatButton save, cancel;
                TextInputEditText remark, competition_name, competition_price;
                AppCompatButton submit;
                MaterialSpinner lost_reason;
                close = customView.findViewById(R.id.close);
                linearLayout = customView.findViewById(R.id.card_details);

                popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
            }
        });
        texttaxcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.tax_charge, null);
                AppCompatButton save, cancel;
                TextInputEditText remark, competition_name, competition_price;
                AppCompatButton submit;
                MaterialSpinner lost_reason;
                close = customView.findViewById(R.id.close);
                linearLayout = customView.findViewById(R.id.card_details);

                popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
            }
        });

        textded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.deducted_tax, null);
                AppCompatButton save, cancel;
                TextInputEditText remark, competition_name, competition_price;
                AppCompatButton submit;
                MaterialSpinner lost_reason;
                close = customView.findViewById(R.id.close);
                linearLayout = customView.findViewById(R.id.card_details);

                popupWindow = new PopupWindow(customView,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        true);
                popupWindow.setFocusable(true);
                popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
            }
        });

        spinnersalesstage.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesstage.getWindowToken(), 0);
                return false;
            }
        });
        spinnerlocation.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerlocation.getWindowToken(), 0);
                return false;
            }
        });
        spinnerassignedto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerassignedto.getWindowToken(), 0);
                return false;
            }
        });
        spinnerleadsource.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnerleadsource.getWindowToken(), 0);
                return false;
            }
        });
        spinnersalesperson.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesperson.getWindowToken(), 0);
                return false;
            }
        });
        spinnersupport.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesperson.getWindowToken(), 0);
                return false;
            }
        });
        spinnerrating.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesperson.getWindowToken(), 0);
                return false;
            }
        });
        spinnermulti.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesperson.getWindowToken(), 0);
                return false;
            }
        });
        spinnerpndt.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(spinnersalesperson.getWindowToken(), 0);
                return false;
            }
        });


        calendarexpodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                strDate = mdformat.format(calendar.getTime());

                display(strDate);
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                expected_po_date.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        expected_delivery_date.setText(date);
        expected_po_date.setText(date);
        calendarexdeliverydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                strDate = mdformat.format(calendar.getTime());

                display(strDate);
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                expected_delivery_date.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        calendardemodate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
                strDate = mdformat.format(calendar.getTime());

                display(strDate);
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                demo_date.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        return rootView;

    }

//    private void upoloadpan() {
//
//        //create file which we want to send to server.
//        File imageFIle = new File(String.valueOf(realUri));
//
//        //request body is used to attach file.
//        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),imageFIle);
//
//        //and request body and file name using multipart.
//        MultipartBody.Part image = MultipartBody.Part.createFormData("image", imageFIle.getName(),requestBody); //"image" is parameter for photo in API.
//
//        Call<ResponsePojo> call = api.submitData(image); //we will get our response in call variable.
//
//        call.enqueue(new Callback<ResponsePojo>() {
//            @Override
//            public void onResponse(Call<ResponsePojo> call, Response<ResponsePojo> response) {
//                ResponsePojo body = response.body(); //get body from response.
//
//
//                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//                alert.setMessage(body.getMessage()); //display response in Alert dialog.
//                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                alert.show();
//            }
//
//            @Override
//            public void onFailure(Call<ResponsePojo> call, Throwable t) {
//
//            }
//        });
//
//    }


    private void display(String num) {
        // TextView textView = (TextView) findViewById(R.id.current_time_view);
        //textView.setText(num);
        time_start = num;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //    private void saveRecord() {
//
//        spinnersalesperson.getSelectedItem().toString();
//
//
//    }
    private void fetchAccountJSON() {

        autoaccount_name.setHint(Html.fromHtml("Account Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));

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
                            tinydb.putString("jsonAccount", jsonAccount);
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
        return;
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

    private void fetchContactJSON() {
        autoconatct_name.setHint(Html.fromHtml("Contact Name" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));

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
                            tinydb.putString("jsonContact", jsonContact);
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
        return;
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


    // String sales_name_value1=spinnersalesstage.getSelectedItem().toString();
    private void fetchJSON() {

        // String sales= String.valueOf(sales_stagenames.add("Sales Stage"));

        spinnersalesstage.setHint(Html.fromHtml("Sales Stage" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerassignedto.setHint(Html.fromHtml("Assigned To" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerleadsource.setHint(Html.fromHtml("Lead Source" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerdepartment.setHint(Html.fromHtml("Department" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerintrest.setHint(Html.fromHtml("Interest Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerprospect.setHint(Html.fromHtml("Prospect Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnersaletype.setHint(Html.fromHtml("Sale Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerpndt.setHint(Html.fromHtml("PNDT / AERB Availbale" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnersalesperson.setHint(Html.fromHtml("Secondary Sales Person" + ""));
        spinnerwinprob.setHint(Html.fromHtml("Win Probability " + ""));
        termsconditions = "Price           : Price offered for the above configuration only.<br />Tax             : Above mentioned price is inclusive of custom duty, Excise, Freight up to site and GST.<br />                    Local levies such as Octroi / Entry tax if any, the same will be at Purchaser’s account.<br />Validity       : This price is valid for a week from the date of the proposal.<br />Payment      : 100% Advance<br />Installation  : Genworks Specialist / Representative at your site will give Training and Installation without any additional cost.<br />Warranty     : 12 Months from the date of installation or 13 Months from the date of Invoice. Warraty will be provided for hardware items only and consumables items are not a part of warranty.</p>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            termscondition = String.valueOf((Html.fromHtml(termsconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            termscondition = String.valueOf((Html.fromHtml(termsconditions)));
        }

        terms_condition.setHint(termscondition);

        //terms_condition.setHint(terms_condition.getHint());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "describe";
                String module = "Potentials";
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
                            String jsonAllOpportunity = g.toJson(accountDescribe);
                            tinydb.putString("jsonAllOpportunity", jsonAllOpportunity);
                            workingOnResponseAll(accountDescribe);
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


        }, 0);
        return;
    }

    private void workingOnResponseAll(AccountDescribe accountDescribe) {

        spinnersalesstage.setHint(Html.fromHtml("Sales Stage" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerassignedto.setHint(Html.fromHtml("Assigned To" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerleadsource.setHint(Html.fromHtml("Lead Source" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerdepartment.setHint(Html.fromHtml("Department" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerintrest.setHint(Html.fromHtml("Interest Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerprospect.setHint(Html.fromHtml("Prospect Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnersaletype.setHint(Html.fromHtml("Sale Type" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnerpndt.setHint(Html.fromHtml("PNDT / AERB Availbale" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
        spinnersalesperson.setHint(Html.fromHtml("Secondary Sales Person" + ""));
        spinnerrating.setHint(Html.fromHtml("Win Probability " + ""));

        String termsconditions = "Price           : Price offered for the above configuration only.<br />Tax             : Above mentioned price is inclusive of custom duty, Excise, Freight up to site and GST.<br />                    Local levies such as Octroi / Entry tax if any, the same will be at Purchaser’s account.<br />Validity       : This price is valid for a week from the date of the proposal.<br />Payment      : 100% Advance<br />Installation  : Genworks Specialist / Representative at your site will give Training and Installation without any additional cost.<br />Warranty     : 12 Months from the date of installation or 13 Months from the date of Invoice. Warraty will be provided for hardware items only and consumables items are not a part of warranty.</p>";
        String termscondition = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            termscondition = String.valueOf((Html.fromHtml(termsconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            termscondition = String.valueOf((Html.fromHtml(termsconditions)));
        }

        terms_condition.setHint(termscondition);


        String success = accountDescribe.getSuccess();

        if (success.equals("true")) {
            Results_Account results = accountDescribe.getResult();

            DescribeDetails describeDetails = results.getDescribe();

            String value = "";

            String label = describeDetails.getLabel();
            if (label.equals("Opportunities")) {
                ArrayList<Fields> desFields = describeDetails.getFields();

                for (Fields desFields1 : desFields) {
                    String name = desFields1.getName();
                    //facility type
                    if (name.equals("sales_stage")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
                        for (PickListValues pickListValues1 : pickListValues) {
                            value = pickListValues1.getValue();
                            label = pickListValues1.getLabel();
                            sales_stagenames.add(value);
                            Log.d("size", String.valueOf(sales_stagenames.size()));
                            PickListValues pickListValues2 = new PickListValues(value, label);
                            pickListSalesStage.add(pickListValues2);
                            if (isDuplicate.equals("true")) {

                            } else {
                                if (value.equals("Closed Won")) {
                                    sales_stagenames.remove(value);
                                } else if (value.equals("Closed Lost")) {
                                    sales_stagenames.remove(value);
                                } else if (pickListValues2.equals("Closed Won")) {
                                    pickListSalesStage.remove(pickListValues2);
                                }
                            }

                        }
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, sales_stagenames) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                                    // spinnerdepartment.setVisibility(View.GONE);
                                    // spinnerdepartment.setVisibility(View.GONE);
                                    tv.setTextColor(Color.GRAY);
                                } else {
                                    tv.setTextColor(Color.BLACK);

                                }
                                return view;
                            }
                        };
                        // ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.borderdashboard.simple_spinner_item, taskTypes);

                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnersalesstage.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnersalesstage.getAdapter();
                        int position = adapter.getPosition(edit_salesstage);
                        spinnerArrayAdapter.notifyDataSetChanged();
                        spinnersalesstage.setSelection(position + 1);
                        spinnersalesstage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                                //  String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                if (position + 1 > 0) {


                                    PickListValues pickListValues3 = pickListSalesStage.get(position);
                                    sales_name_value = pickListValues3.getValue();
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + sales_name_value, Toast.LENGTH_SHORT)
//                                                                    .show();
                                    if (sales_name_value.equals("Lead")) {
                                        lead_details.setVisibility(View.VISIBLE);
                                        rating.setVisibility(View.VISIBLE);
                                        intrest_types.setVisibility(View.VISIBLE);
                                        spinnerdepartment.setVisibility(View.VISIBLE);
                                        descrptions.setVisibility(View.VISIBLE);
                                        opportunity.setVisibility(View.GONE);
                                        productd.setVisibility(View.GONE);


                                    } else if (isDuplicate.equals("true") && spinnersalesstage.getSelectedItem().toString().equals("Closed Won")) {

                                        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View customView = layoutInflater.inflate(R.layout.custom_popup_clossure, null);
                                        Button pan_card, aadhar_id;
                                        final AppCompatButton submit;
                                        close = customView.findViewById(R.id.close);
                                        linearLayout = customView.findViewById(R.id.card_details);
                                        popupWindow = new PopupWindow(customView,
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                true);
                                        popupWindow.setFocusable(true);
                                        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                                        aadhar_id = customView.findViewById(R.id.aadhar_id);
                                        pan_card = customView.findViewById(R.id.pancard_id);
                                        pancard = customView.findViewById(R.id.pan_card);
                                        no_file_pan = customView.findViewById(R.id.no_file_pan);
                                        pndt = customView.findViewById(R.id.pndt);
                                        cheque = customView.findViewById(R.id.cheque);
                                        first_account = customView.findViewById(R.id.first_account);
                                        close_deal = customView.findViewById(R.id.close_deal);
                                        purchase_order = customView.findViewById(R.id.purchase_order);
                                        pancard.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "* " + "</font>" + " " + pancard.getHint()));
                                        pndt.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "*" + "</font>" + "<font color=\"#ff0000\">" + "*" + "</font>" + " " + pndt.getHint()));
                                        purchase_order.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "*" + "</font>" + "<font color=\"#ff0000\">" + "*" + "</font>" + purchase_order.getHint()));
                                        cheque.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "*" + "</font>" + "<font color=\"#ff0000\">" + "*" + "</font>" + cheque.getHint()));
                                        first_account.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "* " + "</font>" + " " + first_account.getHint()));
                                        close_deal.setHint(Html.fromHtml("<font color=\"#ff0000\">" + "*" + "</font>" + "<font color=\"#ff0000\">" + "*" + "</font>" + close_deal.getHint()));


                                        submit = customView.findViewById(R.id.submit);


                                        pan_card.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                 selectImage();
                                               // Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                               // startActivityForResult(cameraIntent, 1000);
                                                notes_title = "PAN";
                                                pan = "1";
                                            }
                                        });
                                        // no_file_pan.setText(mPhotoFile.getAbsolutePath().substring(mPhotoFile.getAbsolutePath().lastIndexOf("/")+1));


                                        submit.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //  String image = convertToString();
                                                //  Log.d("image",image);
                                                saveclosedwon();
                                            }
                                        });


                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupWindow.dismiss();  //merge 3 into single listitem all the details are same but only product changed/ actually i don't understand what you say.

                                            }
                                        });

                                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                                        popupWindow.setOutsideTouchable(true);


                                    } else if (isDuplicate.equals("true") && spinnersalesstage.getSelectedItem().toString().equals("Closed Lost")) {

                                        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        View customView = layoutInflater.inflate(R.layout.custompopup_lost, null);
                                        AppCompatButton save, cancel;
                                        TextInputEditText remark, competition_name, competition_price;
                                        AppCompatButton submit;
                                        MaterialSpinner lost_reason;
                                        close = customView.findViewById(R.id.close);
                                        linearLayout = customView.findViewById(R.id.card_details);

                                        popupWindow = new PopupWindow(customView,
                                                LinearLayout.LayoutParams.MATCH_PARENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                true);
                                        popupWindow.setFocusable(true);
                                        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
                                        remark = customView.findViewById(R.id.remark);
                                        competition_name = customView.findViewById(R.id.competition_name);
                                        competition_price = customView.findViewById(R.id.competition_price);
                                        lost_reason = customView.findViewById(R.id.lost_reason);
                                        save = customView.findViewById(R.id.save);
                                        cancel = customView.findViewById(R.id.cancel);
                                        @SuppressLint("ResourceAsColor")
                                        ColorStateList colorStateList = ColorStateList.valueOf(R.color.black);
                                        ViewCompat.setBackgroundTintList(competition_name, colorStateList);
                                        ViewCompat.setBackgroundTintList(competition_price, colorStateList);
                                        ViewCompat.setBackgroundTintList(remark, colorStateList);
                                        competition_name.setHint(competition_name.getHint());
                                        competition_price.setHint(competition_price.getHint());
                                        lost_reason.setHint(Html.fromHtml("Lost Reason"));
                                        competition_names = competition_name.getText().toString();
                                        competition_prices = competition_price.getText().toString();
                                        remarks = remark.getText().toString();
                                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                                getContext(), R.layout.spinner_item, lost_reasonlist) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                                        lost_reason.setAdapter(spinnerArrayAdapter);
                                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) lost_reason.getAdapter();
                                        int position1 = adapter.getPosition(edit_leadsource);
                                        lost_reason.setSelection(position1 + 1);
                                        lost_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                            @Override
                                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                                String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                                if (position + 1 > 0) {

                                                    PickListValues pickListValues3 = lostreasonList.get(position);
                                                    lostreason_name = pickListValues3.getValue();
                                                    Log.e("lostreason_name", lostreason_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + lead_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                                }
                                            }

                                            @Override
                                            public void onNothingSelected(AdapterView<?> parent) {
                                            }
                                        });


                                        remark.setHint(Html.fromHtml(remark.getHint() + "<font color=\"#ff0000\">" + "* " + "</font>"));


                                        save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                saveclosedlost();
                                            }
                                        });


                                        close.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                popupWindow.dismiss();  //merge 3 into single listitem all the details are same but only product changed/ actually i don't understand what you say.

                                            }
                                        });

                                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                                        popupWindow.setOutsideTouchable(true);


                                    } else if (sales_name_value.equals("Opportunity")) {
                                        lead_details.setVisibility(View.GONE);
                                        sales_types.setVisibility(View.GONE);
                                        win_probs.setVisibility(View.GONE);
                                        support_requireds.setVisibility(View.GONE);
                                        exp_po_dates.setVisibility(View.GONE);
                                        exp_del_date.setVisibility(View.GONE);
                                        pndts.setVisibility(View.GONE);
                                        demo_dones.setVisibility(View.GONE);
                                        si.setVisibility(View.GONE);
                                        demo_dates.setVisibility(View.GONE);
                                        funding_requireds.setVisibility(View.GONE);
                                        site.setVisibility(View.GONE);
                                        multi_product_deals.setVisibility(View.GONE);
                                        rating.setVisibility(View.VISIBLE);
                                        intrest_types.setVisibility(View.VISIBLE);
                                        spinnerdepartment.setVisibility(View.VISIBLE);
                                        descrptions.setVisibility(View.VISIBLE);
                                        multi_product_deals.setVisibility(View.VISIBLE);
                                        funding_requireds.setVisibility(View.VISIBLE);
                                        sales_types.setVisibility(View.VISIBLE);

                                        opportunity.setVisibility(View.VISIBLE);
                                        win_probs.setVisibility(View.VISIBLE);
                                        si.setVisibility(View.VISIBLE);
                                        site.setVisibility(View.VISIBLE);
                                        exp_del_date.setVisibility(View.VISIBLE);
                                        expected_po_date.setVisibility(View.VISIBLE);
                                        exp_po_dates.setVisibility(View.VISIBLE);
                                        pndts.setVisibility(View.VISIBLE);
                                        support_requireds.setVisibility(View.VISIBLE);
                                        expected_delivery_date.setVisibility(View.VISIBLE);
                                        productd.setVisibility(View.VISIBLE);
                                        grands.setVisibility(View.VISIBLE);
                                        adjustments.setVisibility(View.VISIBLE);
                                        deducatedtaxs.setVisibility(View.VISIBLE);
                                        taxcharges.setVisibility(View.VISIBLE);
                                        pretaxs.setVisibility(View.VISIBLE);
                                        chargess.setVisibility(View.VISIBLE);
                                        discounts.setVisibility(View.VISIBLE);
                                        itemtotals.setVisibility(View.VISIBLE);
                                        taxs.setVisibility(View.VISIBLE);
                                        netprices.setVisibility(View.VISIBLE);

                                        demo_dones.setVisibility(View.VISIBLE);
                                        demodonecheck.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (demodonecheck.isChecked()) {
                                                    demo_dates.setVisibility(View.VISIBLE);
                                                    //  Toast.makeText(getContext(),"checked",Toast.LENGTH_LONG).show();
                                                } else {
                                                    demo_dates.setVisibility(View.GONE);
                                                }
                                            }
                                        });


                                        stk.setVisibility(View.VISIBLE);
                                        product_details.setVisibility(View.VISIBLE);
                                        lead_details.setVisibility(View.GONE);
                                        rating.setVisibility(View.GONE);
                                        intrest_types.setVisibility(View.GONE);
                                        spinnerdepartment.setVisibility(View.GONE);
                                    } else {
                                        lead_details.setVisibility(View.GONE);
                                        rating.setVisibility(View.GONE);
                                        intrest_types.setVisibility(View.GONE);
                                        spinnerdepartment.setVisibility(View.GONE);
                                        descrptions.setVisibility(View.GONE);
                                        multi_product_deals.setVisibility(View.GONE);
                                        site.setVisibility(View.GONE);
                                        demo_dates.setVisibility(View.GONE);
                                        stk.setVisibility(View.GONE);
                                        product_details.setVisibility(View.GONE);
                                        productd.setVisibility(View.GONE);
                                        grands.setVisibility(View.GONE);
                                        adjustments.setVisibility(View.GONE);
                                        deducatedtaxs.setVisibility(View.GONE);
                                        taxcharges.setVisibility(View.GONE);
                                        pretaxs.setVisibility(View.GONE);
                                        chargess.setVisibility(View.GONE);
                                        discounts.setVisibility(View.GONE);
                                        itemtotals.setVisibility(View.GONE);
                                        taxs.setVisibility(View.GONE);
                                        netprices.setVisibility(View.GONE);

                                    }


                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });


                    } else if (name.equals("assigned_user_id")) {
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

                        for (final Map.Entry<String, JsonElement> entry : entries) {
                            userids = entry.getKey();
                            usernames = entry.getValue();
                            assignedlist.add(usernames.getAsString().toString());
                            Users users = new Users(userids, usernames);
                            usersListAccount.add(users);
                        }
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, assignedlist) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                        spinnerassignedto.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerassignedto.getAdapter();
                        if (isDuplicate.equals("true")) {
                            int position = adapter.getPosition(edit_assigned);
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnerassignedto.setSelection(position + 1);
                        } else {
                            int position = adapter.getPosition(firstname.concat(" ").concat(lastname));
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnerassignedto.setSelection(position + 1);
                        }

                        spinnerassignedto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position + 1 > 0) {
                                    Users users1 = usersListAccount.get(position);
                                    assigned_id = users1.getUserid();
                                   // Toast.makeText(getContext(), assigned_id, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });


                        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnersalesperson.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter1 = (ArrayAdapter<String>) spinnersalesperson.getAdapter();
                        if (isDuplicate.equals("true")) {
                            int position = adapter.getPosition(edit_employee);
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnersalesperson.setSelection(position + 1);
                        } else {
                            int position = adapter.getPosition(firstname.concat(" ").concat(lastname));
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnersalesperson.setSelection(position + 1);
                        }

                        spinnersalesperson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                if (position + 1 > 0) {

                                    Users users1 = usersListAccount.get(position);
                                    salesperson_id = users1.getUserid();

                                }


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    //lead source

                    else if (name.equals("leadsource")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            lead_sorcelist.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            leadsourceList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, lead_sorcelist) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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

                                    if (position == 0) {
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
                            spinnerleadsource.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerleadsource.getAdapter();
                            int position = adapter.getPosition(edit_leadsource);
                            spinnerleadsource.setSelection(position + 1);
                            spinnerleadsource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = leadsourceList.get(position);
                                        lead_name = String.valueOf(leadsourceList.get(position).getValue());
                                        Log.e("lead_name", lead_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + lead_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //rating
                    else if (name.equals("cf_893")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            rating_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            ratingList.add(pickListValues2);
                        }
                        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                getContext(), R.layout.spinner_item, rating_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                        spinnerrating.setAdapter(spinnerArrayAdapter);
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerrating.getAdapter();
                        if (isDuplicate.equals("true")) {
                            int position = adapter.getPosition(edit_winprob);
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnerrating.setSelection(position + 1);
                        } else {
                            int position = adapter.getPosition(edit_winprob);
                            spinnerArrayAdapter.notifyDataSetChanged();
                            spinnerrating.setSelection(position + 2);
                        }


                        spinnerrating.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                if (position + 1 > 0) {

                                    PickListValues pickListValues3 = ratingList.get(position);
                                    rating_name = pickListValues3.getValue();
                                    Log.e("rating_name", rating_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + rating_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });

                    }
                    //lost reason
                    else if (name.equals("lost_reason")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            lost_reasonlist.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            lostreasonList.add(pickListValues2);

                        }
                    }
                    //interest type
                    else if (name.equals("cf_895")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            interesttype_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            interesttypeList.add(pickListValues2);

                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, interesttype_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnerintrest.setAdapter(spinnerArrayAdapter);

                            spinnerintrest.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = interesttypeList.get(position);
                                        interest_name = pickListValues3.getValue();
                                        Log.e("interest_name", interest_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + interest_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //department
                    else if (name.equals("cf_891")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            department_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            departmentList.add(pickListValues2);

                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, department_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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

                                    if (position == 0) {
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
                            spinnerdepartment.setAdapter(spinnerArrayAdapter);
                            spinnerdepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = departmentList.get(position);
                                        department_name = String.valueOf(departmentList.get(position).getValue());
                                        Log.e("department_name", department_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + department_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //prospect type
                    else if (name.equals("cf_960")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            prospect_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            prospectList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, prospect_list) {
                                // dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnerprospect.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerprospect.getAdapter();
                            int position = adapter.getPosition(edit_prospecttype);
                            spinnerprospect.setSelection(position + 1);
                            spinnerprospect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = prospectList.get(position);
                                        prospect_name = pickListValues3.getValue();
                                        Log.e("prospect_name", prospect_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + prospect_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //sale type
                    else if (name.equals("cf_992")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            saletype_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            saletypeList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, saletype_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnersaletype.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnersaletype.getAdapter();
                            int position = adapter.getPosition(edit_salestype);
                            spinnersaletype.setSelection(position + 1);
                            spinnersaletype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = saletypeList.get(position);
                                        saletype_name = String.valueOf(saletypeList.get(position).getValue());
                                        Log.e("saletype_name", saletype_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + saletype_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //winprob
                    else if (name.equals("cf_897")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            winprob_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            winprobList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, winprob_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnerwinprob.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerrating.getAdapter();
                            if (isDuplicate.equals("true")) {
                                int position = adapter.getPosition(edit_winprob);
                                spinnerArrayAdapter.notifyDataSetChanged();
                                spinnerrating.setSelection(position + 1);
                            } else {
                                int position = adapter.getPosition(edit_winprob);
                                spinnerArrayAdapter.notifyDataSetChanged();
                                spinnerrating.setSelection(position + 2);
                            }
                            spinnerwinprob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = winprobList.get(position);
                                        winprob_name = pickListValues3.getValue();
                                        Log.e("winprob_name", winprob_name);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //support required
                    else if (name.equals("cf_996")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            support_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            supportList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, support_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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
                            spinnersupport.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnersupport.getAdapter();
                            int position = adapter.getPosition(edit_supportrequired);
                            spinnersupport.setSelection(position + 1);
                            spinnersupport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = supportList.get(position);
                                        support_name = String.valueOf(supportList.get(position).getValue());
                                        Log.e("support_name", support_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + support_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //pndt
                    else if (name.equals("cf_998")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            pndt_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            pndtList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, pndt_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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

                                    if (position == 0) {
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
                            spinnerpndt.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerpndt.getAdapter();
                            int position = adapter.getPosition(edit_pndts);
                            spinnerpndt.setSelection(position + 1);
                            spinnerpndt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = pndtList.get(position);
                                        pndt_name = String.valueOf(pndtList.get(position).getValue());
                                        Log.e("pndt_name", pndt_name);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + pndt_name, Toast.LENGTH_SHORT)
//                                                                    .show();


                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        }
                    }
                    //multi product deal
                    else if (name.equals("opportunity_type")) {
                        TypeDetails typeDetails = desFields1.getType();

                        ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();


                        for (PickListValues pickListValues1 : pickListValues) {

                            value = pickListValues1.getValue();
                            multi_list.add(value);
                            PickListValues pickListValues2 = new PickListValues(label, value);
                            multiList.add(pickListValues2);
                            final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                                    getContext(), R.layout.spinner_item, multi_list) {// dataAdapter.setDropDownViewResource(android.R.borderdashboard.simple_spinner_dropdown_item);
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

                                    if (position == 0) {
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
                            spinnermulti.setAdapter(spinnerArrayAdapter);
                            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnermulti.getAdapter();
                            int position = adapter.getPosition(edit_multideal);
                            spinnermulti.setSelection(position + 1);
                            spinnermulti.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                    // String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                                    if (position + 1 > 0) {

                                        PickListValues pickListValues3 = multiList.get(position);
                                        multi_product = String.valueOf(multiList.get(position).getValue());
                                        Log.e("multi_product", multi_product);
//                                                            Toast.makeText
//                                                                    (getContext(), "Selected : " + multi_product, Toast.LENGTH_SHORT)
//                                                                    .show();


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

//    private String convertToString()
//    {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
//        byte[] imgByte = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(imgByte,Base64.DEFAULT);
//    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    requestStoragePermission(true);
                } else if (items[item].equals("Choose from Gallery")) {
                    requestStoragePermission(false);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Capture image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = GenericFileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);

                mPhotoFile = photoFile;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);

            }
        }
    }


    /**
     * Select image fro gallery
     */
    private void dispatchGalleryIntent() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickPhoto.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(pickPhoto, REQUEST_GALLERY_PHOTO);

    }


//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK){
            no_file_pan.setText(mPhotoFile.getAbsolutePath().substring(mPhotoFile.getAbsolutePath().lastIndexOf("/")+1));
                //  Glide.with(getContext()).load(mPhotoFile).apply(new RequestOptions().centerCrop().circleCrop().placeholder(R.drawable.profile_pic_place_holder)).into(profile);

            } else if (requestCode==REQUEST_GALLERY_PHOTO && resultCode == Activity.RESULT_OK) {



            try {
                selectedImage = data.getData();
                InputStream is = getContext().getContentResolver().openInputStream(selectedImage);
                uploadImage(getBytes(is),selectedImage);


            } catch (IOException e) {
                e.printStackTrace();
            }
            // no_file_pan.setText(selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf("/")+1));

            }
        }



    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }
    private void uploadImage(byte[] imageBytes,Uri selectedImage) {
        file = new File(getRealPathFromURI(selectedImage));
        filename=file.getName();
        no_file_pan.setText(filename);

        requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
       // MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
    }



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    //
//    /**
//     * Requesting multiple permissions (storage and camera) at once
//     * This uses multiple permission model from dexter
//     * On permanent denial opens settings dialog
//     */
    private void requestStoragePermission(final boolean isCamera) {
        Dexter.withActivity((Activity) getContext()).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (isCamera) {
                                dispatchTakePictureIntent();
                            } else {
                                dispatchGalleryIntent();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            @Override
            public void onError(DexterError error) {
                Toast.makeText(getActivity().getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                .onSameThread()
                .check();
    }


    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**
     * Create file with current timestamp name
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File mFile = File.createTempFile(mFileName, ".jpg", storageDir);
        return mFile;
    }

    /**
     * Get real file path from URI
     *
     * @param contentUri
     * @return
     */
    public String getRealPathFromUri(Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
            assert cursor != null;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    private void saveclosedlost() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
               // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "saveRecord";
                String module = "Potentials";
                String lost_reason = lostreason_name;
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("lost_reason", lost_reason);
                    postdata.put("competition_name", competition_names);
                    postdata.put("price_quoted", competition_prices);
                    postdata.put("general_remark", remarks);
                    postdata.put("cf_1000", expected_delivery_date.getText().toString());
                    postdata.put("cf_960", prospect_name);
                    postdata.put("cf_992", saletype_name);
                    postdata.put("cf_998", pndt_name);
                    postdata.put("closingdate", expected_po_date.getText().toString());
                    postdata.put("contact_id", contact_id);
                    postdata.put("demo_date", expected_delivery_date.getText().toString());
                    postdata.put("leadsource", lead_name);
                    postdata.put("location", locationids);
                    postdata.put("potentialname", opportunity_name.getText().toString());
                    postdata.put("productid", product_id);
                    postdata.put("related_to", account_id);
                    postdata.put("sales_stage", sales_name_value);
                    postdata.put("assigned_user_id", ids);


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
                                fragment = new SalesStageFragment();
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
                        progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;

    }

    private void saveclosedwon() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Loading...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            //    sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String path = getPath(selectedImage);
                String uploadId = UUID.randomUUID().toString();
                //Creating a multi part request
                try {
                    new MultipartUploadRequest(getContext(), uploadId, Constants.UPLOAD_URL)
                            .addFileToUpload(path, "file") //Adding file
                            .addParameter("name", filename) //Adding text parameter to the request
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload(); //Starting the upload
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                String operation = "saveRecord";
                if(isDuplicate.equals("true")) {
                    sourceRecord = opprtunity_id;
                }
                String module = "Documents";
                String sourceModule = "Potentials";
              //  RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(filePath)), mPhotoFile);

                JSONObject postdata = new JSONObject();
                MultipartBody.Part body = MultipartBody.Part.createFormData("file", filename, requestFile);
                try {
                    postdata.put("notes_title", notes_title);
                    postdata.put("filename", body);
                    postdata.put("assigned_user_id", ids);
                    postdata.put("filelocationtype", "I");
                    postdata.put("filestatus", "1");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //String values= postdata.getString(firstname,lastname,jobtile,contacttype,speclization,mobiles,email);
                final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
                RequestBody operation_won = RequestBody.create(MediaType.parse("text/plain"), operation);
                RequestBody session = RequestBody.create(MediaType.parse("text/plain"), sessionId);
                RequestBody module_won = RequestBody.create(MediaType.parse("text/plain"), module);
                RequestBody sourceRecord_won = RequestBody.create(MediaType.parse("text/plain"), sourceRecord);
                RequestBody sourceModule_won = RequestBody.create(MediaType.parse("text/plain"), sourceModule);
              //  RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(selectedImage)), file);
               // MultipartBody.Part sourceModule_postdata = MultipartBody.Part.createFormData("values",postdata.toString());
              final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
              RequestBody sourceModule_postdata = RequestBody.create(JSON, String.valueOf(postdata));
              //  MultipartBody.Part file = MultipartBody.Part.createFormData("file", "file");


                /** Call the method with parameter in the interface to get the notice data*/
                Call<SaveContactModule> call = service.SaveDocuments(operation_won, session, module_won, sourceRecord_won, sourceModule_postdata, sourceModule_won,body);
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
                                fragment = new SalesStageFragment();
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
                        progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;

    }

    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void fetchProductnameJSON() {
        autoproduct_name.setHint(Html.fromHtml("Item Name" + ""));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

               // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "syncModuleRecords";
                String module = "Products";
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
                            String jsonAllProducts = g.toJson(syncModule);
                            tinydb.putString("jsonAllProducts", jsonAllProducts);
                            workingOnResponseProduct(syncModule);
                        }
                    }

                    @Override
                    public void onFailure(Call<SyncModule> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return;
    }

    private void workingOnResponseProduct(SyncModule syncModule) {
        autoproduct_name.setHint(Html.fromHtml("Item Name" + ""));
        String success = syncModule.getSuccess();

        if (success.equals("true")) {

            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {


                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
                for (SyncBlocks syncBlocks1 : syncBlocks) {
                    String label = syncBlocks1.getLabel();
                    //Basic Information
                    if (label.equals("Product Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("productname")) {
                                productname = String.valueOf(values);
                                product_name.add(productname);


                            }
                        }
                    }

                }

            }
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, product_name);
        autoproduct_name.setAdapter(adapter);
        autoproduct_name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productnames = adapter.getItem(position);
                boolean isSuccess = false;
                for (SynFields synFields2 : productList) {
                    Log.d("productList", String.valueOf(productList.get(position).getLabel()));
                    if (synFields2.getLabel().equalsIgnoreCase(productnames)) {
                        isSuccess = true;
                        product_id = String.valueOf(synFields2.getValue());
                        Log.d("product_id",product_id);
                        Toast.makeText(getContext(),product_id,Toast.LENGTH_LONG).show();
                        String grandtotal = synFields2.getHdnGrandTotal();
                        String pretax_total = synFields2.getPre_tax_total();
                        String qty = synFields2.getQuantity();
                        String list_price = synFields2.getListprice();
                        grand_total.setText(grandtotal);
                        pretax.setText(pretax_total);
                        listprice.setText(list_price);
                        String totals = String.valueOf(Integer.parseInt(qty) * Integer.parseInt(list_price));
                        total.setText(totals);
                        quantitys.setText(qty);

                        break;
                    } else {
                        grand_total.setText("0");
                        pretax.setText("0");
                        listprice.setText("0");
                    }
                }
            }
        });
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, Typeface.NORMAL, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE, Typeface.NORMAL, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, Typeface.NORMAL, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, Typeface.NORMAL, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0, getLayoutParams());
        //String[] namesList = p.split(",");
        //String[] priceList = listprices.split(",");
    }


    private void fetchProductPotentialJSON() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

              //  sessionId = getActivity().getIntent().getStringExtra("sessionId");
                String operation = "syncModuleRecords";
                String module = "Potentials";
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

                            SyncModule syncModule1 = response.body();
                            Gson g = new Gson();
                            String jsonAllProductsPotentials = g.toJson(syncModule1);
                            tinydb.putString("jsonAllProductsPotentials", jsonAllProductsPotentials);
                            workingOnResponseProductPotential(syncModule1);
                        }
                    }

                    @Override
                    public void onFailure(Call<SyncModule> call, Throwable t) {

                    }
                    //     progressDialog.dismiss();
                });
            }
        }, 0);
        return;
    }

    private void workingOnResponseProductPotential(SyncModule syncModule) {
        autoproduct_name.setHint(Html.fromHtml("Item Name" + ""));
        String success = syncModule.getSuccess();


        if (success.equals("true")) {

            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {


                  opprtunity_id = syncUpdated.getId();
                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
                for (SyncBlocks syncBlocks1 : syncBlocks) {
                    String label = syncBlocks1.getLabel();
                    //Basic Information
                    if (label.equals("Products")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("productid")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    String id = jsonObject.getString("value");
                                    productValue = v;
                                    productID = id;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }
                            } else if (name.equals("hdnGrandTotal")) {
                                grandtotal = String.valueOf(values);
                            } else if (name.equals("pre_tax_total")) {
                                pre_tax_total = String.valueOf(values);
                            } else if (name.equals("quantity")) {
                                quantity = String.valueOf(values);
                                if (quantity.contains(".")) {
                                    quantity_product = quantity.substring(0, quantity.indexOf(".")).concat(")").replace(")", "");//error
                                } else {
                                    quantity_product = quantity; // DOES NOT CONTAIN "." PROCEED ACCORDINGLY
                                }
                            } else if (name.equals("listprice")) {
                                unitprice = String.valueOf(values);
                                if (unitprice.contains(".")) {
                                    unitprice_product = unitprice.substring(0, unitprice.indexOf(".")).concat(")").replace(")", "");//error
                                } else {
                                    unitprice_product = unitprice;
                                }

                            }
                            SynFields synFields2 = new SynFields(name, productID, productValue, grandtotal, pre_tax_total, quantity_product, unitprice_product);
                            productList.add(synFields2);

                        }
                    }

                }

            }
        }

    }

    private void fetchLocationJSON() {

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
                spinnerlocation.setHint(Html.fromHtml("Location" + " " + "<font color=\"#ff0000\">" + "* " + "</font>"));
            //    sessionId = getActivity().getIntent().getStringExtra("sessionId");
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
                            String jsonLocationopp = g.toJson(locationModule);
                            tinydb.putString("jsonLocationopp", jsonLocationopp);
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
        return;
    }

    @NonNull
    private TableRow.LayoutParams getLayoutParams() {
        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    private TextView getTextView(int id, String title, int color, int typeface, int bgColor) {
        TextView tv = new TextView(getContext());
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 10, 10, 10);
        tv.setTypeface(Typeface.DEFAULT, typeface);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
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
                LocationRecords locationRecords = new LocationRecords(locationno, district, circle, id);
                locationList.add(locationRecords);
                location_list.add(district.concat(" ").concat(",").concat(circle));
            }
            // HintAdapter adapter = new HintAdapter(getActivity(), account_manger, android.R.borderdashboard.simple_spinner_item);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, location_list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerlocation.setAdapter(dataAdapter);

            ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinnerlocation.getAdapter();
            int position = adapter.getPosition(edit_location);
            spinnerlocation.setSelection(position + 1);
            spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (position + 1 > 0) {
                        //    ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                        //  ((TextView) parent.getChildAt(0)).setText("Assigned To");
                        LocationRecords locationRecords = locationList.get(position);

                        locationidss = locationRecords.getId();
                        String[] moduleid = locationidss.split("x");
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


    //saving a record
    private void saverecords() {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic

                if (spinnersalesstage.getSelectedItem() == null) {
                    spinnersalesstage.setError("Please Select an option");
                } else if (opportunity_name.getText().toString().isEmpty()) {
                    opportunity_name.setError("Please Enter Opportunity Name");
                } else if (spinnerassignedto.getSelectedItem() == null) {
                    spinnerassignedto.setError("Please Select an option");
                } else if (autoconatct_name.getText().toString().isEmpty()) {
                    autoconatct_name.setError("Type to search contact name");
                } else if (autoaccount_name.getText().toString().isEmpty()) {
                    autoaccount_name.setError("Type to search Account name");
                } else if (spinnerlocation.getSelectedItem() == null) {
                    spinnerlocation.setError("Please Select an option");
                } else if (spinnerleadsource.getSelectedItem() == null) {
                    spinnerleadsource.setError("Please Select an option");
                } else if (spinnerprospect.getSelectedItem() == null) {
                    spinnerprospect.setError("Please Select an option");
                } else if (spinnersaletype.getSelectedItem() == null) {
                    spinnersaletype.setError("Please Select an option");
                } else if (spinnerpndt.getSelectedItem() == null) {
                    spinnerpndt.setError("Please Select an option");
                } else {

                    progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Loading...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                    progressDialog.show();

                   // sessionId = getActivity().getIntent().getStringExtra("sessionId");
                    String operation = "saveRecord";
                    String module = "Potentials";

                    String potentialname = opportunity_name.getText().toString();
                    String accountmanger = assigned_id;
                    String contact_ids = contact_id;
                    String account_ids = account_id;
                    String products_ids=product_id;
                    String record = "";
                    JSONObject postdata = new JSONObject();

                    try {
                        postdata.put("sales_stage", sales_name_value);
                        Log.d("sales_stage", sales_name_value);
                        postdata.put("potentialname", potentialname);
                        Log.d("potentialname", potentialname);
                        postdata.put("assigned_user_id", accountmanger);
                        Log.d("accountmanger", accountmanger);
                        postdata.put("contact_id", contact_ids);
                        Log.d("contact_ids", contact_ids);
                        postdata.put("related_to", account_ids);
                        Log.d("account_ids", account_ids);
                        postdata.put("productid", products_ids);
                        Log.d("products_ids", products_ids);
                        postdata.put("cf_998", pndt_name);
                        postdata.put("location", locationidss);
                        Log.d("locationids", locationidss);
                        postdata.put("leadsource", lead_name);
                        Log.d("lead_name", lead_name);
                        postdata.put("termsnconditions", termscondition);
                        Log.d("termsnconditions", termscondition);
                        postdata.put("employee", accountmanger);
                        Log.d("employee", accountmanger);
                        postdata.put("cf_895", interest_name);

                            postdata.put("cf_960", prospect_name);
                            Log.d("cf_960", prospect_name);


                            postdata.put("cf_992", saletype_name);
                            Log.d("cf_992", saletype_name);
                        postdata.put("closingdate", expected_po_date.getText().toString());
                        Log.d("closingdate", expected_po_date.getText().toString());
                        postdata.put("cf_1000", expected_delivery_date.getText().toString());
                        Log.d("cf_1000", expected_delivery_date.getText().toString());
                        postdata.put("demo_date", expected_delivery_date.getText().toString());
                        Log.d("demo_date", expected_delivery_date.getText().toString());

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
                                    fragment = new SalesStageFragment();
                                    loadFragment(fragment);

                                } else if (success.equals("false")) {

                                    String message = "Duplicates detected";
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
        return;
    }

    //update records
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
                String module = "Potentials";

                String potentialname = opportunity_name.getText().toString();
                String accountmanger = assigned_id;
                String contact_ids = contact_id;
                String account_ids = account_id;
                String product_ids=product_id;
                String record = opprtunity_id;
                JSONObject postdata = new JSONObject();

                try {
                    postdata.put("sales_stage", sales_name_value);
                    Log.d("sales_stage", sales_name_value);
                    postdata.put("potentialname", potentialname);
                    Log.d("potentialname", potentialname);
                    postdata.put("assigned_user_id", accountmanger);
                    Log.d("accountmanger", accountmanger);
                    postdata.put("contact_id", contact_ids);
                    Log.d("contact_ids", contact_ids);
                    postdata.put("related_to", account_ids);
                    Log.d("account_ids", account_ids);
                    postdata.put("productid", product_ids);
                    postdata.put("cf_998", pndt_name);
                    //  Log.d("product_id", product_id);
                    postdata.put("location", locationids);
                    Log.d("locationids", locationids);
                    postdata.put("leadsource", lead_name);
                    Log.d("lead_name", lead_name);
                    postdata.put("cf_895", interest_name);
                    if (prospect_name != null) {
                        postdata.put("cf_960", prospect_name);
                        Log.d("cf_960", prospect_name);
                    }
                    if (saletype_name != null) {
                        postdata.put("cf_992", saletype_name);
                        Log.d("cf_992", saletype_name);
                    }
                    postdata.put("closingdate", expected_po_date.getText().toString());
                    Log.d("closingdate", expected_po_date.getText().toString());
                    postdata.put("cf_1000", expected_delivery_date.getText().toString());
                    Log.d("cf_1000", expected_delivery_date.getText().toString());
                    postdata.put("cf_1000", expected_delivery_date.getText().toString());
                    Log.d("cf_1000", expected_delivery_date.getText().toString());
                    postdata.put("demo_date", expected_delivery_date.getText().toString());
                    Log.d("demo_date", expected_delivery_date.getText().toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //String values= postdata.getString(firstname,lastname,jobtile,contacttype,speclization,mobiles,email);
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

                            SaveContactModule saveContactModule = response.body();


                            String success = saveContactModule.getSuccess();


                            if (success.equals("true")) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Updated Successfully", Toast.LENGTH_LONG).show();
                                fragment = new SalesStageFragment();
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
                        progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;
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
                fetchLocationJSON();
                fetchProductnameJSON();
                fetchProductPotentialJSON();
                fetchAccountJSON();
                fetchContactJSON();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==1000 && resultCode == Activity.RESULT_OK){
//            no_file_pan.setText("image");
//
//        }
//    }
}

//    private void uploadFile(Uri fileUri, String desc) {
//
//        //creating a file
//        File file = new File(getRealPathFromURI(fileUri));
//
//        //creating request body for file
//        RequestBody requestFile = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(fileUri)), file);
//        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);
//
//        //The gson builder
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//
//
//        //creating retrofit object
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(RetrofitInstance.getRetrofitInstance().baseUrl())
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        //creating our api
//        Api api = retrofit.create(Api.class);
//
//        //creating a call and calling the upload image method
//        Call<MyResponse> call = api.uploadImage(requestFile, descBody);
//
//        //finally performing the call
//        call.enqueue(new Callback<MyResponse>() {
//            @Override
//            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                if (!response.body().error) {
//                    Toast.makeText(getContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MyResponse> call, Throwable t) {
//                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    /*
     * This method is fetching the absolute path of the image file
     * if you want to upload other kind of files like .pdf, .docx
     * you need to make changes on this method only
     * Rest part will be the same
     * */
//    private String getRealPathFromURI(Uri contentUri) {
//        String[] proj = {MediaStore.Images.Media.DATA};
//        android.content.CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
//        Cursor cursor = loader.loadInBackground();
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        String result = cursor.getString(column_index);
//        cursor.close();
//        return result;
//    }




