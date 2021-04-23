package com.example.coopapp20.Administration.Department;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Administration.AdminViewModel;
import com.example.coopapp20.Data.Objects.DepartmentObject;
import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.Main.ToolbarFrag;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragAdminDepartmentAddBinding;
import com.example.coopapp20.zOtherFiles.CustomDropdownMenuAdapter;

import java.util.Arrays;
import java.util.List;

public class DepartmentAddFrag extends Fragment {

    private MainViewModel mainViewModel;
    private AdminViewModel viewModel;
    private FragAdminDepartmentAddBinding Binding;
    private List<String> ColorNames, ColorIds;
    private AlertDialog dialog = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding = FragAdminDepartmentAddBinding.inflate(getLayoutInflater());
        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(AdminViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CreateDropDown();
        CreateDialog();
        setValues();

        Binding.ExecuteBtn.setOnClickListener(v -> dialog.show());
        mainViewModel.getCurrentToolbar().setValue(new ToolbarFrag());

        return Binding.getRoot();
    }

    @SuppressWarnings("ResourceType")
    private void CreateDropDown(){
        ColorNames = Arrays.asList("grøn","brun","rød","blå");
        ColorIds = Arrays.asList(getString(R.color.Servicearbejder),getString(R.color.Bager),getString(R.color.Slagter),getString(R.color.Afdelingsleder));
        Binding.ColorDropDown.setAdapter(new CustomDropdownMenuAdapter(requireContext(), R.menu.menu_item_layout, ColorNames));
    }

    private void CreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setPositiveButton(android.R.string.yes, ((dialog, which) -> Execute()));
        builder.setNegativeButton(android.R.string.no, null);
        dialog = builder.create();
    }

    private void Execute(){
        String Name = Binding.NameTextView.getText().toString();
        String ColorId = ColorIds.get(ColorNames.indexOf(Binding.ColorDropDown.getText().toString()));

        if(!Name.equals("")){
            viewModel.ExecuteDepartment(Name, ColorId);
            Binding.NameTextView.getText().clear();
            viewModel.getSelectedDepartment().setValue(null);
            if (!Binding.StaySwitch.isChecked()) {
                mainViewModel.getMainNavController().popBackStack(R.id.departmentAdministration, true);
                mainViewModel.getMainNavController().navigate(R.id.departmentAdministration);
            } else { Toast.makeText(getContext(), getString(R.string.done), Toast.LENGTH_SHORT).show();}
        }else {Toast.makeText(getContext(), getString(R.string.fill_required_fields), Toast.LENGTH_SHORT).show();}
    }

    private void setValues(){
        SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
        DepartmentObject department = viewModel.getSelectedDepartment().getValue();
        Binding.NameTextView.setText(department != null ? department.getName() : SharedPref.getString("AddDepartmentEditTextTitle", ""));
        Binding.ColorDropDown.setText(department != null ? ColorNames.get(ColorIds.indexOf(department.getColor())) : !ColorNames.contains(SharedPref.getString("AddDepartmentDropDownColor","")) && !ColorNames.isEmpty() ? ColorNames.get(0) : SharedPref.getString("AddDepartmentDropDownColor",""));
        dialog.setTitle(department != null ? "VIL DU ÆNDRE AFDELINGEN" : "VIL DU TILFØJE AFDELINGEN");
        Binding.ExecuteBtn.setText(department != null ? "ÆNDRE" : "TILFØJ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(viewModel.getSelectedDepartment().getValue() == null){
            SharedPreferences SharedPref = requireContext().getSharedPreferences(getString(R.string.BasicPreferences), Context.MODE_PRIVATE);
            SharedPref.edit().putString("AddDepartmentEditTextTitle", Binding.NameTextView.getText().toString()).apply();
            SharedPref.edit().putString("AddDepartmentDropDownColor", Binding.ColorDropDown.getText().toString()).apply();
        }else {viewModel.getSelectedDepartment().setValue(null);}
    }
}
