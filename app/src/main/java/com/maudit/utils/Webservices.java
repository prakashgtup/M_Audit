package com.maudit.utils;

import android.content.Context;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 */
public class Webservices {
    private Context mContext;
    public final String URL="http://54.169.8.57/internalaudit/internalauditWS.asmx";
    private  String NAMESPACE="http://tempuri.org/";
    private String android_deviceid ="";
    public final String IMAGE_URL = "http://54.169.8.57/internalaudit";
    public Webservices(Context context){
        mContext=context;
        android_deviceid = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public  SoapObject mVerficationLogin(String adid,String password,int roleId ){
        String soapAction="http://tempuri.org/VerifyLogin";
        String methodName="VerifyLogin";
        SoapObject mSoapLoginResult=null;
        try{
            SoapObject mSoapObject=new SoapObject(NAMESPACE,methodName);
        // Set the property info for the to ADID
            PropertyInfo infoADID = new PropertyInfo();
            infoADID.setName("strADID");
            infoADID.setValue(adid);
            infoADID.setType(String.class);
            mSoapObject.addProperty(infoADID);
            mSoapObject.addProperty("strADID", adid);
            mSoapObject.addProperty("strEncPassword", password);
            Log.i("strEncPassword-->", "" + password);
            mSoapObject.addProperty("intRoleId", roleId);
            // Create the envelop.Envelop will be used to send the request
            SoapSerializationEnvelope mSoapEnvelop = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11);   // Says that the soap webservice is a .Net service
            mSoapEnvelop.dotNet = true;
            mSoapEnvelop.setOutputSoapObject(mSoapObject);
            HttpTransportSE mResponse = new HttpTransportSE(URL);
            mResponse.call(soapAction, mSoapEnvelop);
            SoapObject mSoapResponse = (SoapObject)mSoapEnvelop.getResponse();
            mSoapLoginResult=(SoapObject)mSoapResponse.getProperty(0);
            Log.i("response", mSoapLoginResult.toString());
        }catch (Exception e){
            Log.i("exception", e.toString());
            e.printStackTrace();
        }
        return mSoapLoginResult;
    }
    //Get AllScheduledChecklist details
    public  String mGetAllScheduledCheckLists(String userId) {
        String result="";
        try{
        StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
        localHttpURLConnection.setRequestMethod("POST");
        localHttpURLConnection.setRequestProperty("Content-type",
                "text/xml; charset=UTF-8");
        localHttpURLConnection.setRequestProperty("Accept",
                "*");
        localHttpURLConnection.setRequestProperty("SOAPAction",
                "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

        localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body> \n" +
                            "<GetDataSetInXMLFromStoredProcedure xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>\n" +
                            "<UserName></UserName>\n" +
                            "<Password></Password> \n" +
                            "<DeviceID>"+android_deviceid+"</DeviceID> \n" +
                            "<ProcedureName>mob_GetAllScheduledCheckLists</ProcedureName> \n" +
                            "<Parameters>\n" +
                            "<string><![CDATA[@UserID~~"+userId+"]]></string>\n" +
                            "</Parameters>\n" +
                            "</GetDataSetInXMLFromStoredProcedure>\n" +
                            "</soap:Body> \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get AllSubmittedChecklists details
    public  String mGetAllSubmittedChecklists(String userId) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body> \n" +
                            "<GetDataSetInXMLFromStoredProcedure xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>\n" +
                            "<UserName></UserName>\n" +
                            "<Password></Password> \n" +
                            "<DeviceID>"+android_deviceid+"</DeviceID> \n" +
                            "<ProcedureName>mob_GetAllSubmittedChecklists</ProcedureName> \n" +
                            "<Parameters>\n" +
                            "<string><![CDATA[@UserID~~"+userId+"]]></string>\n" +
                            "</Parameters>\n" +
                            "</GetDataSetInXMLFromStoredProcedure>\n" +
                            "</soap:Body> \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }
    //Get Checklist details based on selected ScheduleId
    public  String mGetScetionList(String scheduleId,String userId) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetChecklist");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "  <soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                            "  <soap:Body>\n" +
                            "  <GetChecklist xmlns=\"http://tempuri.org/\">\n" +
                            "  <iScheduleID>"+scheduleId+"</iScheduleID>\n" +
                            "  <iAuditorID>"+userId+"</iAuditorID>\n" +
                            "  </GetChecklist>\n" +
                            "  </soap:Body>\n" +
                            "  </soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" +  " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Reassign Sections to Auditors Details
    public  String mReassignSection(String LeadAuditorID,String AuditScheduleID, String UserSection) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope                            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>                            \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_ReassignSection</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@LeadAuditorID~~"+LeadAuditorID+"]]></string>\n" +
                            "<string><![CDATA[@AuditScheduleID~~"+AuditScheduleID+"]]></string>\n" +
                            "<string><![CDATA[@UserSectionXml~~<ReassignSection>                            \n" +
                            UserSection+
                            "</ReassignSection>]]></string>                            \n" +
                            "</Parameters>                            \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get initials call to get required details for reassign section
    public  String mGetAuditorsAndAssignedSections(String AuditScheduleID,String userID) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>     \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_GetAuditorsAndAssignedSections</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@AuditScheduleID~~"+AuditScheduleID+"]]></string>                              \n" +
                            "<string><![CDATA[@LoggedInUser~~"+userID+"]]></string>                              \n" +
                            "</Parameters>                           \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get AllAdhoc Auditorlist details
    public  String mGetAllAdhocList(String userId) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>     \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_GetDataForAdhocSchedule</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@UserID~~"+userId+"]]></string>                              \n" +
                            "</Parameters>                           \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Add Adhoc Audit Details
    public  String mAddAdhocSchedule(Integer AuditTypeID,Integer CheckListTypeID, Integer LocationID,Integer UserID, String UserSection, String Auditee,String PatientCaseNo) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/AddAdhocSchedule");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                            "<soap:Body>\n" +
                            "<AddAdhocSchedule xmlns=\"http://tempuri.org/\"> \n" +
                            "<strScheduleXml>\n" +
                            "<![CDATA[<CreateAdhocSchedule>\n" +
                            "<AdhocSchedule>\n" +
                            "<AuditTypeID>"+AuditTypeID+"</AuditTypeID>\n" +
                            "<ChecklistTypeID>"+CheckListTypeID+"</ChecklistTypeID>\n" +
                            "<LocationID>"+LocationID+"</LocationID>\n" +
                            "<Auditee>"+Auditee+"</Auditee>\n" +
                            "<PatientCaseNo>"+PatientCaseNo+"</PatientCaseNo>\n" +
                            "<LoggedInUserID>"+UserID+"</LoggedInUserID>\n" +
                            "</AdhocSchedule>\n" +
                            UserSection+
                            "</CreateAdhocSchedule>]]>\n" +
                            "</strScheduleXml>\n" +
                            "</AddAdhocSchedule>\n" +
                            "</soap:Body>\n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get Auditors and Selection list details
    public  String mGetAuditorsAndSectionList(String AuditTypeID,String CheckListTypeID) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope     xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>     \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_GetAuditorsAndSection</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@AuditTypeID~~"+AuditTypeID+"]]></string>                              \n" +
                            "<string><![CDATA[@CheckListTypeID~~"+CheckListTypeID+"]]></string>                              \n" +
                            "</Parameters>                           \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;

            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" + " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get Checklist details based on selected ScheduleId
    public  String mSubmitCheckList(int auditScheduleId,int userId,int isSubmit,String questionsAnswers) {
        Log.i("String value--->",questionsAnswers);
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/SubmitChecklist");

            localHttpURLConnection.setDoOutput(true);
            String request="";
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                            "<soap:Body>\n" +
                            "<SubmitChecklist xmlns=\"http://tempuri.org/\">\n" +
                            "<strXmlChecklist><![CDATA[<SubmitChecklist><CheckList>         \n" +
                            "         <AuditScheduleID>"+auditScheduleId+"</AuditScheduleID>         \n" +
                            "         <LoggedInUserID>"+userId+"</LoggedInUserID>        \n" +
                            "          <IsLeadAuditor>true</IsLeadAuditor>\n" +
                            "</CheckList>"+questionsAnswers+
                            "           </SubmitChecklist>]]>\n" +
                            "            </strXmlChecklist>\n" +
                            "            <IsSubmit>"+isSubmit+"</IsSubmit>  \n" +
                            "            </SubmitChecklist>\n" +
                            "            </soap:Body>\n" +
                            "            </soap:Envelope>");

            Log.d("request", request_builder.toString());
            appendLog(request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;
            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" +  " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    public void appendLog(String text)
    {File externalStorageDir = Environment.getExternalStorageDirectory();
        File logFile = new File(externalStorageDir,"log.file");
        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //for Accept or Reject checklist
    public  String mRejectChecklist(int userId,int auditScheduleID,int statusID,String rejectionReason) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/RejectChecklist");

            localHttpURLConnection.setDoOutput(true);
            String request="";
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                            "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" \n" +
                            "xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                            "<soap:Body>\n" +
                            "<RejectChecklist xmlns=\"http://tempuri.org/\">\n" +
                            "<AuditScheduleID>"+auditScheduleID+"</AuditScheduleID>\n" +
                            "<StatusID>"+statusID+"</StatusID>\n" +
                            "<RejectionReason>"+rejectionReason+"</RejectionReason>\n" +
                            "<UserId>"+userId+"</UserId>\n" +
                            "</RejectChecklist>\n" +
                            "</soap:Body>\n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;
            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" +  " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Get Other Remarks details based on selected ScheduleId
    public  String mGetOtherRemarks(int auditScheduleId) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            String request="";
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope                            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>                            \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_GetAuditorComment</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@ScheduleID~~"+auditScheduleId+"]]></string>                            \n" +
                            "</Parameters>                            \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;
            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" +  " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }

    //Save Other Remarks details based on selected ScheduleId
    public  String mSaveOtherRemarks(int auditScheduleId,String comment) {
        String result="";
        try{
            StringBuilder response = new StringBuilder();
            HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
                    URL).openConnection();
            localHttpURLConnection.setRequestMethod("POST");
            localHttpURLConnection.setRequestProperty("Content-type",
                    "text/xml; charset=UTF-8");
            localHttpURLConnection.setRequestProperty("Accept",
                    "*");
            localHttpURLConnection.setRequestProperty("SOAPAction",
                    "http://tempuri.org/GetDataSetInXMLFromStoredProcedure");

            localHttpURLConnection.setDoOutput(true);
            String request="";
            StringBuilder request_builder = new StringBuilder(
                    "<?xml version=\"1.0\" encoding=\"utf-8\"?>                            \n" +
                            "<soap:Envelope                            xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"                            xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"                            xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">                            \n" +
                            "<soap:Body>                            \n" +
                            "<GetDataSetInXMLFromStoredProcedure                            xmlns=\"http://tempuri.org/\">                            \n" +
                            "<Token></Token>                            \n" +
                            "<UserName></UserName>                            \n" +
                            "<Password></Password>                            \n" +
                            "<DeviceID></DeviceID>                            \n" +
                            "<ProcedureName>mob_UpdateAuditorComment</ProcedureName>                            \n" +
                            "<Parameters>                            \n" +
                            "<string><![CDATA[@AuditorComment~~"+comment+"]]></string>\n" +
                            "<string><![CDATA[@ScheduleID~~"+auditScheduleId+"]]></string>                            \n" +
                            "</Parameters>                            \n" +
                            "</GetDataSetInXMLFromStoredProcedure>                            \n" +
                            "</soap:Body>                            \n" +
                            "</soap:Envelope>");

            Log.i("request", request_builder.toString());
            OutputStreamWriter localOutputStreamWriter;
            localOutputStreamWriter = new OutputStreamWriter(
                    localHttpURLConnection.getOutputStream());
            localOutputStreamWriter.write(request_builder.toString());
            localOutputStreamWriter.flush();
            localOutputStreamWriter.close();
            System.out.println("code...for" +  " "
                    + localHttpURLConnection.getResponseCode());
            BufferedReader localBufferedReader = new BufferedReader(
                    new InputStreamReader(localHttpURLConnection.getInputStream()));
            String data = "";
            while ((data = localBufferedReader.readLine()) != null) {
                response.append(data);
            }
            Log.i("response", response.toString());
            result = response.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = "Error";
        }
        return result;
    }
}
