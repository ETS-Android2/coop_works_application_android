package com.example.coopapp20.Contact.Contact;

import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.coopapp20.Contact.ContactViewModel;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragRecyclerviewBinding;
import com.example.coopapp20.zOtherFiles.AdapterSwipeHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.*;


public class ContactFrag extends Fragment implements AdapterSwipeHelper.onSwipeListener {

    private MainViewModel mainViewModel;
    private ContactViewModel viewModel;
    private FragRecyclerviewBinding Binding;
    private ContactAdapter Adapter;
    private ToolbarFrag toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Binding  = FragRecyclerviewBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ContactViewModel.class);
        viewModel.setCurrentUser(mainViewModel.getCurrentUser());
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        CreateToolbar();
        CreateRecyclerView();
        viewModel.getContacts().observe(getViewLifecycleOwner(), Adapter::UpdateData);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), userObject -> viewModel.ContactUserObjects(userObject.getId()));

        return Binding.getRoot();
    }

    private void CreateToolbar(){
        toolbar = new ToolbarFrag();
        toolbar.setSearchBtnVisible(true);
        toolbar.setListBtn(v -> mainViewModel.getMainNavController().navigate(R.id.contactInteractionFrag));
        mainViewModel.getCurrentToolbar().setValue(toolbar);
    }

    private void CreateRecyclerView(){
        Binding.Recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter = new ContactAdapter(contact -> {
            viewModel.getSelectedContact().setValue(contact);
            mainViewModel.getMainNavController().navigate(R.id.contactDetailFrag);}
            , requireContext().getResources().getDisplayMetrics());
        Binding.Recyclerview.setAdapter(Adapter);
        toolbar.SearchText.observe(getViewLifecycleOwner(), s -> Adapter.FilterData(s));

        Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.round_call_icon);
        Drawable background = ContextCompat.getDrawable(requireContext(),R.drawable.rounded_rectangle);
        icon.setTint(ContextCompat.getColor(requireContext(),R.color.White));
        background.mutate();
        background.setTint(ContextCompat.getColor(requireContext(),R.color.SlideBackGround));
        androidx.recyclerview.widget.ItemTouchHelper itemTouchHelper = new androidx.recyclerview.widget.ItemTouchHelper(new AdapterSwipeHelper(this, icon, background,null,null,null,getContext().getResources().getDisplayMetrics()));
        itemTouchHelper.attachToRecyclerView(Binding.Recyclerview);
    }

    @Override
    public void onSwipe(int position) {
        Adapter.notifyItemChanged(position);
        ContactObject contactObject = viewModel.getContacts().getValue().get(position);


        if(contactObject.getActive()){
            //implement call function
        }
        else{
            if (contactObject.getName().equals(contactObject.getDepartmentName())) {
                Toast.makeText(getContext(), "Ingen " + contactObject.getName() + " på arbejde", Toast.LENGTH_SHORT).show();
            } else {
                new AlertDialog.Builder(getContext())
                        .setTitle(contactObject.getName() + " er ikke på arbejde")
                        .setMessage("ring kun privat hvis det er meget vigtigt")
                        .setPositiveButton("annuller", (dialog, which) -> {})
                        .setNeutralButton("ring privat", ((dialog, which) -> {}))
                        .show();
            }
        }
    }
}
