package com.genworks.oppm.views;



import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.widget.FrameLayout;
import android.widget.TableRow.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
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
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.genworks.oppm.Adapter.ClosedLostAdapter;
import com.genworks.oppm.Adapter.ClosedWonAdapter;
import com.genworks.oppm.Adapter.DemoRequiredAdapter;
import com.genworks.oppm.Adapter.FundingrequiredAdapter;
import com.genworks.oppm.Adapter.NegotiationAdapter;
import com.genworks.oppm.Adapter.NosupportAdapter;
import com.genworks.oppm.Adapter.OpportunityAdapter;
import com.genworks.oppm.Adapter.PricesupportAdapter;
import com.genworks.oppm.Adapter.ProductAdapter;
import com.genworks.oppm.Adapter.ProposalAdapter;
import com.genworks.oppm.Adapter.TravelsuppotyAdapter;
import com.genworks.oppm.BuildConfig;
import com.genworks.oppm.model.AdharModel;
import com.genworks.oppm.model.ChecqueModel;
import com.genworks.oppm.model.ClosedwonResults;
import com.genworks.oppm.model.DocModel;
import com.genworks.oppm.model.Doc_oneModel;
import com.genworks.oppm.model.Doc_twoModel;
import com.genworks.oppm.model.DocumentModel;
import com.genworks.oppm.model.GSTModel;
import com.genworks.oppm.model.ModelTest;
import com.genworks.oppm.model.PanModel;
import com.genworks.oppm.model.Purchase_orderModel;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.PreferenceManagerSalesSupport;
import com.genworks.oppm.Utils.TinyDB;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.Normalizer;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.View.VISIBLE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SalesStageTabFragement extends Fragment implements OpportunityAdapter.MyItemClickListener, OpportunityAdapter.MyEditItemClickListner, OpportunityAdapter.MyPDfItemClickListner, OpportunityAdapter.Listener, ProposalAdapter.MyEditProposalItemClickListner, ProposalAdapter.MyProposalItemClickListener, NosupportAdapter.MySupportEditItemClickListner, NosupportAdapter.MySupportItemClickListener
        , FundingrequiredAdapter.MyFundingEditItemClickListner, FundingrequiredAdapter.MyFundingItemClickListener, TravelsuppotyAdapter.MyTravelEditItemClickListner, TravelsuppotyAdapter.MyTravelItemClickListener, DemoRequiredAdapter.MyDemoEditItemClickListner, DemoRequiredAdapter.MyDemoItemClickListener, PricesupportAdapter.MyPriceItemClickListener, PricesupportAdapter.MyPriceEditItemClickListner
        , ClosedWonAdapter.MyWonEditItemClickListner, ClosedWonAdapter.MyWonItemClickListener, ClosedLostAdapter.MyLostEditItemClickListner, ClosedLostAdapter.MyLostItemClickListener
        , NegotiationAdapter.MyNegotiationEditItemClickListner, NegotiationAdapter.MyNegotiationItemClickListener, ProductAdapter.MyProductEditItemClickListner, ProductAdapter.MyProductItemClickListener {

    private OpportunityAdapter opportunityAdapter;
    private ProposalAdapter proposalAdapter;
    private LinearLayout sales_supportlist, support_requiredlist, all, incomplete;
    private NegotiationAdapter negotiationAdapter;
    private ClosedWonAdapter closedwonAdapter;
    private ClosedLostAdapter closedLostAdapter;
    private NosupportAdapter nosupportAdapter;
    private String product_list, name, opportunity_id;
    private String item_list;
    private DocModel docModel;
    private PopupWindow popupWindow;
    private androidx.appcompat.widget.Toolbar searchtollbar;
    private View mFloatingView;
    private WindowManager mWindowManager;
    private LinearLayout llPdf;
    Menu search_menu;
    String documentIDS,documentFILENAMES;
    private TinyDB tinyDB;
    MenuItem item_search;
    private PricesupportAdapter pricesupportAdapter;
    private DemoRequiredAdapter demoRequiredAdapter;
    private Button closebtn;
    private Bitmap bitmap;
    private boolean isDuplicate = false;
    private TextView s_i, text_opp, listprice, sale_type, demo_date, sales_stage, opp_name, assigned_to, contact_name, opp_no, account_name, second_sales_person, location, lead_source, remark_sales_person, segment, modality, circle, prospect_type, equip_detail, sales_type, exp_date, exp_del_date, pndt, demo_done, funding_required, site_read, support_person, support_type, multi_product_deal, department, rating, intrest_type, total, adjustment, charge, pre_tax_total, sub_total, item_name, quantity, selling_price, item_comment, item_dis_amount, vat, sale, service, lost_reason, competation_name, competation_price, lost_mark, descrption, createdtime, modifiedtime, modifiedby, terms_condition, win_prob;

    private FundingrequiredAdapter fundingrequiredAdapter;
    private TravelsuppotyAdapter travelsuppotyAdapter;
    private ProductAdapter productAdapter;
    RecyclerView recyclerViewOpportunity, recyclerPriceValid, recyclerProposal, recyclerNegotiation, recyclerClosedWon, recyclerClosedLost, recyclerView5;
    private RecyclerView recyclerViewNoSupport, recyclerViewpricesupport, recyclerDemoRequired, recyclerFundingReq, recyclerTravelSupport;
    private TableLayout documents;
    Fragment fragment;
    private LinearLayout pdf_header;
    private ProgressDialog progressDialog = null;
    String sessionId, sales_value;
    private int mPage;
    private boolean msalesstageSelected;
    private final Handler handler = new Handler();
    private Object values, field_value;
    ViewPager viewPager;
    private static final String TAG_VALUE = "value";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String mobie;
    LinearLayout linearLayout;
    Gson gson;
    private String opportunitywon_id;
    TextView textEmptyList;
    TextView sales_support, support_required;
    private HorizontalScrollView hscroll;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private String ismenutoggle;
    private TabLayout tabLayout;
    private FrameLayout frameLayout;
    public static final String ARG_PAGE = "ARG_PAGE";
    private ArrayList<ModelTest> listSalesStageNegotiation;
    private ArrayList<ModelTest> listSalesStageOpportunity;
    private ArrayList<ModelTest> listSalesStageProposal;
    private ArrayList<ModelTest> listSalesStageClosedWon;
    private ArrayList<ModelTest> listSalesStageClosedLost;
    private ArrayList<DocModel> closedwonList=new ArrayList<>();

    private ArrayList<SynFields> productList = new ArrayList<>();

    private String adharId="",adharFilename="",adharPath="",checqueId="",checqueFilename="",checquepath="",purchaseId="",purchaseFilename="",purchasePath="",gstId="",gstFilename="",gstpath="",id="",filename="",path="",doc_oneid="",doc_onefilename="",doc_onepath="",doc_twoid="",doc_twofilename="",doc_twopath="";
    private ArrayList<ModelTest> listSupportStageProduct;
    private ArrayList<ModelTest> listSupportStagePrice;
    private ArrayList<ModelTest> listSupportStageNoSupport;
    private ArrayList<ModelTest> listSupportStageDemo;
    private ArrayList<ModelTest> listSupportStageCFundingrequired;
    private ArrayList<ModelTest> listSupportStageTravelSupport;
    private static final String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment
        //loadFragment(new SalesStageFragment());
        //  ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Opportunities List");
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.loader);
        progressDialog.show();
        View rootView = inflater.inflate(R.layout.account, container, false);
        progressDialog.dismiss();
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 112);
        setSearchtollbar();
        setHasOptionsMenu(true);

        FloatingActionButton fb = rootView.findViewById(R.id.fab);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDuplicate = false;
                Bundle args = new Bundle();
                args.putString("isDuplicate", String.valueOf(isDuplicate));
                // ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Create New Opportunity");
                fragment = new OpportunityCreateFragment();
                sessionId = getActivity().getIntent().getStringExtra("sessionId");
                fragment.setArguments(args);
                loadFragment(fragment);
            }
        });
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        // pdf_header=rootView.findViewById(R.id.header_pdf);

      //  hscroll = rootView.findViewById(R.id.hscroll);
        sales_support = (TextView) rootView.findViewById(R.id.sales_stage);
        support_required = (TextView) rootView.findViewById(R.id.support_required);
        sales_supportlist = (LinearLayout) rootView.findViewById(R.id.sales_stagelist);
       // support_requiredlist = (LinearLayout) rootView.findViewById(R.id.suppport_requirdlist);
      //  textEmptyList = rootView.findViewById(R.id.textEmptyList);
        recyclerViewOpportunity = rootView.findViewById(R.id.recyclerOpportunity);
        recyclerProposal = rootView.findViewById(R.id.recyclerProposal);
        recyclerNegotiation = rootView.findViewById(R.id.recyclerNegotiation);
        recyclerClosedWon = rootView.findViewById(R.id.recyclerClosedWon);
//        recyclerClosedLost.setBackgroundColor(R.color.trans);
        recyclerClosedLost = rootView.findViewById(R.id.recyclerClosedLost);


        listSalesStageOpportunity = new ArrayList<>();
        opportunityAdapter = new OpportunityAdapter(requireContext(), listSalesStageOpportunity, this, this, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerViewOpportunity.setLayoutManager(linearLayoutManager);
        recyclerViewOpportunity.setAdapter(opportunityAdapter);

        listSalesStageProposal = new ArrayList<>();
        closedwonList=new ArrayList<>();
        proposalAdapter = new ProposalAdapter(requireContext(), listSalesStageProposal, this, this);
        LinearLayoutManager linearLayoutManager5 = new LinearLayoutManager(getContext());
        linearLayoutManager5.setReverseLayout(true);
        linearLayoutManager5.setStackFromEnd(true);
        recyclerProposal.setLayoutManager(linearLayoutManager5);
        recyclerProposal.setAdapter(proposalAdapter);

        listSalesStageNegotiation = new ArrayList<>();
        negotiationAdapter = new NegotiationAdapter(requireContext(), listSalesStageNegotiation, this, this);
        LinearLayoutManager linearLayoutManager3 = new LinearLayoutManager(getContext());
        linearLayoutManager3.setReverseLayout(true);
        linearLayoutManager3.setStackFromEnd(true);
        recyclerNegotiation.setLayoutManager(linearLayoutManager3);
        recyclerNegotiation.setAdapter(negotiationAdapter);

        listSalesStageClosedWon = new ArrayList<>();
        closedwonAdapter = new ClosedWonAdapter(requireContext(), listSalesStageClosedWon, this, this);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        linearLayoutManager2.setReverseLayout(true);
        linearLayoutManager2.setStackFromEnd(true);
        recyclerClosedWon.setLayoutManager(linearLayoutManager2);
        recyclerClosedWon.setAdapter(closedwonAdapter);


        listSalesStageClosedLost = new ArrayList<>();
        closedLostAdapter = new ClosedLostAdapter(requireContext(), listSalesStageClosedLost, this, this);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        linearLayoutManager1.setReverseLayout(true);
        linearLayoutManager1.setStackFromEnd(true);
        recyclerClosedLost.setLayoutManager(linearLayoutManager1);
        recyclerClosedLost.setAdapter(closedLostAdapter);


        recyclerViewNoSupport = rootView.findViewById(R.id.recyclerNoSupport);
        recyclerDemoRequired = rootView.findViewById(R.id.recyclerDemoReq);
        recyclerViewpricesupport = rootView.findViewById(R.id.recyclerPriceSupport);
        recyclerTravelSupport = rootView.findViewById(R.id.recyclerTravelSupport);
        recyclerFundingReq = rootView.findViewById(R.id.recyclerFundingReq);
        recyclerPriceValid = rootView.findViewById(R.id.recyclerPriceValid);

        listSupportStageNoSupport = new ArrayList<>();
        nosupportAdapter = new NosupportAdapter(requireContext(), listSupportStageNoSupport, this, this);
        final LinearLayoutManager linearLayoutManagerNosupport = new LinearLayoutManager(getContext());
        linearLayoutManagerNosupport.setReverseLayout(true);
        linearLayoutManagerNosupport.setStackFromEnd(true);
        recyclerViewNoSupport.setLayoutManager(linearLayoutManagerNosupport);
        recyclerViewNoSupport.setAdapter(nosupportAdapter);

        listSupportStageProduct = new ArrayList<>();
        productAdapter = new ProductAdapter(requireContext(), listSupportStageProduct, this, this);
        LinearLayoutManager linearLayoutManagerProduct = new LinearLayoutManager(getContext());
        linearLayoutManagerProduct.setReverseLayout(true);
        linearLayoutManagerProduct.setStackFromEnd(true);
        recyclerPriceValid.setLayoutManager(linearLayoutManagerProduct);
        recyclerPriceValid.setAdapter(productAdapter);

        listSupportStagePrice = new ArrayList<>();
        pricesupportAdapter = new PricesupportAdapter(requireContext(), listSupportStagePrice, this, this);
        LinearLayoutManager linearLayoutManagerprice = new LinearLayoutManager(getContext());
        linearLayoutManagerprice.setReverseLayout(true);
        linearLayoutManagerprice.setStackFromEnd(true);
        recyclerViewpricesupport.setLayoutManager(linearLayoutManagerprice);
        recyclerViewpricesupport.setAdapter(pricesupportAdapter);
//
        listSupportStageTravelSupport = new ArrayList<>();
        travelsuppotyAdapter = new TravelsuppotyAdapter(requireContext(), listSupportStageTravelSupport, this, this);
        LinearLayoutManager linearLayoutManagerTravel = new LinearLayoutManager(getContext());
        linearLayoutManagerTravel.setReverseLayout(true);
        linearLayoutManagerTravel.setStackFromEnd(true);
        recyclerTravelSupport.setLayoutManager(linearLayoutManagerTravel);
        recyclerTravelSupport.setAdapter(travelsuppotyAdapter);
//
        listSupportStageDemo = new ArrayList<>();
        demoRequiredAdapter = new DemoRequiredAdapter(requireContext(), listSupportStageDemo, this, this);
        LinearLayoutManager linearLayoutManagerdemo = new LinearLayoutManager(getContext());
        linearLayoutManagerdemo.setReverseLayout(true);
        linearLayoutManagerdemo.setStackFromEnd(true);
        recyclerDemoRequired.setLayoutManager(linearLayoutManagerdemo);
        recyclerDemoRequired.setAdapter(demoRequiredAdapter);

        listSupportStageCFundingrequired = new ArrayList<>();
        fundingrequiredAdapter = new FundingrequiredAdapter(requireContext(), listSupportStageCFundingrequired, this, this);
        LinearLayoutManager linearLayoutManagerfunding = new LinearLayoutManager(getContext());
        linearLayoutManagerfunding.setReverseLayout(true);
        linearLayoutManagerfunding.setStackFromEnd(true);
        recyclerFundingReq.setLayoutManager(linearLayoutManagerfunding);
        recyclerFundingReq.setAdapter(fundingrequiredAdapter);



        String apiResponseStr = PreferenceManagerSalesSupport.getInstance(requireContext()).getAPIResponseSales(); // so you have to press your refresh buttonok

        if (!apiResponseStr.equals("")) {
            Gson gson = new Gson();
            SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
            workingOnResponse1(obj);
        }  else {
            listSalesStageClosedWon.clear();
            listSalesStageNegotiation.clear();
            listSalesStageOpportunity.clear();
            listSalesStageProposal.clear();
            listSalesStageClosedLost.clear();
            fetchJSONForSales();

        }
        // fetchJSONForSales();

        sales_support.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                String apiResponseStr = PreferenceManagerSalesSupport.getInstance(requireContext()).getAPIResponseSales(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {
                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    listSalesStageOpportunity.clear();
                    listSalesStageProposal.clear();
                    listSalesStageNegotiation.clear();
                    listSalesStageClosedLost.clear();
                    listSalesStageClosedWon.clear();
                    workingOnResponse1(obj);
                } else {
                    listSalesStageOpportunity.clear();
                    listSalesStageProposal.clear();
                    listSalesStageNegotiation.clear();
                    listSalesStageClosedLost.clear();
                    listSalesStageClosedWon.clear();
                    fetchJSONForSales();
                }

                support_required.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.White));
                sales_support.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.linecolor));
                sales_support.setTextColor(ContextCompat.getColor(getContext(), R.color.White));
                support_required.setTextColor(ContextCompat.getColor(getContext(), R.color.linecolor));


                sales_supportlist.setVisibility(VISIBLE);
                support_requiredlist.setVisibility(View.GONE);
                return true;
            }
        });

        support_required.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String apiResponseStr = PreferenceManagerSalesSupport.getInstance(requireContext()).getAPIResponseSales(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {
                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);

                    listSupportStageCFundingrequired.clear();
                    listSupportStageNoSupport.clear();
                    listSupportStagePrice.clear();
                    listSupportStageProduct.clear();
                    listSupportStageDemo.clear();
                    listSupportStageTravelSupport.clear();
                    workingOnResponse(obj);
                } else {
                    listSupportStageCFundingrequired.clear();
                    listSupportStageNoSupport.clear();
                    listSupportStagePrice.clear();
                    listSupportStageProduct.clear();
                    listSupportStageDemo.clear();
                    listSupportStageTravelSupport.clear();
                    fetchJSONForSupport();
                }

                support_required.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.linecolor));
                sales_support.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.White));
                sales_support.setTextColor(ContextCompat.getColor(getContext(), R.color.linecolor));
                support_required.setTextColor(ContextCompat.getColor(getContext(), R.color.White));
                support_requiredlist.setVisibility(VISIBLE);
                sales_supportlist.setVisibility(View.GONE);

                return true;
            }
        });


        return rootView;
    }

    private void fetchJSONForSales() {

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
                sessionId = getActivity().getIntent().getStringExtra("sessionId");
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

                            SyncModule syncModule = response.body();


                            Gson g = new Gson();
                            String json = g.toJson(syncModule);
                            PreferenceManagerSalesSupport.getInstance(requireContext()).setAPIResponseSales(json);

                            workingOnResponse1(syncModule);

                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SyncModule> call, Throwable t) {
                        Log.d("error", t.getMessage());
                        // progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;

    }

