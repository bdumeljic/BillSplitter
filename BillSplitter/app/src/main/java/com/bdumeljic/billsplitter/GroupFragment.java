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
import android.widget.TextView;

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
 * {@link GroupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    @Bind(R.id.group_name) TextView mGroupName;
    @Bind(R.id.group_number_members) TextView mGroupMembers;
    @Bind(R.id.group_number_bills) TextView mGroupNumberBills;
    @Bind(R.id.group_spent) TextView mGroupSpent;
    @Bind(R.id.list_recent_bills) RecyclerView mGroupMembersList;

    private ParseUser mCurrentUser;
    private Group mCurrentGroup;

    private MembersRecyclerViewAdapter mMembersAdapter;
    private static List<ParseUser> mMembers;

    private MainActivity mActivity;

    public GroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GroupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        mMembers = new ArrayList<ParseUser>();
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
    }

    public void getGroupMembers() {
        if (mCurrentGroup != null) {
            mCurrentGroup.getMembersList().getQuery().findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> members, ParseException e) {
                    if (e == null) {
                        mMembers.clear();
                        mMembers.addAll(members);
                        mMembersAdapter.notifyDataSetChanged();
                    } else {
                        Log.d("group", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, view);
        mGroupMembersList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMembersAdapter = new MembersRecyclerViewAdapter(getActivity(), mMembers);
        mGroupMembersList.setAdapter(mMembersAdapter);

        getGroupMembers();

        mGroupName.setText(mCurrentGroup.getName());
        mGroupMembers.setText(String.valueOf(mCurrentGroup.getMembersCount()));
        mGroupNumberBills.setText(String.valueOf(mCurrentGroup.getBillsCount()));
        mGroupSpent.append(String.valueOf(mCurrentGroup.getSpent()));

        return view;
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
