package com.helpplusapp.amit.helpplus;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by amit on 7/31/2016.
 */
public class AddTagDialogFragment extends DialogFragment {

    private EditText mEditText;

    public AddTagDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static AddTagDialogFragment newInstance() {
        AddTagDialogFragment f = new AddTagDialogFragment();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_addtag, container);
        mEditText = (EditText) view.findViewById(R.id.txt_your_name);
        getDialog().setTitle("Add tag");

        return view;
    }
}
