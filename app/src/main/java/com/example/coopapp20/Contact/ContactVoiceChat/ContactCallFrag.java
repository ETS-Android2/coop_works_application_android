package com.example.coopapp20.Contact.ContactVoiceChat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragContactCallBinding;

public class ContactCallFrag extends Fragment {

    private VoiceChatViewModel viewModel;
    private FragContactCallBinding Binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Binding = FragContactCallBinding.inflate(inflater);

        viewModel = ViewModelProviders.of(getActivity()).get(VoiceChatViewModel.class);

        viewModel.getCallUser().observe(getViewLifecycleOwner(), user -> setCaller());

        viewModel.getCallStatus().observe(getViewLifecycleOwner(), integer -> {
            if(viewModel.getOutgoing().getValue()){setOutStatus(integer);}else {setInStatus(integer);}
        });

        Binding.EndCallBtn.setOnClickListener(v-> viewModel.EndCall());
        Binding.CallBtn.setOnClickListener(v-> viewModel.AcceptCall());
        Binding.BackBtn.setOnClickListener(v -> getActivity().onBackPressed());

        return Binding.getRoot();
    }

    private void setOutStatus(Integer status){
        switch (status){
            case VoiceChatViewModel.STATUS_PENDING:

                Binding.StatusTextView.setText(getString(R.string.calling)+" ...");
                Binding.EndCallBtn.setRotation(135);

                break;

            case VoiceChatViewModel.STATUS_ACCEPTED:

                Binding.EndCallBtn.setRotation(135);
                viewModel.getCallDuration().observe(getViewLifecycleOwner(), s -> Binding.StatusTextView.setText(s));

                break;
            case VoiceChatViewModel.STATUS_ENDED:

                Binding.StatusTextView.setText(getString(R.string.call_ended));
                Binding.EndCallBtn.setVisibility(View.GONE);
                Binding.BackBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setInStatus(Integer status){
        switch (status){
            case VoiceChatViewModel.STATUS_PENDING:

                Binding.StatusTextView.setText(getString(R.string.calling)+" ...");
                Binding.CallBtn.setVisibility(View.VISIBLE);
                Binding.EndCallBtn.setRotation(135);

                break;
            case VoiceChatViewModel.STATUS_ACCEPTED:

                Binding.CallBtn.setVisibility(View.GONE);
                Binding.EndCallBtn.setRotation(135);
                viewModel.getCallDuration().observe(getViewLifecycleOwner(), s -> Binding.StatusTextView.setText(s));

                break;
            case VoiceChatViewModel.STATUS_ENDED:

                Binding.StatusTextView.setText(getString(R.string.call_ended));
                Binding.EndCallBtn.setVisibility(View.GONE);
                Binding.BackBtn.setVisibility(View.VISIBLE);

                break;
        }
    }

    void setCaller(){
        if(viewModel.getCallUser().getValue() != null) {
            UserObject user = viewModel.getCallUser().getValue();
            Binding.NameTextView.setText(user.getName());
            //Binding.DepartmentTextView.setText(contact.getDepartmentName());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.ClearCall();
    }
}
