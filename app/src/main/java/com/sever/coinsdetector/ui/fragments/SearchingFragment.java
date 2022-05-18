package com.sever.coinsdetector.ui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.sever.coinsdetector.App;
import com.sever.coinsdetector.R;
import com.sever.coinsdetector.database.Coin;
import com.sever.coinsdetector.databinding.SearchingFragmentBinding;
import com.sever.coinsdetector.ml.Model;
import com.sever.coinsdetector.ui.dialogs.NotFoundDialog;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SearchingFragment extends Fragment {
    private SearchingFragmentBinding binding;
    private List<String> labels;
    private String year, name;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            year = getArguments().getString("year");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchingFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        File avers = new File(requireActivity().getFilesDir(), "avers.jpg");
        if (avers.canRead()) {
            Bitmap bitmap = BitmapFactory.decodeFile(requireActivity().getFilesDir().getAbsolutePath() + "/avers.jpg");
            if (bitmap != null) {
                try {
                    checkPhotoInML(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        binding.animation.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                Coin coin = App.getInstance().getDatabase().coinDao().getByNameAndYear(name, year);
                if (coin != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", name);
                    bundle.putString("year", year);

                    NavHostFragment.findNavController(SearchingFragment.this).navigate(R.id.action_searchingFragment_to_aboutCoinFragment, bundle);
                } else {
                    binding.animation.cancelAnimation();
                    new NotFoundDialog(requireActivity()).openDialog();
                }
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
            }
        });
    }

    private void checkPhotoInML(@NonNull Bitmap bitmap) throws IOException {
        Model model = Model.newInstance(requireContext());
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();

        TensorImage image = new TensorImage(DataType.FLOAT32);
        image.load(bitmap);
        image = imageProcessor.process(image);

        Model.Outputs outputs = model.process(image);
        TensorBuffer outputFeature0 = outputs.getLocationAsTensorBuffer();
        showResult(outputFeature0);
        model.close();
    }

    private void showResult(@NonNull TensorBuffer buffer) {
        try {
            labels = FileUtil.loadLabels(requireContext(), "data.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Float> labeledProbability = new TensorLabel(labels, buffer).getMapWithFloatValue();
        float maxValueInMap = (Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet())
            if (entry.getValue() == maxValueInMap)
                name = entry.getKey();
    }

    @NonNull
    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }

    @Override
    public void onDestroyView() {
        binding.animation.removeAllAnimatorListeners();
        binding = null;
        super.onDestroyView();
    }
}
