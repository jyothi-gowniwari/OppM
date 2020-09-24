package com.genworks.oppm.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.genworks.oppm.model.ModelTest;
import com.genworks.oppm.model.MyOpportunityModel;
import com.genworks.oppm.model.MyTaskModel;
import com.genworks.oppm.model.PickListValues;
import com.genworks.oppm.model.SynFields;
import com.genworks.oppm.model.Sync;
import com.genworks.oppm.model.SyncBlocks;
import com.genworks.oppm.model.SyncModule;
import com.genworks.oppm.model.SyncResults;
import com.genworks.oppm.model.SyncUpdated;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.MyDecimalValueFormatter;
import com.genworks.oppm.Utils.MyValueFormatter;
import com.genworks.oppm.Utils.PreferenceManagerMyOpportunity;
import com.genworks.oppm.Utils.PreferenceManagerMyTask;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static okhttp3.internal.Util.concat;

public class DashboardFragement extends Fragment {

    private BarChart chart;
    private BarChart chart1;
    private ImageView share;
    LocalDateTime localDateTime = null;
    Month month;
    private String iscurrentmonth_opp;
    private boolean isonetimelogin=false;
    String isModifiedThisMonth;
    private ArrayList<String> monthlist,monthyearlist;
    private float barWidth = 0.9f;
    int endColor1,endColor2,endColor3,endColor4,endColor5;
    private LocalDateTime startweek, endweek, modifiedweek;
    private String modifiedmonth;
    private String modifiedyear,modifiedday,finalmonthyear;
    private ArrayList<MyOpportunityModel> salesstageSupport;
    private LocalDateTime finalmonthyearcurrent;
    private DateFormat inputFormat, outputFormat;
    private int Count_Incomplete, Complted_Count, Count_schedueled;
    private int Count_ColdCall, Count_FollowupMeeting, Count_Courtesy, Count_Demonstration, Count_Technical, Count_Closure, Count_Payment, Count_Execution, Count_Training, Count_Office, Count_Conference;
    private int Count_Followup, Count_OrderClosed, Count_Interested1, Count_Done, Count_Collected, Count_NotInterested;
    private int Count_30, Count_50, Count_80;
    private int Count_opportunity, Count_proposal, Count_negotiation, Count_won, Count_lost;
    private int Count_IVD, Count_WHS, Count_UltraSound, Count_LCS, Count_NBC;
    private String modality_ivd, modality_whs, modality_ultrasound, modality_lcs, modality_nbc;
    private BarChart chart_support, chart_modality;
    private SeekBar seekBarX, seekBarY, seekBarX1, seekBarY1, seekBar_sales1, seekBar_sales2;
    private TextView tvX, tvY, tvX1, tvY1;
    private String firstDayOfQuarter1, lastDayOfQuarter1;
    private PieChart chart2, chart_sales, chart_winprob;
    private SeekBar seekBarX2, seekBarY2;
    private TextView tvX2, tvY2, my_task, opportunity, tvXMaxsales1, tvXMaxsales2;
    public Typeface tfRegular;
    LocalDate modify_date;
    private int Count_NoSupportRequired, Count_Travel, Count_Funding, Count_Demo, Count_Price, Count_Product;
    public Typeface tfLight;
    ArrayList<IBarDataSet> dataSets = null, dataSets1 = null;
    private String sessionId, name;
    private Object values;
    private String[] outList;
    private String outcome, tasktype;
    private String Not_Interested;
    private String OrderClosed;
    private String Done;
    private String Followup;
    private String Interested;
    private String Collected, status_list, In_Complete, Completed, Scheduled;
    private String Cold_Call, Followup_Meeting, Courtesy_Call, Demonstration, Technical_discussion, Closure_Call, Payment_Followup, Execution_documents, Training, OFfice_meeting, conference;
    private XAxis xAxis, xAxis1, xAxis_modality, xAxis_support;
    private String noSupport_required, travel_support, funding_req, demo_required, price_support, product_validation;
    private ScrollView mytask_dashboard, myopportunity_dashboard;
    private LinearLayout mytask_tabs, opportunity_tab, task, opportunitys;
    public final String[] parties = new String[]{"Completed", "In Completed", "Scheduled"};
    public final String[] parties_win = new String[]{"30%", "50%", "80%"};
    public final String[] parties_sales = new String[]{"Opportunity", "Proposal", "Negotation and Review", "Closed Won", "Closed Lost"};
    private final Handler handler = new Handler();
    private String[] count_status;
    private ArrayList<PickListValues> pickListTask = new ArrayList<>();
    private ArrayList<String> taskTypes, outcome_list1, support_required, modality_list, taskTypes1;
    private List<String> status;
    private LocalDate firstDayOfQuarter = null;
    private LocalDateTime firstDayOfQuarteropp = null;
    private LocalDateTime lastDayOfQuarteropp = null;
    LocalDate localModifiedDate;
    private LocalDate lastDayOfQuarter = null;
    private List<String> winprob;
    private ArrayList<String> salesstage_list;
    private ArrayList<String> outcome_list, tasktype_list, supportrequired_list, winprob_list;
    private LinearLayout task_today, task_thisweek, task_currentmonth, task_currentquarter, myopportunity_tabs;
    private TextView today, this_week, current_month, current_quarter;
    private LinearLayout opp_thisweek, opp_currentmonth, opp_currentquarter;
    private TextView this_weekopp, current_monthopp, current_quateropp;
    private static final int PERMISSION_STORAGE = 0;
    private ArrayList<String> modifiedtime_list;
    private ImageView share_status, share_task, share_outcome, share_sales, share_support, share_winprob, share_modality;

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the borderdashboard for this fragment

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Dashboard");
        //   ((MainActivity)getActivity()).tabLayout.setVisibility(View.GONE);
        final View rootView = inflater.inflate(R.layout.activity_barchart, container, false);

        my_task = rootView.findViewById(R.id.my_task);
        opportunity = rootView.findViewById(R.id.my_opportunity);
        task = rootView.findViewById(R.id.task);
        opportunitys = rootView.findViewById(R.id.opportunity);

        tvX = rootView.findViewById(R.id.tvXMax);
        tvY = rootView.findViewById(R.id.tvYMax);
        // share=rootView.findViewById(R.id.share);
        //tabs for textview date wise
        today = rootView.findViewById(R.id.today);
        this_week = rootView.findViewById(R.id.this_week);
        current_month = rootView.findViewById(R.id.current_month);
        current_quarter = rootView.findViewById(R.id.current_quater);
        monthlist=new ArrayList<String>();
        monthyearlist=new ArrayList<String>();
        //tabs for textviews opp
        current_monthopp = rootView.findViewById(R.id.current_monthopp);
        this_weekopp = rootView.findViewById(R.id.this_weekopp);
        current_quateropp = rootView.findViewById(R.id.current_quateropp);

        opp_thisweek = rootView.findViewById(R.id.opp_thisweek);
        opp_currentmonth = rootView.findViewById(R.id.opp_currentmonth);
        opp_currentquarter = rootView.findViewById(R.id.opp_currentquarter);


        task_today = rootView.findViewById(R.id.task_today);
        task_thisweek = rootView.findViewById(R.id.task_thisweek);
        task_currentmonth = rootView.findViewById(R.id.task_currentmonth);
        task_currentquarter = rootView.findViewById(R.id.task_currentquarter);
        salesstageSupport=new ArrayList<>();
        //share for all charts
//        share_status=rootView.findViewById(R.id.share_status);
//        share_task=rootView.findViewById(R.id.share_task);
//        share_outcome=rootView.findViewById(R.id.share_outcome);
//        share_sales=rootView.findViewById(R.id.share_sales);
//        share_support=rootView.findViewById(R.id.share_support);
//        share_modality=rootView.findViewById(R.id.share_modality);
//        share_winprob=rootView.findViewById(R.id.share_winprob);
//        share_modality.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart_modality);
//                }
//            }
//        });
//
//        share_status.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGalleryStatus();
//                } else {
//                    requestStoragePermission(chart2);
//                }
//            }
//        });

        if(!isonetimelogin){
            sessionId = getActivity().getIntent().getStringExtra("session_Id");
        }else {
            sessionId = getActivity().getIntent().getStringExtra("sessionId");
        }
//        share_task.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGalleryTask();
//                } else {
//                    requestStoragePermission(chart);
//                }
//            }
//        });
//        share_outcome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGalleryOutcome();
//                } else {
//                    requestStoragePermission(chart1);
//                }
//            }
//        });
//        share_sales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart_sales);
//                }
//            }
//        });
//        share_winprob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart_winprob);
//                }
//            }
//        });
//        share_support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // takeScreenshot();
//                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    saveToGallery();
//                } else {
//                    requestStoragePermission(chart_support);
//                }
//            }
//        });


        myopportunity_tabs = rootView.findViewById(R.id.myopportunity_tabs);

        taskTypes = new ArrayList<>();
        taskTypes1 = new ArrayList<>();
        status = new ArrayList<String>();
        winprob_list = new ArrayList<String>();
        salesstage_list = new ArrayList<String>();
        outcome_list = new ArrayList<>();
        outcome_list1 = new ArrayList<>();
        supportrequired_list = new ArrayList<>();
        modality_list = new ArrayList<>();
        tasktype_list = new ArrayList<>();
        modifiedtime_list = new ArrayList<>();


        mytask_tabs = rootView.findViewById(R.id.mytask_tabs);
        opportunity_tab = rootView.findViewById(R.id.opportunity_tab);
        mytask_dashboard = rootView.findViewById(R.id.mytask_dashboard);
        myopportunity_dashboard = rootView.findViewById(R.id.myopportunity_dashboard);


        seekBarX = rootView.findViewById(R.id.seekBar1);

        //   seekBarX1 = rootView.findViewById(R.id.seekBar11);
        //   seekBarX1.setOnSeekBarChangeListener(this);


        seekBarY = rootView.findViewById(R.id.seekBar2);

        //  seekBarY1 = rootView.findViewById(R.id.seekBar21);
        // seekBarY1.setOnSeekBarChangeListener(this);

        tvX2 = rootView.findViewById(R.id.tvXMax2);
        tvY2 = rootView.findViewById(R.id.tvYMax2);

        seekBarX2 = rootView.findViewById(R.id.seekBar3);
        seekBarY2 = rootView.findViewById(R.id.seekBar4);
        //   seekBarX2.setOnSeekBarChangeListener(this);
        // seekBarY2.setOnSeekBarChangeListener(this);

        chart = rootView.findViewById(R.id.chart1);
        chart1 = rootView.findViewById(R.id.barChart_task);
        chart2 = rootView.findViewById(R.id.chart2);
        chart_winprob = rootView.findViewById(R.id.chart_winprob);
        chart_sales = rootView.findViewById(R.id.chart_sales);
        chart_support = rootView.findViewById(R.id.chart_support);
        chart_modality = rootView.findViewById(R.id.chart_modality);

//        chart.setNoDataText("No Data available for the Selected Period");
//        chart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        chart.setNoDataTextTypeface(tfLight);
//        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
//        chart.setNoDataTextColor(endColor3);


//        chart1.setNoDataText("No Data available for the Selected Period");
//        chart1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//        chart1.setNoDataTextTypeface(tfLight);
//        chart1.setNoDataTextColor(endColor3);


        chart.getDescription().setEnabled(false);
        chart1.getDescription().setEnabled(false);
        chart_support.getDescription().setEnabled(false);
        chart_modality.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn

        // chart_support.setMaxVisibleValueCount(60);
        // chart_modality.setMaxVisibleValueCount(60);


        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);
        chart1.setDrawBarShadow(false);
        chart1.setDrawGridBackground(false);
        chart_support.setDrawBarShadow(false);
        chart_support.setDrawGridBackground(false);
        chart_modality.setDrawBarShadow(false);
        chart_modality.setDrawGridBackground(false);
        XAxis xAxis2 = chart_support.getXAxis();
        xAxis2.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis2.setDrawGridLines(false);
        chart_support.getAxisLeft().setDrawGridLines(false);

        chart_support.getAxisRight().setDrawGridLines(false);
        chart_support.getAxisLeft().setDrawGridLines(false);
        chart_support.getXAxis().setDrawGridLines(false);

        chart1.getAxisRight().setDrawGridLines(false);
        chart1.getAxisLeft().setDrawGridLines(false);
        chart1.getXAxis().setDrawGridLines(false);

        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getXAxis().setDrawGridLines(false);

        XAxis xAxis3 = chart_modality.getXAxis();
        xAxis3.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis3.setDrawGridLines(false);
        chart_modality.getAxisLeft().setDrawGridLines(false);

        chart_modality.getAxisRight().setDrawGridLines(false);
        chart_modality.getAxisLeft().setDrawGridLines(false);
        chart_modality.getXAxis().setDrawGridLines(false);

        chart1.getLegend().setEnabled(false);
        chart.getLegend().setEnabled(false);
        // chart2.getLegend().setEnabled(false);

        chart_support.animateY(1500);

        chart_support.getLegend().setEnabled(false);
        chart_modality.animateY(1500);

        chart_modality.getLegend().setEnabled(false);


        //chart2
        chart2.setUsePercentValues(true);
        chart2.setDrawSliceText(false);
        chart2.getDescription().setEnabled(false);
        chart2.setExtraOffsets(2, 10, 2, 5);

        chart2.setDragDecelerationFrictionCoef(0.95f);

        chart2.setCenterTextTypeface(tfLight);
        chart2.setCenterText(generateCenterSpannableText());

        chart2.setDrawHoleEnabled(true);
        chart2.setHoleColor(Color.WHITE);

        chart2.setTransparentCircleColor(Color.WHITE);
        chart2.setTransparentCircleAlpha(110);

        chart2.setHoleRadius(58f);
        chart2.setTransparentCircleRadius(61f);

        chart2.setDrawCenterText(true);

        chart2.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart2.setRotationEnabled(true);
        chart2.setHighlightPerTapEnabled(true);


        //  seekBarX2.setProgress(3);
        seekBarX2.setProgress(View.GONE);
        //  seekBarY2.setProgress(10);
        seekBarY2.setProgress(View.GONE);


        chart2.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(18f);
        l.setEnabled(true);

        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        //chart winprob
        chart_winprob.setUsePercentValues(true);
        chart_winprob.getDescription().setEnabled(false);
        chart_winprob.setExtraOffsets(5, 10, 5, 5);
        chart_winprob.setDrawSliceText(false);

        chart_winprob.setDragDecelerationFrictionCoef(0.95f);

        chart_winprob.setCenterTextTypeface(tfLight);
        chart_winprob.setCenterText(generateCenterSpannableText2());

        chart_winprob.setDrawHoleEnabled(true);
        chart_winprob.setHoleColor(Color.WHITE);

        chart_winprob.setTransparentCircleColor(Color.WHITE);
        chart_winprob.setTransparentCircleAlpha(110);

        chart_winprob.setHoleRadius(58f);
        chart_winprob.setTransparentCircleRadius(61f);

        chart_winprob.setDrawCenterText(true);

        chart_winprob.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart_winprob.setRotationEnabled(true);
        chart_winprob.setHighlightPerTapEnabled(true);
        chart_winprob.animateY(1400, Easing.EaseInOutQuad);
        l = chart_winprob.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(18f);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);


        //chart_sales

        chart_sales.setUsePercentValues(true);
        chart_sales.setDrawSliceText(false);
        chart_sales.getDescription().setEnabled(false);
        chart_sales.setExtraOffsets(5f, 0f, 5f, 10f);

        chart_sales.setDragDecelerationFrictionCoef(0.95f);

        chart_sales.setCenterTextTypeface(tfLight);
        chart_sales.setCenterText(generateCenterSpannableText1());

        chart_sales.setDrawHoleEnabled(true);
        chart_sales.setHoleColor(Color.WHITE);

        chart_sales.setTransparentCircleColor(Color.WHITE);
        chart_sales.setTransparentCircleAlpha(110);

        chart_sales.setHoleRadius(54f);
        chart_sales.setTransparentCircleRadius(58f);

        chart_sales.setDrawCenterText(true);

        chart_sales.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart_sales.setRotationEnabled(true);
        chart_sales.setHighlightPerTapEnabled(true);

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        // chart_sales.setOnChartValueSelectedListener(this);
        chart_sales.animateY(1400, Easing.EaseInOutQuad);
        chart_sales.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        chart_sales.drawSliceTextEnabled = false;


        //    final ArrayList<String> weekdays = taskTypes; // Your List / array with String Values For X-axis Labels

// task type
        xAxis1 = chart1.getXAxis();

        chart1.setPinchZoom(false);
        chart1.setDrawGridBackground(false);
        xAxis1.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis1.setTypeface(tfLight);
        xAxis1.setDrawAxisLine(false);
        xAxis1.setDrawGridLines(false);
        chart1.getXAxis().setLabelCount(11);

        chart1.setExtraOffsets(10f, 2f, 0f, 10f);
        //  xAxis1.setLabelRotationAngle(-90f);
        //  chart1.setXAxisRenderer(new CustomXAxisRenderer(chart1.getViewPortHandler(), chart1.getXAxis(), chart1.getTransformer(YAxis.AxisDependency.LEFT)));


        //outcome
        xAxis = chart.getXAxis();
        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tfLight);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        chart.getXAxis().setLabelCount(8);


        chart.setExtraOffsets(10f, 2f, 20f, 10f);
        //xAxis.setLabelRotationAngle(90f);

        //modality

        xAxis_modality = chart_modality.getXAxis();
        chart_modality.setPinchZoom(false);
        chart_modality.setDrawGridBackground(false);
        xAxis_modality.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_modality.setTypeface(tfLight);
        xAxis_modality.setDrawAxisLine(false);
        xAxis_modality.setDrawGridLines(false);
        chart_modality.getXAxis().setLabelCount(8);
        chart_modality.setExtraOffsets(20f, 2f, 20f, 10f);
        // xAxis_modality.setLabelRotationAngle(-45f);

        //support require


        xAxis_support = chart_support.getXAxis();
        chart_support.setPinchZoom(false);
        chart_support.setDrawGridBackground(false);
        xAxis_support.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_support.setTypeface(tfLight);
        xAxis_support.setDrawAxisLine(false);
        xAxis_support.setDrawGridLines(false);
        chart_support.getXAxis().setLabelCount(6);

        chart_support.setExtraOffsets(10f, 2f, 20f, 10f);
        //s  xAxis_support.setLabelRotationAngle(-30f);


        //  xAxis.setValueFormatter(new IndexAxisValueFormatter(getAreaCount()));


        ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = chart.getAxisLeft();
        YAxis leftAxis1 = chart1.getAxisLeft();
        YAxis leftAxis2 = chart_modality.getAxisLeft();
        YAxis leftAxis3 = chart_support.getAxisLeft();
        leftAxis.setTypeface(tfLight);
        leftAxis.setLabelCount(5, true);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis1.setTypeface(tfLight);
        leftAxis1.setLabelCount(5, true);
        leftAxis1.setValueFormatter(custom);
        leftAxis1.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis1.setSpaceTop(15f);

        leftAxis1.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis2.setTypeface(tfLight);
        leftAxis2.setLabelCount(5, false);
        leftAxis2.setValueFormatter(custom);
        leftAxis2.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis2.setSpaceTop(15f);
        leftAxis2.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis3.setTypeface(tfLight);
        leftAxis3.setLabelCount(5, false);
        leftAxis3.setValueFormatter(custom);
        leftAxis3.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis3.setSpaceTop(15f);
        leftAxis3.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setEnabled(false);
        rightAxis.setEnabled(false);

        YAxis rightAxis1 = chart1.getAxisRight();
        rightAxis1.setEnabled(false);
        YAxis rightAxis2 = chart_modality.getAxisRight();
        rightAxis2.setEnabled(false);
        YAxis rightAxis3 = chart_support.getAxisRight();
        rightAxis3.setEnabled(false);

