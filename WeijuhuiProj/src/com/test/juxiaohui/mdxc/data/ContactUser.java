package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/4/18.
 * 用来表示联系人,既可以给机票使用，也可以给酒店使用
 */
public class ContactUser {
    public String mFirstName = "";
    public String mLastName = "";
    public String mEmail = "";
    public String mPhoneNumber = "";
    public String contactName = "";
    public String contCountryCode = "";
    public String contPhone = "";
    public String contEmail = "";
    public String recipient = "";
    public String reciPhone = "";
    public String reciAddress = "";
    public String reciPostalCode = "";
    public String pickUpTime = "";

    public static ContactUser NULL = new ContactUser();

    public static JSONObject toJSON(ContactUser contactUser)
    {
        if(null!=contactUser)
        {
            JSONObject obj = new JSONObject();
            try {
                obj.put("firstName", contactUser.mFirstName);
                obj.put("lastName", contactUser.mLastName);
                obj.put("email", contactUser.mEmail);
                obj.put("phoneNumber", contactUser.mPhoneNumber);
                return obj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public static ContactUser fromJSON(JSONObject jsonObject)
    {
        if(null!=jsonObject)
        {
            ContactUser contactUser = new ContactUser();
            try {
                contactUser.mFirstName = jsonObject.getString("firstName");
                contactUser.mLastName = jsonObject.getString("lastName");
                contactUser.mEmail = jsonObject.getString("email");
                contactUser.mPhoneNumber = jsonObject.getString("phoneNumber");
                return contactUser;
            } catch (JSONException e) {
                e.printStackTrace();
                return ContactUser.NULL;
            }
        }
        return ContactUser.NULL;
    }
}