//    private void filter() {
//
//        for(String s:potentialNo){
//
//        }
//    }


    private void fetchJSONForSupport() {

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
                sessionId = getActivity().getIntent().getStringExtra("sessionId");
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

                            SyncModule syncModule = response.body();

                            Gson g = new Gson();
                            String json = g.toJson(syncModule);
                            PreferenceManagerSalesSupport.getInstance(requireContext()).setAPIResponseSales(json);

                            workingOnResponse(syncModule);


                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<SyncModule> call, Throwable t) {
                        Log.d("error", t.getMessage());
                        // progressDialog.dismiss();
                    }


                });

            }


        }, 0);
        return;

    }


    private void workingOnResponse(SyncModule syncModule) {

        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
                String title = "";
                String value = "";
                String potentialValue = "";
                String potentialNo = "";
                String assigned = "";
                String contact = "";
                String location = "";
                String related = "";
                String winprobValue = "";
                String valueAndQty = "";
                String productValue = "";
                String quantity = "";
                String salesstageValue = "";
                String employee = "";
                String leadsource = "";
                String remark_sales_person = "";
                String segment = "";
                String modality = "";
                String circle = "";
                String productID = "";
                String quantitys = "";
                String listprices = "";
                String pdf_link = "";
                String prospect_type = "", equ_details = "", sale_type = "", createdtime = "", modifiedtime = "", modifiedby = "", closingdate = "", exp_delivery_date = "", pndt = "", demo_done = "", s_i = "", demo_date = "", fun_req = "", site_read = "",
                        support_person = "", support_type = "", opportunity_type = "", department = "", rating = "", interest_type = "",
                        txt_adj = "", hdnS_H_Amount = "", pre_tax_total = "", hdnSubTotal = "", listprice = "", comment = "", discount_amount = "", tax1 = "", tax2 = "", tax3 = "", lost_reason = "",
                        competition_name = "", price_quoted = "", general_remark = "", description = "", termsnconditions = "";

                for (SyncBlocks syncBlocks1 : syncBlocks) {
                    String label = syncBlocks1.getLabel();
                    //Basic Information
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("sales_stage")) {
                                salesstageValue = String.valueOf(values);


                            } else if (name.equals("potentialname")) {
                                potentialValue = String.valueOf(values);


                            } else if (name.equals("assigned_user_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    assigned = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("related_to")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    related = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("contact_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    contact = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("potential_no")) {
                                potentialNo = String.valueOf(values);

                            } else if (name.equals("location")) {
                                location = String.valueOf(values);

                            } else if (name.equals("employee")) {
                                employee = String.valueOf(values);
                            } else if (name.equals("leadsource")) {
                                leadsource = String.valueOf(values);
                            } else if (name.equals("cf_954")) {
                                remark_sales_person = String.valueOf(values);
                            } else if (name.equals("segment")) {
                                segment = String.valueOf(values);
                            } else if (name.equals("modality")) {
                                modality = String.valueOf(values);
                            } else if (name.equals("circle")) {
                                circle = String.valueOf(values);
                            }


                        }
                    } else if (label.equals("Opportunity Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("cf_996")) {
                                title = String.valueOf(values);
                            } else if (name.equals("cf_897")) {
                                winprobValue = String.valueOf(values);
                            } else if (name.equals("cf_960")) {
                                prospect_type = String.valueOf(values);
                            } else if (name.equals("cf_1010")) {
                                equ_details = String.valueOf(values);
                            } else if (name.equals("cf_992")) {
                                sale_type = String.valueOf(values);
                            } else if (name.equals("createdtime")) {
                                createdtime = String.valueOf(values);
                            } else if (name.equals("modifiedtime")) {
                                modifiedtime = String.valueOf(values);
                            } else if (name.equals("modifiedby")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    modifiedby = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("closingdate")) {
                                closingdate = String.valueOf(values);
                            } else if (name.equals("cf_1000")) {
                                exp_delivery_date = String.valueOf(values);
                            } else if (name.equals("cf_998")) {
                                pndt = String.valueOf(values);
                            } else if (name.equals("demo_done")) {
                                demo_done = String.valueOf(values);
                            } else if (name.equals("cf_903")) {
                                s_i = String.valueOf(values);
                            } else if (name.equals("demo_date")) {
                                demo_date = String.valueOf(values);
                            } else if (name.equals("cf_899")) {
                                fun_req = String.valueOf(values);
                            } else if (name.equals("cf_1002")) {
                                site_read = String.valueOf(values);
                            } else if (name.equals("cf_1004")) {
                                support_person = String.valueOf(values);
                            } else if (name.equals("cf_1006")) {
                                support_type = String.valueOf(values);
                            } else if (name.equals("opportunity_type")) {
                                opportunity_type = String.valueOf(values);
                            } else if (name.equals("pdf_link")) {
                                pdf_link = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Lead Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("cf_891")) {
                                department = String.valueOf(values);
                            } else if (name.equals("cf_893")) {
                                rating = String.valueOf(values);
                            } else if (name.equals("cf_895")) {
                                interest_type = String.valueOf(values);
                            }


                        }


                    } else if (label.equals("Products")) {
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
                                valueAndQty = String.valueOf(values);
                            } else if (name.equals("quantity")) {
                                quantity = String.valueOf(values);

                                if (quantity.contains(".")) {
                                    quantitys = quantity.substring(0, quantity.indexOf(".")).concat(")");//error
                                } else {
                                    quantitys = quantity; // DOES NOT CONTAIN "." PROCEED ACCORDINGLY
                                }

                            } else if (name.equals("cf_897")) {
                                winprobValue = String.valueOf(values);
                            } else if (name.equals("txtAdjustment")) {
                                txt_adj = String.valueOf(values);
                            } else if (name.equals("hdnS_H_Amount")) {
                                hdnS_H_Amount = String.valueOf(values);
                            } else if (name.equals("pre_tax_total")) {
                                pre_tax_total = String.valueOf(values);
                            } else if (name.equals("hdnSubTotal")) {
                                hdnSubTotal = String.valueOf(values);
                            } else if (name.equals("listprice")) {
                                listprice = String.valueOf(values);
                                if (listprice.contains(".")) {
                                    listprices = listprice.substring(0, listprice.indexOf(".")).concat(")");//error
                                } else {
                                    listprices = listprice; // DOES NOT CONTAIN "." PROCEED ACCORDINGLY
                                }
                            } else if (name.equals("comment")) {
                                comment = String.valueOf(values);
                            } else if (name.equals("discount_amount")) {
                                discount_amount = String.valueOf(values);
                            } else if (name.equals("tax1")) {
                                tax1 = String.valueOf(values);
                            } else if (name.equals("tax2")) {
                                tax2 = String.valueOf(values);
                            } else if (name.equals("tax3")) {
                                tax3 = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Lost Reasons")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("lost_reason")) {
                                lost_reason = String.valueOf(values);
                            } else if (name.equals("competition_name")) {
                                competition_name = String.valueOf(values);
                            } else if (name.equals("price_quoted")) {
                                price_quoted = String.valueOf(values);
                            } else if (name.equals("general_remark")) {
                                general_remark = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Description Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("description")) {
                                description = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Terms And Conditions")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("termsnconditions")) {
                                termsnconditions = String.valueOf(values);
                            }
                        }
                    }
                    String rupee = getResources().getString(R.string.Rs);
                    //        int paswd = (int) Double.parseDouble(quantity);
                    //       String qty = String.valueOf(paswd);
                    SynFields synFields2 = new SynFields(name, productID, productValue,hdnSubTotal,pre_tax_total,quantity,listprice);
                    productList.add(synFields2);
                    PreferenceManagerSalesSupport.getInstance(requireContext()).setSupportMultipleData(potentialValue, related, assigned, contact, location, productValue, rupee.concat(valueAndQty),
                            potentialNo, mobie, competition_name, circle, closingdate, comment, createdtime, demo_date, demo_done, department
                            , description, discount_amount, equ_details, employee, exp_delivery_date, fun_req, general_remark, hdnS_H_Amount, hdnSubTotal,
                            interest_type, lost_reason, leadsource, listprices, modality, modifiedby, modifiedtime, opportunity_type, rupee.concat(pre_tax_total), pndt, price_quoted, prospect_type,
                            rating, remark_sales_person, sale_type, support_type, s_i, segment, site_read, txt_adj, tax1, tax2, tax3, termsnconditions, winprobValue, support_person, quantity, salesstageValue, title, pdf_link);

                }
                String rupee = getResources().getString(R.string.Rs);
                //  int paswd = (int) Double.parseDouble(quantity);
                //  String qty = String.valueOf(paswd);


                ModelTest modelTest = new ModelTest(potentialValue, related, assigned, contact, location, productValue, rupee.concat(valueAndQty).concat("("),
                        potentialNo, mobie, competition_name, circle, closingdate, comment, createdtime, demo_date, demo_done, department
                        , description, discount_amount, equ_details, employee, exp_delivery_date, fun_req, general_remark, hdnS_H_Amount, hdnSubTotal,
                        interest_type, lost_reason, leadsource, listprices, modality, modifiedby, modifiedtime, opportunity_type, rupee.concat(pre_tax_total), pndt, price_quoted, prospect_type,
                        rating, remark_sales_person, sale_type, support_type, s_i, segment, site_read, txt_adj, tax1, tax2, tax3, termsnconditions, winprobValue, support_person, quantitys, salesstageValue, title, pdf_link, opportunity_id);
                if (title.equals("No Support Required")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStageNoSupport) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStageNoSupport.add(modelTest);
                    nosupportAdapter.notifyDataSetChanged();
                } else if (title.equals("Price Support")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStagePrice) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStagePrice.add(modelTest);
                    pricesupportAdapter.notifyDataSetChanged();
                } else if (title.equals("Product Validation Support")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStageProduct) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStageProduct.add(modelTest);
                    productAdapter.notifyDataSetChanged();
                } else if (title.equals("Demo Required")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStageDemo) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStageDemo.add(modelTest);
                    demoRequiredAdapter.notifyDataSetChanged();
                } else if (title.equals("Travel Support")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStageTravelSupport) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStageTravelSupport.add(modelTest);
                    travelsuppotyAdapter.notifyDataSetChanged();
                } else if (title.equals("Funding Required")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSupportStageCFundingrequired) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSupportStageCFundingrequired.add(modelTest);
                    fundingrequiredAdapter.notifyDataSetChanged();
                }
            }


        }


    }

    private void workingOnResponse1(SyncModule syncModule) {

        String success = syncModule.getSuccess();

        if (success.equals("true")) {

            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {
                opportunity_id = syncUpdated.getId();
                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
                String title = "";
                String value = "";
                String potentialValue = "";
                String potentialNo = "";
                String assigned = "";
                String contact = "";
                String location = "";
                String related = "";
                String winprobValue = "";
                String valueAndQty = "";
                String productValue = "";
                String quantity = "";
                String salesstageValue = "";
                String employee = "";
                String leadsource = "";
                String remark_sales_person = "";
                String segment = "";
                String modality = "";
                String circle = "";
                String productID = "";
                String list_prices = "";
                String quantitys = "";
                String pdf_link = "";
                String prospect_type = "", equ_details = "", sale_type = "", createdtime = "", modifiedtime = "", modifiedby = "", closingdate = "", exp_delivery_date = "", pndt = "", demo_done = "", s_i = "", demo_date = "", fun_req = "", site_read = "",
                        support_person = "", support_type = "", opportunity_type = "", department = "", rating = "", interest_type = "",
                        txt_adj = "", hdnS_H_Amount = "", pre_tax_total = "", hdnSubTotal = "", listprice = "", comment = "", discount_amount = "", tax1 = "", tax2 = "", tax3 = "", lost_reason = "",
                        competition_name = "", price_quoted = "", general_remark = "", description = "", termsnconditions = "";

                for (SyncBlocks syncBlocks1 : syncBlocks) {
                    String label = syncBlocks1.getLabel();
                    //Basic Information
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("sales_stage")) {
                                salesstageValue = String.valueOf(values);


                            } else if (name.equals("potentialname")) {
                                potentialValue = String.valueOf(values);


                            } else if (name.equals("assigned_user_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    assigned = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("related_to")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
//                                            assigned_tos.add(v);
                                    related = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("contact_id")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    contact = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("potential_no")) {
                                potentialNo = String.valueOf(values);

                            } else if (name.equals("location")) {
                                location = String.valueOf(values);

                            } else if (name.equals("employee")) {
                                employee = String.valueOf(values);
                            } else if (name.equals("leadsource")) {
                                leadsource = String.valueOf(values);
                            } else if (name.equals("cf_954")) {
                                remark_sales_person = String.valueOf(values);
                            } else if (name.equals("segment")) {
                                segment = String.valueOf(values);
                            } else if (name.equals("modality")) {
                                modality = String.valueOf(values);
                            } else if (name.equals("circle")) {
                                circle = String.valueOf(values);
                            }


                        }
                    } else if (label.equals("Opportunity Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("cf_996")) {
                                title = String.valueOf(values);
                            } else if (name.equals("cf_897")) {
                                winprobValue = String.valueOf(values);
                            } else if (name.equals("cf_960")) {
                                prospect_type = String.valueOf(values);
                            } else if (name.equals("cf_1010")) {
                                equ_details = String.valueOf(values);
                            } else if (name.equals("cf_992")) {
                                sale_type = String.valueOf(values);
                            } else if (name.equals("createdtime")) {
                                createdtime = String.valueOf(values);
                            } else if (name.equals("modifiedtime")) {
                                modifiedtime = String.valueOf(values);
                            } else if (name.equals("modifiedby")) {
                                try {
                                    Gson gson = new Gson();
                                    String strJson = gson.toJson(values);
                                    JSONObject jsonObject = new JSONObject(strJson);
                                    String v = jsonObject.getString("label");
                                    modifiedby = v;
                                } catch (Exception ex) {
                                    Log.e("SalesStageFragment", "Exception is : " + ex.toString());
                                }

                            } else if (name.equals("closingdate")) {
                                closingdate = String.valueOf(values);
                            } else if (name.equals("cf_1000")) {
                                exp_delivery_date = String.valueOf(values);
                            } else if (name.equals("cf_998")) {
                                pndt = String.valueOf(values);
                            } else if (name.equals("demo_done")) {
                                demo_done = String.valueOf(values);
                            } else if (name.equals("cf_903")) {
                                s_i = String.valueOf(values);
                            } else if (name.equals("demo_date")) {
                                demo_date = String.valueOf(values);
                            } else if (name.equals("cf_899")) {
                                fun_req = String.valueOf(values);
                            } else if (name.equals("cf_1002")) {
                                site_read = String.valueOf(values);
                            } else if (name.equals("cf_1004")) {
                                support_person = String.valueOf(values);
                            } else if (name.equals("cf_1006")) {
                                support_type = String.valueOf(values);
                            } else if (name.equals("opportunity_type")) {
                                opportunity_type = String.valueOf(values);
                            } else if (name.equals("pdf_link")) {
                                pdf_link = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Lead Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("cf_891")) {
                                department = String.valueOf(values);
                            } else if (name.equals("cf_893")) {
                                rating = String.valueOf(values);
                            } else if (name.equals("cf_895")) {
                                interest_type = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Products")) {
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
                                valueAndQty = String.valueOf(values);
                            } else if (name.equals("quantity")) {
                                quantity = String.valueOf(values);
                                if (quantity.contains(".")) {
                                    quantitys = quantity.substring(0, quantity.indexOf(".")).concat(")");//error
                                } else {
                                    quantitys = quantity; // DOES NOT CONTAIN "." PROCEED ACCORDINGLY
                                }
                                //  quantitys=quantity.substring(0,quantity.indexOf(".")).concat(")");
                            } else if (name.equals("cf_897")) {
                                winprobValue = String.valueOf(values);
                            } else if (name.equals("txtAdjustment")) {
                                txt_adj = String.valueOf(values);
                            } else if (name.equals("hdnS_H_Amount")) {
                                hdnS_H_Amount = String.valueOf(values);
                            } else if (name.equals("pre_tax_total")) {
                                pre_tax_total = String.valueOf(values);
                            } else if (name.equals("hdnSubTotal")) {
                                hdnSubTotal = String.valueOf(values);
                            } else if (name.equals("listprice")) {

                                listprice = String.valueOf(values);
                                if (listprice.contains(".")) {
                                    list_prices = listprice.substring(0, listprice.indexOf("."));
                                } else {
                                    list_prices = listprice;
                                }
                            } else if (name.equals("comment")) {
                                comment = String.valueOf(values);
                            } else if (name.equals("discount_amount")) {
                                discount_amount = String.valueOf(values);
                            } else if (name.equals("tax1")) {
                                tax1 = String.valueOf(values);
                            } else if (name.equals("tax2")) {
                                tax2 = String.valueOf(values);
                            } else if (name.equals("tax3")) {
                                tax3 = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Lost Reasons")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("lost_reason")) {
                                lost_reason = String.valueOf(values);
                            } else if (name.equals("competition_name")) {
                                competition_name = String.valueOf(values);
                            } else if (name.equals("price_quoted")) {
                                price_quoted = String.valueOf(values);
                            } else if (name.equals("general_remark")) {
                                general_remark = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Description Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("description")) {
                                description = String.valueOf(values);
                            }
                        }
                    } else if (label.equals("Terms And Conditions")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();


                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("termsnconditions")) {
                                termsnconditions = String.valueOf(values);
                            }
                        }
                    }
                    String rupee = getResources().getString(R.string.Rs);


                    PreferenceManagerSalesSupport.getInstance(requireContext()).setSupportMultipleData(potentialValue, related, assigned, contact, location, productValue, rupee.concat(valueAndQty),
                            potentialNo, mobie, competition_name, circle, closingdate, comment, createdtime, demo_date, demo_done, department
                            , description, discount_amount, equ_details, employee, exp_delivery_date, fun_req, general_remark, hdnS_H_Amount, hdnSubTotal,
                            interest_type, lost_reason, leadsource, list_prices, modality, modifiedby, modifiedtime, opportunity_type, rupee.concat(pre_tax_total), pndt, price_quoted, prospect_type,
                            rating, remark_sales_person, sale_type, support_type, s_i, segment, site_read, txt_adj, tax1, tax2, tax3, termsnconditions, winprobValue, support_person, quantitys, salesstageValue, title, pdf_link);

                }
                String rupee = getResources().getString(R.string.Rs);
                SynFields synFields2 = new SynFields(name, productID, productValue,hdnSubTotal,pre_tax_total,quantity,listprice);
                productList.add(synFields2);

                ModelTest modelTest = new ModelTest(potentialValue, related, assigned, contact, location, productValue, rupee.concat(valueAndQty).concat("("),
                        potentialNo, mobie, competition_name, circle, closingdate, comment, createdtime, demo_date, demo_done, department
                        , description, discount_amount, equ_details, employee, exp_delivery_date, fun_req, general_remark, hdnS_H_Amount, hdnSubTotal,
                        interest_type, lost_reason, leadsource, list_prices, modality, modifiedby, modifiedtime, opportunity_type, rupee.concat(pre_tax_total), pndt, price_quoted, prospect_type,
                        rating, remark_sales_person, sale_type, support_type, s_i, segment, site_read, txt_adj, tax1, tax2, tax3, termsnconditions, winprobValue, support_person, quantitys, salesstageValue, title, pdf_link, opportunity_id);
                if (salesstageValue.equals("Opportunity")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSalesStageOpportunity) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSalesStageOpportunity.add(modelTest);
                    opportunityAdapter.notifyDataSetChanged();


                } else if (salesstageValue.equals("Proposal or Price Quote")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSalesStageProposal) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSalesStageProposal.add(modelTest);
                    proposalAdapter.notifyDataSetChanged();

                } else if (salesstageValue.equals("Negotiation or Review")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSalesStageNegotiation) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSalesStageNegotiation.add(modelTest);
                    negotiationAdapter.notifyDataSetChanged();
                } else if (salesstageValue.equals("Closed Won")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSalesStageClosedWon) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSalesStageClosedWon.add(modelTest);
                    closedwonAdapter.notifyDataSetChanged();

                } else if (salesstageValue.equals("Closed Lost")) {
                    boolean isDuplicate = false;
                    for (ModelTest modelTest1 : listSalesStageClosedLost) {
                        if (modelTest1.getPotentialNo().equals(modelTest.getPotentialNo())) {
                            modelTest1.setProductValue(modelTest1.getProductValue() + "," + modelTest.getProductValue());
                            modelTest1.setListprice(modelTest1.getListprice() + "," + modelTest.getListprice());
                            isDuplicate = true;
                        }
                    }
                    if (!isDuplicate)
                        listSalesStageClosedLost.add(modelTest);
                    closedLostAdapter.notifyDataSetChanged();
//
                }
            }
        }
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
                    ((MainActivity) getActivity()).circleReveal(R.id.searchtoolbar, 1, true, true);
                else
                    searchtollbar.setVisibility(View.VISIBLE);

                item_search.expandActionView();
                return true;
            case R.id.refresh:
                listSalesStageOpportunity.clear();
                listSalesStageProposal.clear();
                listSalesStageNegotiation.clear();
                listSalesStageClosedLost.clear();
                listSalesStageClosedWon.clear();
                fetchJSONForSales();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }


    @Override
    public void myEditItemClick(int position) {

        isDuplicate = true;

        String sales_stages = listSalesStageOpportunity.get(position).getSalesstageValue();
        String opp_names = listSalesStageOpportunity.get(position).getPotentialValue();
        String assignedto = listSalesStageOpportunity.get(position).getAssigned();
        String contact = listSalesStageOpportunity.get(position).getContact();
        String competition_name = listSalesStageOpportunity.get(position).getCompetition_name();
        String circles = listSalesStageOpportunity.get(position).getCircle();
        String closingdates = listSalesStageOpportunity.get(position).getClosingdate();
        String comments = listSalesStageOpportunity.get(position).getComment();
        String createdtimes = listSalesStageOpportunity.get(position).getCreatedtime();
        String demo_dates = listSalesStageOpportunity.get(position).getDemo_date();
        String demo_dones = listSalesStageOpportunity.get(position).getDemo_done();
        String departments = listSalesStageOpportunity.get(position).getDepartment();
        String discount_amounts = listSalesStageOpportunity.get(position).getDiscount_amount();
        String descriptions = listSalesStageOpportunity.get(position).getDescription();
        String employees = listSalesStageOpportunity.get(position).getEmployee();
        String equ_details = listSalesStageOpportunity.get(position).getEqu_details();
        String exp_del_dates = listSalesStageOpportunity.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageOpportunity.get(position).getFun_req();
        String general_remarks = listSalesStageOpportunity.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageOpportunity.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageOpportunity.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageOpportunity.get(position).getInterest_type();
        String leadsources = listSalesStageOpportunity.get(position).getLeadsource();
        String listprices = listSalesStageOpportunity.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSalesStageOpportunity.get(position).getLocation();
        String lost_reasons = listSalesStageOpportunity.get(position).getLost_reason();
        String modalitys = listSalesStageOpportunity.get(position).getModality();
        String modifiedbys = listSalesStageOpportunity.get(position).getModifiedby();
        String modifiedtimes = listSalesStageOpportunity.get(position).getModifiedtime();
        String multi_deals = listSalesStageOpportunity.get(position).getOpportunity_type();
        String potentialNos = listSalesStageOpportunity.get(position).getPotentialNo();
        String pndtsopp = listSalesStageOpportunity.get(position).getPndt();
        String pre_tax_totals = listSalesStageOpportunity.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageOpportunity.get(position).getPrice_quoted();
        String productValues = listSalesStageOpportunity.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSalesStageOpportunity.get(position).getProspect_type();
        String quantitys = listSalesStageOpportunity.get(position).getQty();
        String ratings = listSalesStageOpportunity.get(position).getRating();
        String s_is = listSalesStageOpportunity.get(position).getS_i();
        String sale_types = listSalesStageOpportunity.get(position).getSale_type();
        String segments = listSalesStageOpportunity.get(position).getSegment();
        String site_reads = listSalesStageOpportunity.get(position).getSite_read();
        String support_persons = listSalesStageOpportunity.get(position).getSupport_person();
        String support_types = listSalesStageOpportunity.get(position).getSupport_type();
        String termsnconditions = listSalesStageOpportunity.get(position).getTermsnconditions();
        String totals = listSalesStageOpportunity.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageOpportunity.get(position).getTxt_adj();
        String winprobValues = listSalesStageOpportunity.get(position).getWinprobValue();
        String account_names = listSalesStageOpportunity.get(position).getRelated();
        String support_person1 = listSalesStageOpportunity.get(position).getSupport_person();
        String services = listSalesStageOpportunity.get(position).getTax3();
        String sales = listSalesStageOpportunity.get(position).getTax2();
        String vats = listSalesStageOpportunity.get(position).getTax1();
        String support_requireds = listSalesStageOpportunity.get(position).getTitle();
        String remark_sales_persons = listSalesStageOpportunity.get(position).getRemark_sales_person();
        String exp_po_date = listSalesStageOpportunity.get(position).getDemo_date();
        String pdf_link = listSalesStageOpportunity.get(position).getPdf_link();
        String opportunity_ids = listSalesStageOpportunity.get(position).getOpportunity_id();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stage", sales_stages);
        args.putString("opp_names", opp_names);
        args.putString("contact", contact);
        args.putString("account_names", account_names);
        args.putString("locations", locations);
        args.putString("leadsources", leadsources);
        args.putString("remark_sales_persons", remark_sales_persons);
        args.putString("prospect_types", prospect_types);
        args.putString("sale_types", sale_types);
        args.putString("winprobValues", winprobValues);
        args.putString("support_requireds", support_requireds);
        args.putString("exp_po_date", exp_po_date);
        args.putString("exp_del_dates", exp_del_dates);
        args.putString("pndtsopp", pndtsopp);
        args.putString("demo_dones", demo_dones);
        args.putString("s_is", s_is);
        args.putString("fun_requireds", fun_requireds);
        args.putString("site_reads", site_reads);
        args.putString("multi_deals", multi_deals);
        args.putString("pdf_link", pdf_link);
        args.putString("descriptions", descriptions);
        args.putString("item_list", item_list);
        args.putString("quantitys", quantitys);
        args.putString("totals", totals);
        args.putString("termsnconditions", termsnconditions);
        args.putString("assignedto", assignedto);
        args.putString("opportunity_ids", opportunity_ids);
        args.putString("employee", employees);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);


    }

    //Proposal edit option
    @Override
    public void mypropsalEditItemClick(int position) {

        isDuplicate = true;

        String sales_stages = listSalesStageProposal.get(position).getSalesstageValue();
        String opp_names = listSalesStageProposal.get(position).getPotentialValue();
        String assigned = listSalesStageProposal.get(position).getAssigned();
        String contact = listSalesStageProposal.get(position).getContact();
        String competition_name = listSalesStageProposal.get(position).getCompetition_name();
        String circles = listSalesStageProposal.get(position).getCircle();
        String closingdates = listSalesStageProposal.get(position).getClosingdate();
        String comments = listSalesStageProposal.get(position).getComment();
        String createdtimes = listSalesStageProposal.get(position).getCreatedtime();
        String demo_dates = listSalesStageProposal.get(position).getDemo_date();
        String demo_dones = listSalesStageProposal.get(position).getDemo_done();
        String departments = listSalesStageProposal.get(position).getDepartment();
        String discount_amounts = listSalesStageProposal.get(position).getDiscount_amount();
        String descriptions = listSalesStageProposal.get(position).getDescription();
        String employees = listSalesStageProposal.get(position).getEmployee();
        String equ_details = listSalesStageProposal.get(position).getEqu_details();
        String exp_del_dates = listSalesStageProposal.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageProposal.get(position).getFun_req();
        String general_remarks = listSalesStageProposal.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageProposal.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageProposal.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageProposal.get(position).getInterest_type();
        String leadsources = listSalesStageProposal.get(position).getLeadsource();
        String listprices = listSalesStageProposal.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSalesStageProposal.get(position).getLocation();
        String lost_reasons = listSalesStageProposal.get(position).getLost_reason();
        String modalitys = listSalesStageProposal.get(position).getModality();
        String modifiedbys = listSalesStageProposal.get(position).getModifiedby();
        String modifiedtimes = listSalesStageProposal.get(position).getModifiedtime();
        String multi_deals = listSalesStageProposal.get(position).getOpportunity_type();
        String potentialNos = listSalesStageProposal.get(position).getPotentialNo();
        String pndts = listSalesStageProposal.get(position).getPndt();
        String pre_tax_totals = listSalesStageProposal.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageProposal.get(position).getPrice_quoted();
        String productValues = listSalesStageProposal.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSalesStageProposal.get(position).getProspect_type();
        String quantitys = listSalesStageProposal.get(position).getQty();
        String ratings = listSalesStageProposal.get(position).getRating();
        String s_is = listSalesStageProposal.get(position).getS_i();
        String sale_types = listSalesStageProposal.get(position).getSale_type();
        String segments = listSalesStageProposal.get(position).getSegment();
        String site_reads = listSalesStageProposal.get(position).getSite_read();
        String support_persons = listSalesStageProposal.get(position).getSupport_person();
        String support_types = listSalesStageProposal.get(position).getSupport_type();
        String termsnconditions = listSalesStageProposal.get(position).getTermsnconditions();
        String totals = listSalesStageProposal.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageProposal.get(position).getTxt_adj();
        String winprobValues = listSalesStageProposal.get(position).getWinprobValue();
        String account_names = listSalesStageProposal.get(position).getRelated();
        String support_person1 = listSalesStageProposal.get(position).getSupport_person();
        String services = listSalesStageProposal.get(position).getTax3();
        String sales = listSalesStageProposal.get(position).getTax2();
        String vats = listSalesStageProposal.get(position).getTax1();
        String support_requireds = listSalesStageProposal.get(position).getTitle();
        String remark_sales_persons = listSalesStageProposal.get(position).getRemark_sales_person();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stages", sales_stages);
//        args.putString("facilityType", facilityType);
//        args.putString("ownership_types", ownership_types);
//        args.putString("pin_code", pin_code);
//        args.putString("bill_streets", bill_streets);
//        args.putString("bill_citys", bill_citys);
//        args.putString("bill_districts", bill_districts);
//        args.putString("assigned_tos", assigned_tos);
//        args.putString("email", email);
//        args.putString("mobiles", mobiles);
//        args.putString("bill_states", bill_states);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);


    }

    //Negation Edit option

    //Proposal edit option
    @Override
    public void myEditNegationItemClick(int position) {

        isDuplicate = true;

        String sales_stages = listSalesStageNegotiation.get(position).getSalesstageValue();
        String opp_names = listSalesStageNegotiation.get(position).getPotentialValue();
        String assigned = listSalesStageNegotiation.get(position).getAssigned();
        String contact = listSalesStageNegotiation.get(position).getContact();
        String competition_name = listSalesStageNegotiation.get(position).getCompetition_name();
        String circles = listSalesStageNegotiation.get(position).getCircle();
        String closingdates = listSalesStageNegotiation.get(position).getClosingdate();
        String comments = listSalesStageNegotiation.get(position).getComment();
        String createdtimes = listSalesStageNegotiation.get(position).getCreatedtime();
        String demo_dates = listSalesStageNegotiation.get(position).getDemo_date();
        String demo_dones = listSalesStageNegotiation.get(position).getDemo_done();
        String departments = listSalesStageNegotiation.get(position).getDepartment();
        String discount_amounts = listSalesStageNegotiation.get(position).getDiscount_amount();
        String descriptions = listSalesStageNegotiation.get(position).getDescription();
        String employees = listSalesStageNegotiation.get(position).getEmployee();
        String equ_details = listSalesStageNegotiation.get(position).getEqu_details();
        String exp_del_dates = listSalesStageNegotiation.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageNegotiation.get(position).getFun_req();
        String general_remarks = listSalesStageNegotiation.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageNegotiation.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageNegotiation.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageNegotiation.get(position).getInterest_type();
        String leadsources = listSalesStageNegotiation.get(position).getLeadsource();
        String listprices = listSalesStageNegotiation.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSalesStageNegotiation.get(position).getLocation();
        String lost_reasons = listSalesStageNegotiation.get(position).getLost_reason();
        String modalitys = listSalesStageNegotiation.get(position).getModality();
        String modifiedbys = listSalesStageNegotiation.get(position).getModifiedby();
        String modifiedtimes = listSalesStageNegotiation.get(position).getModifiedtime();
        String multi_deals = listSalesStageNegotiation.get(position).getOpportunity_type();
        String potentialNos = listSalesStageNegotiation.get(position).getPotentialNo();
        String pndts = listSalesStageNegotiation.get(position).getPndt();
        String pre_tax_totals = listSalesStageNegotiation.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageNegotiation.get(position).getPrice_quoted();
        String productValues = listSalesStageNegotiation.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSalesStageNegotiation.get(position).getProspect_type();
        String quantitys = listSalesStageNegotiation.get(position).getQty();
        String ratings = listSalesStageNegotiation.get(position).getRating();
        String s_is = listSalesStageNegotiation.get(position).getS_i();
        String sale_types = listSalesStageNegotiation.get(position).getSale_type();
        String segments = listSalesStageNegotiation.get(position).getSegment();
        String site_reads = listSalesStageNegotiation.get(position).getSite_read();
        String support_persons = listSalesStageNegotiation.get(position).getSupport_person();
        String support_types = listSalesStageNegotiation.get(position).getSupport_type();
        String termsnconditions = listSalesStageNegotiation.get(position).getTermsnconditions();
        String totals = listSalesStageNegotiation.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageNegotiation.get(position).getTxt_adj();
        String winprobValues = listSalesStageNegotiation.get(position).getWinprobValue();
        String account_names = listSalesStageNegotiation.get(position).getRelated();
        String support_person1 = listSalesStageNegotiation.get(position).getSupport_person();
        String services = listSalesStageNegotiation.get(position).getTax3();
        String sales = listSalesStageNegotiation.get(position).getTax2();
        String vats = listSalesStageNegotiation.get(position).getTax1();
        String support_requireds = listSalesStageNegotiation.get(position).getTitle();
        String remark_sales_persons = listSalesStageNegotiation.get(position).getRemark_sales_person();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stages", sales_stages);
//        args.putString("facilityType", facilityType);
//        args.putString("ownership_types", ownership_types);
//        args.putString("pin_code", pin_code);
//        args.putString("bill_streets", bill_streets);
//        args.putString("bill_citys", bill_citys);
//        args.putString("bill_districts", bill_districts);
//        args.putString("assigned_tos", assigned_tos);
//        args.putString("email", email);
//        args.putString("mobiles", mobiles);
//        args.putString("bill_states", bill_states);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);


    }


    @Override
    public void myPDfItemClick(int position) {

        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 112);


        if (!hasPermissions(getContext(), PERMISSIONS)) {

            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ");

            Toast t = Toast.makeText(getContext().getApplicationContext(), "You don't have read access !", Toast.LENGTH_LONG);
            t.show();

        } else {
            File d = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);  // -> filename = maven.pdf
            String url_path = listSalesStageOpportunity.get(position).getPdf_link();
            ///Log.d("url_path", url_path);
            String file_name = url_path.substring(url_path.lastIndexOf('/') + 1);
            //Log.d("file_name", file_name);
            File pdfFile = new File(d, file_name);

            Log.v(TAG, "view() Method pdfFile " + pdfFile.getAbsolutePath());

            if (pdfFile != null) {

                Uri path = GenericFileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", pdfFile);


                Log.v(TAG, "view() Method path " + url_path);

                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
                //  }
            }
            Log.v(TAG, "view() Method completed ");
            // download(v);

        }
        download(position);
        request(position);


    }

    public void request(int position) {

        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 112);

    }

    public void download(int position) {
        Log.v(TAG, "download() Method invoked ");
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, 112);

        if (!hasPermissions(getContext(), PERMISSIONS)) {

            Log.v(TAG, "download() Method DON'T HAVE PERMISSIONS ");

            Toast t = Toast.makeText(getContext().getApplicationContext(), "You don't have write access !", Toast.LENGTH_LONG);
            t.show();

        } else {
            Log.v(TAG, "download() Method HAVE PERMISSIONS ");
            String url_path = listSalesStageOpportunity.get(position).getPdf_link();
            Log.d("url_path", url_path);
            String file_name = url_path.substring(url_path.lastIndexOf('/') + 1);
            Log.d("file_name", file_name);

            new DownloadFile().execute(url_path, file_name);

        }

        Log.v(TAG, "download() Method completed ");

    }

    @Override
    public void myFundingEditItemClick(int position) {

    }

    @Override
    public void myTravelItemClick(int position) {
        String sales_stages = listSupportStageTravelSupport.get(position).getTitle();
        String opp_names = listSupportStageTravelSupport.get(position).getPotentialValue();
        String assigned = listSupportStageTravelSupport.get(position).getAssigned();
        String contact = listSupportStageTravelSupport.get(position).getContact();
        String competition_name = listSupportStageTravelSupport.get(position).getCompetition_name();
        String circles = listSupportStageTravelSupport.get(position).getCircle();
        String closingdates = listSupportStageTravelSupport.get(position).getClosingdate();
        String comments = listSupportStageTravelSupport.get(position).getComment();
        String createdtimes = listSupportStageTravelSupport.get(position).getCreatedtime();
        String demo_dates = listSupportStageTravelSupport.get(position).getDemo_date();
        String demo_dones = listSupportStageTravelSupport.get(position).getDemo_done();
        String departments = listSupportStageTravelSupport.get(position).getDepartment();
        String discount_amounts = listSupportStageTravelSupport.get(position).getDiscount_amount();
        String descriptions = listSupportStageTravelSupport.get(position).getDescription();
        String employees = listSupportStageTravelSupport.get(position).getEmployee();
        String equ_details = listSupportStageTravelSupport.get(position).getEqu_details();
        String exp_del_dates = listSupportStageTravelSupport.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageTravelSupport.get(position).getFun_req();
        String general_remarks = listSupportStageTravelSupport.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageTravelSupport.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageTravelSupport.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageTravelSupport.get(position).getInterest_type();
        String leadsources = listSupportStageTravelSupport.get(position).getLeadsource();
        String listprices = listSupportStageTravelSupport.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSupportStageTravelSupport.get(position).getLocation();
        String lost_reasons = listSupportStageTravelSupport.get(position).getLost_reason();
        String modalitys = listSupportStageTravelSupport.get(position).getModality();
        String modifiedbys = listSupportStageTravelSupport.get(position).getModifiedby();
        String modifiedtimes = listSupportStageTravelSupport.get(position).getModifiedtime();
        String multi_deals = listSupportStageTravelSupport.get(position).getOpportunity_type();
        String potentialNos = listSupportStageTravelSupport.get(position).getPotentialNo();
        String pndts = listSupportStageTravelSupport.get(position).getPndt();
        String pre_tax_totals = listSupportStageTravelSupport.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageTravelSupport.get(position).getPrice_quoted();
        String productValues = listSupportStageTravelSupport.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSupportStageTravelSupport.get(position).getProspect_type();
        String quantitys = listSupportStageTravelSupport.get(position).getQty();
        String ratings = listSupportStageTravelSupport.get(position).getRating();
        String s_is = listSupportStageTravelSupport.get(position).getS_i();
        String sale_types = listSupportStageTravelSupport.get(position).getSale_type();
        String segments = listSupportStageTravelSupport.get(position).getSegment();
        String site_reads = listSupportStageTravelSupport.get(position).getSite_read();
        String support_persons = listSupportStageTravelSupport.get(position).getSupport_person();
        String support_types = listSupportStageTravelSupport.get(position).getSupport_type();
        String termsnconditions = listSupportStageTravelSupport.get(position).getTermsnconditions();
        String totals = listSupportStageTravelSupport.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageTravelSupport.get(position).getTxt_adj();
        String winprobValues = listSupportStageTravelSupport.get(position).getWinprobValue();
        String account_names = listSupportStageTravelSupport.get(position).getRelated();
        String support_person1 = listSupportStageTravelSupport.get(position).getSupport_person();
        String services = listSupportStageTravelSupport.get(position).getTax3();
        String sales = listSupportStageTravelSupport.get(position).getTax2();
        String vats = listSupportStageTravelSupport.get(position).getTax1();
        String support_requireds = listSupportStageTravelSupport.get(position).getTitle();
        String remark_sales_persons = listSupportStageTravelSupport.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk = customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0, getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty = quantitys.replace(")", "");
            String total = totals.replace("(", "");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        // listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        // total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //  quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public void myTravelEditItemClick(int position) {

    }

    @Override
    public void myDemoItemClick(int position) {
        String sales_stages = listSupportStageDemo.get(position).getTitle();
        String opp_names = listSupportStageDemo.get(position).getPotentialValue();
        String assigned = listSupportStageDemo.get(position).getAssigned();
        String contact = listSupportStageDemo.get(position).getContact();
        String competition_name = listSupportStageDemo.get(position).getCompetition_name();
        String circles = listSupportStageDemo.get(position).getCircle();
        String closingdates = listSupportStageDemo.get(position).getClosingdate();
        String comments = listSupportStageDemo.get(position).getComment();
        String createdtimes = listSupportStageDemo.get(position).getCreatedtime();
        String demo_dates = listSupportStageDemo.get(position).getDemo_date();
        String demo_dones = listSupportStageDemo.get(position).getDemo_done();
        String departments = listSupportStageDemo.get(position).getDepartment();
        String discount_amounts = listSupportStageDemo.get(position).getDiscount_amount();
        String descriptions = listSupportStageDemo.get(position).getDescription();
        String employees = listSupportStageDemo.get(position).getEmployee();
        String equ_details = listSupportStageDemo.get(position).getEqu_details();
        String exp_del_dates = listSupportStageDemo.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageDemo.get(position).getFun_req();
        String general_remarks = listSupportStageDemo.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageDemo.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageDemo.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageDemo.get(position).getInterest_type();
        String leadsources = listSupportStageDemo.get(position).getLeadsource();
        String listprices = listSupportStageDemo.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSupportStageDemo.get(position).getLocation();
        String lost_reasons = listSupportStageDemo.get(position).getLost_reason();
        String modalitys = listSupportStageDemo.get(position).getModality();
        String modifiedbys = listSupportStageDemo.get(position).getModifiedby();
        String modifiedtimes = listSupportStageDemo.get(position).getModifiedtime();
        String multi_deals = listSupportStageDemo.get(position).getOpportunity_type();
        String potentialNos = listSupportStageDemo.get(position).getPotentialNo();
        String pndts = listSupportStageDemo.get(position).getPndt();
        String pre_tax_totals = listSupportStageDemo.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageDemo.get(position).getPrice_quoted();
        String productValues = listSupportStageDemo.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSupportStageDemo.get(position).getProspect_type();
        String quantitys = listSupportStageDemo.get(position).getQty();
        String ratings = listSupportStageDemo.get(position).getRating();
        String s_is = listSupportStageDemo.get(position).getS_i();
        String sale_types = listSupportStageDemo.get(position).getSale_type();
        String segments = listSupportStageDemo.get(position).getSegment();
        String site_reads = listSupportStageDemo.get(position).getSite_read();
        String support_persons = listSupportStageDemo.get(position).getSupport_person();
        String support_types = listSupportStageDemo.get(position).getSupport_type();
        String termsnconditions = listSupportStageDemo.get(position).getTermsnconditions();
        String totals = listSupportStageDemo.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageDemo.get(position).getTxt_adj();
        String winprobValues = listSupportStageDemo.get(position).getWinprobValue();
        String account_names = listSupportStageDemo.get(position).getRelated();
        String support_person1 = listSupportStageDemo.get(position).getSupport_person();
        String services = listSupportStageDemo.get(position).getTax3();
        String sales = listSupportStageDemo.get(position).getTax2();
        String vats = listSupportStageDemo.get(position).getTax1();
        String support_requireds = listSupportStageDemo.get(position).getTitle();
        String remark_sales_persons = listSupportStageDemo.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        TableLayout stk = customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0, getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty = quantitys.replace(")", "");
            String total = totals.replace("(", "");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK,ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK,ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        // listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        // total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        // quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public void myDemoEditItemClick(int position) {

    }

    @Override
    public void myPriceItemClick(int position) {
        String sales_stages = listSupportStagePrice.get(position).getTitle();
        String opp_names = listSupportStagePrice.get(position).getPotentialValue();
        String assigned = listSupportStagePrice.get(position).getAssigned();
        String contact = listSupportStagePrice.get(position).getContact();
        String competition_name = listSupportStagePrice.get(position).getCompetition_name();
        String circles = listSupportStagePrice.get(position).getCircle();
        String closingdates = listSupportStagePrice.get(position).getClosingdate();
        String comments = listSupportStagePrice.get(position).getComment();
        String createdtimes = listSupportStagePrice.get(position).getCreatedtime();
        String demo_dates = listSupportStagePrice.get(position).getDemo_date();
        String demo_dones = listSupportStagePrice.get(position).getDemo_done();
        String departments = listSupportStagePrice.get(position).getDepartment();
        String discount_amounts = listSupportStagePrice.get(position).getDiscount_amount();
        String descriptions = listSupportStagePrice.get(position).getDescription();
        String employees = listSupportStagePrice.get(position).getEmployee();
        String equ_details = listSupportStagePrice.get(position).getEqu_details();
        String exp_del_dates = listSupportStagePrice.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStagePrice.get(position).getFun_req();
        String general_remarks = listSupportStagePrice.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStagePrice.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStagePrice.get(position).getHdnSubTotal();
        String intrest_types = listSupportStagePrice.get(position).getInterest_type();
        String leadsources = listSupportStagePrice.get(position).getLeadsource();
        String listprices = listSupportStagePrice.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSupportStagePrice.get(position).getLocation();
        String lost_reasons = listSupportStagePrice.get(position).getLost_reason();
        String modalitys = listSupportStagePrice.get(position).getModality();
        String modifiedbys = listSupportStagePrice.get(position).getModifiedby();
        String modifiedtimes = listSupportStagePrice.get(position).getModifiedtime();
        String multi_deals = listSupportStagePrice.get(position).getOpportunity_type();
        String potentialNos = listSupportStagePrice.get(position).getPotentialNo();
        String pndts = listSupportStagePrice.get(position).getPndt();
        String pre_tax_totals = listSupportStagePrice.get(position).getPre_tax_total();
        String price_quoteds = listSupportStagePrice.get(position).getPrice_quoted();
        String productValues = listSupportStagePrice.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSupportStagePrice.get(position).getProspect_type();
        String quantitys = listSupportStagePrice.get(position).getQty();
        String ratings = listSupportStagePrice.get(position).getRating();
        String s_is = listSupportStagePrice.get(position).getS_i();
        String sale_types = listSupportStagePrice.get(position).getSale_type();
        String segments = listSupportStagePrice.get(position).getSegment();
        String site_reads = listSupportStagePrice.get(position).getSite_read();
        String support_persons = listSupportStagePrice.get(position).getSupport_person();
        String support_types = listSupportStagePrice.get(position).getSupport_type();
        String termsnconditions = listSupportStagePrice.get(position).getTermsnconditions();
        String totals = listSupportStagePrice.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStagePrice.get(position).getTxt_adj();
        String winprobValues = listSupportStagePrice.get(position).getWinprobValue();
        String account_names = listSupportStagePrice.get(position).getRelated();
        String support_person1 = listSupportStagePrice.get(position).getSupport_person();
        String services = listSupportStagePrice.get(position).getTax3();
        String sales = listSupportStagePrice.get(position).getTax2();
        String vats = listSupportStagePrice.get(position).getTax1();
        String support_requireds = listSupportStagePrice.get(position).getTitle();
        String remark_sales_persons = listSupportStagePrice.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk = customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0, getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty = quantitys.replace(")", "");
            String total = totals.replace("(", "");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK,ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }




        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        //  listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        // quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }

    @Override
    public void myPriceEditItemClick(int position) {

    }

    @Override
    public void myProductItemClick(int position) {

        String sales_stages = listSupportStageProduct.get(position).getTitle();
        String opp_names = listSupportStageProduct.get(position).getPotentialValue();
        String assigned = listSupportStageProduct.get(position).getAssigned();
        String contact = listSupportStageProduct.get(position).getContact();
        String competition_name = listSupportStageProduct.get(position).getCompetition_name();
        String circles = listSupportStageProduct.get(position).getCircle();
        String closingdates = listSupportStageProduct.get(position).getClosingdate();
        String comments = listSupportStageProduct.get(position).getComment();
        String createdtimes = listSupportStageProduct.get(position).getCreatedtime();
        String demo_dates = listSupportStageProduct.get(position).getDemo_date();
        String demo_dones = listSupportStageProduct.get(position).getDemo_done();
        String departments = listSupportStageProduct.get(position).getDepartment();
        String discount_amounts = listSupportStageProduct.get(position).getDiscount_amount();
        String descriptions = listSupportStageProduct.get(position).getDescription();
        String employees = listSupportStageProduct.get(position).getEmployee();
        String equ_details = listSupportStageProduct.get(position).getEqu_details();
        String exp_del_dates = listSupportStageProduct.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageProduct.get(position).getFun_req();
        String general_remarks = listSupportStageProduct.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageProduct.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageProduct.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageProduct.get(position).getInterest_type();
        String leadsources = listSupportStageProduct.get(position).getLeadsource();
        String listprices = listSupportStageProduct.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSupportStageProduct.get(position).getLocation();
        String lost_reasons = listSupportStageProduct.get(position).getLost_reason();
        String modalitys = listSupportStageProduct.get(position).getModality();
        String modifiedbys = listSupportStageProduct.get(position).getModifiedby();
        String modifiedtimes = listSupportStageProduct.get(position).getModifiedtime();
        String multi_deals = listSupportStageProduct.get(position).getOpportunity_type();
        String potentialNos = listSupportStageProduct.get(position).getPotentialNo();
        String pndts = listSupportStageProduct.get(position).getPndt();
        String pre_tax_totals = listSupportStageProduct.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageProduct.get(position).getPrice_quoted();
        String productValues = listSupportStageProduct.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSupportStageProduct.get(position).getProspect_type();
        String quantitys = listSupportStageProduct.get(position).getQty();
        String ratings = listSupportStageProduct.get(position).getRating();
        String s_is = listSupportStageProduct.get(position).getS_i();
        String sale_types = listSupportStageProduct.get(position).getSale_type();
        String segments = listSupportStageProduct.get(position).getSegment();
        String site_reads = listSupportStageProduct.get(position).getSite_read();
        String support_persons = listSupportStageProduct.get(position).getSupport_person();
        String support_types = listSupportStageProduct.get(position).getSupport_type();
        String termsnconditions = listSupportStageProduct.get(position).getTermsnconditions();
        String totals = listSupportStageProduct.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageProduct.get(position).getTxt_adj();
        String winprobValues = listSupportStageProduct.get(position).getWinprobValue();
        String account_names = listSupportStageProduct.get(position).getRelated();
        String support_person1 = listSupportStageProduct.get(position).getSupport_person();
        String services = listSupportStageProduct.get(position).getTax3();
        String sales = listSupportStageProduct.get(position).getTax2();
        String vats = listSupportStageProduct.get(position).getTax1();
        String support_requireds = listSupportStageProduct.get(position).getTitle();
        String remark_sales_persons = listSupportStageProduct.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE,resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        // listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //  quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

    }

    @Override
    public void myProductEditItemClick(int position) {

    }


    private class DownloadFile extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            Log.v(TAG, "doInBackground() Method invoked ");
            String fileUrl = strings[0];
            Log.d("fileurl",fileUrl);

            String fileName = strings[1];  // -> maven.pdf
            Log.d("fileName",fileName);
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            File pdfFile = new File(folder, fileName);
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.getAbsolutePath());
            Log.v(TAG, "doInBackground() pdfFile invoked " + pdfFile.getAbsoluteFile());

            try {
                pdfFile.createNewFile();
                Log.v(TAG, "doInBackground() file created" + pdfFile);

            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground() error" + e.getMessage());
                Log.e(TAG, "doInBackground() error" + e.getStackTrace());


            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            Log.v(TAG, "doInBackground() file download completed");

            return null;
        }
    }



    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void myFundingItemClick(int position) {

        String sales_stages = listSupportStageCFundingrequired.get(position).getTitle();
        String opp_names = listSupportStageCFundingrequired.get(position).getPotentialValue();
        String assigned = listSupportStageCFundingrequired.get(position).getAssigned();
        String contact = listSupportStageCFundingrequired.get(position).getContact();
        String competition_name = listSupportStageCFundingrequired.get(position).getCompetition_name();
        String circles = listSupportStageCFundingrequired.get(position).getCircle();
        String closingdates = listSupportStageCFundingrequired.get(position).getClosingdate();
        String comments = listSupportStageCFundingrequired.get(position).getComment();
        String createdtimes = listSupportStageCFundingrequired.get(position).getCreatedtime();
        String demo_dates = listSupportStageCFundingrequired.get(position).getDemo_date();
        String demo_dones = listSupportStageCFundingrequired.get(position).getDemo_done();
        String departments = listSupportStageCFundingrequired.get(position).getDepartment();
        String discount_amounts = listSupportStageCFundingrequired.get(position).getDiscount_amount();
        String descriptions = listSupportStageCFundingrequired.get(position).getDescription();
        String employees = listSupportStageCFundingrequired.get(position).getEmployee();
        String equ_details = listSupportStageCFundingrequired.get(position).getEqu_details();
        String exp_del_dates = listSupportStageCFundingrequired.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageCFundingrequired.get(position).getFun_req();
        String general_remarks = listSupportStageCFundingrequired.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageCFundingrequired.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageCFundingrequired.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageCFundingrequired.get(position).getInterest_type();
        String leadsources = listSupportStageCFundingrequired.get(position).getLeadsource();
        String listprices = listSupportStageCFundingrequired.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSupportStageCFundingrequired.get(position).getLocation();
        String lost_reasons = listSupportStageCFundingrequired.get(position).getLost_reason();
        String modalitys = listSupportStageCFundingrequired.get(position).getModality();
        String modifiedbys = listSupportStageCFundingrequired.get(position).getModifiedby();
        String modifiedtimes = listSupportStageCFundingrequired.get(position).getModifiedtime();
        String multi_deals = listSupportStageCFundingrequired.get(position).getOpportunity_type();
        String potentialNos = listSupportStageCFundingrequired.get(position).getPotentialNo();
        String pndts = listSupportStageCFundingrequired.get(position).getPndt();
        String pre_tax_totals = listSupportStageCFundingrequired.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageCFundingrequired.get(position).getPrice_quoted();
        String productValues = listSupportStageCFundingrequired.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSupportStageCFundingrequired.get(position).getProspect_type();
        String quantitys = listSupportStageCFundingrequired.get(position).getQty();
        String ratings = listSupportStageCFundingrequired.get(position).getRating();
        String s_is = listSupportStageCFundingrequired.get(position).getS_i();
        String sale_types = listSupportStageCFundingrequired.get(position).getSale_type();
        String segments = listSupportStageCFundingrequired.get(position).getSegment();
        String site_reads = listSupportStageCFundingrequired.get(position).getSite_read();
        String support_persons = listSupportStageCFundingrequired.get(position).getSupport_person();
        String support_types = listSupportStageCFundingrequired.get(position).getSupport_type();
        String termsnconditions = listSupportStageCFundingrequired.get(position).getTermsnconditions();
        String totals = listSupportStageCFundingrequired.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageCFundingrequired.get(position).getTxt_adj();
        String winprobValues = listSupportStageCFundingrequired.get(position).getWinprobValue();
        String account_names = listSupportStageCFundingrequired.get(position).getRelated();
        String support_person1 = listSupportStageCFundingrequired.get(position).getSupport_person();
        String services = listSupportStageCFundingrequired.get(position).getTax3();
        String sales = listSupportStageCFundingrequired.get(position).getTax2();
        String vats = listSupportStageCFundingrequired.get(position).getTax1();
        String support_requireds = listSupportStageCFundingrequired.get(position).getTitle();
        String remark_sales_persons = listSupportStageCFundingrequired.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        //  listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        // quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }
    @Override
    public void myItemClick(int position) {

        String sales_stages = listSalesStageOpportunity.get(position).getSalesstageValue();
        String opp_names = listSalesStageOpportunity.get(position).getPotentialValue();
        String assigned = listSalesStageOpportunity.get(position).getAssigned();
        String contact = listSalesStageOpportunity.get(position).getContact();
        String competition_name = listSalesStageOpportunity.get(position).getCompetition_name();
        String circles = listSalesStageOpportunity.get(position).getCircle();
        String closingdates = listSalesStageOpportunity.get(position).getClosingdate();
        String comments = listSalesStageOpportunity.get(position).getComment();
        String createdtimes = listSalesStageOpportunity.get(position).getCreatedtime();
        String demo_dates = listSalesStageOpportunity.get(position).getDemo_date();
        String demo_dones = listSalesStageOpportunity.get(position).getDemo_done();
        String departments = listSalesStageOpportunity.get(position).getDepartment();
        String discount_amounts = listSalesStageOpportunity.get(position).getDiscount_amount();
        String descriptions = listSalesStageOpportunity.get(position).getDescription();
        String employees = listSalesStageOpportunity.get(position).getEmployee();
        String equ_details = listSalesStageOpportunity.get(position).getEqu_details();
        String exp_del_dates = listSalesStageOpportunity.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageOpportunity.get(position).getFun_req();
        String general_remarks = listSalesStageOpportunity.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageOpportunity.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageOpportunity.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageOpportunity.get(position).getInterest_type();
        String leadsources = listSalesStageOpportunity.get(position).getLeadsource();
        String listprices = listSalesStageOpportunity.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSalesStageOpportunity.get(position).getLocation();
        String lost_reasons = listSalesStageOpportunity.get(position).getLost_reason();
        String modalitys = listSalesStageOpportunity.get(position).getModality();
        String modifiedbys = listSalesStageOpportunity.get(position).getModifiedby();
        String modifiedtimes = listSalesStageOpportunity.get(position).getModifiedtime();
        String multi_deals = listSalesStageOpportunity.get(position).getOpportunity_type();
        String potentialNos = listSalesStageOpportunity.get(position).getPotentialNo();
        String pndts = listSalesStageOpportunity.get(position).getPndt();
        String pre_tax_totals = listSalesStageOpportunity.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageOpportunity.get(position).getPrice_quoted();
        String productValues = listSalesStageOpportunity.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSalesStageOpportunity.get(position).getProspect_type();
        String quantitys = listSalesStageOpportunity.get(position).getQty();
        Log.d("quantitys",quantitys.replace(")",""));
        String ratings = listSalesStageOpportunity.get(position).getRating();
        String s_is = listSalesStageOpportunity.get(position).getS_i();
        String sale_types = listSalesStageOpportunity.get(position).getSale_type();
        String segments = listSalesStageOpportunity.get(position).getSegment();
        String site_reads = listSalesStageOpportunity.get(position).getSite_read();
        String support_persons = listSalesStageOpportunity.get(position).getSupport_person();
        String support_types = listSalesStageOpportunity.get(position).getSupport_type();
        String termsnconditions = listSalesStageOpportunity.get(position).getTermsnconditions();
        String totals = listSalesStageOpportunity.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageOpportunity.get(position).getTxt_adj();
        String winprobValues = listSalesStageOpportunity.get(position).getWinprobValue();
        String account_names = listSalesStageOpportunity.get(position).getRelated();
        String support_person1 = listSalesStageOpportunity.get(position).getSupport_person();
        String services = listSalesStageOpportunity.get(position).getTax3();
        String sales = listSalesStageOpportunity.get(position).getTax2();
        String vats = listSalesStageOpportunity.get(position).getTax1();
        String support_requireds = listSalesStageOpportunity.get(position).getTitle();
        String remark_sales_persons = listSalesStageOpportunity.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i]), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }


        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        //  listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        // quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
    }
    @Override
    public void setEmptyList(boolean visibility) {
        textEmptyList.setVisibility(visibility ? View.VISIBLE : View.GONE);
        recyclerNegotiation.setVisibility(visibility ? View.GONE : View.VISIBLE);
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        params.setMargins(2, 0, 0, 2);
        return params;
    }

    private TextView getTextView(int id, String title, int color, int bgColor) {
        TextView tv = new TextView(getContext());
//        String yourText = getString(R.string.your_text);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            tv.setText(Html.fromHtml(yourText, Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            tv.setText(Html.fromHtml(yourText));
//        }
//        try {
//            tv.setMovementMethod(LinkMovementMethod.getInstance());
//        } catch (Exception e) {
////This code seems to crash in some Samsung devices.
////You can handle this edge case base on your needs.
//        }
        tv.setId(id);
        tv.setId(id);
        tv.setText(title.toUpperCase());
        tv.setTextColor(color);
        tv.setGravity(Gravity.CENTER);
        tv.setPadding(10, 10, 10, 10);
        tv.setBackgroundColor(bgColor);
        tv.setLayoutParams(getLayoutParams());
        return tv;
    }



    // < ---- Implementing Search begins here ---- > //
    public void onSearch(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSalesStageOpportunity.size(); i++) {

            final String opp_name = listSalesStageOpportunity.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSalesStageOpportunity.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewOpportunity.setLayoutManager(mLayoutManager);
        opportunityAdapter = new OpportunityAdapter(getContext(),filteredList,this,this,this,this);
        recyclerViewOpportunity.setAdapter(opportunityAdapter);
        opportunityAdapter.notifyDataSetChanged();

    }

    public void onSearchNosupport(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStageNoSupport.size(); i++) {

            final String opp_name = listSupportStageNoSupport.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStageNoSupport.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewNoSupport.setLayoutManager(mLayoutManager);
        nosupportAdapter = new NosupportAdapter(getContext(),filteredList,this,this);
        recyclerViewNoSupport.setAdapter(nosupportAdapter);
        nosupportAdapter.notifyDataSetChanged();

    }
    public void onSearchProduct(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStageProduct.size(); i++) {

            final String opp_name = listSupportStageProduct.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStageProduct.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerPriceValid.setLayoutManager(mLayoutManager);
        productAdapter = new ProductAdapter(getContext(),filteredList,this,this);
        recyclerViewNoSupport.setAdapter(productAdapter);
        productAdapter.notifyDataSetChanged();

    }
    public void onSearchPrice(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStagePrice.size(); i++) {

            final String opp_name = listSupportStagePrice.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStagePrice.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewpricesupport.setLayoutManager(mLayoutManager);
        pricesupportAdapter = new PricesupportAdapter(getContext(),filteredList,this,this);
        recyclerViewpricesupport.setAdapter(pricesupportAdapter);
        pricesupportAdapter.notifyDataSetChanged();

    }

    public void onSearchDemo(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStagePrice.size(); i++) {

            final String opp_name = listSupportStagePrice.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStagePrice.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewpricesupport.setLayoutManager(mLayoutManager);
        pricesupportAdapter = new PricesupportAdapter(getContext(),filteredList,this,this);
        recyclerViewpricesupport.setAdapter(pricesupportAdapter);
        pricesupportAdapter.notifyDataSetChanged();

    }
    public void onSearchTravel(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStageTravelSupport.size(); i++) {

            final String opp_name = listSupportStageTravelSupport.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStageTravelSupport.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerTravelSupport.setLayoutManager(mLayoutManager);
        travelsuppotyAdapter = new TravelsuppotyAdapter(getContext(),filteredList,this,this);
        recyclerTravelSupport.setAdapter(travelsuppotyAdapter);
        travelsuppotyAdapter.notifyDataSetChanged();

    }
    public void onSearchFunding(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSupportStageCFundingrequired.size(); i++) {

            final String opp_name = listSupportStageCFundingrequired.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSupportStageCFundingrequired.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerFundingReq.setLayoutManager(mLayoutManager);
        fundingrequiredAdapter = new FundingrequiredAdapter(getContext(),filteredList,this,this);
        recyclerFundingReq.setAdapter(fundingrequiredAdapter);
        fundingrequiredAdapter.notifyDataSetChanged();

    }

    public void onSearchClosedwon(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSalesStageClosedWon.size(); i++) {

            final String opp_name = listSalesStageClosedWon.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSalesStageClosedWon.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerClosedWon.setLayoutManager(mLayoutManager);
        closedwonAdapter = new ClosedWonAdapter(getContext(),filteredList,this,this);
        recyclerClosedWon.setAdapter(closedwonAdapter);
        closedwonAdapter.notifyDataSetChanged();

    }

    public void onSearchNegotitation(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSalesStageNegotiation.size(); i++) {

            final String opp_name = listSalesStageNegotiation.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSalesStageNegotiation.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerNegotiation.setLayoutManager(mLayoutManager);
        negotiationAdapter = new NegotiationAdapter(getContext(),filteredList,this,this);
        recyclerNegotiation.setAdapter(negotiationAdapter);
        negotiationAdapter.notifyDataSetChanged();

    }

    public void onSearchProposal(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSalesStageProposal.size(); i++) {

            final String opp_name = listSalesStageProposal.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSalesStageProposal.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerProposal.setLayoutManager(mLayoutManager);
        proposalAdapter = new ProposalAdapter(getContext(),filteredList,this,this);
        recyclerProposal.setAdapter(proposalAdapter);
        proposalAdapter.notifyDataSetChanged();

    }
    public void onSearchClosedlost(String query){
        Log.d("Query From Activity",query);
        ArrayList<ModelTest> filteredList = new ArrayList<>();
        //resultView.setVisibility(View.GONE);
        for (int i = 0; i < listSalesStageClosedLost.size(); i++) {

            final String opp_name = listSalesStageClosedLost.get(i).getPotentialValue() .toLowerCase();


            if (opp_name.contains(query)) {
                // make them also bold

                filteredList.add(listSalesStageClosedLost.get(i));
                highlightText(query,opp_name);

            } else {
                //    resultView.setText("No results found for : " + query);
                //  resultView.setVisibility(View.VISIBLE);

            }
        }
        final LinearLayoutManager  mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerClosedLost.setLayoutManager(mLayoutManager);
        closedLostAdapter = new ClosedLostAdapter(getContext(),filteredList,this,this);
        recyclerClosedLost.setAdapter(closedLostAdapter);
        closedLostAdapter.notifyDataSetChanged();

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
                onSearchNosupport(query);
                onSearchProduct(query);
                onSearchPrice(query);
                onSearchDemo(query);
                onSearchTravel(query);
                onSearchFunding(query);
                onSearchProposal(query);
                onSearchNegotitation(query);
                onSearchClosedwon(query);
                onSearchClosedlost(query);
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

    @Override
    public void myproposalItemClick(int position) {


        String sales_stages = listSalesStageProposal.get(position).getSalesstageValue();
        String opp_names = listSalesStageProposal.get(position).getPotentialValue();
        String assigned = listSalesStageProposal.get(position).getAssigned();
        String contact = listSalesStageProposal.get(position).getContact();
        String competition_name = listSalesStageProposal.get(position).getCompetition_name();
        String circles = listSalesStageProposal.get(position).getCircle();
        String closingdates = listSalesStageProposal.get(position).getClosingdate();
        String comments = listSalesStageProposal.get(position).getComment();
        String createdtimes = listSalesStageProposal.get(position).getCreatedtime();
        String demo_dates = listSalesStageProposal.get(position).getDemo_date();
        String demo_dones = listSalesStageProposal.get(position).getDemo_done();
        String departments = listSalesStageProposal.get(position).getDepartment();
        String discount_amounts = listSalesStageProposal.get(position).getDiscount_amount();
        String descriptions = listSalesStageProposal.get(position).getDescription();
        String employees = listSalesStageProposal.get(position).getEmployee();
        String equ_details = listSalesStageProposal.get(position).getEqu_details();
        String exp_del_dates = listSalesStageProposal.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageProposal.get(position).getFun_req();
        String general_remarks = listSalesStageProposal.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageProposal.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageProposal.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageProposal.get(position).getInterest_type();
        String leadsources = listSalesStageProposal.get(position).getLeadsource();
        String listprices = listSalesStageProposal.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSalesStageProposal.get(position).getLocation();
        String lost_reasons = listSalesStageProposal.get(position).getLost_reason();
        String modalitys = listSalesStageProposal.get(position).getModality();
        String modifiedbys = listSalesStageProposal.get(position).getModifiedby();
        String modifiedtimes = listSalesStageProposal.get(position).getModifiedtime();
        String multi_deals = listSalesStageProposal.get(position).getOpportunity_type();
        String potentialNos = listSalesStageProposal.get(position).getPotentialNo();
        String pndts = listSalesStageProposal.get(position).getPndt();
        String pre_tax_totals = listSalesStageProposal.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageProposal.get(position).getPrice_quoted();
        String productValues = listSalesStageProposal.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSalesStageProposal.get(position).getProspect_type();
        String quantitys = listSalesStageProposal.get(position).getQty();
        String ratings = listSalesStageProposal.get(position).getRating();
        String s_is = listSalesStageProposal.get(position).getS_i();
        String sale_types = listSalesStageProposal.get(position).getSale_type();
        String segments = listSalesStageProposal.get(position).getSegment();
        String site_reads = listSalesStageProposal.get(position).getSite_read();
        String support_persons = listSalesStageProposal.get(position).getSupport_person();
        String support_types = listSalesStageProposal.get(position).getSupport_type();
        String termsnconditions = listSalesStageProposal.get(position).getTermsnconditions();
        String totals = listSalesStageProposal.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageProposal.get(position).getTxt_adj();
        String winprobValues = listSalesStageProposal.get(position).getWinprobValue();
        String account_names = listSalesStageProposal.get(position).getRelated();
        String support_person1 = listSalesStageProposal.get(position).getSupport_person();
        String services = listSalesStageProposal.get(position).getTax3();
        String sales = listSalesStageProposal.get(position).getTax2();
        String vats = listSalesStageProposal.get(position).getTax1();
        String support_requireds = listSalesStageProposal.get(position).getTitle();
        String remark_sales_persons = listSalesStageProposal.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());

            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i]), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }


        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        //  listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //   quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

    }


    //Negoatiation view
    @Override
    public void myNegatiationItemClick(int position) {


        String sales_stages = listSalesStageNegotiation.get(position).getSalesstageValue();
        String opp_names = listSalesStageNegotiation.get(position).getPotentialValue();
        String assigned = listSalesStageNegotiation.get(position).getAssigned();
        String contact = listSalesStageNegotiation.get(position).getContact();
        String competition_name = listSalesStageNegotiation.get(position).getCompetition_name();
        String circles = listSalesStageNegotiation.get(position).getCircle();
        String closingdates = listSalesStageNegotiation.get(position).getClosingdate();
        String comments = listSalesStageNegotiation.get(position).getComment();
        String createdtimes = listSalesStageNegotiation.get(position).getCreatedtime();
        String demo_dates = listSalesStageNegotiation.get(position).getDemo_date();
        String demo_dones = listSalesStageNegotiation.get(position).getDemo_done();
        String departments = listSalesStageNegotiation.get(position).getDepartment();
        String discount_amounts = listSalesStageNegotiation.get(position).getDiscount_amount();
        String descriptions = listSalesStageNegotiation.get(position).getDescription();
        String employees = listSalesStageNegotiation.get(position).getEmployee();
        String equ_details = listSalesStageNegotiation.get(position).getEqu_details();
        String exp_del_dates = listSalesStageNegotiation.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageNegotiation.get(position).getFun_req();
        String general_remarks = listSalesStageNegotiation.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageNegotiation.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageNegotiation.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageNegotiation.get(position).getInterest_type();
        String leadsources = listSalesStageNegotiation.get(position).getLeadsource();
        String listprices = listSalesStageNegotiation.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSalesStageNegotiation.get(position).getLocation();
        String lost_reasons = listSalesStageNegotiation.get(position).getLost_reason();
        String modalitys = listSalesStageNegotiation.get(position).getModality();
        String modifiedbys = listSalesStageNegotiation.get(position).getModifiedby();
        String modifiedtimes = listSalesStageNegotiation.get(position).getModifiedtime();
        String multi_deals = listSalesStageNegotiation.get(position).getOpportunity_type();
        String potentialNos = listSalesStageNegotiation.get(position).getPotentialNo();
        String pndts = listSalesStageNegotiation.get(position).getPndt();
        String pre_tax_totals = listSalesStageNegotiation.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageNegotiation.get(position).getPrice_quoted();
        String productValues = listSalesStageNegotiation.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSalesStageNegotiation.get(position).getProspect_type();
        String quantitys = listSalesStageNegotiation.get(position).getQty();
        String ratings = listSalesStageNegotiation.get(position).getRating();
        String s_is = listSalesStageNegotiation.get(position).getS_i();
        String sale_types = listSalesStageNegotiation.get(position).getSale_type();
        String segments = listSalesStageNegotiation.get(position).getSegment();
        String site_reads = listSalesStageNegotiation.get(position).getSite_read();
        String support_persons = listSalesStageNegotiation.get(position).getSupport_person();
        String support_types = listSalesStageNegotiation.get(position).getSupport_type();
        String termsnconditions = listSalesStageNegotiation.get(position).getTermsnconditions();
        String totals = listSalesStageNegotiation.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageNegotiation.get(position).getTxt_adj();
        String winprobValues = listSalesStageNegotiation.get(position).getWinprobValue();
        String account_names = listSalesStageNegotiation.get(position).getRelated();
        String support_person1 = listSalesStageNegotiation.get(position).getSupport_person();
        String services = listSalesStageNegotiation.get(position).getTax3();
        String sales = listSalesStageNegotiation.get(position).getTax2();
        String vats = listSalesStageNegotiation.get(position).getTax1();
        String support_requireds = listSalesStageNegotiation.get(position).getTitle();
        String remark_sales_persons = listSalesStageNegotiation.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i]), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }


        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        //  listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //total = (TextView) customView.findViewById(R.id.total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        // quantity = (TextView) customView.findViewById(R.id.quantity);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

    }


    //Closed Won

    @Override
    public void myWonEditItemClick(int position) {


        isDuplicate = true;

        String sales_stages = listSalesStageClosedLost.get(position).getSalesstageValue();
        String opp_names = listSalesStageClosedLost.get(position).getPotentialValue();
        String assigned = listSalesStageClosedLost.get(position).getAssigned();
        String contact = listSalesStageClosedLost.get(position).getContact();
        String competition_name = listSalesStageClosedLost.get(position).getCompetition_name();
        String circles = listSalesStageClosedLost.get(position).getCircle();
        String closingdates = listSalesStageClosedLost.get(position).getClosingdate();
        String comments = listSalesStageClosedLost.get(position).getComment();
        String createdtimes = listSalesStageClosedLost.get(position).getCreatedtime();
        String demo_dates = listSalesStageClosedLost.get(position).getDemo_date();
        String demo_dones = listSalesStageClosedLost.get(position).getDemo_done();
        String departments = listSalesStageClosedLost.get(position).getDepartment();
        String discount_amounts = listSalesStageClosedLost.get(position).getDiscount_amount();
        String descriptions = listSalesStageClosedLost.get(position).getDescription();
        String employees = listSalesStageClosedLost.get(position).getEmployee();
        String equ_details = listSalesStageClosedLost.get(position).getEqu_details();
        String exp_del_dates = listSalesStageClosedLost.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageClosedLost.get(position).getFun_req();
        String general_remarks = listSalesStageClosedLost.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageClosedLost.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageClosedLost.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageClosedLost.get(position).getInterest_type();
        String leadsources = listSalesStageClosedLost.get(position).getLeadsource();
        String listprices = listSalesStageClosedLost.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSalesStageClosedLost.get(position).getLocation();
        String lost_reasons = listSalesStageClosedLost.get(position).getLost_reason();
        String modalitys = listSalesStageClosedLost.get(position).getModality();
        String modifiedbys = listSalesStageClosedLost.get(position).getModifiedby();
        String modifiedtimes = listSalesStageClosedLost.get(position).getModifiedtime();
        String multi_deals = listSalesStageClosedLost.get(position).getOpportunity_type();
        String potentialNos = listSalesStageClosedLost.get(position).getPotentialNo();
        String pndts = listSalesStageClosedLost.get(position).getPndt();
        String pre_tax_totals = listSalesStageClosedLost.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageClosedLost.get(position).getPrice_quoted();
        String productValues = listSalesStageClosedLost.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSalesStageClosedLost.get(position).getProspect_type();
        String quantitys = listSalesStageClosedLost.get(position).getQty();
        String ratings = listSalesStageClosedLost.get(position).getRating();
        String s_is = listSalesStageClosedLost.get(position).getS_i();
        String sale_types = listSalesStageClosedLost.get(position).getSale_type();
        String segments = listSalesStageClosedLost.get(position).getSegment();
        String site_reads = listSalesStageClosedLost.get(position).getSite_read();
        String support_persons = listSalesStageClosedLost.get(position).getSupport_person();
        String support_types = listSalesStageClosedLost.get(position).getSupport_type();
        String termsnconditions = listSalesStageClosedLost.get(position).getTermsnconditions();
        String totals = listSalesStageClosedLost.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageClosedLost.get(position).getTxt_adj();
        String winprobValues = listSalesStageClosedLost.get(position).getWinprobValue();
        String account_names = listSalesStageClosedLost.get(position).getRelated();
        String support_person1 = listSalesStageClosedLost.get(position).getSupport_person();
        String services = listSalesStageClosedLost.get(position).getTax3();
        String sales = listSalesStageClosedLost.get(position).getTax2();
        String vats = listSalesStageClosedLost.get(position).getTax1();
        String support_requireds = listSalesStageClosedLost.get(position).getTitle();
        String remark_sales_persons = listSalesStageClosedLost.get(position).getRemark_sales_person();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stages", sales_stages);
//        args.putString("facilityType", facilityType);
//        args.putString("ownership_types", ownership_types);
//        args.putString("pin_code", pin_code);
//        args.putString("bill_streets", bill_streets);
//        args.putString("bill_citys", bill_citys);
//        args.putString("bill_districts", bill_districts);
//        args.putString("assigned_tos", assigned_tos);
//        args.putString("email", email);
//        args.putString("mobiles", mobiles);
//        args.putString("bill_states", bill_states);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);


    }


    //Negoatiation view
    @Override
    public void myWonItemClick(final int position) {

        String sales_stages = listSalesStageClosedWon.get(position).getSalesstageValue();
        String opp_names = listSalesStageClosedWon.get(position).getPotentialValue();
        String assigned = listSalesStageClosedWon.get(position).getAssigned();
        String contact = listSalesStageClosedWon.get(position).getContact();
        String competition_name = listSalesStageClosedWon.get(position).getCompetition_name();
        String circles = listSalesStageClosedWon.get(position).getCircle();
        String closingdates = listSalesStageClosedWon.get(position).getClosingdate();
        String comments = listSalesStageClosedWon.get(position).getComment();
        String createdtimes = listSalesStageClosedWon.get(position).getCreatedtime();
        String demo_dates = listSalesStageClosedWon.get(position).getDemo_date();
        String demo_dones = listSalesStageClosedWon.get(position).getDemo_done();
        String departments = listSalesStageClosedWon.get(position).getDepartment();
        String discount_amounts = listSalesStageClosedWon.get(position).getDiscount_amount();
        String descriptions = listSalesStageClosedWon.get(position).getDescription();
        String employees = listSalesStageClosedWon.get(position).getEmployee();
        String equ_details = listSalesStageClosedWon.get(position).getEqu_details();
        String exp_del_dates = listSalesStageClosedWon.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageClosedWon.get(position).getFun_req();
        String general_remarks = listSalesStageClosedWon.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageClosedWon.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageClosedWon.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageClosedWon.get(position).getInterest_type();
        String leadsources = listSalesStageClosedWon.get(position).getLeadsource();
        String listprices = listSalesStageClosedWon.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSalesStageClosedWon.get(position).getLocation();
        String lost_reasons = listSalesStageClosedWon.get(position).getLost_reason();
        String modalitys = listSalesStageClosedWon.get(position).getModality();
        String modifiedbys = listSalesStageClosedWon.get(position).getModifiedby();
        String modifiedtimes = listSalesStageClosedWon.get(position).getModifiedtime();
        String multi_deals = listSalesStageClosedWon.get(position).getOpportunity_type();
        String potentialNos = listSalesStageClosedWon.get(position).getPotentialNo();
        String pndts = listSalesStageClosedWon.get(position).getPndt();
        String pre_tax_totals = listSalesStageClosedWon.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageClosedWon.get(position).getPrice_quoted();
        String productValues = listSalesStageClosedWon.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSalesStageClosedWon.get(position).getProspect_type();
        String quantitys = listSalesStageClosedWon.get(position).getQty();
        String ratings = listSalesStageClosedWon.get(position).getRating();
        String s_is = listSalesStageClosedWon.get(position).getS_i();
        String sale_types = listSalesStageClosedWon.get(position).getSale_type();
        String segments = listSalesStageClosedWon.get(position).getSegment();
        String site_reads = listSalesStageClosedWon.get(position).getSite_read();
        String support_persons = listSalesStageClosedWon.get(position).getSupport_person();
        String support_types = listSalesStageClosedWon.get(position).getSupport_type();
        String termsnconditions = listSalesStageClosedWon.get(position).getTermsnconditions();
        String totals = listSalesStageClosedWon.get(position).getValueAndQty();

        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageClosedWon.get(position).getTxt_adj();
        String winprobValues = listSalesStageClosedWon.get(position).getWinprobValue();
        String account_names = listSalesStageClosedWon.get(position).getRelated();
        String support_person1 = listSalesStageClosedWon.get(position).getSupport_person();
        String services = listSalesStageClosedWon.get(position).getTax3();
        String sales = listSalesStageClosedWon.get(position).getTax2();
        String vats = listSalesStageClosedWon.get(position).getTax1();
        String support_requireds = listSalesStageClosedWon.get(position).getTitle();
        String remark_sales_persons = listSalesStageClosedWon.get(position).getRemark_sales_person();

        opportunitywon_id=listSalesStageClosedWon.get(position).getOpportunity_id();

        //  fetchrecord(position);


        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_closedwon, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk =customView.findViewById(R.id.tables);
        documents =customView.findViewById(R.id.doctables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        //DocModel docModel=closedwonList.get(position);
        //   String doc_id=closedwonList.get(position).getId();
        // Log.d("doc_id",doc_id);
//        String doc_filename=closedwonList.get(position).getFilename();
//        String doc_path=closedwonList.get(position).getPath();
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK,  ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i]), Color.BLACK,ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK,ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }

        sessionId = getActivity().getIntent().getStringExtra("sessionId");
        String operation = "getRelatedDocuments";
        String module = "Documents";
        final String record=opportunitywon_id;
        Log.d("record",record);
        final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);
        /** Call the method with parameter in the interface to get the notice data*/
        Call<DocumentModel> call = service.GetDocumentList(operation, sessionId, module,record);
        /**Log the URL called*/
        Log.i("URL Called", call.request().url() + "");
        call.enqueue(new Callback<DocumentModel>() {
            @Override
            public void onResponse(Call<DocumentModel> call, Response<DocumentModel> response) {
                Log.e("response", new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    Log.e("response", new Gson().toJson(response.body()));
                    DocumentModel documentModel = response.body();
                    // Gson g = new Gson();
                    //String jsonAllDocuments = g.toJson(documentModel);
                    //  tinyDB.putString("jsonAllDocuments", jsonAllDocuments);
                    String success = documentModel.getSuccess();
                    if (success.equals("true")) {
                        ClosedwonResults closedwonResults = documentModel.getResult();
                        PanModel panModel = closedwonResults.getPan();
                        id = panModel.getId();
                        Log.d("docid", id);
                        filename = panModel.getFilename();
                        Log.d("filename", filename);
                        path = panModel.getPath();
                        Log.d("path", path);

                        GSTModel gstModel=closedwonResults.getGst();
                        if(gstId!=null) {
                            gstId = gstModel.getId();
                        }
                        gstFilename=gstModel.getFilename();
                        gstpath=gstModel.getPath();

                        Purchase_orderModel purchase_orderModel=closedwonResults.getPurchase_order();
                        purchaseId=purchase_orderModel.getId();
                        purchaseFilename=purchase_orderModel.getFilename();
                        purchasePath=purchase_orderModel.getPath();

                        AdharModel adharModel=closedwonResults.getAdhar();
                        adharId=adharModel.getId();
                        adharFilename=adharModel.getFilename();
                        adharPath=adharModel.getPath();


                        ChecqueModel checqueModel=closedwonResults.getChecque();
                        checqueId=checqueModel.getId();
                        checqueFilename=checqueModel.getFilename();
                        checquepath=checqueModel.getPath();

                        Doc_oneModel doc_oneModel = closedwonResults.getDoc_one();
                        doc_oneid = doc_oneModel.getId();
                        Log.d("doc_oneid", doc_oneid);
                        doc_onefilename = doc_oneModel.getFilename();
                        Log.d("doc_onefilename", doc_onefilename);
                        doc_onepath = doc_oneModel.getPath();
                        Log.d("doc_onepath", doc_onepath);
                        Doc_twoModel doc_twoModel = closedwonResults.getDoc_two();
                        doc_twoid = doc_twoModel.getId();
                        Log.d("doc_twoid", doc_twoid);
                        doc_twofilename = doc_twoModel.getFilename();
                        Log.d("doc_twofilename", doc_twofilename);
                        doc_twopath = doc_twoModel.getPath();
                        Log.d("doc_twopath", doc_twopath);
                        docModel = new DocModel(filename,adharFilename,gstFilename,purchaseFilename,checqueFilename,doc_onefilename,doc_twofilename,path,adharPath,gstpath,checquepath,purchasePath,doc_onepath,doc_twopath);
                        closedwonList.add(docModel);
                    }
                    String  documentIDS=docModel.getFilename()+","+docModel.getDoc_onefilename()+","+docModel.getDoc_twofilename()+","+
                            docModel.getAdharfilename()+","+docModel.getChecquefilename()+","+docModel.getGstfilename()+","+docModel.getPurchasefilename();
                    String  Paths=docModel.getPath()+","+docModel.getGstpath()+","+docModel.getChecquepath()+","+
                            docModel.getAdharpath()+","+docModel.getPurchasepath()+","+docModel.getDoc_onepath()+","+docModel.getDoctwopath();

                    String[] stringArray2 = {"PAN","AADHAR","GST","PURCHASE ORDER","CHECQUE","dOC ONE","DOC TWO"};
                    TableRow tbrowdoc = new TableRow(getContext());
                    Resources resourcedoc = getContext().getResources();
                    tbrowdoc.setLayoutParams(getLayoutParams());
                    tbrowdoc.addView(getTextView(0, "Title", Color.WHITE, resourcedoc.getColor(R.color.tabs1)));
                    tbrowdoc.addView(getTextView(0, "File Name", Color.WHITE, resourcedoc.getColor(R.color.tabs1)));
                    tbrowdoc.addView(getTextView(0, "View/Download", Color.WHITE,resourcedoc.getColor(R.color.tabs1)));
                    documents.addView(tbrowdoc, getLayoutParams());

                    String[] IDSList = documentIDS.split(",");
                    String[] PathList=Paths.split(",");
                    String url="http://crmtest.genworkshealth.com/".concat(String.valueOf(PathList));

                    int arrayDoc = stringArray2.length;
                    TextView tv=new TextView(getContext());
                    tv.setLayoutParams(getLayoutParams());
                    tv.setText(url);

                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    for (int i = 0; i < arrayDoc; i++) {

                        final TableRow tbrowdocdetails = new TableRow(getContext());
                        tbrowdocdetails.setLayoutParams(getLayoutParams());

                        tbrowdocdetails.addView(getTextView(i + arrayDoc, stringArray2[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
                        tbrowdocdetails.addView(getTextView(i + arrayDoc, IDSList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
                        tbrowdocdetails.addView(getTextView(i + arrayDoc, "View", Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
                        // tbrowdocdetails.setTag(Integer.parseInt("View"),url);
                        tbrowdocdetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getContext(),"clicked",Toast.LENGTH_LONG).show();

                            }
                        });
                        documents.addView(tbrowdocdetails);
                    }
                }
            }
            @Override
            public void onFailure(Call<DocumentModel> call, Throwable t) {
                Log.d("error", t.getMessage());
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        // listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        // total = (TextView) customView.findViewById(R.id.total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //  quantity = (TextView) customView.findViewById(R.id.quantity);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(opportunitywon_id);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);

    }

//    private void fetchrecord(int position) {
//
//        final String recordid=listSalesStageClosedWon.get(position).getOpportunity_id();
//
//       // String apiResponseDocuments = tinyDB.getString("jsonAllDocuments");
//       fetchClosedWon(recordid);
//
//        Log.d("recordid",recordid);
//    }

//    private void fetchClosedWon(final String recordid) {
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sessionId = getActivity().getIntent().getStringExtra("sessionId");
//                String operation = "getRelatedDocuments";
//                String module = "Documents";
//                final String record=recordid;
//                 Log.d("record",record);
//                final GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
//                /** Call the method with parameter in the interface to get the notice data*/
//                Call<DocumentModel> call = service.GetDocumentList(operation, sessionId, module,record);
//                /**Log the URL called*/
//                Log.i("URL Called", call.request().url() + "");
//                call.enqueue(new Callback<DocumentModel>() {
//                    @Override
//                    public void onResponse(Call<DocumentModel> call, Response<DocumentModel> response) {
//                        Log.e("response", new Gson().toJson(response.body()));
//                        if (response.isSuccessful()) {
//                            Log.e("response", new Gson().toJson(response.body()));
//                            DocumentModel documentModel = response.body();
//                           // Gson g = new Gson();
//                            //String jsonAllDocuments = g.toJson(documentModel);
//                          //  tinyDB.putString("jsonAllDocuments", jsonAllDocuments);
//                            String success = documentModel.getSuccess();
//                            if (success.equals("true")) {
//                                ClosedwonResults closedwonResults = documentModel.getResult();
//                                PanModel panModel = closedwonResults.getPan();
//
////            PndtModel pndtModel = closedwonResults.getPndt();
////            String pndtid=pndtModel.getId();
////            String pndtfilename=pndtModel.getFilename();
////            String pndtpath=pndtModel.getPath();
////            Doc_oneModel doc_oneModel=closedwonResults.getDoc_one();
//
//                                id=panModel.getId();
//                                Log.d("docid",id);
//                                filename=panModel.getFilename();
//                                Log.d("filename",filename);
//                                path=panModel.getPath();
//                                Log.d("path",path);
//
//                                //  String id=closedwonList.get(position).getId();
//                                // Log.d("id", String.valueOf(id));
//                            }
//                            DocModel docModel=new DocModel(id,filename,path);
//                            closedwonList.add(docModel);
//                            Log.d("closedwonList", String.valueOf(closedwonList));
//                          //  workingOnResponseDcuments(documentModel);
//                        }
//                        //   progressDialog.dismiss();
//                    }
//                    @Override
//                    public void onFailure(Call<DocumentModel> call, Throwable t) {
//                        Log.d("error", t.getMessage());
//                        //    progressDialog.dismiss();
//                    }
//                });
//            }
//        }, 0);
//        return;
//    }

//    private void workingOnResponseDcuments(DocumentModel documentModel) {
//
//    }


    @Override
    public void myLostItemClick(int position) {

        String sales_stages = listSalesStageClosedLost.get(position).getSalesstageValue();
        String opp_names = listSalesStageClosedLost.get(position).getPotentialValue();
        String assigned = listSalesStageClosedLost.get(position).getAssigned();
        String contact = listSalesStageClosedLost.get(position).getContact();
        String competition_name = listSalesStageClosedLost.get(position).getCompetition_name();
        String circles = listSalesStageClosedLost.get(position).getCircle();
        String closingdates = listSalesStageClosedLost.get(position).getClosingdate();
        String comments = listSalesStageClosedLost.get(position).getComment();
        String createdtimes = listSalesStageClosedLost.get(position).getCreatedtime();
        String demo_dates = listSalesStageClosedLost.get(position).getDemo_date();
        String demo_dones = listSalesStageClosedLost.get(position).getDemo_done();
        String departments = listSalesStageClosedLost.get(position).getDepartment();
        String discount_amounts = listSalesStageClosedLost.get(position).getDiscount_amount();
        String descriptions = listSalesStageClosedLost.get(position).getDescription();
        String employees = listSalesStageClosedLost.get(position).getEmployee();
        String equ_details = listSalesStageClosedLost.get(position).getEqu_details();
        String exp_del_dates = listSalesStageClosedLost.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageClosedLost.get(position).getFun_req();
        String general_remarks = listSalesStageClosedLost.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageClosedLost.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageClosedLost.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageClosedLost.get(position).getInterest_type();
        String leadsources = listSalesStageClosedLost.get(position).getLeadsource();
        String listprices = listSalesStageClosedLost.get(position).getListprice();
        Log.d("listprices", listprices);
//        String list_price=listprices.substring(0,listprices.indexOf("."));
        //      Log.d("list_price",list_price);
        String locations = listSalesStageClosedLost.get(position).getLocation();
        String lost_reasons = listSalesStageClosedLost.get(position).getLost_reason();
        String modalitys = listSalesStageClosedLost.get(position).getModality();
        String modifiedbys = listSalesStageClosedLost.get(position).getModifiedby();
        String modifiedtimes = listSalesStageClosedLost.get(position).getModifiedtime();
        String multi_deals = listSalesStageClosedLost.get(position).getOpportunity_type();
        String potentialNos = listSalesStageClosedLost.get(position).getPotentialNo();
        String pndts = listSalesStageClosedLost.get(position).getPndt();
        String pre_tax_totals = listSalesStageClosedLost.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageClosedLost.get(position).getPrice_quoted();
        String productValues = listSalesStageClosedLost.get(position).getProductValue();
        Log.d("productValues", productValues);
//        String[] namesList = productValues.split(",");
//        ArrayList<String> mylist = new ArrayList<String>();
//        for (String name : namesList) {
//            System.out.println(name);
//            item_list = name;
//        }


        String prospect_types = listSalesStageClosedLost.get(position).getProspect_type();
        String quantitys = listSalesStageClosedLost.get(position).getQty();
        String ratings = listSalesStageClosedLost.get(position).getRating();
        String s_is = listSalesStageClosedLost.get(position).getS_i();
        String sale_types = listSalesStageClosedLost.get(position).getSale_type();
        String segments = listSalesStageClosedLost.get(position).getSegment();
        String site_reads = listSalesStageClosedLost.get(position).getSite_read();
        String support_persons = listSalesStageClosedLost.get(position).getSupport_person();
        String support_types = listSalesStageClosedLost.get(position).getSupport_type();
        String termsnconditions = listSalesStageClosedLost.get(position).getTermsnconditions();
        String totals = listSalesStageClosedLost.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageClosedLost.get(position).getTxt_adj();
        String winprobValues = listSalesStageClosedLost.get(position).getWinprobValue();
        String account_names = listSalesStageClosedLost.get(position).getRelated();
        String support_person1 = listSalesStageClosedLost.get(position).getSupport_person();
        String services = listSalesStageClosedLost.get(position).getTax3();
        String sales = listSalesStageClosedLost.get(position).getTax2();
        String vats = listSalesStageClosedLost.get(position).getTax1();
        String support_requireds = listSalesStageClosedLost.get(position).getTitle();
        String remark_sales_persons = listSalesStageClosedLost.get(position).getRemark_sales_person();

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);


        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i]), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        // listprice = (TextView) customView.findViewById(R.id.listprice);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        //  total = (TextView) customView.findViewById(R.id.total);
        // adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //   quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        demo_date.setText(demo_dates);
        s_i.setText(s_is);
        demo_date.setText(demo_dates);
        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
        support_type.setText(support_types);
        account_name.setText(account_names);
        rating.setText(ratings);
        assigned_to.setText(assigned);
        // adjustment.setText(txtAdjustments);
        circle.setText(circles);
//        listprice.setText(String.valueOf(unitprice));
        demo_done.setText(demo_dones);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        pndt.setText(pndts);
//        total.setText(totals);
        // support_required.setText(support_requireds);
        //pre_tax_total.setText(pre_tax_totals);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        createdtime.setText(createdtimes);
        descrption.setText(descriptions);
        competation_name.setText(competition_name);
        lost_reason.setText(lost_reasons);
        sales_stage.setText(sales_stages);
        contact_name.setText(contact);
        location.setText(locations);
        second_sales_person.setText(employees);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        modality.setText(modalitys);
        circle.setText(circles);
        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_types);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        //  service.setText(services);
        //sale.setText(sales);
        //vat.setText(vats);
        support_person.setText(support_persons);
//        quantity.setText(qty);
        funding_required.setText(fun_requireds);
        intrest_type.setText(intrest_types);
        circle.setText(circles);
        sale_type.setText(sale_types);
        exp_date.setText(exp_del_dates);
//        item_name.setText(item_list);
        //sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);

        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);


    }

    @Override
    public void myLostEditItemClick(int position) {

        isDuplicate = true;

        String sales_stages = listSalesStageClosedLost.get(position).getSalesstageValue();
        String opp_names = listSalesStageClosedLost.get(position).getPotentialValue();
        String assigned = listSalesStageClosedLost.get(position).getAssigned();
        String contact = listSalesStageClosedLost.get(position).getContact();
        String competition_name = listSalesStageClosedLost.get(position).getCompetition_name();
        String circles = listSalesStageClosedLost.get(position).getCircle();
        String closingdates = listSalesStageClosedLost.get(position).getClosingdate();
        String comments = listSalesStageClosedLost.get(position).getComment();
        String createdtimes = listSalesStageClosedLost.get(position).getCreatedtime();
        String demo_dates = listSalesStageClosedLost.get(position).getDemo_date();
        String demo_dones = listSalesStageClosedLost.get(position).getDemo_done();
        String departments = listSalesStageClosedLost.get(position).getDepartment();
        String discount_amounts = listSalesStageClosedLost.get(position).getDiscount_amount();
        String descriptions = listSalesStageClosedLost.get(position).getDescription();
        String employees = listSalesStageClosedLost.get(position).getEmployee();
        String equ_details = listSalesStageClosedLost.get(position).getEqu_details();
        String exp_del_dates = listSalesStageClosedLost.get(position).getExp_delivery_date();
        String fun_requireds = listSalesStageClosedLost.get(position).getFun_req();
        String general_remarks = listSalesStageClosedLost.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSalesStageClosedLost.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSalesStageClosedLost.get(position).getHdnSubTotal();
        String intrest_types = listSalesStageClosedLost.get(position).getInterest_type();
        String leadsources = listSalesStageClosedLost.get(position).getLeadsource();
        String listprices = listSalesStageClosedLost.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSalesStageClosedLost.get(position).getLocation();
        String lost_reasons = listSalesStageClosedLost.get(position).getLost_reason();
        String modalitys = listSalesStageClosedLost.get(position).getModality();
        String modifiedbys = listSalesStageClosedLost.get(position).getModifiedby();
        String modifiedtimes = listSalesStageClosedLost.get(position).getModifiedtime();
        String multi_deals = listSalesStageClosedLost.get(position).getOpportunity_type();
        String potentialNos = listSalesStageClosedLost.get(position).getPotentialNo();
        String pndts = listSalesStageClosedLost.get(position).getPndt();
        String pre_tax_totals = listSalesStageClosedLost.get(position).getPre_tax_total();
        String price_quoteds = listSalesStageClosedLost.get(position).getPrice_quoted();
        String productValues = listSalesStageClosedLost.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSalesStageClosedLost.get(position).getProspect_type();
        String quantitys = listSalesStageClosedLost.get(position).getQty();
        String ratings = listSalesStageClosedLost.get(position).getRating();
        String s_is = listSalesStageClosedLost.get(position).getS_i();
        String sale_types = listSalesStageClosedLost.get(position).getSale_type();
        String segments = listSalesStageClosedLost.get(position).getSegment();
        String site_reads = listSalesStageClosedLost.get(position).getSite_read();
        String support_persons = listSalesStageClosedLost.get(position).getSupport_person();
        String support_types = listSalesStageClosedLost.get(position).getSupport_type();
        String termsnconditions = listSalesStageClosedLost.get(position).getTermsnconditions();
        String totals = listSalesStageClosedLost.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSalesStageClosedLost.get(position).getTxt_adj();
        String winprobValues = listSalesStageClosedLost.get(position).getWinprobValue();
        String account_names = listSalesStageClosedLost.get(position).getRelated();
        String support_person1 = listSalesStageClosedLost.get(position).getSupport_person();
        String services = listSalesStageClosedLost.get(position).getTax3();
        String sales = listSalesStageClosedLost.get(position).getTax2();
        String vats = listSalesStageClosedLost.get(position).getTax1();
        String support_requireds = listSalesStageClosedLost.get(position).getTitle();
        String remark_sales_persons = listSalesStageClosedLost.get(position).getRemark_sales_person();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stages", sales_stages);
//        args.putString("facilityType", facilityType);
//        args.putString("ownership_types", ownership_types);
//        args.putString("pin_code", pin_code);
//        args.putString("bill_streets", bill_streets);
//        args.putString("bill_citys", bill_citys);
//        args.putString("bill_districts", bill_districts);
//        args.putString("assigned_tos", assigned_tos);
//        args.putString("email", email);
//        args.putString("mobiles", mobiles);
//        args.putString("bill_states", bill_states);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);
    }

    @Override
    public void myItemSupportClick(int position) {
        String sales_stages = listSupportStageNoSupport.get(position).getTitle();
        String opp_names = listSupportStageNoSupport.get(position).getPotentialValue();
        String assigned = listSupportStageNoSupport.get(position).getAssigned();
        String contact = listSupportStageNoSupport.get(position).getContact();
        String competition_name = listSupportStageNoSupport.get(position).getCompetition_name();
        String circles = listSupportStageNoSupport.get(position).getCircle();
        String closingdates = listSupportStageNoSupport.get(position).getClosingdate();
        String support_requireds = listSupportStageNoSupport.get(position).getTitle();
        String comments = listSupportStageNoSupport.get(position).getComment();
        String createdtimes = listSupportStageNoSupport.get(position).getCreatedtime();
        String demo_dates = listSupportStageNoSupport.get(position).getDemo_date();
        String demo_dones = listSupportStageNoSupport.get(position).getDemo_done();
        String departments = listSupportStageNoSupport.get(position).getDepartment();
        String discount_amounts = listSupportStageNoSupport.get(position).getDiscount_amount();
        String descriptions = listSupportStageNoSupport.get(position).getDescription();
        String employees = listSupportStageNoSupport.get(position).getEmployee();
        String equ_details = listSupportStageNoSupport.get(position).getEqu_details();
        String exp_del_dates = listSupportStageNoSupport.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageNoSupport.get(position).getFun_req();
        String general_remarks = listSupportStageNoSupport.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageNoSupport.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageNoSupport.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageNoSupport.get(position).getInterest_type();
        String leadsources = listSupportStageNoSupport.get(position).getLeadsource();
        String listprices = listSupportStageNoSupport.get(position).getListprice();
        String locations = listSupportStageNoSupport.get(position).getLocation();
        String lost_reasons = listSupportStageNoSupport.get(position).getLost_reason();
        String modalitys = listSupportStageNoSupport.get(position).getModality();
        String modifiedbys = listSupportStageNoSupport.get(position).getModifiedby();
        String modifiedtimes = listSupportStageNoSupport.get(position).getModifiedtime();
        String multi_deals = listSupportStageNoSupport.get(position).getOpportunity_type();
        String potentialNos = listSupportStageNoSupport.get(position).getPotentialNo();
        String pndts = listSupportStageNoSupport.get(position).getPndt();
        String pre_tax_totals = listSupportStageNoSupport.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageNoSupport.get(position).getPrice_quoted();
        String productValues = listSupportStageNoSupport.get(position).getProductValue();
        String prospect_types = listSupportStageNoSupport.get(position).getProspect_type();
        String quantitys = listSupportStageNoSupport.get(position).getQty();
        String ratings = listSupportStageNoSupport.get(position).getRating();
        String s_is = listSupportStageNoSupport.get(position).getS_i();
        String sale_types = listSupportStageNoSupport.get(position).getSale_type();
        String segments = listSupportStageNoSupport.get(position).getSegment();
        String site_reads = listSupportStageNoSupport.get(position).getSite_read();
        String support_persons = listSupportStageNoSupport.get(position).getSupport_person();
        String support_types = listSupportStageNoSupport.get(position).getSupport_type();
        String termsnconditions = listSupportStageNoSupport.get(position).getTermsnconditions();
        String totals = listSupportStageNoSupport.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageNoSupport.get(position).getTxt_adj();
        String winprobValues = listSupportStageNoSupport.get(position).getWinprobValue();
        String account_names = listSupportStageNoSupport.get(position).getRelated();
        String support_person1 = listSupportStageNoSupport.get(position).getSupport_person();
        String services = listSupportStageNoSupport.get(position).getTax3();
        String sales = listSupportStageNoSupport.get(position).getTax2();
        String vats = listSupportStageNoSupport.get(position).getTax1();
        String remark_sales_persons = listSupportStageNoSupport.get(position).getRemark_sales_person();
        String exp_dates=listSupportStageNoSupport.get(position).getExp_delivery_date();


        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.custom_popup_opportunity, null);

        linearLayout = customView.findViewById(R.id.card_details);
        final PopupWindow popupWindow = new PopupWindow(customView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        TableLayout stk =customView.findViewById(R.id.tables);
        TableRow tbrow0 = new TableRow(getContext());
        Resources resource = getContext().getResources();
        tbrow0.setLayoutParams(getLayoutParams());
        tbrow0.addView(getTextView(0, "Product Name", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Quantity", Color.WHITE,resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Unit Price", Color.WHITE, resource.getColor(R.color.tabs1)));
        tbrow0.addView(getTextView(0, "Total", Color.WHITE, resource.getColor(R.color.tabs1)));
        stk.addView(tbrow0,getLayoutParams());
        String[] namesList = productValues.split(",");
        String[] priceList = listprices.split(",");
        int numCompanies = namesList.length;
        for (int i = 0; i < numCompanies; i++) {
            TableRow tbrow = new TableRow(getContext());
            TextView t0v = new TextView(getContext());
            tbrow.setLayoutParams(getLayoutParams());
            tbrow.addView(getTextView(i + numCompanies, namesList[i], Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            String rupee = getResources().getString(R.string.Rs);
            TextView t2v = new TextView(getContext());
            String qty=quantitys.replace(")","");
            String total=totals.replace("(","");
            tbrow.addView(getTextView(i + numCompanies, qty, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, rupee.concat(priceList[i].replace(")","")), Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            tbrow.addView(getTextView(i + numCompanies, total, Color.BLACK, ContextCompat.getColor(getContext(), R.color.back_blue)));
            stk.addView(tbrow);
        }

        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(
                android.graphics.Color.TRANSPARENT));
        popupWindow.showAtLocation(customView, Gravity.CENTER, 0, 0);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        sales_stage = (TextView) customView.findViewById(R.id.sales_stage);
        opp_name = (TextView) customView.findViewById(R.id.opp_name);
        assigned_to = (TextView) customView.findViewById(R.id.assigned_to);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        contact_name = (TextView) customView.findViewById(R.id.contact_name);
        opp_no = (TextView) customView.findViewById(R.id.opp_no);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        second_sales_person = (TextView) customView.findViewById(R.id.second_sales_person);
        location = (TextView) customView.findViewById(R.id.location);
        lead_source = (TextView) customView.findViewById(R.id.lead_source);
        remark_sales_person = (TextView) customView.findViewById(R.id.remark_sales_person);
        segment = (TextView) customView.findViewById(R.id.segment);
        modality = (TextView) customView.findViewById(R.id.modality);
        circle = (TextView) customView.findViewById(R.id.circle);
        prospect_type = (TextView) customView.findViewById(R.id.prospect_type);
        equip_detail = (TextView) customView.findViewById(R.id.equip_detail);
        sales_type = (TextView) customView.findViewById(R.id.sales_type);
        win_prob = (TextView) customView.findViewById(R.id.win_prob);
        support_required = (TextView) customView.findViewById(R.id.support_required);
        exp_date = (TextView) customView.findViewById(R.id.exp_date);
        exp_del_date = (TextView) customView.findViewById(R.id.exp_del_date);
        pndt = (TextView) customView.findViewById(R.id.pndt);
        demo_done = (TextView) customView.findViewById(R.id.demo_done);
        funding_required = (TextView) customView.findViewById(R.id.funding_required);
        site_read = (TextView) customView.findViewById(R.id.site_read);
        support_person = (TextView) customView.findViewById(R.id.support_person);
        support_type = (TextView) customView.findViewById(R.id.support_type);
        multi_product_deal = (TextView) customView.findViewById(R.id.multi_product_deal);
        department = (TextView) customView.findViewById(R.id.department);
        rating = (TextView) customView.findViewById(R.id.rating);
        intrest_type = (TextView) customView.findViewById(R.id.intrest_type);
        // total = (TextView) customView.findViewById(R.id.total);
        //adjustment = (TextView) customView.findViewById(R.id.adjustment);
        //  charge = (TextView) customView.findViewById(R.id.charge);
        // pre_tax_total = (TextView) customView.findViewById(R.id.pre_tax_total);
        //sub_total = (TextView) customView.findViewById(R.id.sub_total);
        item_name = (TextView) customView.findViewById(R.id.item_name);
        //  quantity = (TextView) customView.findViewById(R.id.quantity);
//                selling_price = (TextView) customView.findViewById(R.id.selling_price);
//                item_comment = (TextView) customView.findViewById(R.id.item_comment);
//                item_dis_amount = (TextView) customView.findViewById(R.id.item_dis_amount);
//                vat = (TextView) customView.findViewById(R.id.vat);
//                sale = (TextView) customView.findViewById(R.id.sale);
//                service = (TextView) customView.findViewById(R.id.service);
        lost_reason = (TextView) customView.findViewById(R.id.lost_reason);
        competation_name = (TextView) customView.findViewById(R.id.competation_name);
        competation_price = (TextView) customView.findViewById(R.id.competation_price);
        lost_mark = (TextView) customView.findViewById(R.id.lost_mark);
        sale_type = (TextView) customView.findViewById(R.id.sales_type);
        descrption = (TextView) customView.findViewById(R.id.descrption);
        createdtime = (TextView) customView.findViewById(R.id.createdtime);
        modifiedtime = (TextView) customView.findViewById(R.id.modifiedtime);
        modifiedby = (TextView) customView.findViewById(R.id.modifiedby);
        terms_condition = (TextView) customView.findViewById(R.id.terms_condition);
        account_name = (TextView) customView.findViewById(R.id.account_name);
        demo_date = (TextView) customView.findViewById(R.id.demo_date);
        s_i = (TextView) customView.findViewById(R.id.s_i);
        //tax1=(TextView) customView.findViewById(R.id.tax);
        //closingdate= (TextView) customView.findViewById(R.id.clo);
        closebtn = (Button) customView.findViewById(R.id.close);
        //email = (Button) customView.findViewById(R.id.email);
        //mobile = (Button) customView.findViewById(R.id.mobile);
        // text_opp.setText("Support Required");
//        demo_date.setText(demo_dates);
        s_i.setText(s_is);
//        demo_date.setText(demo_dates);
//        support_required.setText(support_requireds);
//        support_person.setText(support_persons);
        funding_required.setText(fun_requireds);
        site_read.setText(site_reads);
//        support_type.setText(support_types);
        account_name.setText(account_names);
        assigned_to.setText(assigned);
//      //  adjustment.setText(txtAdjustments);
//        circle.setText(circles);
//        demo_done.setText(demo_dones);
        rating.setText(ratings);
        department.setText(departments);
        terms_condition.setText(terms);
        opp_name.setText(opp_names);
        opp_no.setText(opp_names);
        second_sales_person.setText(employees);
        descrption.setText(descriptions);
        modifiedby.setText(modifiedbys);
        modifiedtime.setText(modifiedtimes);
        contact_name.setText(contact);
        location.setText(locations);
        lead_source.setText(leadsources);
        remark_sales_person.setText(general_remarks);
        segment.setText(segments);
        createdtime.setText(createdtimes);
        modifiedby.setText(modifiedbys);

        prospect_type.setText(prospect_types);
        equip_detail.setText(equ_details);
        sales_type.setText(sale_types);
        win_prob.setText(winprobValues);
        support_required.setText(support_requireds);
        exp_del_date.setText(exp_del_dates);
        equip_detail.setText(equ_details);
        pndt.setText(pndts);
        demo_done.setText(demo_dones);
        circle.setText(circles);
        support_person.setText(support_person1);
        support_person.setText(support_persons);
        exp_date.setText(exp_dates);

        sales_stage.setText(sales_stages);
        equip_detail.setText(equ_details);
        if (winprobValues.equals("30 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("50 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (winprobValues.equals("80 %")) {
            win_prob.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        if (ratings.equals("30 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("50 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange));
        } else if (ratings.equals("80 %")) {
            rating.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.green));
        }

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }


    @Override
    public void myEditSupportItemClick(int position) {
        isDuplicate = true;

        String sales_stages = listSupportStageNoSupport.get(position).getSalesstageValue();
        String opp_names = listSupportStageNoSupport.get(position).getPotentialValue();
        String assigned = listSupportStageNoSupport.get(position).getAssigned();
        String contact = listSupportStageNoSupport.get(position).getContact();
        String competition_name = listSupportStageNoSupport.get(position).getCompetition_name();
        String circles = listSupportStageNoSupport.get(position).getCircle();
        String closingdates = listSupportStageNoSupport.get(position).getClosingdate();
        String comments = listSupportStageNoSupport.get(position).getComment();
        String createdtimes = listSupportStageNoSupport.get(position).getCreatedtime();
        String demo_dates = listSupportStageNoSupport.get(position).getDemo_date();
        String demo_dones = listSupportStageNoSupport.get(position).getDemo_done();
        String departments = listSupportStageNoSupport.get(position).getDepartment();
        String discount_amounts = listSupportStageNoSupport.get(position).getDiscount_amount();
        String descriptions = listSupportStageNoSupport.get(position).getDescription();
        String employees = listSupportStageNoSupport.get(position).getEmployee();
        String equ_details = listSupportStageNoSupport.get(position).getEqu_details();
        String exp_del_dates = listSupportStageNoSupport.get(position).getExp_delivery_date();
        String fun_requireds = listSupportStageNoSupport.get(position).getFun_req();
        String general_remarks = listSupportStageNoSupport.get(position).getGeneral_remark();
        String hdnS_h_amounts = listSupportStageNoSupport.get(position).getHdnS_H_Amount();
        String hdnSubTotals = listSupportStageNoSupport.get(position).getHdnSubTotal();
        String intrest_types = listSupportStageNoSupport.get(position).getInterest_type();
        String leadsources = listSupportStageNoSupport.get(position).getLeadsource();
        String listprices = listSupportStageNoSupport.get(position).getListprice();
//        int unitprice = (int) Double.parseDouble(listprices);
        String locations = listSupportStageNoSupport.get(position).getLocation();
        String lost_reasons = listSupportStageNoSupport.get(position).getLost_reason();
        String modalitys = listSupportStageNoSupport.get(position).getModality();
        String modifiedbys = listSupportStageNoSupport.get(position).getModifiedby();
        String modifiedtimes = listSupportStageNoSupport.get(position).getModifiedtime();
        String multi_deals = listSupportStageNoSupport.get(position).getOpportunity_type();
        String potentialNos = listSupportStageNoSupport.get(position).getPotentialNo();
        String pndts = listSupportStageNoSupport.get(position).getPndt();
        String pre_tax_totals = listSupportStageNoSupport.get(position).getPre_tax_total();
        String price_quoteds = listSupportStageNoSupport.get(position).getPrice_quoted();
        String productValues = listSupportStageNoSupport.get(position).getProductValue();
        ArrayList<String> mylist = new ArrayList<String>();
        for (int i = 0; i < productValues.length(); i++) {

            product_list = String.valueOf(mylist.add(productValues));

        }
        String prospect_types = listSupportStageNoSupport.get(position).getProspect_type();
        String quantitys = listSupportStageNoSupport.get(position).getQty();
        String ratings = listSupportStageNoSupport.get(position).getRating();
        String s_is = listSupportStageNoSupport.get(position).getS_i();
        String sale_types = listSupportStageNoSupport.get(position).getSale_type();
        String segments = listSupportStageNoSupport.get(position).getSegment();
        String site_reads = listSupportStageNoSupport.get(position).getSite_read();
        String support_persons = listSupportStageNoSupport.get(position).getSupport_person();
        String support_types = listSupportStageNoSupport.get(position).getSupport_type();
        String termsnconditions = listSupportStageNoSupport.get(position).getTermsnconditions();
        String totals = listSupportStageNoSupport.get(position).getValueAndQty();
        String terms = "";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            terms = String.valueOf((Html.fromHtml(termsnconditions, Html.FROM_HTML_MODE_LEGACY)));
        } else {
            terms = String.valueOf((Html.fromHtml(termsnconditions)));
        }
        String txtAdjustments = listSupportStageNoSupport.get(position).getTxt_adj();
        String winprobValues = listSupportStageNoSupport.get(position).getWinprobValue();
        String account_names = listSupportStageNoSupport.get(position).getRelated();
        String support_person1 = listSupportStageNoSupport.get(position).getSupport_person();
        String services = listSupportStageNoSupport.get(position).getTax3();
        String sales = listSupportStageNoSupport.get(position).getTax2();
        String vats = listSupportStageNoSupport.get(position).getTax1();
        String support_requireds = listSupportStageNoSupport.get(position).getTitle();
        String remark_sales_persons = listSupportStageNoSupport.get(position).getRemark_sales_person();


        Fragment fragment = new OpportunityCreateFragment();
        Bundle args = new Bundle();
        args.putString("sales_stages", sales_stages);
//        args.putString("facilityType", facilityType);
//        args.putString("ownership_types", ownership_types);
//        args.putString("pin_code", pin_code);
//        args.putString("bill_streets", bill_streets);
//        args.putString("bill_citys", bill_citys);
//        args.putString("bill_districts", bill_districts);
//        args.putString("assigned_tos", assigned_tos);
//        args.putString("email", email);
//        args.putString("mobiles", mobiles);
//        args.putString("bill_states", bill_states);
        args.putString("isDuplicate", String.valueOf(isDuplicate));
        fragment.setArguments(args);
        loadFragment(fragment);
    }


}


