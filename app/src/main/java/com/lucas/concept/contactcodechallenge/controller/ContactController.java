package com.lucas.concept.contactcodechallenge.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.lucas.concept.contactcodechallenge.model.Contact;
import com.lucas.concept.contactcodechallenge.model.PhoneInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ContactController {

    private static final String TAG = ContactController.class.getCanonicalName();
    public static final String CONTACT_INDEX = "contact_index";
    private static ContactController mInstance;
    private final IListInformationAvailableListener mIInformationAvailableListener;
    private IDetailInformationAvailableListener mDetailInformationAvailableListener;
    private Context mContext;
    private ArrayList<Contact> mData;
    private String urlContacts = "https://private-d0cc1-iguanafixtest.apiary-mock.com/contacts";
    private String urlContactDetail = "https://private-d0cc1-iguanafixtest.apiary-mock.com/contacts/";

    public static ContactController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ContactController(context);
        }
        return mInstance;
    }

    private ContactController(Context context) {
        mContext = context;
        mData = new ArrayList<>();
        mIInformationAvailableListener = (IListInformationAvailableListener) context;
        pupulateInfo();
    }

    public void populateDetailInfo(final int index, IDetailInformationAvailableListener listener) {
        mDetailInformationAvailableListener = listener;
        String detailURL = urlContactDetail + mData.get(index).getUid();
        JsonObjectRequest contactDetailRequest = new JsonObjectRequest(detailURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray addressArray = response.getJSONArray("addresses");
                    for (int i = 0; i < addressArray.length(); i++) {
                        JSONObject currentAddress = addressArray.getJSONObject(i);
                        mData.get(index).setHomeAddress(currentAddress.has("home") ? currentAddress.getString("home") : null);
                        mData.get(index).setWorkAddress(currentAddress.has("work") ? currentAddress.getString("work") : null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mDetailInformationAvailableListener.refreshDetailView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(contactDetailRequest);
    }

    private void pupulateInfo() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlContacts, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int index = 0; index < response.length(); index++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(index);
                        Contact contact = new Contact();
                        contact.setUid(jsonObject.getString("user_id"));
                        contact.setFirstName(jsonObject.getString("first_name"));
                        contact.setLastName(jsonObject.getString("last_name"));
                        contact.setBirthDate(jsonObject.getString("birth_date"));
                        contact.setThumbUrl(jsonObject.getString("thumb"));

                        JSONArray phoneArray = jsonObject.getJSONArray("phones");
                        for (int i = 0; i < phoneArray.length(); i++) {
                            JSONObject thisPhone = phoneArray.getJSONObject(i);
                            PhoneInfo currentPhoneInfo = new PhoneInfo();
                            currentPhoneInfo.setPhoneType(thisPhone.getString("type"));
                            currentPhoneInfo.setPhoneNumber(thisPhone.getString("number"));
                            contact.getPhoneList().add(currentPhoneInfo);
                        }
                        mData.add(contact);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mIInformationAvailableListener.refreshListViews();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(jsonArrayRequest);
    }

    public ArrayList<Contact> getData() {
        return mData;
    }
}
