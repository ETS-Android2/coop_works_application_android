package com.example.coopapp20.Main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.coopapp20.databinding.ToolbarBinding;
import com.google.android.material.tabs.TabLayout;

public class ToolbarFrag extends Fragment {

    private ToolbarBinding Binding;
    private String ToolbarId = null;

    private View.OnClickListener BarcodeListener = null;
    private View.OnClickListener DeleteListener = null;
    private View.OnLongClickListener CheckListener = null;
    private View.OnClickListener SettingsListener = null;
    private View.OnClickListener CallListener = null;
    private View.OnClickListener AddListener = null;
    private View.OnClickListener ListListener = null;
    private View.OnClickListener OptionsListener = null;
    private boolean SearchBtnVisible = false;
    private onTabLayoutClickListener TabListener = null;
    private String Tab1Text = null;
    private String Tab2Text = null;
    private int Selected = 0;
    private String[] TextViewStrings;
    public MutableLiveData<String> SearchText = new MutableLiveData<>();

    public ToolbarFrag ToolbarFrag(){return this;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Binding = ToolbarBinding.inflate(getLayoutInflater());

        setSearch();
        setViews();

        return Binding.getRoot();
    }

    private void setSearch(){
        Binding.SearchBtn.setOnClickListener(v -> {
            Binding.SearchLayout.setVisibility(View.VISIBLE);
            SearchAnimation(Binding.SearchLayout,Binding.SearchLayout.getWidth(), Binding.SearchLayout.getHeight(), true).start();
        });
        Binding.BackBtn.setOnClickListener(v -> {
            Animator anim = SearchAnimation(Binding.SearchLayout,Binding.SearchLayout.getWidth(), Binding.SearchLayout.getHeight(), false);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    Binding.SearchLayout.setVisibility(View.GONE);
                }
            });
            anim.start();
        });
        Binding.EraseBtn.setOnClickListener(v -> Binding.TextView.setText(""));
        Binding.TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {SearchText.setValue(s.toString());}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private Animator SearchAnimation(View view, int centerX, int centerY, boolean show) {
        float finalRadius = (float) Math.hypot(centerX, centerY)*2;
        if(show) { return ViewAnimationUtils.createCircularReveal(view, 0, centerY/2, 0f, finalRadius).setDuration(500); }else
            {return ViewAnimationUtils.createCircularReveal(view, 0, centerY/2, finalRadius, 0f).setDuration(500);}
    }

    public void setViews(){
        if(BarcodeListener != null){
            Binding.BarcodeBtn.setVisibility(View.VISIBLE);
            Binding.BarcodeBtn.setOnClickListener(BarcodeListener);
        }
        if(DeleteListener != null){
            Binding.DeleteBtn.setVisibility(View.VISIBLE);
            Binding.DeleteBtn.setOnClickListener(DeleteListener);
        }
        if(CheckListener != null){
            Binding.CheckBtn.setVisibility(View.VISIBLE);
            Binding.CheckBtn.setOnLongClickListener(CheckListener);
        }
        if(SettingsListener != null){
            Binding.SettingsBtn.setVisibility(View.VISIBLE);
            Binding.SettingsBtn.setOnClickListener(SettingsListener);
        }
        if(CallListener != null){
            Binding.CallBtn.setVisibility(View.VISIBLE);
            Binding.CallBtn.setOnClickListener(CallListener);
        }
        if(AddListener != null){
            Binding.AddBtn.setVisibility(View.VISIBLE);
            Binding.AddBtn.setOnClickListener(AddListener);
        }
        if(ListListener != null){
            Binding.ListBtn.setVisibility(View.VISIBLE);
            Binding.ListBtn.setOnClickListener(ListListener);
        }
        if(OptionsListener != null){
            Binding.OptionsBtn.setVisibility(View.VISIBLE);
            Binding.OptionsBtn.setOnClickListener(OptionsListener);
        }
        if(SearchBtnVisible){
            Binding.SearchBtn.setVisibility(View.VISIBLE);
        }
        if(TabListener != null){
            Binding.TabLayout.addTab(Binding.TabLayout.newTab().setText(Tab1Text));
            Binding.TabLayout.addTab(Binding.TabLayout.newTab().setText(Tab2Text));

            Binding.TabLayout.setVisibility(View.VISIBLE);
            Binding.TabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    TabListener.onTabClick(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) { }
                @Override
                public void onTabReselected(TabLayout.Tab tab) { }
            });
            Binding.TabLayout.selectTab(Binding.TabLayout.getTabAt(Selected));
        }
        if(TextViewStrings != null){
            Binding.TextViewLayout.setVisibility(View.VISIBLE);
            Binding.TopTextView.setText(TextViewStrings[0]);
            Binding.BottomTextView.setText(TextViewStrings[1]);
            Binding.RightTextView.setText(TextViewStrings[2]);
        }
    }

    public void setBarcodeBtn(View.OnClickListener listener){
        BarcodeListener = listener;
    }
    public ToolbarFrag setDeleteBtn(View.OnClickListener listener){
        DeleteListener = listener; return this;
    }
    public void setCheckBtn(View.OnLongClickListener listener){
        CheckListener = listener;
    }
    public void setSettingsBtn(View.OnClickListener listener){
        SettingsListener = listener;
    }
    public void setCallBtn(View.OnClickListener listener){
        CallListener = listener;
    }
    public void setAddBtn(View.OnClickListener listener){
        AddListener = listener;
    }
    public void setListBtn(View.OnClickListener listener){
        ListListener = listener;
    }
    public void setOptionsBtn(View.OnClickListener listener){
        OptionsListener = listener;
    }
    public ToolbarFrag setSearchBtnVisible(boolean visible){
        SearchBtnVisible = visible;
        return this;
    }
    public void setTabLayout(onTabLayoutClickListener listener, String tab1Text, String tab2Text, int selected){
        TabListener = listener;
        Tab1Text = tab1Text;
        Tab2Text = tab2Text;
        Selected = selected;
    }
    public void setTextLayout(String TopText, String BottomText, String RightText) {
        TextViewStrings = new String[]{TopText, BottomText, RightText};
    }

    public void setToolbarId(String id){ ToolbarId = id; }
    public String getToolbarId(){return ToolbarId;}
    public void setToolbarSelectedTab(int Selected){
        Binding.TabLayout.selectTab(Binding.TabLayout.getTabAt(Selected));
    }

    public interface onTabLayoutClickListener{
        void onTabClick(int position);
    }
}
