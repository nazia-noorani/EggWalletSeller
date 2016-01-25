package com.egneese.sellers.dashboardfragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import com.egneese.sellers.R;
import com.egneese.sellers.adapters.ProfileViewGridAdapter;
import com.egneese.sellers.asynctasks.LogoutAsyncTask;
import com.egneese.sellers.dto.MessageCustomDialogDTO;
import com.egneese.sellers.dto.SellerDTO;
import com.egneese.sellers.ui.ExpandableHeightGridView;
import com.egneese.sellers.ui.SnackBar;
import com.egneese.sellers.util.NetworkCheck;
import com.neopixl.pixlui.components.textview.TextView;
import com.pkmmte.view.CircularImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.drakeet.materialdialog.MaterialDialog;


/**
 * Created by Dell on 1/8/2016.
 */
public class ProfileFragment extends Fragment {
    private View rootView;
    @InjectView(R.id.scrollViewProfile)
    ScrollView scrollViewProfile;
    @InjectView(R.id.imgProfilePic)
    CircularImageView imgProfilePic;
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.gridProfileItems)
    ExpandableHeightGridView gridProfileItems;
    @InjectView(R.id.btnLogout)
    Button btnLogout;
    private SellerDTO sellerDTO;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.inject(this, rootView);
        populate();

        return rootView;
    }

    private void populate(){
      //  buyerDTO = SessionDTODFactory.getBuyerDTO(getActivity());

        gridProfileItems.setExpanded(true);
        ProfileViewGridAdapter profileViewGridAdapter = new ProfileViewGridAdapter(getActivity());
        gridProfileItems.setAdapter(profileViewGridAdapter);
        profileViewGridAdapter.notifyDataSetChanged();

        scrollViewProfile.smoothScrollTo(0, 0);
        /*if(buyerDTO.getName() != null && !buyerDTO.getName().equals(""))
            txtName.setText(buyerDTO.getName());*/

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkCheck.isNetworkAvailable(getActivity())) {
                    final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setTitle("Logout").setMessage("Are you sure you want to logout ?");
                    mMaterialDialog.setPositiveButton("Logout", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                            LogoutAsyncTask logoutAsyncTask = new LogoutAsyncTask(getActivity());
                            logoutAsyncTask.execute();
                        }
                    });

                    mMaterialDialog.setNegativeButton("CANCEL", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMaterialDialog.dismiss();
                        }
                    });

                    mMaterialDialog.show();
                }else{
                    MessageCustomDialogDTO messageCustomDialogDTO = new MessageCustomDialogDTO();
                    messageCustomDialogDTO.setTitle(getResources().getString(R.string.login_activity_no_internet_title));
                    messageCustomDialogDTO.setButton(getResources().getString(R.string.ok));
                    messageCustomDialogDTO.setMessage(getResources().getString(R.string.login_activity_no_internet));
                    messageCustomDialogDTO.setContext(getActivity());
                    SnackBar.show(getActivity(), messageCustomDialogDTO);
                }
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