//        rightAxis.setDrawGridLines(false);
//        rightAxis.setTypeface(tfLight);
//        rightAxis.setLabelCount(8, false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l1 = chart_sales.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l1.setOrientation(Legend.LegendOrientation.VERTICAL);
        l1.setTextSize(15f);
        l1.setDrawInside(false);
        l1.setXEntrySpace(7f);
        l1.setYEntrySpace(0f);
        l1.setYOffset(0f);


        Legend lchart = chart1.getLegend();
        lchart.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        lchart.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        lchart.setOrientation(Legend.LegendOrientation.VERTICAL);
        lchart.setTextSize(2f);
        lchart.setDrawInside(false);
        lchart.setXEntrySpace(2f);
        lchart.setYEntrySpace(0f);
        chart1.getLegend().setWordWrapEnabled(true);
        lchart.setYOffset(0f);


        String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok
        if (!apiResponseStr.equals("")) {
            //  chart.clear();
            Gson gson = new Gson();
            SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
            outcome_list.clear();
            status.clear();
            tasktype_list.clear();
            workingOnResponseall(obj);
            chart.notifyDataSetChanged();
            chart.invalidate();
        } else {
            fetchJSON();
        }


        task.setBackgroundColor(Color.rgb(31, 104, 120));

        my_task.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                task.setBackgroundColor(Color.rgb(31, 104, 120));
                my_task.setTextColor(getResources().getColorStateList(R.color.White));
                opportunitys.setBackgroundColor(Color.TRANSPARENT);

                opportunity.setTextColor(getResources().getColorStateList(R.color.tabs));
                myopportunity_tabs.setVisibility(View.GONE);
                mytask_tabs.setVisibility(View.VISIBLE);
                myopportunity_dashboard.setVisibility(View.GONE);
                mytask_dashboard.setVisibility(View.VISIBLE);
                String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {
                    //  chart.clear();
                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    outcome_list.clear();
                    status.clear();
                    tasktype_list.clear();
                    workingOnResponseall(obj);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                } else {
                    fetchJSON();
                }


            }
        });
        opportunity.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                opportunitys.setBackgroundColor(Color.rgb(31, 104, 120));
                opportunity.setTextColor(getResources().getColorStateList(R.color.White));
                task.setBackgroundColor(Color.TRANSPARENT);
                my_task.setTextColor(getResources().getColorStateList(R.color.tabs));
                myopportunity_tabs.setVisibility(View.VISIBLE);
                mytask_tabs.setVisibility(View.GONE);

                myopportunity_dashboard.setVisibility(View.VISIBLE);
                mytask_dashboard.setVisibility(View.GONE);

                Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.opportunity);
                // currentFragment.onOptionsItemSelected(hasOptionsMenu());
                //currentFragment.setSelected(true);

                String apiResponseStr = PreferenceManagerMyOpportunity.getInstance(requireContext()).getAPIResponseDashboardOpportunity(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {
                    //  chart.clear();
                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    salesstage_list.clear();
                    supportrequired_list.clear();
                    winprob_list.clear();
                    modality_list.clear();
                    workingOnResponseweekwiseOpportunity(obj);
                    chart_sales.notifyDataSetChanged();
                    chart_sales.invalidate();
                } else {
                    fetchJSONOpportunity();
                }


            }
        });
        task_today.setBackgroundColor(Color.rgb(31, 104, 120));
        opp_thisweek.setBackgroundColor(Color.rgb(31, 104, 120));
        //today
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task_today.setBackgroundColor(Color.rgb(31, 104, 120));
                today.setTextColor(getResources().getColorStateList(R.color.White));
                task_thisweek.setBackgroundColor(Color.TRANSPARENT);
                this_week.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_currentmonth.setBackgroundColor(Color.TRANSPARENT);
                current_month.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_currentquarter.setBackgroundColor(Color.TRANSPARENT);
                current_quarter.setTextColor(getResources().getColorStateList(R.color.tabs));


                chart.clearFocus();
                chart.clear();
                chart1.clearFocus();
                chart1.clear();
                chart2.clearFocus();
                chart2.clear();

                String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok

                if (!apiResponseStr.equals("")) {
                    //  chart.clear();

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);

                    outcome_list.clear();
                    status.clear();
                    tasktype_list.clear();
                    workingOnResponseall(obj);
                    chart.clear();
                    chart.clearFocus();
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    chart2.notifyDataSetChanged();
                    chart2.invalidate();
                    chart1.notifyDataSetChanged();
                    chart1.invalidate();
                }


            }

        });
        //  this week
        this_week.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {


                task_thisweek.setBackgroundColor(Color.rgb(31, 104, 120));
                task_today.setBackgroundColor(Color.TRANSPARENT);
                today.setTextColor(getResources().getColorStateList(R.color.tabs));
                this_week.setTextColor(getResources().getColorStateList(R.color.White));

                task_currentmonth.setBackgroundColor(Color.TRANSPARENT);
                current_month.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_currentquarter.setBackgroundColor(Color.TRANSPARENT);
                current_quarter.setTextColor(getResources().getColorStateList(R.color.tabs));
                chart.clearFocus();
                chart.clear();
                chart1.clearFocus();
                chart1.clear();
                chart2.clearFocus();
                chart2.clear();

                String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    outcome_list.clear();
                    status.clear();
                    tasktype_list.clear();
                    workingOnResponseweekwise(obj);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    chart2.notifyDataSetChanged();
                    chart2.invalidate();
                    chart1.notifyDataSetChanged();
                    chart1.invalidate();
                }

            }


        });
//        //current month
        current_month.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                task_currentmonth.setBackgroundColor(Color.rgb(31, 104, 120));
                current_month.setTextColor(getResources().getColorStateList(R.color.White));

                task_thisweek.setBackgroundColor(Color.TRANSPARENT);
                this_week.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_today.setBackgroundColor(Color.TRANSPARENT);
                today.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_currentquarter.setBackgroundColor(Color.TRANSPARENT);
                current_quarter.setTextColor(getResources().getColorStateList(R.color.tabs));

                chart.clearFocus();
                chart.clear();
                chart1.clearFocus();
                chart1.clear();
                chart2.clearFocus();
                chart2.clear();

                String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok
                if (!apiResponseStr.equals("")) {

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    outcome_list.clear();
                    status.clear();
                    tasktype_list.clear();
                    workingOnResponsemonthwise(obj);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    chart2.notifyDataSetChanged();
                    chart2.invalidate();
                    chart1.notifyDataSetChanged();
                    chart1.invalidate();
                }

            }

        });
        //current quarter
        current_quarter.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                task_currentquarter.setBackgroundColor(Color.rgb(31, 104, 120));
                current_quarter.setTextColor(getResources().getColorStateList(R.color.White));

                task_thisweek.setBackgroundColor(Color.TRANSPARENT);
                this_week.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_today.setBackgroundColor(Color.TRANSPARENT);
                today.setTextColor(getResources().getColorStateList(R.color.tabs));

                task_currentmonth.setBackgroundColor(Color.TRANSPARENT);
                current_month.setTextColor(getResources().getColorStateList(R.color.tabs));

                chart.clearFocus();
                chart.clear();
                chart1.clearFocus();
                chart1.clear();
                chart2.clearFocus();
                chart2.clear();
                String apiResponseStr = PreferenceManagerMyTask.getInstance(requireContext()).getAPIResponseDashboardTask(); // so you have to press your refresh buttonok

                if (!apiResponseStr.equals("")) {
                    //  chart.clear();

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);

                    outcome_list.clear();
                    status.clear();
                    tasktype_list.clear();
                    workingOnResponsequarterwise(obj);
                    chart.notifyDataSetChanged();
                    chart.invalidate();
                    chart2.notifyDataSetChanged();
                    chart2.invalidate();
                    chart1.notifyDataSetChanged();
                    chart1.invalidate();
                }
            }

        });


        //opp this week
        this_weekopp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                opp_thisweek.setBackgroundColor(Color.rgb(31, 104, 120));
                this_weekopp.setTextColor(getResources().getColorStateList(R.color.White));

                opp_currentmonth.setBackgroundColor(Color.TRANSPARENT);
                current_monthopp.setTextColor(getResources().getColorStateList(R.color.tabs));


                opp_currentquarter.setBackgroundColor(Color.TRANSPARENT);
                current_quateropp.setTextColor(getResources().getColorStateList(R.color.tabs));

                chart_sales.clearFocus();
                chart_sales.clear();
                chart_support.clearFocus();
                chart_support.clear();
                chart_winprob.clearFocus();
                chart_winprob.clear();
                chart_modality.clearFocus();
                chart_modality.clear();

                String apiResponseStr = PreferenceManagerMyOpportunity.getInstance(requireContext()).getAPIResponseDashboardOpportunity(); // so you have to press your refresh buttonok

                if (!apiResponseStr.equals("")) {
                    //  chart.clear();

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);

                    salesstage_list.clear();
                    supportrequired_list.clear();
                    winprob_list.clear();
                    modality_list.clear();
                    workingOnResponseweekwiseOpportunity(obj);
                    chart_sales.notifyDataSetChanged();
                    chart_sales.invalidate();
                    chart_support.notifyDataSetChanged();
                    chart_support.invalidate();
                    chart_winprob.notifyDataSetChanged();
                    chart_winprob.invalidate();
                    chart_modality.notifyDataSetChanged();
                    chart_modality.invalidate();
                }


            }

        });
//        //current month
        current_monthopp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                opp_currentmonth.setBackgroundColor(Color.rgb(31, 104, 120));
                current_monthopp.setTextColor(getResources().getColorStateList(R.color.White));

                opp_thisweek.setBackgroundColor(Color.TRANSPARENT);
                this_weekopp.setTextColor(getResources().getColorStateList(R.color.tabs));
                opp_currentquarter.setBackgroundColor(Color.TRANSPARENT);
                current_quateropp.setTextColor(getResources().getColorStateList(R.color.tabs));

                chart_sales.clearFocus();
                chart_sales.clear();
                chart_support.clearFocus();
                chart_support.clear();
                chart_winprob.clearFocus();
                chart_winprob.clear();
                chart_modality.clearFocus();
                chart_modality.clear();

                String apiResponseStr = PreferenceManagerMyOpportunity.getInstance(requireContext()).getAPIResponseDashboardOpportunity(); // so you have to press your refresh buttonok

                if (!apiResponseStr.equals("")) {
                    //  chart.clear();

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);

                    salesstage_list.clear();
                    supportrequired_list.clear();
                    winprob_list.clear();
                    modality_list.clear();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        workingOnResponseMonthwiseOpportunity(obj);
                    }
                    chart_sales.notifyDataSetChanged();
                    chart_sales.invalidate();
                    chart_winprob.notifyDataSetChanged();
                    chart_winprob.invalidate();
                    chart_modality.notifyDataSetChanged();
                    chart_modality.invalidate();
                }


            }

        });
        //current quarter
        current_quateropp.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                opp_currentquarter.setBackgroundColor(Color.rgb(31, 104, 120));
                current_quateropp.setTextColor(getResources().getColorStateList(R.color.White));

                opp_currentmonth.setBackgroundColor(Color.TRANSPARENT);
                current_monthopp.setTextColor(getResources().getColorStateList(R.color.tabs));


                opp_thisweek.setBackgroundColor(Color.TRANSPARENT);
                this_weekopp.setTextColor(getResources().getColorStateList(R.color.tabs));
                chart_sales.clearFocus();
                chart_sales.clear();
                chart_support.clearFocus();
                chart_support.clear();
                chart_winprob.clearFocus();
                chart_winprob.clear();
                chart_modality.clearFocus();
                chart_modality.clear();

                String apiResponseStr = PreferenceManagerMyOpportunity.getInstance(requireContext()).getAPIResponseDashboardOpportunity(); // so you have to press your refresh buttonok

                if (!apiResponseStr.equals("")) {
                    //  chart.clear();

                    Gson gson = new Gson();
                    SyncModule obj = gson.fromJson(apiResponseStr, SyncModule.class);
                    salesstage_list.clear();
                    supportrequired_list.clear();
                    winprob_list.clear();
                    modality_list.clear();

                    workingOnResponseQuarterwiseOpportunity(obj);
                    chart_sales.notifyDataSetChanged();
                    chart_sales.invalidate();
                    chart_support.notifyDataSetChanged();
                    chart_support.invalidate();
                    chart_winprob.notifyDataSetChanged();
                    chart_winprob.invalidate();
                    chart_modality.notifyDataSetChanged();
                    chart_modality.invalidate();
                }
            }

        });
        return rootView;
    }

    protected void requestStoragePermission(View view) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(view, "Write permission is required to save image to gallery", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
                        }
                    }).show();
        } else {
            Toast.makeText(getContext(), "Permission Required!", Toast.LENGTH_SHORT)
                    .show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }


    private void saveToGallery() {
        if (chart2 != null) {
            saveToGallery(chart, "Chart");
        } else {
            Toast.makeText(getContext(), "Sorry No Chart Avialabel", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToGalleryStatus() {
        if (chart2 != null) {
            saveToGallery(chart2, "Status");
        } else {
            Toast.makeText(getContext(), "Sorry No Chart Avialabel", Toast.LENGTH_LONG).show();
        }
    }

    private void saveToGalleryOutcome() {
        saveToGallery(chart, "Outcome");
    }

    private void saveToGalleryTask() {
        saveToGallery(chart1, "Task Type");
    }

    private void fetchJSON() {//from api call

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
                String operation = "syncModuleRecords";
                String module = "Calendar";
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
                            // here you have to save syncModule object in your shared preferenct.
                            // remember one thing save or store object in sharedpreference is not easy you have to use GSON or any other mechanishm
                            //did you get it?
                            //yes tried and removed .can you do one time i will do it next time

                            outcome_list.clear();
                            tasktype_list.clear();
                            status.clear();
                            Gson g = new Gson();
                            String json1 = g.toJson(syncModule);
                            PreferenceManagerMyTask.getInstance(requireContext()).setAPIResponseDashboardTask(json1);
                            //outcome_list.clear();
                            workingOnResponseall(syncModule);
                            workingOnResponseweekwise(syncModule);
                            workingOnResponsemonthwise(syncModule);
                            workingOnResponsequarterwise(syncModule);

                        }
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

    private void fetchJSONOpportunity() {//from api call

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

               // sessionId = getActivity().getIntent().getStringExtra("sessionId");
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
                            String json1 = g.toJson(syncModule);
                            PreferenceManagerMyOpportunity.getInstance(requireContext()).setAPIResponseDashboardOpportunity(json1);
                            workingOnResponseweekwiseOpportunity(syncModule);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                workingOnResponseMonthwiseOpportunity(syncModule);
                            }
                            workingOnResponseQuarterwiseOpportunity(syncModule);
                        }
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

    @SuppressLint("ResourceAsColor")
    private void workingOnResponseall(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Task Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("date_start")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputFormat.format(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //status
                            else if (name.equals("taskstatus")) {
                                status_list = String.valueOf(values);
                                status.add(status_list);
                                //Outcome
                            } else if (name.equals("cf_1015")) {
                                outcome = String.valueOf(values);
                                outcome_list.add(outcome);
                            }
                            //task type
                            else if (name.equals("cf_956")) {
                                tasktype = String.valueOf(values);
                                tasktype_list.add(tasktype);
                            }
                        }
                        PreferenceManagerMyTask.getInstance(requireContext()).setMultipleDataOutcome(scheduleDate, outcome, status_list, tasktype);
                    }

                    MyTaskModel outcomeModel = new MyTaskModel(scheduleDate, outcome, status_list, tasktype);

                    // outcome_list.add(outcomeModel.getOutcome());
                    // tasktype_list.add(outcomeModel.getTasktype());
                    //status.add(outcomeModel.getStatus());
                    Log.d("status", String.valueOf(status));

                    if (outcomeModel.getDate_start().equals("")) {

                        Count_Incomplete = Collections.frequency(status, "In Complete");
                        Complted_Count = Collections.frequency(status, "Completed");
                        Count_schedueled = Collections.frequency(status, "Scheduled");

                        String[] valueArray = new String[]{"In_Complete", "Completed", "Scheduled"};
                        // Values for pie chart
                        ArrayList<PieEntry> yVals1 = new ArrayList<>();
                        yVals1.add(new PieEntry(Count_Incomplete, valueArray[0 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_Incomplete)))));
                        yVals1.add(new PieEntry(Complted_Count, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Complted_Count)))));
                        yVals1.add(new PieEntry(Count_schedueled, valueArray[2 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_schedueled)))));


                        PieDataSet dataSet_status = new PieDataSet(yVals1, "");
                        dataSet_status.setDrawIcons(false);

                        dataSet_status.setIconsOffset(new MPPointF(0, 40));
                        dataSet_status.setSliceSpace(2f);
                        dataSet_status.setSelectionShift(5f);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                        int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                        dataSet_status.setColors(endColor5, endColor3, endColor4);
                        PieData data1 = new PieData(dataSet_status);
                        data1.setValueFormatter(new PercentFormatter(chart2));
                        data1.setValueTextSize(12f);
                        data1.setValueTextColor(Color.WHITE);
                        data1.setValueTypeface(tfLight);
                        chart2.setData(data1);
                        chart2.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart2.highlightValues(null);


                        Log.d("outcome_list", String.valueOf(outcome_list));


                        if (outcomeModel.getOutcome().equals("Order Closed")) {
                            OrderClosed = outcomeModel.getOutcome();

                            Log.d("Count Order Closed", String.valueOf(Count_OrderClosed));
                        } else if (outcomeModel.getOutcome().equals("Follow-up")) {
                            Followup = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Interested")) {
                            Interested = outcomeModel.getOutcome();
                            Log.d("Interested", Interested);

                            Log.d("Count_Interested1", String.valueOf(Count_Interested1));
                        } else if (outcomeModel.getOutcome().equals("Done")) {
                            Done = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Collected")) {
                            Collected = outcomeModel.getOutcome();
                            Log.d("Collected", Collected);
                            String coll = String.valueOf(outcome.length());

                        } else if (outcomeModel.getOutcome().equals("Not Interested")) {
                            Not_Interested = outcomeModel.getOutcome();

                        }


                        Count_OrderClosed = Collections.frequency(outcome_list, "Order Closed");
                        Count_Followup = Collections.frequency(outcome_list, "Follow-up");
                        Count_Interested1 = Collections.frequency(outcome_list, "Interested");
                        Count_Done = Collections.frequency(outcome_list, "Done");
                        Count_Collected = Collections.frequency(outcome_list, "Collected");
                        Count_NotInterested = Collections.frequency(outcome_list, "Not Interested");


                        final ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add(OrderClosed);
                        arrayList.add(Not_Interested);
                        arrayList.add(Collected);
                        arrayList.add(Done);
                        arrayList.add(Interested);
                        arrayList.add(Followup);
                        Log.d("arraylist", String.valueOf(arrayList));


                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList);
                                String[] outcomeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return outcomeList[(int) value];
                            }
                        });
                        xAxis.setGranularity(1);
                        xAxis.setTextSize(10f);
                        xAxis.setLabelRotationAngle(-90f);
                        xAxis.setLabelCount(outcome_list.size());
                        xAxis.setGranularityEnabled(true);


                        float barWidth = 0.9f;
