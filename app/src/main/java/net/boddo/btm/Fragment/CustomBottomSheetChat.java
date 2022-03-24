package net.boddo.btm.Fragment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import net.boddo.btm.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CustomBottomSheetChat extends BottomSheetDialogFragment {


    @BindView(R.id.image_list_recycler_view)
    RecyclerView recyclerView;



     @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getParentFragment().getContext()).inflate(R.layout.custom_chat_bottom_sheet_layout, container, false);
        ButterKnife.bind(this, view);

        loadAllImage();

        return view;
    }

    private void loadAllImage() {


    }


}
