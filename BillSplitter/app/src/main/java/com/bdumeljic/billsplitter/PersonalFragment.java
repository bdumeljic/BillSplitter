package com.bdumeljic.billsplitter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonalFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.person_photo) ImageView mPersonPhoto;
    @Bind(R.id.person_name) TextView mPersonName;
    @Bind(R.id.person_balance) TextView mPersonBalance;
    @Bind(R.id.person_bills) TextView mPersonBills;

    @Bind(R.id.list_recent_bills) RecyclerView mPersonRecentBillsList;
    private RecentBillsRecyclerViewAdapter mRecentBillsAdapter;
    private static List<Bill> mBills;
    private MainActivity mActivity;

    private ParseUser mCurrentUser;
    private Group mCurrentGroup;

    public PersonalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonalFragment newInstance(String param1, String param2) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mCurrentUser = mActivity.getCurrentUser();
        mCurrentGroup = mActivity.getCurrentGroup();
        mBills = new ArrayList<Bill>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_personal, container, false);

        ButterKnife.bind(this, view);

        Glide.with(this)
                .load(mCurrentUser.getParseFile("photo").getUrl())
                .centerCrop()
                .transform(new CircleTransform(mActivity))
                .into(mPersonPhoto);

        mPersonName.setText(mCurrentUser.getString("name"));
        mPersonBalance.append(String.valueOf(mCurrentUser.getInt("balance")));

        mPersonRecentBillsList.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecentBillsAdapter = new RecentBillsRecyclerViewAdapter(mBills);
        mPersonRecentBillsList.setAdapter(mRecentBillsAdapter);

        getRecentBills();

        return view;
    }

    private void getRecentBills() {
        if (mCurrentGroup != null) {
            mCurrentGroup.getBillsList().getQuery().findInBackground(new FindCallback<Bill>() {
                @Override
                public void done(List<Bill> bills, ParseException e) {
                    if (e == null) {
                        mBills.clear();
                        for (Bill bill : bills) {
                            if (mCurrentUser.equals(bill.getPayedBy())) {
                                mBills.add(bill);
                            }
                        }
                        mRecentBillsAdapter.notifyDataSetChanged();
                        mPersonBills.setText(String.valueOf(mBills.size()));
                    } else {
                        Log.d("bills", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        mActivity = (MainActivity) context;

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