//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                        ArrayList<BarEntry> values = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {
                        values.add(new BarEntry(0, Count_OrderClosed));
                        values.add(new BarEntry(1, Count_NotInterested));
                        values.add(new BarEntry(2, Count_Collected));
                        values.add(new BarEntry(3, Count_Done));
                        values.add(new BarEntry(4, Count_Interested1));
                        values.add(new BarEntry(5, Count_Followup));
                        //    }


                        BarDataSet set1;
                        if (chart.getData() != null &&
                                chart.getData().getDataSetCount() > 0) {
                            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

                            set1.setValues(values);
                            chart.getData().notifyDataChanged();


                        } else {

                            set1 = new BarDataSet(values, "Outcome");
                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            set1.setColors(endColor1);
                            set1.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(set1);

                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart.setData(data);


                        }


                        if (outcomeModel.getTasktype().equals("Cold Call")) {
                            Cold_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Followup Meeting")) {
                            Followup_Meeting = outcomeModel.getTasktype();//what happend?
                        } else if (outcomeModel.getTasktype().equals("Courtesy Call")) {
                            Courtesy_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Demonstration")) {
                            Demonstration = outcomeModel.getTasktype();//r u understanding what i am doing
                        } else if (outcomeModel.getTasktype().equals("Technical Discussion")) {
                            Technical_discussion = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Closure Call")) {
                            Closure_Call = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Payment Followup")) {
                            Payment_Followup = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Execution DocumentFragements")) {
                            Execution_documents = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Training")) {
                            Training = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Office Meeting")) {
                            OFfice_meeting = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Conference")) {
                            conference = outcomeModel.getTasktype();

                        }


                        Count_ColdCall = Collections.frequency(tasktype_list, "Cold Call");
                        Count_FollowupMeeting = Collections.frequency(tasktype_list, "Followup Meeting");
                        Count_Courtesy = Collections.frequency(tasktype_list, "Courtesy Call");
                        Count_Demonstration = Collections.frequency(tasktype_list, "Demonstration");
                        Count_Technical = Collections.frequency(tasktype_list, "Technical Discussion");
                        Count_Closure = Collections.frequency(tasktype_list, "Closure Call");
                        Count_Payment = Collections.frequency(tasktype_list, "Payment Followup");
                        Count_Execution = Collections.frequency(tasktype_list, "Execution DocumentFragements");
                        Count_Training = Collections.frequency(tasktype_list, "Training");
                        Count_Office = Collections.frequency(tasktype_list, "Office Meeting");
                        Count_Conference = Collections.frequency(tasktype_list, "Conference");

                        final ArrayList<String> task_arrayList = new ArrayList<String>();
                        task_arrayList.add(Cold_Call);
                        task_arrayList.add(Followup_Meeting);
                        task_arrayList.add(Courtesy_Call);
                        task_arrayList.add(Demonstration);
                        task_arrayList.add(Technical_discussion);
                        task_arrayList.add(Closure_Call);//x-axis labels
                        task_arrayList.add(Payment_Followup);
                        task_arrayList.add(Execution_documents);
                        task_arrayList.add(Training);
                        task_arrayList.add(OFfice_meeting);
                        task_arrayList.add(conference);


                        xAxis1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > task_arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(task_arrayList);
                                String[] tasktypeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return tasktypeList[(int) value];
                            }
                        });
                        xAxis1.setGranularity(1);
                        xAxis1.setTextSize(10f);
                        xAxis1.setLabelRotationAngle(-90f);
                        xAxis1.setLabelCount(tasktype_list.size());
                        xAxis1.setGranularityEnabled(true);


                        ArrayList<BarEntry> task_values = new ArrayList<>();

                        task_values.add(new BarEntry(0, Count_ColdCall));
                        task_values.add(new BarEntry(1, Count_FollowupMeeting));
                        task_values.add(new BarEntry(2, Count_Courtesy));
                        task_values.add(new BarEntry(3, Count_Demonstration));
                        task_values.add(new BarEntry(4, Count_Technical));//y-axis count
                        task_values.add(new BarEntry(5, Count_Closure));
                        task_values.add(new BarEntry(6, Count_Payment));
                        task_values.add(new BarEntry(7, Count_Execution));
                        task_values.add(new BarEntry(8, Count_Training));
                        task_values.add(new BarEntry(9, Count_Office));
                        task_values.add(new BarEntry(10, Count_Conference));


                        BarDataSet taskset;
                        if (chart1.getData() != null &&
                                chart1.getData().getDataSetCount() > 0) {
                            taskset = (BarDataSet) chart1.getData().getDataSetByIndex(0);

                            taskset.setValues(task_values);
                            chart1.getData().notifyDataChanged();


                        } else {

                            taskset = new BarDataSet(task_values, "Task Type");


                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            taskset.setColors(endColor1);
                            taskset.setDrawIcons(false);
                            dataSets1 = new ArrayList<>();
                            dataSets1.add(taskset);

                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data2 = new BarData(dataSets1);
                            data2.setValueTextSize(12f);
                            data2.setValueFormatter(formatter);
                            data2.setValueTypeface(tfLight);
                            data2.setBarWidth(0.9f);
                            chart1.setData(data2);


                        }
                    } else {

                        //status
                        chart2.setNoDataText("No Data Available For The Selected Period");
                        chart2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart2.setNoDataTextTypeface(tfLight);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        chart2.setNoDataTextColor(endColor3);

                        chart1.setNoDataText("No Data Available For The Selected Period");
                        chart1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart1.setNoDataTextTypeface(tfLight);
                        chart1.setNoDataTextColor(endColor3);

                        chart.setNoDataText("No Data Available For The Selected Period");
                        chart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart.setNoDataTextTypeface(tfLight);
                        chart.setNoDataTextColor(endColor3);
                    }

                    //status
//                    if (outcomeModel.getStatus().equals("In Complete")) {
//                        In_Complete = outcomeModel.getStatus();
//                    } else if (outcomeModel.getStatus().equals("Completed")) {
//                        Completed = outcomeModel.getStatus();
//                    } else if (outcomeModel.getStatus().equals("Scheduled")) {
//                        Scheduled = outcomeModel.getStatus();
//                    }
//                    if (scheduleDate.equals("11-01-2020")) {

                }

            }

        }
    }

    @SuppressLint("ResourceAsColor")
    private void workingOnResponseweekwise(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Task Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("date_start")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputFormat.format(date);
                                    Log.d("scheduleDate", scheduleDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //status
                            else if (name.equals("taskstatus")) {
                                status_list = String.valueOf(values);
                                status.add(status_list);
                                //Outcome
                            } else if (name.equals("cf_1015")) {
                                outcome = String.valueOf(values);
                                outcome_list.add(outcome);
                            }
                            //task type
                            else if (name.equals("cf_956")) {
                                tasktype = String.valueOf(values);
                                tasktype_list.add(tasktype);
                            }
                        }
                        PreferenceManagerMyTask.getInstance(requireContext()).setMultipleDataOutcome(scheduleDate, outcome, status_list, tasktype);
                    }

                    MyTaskModel outcomeModel = new MyTaskModel(scheduleDate, outcome, status_list, tasktype);

                    // outcome_list.add(outcomeModel.getOutcome());
                    // tasktype_list.add(outcomeModel.getTasktype());
                    //status.add(outcomeModel.getStatus());
                    Log.d("status", String.valueOf(status));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    Date startDate = calendar.getTime();
                    calendar.add(Calendar.DATE, 6);
                    Date endDate = calendar.getTime();

                    Log.d(TAG, "Start Date = " + startDate);
                    Log.d(TAG, "End Date = " + endDate);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;
                    try {
                        date = format.parse(outcomeModel.getDate_start());

                        if (date.before(startDate) && date.after(endDate)) {
                            // if (outcomeModel.getDate_start().equals("12-01-2020")) {
                            Count_Incomplete = Collections.frequency(status, "In Complete");
                            Complted_Count = Collections.frequency(status, "Completed");
                            Count_schedueled = Collections.frequency(status, "Scheduled");


                            String[] valueArray = new String[]{"In_Complete", "Completed", "Scheduled"};
                            // Values for pie chart
                            ArrayList<PieEntry> yVals1 = new ArrayList<>();
                            yVals1.add(new PieEntry(Count_Incomplete, valueArray[0 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_Incomplete)))));
                            yVals1.add(new PieEntry(Complted_Count, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Complted_Count)))));
                            yVals1.add(new PieEntry(Count_schedueled, valueArray[2 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_schedueled)))));


                            PieDataSet dataSet_status = new PieDataSet(yVals1, "");
                            dataSet_status.setDrawIcons(false);

                            dataSet_status.setIconsOffset(new MPPointF(0, 40));
                            dataSet_status.setSliceSpace(2f);
                            dataSet_status.setSelectionShift(5f);
                            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                            int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                            int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                            dataSet_status.setColors(endColor5, endColor3, endColor4);
                            PieData data1 = new PieData(dataSet_status);
                            data1.setValueFormatter(new PercentFormatter(chart2));
                            data1.setValueTextSize(12f);
                            data1.setValueTextColor(Color.WHITE);
                            data1.setValueTypeface(tfLight);
                            chart2.setData(data1);
                            chart2.drawSliceTextEnabled = false;
                            // undo all highlights
                            chart2.highlightValues(null);


                            Log.d("outcome_list", String.valueOf(outcome_list));


                            if (outcomeModel.getOutcome().equals("Order Closed")) {
                                OrderClosed = outcomeModel.getOutcome();

                                Log.d("Count Order Closed", String.valueOf(Count_OrderClosed));
                            } else if (outcomeModel.getOutcome().equals("Follow-up")) {
                                Followup = outcomeModel.getOutcome();

                            } else if (outcomeModel.getOutcome().equals("Interested")) {
                                Interested = outcomeModel.getOutcome();
                                Log.d("Interested", Interested);

                                Log.d("Count_Interested1", String.valueOf(Count_Interested1));
                            } else if (outcomeModel.getOutcome().equals("Done")) {
                                Done = outcomeModel.getOutcome();

                            } else if (outcomeModel.getOutcome().equals("Collected")) {
                                Collected = outcomeModel.getOutcome();
                                Log.d("Collected", Collected);
                                String coll = String.valueOf(outcome.length());

                            } else if (outcomeModel.getOutcome().equals("Not Interested")) {
                                Not_Interested = outcomeModel.getOutcome();

                            }


                            if (outcomeModel.getDate_start().equals("11-01-2020")) {
                                Count_OrderClosed = Collections.frequency(outcome_list, "Order Closed");
                                Count_Followup = Collections.frequency(outcome_list, "Follow-up");
                                Count_Interested1 = Collections.frequency(outcome_list, "Interested");
                                Count_Done = Collections.frequency(outcome_list, "Done");
                                Count_Collected = Collections.frequency(outcome_list, "Collected");
                                Count_NotInterested = Collections.frequency(outcome_list, "Not Interested");
                            }

                            final ArrayList<String> arrayList = new ArrayList<String>();
                            arrayList.add(OrderClosed);
                            arrayList.add(Not_Interested);
                            arrayList.add(Collected);
                            arrayList.add(Done);
                            arrayList.add(Interested);
                            arrayList.add(Followup);
                            Log.d("arraylist", String.valueOf(arrayList));


                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {

                                    if (value < 0 || value > arrayList.size() - 1) {
                                        return "";
                                    }
                                    String valueStr = String.valueOf(arrayList);
                                    String[] outcomeList = valueStr.replace("[", "").replace("]", "").split(",");
                                    return outcomeList[(int) value];
                                }
                            });
                            xAxis.setGranularity(1);
                            xAxis.setTextSize(10f);
                            xAxis.setLabelRotationAngle(-90f);
                            xAxis.setLabelCount(outcome_list.size());
                            xAxis.setGranularityEnabled(true);


                            float barWidth = 0.9f;
