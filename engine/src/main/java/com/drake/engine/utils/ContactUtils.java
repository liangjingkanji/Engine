/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.widget.Toast;

public class ContactUtils {

  public static final int REQUEST_CONTACT = 1001;
  public String contactName;
  public String contactNumber;
  private Activity activity;

  public ContactUtils(Activity activity) {
    this.activity = activity;
  }

  /**
   * 打开系统联系人列表
   */
  public void intoContact(int requestCode) {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_PICK);
    intent.setData(ContactsContract.Contacts.CONTENT_URI);
    activity.startActivityForResult(intent, requestCode);
  }

  public ContactUtils init(Intent data) {
    ContentResolver cr = activity.getContentResolver();
    Cursor cursor = cr.query(data.getData(), null, null, null, null);
    if (cursor != null && cursor.moveToFirst()) {
      //取得联系人名字
      int nameFieldColumnIndex =
              cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
      contactName = cursor.getString(nameFieldColumnIndex)
              .replace(" ", "");
      //取得电话号码
      String ContactId =
              cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
      Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
              ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="
                      + ContactId, null,
              null);
      if (phone != null && phone.getCount() != 0 && phone.moveToFirst()) {
        contactNumber = phone.getString(
                phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                .replace(" ", "");
      }
      phone.close();
      cursor.close();
    } else {
      Toast.makeText(activity, "获取联系人失败", Toast.LENGTH_SHORT)
              .show();
    }
    return this;
  }

}
