package com.example.coopapp20.Administration.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Data.Objects.UserObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragAdminUserAddBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;


public class AddUserFrag extends Fragment {

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private List<String> DepartmentNames;
    private List<Integer> DepartmentNrs;
    private FragAdminUserAddBinding Binding;
    private AlertDialog dialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Binding = FragAdminUserAddBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        viewModel.getAllDepartments().observe(getViewLifecycleOwner(), this::CreateDropDowns);

        CreateDialog();

        //Set execute btn
        Binding.ExecuteBtn.setOnClickListener(v -> dialog.show());
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        return Binding.getRoot();
    }

    private void CreateDropDowns(List<DepartmentObject> departments){
        if(departments != null) {
            DepartmentNames = departments.stream().map(DepartmentObject::getName).collect(Collectors.toList());
            DepartmentNrs = departments.stream().map(DepartmentObject::getId).collect(Collectors.toList());
            Binding.DepartmentDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, DepartmentNames));
            setValues();
        }
    }

    private void CreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setPositiveButton(android.R.string.yes, ((dialog, which) -> Execute()));
        builder.setNegativeButton(android.R.string.no, null);
        dialog = builder.create();
        dialog.setTitle(getString(R.string.are_you_sure));
    }

    private void Execute(){
        Integer DepartmentId    = DepartmentNames.contains(Binding.DepartmentDropDown.getText().toString()) ? DepartmentNrs.get(DepartmentNames.indexOf(Binding.DepartmentDropDown.getText().toString())) : null;
        String Name             = Binding.NameTextView.getText().toString();
        String Email            = Binding.EmailTextView.getText().toString();
        String PhoneNr          = Binding.PhoneNrTextView.getText().toString();
        String Username         = Binding.UsernameTextView.getText().toString();
        String Password         = Binding.PasswordTextView.getText().toString();

        if(VerifyUserInput(DepartmentId, Name, Email, PhoneNr, Username, Password)) {
            viewModel.ExecuteUser(Name, Email, PhoneNr, DepartmentId, Username, Password, requireContext());

            Binding.NameTextView.getText().clear();
            Binding.EmailTextView.getText().clear();
            Binding.PhoneNrTextView.getText().clear();
            Binding.UsernameTextView.getText().clear();
            Binding.PasswordTextView.getText().clear();

            viewModel.getSelectedUser().setValue(null);
            if (!Binding.StaySwitch.isChecked()) {
                mainViewModel.getMainNavController().popBackStack(R.id.userAdministration, true);
                mainViewModel.getMainNavController().navigate(R.id.userAdministration);
            } else { Toast.makeText(getContext(), getString(R.string.done), Toast.LENGTH_SHORT).show(); }
        }
    }

    private boolean VerifyUserInput(Integer departmentId, String name, String email, String phonenr, String username, String password){
        boolean Accepted = true;

        if(departmentId == null){
            Accepted=false;
            Toast.makeText(getContext(),"afdeling eksistere ikke",Toast.LENGTH_LONG).show();}

        if(!name.matches("^[ A-Za-z]{2,50}$")){
            Accepted=false;
            Toast.makeText(getContext(),"navn må kun indeholder bogstaver og mellemrum",Toast.LENGTH_LONG).show();}

        if(!email.matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")){
            Accepted=false;
            Toast.makeText(getContext(),"forkert email",Toast.LENGTH_LONG).show();}

        if(!phonenr.matches("^[0-9]{8}$")){
            Accepted=false;
            Toast.makeText(getContext(),"telefonnr skal være 8 tal",Toast.LENGTH_LONG).show();}

        if(!username.matches("^[A-Za-z0-9]{4,20}$")){
            Accepted=false;
            Toast.makeText(getContext(),"brugernavn må kun være bogstaver og tal\nbrugernavn skal være mellem 4-20 karakterer",Toast.LENGTH_LONG).show();}

        if(!password.matches("^.{6,20}$")){
            Accepted=false;
            Toast.makeText(getContext(),"password skal være mellem 6-20 karakterer",Toast.LENGTH_LONG).show();}

        return Accepted;
    }

    private void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        UserObject user = viewModel.getSelectedUser().getValue();

        Binding.DepartmentDropDown.setText(user != null && DepartmentNrs.contains(user.getDepartmentId()) ? DepartmentNames.get(DepartmentNrs.indexOf(user.getDepartmentId())) : DepartmentNames.contains(SharedPref.getString("AddEmployeeDepartmentName", "")) ? SharedPref.getString("AddEmployeeDepartmentName", "") : "");
        Binding.NameTextView.setText(user != null ? user.getName() : SharedPref.getString("AddEmployeeEdittextName", ""));
        Binding.EmailTextView.setText(user != null ? user.getEmail(): SharedPref.getString("AddEmployeeEdittextEmail", ""));
        Binding.PhoneNrTextView.setText(user != null ? user.getPhoneNr() : SharedPref.getString("AddEmployeeEdittextPhonenr", ""));
        Binding.UsernameTextView.setText(user != null ? user.getUserName() : SharedPref.getString("AddEmployeeEdittextUsername", ""));
        Binding.PasswordTextView.setText(user != null ? user.getPassword() : SharedPref.getString("AddEmployeeEdittextPassword", ""));
        Binding.ExecuteBtn.setText(user != null ? getString(R.string.edit) : getString(R.string.add));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewModel.getSelectedUser().getValue() == null) {
            SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddEmployeeEdittextName", Binding.NameTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddEmployeeEdittextEmail", Binding.EmailTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddEmployeeEdittextPhonenr", Binding.PhoneNrTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddEmployeeEdittextUsername", Binding.UsernameTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddEmployeeEdittextPassword", Binding.PasswordTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddEmployeeDepartmentName", Binding.DepartmentDropDown.getText().toString()).apply();
        }else {viewModel.getSelectedUser().setValue(null);}
    }
}
