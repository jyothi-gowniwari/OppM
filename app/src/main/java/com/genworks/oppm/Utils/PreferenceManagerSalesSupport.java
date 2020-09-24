package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerSalesSupport {

    private static PreferenceManagerSalesSupport jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private PreferenceManagerSalesSupport(Context context) {
        prefs = context.getSharedPreferences("Genworks_salessupport", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized PreferenceManagerSalesSupport getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManagerSalesSupport(context);
            return jInstance;
        }
    }

    public void setAPIResponseSales(String res){
        editor.putString("response",res);
        editor.apply();
    }
    public String getAPIResponseSales(){
        return prefs.getString("response","");
    }


    public void setSupportMultipleData(String potentialValue, String related, String assigned, String contact, String location, String productValue, String valueAndQty, String potentialNo, String mobie, String competition_name, String circle, String closingdate, String comment, String createdtime, String demo_date, String demo_done, String department, String description, String discount_amount, String equ_details, String employee, String exp_delivery_date, String fun_req, String general_remark, String hdnS_H_Amount, String hdnSubTotal, String interest_type, String lost_reason, String leadsource, String listprices, String modality, String modifiedby, String modifiedtime, String opportunity_type, String pre_tax_total, String pndt, String price_quoted, String prospect_type, String rating, String remark_sales_person, String sale_type, String support_type, String s_i, String segment, String site_read, String txt_adj, String tax1, String tax2, String tax3, String termsnconditions, String winprobValue, String support_person, String qty, String salesstageValue, String title,String pdf_link) {
        editor.putString("potentialValue", potentialValue);
        editor.putString("related", related);
        editor.putString("assigned", assigned);
        editor.putString("contact", contact);
        editor.putString("location", location);
        editor.putString("productValue", productValue);
        editor.putString("valueAndQty", valueAndQty);
        editor.putString("potentialNo", potentialNo);
        editor.putString("mobie", mobie);
        editor.putString("competition_name", competition_name);
        editor.putString("circle", circle);
        editor.putString("closingdate", closingdate);
        editor.putString("comment", comment);
        editor.putString("createdtime", createdtime);
        editor.putString("demo_date", demo_date);
        editor.putString("demo_done", demo_done);
        editor.putString("department", department);
        editor.putString("description", description);
        editor.putString("discount_amount", discount_amount);
        editor.putString("equ_details", equ_details);
        editor.putString("employee", employee);
        editor.putString("exp_delivery_date", exp_delivery_date);
        editor.putString("fun_req", fun_req);
        editor.putString("general_remark", general_remark);
        editor.putString("hdnS_H_Amount", hdnS_H_Amount);
        editor.putString("hdnSubTotal", hdnSubTotal);
        editor.putString("interest_type", interest_type);
        editor.putString("lost_reason", lost_reason);
        editor.putString("leadsource", leadsource);
        editor.putString("listprices", listprices);
        editor.putString("modality", modality);
        editor.putString("modifiedby", modifiedby);
        editor.putString("modifiedtime", modifiedtime);
        editor.putString("opportunity_type", opportunity_type);
        editor.putString("pre_tax_total", pre_tax_total);
        editor.putString("pndt", pndt);
        editor.putString("price_quoted", price_quoted);
        editor.putString("prospect_type", prospect_type);
        editor.putString("rating", rating);
        editor.putString("remark_sales_person", remark_sales_person);
        editor.putString("sale_type", sale_type);
        editor.putString("support_type", support_type);
        editor.putString("s_i", s_i);
        editor.putString("segment", segment);
        editor.putString("site_read", site_read);
        editor.putString("txt_adj", txt_adj);
        editor.putString("tax1", tax1);
        editor.putString("tax2", tax2);
        editor.putString("tax3", tax3);
        editor.putString("termsnconditions", termsnconditions);
        editor.putString("winprobValue", winprobValue);
        editor.putString("support_person", support_person);
        editor.putString("qty", qty);
        editor.putString("salesstageValue", salesstageValue);
        editor.putString("title", title);
        editor.putString("pdf_link", pdf_link);
        editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }
    public String getTitle(){
        return prefs.getString("title","");
    }
    public String getSalesstageValue(){
        return prefs.getString("salesstageValue","");
    }
    public String getSupport_person(){
        return prefs.getString("support_person","");
    }
    public String getQty(){
        return prefs.getString("qty","");
    }
    public String getWinprobValue(){
        return prefs.getString("winprobValue","");
    }
    public String getTermsnconditions(){
        return prefs.getString("termsnconditions","");
    }
    public String getTax3(){
        return prefs.getString("tax3","");
    }
    public String getTax2(){
        return prefs.getString("tax2","");
    }
    public String getTax1(){
        return prefs.getString("tax1","");
    }
    public String getTxt_adj(){
        return prefs.getString("txt_adj","");
    }
    public String getSite_read(){
        return prefs.getString("site_read","");
    }
    public String getSegment(){
        return prefs.getString("segment","");
    }
    public String getS_i(){
        return prefs.getString("s_i","");
    }
    public String getSupport_type(){
        return prefs.getString("support_type","");
    }
    public String getSale_type(){
        return prefs.getString("sale_type","");
    }
    public String getRemark_sales_person(){
        return prefs.getString("remark_sales_person","");
    }
    public String getRating(){
        return prefs.getString("rating","");
    }
    public String getProspect_type(){
        return prefs.getString("prospect_type","");
    }
    public String getPrice_quoted(){
        return prefs.getString("price_quoted","");
    }
    public String getPndt(){
        return prefs.getString("pndt","");
    }
    public String getPre_tax_total(){
        return prefs.getString("pre_tax_total","");
    }
    public String getOpportunity_type(){
        return prefs.getString("opportunity_type","");
    }public String getModifiedtime(){
        return prefs.getString("modifiedtime","");
    }public String getModifiedby(){
        return prefs.getString("modifiedby","");
    }
    public String getModality(){
        return prefs.getString("modality","");
    }public String getListprice(){
        return prefs.getString("listprice","");
    }public String getLeadsource(){
        return prefs.getString("leadsource","");
    }public String getLost_reason(){
        return prefs.getString("lost_reason","");
    }public String getInterest_type(){
        return prefs.getString("interest_type","");
    }public String getHdnSubTotal(){
        return prefs.getString("hdnSubTotal","");
    }public String getHdnS_H_Amount(){
        return prefs.getString("hdnS_H_Amount","");
    }public String getGeneral_remark(){
        return prefs.getString("general_remark","");
    }
    public String getFun_req(){
        return prefs.getString("fun_req","");
    }public String getExp_delivery_date(){
        return prefs.getString("exp_delivery_date","");
    }public String getEmployee(){
        return prefs.getString("employee","");
    }public String getEqu_details(){
        return prefs.getString("equ_details","");
    }public String getDiscount_amount(){
        return prefs.getString("discount_amount","");
    }public String getDescription(){
        return prefs.getString("description","");
    }
    public String getDepartment(){
        return prefs.getString("department","");
    }
    public String getDemo_done(){
        return prefs.getString("demo_done","");
    }
    public String getDemo_date(){
        return prefs.getString("demo_date","");
    }
    public String getCreatedtime(){
        return prefs.getString("createdtime","");
    }
    public String getComment(){
        return prefs.getString("comment","");
    }public String getClosingdate(){
        return prefs.getString("closingdate","");
    }public String getCircle(){
        return prefs.getString("circle","");
    }public String getCompetition_name(){
        return prefs.getString("competition_name","");
    }public String getMobie(){
        return prefs.getString("mobie","");
    }public String getPotentialNo(){
        return prefs.getString("potentialNo","");
    }public String getValueAndQty(){
        return prefs.getString("valueAndQty","");
    }public String getProductValue(){
        return prefs.getString("productValue","");
    }

    public String getContact(){
        return prefs.getString("contact","");
    }

    public String getRelated(){
        return prefs.getString("related","");
    }
    public String getPotentialValue(){
        return prefs.getString("potentialValue","");
    }




}