//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                            ArrayList<BarEntry> values = new ArrayList<>();
                            //     if (scheduleDate.equals("11-01-2020")) {
                            values.add(new BarEntry(0, Count_OrderClosed));
                            values.add(new BarEntry(1, Count_NotInterested));
                            values.add(new BarEntry(2, Count_Collected));
                            values.add(new BarEntry(3, Count_Done));
                            values.add(new BarEntry(4, Count_Interested1));
                            values.add(new BarEntry(5, Count_Followup));
                            //    }


                            BarDataSet set1;
                            if (chart.getData() != null &&
                                    chart.getData().getDataSetCount() > 0) {
                                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

                                set1.setValues(values);
                                chart.getData().notifyDataChanged();


                            } else {

                                set1 = new BarDataSet(values, "Outcome");


                                int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                                set1.setColors(endColor1);
                                set1.setDrawIcons(false);
                                dataSets = new ArrayList<>();
                                dataSets.add(set1);


                                MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                                BarData data = new BarData(dataSets);
                                data.setValueTextSize(12f);
                                data.setValueFormatter(formatter);
                                data.setValueTypeface(tfLight);
                                data.setBarWidth(barWidth);
                                chart.setData(data);
                            }

                            if (outcomeModel.getTasktype().equals("Cold Call")) {
                                Cold_Call = outcomeModel.getTasktype();
                            } else if (outcomeModel.getTasktype().equals("Followup Meeting")) {
                                Followup_Meeting = outcomeModel.getTasktype();//what happend?
                            } else if (outcomeModel.getTasktype().equals("Courtesy Call")) {
                                Courtesy_Call = outcomeModel.getTasktype();
                            } else if (outcomeModel.getTasktype().equals("Demonstration")) {
                                Demonstration = outcomeModel.getTasktype();
                            } else if (outcomeModel.getTasktype().equals("Technical Discussion")) {
                                Technical_discussion = outcomeModel.getTasktype();
                            } else if (outcomeModel.getTasktype().equals("Closure Call")) {
                                Closure_Call = outcomeModel.getTasktype();

                            } else if (outcomeModel.getTasktype().equals("Payment Followup")) {
                                Payment_Followup = outcomeModel.getTasktype();

                            } else if (outcomeModel.getTasktype().equals("Execution DocumentFragements")) {
                                Execution_documents = outcomeModel.getTasktype();

                            } else if (outcomeModel.getTasktype().equals("Training")) {
                                Training = outcomeModel.getTasktype();

                            } else if (outcomeModel.getTasktype().equals("Office Meeting")) {
                                OFfice_meeting = outcomeModel.getTasktype();

                            } else if (outcomeModel.getTasktype().equals("Conference")) {
                                conference = outcomeModel.getTasktype();

                            }
                            if (outcomeModel.getDate_start().equals("12-01-2020")) {
                                Count_ColdCall = Collections.frequency(tasktype_list, "Cold Call");
                                Count_FollowupMeeting = Collections.frequency(tasktype_list, "Followup Meeting");
                                Count_Courtesy = Collections.frequency(tasktype_list, "Courtesy Call");
                                Count_Demonstration = Collections.frequency(tasktype_list, "Demonstration");
                                Count_Technical = Collections.frequency(tasktype_list, "Technical Discussion");
                                Count_Closure = Collections.frequency(tasktype_list, "Closure Call");
                                Count_Payment = Collections.frequency(tasktype_list, "Payment Followup");
                                Count_Execution = Collections.frequency(tasktype_list, "Execution DocumentFragements");
                                Count_Training = Collections.frequency(tasktype_list, "Training");
                                Count_Office = Collections.frequency(tasktype_list, "Office Meeting");
                                Count_Conference = Collections.frequency(tasktype_list, "Conference");
                            }
                            final ArrayList<String> task_arrayList = new ArrayList<String>();
                            task_arrayList.add(Cold_Call);
                            task_arrayList.add(Followup_Meeting);
                            task_arrayList.add(Courtesy_Call);
                            task_arrayList.add(Demonstration);
                            task_arrayList.add(Technical_discussion);
                            task_arrayList.add(Closure_Call);//x-axis labels
                            task_arrayList.add(Payment_Followup);
                            task_arrayList.add(Execution_documents);
                            task_arrayList.add(Training);
                            task_arrayList.add(OFfice_meeting);
                            task_arrayList.add(conference);


                            xAxis1.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {

                                    if (value < 0 || value > task_arrayList.size() - 1) {
                                        return "";
                                    }
                                    String valueStr = String.valueOf(task_arrayList);
                                    String[] tasktypeList = valueStr.replace("[", "").replace("]", "").split(",");
                                    return tasktypeList[(int) value];
                                }
                            });
                            xAxis1.setGranularity(1);
                            xAxis1.setTextSize(10f);
                            xAxis1.setLabelRotationAngle(-90f);
                            xAxis1.setLabelCount(tasktype_list.size());
                            xAxis1.setGranularityEnabled(true);


                            ArrayList<BarEntry> task_values = new ArrayList<>();

                            task_values.add(new BarEntry(0, Count_ColdCall));
                            task_values.add(new BarEntry(1, Count_FollowupMeeting));
                            task_values.add(new BarEntry(2, Count_Courtesy));
                            task_values.add(new BarEntry(3, Count_Demonstration));
                            task_values.add(new BarEntry(4, Count_Technical));//y-axis count
                            task_values.add(new BarEntry(5, Count_Closure));
                            task_values.add(new BarEntry(6, Count_Payment));
                            task_values.add(new BarEntry(7, Count_Execution));
                            task_values.add(new BarEntry(8, Count_Training));
                            task_values.add(new BarEntry(9, Count_Office));
                            task_values.add(new BarEntry(10, Count_Conference));


                            BarDataSet taskset;
                            if (chart1.getData() != null &&
                                    chart1.getData().getDataSetCount() > 0) {
                                taskset = (BarDataSet) chart1.getData().getDataSetByIndex(0);

                                taskset.setValues(task_values);
                                chart1.getData().notifyDataChanged();


                            } else {

                                taskset = new BarDataSet(task_values, "Task Type");


                                int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                                taskset.setColors(endColor1);
                                taskset.setDrawIcons(false);
                                dataSets1 = new ArrayList<>();
                                dataSets1.add(taskset);

                                MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                                BarData data2 = new BarData(dataSets1);
                                data2.setValueTextSize(12f);
                                data2.setValueFormatter(formatter);
                                data2.setValueTypeface(tfLight);
                                data2.setBarWidth(0.9f);
                                chart1.setData(data2);


                            }
                        } else {

                            //status
                            chart2.setNoDataText("No Data Available For The Selected Period");
                            chart2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            chart2.setNoDataTextTypeface(tfLight);
                            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                            chart2.setNoDataTextColor(endColor3);

                            chart1.setNoDataText("No Data Available For The Selected Period");
                            chart1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            chart1.setNoDataTextTypeface(tfLight);
                            chart1.setNoDataTextColor(endColor3);

                            chart.setNoDataText("No Data Available For The Selected Period");
                            chart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            chart.setNoDataTextTypeface(tfLight);
                            chart.setNoDataTextColor(endColor3);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
    }

    //Weekwise
    @SuppressLint("ResourceAsColor")
    private void workingOnResponsemonthwise(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Task Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("date_start")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                DateFormat outputMonth = new SimpleDateFormat("MM");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputMonth.format(date);
                                    Log.d("scheduleDate", scheduleDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //status
                            else if (name.equals("taskstatus")) {
                                status_list = String.valueOf(values);
                                status.add(status_list);
                                //Outcome
                            } else if (name.equals("cf_1015")) {
                                outcome = String.valueOf(values);
                                outcome_list.add(outcome);
                            }
                            //task type
                            else if (name.equals("cf_956")) {
                                tasktype = String.valueOf(values);
                                tasktype_list.add(tasktype);
                            }
                        }
                        PreferenceManagerMyTask.getInstance(requireContext()).setMultipleDataOutcome(scheduleDate, outcome, status_list, tasktype);
                    }

                    MyTaskModel outcomeModel = new MyTaskModel(scheduleDate, outcome, status_list, tasktype);

                    // outcome_list.add(outcomeModel.getOutcome());
                    // tasktype_list.add(outcomeModel.getTasktype());
                    //status.add(outcomeModel.getStatus());
                    Log.d("status", String.valueOf(status));
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    Date startDate = calendar.getTime();
                    calendar.add(Calendar.DATE, 6);
                    Date endDate = calendar.getTime();

                    Log.d(TAG, "Start Date = " + startDate);
                    Log.d(TAG, "End Date = " + endDate);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    Date date = null;

                    // date = format.parse(outcomeModel.getDate_start());
                    //  if (startDate.before(date) && endDate.after(date)) {
                    // if (outcomeModel.getDate_start().equals("10-01-2020")) {
                    DateFormat dateFormat = new SimpleDateFormat("MM");
                    Date date2 = new Date();
                    Log.d("Month", dateFormat.format(date2));
                    if (outcomeModel.getDate_start().equals(date2)) {
                        Count_Incomplete = Collections.frequency(status, "In Complete");
                        Complted_Count = Collections.frequency(status, "Completed");
                        Count_schedueled = Collections.frequency(status, "Scheduled");


                        String[] valueArray = new String[]{"In_Complete", "Completed", "Scheduled"};
                        // Values for pie chart
                        ArrayList<PieEntry> yVals1 = new ArrayList<>();
                        yVals1.add(new PieEntry(Count_Incomplete, valueArray[0 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_Incomplete)))));
                        yVals1.add(new PieEntry(Complted_Count, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Complted_Count)))));
                        yVals1.add(new PieEntry(Count_schedueled, valueArray[2 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_schedueled)))));


                        PieDataSet dataSet_status = new PieDataSet(yVals1, "");
                        dataSet_status.setDrawIcons(false);

                        dataSet_status.setIconsOffset(new MPPointF(0, 40));
                        dataSet_status.setSliceSpace(2f);
                        dataSet_status.setSelectionShift(5f);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                        int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                        dataSet_status.setColors(endColor5, endColor3, endColor4);
                        PieData data1 = new PieData(dataSet_status);
                        data1.setValueFormatter(new PercentFormatter(chart2));
                        data1.setValueTextSize(12f);
                        data1.setValueTextColor(Color.WHITE);
                        data1.setValueTypeface(tfLight);
                        chart2.setData(data1);
                        chart2.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart2.highlightValues(null);


                        Log.d("outcome_list", String.valueOf(outcome_list));


                        if (outcomeModel.getOutcome().equals("Order Closed")) {
                            OrderClosed = outcomeModel.getOutcome();

                            Log.d("Count Order Closed", String.valueOf(Count_OrderClosed));
                        } else if (outcomeModel.getOutcome().equals("Follow-up")) {
                            Followup = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Interested")) {
                            Interested = outcomeModel.getOutcome();
                            Log.d("Interested", Interested);

                            Log.d("Count_Interested1", String.valueOf(Count_Interested1));
                        } else if (outcomeModel.getOutcome().equals("Done")) {
                            Done = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Collected")) {
                            Collected = outcomeModel.getOutcome();
                            Log.d("Collected", Collected);
                            String coll = String.valueOf(outcome.length());

                        } else if (outcomeModel.getOutcome().equals("Not Interested")) {
                            Not_Interested = outcomeModel.getOutcome();

                        }


                        Count_OrderClosed = Collections.frequency(outcome_list, "Order Closed");
                        Count_Followup = Collections.frequency(outcome_list, "Follow-up");
                        Count_Interested1 = Collections.frequency(outcome_list, "Interested");
                        Count_Done = Collections.frequency(outcome_list, "Done");
                        Count_Collected = Collections.frequency(outcome_list, "Collected");
                        Count_NotInterested = Collections.frequency(outcome_list, "Not Interested");


                        final ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add(OrderClosed);
                        arrayList.add(Not_Interested);
                        arrayList.add(Collected);
                        arrayList.add(Done);
                        arrayList.add(Interested);
                        arrayList.add(Followup);
                        Log.d("arraylist", String.valueOf(arrayList));


                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList);
                                String[] outcomeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return outcomeList[(int) value];
                            }
                        });
                        xAxis.setGranularity(1);
                        xAxis.setTextSize(10f);
                        xAxis.setLabelRotationAngle(-90f);
                        xAxis.setLabelCount(outcome_list.size());
                        xAxis.setGranularityEnabled(true);


                        float barWidth = 0.9f;
//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                        ArrayList<BarEntry> values = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {
                        values.add(new BarEntry(0, Count_OrderClosed));
                        values.add(new BarEntry(1, Count_NotInterested));
                        values.add(new BarEntry(2, Count_Collected));
                        values.add(new BarEntry(3, Count_Done));
                        values.add(new BarEntry(4, Count_Interested1));
                        values.add(new BarEntry(5, Count_Followup));
                        //    }


                        BarDataSet set1;
                        if (chart.getData() != null &&
                                chart.getData().getDataSetCount() > 0) {
                            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

                            set1.setValues(values);
                            chart.getData().notifyDataChanged();


                        } else {

                            set1 = new BarDataSet(values, "Outcome");


                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            set1.setColors(endColor1);
                            set1.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(set1);


                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart.setData(data);
                        }

                        if (outcomeModel.getTasktype().equals("Cold Call")) {
                            Cold_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Followup Meeting")) {
                            Followup_Meeting = outcomeModel.getTasktype();//what happend?
                        } else if (outcomeModel.getTasktype().equals("Courtesy Call")) {
                            Courtesy_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Demonstration")) {
                            Demonstration = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Technical Discussion")) {
                            Technical_discussion = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Closure Call")) {
                            Closure_Call = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Payment Followup")) {
                            Payment_Followup = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Execution DocumentFragements")) {
                            Execution_documents = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Training")) {
                            Training = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Office Meeting")) {
                            OFfice_meeting = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Conference")) {
                            conference = outcomeModel.getTasktype();

                        }

                        Count_ColdCall = Collections.frequency(tasktype_list, "Cold Call");
                        Count_FollowupMeeting = Collections.frequency(tasktype_list, "Followup Meeting");
                        Count_Courtesy = Collections.frequency(tasktype_list, "Courtesy Call");
                        Count_Demonstration = Collections.frequency(tasktype_list, "Demonstration");
                        Count_Technical = Collections.frequency(tasktype_list, "Technical Discussion");
                        Count_Closure = Collections.frequency(tasktype_list, "Closure Call");
                        Count_Payment = Collections.frequency(tasktype_list, "Payment Followup");
                        Count_Execution = Collections.frequency(tasktype_list, "Execution DocumentFragements");
                        Count_Training = Collections.frequency(tasktype_list, "Training");
                        Count_Office = Collections.frequency(tasktype_list, "Office Meeting");
                        Count_Conference = Collections.frequency(tasktype_list, "Conference");

                        final ArrayList<String> task_arrayList = new ArrayList<String>();
                        task_arrayList.add(Cold_Call);
                        task_arrayList.add(Followup_Meeting);
                        task_arrayList.add(Courtesy_Call);
                        task_arrayList.add(Demonstration);
                        task_arrayList.add(Technical_discussion);
                        task_arrayList.add(Closure_Call);//x-axis labels
                        task_arrayList.add(Payment_Followup);
                        task_arrayList.add(Execution_documents);
                        task_arrayList.add(Training);
                        task_arrayList.add(OFfice_meeting);
                        task_arrayList.add(conference);


                        xAxis1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > task_arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(task_arrayList);
                                String[] tasktypeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return tasktypeList[(int) value];
                            }
                        });
                        xAxis1.setGranularity(1);
                        xAxis1.setTextSize(10f);
                        xAxis1.setLabelRotationAngle(-90f);
                        xAxis1.setLabelCount(tasktype_list.size());
                        xAxis1.setGranularityEnabled(true);


                        ArrayList<BarEntry> task_values = new ArrayList<>();

                        task_values.add(new BarEntry(0, Count_ColdCall));
                        task_values.add(new BarEntry(1, Count_FollowupMeeting));
                        task_values.add(new BarEntry(2, Count_Courtesy));
                        task_values.add(new BarEntry(3, Count_Demonstration));
                        task_values.add(new BarEntry(4, Count_Technical));//y-axis count
                        task_values.add(new BarEntry(5, Count_Closure));
                        task_values.add(new BarEntry(6, Count_Payment));
                        task_values.add(new BarEntry(7, Count_Execution));
                        task_values.add(new BarEntry(8, Count_Training));
                        task_values.add(new BarEntry(9, Count_Office));
                        task_values.add(new BarEntry(10, Count_Conference));


                        BarDataSet taskset;
                        if (chart1.getData() != null &&
                                chart1.getData().getDataSetCount() > 0) {
                            taskset = (BarDataSet) chart1.getData().getDataSetByIndex(0);

                            taskset.setValues(task_values);
                            chart1.getData().notifyDataChanged();


                        } else {

                            taskset = new BarDataSet(task_values, "Task Type");


                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            taskset.setColors(endColor1);
                            taskset.setDrawIcons(false);
                            dataSets1 = new ArrayList<>();
                            dataSets1.add(taskset);

                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data2 = new BarData(dataSets1);
                            data2.setValueTextSize(12f);
                            data2.setValueFormatter(formatter);
                            data2.setValueTypeface(tfLight);
                            data2.setBarWidth(0.9f);
                            chart1.setData(data2);


                        }
                    } else {

                        //status
                        chart2.setNoDataText("No Data Available For The Selected Period");
                        chart2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart2.setNoDataTextTypeface(tfLight);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        chart2.setNoDataTextColor(endColor3);

                        chart1.setNoDataText("No Data Available For The Selected Period");
                        chart1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart1.setNoDataTextTypeface(tfLight);
                        chart1.setNoDataTextColor(endColor3);

                        chart.setNoDataText("No Data Available For The Selected Period");
                        chart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart.setNoDataTextTypeface(tfLight);
                        chart.setNoDataTextColor(endColor3);
                    }

                }

            }

        }
    }

    //Weekwise
    @SuppressLint("ResourceAsColor")
    private void workingOnResponsequarterwise(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Task Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("date_start")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");

                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputFormat.format(date);

                                    Log.d("scheduleDates", scheduleDates);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //status
                            else if (name.equals("taskstatus")) {
                                status_list = String.valueOf(values);
                                status.add(status_list);
                                //Outcome
                            } else if (name.equals("cf_1015")) {
                                outcome = String.valueOf(values);
                                outcome_list.add(outcome);
                            }
                            //task type
                            else if (name.equals("cf_956")) {
                                tasktype = String.valueOf(values);
                                tasktype_list.add(tasktype);
                            }
                        }
                        PreferenceManagerMyTask.getInstance(requireContext()).setMultipleDataOutcome(scheduleDates, outcome, status_list, tasktype);
                    }

                    MyTaskModel outcomeModel = new MyTaskModel(scheduleDates, outcome, status_list, tasktype);

                    // outcome_list.add(outcomeModel.getOutcome());
                    // tasktype_list.add(outcomeModel.getTasktype());
                    //status.add(outcomeModel.getStatus());
                    Log.d("status", String.valueOf(status));

                    LocalDate localDate = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        localDate = LocalDate.now();
                        firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
                                .with(TemporalAdjusters.firstDayOfMonth());


                        Log.d("firstDayOfQuarter", String.valueOf(firstDayOfQuarter));
                        lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                                .with(TemporalAdjusters.lastDayOfMonth());
                        Log.d("lastDayOfQuarter", String.valueOf(lastDayOfQuarter));
                    }


                    if (outcomeModel.getDate_start().equals("2020-01-01") && outcomeModel.getDate_start().equals("2020-04-30")) {
                        Count_Incomplete = Collections.frequency(status, "In Complete");
                        Complted_Count = Collections.frequency(status, "Completed");
                        Count_schedueled = Collections.frequency(status, "Scheduled");


                        String[] valueArray = new String[]{"In_Complete", "Completed", "Scheduled"};
                        // Values for pie chart
                        ArrayList<PieEntry> yVals1 = new ArrayList<>();
                        yVals1.add(new PieEntry(Count_Incomplete, valueArray[0 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_Incomplete)))));
                        yVals1.add(new PieEntry(Complted_Count, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Complted_Count)))));
                        yVals1.add(new PieEntry(Count_schedueled, valueArray[2 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_schedueled)))));


                        PieDataSet dataSet_status = new PieDataSet(yVals1, "");
                        dataSet_status.setDrawIcons(false);

                        dataSet_status.setIconsOffset(new MPPointF(0, 40));
                        dataSet_status.setSliceSpace(2f);
                        dataSet_status.setSelectionShift(5f);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                        int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                        dataSet_status.setColors(endColor5, endColor3, endColor4);
                        PieData data1 = new PieData(dataSet_status);
                        data1.setValueFormatter(new PercentFormatter(chart2));
                        data1.setValueTextSize(12f);
                        data1.setValueTextColor(Color.WHITE);
                        data1.setValueTypeface(tfLight);
                        chart2.setData(data1);
                        chart2.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart2.highlightValues(null);


                        Log.d("outcome_list", String.valueOf(outcome_list));


                        if (outcomeModel.getOutcome().equals("Order Closed")) {
                            OrderClosed = outcomeModel.getOutcome();

                            Log.d("Count Order Closed", String.valueOf(Count_OrderClosed));
                        } else if (outcomeModel.getOutcome().equals("Follow-up")) {
                            Followup = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Interested")) {
                            Interested = outcomeModel.getOutcome();
                            Log.d("Interested", Interested);

                            Log.d("Count_Interested1", String.valueOf(Count_Interested1));
                        } else if (outcomeModel.getOutcome().equals("Done")) {
                            Done = outcomeModel.getOutcome();

                        } else if (outcomeModel.getOutcome().equals("Collected")) {
                            Collected = outcomeModel.getOutcome();
                            Log.d("Collected", Collected);
                            String coll = String.valueOf(outcome.length());

                        } else if (outcomeModel.getOutcome().equals("Not Interested")) {
                            Not_Interested = outcomeModel.getOutcome();

                        }


                        Count_OrderClosed = Collections.frequency(outcome_list, "Order Closed");
                        Count_Followup = Collections.frequency(outcome_list, "Follow-up");
                        Count_Interested1 = Collections.frequency(outcome_list, "Interested");
                        Count_Done = Collections.frequency(outcome_list, "Done");
                        Count_Collected = Collections.frequency(outcome_list, "Collected");
                        Count_NotInterested = Collections.frequency(outcome_list, "Not Interested");


                        final ArrayList<String> arrayList = new ArrayList<String>();
                        arrayList.add(OrderClosed);
                        arrayList.add(Not_Interested);
                        arrayList.add(Collected);
                        arrayList.add(Done);
                        arrayList.add(Interested);
                        arrayList.add(Followup);
                        Log.d("arraylist", String.valueOf(arrayList));


                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList);
                                String[] outcomeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return outcomeList[(int) value];
                            }
                        });
                        xAxis.setGranularity(1);
                        xAxis.setTextSize(10f);
                        xAxis.setLabelRotationAngle(-90f);
                        xAxis.setLabelCount(outcome_list.size());
                        xAxis.setGranularityEnabled(true);


                        float barWidth = 0.9f;
