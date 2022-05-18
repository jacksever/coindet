package com.sever.coinsdetector.ui.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Rational;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.UseCaseGroup;
import androidx.camera.core.ViewPort;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.databinding.CameraFragmentBinding;
import com.sever.coinsdetector.utils.Storage;
import com.sever.coinsdetector.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraFragment extends Fragment {
    private CameraFragmentBinding binding;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private CameraSelector cameraSelector;
    private ImageCapture imageCapture;
    private ExecutorService imageCaptureExecutor;

    private boolean isFlashOn = false;
    private ActivityResultLauncher<Intent> photoPickerResultLauncher;
    private ActivityResultLauncher<String[]> permissionActivityLauncher;
    private Camera camera;

    private String STATE = "avers";
    private String STATE_RELOAD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            STATE_RELOAD = getArguments().getString("state", null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.binding = CameraFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
        imageCaptureExecutor = Executors.newSingleThreadExecutor();

        registerPermissionLauncher();
        registerActivityResult();

        if (requestCameraPermission())
            startCamera();
        else
            permissionActivityLauncher.launch(new String[]{
                    Manifest.permission.CAMERA
            });

        if (STATE_RELOAD != null)
            if (STATE_RELOAD.equals("revers")) {
                binding.title.setText(requireContext().getString(R.string.camera_title2));
                binding.subtitle.setText(requireContext().getString(R.string.camera_subtitle2));
            }

        binding.takePhoto.setOnClickListener(v -> {
            try {
                takePhoto();
                animateFlash();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        binding.flash.setOnClickListener(v -> {
            isFlashOn = !isFlashOn;

            if (camera.getCameraInfo().hasFlashUnit())
                camera.getCameraControl().enableTorch(isFlashOn);

            if (isFlashOn)
                binding.flash.setCardBackgroundColor(requireContext().getColor(R.color.yellow));
            else
                binding.flash.setCardBackgroundColor(requireContext().getColor(R.color.white));
        });

        binding.gallery.setOnClickListener(v -> {
            String[] mimeTypes = {"image/jpeg", "image/png"};

            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (Storage.SETTINGS.getBoolean(Storage.GALLERY, true)) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                    photoPickerResultLauncher.launch(photoPickerIntent);
                } else {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    photoPickerIntent.setType("image/*");
                    photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

                    photoPickerResultLauncher.launch(photoPickerIntent);
                }
            } else
                ActivityCompat.requestPermissions(
                        requireActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavHostFragment.findNavController(CameraFragment.this).popBackStack();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    private void startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(binding.cameraView.getSurfaceProvider());

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .setTargetRotation(requireActivity().getWindowManager().getDefaultDisplay().getRotation())
                        .build();
                cameraProvider.unbindAll();

                ViewPort viewPort = new ViewPort.Builder(new Rational(1200, 1200), requireActivity().getWindowManager().getDefaultDisplay().getRotation())
                        .setScaleType(ViewPort.FILL_CENTER)
                        .build();

                UseCaseGroup useCaseGroup = new UseCaseGroup.Builder()
                        .addUseCase(preview)
                        .addUseCase(imageCapture)
                        .setViewPort(viewPort)
                        .build();
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(requireContext()));
    }

    private void animateFlash() {
        binding.getRoot().postDelayed(() -> {
            binding.getRoot().setForeground(new ColorDrawable(Color.WHITE));
            binding.getRoot().postDelayed(() -> {
                binding.getRoot().setForeground(null);
            }, 50);
        }, 100);
    }

    private boolean requestCameraPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void registerPermissionLauncher() {
        permissionActivityLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            if (result != null && result.size() == 1) {
                result.forEach((key, value) -> {
                    if (value)
                        startCamera();
                    else
                        Toast.makeText(requireContext(), "Camera permission is denied!", Toast.LENGTH_SHORT).show();
                });
            } else
                Toast.makeText(requireContext(), "Camera permission is denied!", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("WrongConstant")
    private void registerActivityResult() {
        photoPickerResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                if (result.getData() != null) {
                    Uri selectedImage = result.getData().getData();
                    if (Storage.SETTINGS.getBoolean(Storage.FILE_MANAGER, false)) {
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION & result.getData().getFlags();
                        ContentResolver resolver = requireActivity().getContentResolver();
                        resolver.takePersistableUriPermission(selectedImage, takeFlags);
                    }

                    if (STATE_RELOAD != null) {
                        try {
                            Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                            Bitmap cropBitmap = Utils.cropBitmap(sourceBitmap);

                            File file = new File(requireActivity().getFilesDir(), STATE_RELOAD + ".jpg");
                            FileOutputStream fOut = new FileOutputStream(file);
                            cropBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        requireActivity().runOnUiThread(() -> NavHostFragment.findNavController(CameraFragment.this).navigate(R.id.action_cameraFragment_to_cameraCompleteFragment));
                    } else {
                        if (STATE.equals("avers")) {
                            binding.title.setText(requireContext().getString(R.string.camera_title2));
                            binding.subtitle.setText(requireContext().getString(R.string.camera_subtitle2));
                        }

                        try {
                            Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImage);
                            Bitmap cropBitmap = Utils.cropBitmap(sourceBitmap);

                            File file = new File(requireActivity().getFilesDir(), STATE + ".jpg");
                            FileOutputStream fOut = new FileOutputStream(file);
                            cropBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                            fOut.flush();
                            fOut.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        requireActivity().runOnUiThread(() -> {
                            binding.buttonProgress.setVisibility(View.GONE);
                            binding.buttonText.setVisibility(View.VISIBLE);
                            binding.takePhoto.setEnabled(true);
                        });

                        if (STATE.equals("revers"))
                            requireActivity().runOnUiThread(() -> NavHostFragment.findNavController(CameraFragment.this).navigate(R.id.action_cameraFragment_to_cameraCompleteFragment));
                        else
                            STATE = "revers";
                    }
                }
            }
        });
    }

    private void takePhoto() throws IOException {
        binding.buttonText.setVisibility(View.GONE);
        binding.buttonProgress.setVisibility(View.VISIBLE);
        binding.takePhoto.setEnabled(false);

        String fileName = "coin.jpg";
        File file = new File(requireActivity().getFilesDir(), fileName);
        if (file.canRead())
            file.delete();

        ImageCapture.OutputFileOptions options = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(options, imageCaptureExecutor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                if (STATE_RELOAD != null) {
                    try {
                        Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), outputFileResults.getSavedUri());
                        Bitmap cropBitmap = Utils.cropBitmap(sourceBitmap);

                        File file = new File(requireActivity().getFilesDir(), STATE_RELOAD + ".jpg");
                        FileOutputStream fOut = new FileOutputStream(file);
                        cropBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    requireActivity().runOnUiThread(() -> NavHostFragment.findNavController(CameraFragment.this).navigate(R.id.action_cameraFragment_to_cameraCompleteFragment));
                } else {
                    if (STATE.equals("avers")) {
                        binding.title.setText(requireContext().getString(R.string.camera_title2));
                        binding.subtitle.setText(requireContext().getString(R.string.camera_subtitle2));
                    }

                    try {
                        Bitmap sourceBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), outputFileResults.getSavedUri());
                        Bitmap cropBitmap = Utils.cropBitmap(sourceBitmap);

                        File file = new File(requireActivity().getFilesDir(), STATE + ".jpg");
                        FileOutputStream fOut = new FileOutputStream(file);
                        cropBitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    requireActivity().runOnUiThread(() -> {
                        binding.buttonProgress.setVisibility(View.GONE);
                        binding.buttonText.setVisibility(View.VISIBLE);
                        binding.takePhoto.setEnabled(true);
                    });

                    if (STATE.equals("revers"))
                        requireActivity().runOnUiThread(() -> NavHostFragment.findNavController(CameraFragment.this).navigate(R.id.action_cameraFragment_to_cameraCompleteFragment));
                    else
                        STATE = "revers";
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Toast.makeText(requireContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
