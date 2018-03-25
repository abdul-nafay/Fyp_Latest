package com.sourcey.movnpack.AppFragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sourcey.movnpack.Adapters.UserScheduledTaskAdapter;
import com.sourcey.movnpack.DataBase.DatabaseManager;
import com.sourcey.movnpack.Helpers.ApplicationContextProvider;
import com.sourcey.movnpack.Helpers.UserScheduledTaskInterface;
import com.sourcey.movnpack.Model.AcceptedBidsModel;
import com.sourcey.movnpack.Model.BaseModel;
import com.sourcey.movnpack.Model.BidModel;
import com.sourcey.movnpack.Model.ConfirmBidModel;
import com.sourcey.movnpack.Model.ViewModels.UserScheduledTaskViewModel;
import com.sourcey.movnpack.R;
import com.sourcey.movnpack.UserServiceProviderCommunication.UserTaskDetailActivity;
import com.sourcey.movnpack.Utility.MemorizerUtil;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserScheduledTaskFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserScheduledTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */


public class UserScheduledTaskFragment extends Fragment implements UserScheduledTaskInterface {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public UserScheduledTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserScheduledTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserScheduledTaskFragment newInstance(String param1, String param2) {
        UserScheduledTaskFragment fragment = new UserScheduledTaskFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_scheduled_task, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Inflate the layout for this fragment
        RecyclerView recList = (RecyclerView)  getView().findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(ApplicationContextProvider.getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        ArrayList<UserScheduledTaskViewModel> scheduledTasks = createScheduledTasks();
        UserScheduledTaskAdapter adp = new UserScheduledTaskAdapter(scheduledTasks);
        adp.intrface = this;
        recList.setAdapter(adp);
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
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void didPressedDetailButtonWithViewModel(UserScheduledTaskViewModel viewModel) {
       // MemorizerUtil.displayToast(getActivity().getApplicationContext(),"Tapped");
        Intent intent = new Intent(getActivity().getApplicationContext(), UserTaskDetailActivity.class);
        intent.putExtra("bidID",viewModel.bidId);
        intent.putExtra("cbmID",viewModel.confrimBidId);
        startActivity(intent);
    }

    ArrayList<UserScheduledTaskViewModel> createScheduledTasks() {

        ArrayList<BaseModel> confirmedBids = DatabaseManager.getInstance(ApplicationContextProvider.getContext()).getConfirmedBids();
        ArrayList<UserScheduledTaskViewModel> scheduledTasks = new ArrayList<>();
        if (confirmedBids != null) {
            for (BaseModel model : confirmedBids) {
                ConfirmBidModel cbm = (ConfirmBidModel) model;
                UserScheduledTaskViewModel viewModel = new UserScheduledTaskViewModel();
                BidModel bid =(BidModel) DatabaseManager.getInstance(ApplicationContextProvider.getContext()).getBidById(cbm.getBidId()).get(0);
                viewModel.subject = bid.getSubject();
                viewModel.time = cbm.getTime();
                viewModel.date = cbm.getDate();
                viewModel.amount = cbm.getAmount();
                viewModel.spNumber = cbm.getSpId();
                viewModel.bidId = bid.getBidId();
                viewModel.confrimBidId = cbm.getID();
                scheduledTasks.add(viewModel);
            }
            return scheduledTasks;
        }
        return scheduledTasks;
    }

}