//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                        ArrayList<BarEntry> values = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {
                        values.add(new BarEntry(0, Count_OrderClosed));
                        values.add(new BarEntry(1, Count_NotInterested));
                        values.add(new BarEntry(2, Count_Collected));
                        values.add(new BarEntry(3, Count_Done));
                        values.add(new BarEntry(4, Count_Interested1));
                        values.add(new BarEntry(5, Count_Followup));
                        //    }


                        BarDataSet set1;
                        if (chart.getData() != null &&
                                chart.getData().getDataSetCount() > 0) {
                            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);

                            set1.setValues(values);
                            chart.getData().notifyDataChanged();


                        } else {

                            set1 = new BarDataSet(values, "Outcome");


                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            set1.setColors(endColor1);
                            set1.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(set1);


                            MyDecimalValueFormatter formatter1 = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter1);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart.setData(data);
                        }

                        if (outcomeModel.getTasktype().equals("Cold Call")) {
                            Cold_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Followup Meeting")) {
                            Followup_Meeting = outcomeModel.getTasktype();//what happend?
                        } else if (outcomeModel.getTasktype().equals("Courtesy Call")) {
                            Courtesy_Call = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Demonstration")) {
                            Demonstration = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Technical Discussion")) {
                            Technical_discussion = outcomeModel.getTasktype();
                        } else if (outcomeModel.getTasktype().equals("Closure Call")) {
                            Closure_Call = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Payment Followup")) {
                            Payment_Followup = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Execution DocumentFragements")) {
                            Execution_documents = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Training")) {
                            Training = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Office Meeting")) {
                            OFfice_meeting = outcomeModel.getTasktype();

                        } else if (outcomeModel.getTasktype().equals("Conference")) {
                            conference = outcomeModel.getTasktype();

                        }

                        Count_ColdCall = Collections.frequency(tasktype_list, "Cold Call");
                        Count_FollowupMeeting = Collections.frequency(tasktype_list, "Followup Meeting");
                        Count_Courtesy = Collections.frequency(tasktype_list, "Courtesy Call");
                        Count_Demonstration = Collections.frequency(tasktype_list, "Demonstration");
                        Count_Technical = Collections.frequency(tasktype_list, "Technical Discussion");
                        Count_Closure = Collections.frequency(tasktype_list, "Closure Call");
                        Count_Payment = Collections.frequency(tasktype_list, "Payment Followup");
                        Count_Execution = Collections.frequency(tasktype_list, "Execution DocumentFragements");
                        Count_Training = Collections.frequency(tasktype_list, "Training");
                        Count_Office = Collections.frequency(tasktype_list, "Office Meeting");
                        Count_Conference = Collections.frequency(tasktype_list, "Conference");

                        final ArrayList<String> task_arrayList = new ArrayList<String>();
                        task_arrayList.add(Cold_Call);
                        task_arrayList.add(Followup_Meeting);
                        task_arrayList.add(Courtesy_Call);
                        task_arrayList.add(Demonstration);
                        task_arrayList.add(Technical_discussion);
                        task_arrayList.add(Closure_Call);//x-axis labels
                        task_arrayList.add(Payment_Followup);
                        task_arrayList.add(Execution_documents);
                        task_arrayList.add(Training);
                        task_arrayList.add(OFfice_meeting);
                        task_arrayList.add(conference);


                        xAxis1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > task_arrayList.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(task_arrayList);
                                String[] tasktypeList = valueStr.replace("[", "").replace("]", "").split(",");
                                return tasktypeList[(int) value];
                            }
                        });
                        xAxis1.setGranularity(1);
                        xAxis1.setTextSize(10f);
                        xAxis1.setLabelRotationAngle(-90f);
                        xAxis1.setLabelCount(tasktype_list.size());
                        xAxis1.setGranularityEnabled(true);


                        ArrayList<BarEntry> task_values = new ArrayList<>();

                        task_values.add(new BarEntry(0, Count_ColdCall));
                        task_values.add(new BarEntry(1, Count_FollowupMeeting));
                        task_values.add(new BarEntry(2, Count_Courtesy));
                        task_values.add(new BarEntry(3, Count_Demonstration));
                        task_values.add(new BarEntry(4, Count_Technical));//y-axis count
                        task_values.add(new BarEntry(5, Count_Closure));
                        task_values.add(new BarEntry(6, Count_Payment));
                        task_values.add(new BarEntry(7, Count_Execution));
                        task_values.add(new BarEntry(8, Count_Training));
                        task_values.add(new BarEntry(9, Count_Office));
                        task_values.add(new BarEntry(10, Count_Conference));


                        BarDataSet taskset;
                        if (chart1.getData() != null &&
                                chart1.getData().getDataSetCount() > 0) {
                            taskset = (BarDataSet) chart1.getData().getDataSetByIndex(0);

                            taskset.setValues(task_values);
                            chart1.getData().notifyDataChanged();


                        } else {

                            taskset = new BarDataSet(task_values, "Task Type");


                            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            taskset.setColors(endColor1);
                            taskset.setDrawIcons(false);
                            dataSets1 = new ArrayList<>();
                            dataSets1.add(taskset);

                            MyDecimalValueFormatter formatter2 = new MyDecimalValueFormatter();
                            BarData data2 = new BarData(dataSets1);
                            data2.setValueTextSize(12f);
                            data2.setValueFormatter(formatter2);
                            data2.setValueTypeface(tfLight);
                            data2.setBarWidth(0.9f);
                            chart1.setData(data2);


                        }

                    } else {

                        //status
                        chart2.setNoDataText("No Data Available For The Selected Period");
                        chart2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart2.setNoDataTextTypeface(tfLight);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        chart2.setNoDataTextColor(endColor3);

                        chart1.setNoDataText("No Data Available For The Selected Period");
                        chart1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart1.setNoDataTextTypeface(tfLight);
                        chart1.setNoDataTextColor(endColor3);

                        chart.setNoDataText("No Data Available For The Selected Period");
                        chart.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart.setNoDataTextTypeface(tfLight);
                        chart.setNoDataTextColor(endColor3);

                    }
                }

            }

        }
    }

    //    private void fetchWinProb() {//from api call
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Write code for your refresh logic
//
//
//
//                sessionId = getActivity().getIntent().getStringExtra("sessionId");
//                String operation = "syncModuleRecords";
//                String module = "Potentials";
//                String syncToken = "";
//                String mode = "public";
//                final GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
//
//                /** Call the method with parameter in the interface to get the notice data*/
//                Call<SyncModule> call = service.GetSyncModuleList(operation, sessionId, module, syncToken, mode);
//
//                /**Log the URL called*/
//                Log.i("URL Called", call.request().url() + "");
//
//                call.enqueue(new Callback<SyncModule>() {
//                    @Override
//                    public void onResponse(Call<SyncModule> call, Response<SyncModule> response) {
//
//
//                        Log.e("response", new Gson().toJson(response.body()));
//                        if (response.isSuccessful()) {
//                            Log.e("response", new Gson().toJson(response.body()));
//
//                            SyncModule syncModule = response.body();
//                            // here you have to save syncModule object in your shared preferenct.
//                            // remember one thing save or store object in sharedpreference is not easy you have to use GSON or any other mechanishm
//                            //did you get it?
//                            //yes tried and removed .can you do one time i will do it next time
//
//
//                            workingOnResponsewinprob(syncModule);
//
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SyncModule> call, Throwable t) {
//                        Log.d("error", t.getMessage());
//                        // progressDialog.dismiss();
//                    }
//
//
//                });
//
//            }
//
//
//        }, 0);
//        return;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void workingOnResponsewinprob(SyncModule syncModule){
//        //isdate=true;
//        String success = syncModule.getSuccess();
//
//        if (success.equals("true")) {
//            SyncResults results = syncModule.getResult();
//
//            Sync sync = results.getSync();
//
//            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();
//
//            for (SyncUpdated syncUpdated : syncUpdateds) {
//
//                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
//
//
//                String winprob_list = "";
//
//
//                for (SyncBlocks syncBlocks1 : syncBlocks) {
//
//                    String label = syncBlocks1.getLabel();
//                    if (label.equals("Opportunity Details")) {
//                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
//
//                        for (SynFields synFields1 : synFields) {
//                            name = synFields1.getName();
//                            values = synFields1.getValue();
//                            if (name.equals("cf_897")) {
//                                winprob_list = String.valueOf(values);
//                                //  Log.d("status_list",status_list);
//                                winprob.add(winprob_list);
//
//                                Count_30 = Collections.frequency(winprob, "30 %");
//                               // System.out.println("Count of 30% is:  "+ Count_30);
//                                Count_50 = Collections.frequency(winprob, "50 %");
//                                //System.out.println("Count of In Complete is:  "+ Count_Incomplete);
//
//                                Count_80 = Collections.frequency(winprob, "80 %");
//                                // System.out.println("Count of Completed is:  "+ Complted_Count);
//
//                                String[] valueArray = new String[] { "30%", "50%", "80%" };
//                                // Values for pie chart
//                                ArrayList<PieEntry> yVals1 = new ArrayList<>();
//                                yVals1.add(new PieEntry(Count_30, valueArray[0 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_30)))));
//                                //System.out.println("Count_Incomplete:  "+ Count_Incomplete);
//                                yVals1.add(new PieEntry(Count_50, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_50)))));
//                                yVals1.add(new PieEntry(Count_80, valueArray[2 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_80)))));
//
//                                PieDataSet dataSet = new PieDataSet(yVals1, "");
//                                dataSet.setDrawIcons(false);
//
//                                dataSet.setIconsOffset(new MPPointF(0, 40));
//                                dataSet.setSliceSpace(2f);
//                                dataSet.setSelectionShift(5f);
//                                int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
//                                int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
//                                int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
//
//
//                                dataSet.setColors(endColor4,endColor5,endColor3);
//
//
//                                PieData data = new PieData(dataSet);
//                                data.setValueFormatter(new PercentFormatter(chart_winprob));
//                                data.setValueTextSize(12f);
//                                data.setValueTextColor(Color.WHITE);
//                                data.setValueTypeface(tfLight);
//
//                                chart_winprob.setData(data);
//                                chart_winprob.drawSliceTextEnabled=false;
//
//                                // undo all highlights
//                                chart_winprob.highlightValues(null);
//
//                                chart_winprob.invalidate();
//
//
//                            }
//
//
//                        }
//                    }
//                }
//
//
//            }
//
//
//
//        }
//    }
//
//    private void fetchSalesStage() {//from api call
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Write code for your refresh logic
//
//                sessionId = getActivity().getIntent().getStringExtra("sessionId");
//                String operation = "syncModuleRecords";
//                String module = "Potentials";
//                String syncToken = "";
//                String mode = "public";
//                final GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
//
//                /** Call the method with parameter in the interface to get the notice data*/
//                Call<SyncModule> call = service.GetSyncModuleList(operation, sessionId, module, syncToken, mode);
//
//                /**Log the URL called*/
//                Log.i("URL Called", call.request().url() + "");
//
//                call.enqueue(new Callback<SyncModule>() {
//                    @Override
//                    public void onResponse(Call<SyncModule> call, Response<SyncModule> response) {
//
//
//                        Log.e("response", new Gson().toJson(response.body()));
//                        if (response.isSuccessful()) {
//                            Log.e("response", new Gson().toJson(response.body()));
//
//                            SyncModule syncModule = response.body();
//
//                            workingOnResponsesalesstage(syncModule);
//
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<SyncModule> call, Throwable t) {
//                        Log.d("error", t.getMessage());
//                        // progressDialog.dismiss();
//                    }
//
//
//                });
//
//            }
//
//
//        }, 0);
//        return;
//    }
//
//    @SuppressLint("ResourceAsColor")
//    private void workingOnResponsesalesstage(SyncModule syncModule){
//        //isdate=true;
//        String success = syncModule.getSuccess();
//
//        if (success.equals("true")) {
//            SyncResults results = syncModule.getResult();
//
//            Sync sync = results.getSync();
//
//            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();
//
//            for (SyncUpdated syncUpdated : syncUpdateds) {
//
//                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
//
//
//                String salesstage_list = "";
//
//
//                for (SyncBlocks syncBlocks1 : syncBlocks) {
//
//                    String label = syncBlocks1.getLabel();
//                    if (label.equals("Basic Information")) {
//                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
//
//                        for (SynFields synFields1 : synFields) {
//                            name = synFields1.getName();
//                            values = synFields1.getValue();
//                            if (name.equals("sales_stage")) {
//                                salesstage_list = String.valueOf(values);
//                                //  Log.d("status_list",status_list);
//                                salesstage.add(salesstage_list);
//
//                                Count_opportunity = Collections.frequency(salesstage, "Opportunity");
//                               // System.out.println("Count of 30% is:  "+ Count_30);
//                                Count_proposal = Collections.frequency(salesstage, "Proposal or Price Quote");
//                                //System.out.println("Count of In Complete is:  "+ Count_Incomplete);
//
//                                Count_negotiation = Collections.frequency(salesstage, "Negotiation or Review");
//                                Count_won = Collections.frequency(salesstage, "Closed Won");
//                                Count_lost = Collections.frequency(salesstage, "Closed Lost");
//                                // System.out.println("Count of Completed is:  "+ Complted_Count);
//
//                                String[] valueArray = new String[] {"Opportunity","Proposal or Price Quote","Negotiation or Review","Closed Won","Closed Lost"};
//                                // Values for pie chart
//                                ArrayList<PieEntry> yVals1 = new ArrayList<>();
//                                yVals1.add(new PieEntry(Count_opportunity, valueArray[0 % valueArray.length].concat("                      ").concat(":".concat(" ").concat(String.valueOf(Count_opportunity)))));
//                                yVals1.add(new PieEntry(Count_proposal, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_proposal)))));
//                                yVals1.add(new PieEntry(Count_negotiation, valueArray[2 % valueArray.length].concat("    ").concat(":".concat(" ").concat(String.valueOf(Count_negotiation)))));
//                                yVals1.add(new PieEntry(Count_won, valueArray[3 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_won)))));
//                                yVals1.add(new PieEntry(Count_lost, valueArray[4 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_lost)))));
//
//                                PieDataSet dataSet = new PieDataSet(yVals1, "");
//                                dataSet.setDrawIcons(false);
//
//                                dataSet.setIconsOffset(new MPPointF(0, 50));
//                                dataSet.setSliceSpace(2f);
//                                dataSet.setSelectionShift(7f);
//                                int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
//                                int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
//                                int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
//                                int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
//                                int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
//
//
//                                dataSet.setColors(endColor1,endColor2,endColor3,endColor4,endColor5);
//
//
//                                PieData data = new PieData(dataSet);
//                                data.setValueFormatter(new PercentFormatter(chart_sales));
//                                data.setValueTextSize(12f);
//                                data.setValueTextColor(Color.WHITE);
//                                data.setValueTypeface(tfLight);
//                                chart_sales.setData(data);
//                                chart_sales.drawSliceTextEnabled=false;
//
//                                // undo all highlights
//                                chart_sales.highlightValues(null);
//
//                                chart_sales.invalidate();
//
//
//                            }
//
//
//                        }
//                    }
//                }
//
//
//            }
//
//
//
//        }
//    }
//
////    private void fetchTask() {
////
////        sessionId = getActivity().getIntent().getStringExtra("sessionId");
////        String operation = "describe";
////        String module = "Calendar";
////        final GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
////
////        /** Call the method with parameter in the interface to get the notice data*/
////        Call<AccountDescribe> call = service.GetFieldDetailsList(operation, sessionId, module);
////
////        /**Log the URL called*/
////        Log.i("URL Called", call.request().url() + "");
////
////
////        call.enqueue(new Callback<AccountDescribe>() {
////            @Override
////            public void onResponse(Call<AccountDescribe> call, Response<AccountDescribe> response) {
////
////
////              //  Log.e("response", new Gson().toJson(response.body()));
////                if (response.isSuccessful()) {
////                 //   Log.e("response", new Gson().toJson(response.body()));
////
////                    AccountDescribe accountDescribe = response.body();
////
////                    String success = accountDescribe.getSuccess();
////
////                    if (success.equals("true")) {
////                        Results_Account results = accountDescribe.getResult();
////
////                        DescribeDetails describeDetails = results.getDescribe();
////
////                        String value = "";
////
////                        String label = describeDetails.getLabel();
////                        if (label.equals("Calendar")) {
////                            ArrayList<Fields> desFields = describeDetails.getFields();
////
////                            for (Fields desFields1 : desFields) {
////                                String name = desFields1.getName();
////                                //task type
////                                if (name.equals("cf_956")) {
////                                    TypeDetails typeDetails = desFields1.getType();
////
////                                    ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
////
////
////                                    for (PickListValues pickListValues1 : pickListValues) {
////
////                                        value = pickListValues1.getValue();
////                                        label = pickListValues1.getLabel();
////                                        //PickListValues pickListValues2=new PickListValues(value,label);
////                                        //pickListTask.add(pickListValues2);
////                                        taskTypes.add(value);
////                                        // String task=String.valueOf(taskTypes);
////                                        Log.d("tasktype", String.valueOf(taskTypes));
////
////
////                                        xAxis1.setValueFormatter(new ValueFormatter() {
////                                            @Override
////                                            public String getFormattedValue(float value) {
////
////                                                if (value < 0 || value > taskTypes.size() - 1) {
////                                                    return "";
////                                                }
////                                                String valueStr = String.valueOf(taskTypes);
////                                                String[] taskList = valueStr.replace("[", "").replace("]", "").split(",");
////                                                return taskList[(int) value];
////                                            }
////                                        });
////                                        xAxis1.setGranularity(1);
////                                        xAxis1.setTextSize(10f);
////
////                                        xAxis1.setLabelRotationAngle(-90f);
////                                        xAxis1.setLabelCount(taskTypes.size());
////
////
////                                    }
////
////
////                                }
//////
////
//////                                 else if (name.equals("cf_1015")) {
//////                                    TypeDetails typeDetails = desFields1.getType();
//////
//////                                    ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
//////
//////
//////                                    for (PickListValues pickListValues1 : pickListValues) {
//////
//////                                        value = pickListValues1.getValue();
//////                                        outcome_list.add(value);
//////                                        Log.d("outcome_list", String.valueOf(outcome_list));
//////                                        //   xAxis.setValueFormatter(new IndexAxisValueFormatter(outcome_list));
//////
//////
//////
//////
//////                                    }
//////
//////                                }
////
////                            }
////                        }
////
////
////                    }
////                }
////                //   progressDialog.dismiss();
////            }
////
////
////            @Override
////            public void onFailure(Call<AccountDescribe> call, Throwable t) {
////                Log.d("error", t.getMessage());
////                //    progressDialog.dismiss();
////            }
////
////
////        });
////
////    }
//
//    private void fetchOpportunity() {
////support required
//
//        sessionId = getActivity().getIntent().getStringExtra("sessionId");
//        String operation = "describe";
//        String module = "Potentials";
//        final GetNoticeDataService service1 = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
//
//        /** Call the method with parameter in the interface to get the notice data*/
//        Call<AccountDescribe> call = service1.GetFieldDetailsList(operation, sessionId, module);
//
//        /**Log the URL called*/
//      //  Log.i("URL Called", call.request().url() + "");
//
//
//        call.enqueue(new Callback<AccountDescribe>() {
//            @Override
//            public void onResponse(Call<AccountDescribe> call, Response<AccountDescribe> response) {
//
//
//             //   Log.e("response", new Gson().toJson(response.body()));
//                if (response.isSuccessful()) {
//                  //  Log.e("response", new Gson().toJson(response.body()));
//
//                    AccountDescribe accountDescribe = response.body();
//
//                    String success = accountDescribe.getSuccess();
//
//                    if (success.equals("true")) {
//                        Results_Account results = accountDescribe.getResult();
//
//                        DescribeDetails describeDetails = results.getDescribe();
//
//                        String value = "";
//
//                        String label = describeDetails.getLabel();
//                        if (label.equals("Opportunities")) {
//                            ArrayList<Fields> desFields = describeDetails.getFields();
//
//                            for (Fields desFields1 : desFields) {
//                                String name = desFields1.getName();
//                                //support required
//                                if (name.equals("cf_996")) {
//                                    TypeDetails typeDetails = desFields1.getType();
//
//                                    ArrayList<PickListValues> pickListValues = typeDetails.getPicklistValues();
//
//
//                                    for (PickListValues pickListValues1 : pickListValues) {
//
//                                        value = pickListValues1.getValue();
//                                        label = pickListValues1.getLabel();
//                                        support_required.add(value);
//                                        // String task=String.valueOf(taskTypes);
//                                       // Log.d("support_required", String.valueOf(support_required));
//
//
//                                        xAxis_support.setValueFormatter(new ValueFormatter() {
//                                            @Override
//                                            public String getFormattedValue(float value) {
//
//                                                if (value < 0 || value > support_required.size() - 1) {
//                                                    return "";
//                                                }
//                                                String valueStr = String.valueOf(support_required);
//                                                String[] taskList = valueStr.replace("[", "").replace("]", "").split(",");
//                                                return taskList[(int) value];
//                                            }
//                                        });
//                                        xAxis_support.setGranularity(1);
//                                        xAxis_support.setTextSize(10f);
//                                        xAxis_support.setLabelRotationAngle(-90f);
//                                        xAxis_support.setLabelCount(support_required.size());
//                                        xAxis_support.setGranularityEnabled(true);
//
//                                        setData_supportrequired(5,2*2*4F);
//
//
//
//
//                                    }
//
//                                }
//
//                            }
//                        }
//
//
//                    }
//                }
//                //   progressDialog.dismiss();
//            }
//
//
//            @Override
//            public void onFailure(Call<AccountDescribe> call, Throwable t) {
//                Log.d("error", t.getMessage());
//                //    progressDialog.dismiss();
//            }
//
//
//        });
//
//    }
//
//    private void fetchJSONForSupport() {
//
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Write code for your refresh logic
//
////                progressDialog = new ProgressDialog(getActivity());
////                progressDialog.setIndeterminate(true);
////                progressDialog.setMessage("Loading...");
////                progressDialog.setCanceledOnTouchOutside(false);
////                progressDialog.setCancelable(false);
////                progressDialog.show();
//                sessionId = getActivity().getIntent().getStringExtra("sessionId");
//                String operation = "syncModuleRecords";
//                String module = "Potentials";
//                String syncToken = "";
//                String mode = "public";
//                final GetNoticeDataService service = RetrofitInstance.getRetrofitInstance().create(GetNoticeDataService.class);
//
//                /** Call the method with parameter in the interface to get the notice data*/
//                Call<SyncModule> call = service.GetSyncModuleList(operation, sessionId, module, syncToken, mode);
//
//                /**Log the URL called*/
//               /// Log.i("URL Called", call.request().url() + "");
//
//                call.enqueue(new Callback<SyncModule>() {
//                    @Override
//                    public void onResponse(Call<SyncModule> call, Response<SyncModule> response) {
//
//                      //  Log.e("response", new Gson().toJson(response.body()));
//                        if (response.isSuccessful()) {
//                         //   Log.e("response", new Gson().toJson(response.body()));
//
//                            SyncModule syncModule = response.body();
//
//                            Gson g = new Gson();
//                            String json = g.toJson(syncModule);
//                            PreferenceManagerSalesSupport.getInstance(requireContext()).setAPIResponseSales(json);
//
//                            workingOnResponse(syncModule);
//
//
//                        }
//                        //  progressDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<SyncModule> call, Throwable t) {
//                      //  Log.d("error", t.getMessage());
//                        // progressDialog.dismiss();
//                    }
//
//
//                });
//
//            }
//
//
//        }, 0);
//        return;
//
//    }
//    private void workingOnResponse(SyncModule syncModule) {
//
//        String success = syncModule.getSuccess();
//
//        if (success.equals("true")) {
//            SyncResults results = syncModule.getResult();
//
//            Sync sync = results.getSync();
//
//            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();
//
//            for (SyncUpdated syncUpdated : syncUpdateds) {
//
//                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();
//
//                String modality = "";
//                for (SyncBlocks syncBlocks1 : syncBlocks) {
//                    String label = syncBlocks1.getLabel();
//                    if (label.equals("Basic Information")) {
//                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
//
//                        for (SynFields synFields1 : synFields) {
//
//                            String name = synFields1.getName();
//                            values = synFields1.getValue();
//                            if (name.equals("modality")) {
//                                modality = String.valueOf(values);
//
//                                // modality_list.add(modality);
//                                String newValue;
//                                if (!modality_list.contains(modality)) {
//                                    modality_list.add(modality);
//                                }
//                               // Log.d("modality_list", String.valueOf(modality_list));
//
//                                xAxis_modality.setValueFormatter(new ValueFormatter() {
//                                    @Override
//                                    public String getFormattedValue(float value) {
//
//                                        if (value < 0 || value > modality_list.size() - 1) {
//                                            return "";
//                                        }
//                                        String valueStr = String.valueOf(modality_list);
//                                        String[] modalityList = valueStr.replace("[", "").replace("]", "").split(",");
//                                        return modalityList[(int) value];
//                                    }
//                                });
//                                xAxis_modality.setGranularity(1);
//                                xAxis_modality.setTextSize(10f);
//                                xAxis_modality.setLabelRotationAngle(-90f);
//                                xAxis_modality.setLabelCount(modality_list.size());
//                                xAxis_modality.setGranularityEnabled(true);
//
//                                setdata_modality(8,2*2*4F);
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("STATUS");

        return s;
    }

    private SpannableString generateCenterSpannableText1() {

        SpannableString s = new SpannableString("SALES STAGE");

        return s;
    }

    private SpannableString generateCenterSpannableText2() {

        SpannableString s = new SpannableString("WIN PROB");

        return s;
    }

    //
