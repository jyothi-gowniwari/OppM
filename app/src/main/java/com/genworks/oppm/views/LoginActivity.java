package com.genworks.oppm.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.genworks.oppm.model.Error;
import com.genworks.oppm.model.GetLoginListDetails;
import com.genworks.oppm.model.Login;
import com.genworks.oppm.model.LoginAndFetchModules;
import com.genworks.oppm.model.LoginListForModules;
import com.genworks.oppm.model.LoginUser;
import com.genworks.oppm.model.Records;
import com.genworks.oppm.model.Results;
import com.genworks.oppm.R;
import com.genworks.oppm.Utils.PreferenceUtils;
import com.genworks.oppm.Utils.SharedPreferenceClass;
import com.genworks.oppm.remote.APIService;
import com.genworks.oppm.remote.RetroClass;
import com.genworks.oppm.sql.DatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText username1, password1;
    Button login;
    String sessionId;
    String username,password;
    Intent i;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedPrefs;
    SharedPreferences.Editor editor;
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9-]";
    private Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    private Matcher matcher;
    AlertDialog.Builder builder;
    private Records records2;
    private GifImageView loader;
    private ProgressDialog progressDialog = null;
    private ProgressBar llProgressBar;
    ImageView facebook, linkedin, logo;
    private ArrayList<Records> records;
    private final Handler handler = new Handler();
    private ArrayList<Records> recordsList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    private String module_id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_login_new);

        username1 = (EditText) findViewById(R.id.editTextUserName);



        loader = findViewById(R.id.loader);


        password1 = (EditText) findViewById(R.id.editTextPassword);


        facebook = (ImageView) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.facebook.com/Genworkshealth/inbox/"));
                startActivity(viewIntent);
            }
        });
        linkedin = (ImageView) findViewById(R.id.linkedin);
        linkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://www.linkedin.com/company/genworks-health/"));
                startActivity(viewIntent);
            }
        });
        logo = (ImageView) findViewById(R.id.gen_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://genworkshealth.com"));
                startActivity(viewIntent);
            }
        });
        //fetchUserJSON();
        login = (Button) findViewById(R.id.login);
        login.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(login.getWindowToken(), 0);
                return false;
            }
        });
        databaseHelper=new DatabaseHelper(LoginActivity.this);
        PreferenceUtils utils = new PreferenceUtils();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isNetworkConnectionAvailable();

            }
        });
    }



    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
           // Log.d("Network", "Connected");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Write code for your refresh logic

                   username = username1.getText().toString();
                   password = password1.getText().toString();

                    if (username.matches("")) {
                        username1.setError("Please Enter Username");
                    } else if (password.matches("")) {
                        password1.setError("Please Enter Password");
                    } else {
                      //  username1.setErrorEnabled(false);
                        //password.setErrorEnabled(false);



                        progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setIndeterminate(true);
                        progressDialog.setMessage("Loading...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.setCancelable(false);
                        progressDialog.show();

                        // loader.setVisibility(View.VISIBLE);
                        //   llProgressBar = new ProgressBar(LoginActivity.this);
                        // llProgressBar.setVisibility(View.VISIBLE);

                        String operation = "login";
                        final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                        /** Call the method with parameter in the interface to get the notice data*/
                        Call<Login> call = service.GetLoginList(operation, username, password);

                        /**Log the URL called*/
                        Log.wtf("URL Called", call.request().url() + "");

                        call.enqueue(new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {

                                Log.e("response", new Gson().toJson(response.body()));
                                if (response.isSuccessful()) {
                                    Log.e("response", new Gson().toJson(response.body()));

                                    Login login = response.body();

                                    String success = login.getSuccess();
                                    Error error = login.getError();

                                    if (success.equals("true")) {


                                        String result = login.getResult().toString();
                                        Log.i("result", "result" + result);
                                        String login_name = String.valueOf(login.getResult().getLogin());
                                        Log.i("login_name", ":" + login_name);
                                        String userid = String.valueOf(login.getResult().getLogin().getUserid());
                                        Log.i("userid", ":" + userid);
                                        String session = String.valueOf(login.getResult().getLogin().getSession());
                                        Log.i("session", ":" + session);
                                        String vtiger_version = String.valueOf(login.getResult().getLogin().getVtiger_version());
                                        Log.i("vtiger_version", ":" + vtiger_version);
                                        String mobile_module_version = String.valueOf(login.getResult().getLogin().getMobile_module_version());
                                        Log.i("mobile_module_version", ":" + mobile_module_version);
                                        String operation = "loginAndFetchModules";
                                        final APIService service = RetroClass.getRetrofitInstance().create(APIService.class);

                                        /** Call the method with parameter in the interface to get the notice data*/
                                        Call<LoginAndFetchModules> call1 = service.GetLoginModuleList(operation, username, password);

                                        /**Log the URL called*/
                                        Log.wtf("URL Called", call1.request().url() + "");

                                        call1.enqueue(new Callback<LoginAndFetchModules>() {
                                            @Override
                                            public void onResponse(Call<LoginAndFetchModules> call1, Response<LoginAndFetchModules> response) {

                                                Log.e("response", new Gson().toJson(response.body()));
                                                if (response.isSuccessful()) {

                                                    Log.e("response", new Gson().toJson(response.body()));

                                                    LoginAndFetchModules loginAndFetchModules = response.body();
                                                    String success = loginAndFetchModules.getSuccess();
                                                    Log.d("success", success);


                                                    if (success.equals("true")) {
                                                        Results results = loginAndFetchModules.getResult();


                                                        //parse login details
                                                        GetLoginListDetails loginDetails = results.getLogin();
                                                        String userId = loginDetails.getUserid();
                                                        String sessionId = loginDetails.getSession();
                                                        String firstname = loginDetails.getFirst_name();
                                                        String lastname = loginDetails.getLast_name();
                                                        String mobile = loginDetails.getMobile();
                                                        String role = loginDetails.getRole();
                                                        String reportto = loginDetails.getReportto();

                                                        //parse modules
                                                        ArrayList<LoginListForModules> modules = results.getModules();

                                                        //parse module information
                                                        for (LoginListForModules module : modules) {
                                                            module_id = module.getId();
                                                            String name = module.getName();
                                                            String isEntity = module.getIsEntity();
                                                            String label = module.getLabel();
                                                            String singular = module.getSingular();
                                                        }


                                                        if (username.equals(username1.getText().toString()) && password.equals(password1.getText().toString())) {
                                                            LoginUser user = new LoginUser();
                                                            user.setUsername(username);
                                                            user.setPassword(password);

                                                            DatabaseHelper databaseHelper = new DatabaseHelper(LoginActivity.this);
                                                            databaseHelper.addUser(user);

                                                            SharedPreferenceClass sharedOBJ = new SharedPreferenceClass(LoginActivity.this);
                                                            sharedOBJ.setLoginStatus("SUCCESS");
                                                            sharedOBJ.setLoginSession(sessionId);
                                                            sharedOBJ.setKeyLoginFirstname(firstname);
                                                            sharedOBJ.setKeyLoginLastname(lastname);
                                                            sharedOBJ.setKeyLoginMobile(mobile);
                                                            sharedOBJ.setKeyLoginRole(role);
                                                            sharedOBJ.setKeyLoginReportto(reportto);
                                                            sharedOBJ.setKeyLoginUsername(username);

                                                            Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                                                            i = new Intent(LoginActivity.this, MainActivity.class);
                                                            // loader.setVisibility(View.GONE);

                                                            //  llProgressBar.setVisibility(View.GONE);
                                                            i.putExtra("sessionId", sessionId);
                                                            i.putExtra("module_id", module_id);
                                                            i.putExtra("username", username);
                                                            i.putExtra("firstname", firstname);
                                                            i.putExtra("lastname", lastname);
                                                            i.putExtra("mobile", mobile);
                                                            i.putExtra("role", role);
                                                            i.putExtra("reportto", reportto);
                                                            startActivity(i);
                                                            finish();
                                                            progressDialog.dismiss();
                                                        }


                                                        //  progressDialog.dismiss();
                                                    }
                                                }
                                                }
                                         //   }

                                            @Override
                                            public void onFailure(Call<LoginAndFetchModules> call1, Throwable t) {
                                                Log.d("error", t.getMessage());
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }else if(success.equals("false")){

                                        String message=error.getMessage();
                                        simpleAlert(message);
                                      //  Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }

                                }

                            }


                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Log.d("error", t.getMessage());
                                progressDialog.dismiss();
                                //  llProgressBar.setVisibility(View.GONE);
                            }
                        });
                    }

                }


            }, 0);
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }

    }

    public void simpleAlert(String message) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Login Failed");
        builder.setMessage(message);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(false);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),
                        android.R.string.no, Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    public boolean validateUsername(String username) {
        matcher = pattern.matcher(username);
        return matcher.matches();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(LogoutService.timer!=null) {
            LogoutService.timer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(LogoutService.timer!=null){

            LogoutService.timer.cancel();
        }
    }


    public boolean validatePassword(String password) {
        return password.length() > 5;
    }
}

