package com.lucas.concept.contactcodechallenge.view;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import com.lucas.concept.contactcodechallenge.R;
import com.lucas.concept.contactcodechallenge.controller.ContactController;
import com.lucas.concept.contactcodechallenge.controller.IItemClickListener;
import com.lucas.concept.contactcodechallenge.controller.IListInformationAvailableListener;
import com.volcaniccoder.volxfastscroll.Volx;


public class MainActivity extends AppCompatActivity implements IItemClickListener, IListInformationAvailableListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private RecyclerView mRecyclerView;
    private ContactAdapter mContactAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;
    private SearchView mSearchView;
    private FrameLayout mParentLayout;
    private Volx volx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setTitle(R.string.action_search);

        mRecyclerView = findViewById(R.id.contact_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) mLayoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), ((LinearLayoutManager) mLayoutManager).getOrientation());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
        mContactAdapter = new ContactAdapter(this);
        mContactAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mContactAdapter);
        mParentLayout = findViewById(R.id.activity_use);

    }

    @Override
    public void onClickDevice(View view, int position) {
        Intent i = new Intent(this, ContactDetailActivity.class);
        i.putExtra(ContactController.CONTACT_INDEX, position);
        startActivity(i);
    }

    @Override
    public void refreshListViews() {
        mContactAdapter.notifyDataSetChanged();
        volx = new Volx.Builder()
                .setUserRecyclerView(mRecyclerView)
                .setParentLayout(mParentLayout)
                .build();
        volx.setInactive(false);
        volx.notifyValueDataChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        mSearchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        mSearchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mContactAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mContactAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!mSearchView.isIconified()) {
            mSearchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        hideKeyboard(this);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
