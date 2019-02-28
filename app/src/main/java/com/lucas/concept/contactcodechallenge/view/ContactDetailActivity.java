package com.lucas.concept.contactcodechallenge.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucas.concept.contactcodechallenge.R;
import com.lucas.concept.contactcodechallenge.controller.ContactController;
import com.lucas.concept.contactcodechallenge.controller.IDetailInformationAvailableListener;
import com.lucas.concept.contactcodechallenge.model.Contact;
import com.squareup.picasso.Picasso;

public class ContactDetailActivity extends Activity implements IDetailInformationAvailableListener {
    public static final String TAG = ContactDetailActivity.class.getCanonicalName();
    public static final int INDEX_HOME_PHONE = 0;
    public static final int INDEX_CELL_PHONE = 1;
    public static final int INDEX_OFFICE_PHONE = 2;
    private Contact mContact;
    private TextView mFirstName;
    private TextView mLastName;
    private TextView mHomePhone;
    private TextView mCellPhone;
    private TextView mOfficePhone;
    private TextView mWorkAddress;
    private TextView mHomeAddress;
    private ImageView mContactImage;
    private ContactController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);

        mFirstName = findViewById(R.id.contact_first_name);
        mLastName = findViewById(R.id.contact_last_name);
        mContactImage = findViewById(R.id.contact_image);
        mHomePhone = findViewById(R.id.home_number);
        mCellPhone = findViewById(R.id.cell_number);
        mOfficePhone = findViewById(R.id.office_number);
        mWorkAddress = findViewById(R.id.work_address);
        mHomeAddress = findViewById(R.id.home_address);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int deviceIndex = extras.getInt(ContactController.CONTACT_INDEX);
            mController = ContactController.getInstance(this);
            mContact = mController.getData().get(deviceIndex);
            if (mContact != null && mContact.getUid() != null) {
                mController.populateDetailInfo(deviceIndex, this);
            }
            updateUIFromContact();
        }
    }

    synchronized private void updateUIFromContact() {
        if (mContact != null) {
            mFirstName.setText(mContact.getFirstName() != null ? mContact.getFirstName() : "-");
            mLastName.setText(mContact.getLastName() != null ? mContact.getLastName() : "-");

            mHomePhone.setText(mContact.getPhoneList().get(INDEX_CELL_PHONE).getPhoneNumber() != null ?
                    mContact.getPhoneList().get(INDEX_HOME_PHONE).getPhoneNumber() : "-");
            mCellPhone.setText(mContact.getPhoneList().get(INDEX_CELL_PHONE).getPhoneNumber() != null ?
                    mContact.getPhoneList().get(INDEX_CELL_PHONE).getPhoneNumber() : "-");
            mOfficePhone.setText(mContact.getPhoneList().get(INDEX_CELL_PHONE).getPhoneNumber() != null ?
                    mContact.getPhoneList().get(INDEX_OFFICE_PHONE).getPhoneNumber() : "-");

            mWorkAddress.setText(mContact.getWorkAddress() != null ? mContact.getWorkAddress() : "-");
            mHomeAddress.setText(mContact.getHomeAddress() != null ? mContact.getHomeAddress() : "-");
            Picasso.with(this).load(mContact.getThumbUrl())
                    .resize((int) this.getResources().getDimension(R.dimen.width_image_large), (int) this.getResources().getDimension(R.dimen.height_image_large))
                    .error(android.R.drawable.ic_menu_camera)
                    .placeholder(android.R.drawable.ic_menu_camera)
                    .into(mContactImage);
        }
    }

    @Override
    public void refreshDetailView() {
        updateUIFromContact();
    }
}