//    private void setData_chart(int count, float range) {
//
//        float barWidth = 0.9f;
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * range);
//            values.add(new BarEntry(i, Math.round(val),
//                    getResources().getDrawable(R.drawable.ic_launcher)));
//        }
//
//        BarDataSet set1;
//        if (chart1.getData() != null &&
//                chart1.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) chart1.getData().getDataSetByIndex(0);
//            set1.setValues(values);
//            chart1.getData().notifyDataChanged();
//            chart1.notifyDataSetChanged();
//        }
//        else {
//            set1 = new BarDataSet(values, "");
//
//            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
//
//
//            set1.setColors(endColor1);
//            set1.setDrawIcons(false);
//
//            dataSets = new ArrayList<>();
//            dataSets.add(set1);
//            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(12f);
//            data.setValueTypeface(tfLight);
//            data.setValueFormatter(formatter);
//            data.setBarWidth(barWidth);
//            chart1.setData(data);
//            chart1.invalidate();
//        }
//    }
////    private void setData_outcome(int count, float range) {
////
////       // float barWidth = 0.9f;
////        float barSpace = 0f;
////        float barWidth = 0.7f;
////        ArrayList<BarEntry> values = new ArrayList<>();
////
////        for (int i = 0; i < count; i++) {
////            float val = (float) (Math.random() * range);
////            values.add(new BarEntry(i, val,
////                    getResources().getDrawable(R.drawable.ic_launcher)));
////        }
////
////        BarDataSet set1;
////        if (chart.getData() != null &&
////                chart.getData().getDataSetCount() > 0) {
////            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
////            set1.setValues(values);
////            chart.getData().notifyDataChanged();
////            chart.notifyDataSetChanged();
////        }
////        else {
////            set1 = new BarDataSet(values, "Outcome");
////
////            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
////
////
////            set1.setColors(endColor1);
////            set1.setDrawIcons(false);
////
////            dataSets = new ArrayList<>();
////            dataSets.add(set1);
////            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
////            BarData data = new BarData(dataSets);
////            data.setValueTextSize(12f);
////            data.setValueFormatter(formatter);
////            data.setValueTypeface(tfLight);
////            data.setBarWidth(barWidth);
////            chart.setData(data);
////        }
////    }
//
//    public class CustomXAxisRenderer extends XAxisRenderer {
//        public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
//            super(viewPortHandler, xAxis, trans);
//        }
//
//        @Override
//        protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
//            String line[] = formattedLabel.split(" ");
//            Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
//            if (line.length >1)
//                Utils.drawXAxisValue(c, line[1], x + mAxisLabelPaint.getTextSize(), y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
//        }
//    }
//    private void setData_supportrequired(int count, float range) {
//
//        float barWidth = 0.9f;
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * range);
//            values.add(new BarEntry(i, val,
//                    getResources().getDrawable(R.drawable.ic_launcher)));
//        }
//
//        BarDataSet set_support;
//        if (chart_support.getData() != null &&
//                chart_support.getData().getDataSetCount() > 0) {
//            set_support = (BarDataSet) chart_support.getData().getDataSetByIndex(0);
//            set_support.setValues(values);
//            chart_support.getData().notifyDataChanged();
//            chart_support.notifyDataSetChanged();
//        }
//        else {
//            set_support = new BarDataSet(values, "Support Required");
//
//            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
//
//
//            set_support.setColors(endColor1);
//
//            set_support.setDrawIcons(false);
//
//            dataSets = new ArrayList<>();
//            dataSets.add(set_support);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(12f);
//            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
//            data.setValueTypeface(tfLight);
//            data.setValueFormatter(formatter);
//            data.setBarWidth(barWidth);
//            chart_support.setData(data);
//        }
//    }
//    private void setdata_modality(int count, float range) {
//
//        float barWidth = 0.9f;
//        ArrayList<BarEntry> values = new ArrayList<>();
//
//        for (int i = 0; i < count; i++) {
//            float val = (float) (Math.random() * range);
//            values.add(new BarEntry(i, val,
//                    getResources().getDrawable(R.drawable.ic_launcher)));
//        }
//
//        BarDataSet set_modality;
//        if (chart_modality.getData() != null &&
//                chart_modality.getData().getDataSetCount() > 0) {
//            set_modality = (BarDataSet) chart_modality.getData().getDataSetByIndex(0);
//            set_modality.setValues(values);
//            chart_modality.getData().notifyDataChanged();
//            chart_modality.notifyDataSetChanged();
//            chart_modality.invalidate();
//        }
//        else {
//            set_modality = new BarDataSet(values, "Modality");
//
//            int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
//
//
//            set_modality.setColors(endColor1);
//
//
//            set_modality.setDrawIcons(false);
//
//            dataSets = new ArrayList<>();
//            dataSets.add(set_modality);
//            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(12f);
//            data.setValueFormatter(formatter);
//            data.setValueTypeface(tfLight);
//            data.setBarWidth(barWidth);
//            chart_modality.setData(data);
//            chart_modality.invalidate();
//        }
//
//    }
    protected void saveToGallery(Chart chart, String name) {
        if (chart.saveToGallery(name + "_" + System.currentTimeMillis(), 70)) {
            Toast.makeText(getContext(), "Saving SUCCESSFUL!",
                    Toast.LENGTH_SHORT).show();
            openScreenshot(new File(name));
        } else
            Toast.makeText(getContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                    .show();
    }

    private void openScreenshot(File imageFile) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

//Weekwise for Opportunity

    @SuppressLint("ResourceAsColor")
    private void workingOnResponseweekwiseOpportunity(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String supportrequired = "";
                String winprob = "";
                String modifydatetime = "";
                String salesstage = "";
                String modifiedtime = "";
                String modality = "";
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();
                            //sales stage
                            if (name.equals("sales_stage")) {
                                salesstage = String.valueOf(values);
                               // salesstage_list.add(salesstage);
                                //support required
                            }
                            //modality
                            else if (name.equals("modality")) {
                                modality = String.valueOf(values);

                            }

                        }
                    } else if (label.equals("Opportunity Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("createdtime")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputFormat.format(date);
                                    Log.d("scheduleDate", scheduleDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (name.equals("cf_996")) {
                                supportrequired = String.valueOf(values);

                            } else if (name.equals("cf_897")) {
                                winprob = String.valueOf(values);

                            } else if (("modifiedtime".equals(name))) {
                                modifydatetime = String.valueOf(values);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    modifiedday=String.valueOf(LocalDateTime.parse(modifydatetime,format));
                                    Log.d("modifiedday", String.valueOf(modifiedday));
                                    finalmonthyearcurrent= LocalDateTime.parse(modifiedday);
                                    monthlist.add(String.valueOf(finalmonthyearcurrent));
                                    Log.d("monthlist", String.valueOf(monthlist));
                                    break;
                                }
                            }

                        }
                    }

                    PreferenceManagerMyOpportunity.getInstance(requireContext()).setMultipleDataOpportunity(scheduleDate, salesstage, supportrequired, winprob, modality, modifiedtime);

                }
                MyOpportunityModel opportunityModel = new MyOpportunityModel(scheduleDate, salesstage, supportrequired, winprob, modality, modifiedtime);
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                Date startDate = calendar.getTime();
                String weekstart = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(startDate);
                Log.v("output date ", weekstart);
                calendar.add(Calendar.DATE, 6);
                Date endDate = calendar.getTime();
                String weekend = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(endDate);
                Log.d(TAG, "Start Date = " + startDate);
                Log.d(TAG, "End Date = " + endDate);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    startweek = LocalDateTime.parse(weekstart, format);
                    Log.d(TAG, "startweek = " + startweek);
                    endweek = LocalDateTime.parse(weekend, format);
                    Log.d(TAG, "endweek = " + endweek);
                        iscurrentmonth_opp = ((startweek.isBefore(finalmonthyearcurrent))&&(endweek.isAfter(finalmonthyearcurrent)) ? "Yes" : "No");
                        Log.d("iscurrentmonth_opp",iscurrentmonth_opp);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (iscurrentmonth_opp.equals("Yes")) {
                        salesstage_list.add(salesstage);
                        boolean ans = salesstage_list.contains("Opportunity") || salesstage_list.contains("Proposal or Price Quote")
                                || salesstage_list.contains("Negotiation or Review") || salesstage_list.contains("Closed Won")
                                || salesstage_list.contains("Closed Lost");
                        if (ans) {
                           // System.out.println("The list contains 2" + salesstage);
                            Count_opportunity = Collections.frequency(salesstage_list, "Opportunity");
                            // System.out.println("Count of 30% is:  "+ Count_30);
                            Count_proposal = Collections.frequency(salesstage_list, "Proposal or Price Quote");
                            //System.out.println("Count of In Complete is:  "+ Count_Incomplete);

                            Count_negotiation = Collections.frequency(salesstage_list, "Negotiation or Review");
                            Count_won = Collections.frequency(salesstage_list, "Closed Won");
                            Count_lost = Collections.frequency(salesstage_list, "Closed Lost");
                            // System.out.println("Count of Completed is:  "+ Complted_Count);


                            String[] valueArray = new String[]{"Opportunity", "Proposal or Price Quote", "Negotiation or Review", "Closed Won", "Closed Lost"};
                            //                                 // Values for pie chart
                            ArrayList<PieEntry> yVals1 = new ArrayList<>();
                            yVals1.add(new PieEntry(Count_opportunity, valueArray[0 % valueArray.length].concat("                      ").concat(":".concat(" ").concat(String.valueOf(Count_opportunity)))));
                            yVals1.add(new PieEntry(Count_proposal, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_proposal)))));
                            yVals1.add(new PieEntry(Count_negotiation, valueArray[2 % valueArray.length].concat("    ").concat(":".concat(" ").concat(String.valueOf(Count_negotiation)))));
                            yVals1.add(new PieEntry(Count_won, valueArray[3 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_won)))));
                            yVals1.add(new PieEntry(Count_lost, valueArray[4 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_lost)))));


                            PieDataSet dataSet_sales = new PieDataSet(yVals1, "");
                            dataSet_sales.setDrawIcons(false);

                            dataSet_sales.setIconsOffset(new MPPointF(0, 40));
                            dataSet_sales.setSliceSpace(2f);
                            dataSet_sales.setSelectionShift(5f);
                            int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                            int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
                            int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                            int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                            int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
                            dataSet_sales.setColors(endColor1, endColor2, endColor3, endColor4, endColor5);
                            PieData data1 = new PieData(dataSet_sales);
                            data1.setValueFormatter(new PercentFormatter(chart_sales));
                            data1.setValueTextSize(12f);
                            data1.setValueTextColor(Color.WHITE);
                            data1.setValueTypeface(tfLight);
                            chart_sales.setData(data1);
                            chart_sales.drawSliceTextEnabled = false;
                            // undo all highlights
                            chart_sales.highlightValues(null);
                            //Sales stage ends
                        }
                    }
                        else if (iscurrentmonth_opp.equals("Yes")) {
                        supportrequired_list.add(supportrequired);
                        boolean support = supportrequired_list.contains("No Support Required") || supportrequired_list.contains("Travel Support")
                                || supportrequired_list.contains("Funding Required") || supportrequired_list.contains("Demo Required")
                                || supportrequired_list.contains("Price Support")||supportrequired_list.contains("Product Validation Support");
                        Log.d("support",supportrequired);
                            if (opportunityModel.getSupport_required().equals("No Support Required")) {
                                noSupport_required = opportunityModel.getSupport_required();
                            } else if (opportunityModel.getSupport_required().equals("Travel Support")) {
                                travel_support = opportunityModel.getSupport_required();
                            } else if (opportunityModel.getSupport_required().equals("Funding Required")) {
                                funding_req = opportunityModel.getSupport_required();
                            } else if (opportunityModel.getSupport_required().equals("Demo Required")) {
                                demo_required = opportunityModel.getSupport_required();
                            } else if (opportunityModel.getSupport_required().equals("Price Support")) {
                                price_support = opportunityModel.getSupport_required();
                            } else if (opportunityModel.getSupport_required().equals("Product Validation Support")) {
                                product_validation = opportunityModel.getSupport_required();
                            }


                            if (support) {

                                //support required starts here

                                Count_NoSupportRequired = Collections.frequency(supportrequired_list, "No Support Required");
                                Count_Travel = Collections.frequency(supportrequired_list, "Travel Support");
                                Count_Funding = Collections.frequency(supportrequired_list, "Funding Required");
                                Count_Demo = Collections.frequency(supportrequired_list, "Demo Required");
                                Count_Price = Collections.frequency(supportrequired_list, "Price Support");
                                Count_Product = Collections.frequency(supportrequired_list, "Product Validation Support");
                                final ArrayList<String> arrayList = new ArrayList<String>();
                                arrayList.add(noSupport_required);
                                arrayList.add(travel_support);
                                arrayList.add(funding_req);
                                arrayList.add(demo_required);
                                arrayList.add(price_support);
                                arrayList.add(product_validation);
                                xAxis_support.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {

                                        if (value < 0 || value > arrayList.size() - 1) {
                                            return "";
                                        }
                                        String valueStr = String.valueOf(arrayList);
                                        String[] outcomeList = valueStr.replace("[", "").replace("]", "").split(",");
                                        return outcomeList[(int) value];
                                    }
                                });
                                xAxis_support.setGranularity(1);
                                xAxis_support.setTextSize(10f);
                                xAxis_support.setLabelRotationAngle(-90f);
                                xAxis_support.setLabelCount(outcome_list.size());
                                xAxis_support.setGranularityEnabled(true);
                                float barWidth = 0.9f;
                                ArrayList<BarEntry> values = new ArrayList<>();
                                values.add(new BarEntry(0, Count_NoSupportRequired));
                                values.add(new BarEntry(1, Count_Travel));
                                values.add(new BarEntry(2, Count_Funding));
                                values.add(new BarEntry(3, Count_Demo));
                                values.add(new BarEntry(4, Count_Price));
                                values.add(new BarEntry(5, Count_Product));
                                BarDataSet set1;
                                if (chart_support.getData() != null &&
                                        chart_support.getData().getDataSetCount() > 0) {
                                    set1 = (BarDataSet) chart_support.getData().getDataSetByIndex(0);

                                    set1.setValues(values);
                                    chart_support.getData().notifyDataChanged();


                                } else {

                                    set1 = new BarDataSet(values, "Support Required");


                                    //  int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                                    set1.setColors(endColor1);
                                    set1.setDrawIcons(false);
                                    dataSets = new ArrayList<>();
                                    dataSets.add(set1);


                                    MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                                    BarData data = new BarData(dataSets);
                                    data.setValueTextSize(12f);
                                    data.setValueFormatter(formatter);
                                    data.setValueTypeface(tfLight);
                                    data.setBarWidth(barWidth);
                                    chart_support.setData(data);
                                }
                            }else {
                                chart_support.setNoDataText("No Data Available For The Selected Period");
                                chart_support.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                chart_support.setNoDataTextTypeface(tfLight);
                                chart_support.setNoDataTextColor(endColor3);

                            }
                        }
                        //Support Required end here

                        //Win Prob Starts here
                    else if (iscurrentmonth_opp.equals("Yes")) {
                        winprob_list.add(winprob);
                        boolean iswin = winprob_list.contains("30 %") || winprob_list.contains("50 %") || winprob_list.contains("80 %");
                        Log.d("iswin", String.valueOf(iswin));
                        if (iswin) {
                            Count_30 = Collections.frequency(winprob_list, "30 %");
                            // System.out.println("Count of 30% is:  "+ Count_30);
                            Count_50 = Collections.frequency(winprob_list, "50 %");
                            //System.out.println("Count of In Complete is:  "+ Count_Incomplete);

                            Count_80 = Collections.frequency(winprob_list, "80 %");
                            // System.out.println("Count of Completed is:  "+ Complted_Count);

                            String[] winprobs = new String[]{"30%", "50%", "80%"};//                                 // Values for pie chart
                            ArrayList<PieEntry> yVals_winprob = new ArrayList<>();

                            yVals_winprob.add(new PieEntry(Count_30, winprobs[0 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_30)))));
                            //System.out.println("Count_Incomplete:  "+ Count_Incomplete);
                            yVals_winprob.add(new PieEntry(Count_50, winprobs[1 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_50)))));
                            yVals_winprob.add(new PieEntry(Count_80, winprobs[2 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_80)))));


                            PieDataSet dataSet_winprob = new PieDataSet(yVals_winprob, "");
                            dataSet_winprob.setDrawIcons(false);

                            dataSet_winprob.setIconsOffset(new MPPointF(0, 40));
                            dataSet_winprob.setSliceSpace(2f);
                            dataSet_winprob.setSelectionShift(5f);
                            dataSet_winprob.setColors(endColor5, endColor3, endColor4);
                            PieData data_winprob = new PieData(dataSet_winprob);
                            data_winprob.setValueFormatter(new PercentFormatter(chart_winprob));
                            data_winprob.setValueTextSize(12f);
                            data_winprob.setValueTextColor(Color.WHITE);
                            data_winprob.setValueTypeface(tfLight);
                            chart_winprob.setData(data_winprob);
                            chart_winprob.drawSliceTextEnabled = false;
                            // undo all highlights
                            chart_winprob.highlightValues(null);

                        } else {
                            chart_modality.setNoDataText("No Data Available For The Selected Period");
                            chart_modality.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            chart_modality.setNoDataTextTypeface(tfLight);
                            chart_modality.setNoDataTextColor(endColor3);
                        }
                    }
                            }

                        //Winprob ends here


                        //Modality starts here

                            else if (iscurrentmonth_opp.equals("Yes")) {

                                if (opportunityModel.getModality().equals("IVD")) {
                                    modality_ivd = opportunityModel.getModality();
                                } else if (opportunityModel.getModality().equals("WHS")) {
                                    modality_whs = opportunityModel.getModality();
                                } else if (opportunityModel.getModality().equals("Ultrasound")) {
                                    modality_ultrasound = opportunityModel.getModality();
                                } else if (opportunityModel.getModality().equals("LCS")) {
                                    modality_lcs = opportunityModel.getModality();

                                } else if (opportunityModel.getModality().equals("NBC")) {
                                    modality_nbc = opportunityModel.getModality();
                                }
                                modality_list.add(modality);
                                boolean ans=modality_list.contains("IVD");
                                if(ans)
                                Count_IVD = Collections.frequency(modality_list, "IVD");
                                Count_WHS = Collections.frequency(modality_list, "WHS");
                                Count_UltraSound = Collections.frequency(modality_list, "Ultrasound");
                                Count_LCS = Collections.frequency(modality_list, "LCS");
                                Count_NBC = Collections.frequency(modality_list, "NBC");


                                final ArrayList<String> arrayList_winprob = new ArrayList<String>();
                                arrayList_winprob.add(noSupport_required);
                                arrayList_winprob.add(travel_support);
                                arrayList_winprob.add(funding_req);
                                arrayList_winprob.add(demo_required);
                                arrayList_winprob.add(price_support);
                                arrayList_winprob.add(product_validation);
                                xAxis_modality.setValueFormatter(new ValueFormatter() {
                                    @Override
                                    public String getFormattedValue(float value) {

                                        if (value < 0 || value > arrayList_winprob.size() - 1) {
                                            return "";
                                        }
                                        String valueStr = String.valueOf(arrayList_winprob);
                                        String[] modalityList = valueStr.replace("[", "").replace("]", "").split(",");
                                        return modalityList[(int) value];
                                    }
                                });
                                xAxis_modality.setGranularity(1);
                                xAxis_modality.setTextSize(10f);
                                xAxis_modality.setLabelRotationAngle(-90f);
                                xAxis_modality.setLabelCount(arrayList_winprob.size());
                                xAxis_modality.setGranularityEnabled(true);
                                ArrayList<BarEntry> valuesModality = new ArrayList<>();
                                //     if (scheduleDate.equals("11-01-2020")) {

                                valuesModality.add(new BarEntry(0, Count_IVD));
                                valuesModality.add(new BarEntry(1, Count_WHS));
                                valuesModality.add(new BarEntry(2, Count_UltraSound));
                                valuesModality.add(new BarEntry(3, Count_LCS));
                                valuesModality.add(new BarEntry(4, Count_NBC));

                                BarDataSet set_modality;
                                if (chart_modality.getData() != null &&
                                        chart_modality.getData().getDataSetCount() > 0) {
                                    set_modality = (BarDataSet) chart_modality.getData().getDataSetByIndex(0);

                                    set_modality.setValues(valuesModality);
                                    chart_modality.getData().notifyDataChanged();


                                } else {

                                    set_modality = new BarDataSet(valuesModality, "Modality");


                                    // int endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                                    set_modality.setColors(endColor1);
                                    set_modality.setDrawIcons(false);
                                    dataSets = new ArrayList<>();
                                    dataSets.add(set_modality);


                                    MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                                    BarData data = new BarData(dataSets);
                                    data.setValueTextSize(12f);
                                    data.setValueFormatter(formatter);
                                    data.setValueTypeface(tfLight);
                                    data.setBarWidth(barWidth);
                                    chart_modality.setData(data);
                                }
                            }
                        //Modality end here


                    } else {

                        //status
                        chart_sales.setNoDataText("No Data Available For The Selected Period");
                        chart_sales.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_sales.setNoDataTextTypeface(tfLight);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        chart_sales.setNoDataTextColor(endColor3);

                        chart_support.setNoDataText("No Data Available For The Selected Period");
                        chart_support.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_support.setNoDataTextTypeface(tfLight);
                        chart_support.setNoDataTextColor(endColor3);

                        chart_winprob.setNoDataText("No Data Available For The Selected Period");
                        chart_winprob.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_winprob.setNoDataTextTypeface(tfLight);
                        chart_winprob.setNoDataTextColor(endColor3);

                        chart_modality.setNoDataText("No Data Available For The Selected Period");
                        chart_modality.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_modality.setNoDataTextTypeface(tfLight);
                        chart_modality.setNoDataTextColor(endColor3);
                    }
                }
            }
        }


    //Mothwise for Opportunity


    private void workingOnResponseMonthwiseOpportunity(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String supportrequired = "";
                String winprob = "";
                String salesstage = "";
                String modality = "";
                String modifydatetime = "";
                //   LocalDateTime modifiedtime= LocalDateTime.parse("");
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {

                    String label = syncBlocks1.getLabel();
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();

                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();


                            //sales stage
                            if (name.equals("sales_stage")) {
                                salesstage = String.valueOf(values);

                                //support required
                            }
                            //modality
                            else if (name.equals("modality")) {
                                modality = String.valueOf(values);
                                modality_list.add(modality);
                            }

                        }
                    } else if (label.equals("Opportunity Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("createdtime")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                DateFormat outputMonth = new SimpleDateFormat("MM");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputMonth.format(date);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (name.equals("cf_996")) {
                                supportrequired = String.valueOf(values);
                                supportrequired_list.add(supportrequired);
                            } else if (name.equals("cf_897")) {
                                winprob = String.valueOf(values);
                                winprob_list.add(winprob);
                            } else if (("modifiedtime".equals(name))) {
                                modifydatetime = String.valueOf(values);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                                    modifiedmonth= String.valueOf(LocalDateTime.parse(modifydatetime,format).getMonthValue());
                                    modifiedyear= String.valueOf(LocalDateTime.parse(modifydatetime,format).getYear());
                                    finalmonthyear=modifiedyear.concat("-").concat(modifiedmonth);
                                    monthlist.add(finalmonthyear);
                                    Log.d("monthlist", String.valueOf(monthlist));
                                    break;
                                }
                            }
                        }
                    }
                    PreferenceManagerMyOpportunity.getInstance(requireContext()).setMultipleDataOpportunity(scheduleDate, salesstage, supportrequired, winprob, modality, modifydatetime);
                }
                MyOpportunityModel opportunityModel = new MyOpportunityModel(scheduleDate, salesstage, supportrequired, winprob, modality, modifydatetime);
                //String month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
                LocalDate today = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    today = LocalDate.now();
                    String year = String.valueOf(today.getYear());
                    String month = String.valueOf(today.getMonthValue());
                    String monthyear = year.concat("-").concat(month);
                  //  monthyearlist.add(monthyear);

                    for (String temp : monthlist)
                    isModifiedThisMonth =(monthyear.contains(temp) ? "Yes" : "No");
                    Log.d("monthyear", String.valueOf(monthyear));
                    Log.d("isModifiedThisMonth", String.valueOf(isModifiedThisMonth));

                    if (isModifiedThisMonth.equals("Yes")) {
                        Log.d("isModifiedThisMonth", String.valueOf(isModifiedThisMonth));
                        //Sales stage starts
                       // Log.d("salesstage", String.valueOf(opportunityModel.getSales_stage()));
                        salesstage_list.add(salesstage);
                        boolean ans = salesstage_list.contains("Opportunity")||salesstage_list.contains("Proposal or Price Quote")
                                ||salesstage_list.contains("Negotiation or Review")||salesstage_list.contains("Closed Won")
                                ||salesstage_list.contains("Closed Lost");
                        if (ans) {
                            System.out.println("The list contains 2" + salesstage);

                            Count_opportunity = Collections.frequency(salesstage_list, "Opportunity");
                            System.out.println("Count_opportunity:  " + Count_opportunity);
                            Count_proposal = Collections.frequency(salesstage_list, "Proposal or Price Quote");
                            System.out.println("Count_proposal:  " + Count_proposal);
                            Count_negotiation = Collections.frequency(salesstage_list, "Negotiation or Review");
                            System.out.println("Count_negotiation:  " + Count_negotiation);
                            Count_won = Collections.frequency(salesstage_list, "Closed Won");
                            System.out.println("Count_won:  " + Count_won);
                            Count_lost = Collections.frequency(salesstage_list, "Closed Lost");
                            System.out.println("Count_lost:  " + Count_lost);
                        }
                        else
                            System.out.println("The list does not contains 2");

                        String[] valueArray = new String[]{"Opportunity", "Proposal or Price Quote", "Negotiation or Review", "Closed Won", "Closed Lost"};
//                                 // Values for pie chart
                        ArrayList<PieEntry> yVals1 = new ArrayList<>();
                        yVals1.add(new PieEntry(Count_opportunity, valueArray[0 % valueArray.length].concat("                      ").concat(":".concat(" ").concat(String.valueOf(Count_opportunity)))));
                        yVals1.add(new PieEntry(Count_proposal, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_proposal)))));
                        yVals1.add(new PieEntry(Count_negotiation, valueArray[2 % valueArray.length].concat("    ").concat(":".concat(" ").concat(String.valueOf(Count_negotiation)))));
                        yVals1.add(new PieEntry(Count_won, valueArray[3 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_won)))));
                        yVals1.add(new PieEntry(Count_lost, valueArray[4 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_lost)))));


                        PieDataSet dataSet_sales = new PieDataSet(yVals1, "");
                        dataSet_sales.setDrawIcons(false);

                        dataSet_sales.setIconsOffset(new MPPointF(0, 40));
                        dataSet_sales.setSliceSpace(2f);
                        dataSet_sales.setSelectionShift(5f);
                        int endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                        int endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                        int endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        int endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);


                        dataSet_sales.setColors(endColor1, endColor2, endColor3, endColor4, endColor5);
                        PieData data1 = new PieData(dataSet_sales);
                        data1.setValueFormatter(new PercentFormatter(chart_sales));
                        data1.setValueTextSize(12f);
                        data1.setValueTextColor(Color.WHITE);
                        data1.setValueTypeface(tfLight);
                        chart_sales.setData(data1);
                        chart_sales.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart_sales.highlightValues(null);
                        //Sales stage ends
                    }
                   else if (isModifiedThisMonth.equals("yes")) {
                        //support required starts here
                        if (opportunityModel.getSupport_required().equals("No Support Required")) {
                            noSupport_required = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Travel Support")) {
                            travel_support = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Funding Required")) {
                            funding_req = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Demo Required")) {
                            demo_required = opportunityModel.getSupport_required();

                        } else if (opportunityModel.getSupport_required().equals("Price Support")) {
                            price_support = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Product Validation Support")) {
                            product_validation = opportunityModel.getSupport_required();

                        }
                        Count_NoSupportRequired = Collections.frequency(supportrequired_list, "No Support Required");
                        Count_Travel = Collections.frequency(supportrequired_list, "Travel Support");
                        Count_Funding = Collections.frequency(supportrequired_list, "Funding Required");
                        Count_Demo = Collections.frequency(supportrequired_list, "Demo Required");
                        Count_Price = Collections.frequency(supportrequired_list, "Price Support");
                        Count_Product = Collections.frequency(supportrequired_list, "Product Validation Support");


                        final ArrayList<String> arrayList_support = new ArrayList<>();
                        arrayList_support.add(noSupport_required);
                        arrayList_support.add(travel_support);
                        arrayList_support.add(funding_req);
                        arrayList_support.add(demo_required);
                        arrayList_support.add(price_support);
                        arrayList_support.add(product_validation);
                        xAxis_support.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList_support.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList_support);
                                String[] supportList = valueStr.replace("[", "").replace("]", "").split(",");
                                return supportList[(int) value];
                            }
                        });
                        xAxis_support.setGranularity(1);
                        xAxis_support.setTextSize(10f);
                        xAxis_support.setLabelRotationAngle(-90f);
                        xAxis_support.setLabelCount(arrayList_support.size());
                        xAxis_support.setGranularityEnabled(true);


                        float barWidth = 0.9f;
