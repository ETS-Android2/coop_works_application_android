package com.example.coopapp20.Products;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.coopapp20.Main.MainViewModel;
import com.example.coopapp20.R;
import com.example.coopapp20.databinding.FragProductScanBinding;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class ScannerFrag extends Fragment {

    private CameraSource cameraSource;

    private Thread DialogThread;
    private String Barcode;
    private FragProductScanBinding Binding;
    private ProductViewModel viewModel;

    private MainViewModel mainViewModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Binding = FragProductScanBinding.inflate(inflater);

        mainViewModel = ViewModelProviders.of(requireActivity()).get(MainViewModel.class);
        viewModel = ViewModelProviders.of(requireActivity()).get(ProductViewModel.class);

        Binding.ScannerActivityCancelBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        if (PermissionChecker.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PermissionChecker.PERMISSION_GRANTED) {
            CreateCameraResource();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
        return Binding.getRoot();
    }


    private void CreateCameraResource() {
        
        BarcodeDetector detector = new BarcodeDetector.Builder(getContext()).build();
        
        cameraSource = new CameraSource.Builder(getContext(), detector).setAutoFocusEnabled(true).setRequestedPreviewSize(1600, 1024).setRequestedFps(15.0f).build();

        Binding.ScannerActivitySurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @SuppressLint("MissingPermission")
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(Binding.ScannerActivitySurfaceView.getHolder());

                } catch (IOException e) { e.getMessage();}
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }

        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                SparseArray<Barcode> barcodeSparseArray = detections.getDetectedItems();
                if (barcodeSparseArray.size() > 0) {
                    Barcode = barcodeSparseArray.valueAt(0).displayValue;
                    System.out.println("BARCODE DETECTED: "+Barcode);

                    CreateDialogThread();
                    DialogThread.start();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> cameraSource.stop());
                    try {handler.wait();}catch (InterruptedException e) {e.printStackTrace();}
                }
            }
        });
    }

    private void CreateDialogThread(){
        DialogThread = new Thread(()->{

            boolean ProductExists = viewModel.SelectProductByBarcode(Barcode);

            getActivity().runOnUiThread(()->{
                if(false) {
                    if (ProductExists) {
                        getActivity().onBackPressed();
                    } else {
                        AlertDialog d = new AlertDialog.Builder(getContext()).create();
                        d.setMessage("Stregkoden matchede ikke nogle produkter");
                        d.setButton(AlertDialog.BUTTON_POSITIVE, "gå tilbage", (dialog, which) -> mainViewModel.getMainNavController().navigate(R.id.productFrag));
                        d.setButton(AlertDialog.BUTTON_NEUTRAL, "Scan igen", (dialog, which) -> { try { cameraSource.start(Binding.ScannerActivitySurfaceView.getHolder()); } catch (IOException e) { e.printStackTrace(); }
                        });
                        d.setButton(AlertDialog.BUTTON_NEGATIVE, "tilføj produkt", (dialog, which) -> {
                            mainViewModel.getMainNavController().navigate(R.id.addProductFrag);
                            //AddProductFrag.Barcode = Barcode;
                        });
                        d.setOnCancelListener(dialog -> { try { cameraSource.start(Binding.ScannerActivitySurfaceView.getHolder()); } catch (IOException e) { e.printStackTrace(); }
                        });
                        d.show();
                    }
                }else {
                    if(!ProductExists){getActivity().onBackPressed();}
                    else {
                        getActivity().runOnUiThread(() -> {
                            AlertDialog d = new AlertDialog.Builder(getContext()).create();
                            d.setMessage("stregkoden er allerede forbundet til et produkt");
                            d.setButton(AlertDialog.BUTTON_POSITIVE, "gå tilbage", (dialog, which) -> {
                                mainViewModel.getMainNavController().navigate(R.id.addProductFrag);
                                //AddProductFrag.Barcode = Barcode;
                            });
                            d.setButton(AlertDialog.BUTTON_NEUTRAL, "Scan igen", (dialog, which) -> { try { cameraSource.start(Binding.ScannerActivitySurfaceView.getHolder()); } catch (IOException e) { e.printStackTrace(); }
                            });
                            d.setButton(AlertDialog.BUTTON_NEGATIVE, "gå til forside", (dialog, which) -> mainViewModel.getMainNavController().navigate(R.id.productFrag));
                            d.setOnCancelListener(dialog -> { try { cameraSource.start(Binding.ScannerActivitySurfaceView.getHolder()); } catch (IOException e) { e.printStackTrace(); }
                            });
                            d.show();
                        });
                    }
                }
            });
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        CreateCameraResource();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraSource.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cameraSource != null){cameraSource.release();}
    }
}
