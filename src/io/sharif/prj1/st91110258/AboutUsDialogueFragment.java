package io.sharif.prj1.st91110258;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by hvdh_ on 4/4/2016.
 */
public class AboutUsDialogueFragment extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.about_us_message)
                .setTitle(R.string.about_us_title);
        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        this.dismiss();
    }
}