//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                        ArrayList<BarEntry> values_support = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {
                        values_support.add(new BarEntry(0, Count_NoSupportRequired));
                        values_support.add(new BarEntry(1, Count_Travel));
                        values_support.add(new BarEntry(2, Count_Funding));
                        values_support.add(new BarEntry(3, Count_Demo));
                        values_support.add(new BarEntry(4, Count_Price));
                        values_support.add(new BarEntry(5, Count_Product));
                        //    }


                        BarDataSet support_set;
                        if (chart_support.getData() != null &&
                                chart_support.getData().getDataSetCount() > 0) {
                            support_set = (BarDataSet) chart_support.getData().getDataSetByIndex(0);

                            support_set.setValues(values_support);
                            chart_support.getData().notifyDataChanged();


                        } else {

                            support_set = new BarDataSet(values_support, "Support Required");


                            endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            support_set.setColors(endColor1);
                            support_set.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(support_set);


                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart_support.setData(data);
                        }
                    }
                    //Support Required end here
                    else if (isModifiedThisMonth.equals("yes")) {
                        //Win Prob Starts here
                        Count_30 = Collections.frequency(winprob_list, "30 %");
                         System.out.println("Count of 30% is:  "+ Count_30);
                        Count_50 = Collections.frequency(winprob_list, "50 %");
                        System.out.println("Count of In Complete is:  "+ Count_Incomplete);

                        Count_80 = Collections.frequency(winprob_list, "80 %");
                        // System.out.println("Count of Completed is:  "+ Complted_Count);

                        String[] winprobs = new String[]{"30%", "50%", "80%"};//                                 // Values for pie chart
                        ArrayList<PieEntry> yVals_winprob = new ArrayList<>();

                        yVals_winprob.add(new PieEntry(Count_30, winprobs[0 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_30)))));
                        //System.out.println("Count_Incomplete:  "+ Count_Incomplete);
                        yVals_winprob.add(new PieEntry(Count_50, winprobs[1 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_50)))));
                        yVals_winprob.add(new PieEntry(Count_80, winprobs[2 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_80)))));


                        PieDataSet dataSet_winprob = new PieDataSet(yVals_winprob, "");
                        dataSet_winprob.setDrawIcons(false);

                        dataSet_winprob.setIconsOffset(new MPPointF(0, 40));
                        dataSet_winprob.setSliceSpace(2f);
                        dataSet_winprob.setSelectionShift(5f);
                        dataSet_winprob.setColors(endColor5, endColor3, endColor4);
                        PieData data_winprob = new PieData(dataSet_winprob);
                        data_winprob.setValueFormatter(new PercentFormatter(chart_winprob));
                        data_winprob.setValueTextSize(12f);
                        data_winprob.setValueTextColor(Color.WHITE);
                        data_winprob.setValueTypeface(tfLight);
                        chart_winprob.setData(data_winprob);
                        chart_winprob.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart_winprob.highlightValues(null);
                        //Winprob ends here
                    } else if (isModifiedThisMonth.equals("yes")) {

                        //Modality starts here
                        if (opportunityModel.getModality().equals("IVD")) {
                            modality_ivd = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("WHS")) {
                            modality_whs = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("Ultrasound")) {
                            modality_ultrasound = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("LCS")) {
                            modality_lcs = opportunityModel.getModality();

                        } else if (opportunityModel.getModality().equals("NBC")) {
                            modality_nbc = opportunityModel.getModality();
                        }
                        Count_IVD = Collections.frequency(modality_list, "IVD");
                        Count_WHS = Collections.frequency(modality_list, "WHS");
                        Count_UltraSound = Collections.frequency(modality_list, "Ultrasound");
                        Count_LCS = Collections.frequency(modality_list, "LCS");
                        Count_NBC = Collections.frequency(modality_list, "NBC");


                        final ArrayList<String> arrayList_modality = new ArrayList<String>();
                        arrayList_modality.add(modality_ivd);
                        arrayList_modality.add(modality_whs);
                        arrayList_modality.add(modality_ultrasound);
                        arrayList_modality.add(modality_lcs);
                        arrayList_modality.add(modality_nbc);
                        xAxis_modality.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList_modality.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList_modality);
                                String[] modalityList = valueStr.replace("[", "").replace("]", "").split(",");
                                return modalityList[(int) value];
                            }
                        });
                        xAxis_modality.setGranularity(1);
                        xAxis_modality.setTextSize(10f);
                        xAxis_modality.setLabelRotationAngle(-90f);
                        xAxis_modality.setLabelCount(arrayList_modality.size());
                        xAxis_modality.setGranularityEnabled(true);
                        ArrayList<BarEntry> valuesModality = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {

                        valuesModality.add(new BarEntry(0, Count_IVD));
                        valuesModality.add(new BarEntry(1, Count_WHS));
                        valuesModality.add(new BarEntry(2, Count_UltraSound));
                        valuesModality.add(new BarEntry(3, Count_LCS));
                        valuesModality.add(new BarEntry(4, Count_NBC));

                        //    }


                        BarDataSet set_modality;
                        if (chart_modality.getData() != null &&
                                chart_modality.getData().getDataSetCount() > 0) {
                            set_modality = (BarDataSet) chart_modality.getData().getDataSetByIndex(0);

                            set_modality.setValues(valuesModality);
                            chart_modality.getData().notifyDataChanged();


                        } else {

                            set_modality = new BarDataSet(valuesModality, "Modality");


                            endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            set_modality.setColors(endColor1);
                            set_modality.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(set_modality);


                            MyDecimalValueFormatter formatter = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart_modality.setData(data);
                        }
                        //Modality end here


                    }

                }else {

                    //status
                    chart_sales.setNoDataText("No Data Available For The Selected Period");
                    chart_sales.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    chart_sales.setNoDataTextTypeface(tfLight);
                    int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                    chart_sales.setNoDataTextColor(endColor3);

                    chart_support.setNoDataText("No Data Available For The Selected Period");
                    chart_support.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    chart_support.setNoDataTextTypeface(tfLight);
                    chart_support.setNoDataTextColor(endColor3);

                    chart_winprob.setNoDataText("No Data Available For The Selected Period");
                    chart_winprob.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    chart_winprob.setNoDataTextTypeface(tfLight);
                    chart_winprob.setNoDataTextColor(endColor3);

                    chart_modality.setNoDataText("No Data Available For The Selected Period");
                    chart_modality.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    chart_modality.setNoDataTextTypeface(tfLight);
                    chart_modality.setNoDataTextColor(endColor3);
                }
            }
        }

    }
    //Quarter wise for Opportunity

    @SuppressLint("ResourceAsColor")
    private void workingOnResponseQuarterwiseOpportunity(SyncModule syncModule) {
        //isdate=true;
        String success = syncModule.getSuccess();

        if (success.equals("true")) {
            SyncResults results = syncModule.getResult();

            Sync sync = results.getSync();

            ArrayList<SyncUpdated> syncUpdateds = sync.getUpdated();

            for (SyncUpdated syncUpdated : syncUpdateds) {

                ArrayList<SyncBlocks> syncBlocks = syncUpdated.getBlocks();

                String scheduleDates = "";
                String scheduleDate = "";
                String supportrequired = "";
                String winprob = "";
                String salesstage = "";
                String modality = "";
                String modifydatetime = "";
                String modifiedtime = "";
                Date moddate = null;
                String startdate = null;
                Date enddate = null;
                String date1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                for (SyncBlocks syncBlocks1 : syncBlocks) {
                    String label = syncBlocks1.getLabel();
                    if (label.equals("Basic Information")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {
                            name = synFields1.getName();
                            values = synFields1.getValue();
                            //sales stage
                            if (name.equals("sales_stage")) {
                                salesstage = String.valueOf(values);
                                salesstage_list.add(salesstage);
                                //support required
                            }
                            //modality
                            else if (name.equals("modality")) {
                                modality = String.valueOf(values);
                                modality_list.add(modality);
                            }
                        }
                    } else if (label.equals("Opportunity Details")) {
                        ArrayList<SynFields> synFields = syncBlocks1.getFields();
                        for (SynFields synFields1 : synFields) {

                            String name = synFields1.getName();
                            values = synFields1.getValue();

                            if (name.equals("createdtime")) {
                                scheduleDates = String.valueOf(values);
                                DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
                                DateFormat outputMonth = new SimpleDateFormat("MM");
                                // String inputDateStr="2013-06-24";
                                Date date = null;
                                try {
                                    date = inputFormat.parse(scheduleDates);
                                    scheduleDate = outputFormat.format(date);
                                    Log.d("scheduleDate", scheduleDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else if (name.equals("cf_996")) {
                                supportrequired = String.valueOf(values);
                                supportrequired_list.add(supportrequired);
                            } else if (name.equals("cf_897")) {
                                winprob = String.valueOf(values);
                                winprob_list.add(winprob);
                            } else if (name.equals("modifiedtime")) {
                                modifydatetime = String.valueOf(values);
                                inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                outputFormat = new SimpleDateFormat("yyy-MM-dd");
                                Date date = null;
                                try {
                                    date = inputFormat.parse(modifydatetime);
                                    modifiedtime = outputFormat.format(date);
                                    modifiedtime_list.add(modifiedtime);
                                    Log.d("modifiedtime_list", String.valueOf(modifiedtime_list));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    PreferenceManagerMyOpportunity.getInstance(requireContext()).setMultipleDataOpportunity(scheduleDates, salesstage, supportrequired, winprob, modality, modifiedtime);
                }
                MyOpportunityModel opportunityModel = new MyOpportunityModel(scheduleDates, salesstage, supportrequired, winprob, modality, modifiedtime);
                localModifiedDate = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    localModifiedDate = LocalDate.parse(opportunityModel.getModifiedtime());
                    Log.d("localModifiedDate", String.valueOf(localModifiedDate));
                    LocalDate localDate = LocalDate.now();
                    firstDayOfQuarter = localDate.with(localDate.getMonth().firstMonthOfQuarter())
                            .with(TemporalAdjusters.firstDayOfMonth());
                    Log.d("firstDayOfQuarter", String.valueOf(firstDayOfQuarter));

                    lastDayOfQuarter = firstDayOfQuarter.plusMonths(2)
                            .with(TemporalAdjusters.lastDayOfMonth());
                    Log.d("lastDayOfQuarter", String.valueOf(lastDayOfQuarter));

                    if (!firstDayOfQuarter.isAfter(localModifiedDate)
                            && !lastDayOfQuarter.isBefore(localModifiedDate)) {

                        Log.d("size", String.valueOf(firstDayOfQuarter.isAfter(firstDayOfQuarter)));

                        Count_opportunity = Collections.frequency(salesstage_list, "Opportunity");
                        System.out.println("Count_opportunity:  " + Count_opportunity);
                        Count_proposal = Collections.frequency(salesstage_list, "Proposal or Price Quote");
                        System.out.println("Count of In Complete is:  " + Count_Incomplete);

                        Count_negotiation = Collections.frequency(salesstage_list, "Negotiation or Review");
                        Count_won = Collections.frequency(salesstage_list, "Closed Won");
                        Count_lost = Collections.frequency(salesstage_list, "Closed Lost");
                        System.out.println("Count of Completed is:  " + Complted_Count);


                        String[] valueArray = new String[]{"Opportunity", "Proposal or Price Quote", "Negotiation or Review", "Closed Won", "Closed Lost"};
//                                 // Values for pie chart
                        ArrayList<PieEntry> yVals1 = new ArrayList<>();
                        yVals1.add(new PieEntry(Count_opportunity, valueArray[0 % valueArray.length].concat("                      ").concat(":".concat(" ").concat(String.valueOf(Count_opportunity)))));
                        yVals1.add(new PieEntry(Count_proposal, valueArray[1 % valueArray.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_proposal)))));
                        yVals1.add(new PieEntry(Count_negotiation, valueArray[2 % valueArray.length].concat("    ").concat(":".concat(" ").concat(String.valueOf(Count_negotiation)))));
                        yVals1.add(new PieEntry(Count_won, valueArray[3 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_won)))));
                        yVals1.add(new PieEntry(Count_lost, valueArray[4 % valueArray.length].concat("                     ").concat(":".concat(" ").concat(String.valueOf(Count_lost)))));


                        PieDataSet dataSet_sales = new PieDataSet(yVals1, "");
                        dataSet_sales.setDrawIcons(false);

                        dataSet_sales.setIconsOffset(new MPPointF(0, 40));
                        dataSet_sales.setSliceSpace(2f);
                        dataSet_sales.setSelectionShift(5f);
                        endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);
                        endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
                        endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
                        endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);


                        dataSet_sales.setColors(endColor1, endColor2, endColor3, endColor4, endColor5);
                        PieData data1 = new PieData(dataSet_sales);
                        data1.setValueFormatter(new PercentFormatter(chart_sales));
                        data1.setValueTextSize(12f);
                        data1.setValueTextColor(Color.WHITE);
                        data1.setValueTypeface(tfLight);
                        chart_sales.setData(data1);
                        chart_sales.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart_sales.highlightValues(null);
                        //Sales stage ends

                        //support required starts here
                        if (opportunityModel.getSupport_required().equals("No Support Required")) {
                            noSupport_required = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Travel Support")) {
                            travel_support = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Funding Required")) {
                            funding_req = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Demo Required")) {
                            demo_required = opportunityModel.getSupport_required();

                        } else if (opportunityModel.getSupport_required().equals("Price Support")) {
                            price_support = opportunityModel.getSupport_required();
                        } else if (opportunityModel.getSupport_required().equals("Product Validation Support")) {
                            product_validation = opportunityModel.getSupport_required();

                        }
                        Count_NoSupportRequired = Collections.frequency(supportrequired_list, "No Support Required");
                        Count_Travel = Collections.frequency(supportrequired_list, "Travel Support");
                        Count_Funding = Collections.frequency(supportrequired_list, "Funding Required");
                        Count_Demo = Collections.frequency(supportrequired_list, "Demo Required");
                        Count_Price = Collections.frequency(supportrequired_list, "Price Support");
                        Count_Product = Collections.frequency(supportrequired_list, "Product Validation Support");


                        final ArrayList<String> arrayList_support = new ArrayList<>();
                        arrayList_support.add(noSupport_required);
                        arrayList_support.add(travel_support);
                        arrayList_support.add(funding_req);
                        arrayList_support.add(demo_required);
                        arrayList_support.add(price_support);
                        arrayList_support.add(product_validation);
                        xAxis_support.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList_support.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList_support);
                                String[] supportList = valueStr.replace("[", "").replace("]", "").split(",");
                                return supportList[(int) value];
                            }
                        });
                        xAxis_support.setGranularity(1);
                        xAxis_support.setTextSize(10f);
                        xAxis_support.setLabelRotationAngle(-90f);
                        xAxis_support.setLabelCount(arrayList_support.size());
                        xAxis_support.setGranularityEnabled(true);



//                                    String[] mStringArray = new String[outcome_list.size()];
//                                    mStringArray = outcome_list.toArray(mStringArray);
//                                    System.out.println(mStringArray);

                        ArrayList<BarEntry> values_support = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {
                        values_support.add(new BarEntry(0, Count_NoSupportRequired));
                        values_support.add(new BarEntry(1, Count_Travel));
                        values_support.add(new BarEntry(2, Count_Funding));
                        values_support.add(new BarEntry(3, Count_Demo));
                        values_support.add(new BarEntry(4, Count_Price));
                        values_support.add(new BarEntry(5, Count_Product));
                        //    }


                        BarDataSet support_set;
                        if (chart_support.getData() != null &&
                                chart_support.getData().getDataSetCount() > 0) {
                            support_set = (BarDataSet) chart_support.getData().getDataSetByIndex(0);

                            support_set.setValues(values_support);
                            chart_support.getData().notifyDataChanged();


                        } else {

                            support_set = new BarDataSet(values_support, "Support Required");


                            endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            support_set.setColors(endColor1);
                            support_set.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(support_set);


                            MyDecimalValueFormatter formatter1 = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter1);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart_support.setData(data);
                        }
                        //Support Required end here

                        //Win Prob Starts here
                        Count_30 = Collections.frequency(winprob_list, "30 %");
                         System.out.println("Count of 30% is:  "+ Count_30);
                        Count_50 = Collections.frequency(winprob_list, "50 %");
                        //System.out.println("Count of In Complete is:  "+ Count_Incomplete);

                        Count_80 = Collections.frequency(winprob_list, "80 %");
                        // System.out.println("Count of Completed is:  "+ Complted_Count);

                        String[] winprobs = new String[]{"30%", "50%", "80%"};//                                 // Values for pie chart
                        ArrayList<PieEntry> yVals_winprob = new ArrayList<>();

                        yVals_winprob.add(new PieEntry(Count_30, winprobs[0 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_30)))));
                        //System.out.println("Count_Incomplete:  "+ Count_Incomplete);
                        yVals_winprob.add(new PieEntry(Count_50, winprobs[1 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_50)))));
                        yVals_winprob.add(new PieEntry(Count_80, winprobs[2 % winprobs.length].concat(" ").concat(":".concat(" ").concat(String.valueOf(Count_80)))));


                        PieDataSet dataSet_winprob = new PieDataSet(yVals_winprob, "");
                        dataSet_winprob.setDrawIcons(false);

                        dataSet_winprob.setIconsOffset(new MPPointF(0, 40));
                        dataSet_winprob.setSliceSpace(2f);
                        dataSet_winprob.setSelectionShift(5f);
                        dataSet_winprob.setColors(endColor5, endColor3, endColor4);
                        PieData data_winprob = new PieData(dataSet_winprob);
                        data_winprob.setValueFormatter(new PercentFormatter(chart_winprob));
                        data_winprob.setValueTextSize(12f);
                        data_winprob.setValueTextColor(Color.WHITE);
                        data_winprob.setValueTypeface(tfLight);
                        chart_winprob.setData(data_winprob);
                        chart_winprob.drawSliceTextEnabled = false;
                        // undo all highlights
                        chart_winprob.highlightValues(null);
                        //Winprob ends here


                        //Modality starts here
                        if (opportunityModel.getModality().equals("IVD")) {
                            modality_ivd = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("WHS")) {
                            modality_whs = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("Ultrasound")) {
                            modality_ultrasound = opportunityModel.getModality();
                        } else if (opportunityModel.getModality().equals("LCS")) {
                            modality_lcs = opportunityModel.getModality();

                        } else if (opportunityModel.getModality().equals("NBC")) {
                            modality_nbc = opportunityModel.getModality();
                        }
                        Count_IVD = Collections.frequency(modality_list, "IVD");
                        Count_WHS = Collections.frequency(modality_list, "WHS");
                        Count_UltraSound = Collections.frequency(modality_list, "Ultrasound");
                        Count_LCS = Collections.frequency(modality_list, "LCS");
                        Count_NBC = Collections.frequency(modality_list, "NBC");


                        final ArrayList<String> arrayList_modality = new ArrayList<String>();
                        arrayList_modality.add(modality_ivd);
                        arrayList_modality.add(modality_whs);
                        arrayList_modality.add(modality_ultrasound);
                        arrayList_modality.add(modality_lcs);
                        arrayList_modality.add(modality_nbc);
                        xAxis_modality.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {

                                if (value < 0 || value > arrayList_modality.size() - 1) {
                                    return "";
                                }
                                String valueStr = String.valueOf(arrayList_modality);
                                String[] modalityList = valueStr.replace("[", "").replace("]", "").split(",");
                                return modalityList[(int) value];
                            }
                        });
                        xAxis_modality.setGranularity(1);
                        xAxis_modality.setTextSize(10f);
                        xAxis_modality.setLabelRotationAngle(-90f);
                        xAxis_modality.setLabelCount(arrayList_modality.size());
                        xAxis_modality.setGranularityEnabled(true);
                        ArrayList<BarEntry> valuesModality = new ArrayList<>();
                        //     if (scheduleDate.equals("11-01-2020")) {

                        valuesModality.add(new BarEntry(0, Count_IVD));
                        valuesModality.add(new BarEntry(1, Count_WHS));
                        valuesModality.add(new BarEntry(2, Count_UltraSound));
                        valuesModality.add(new BarEntry(3, Count_LCS));
                        valuesModality.add(new BarEntry(4, Count_NBC));

                        //    }


                        BarDataSet set_modality;
                        if (chart_modality.getData() != null &&
                                chart_modality.getData().getDataSetCount() > 0) {
                            set_modality = (BarDataSet) chart_modality.getData().getDataSetByIndex(0);

                            set_modality.setValues(valuesModality);
                            chart_modality.getData().notifyDataChanged();


                        } else {

                            set_modality = new BarDataSet(valuesModality, "Modality");


                            endColor1 = ContextCompat.getColor(getContext(), R.color.linecolor);
                            set_modality.setColors(endColor1);
                            set_modality.setDrawIcons(false);
                            dataSets = new ArrayList<>();
                            dataSets.add(set_modality);


                            MyDecimalValueFormatter formatter2 = new MyDecimalValueFormatter();
                            BarData data = new BarData(dataSets);
                            data.setValueTextSize(12f);
                            data.setValueFormatter(formatter2);
                            data.setValueTypeface(tfLight);
                            data.setBarWidth(barWidth);
                            chart_modality.setData(data);
                        }
                        //Modality end here


                    } else {

                        //status
                        chart_sales.setNoDataText("No Data Available For The Selected Period");
                        chart_sales.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_sales.setNoDataTextTypeface(tfLight);
                        int endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
                        chart_sales.setNoDataTextColor(endColor3);

                        chart_support.setNoDataText("No Data Available For The Selected Period");
                        chart_support.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_support.setNoDataTextTypeface(tfLight);
                        chart_support.setNoDataTextColor(endColor3);

                        chart_winprob.setNoDataText("No Data Available For The Selected Period");
                        chart_winprob.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_winprob.setNoDataTextTypeface(tfLight);
                        chart_winprob.setNoDataTextColor(endColor3);

                        chart_modality.setNoDataText("No Data Available For The Selected Period");
                        chart_modality.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        chart_modality.setNoDataTextTypeface(tfLight);
                        chart_modality.setNoDataTextColor(endColor3);
                    }
                }

            }

        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.refresh:
                salesstage_list.clear();
                chart_sales.clear();
                modality_list.clear();
                chart_modality.clear();
                fetchJSONOpportunity();
                return true;
            default:
                getActivity().onBackPressed();
                return super.onOptionsItemSelected(item);
        }

    }

}


