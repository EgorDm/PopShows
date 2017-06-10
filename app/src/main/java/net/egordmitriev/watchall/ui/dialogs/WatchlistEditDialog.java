package net.egordmitriev.watchall.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import net.egordmitriev.watchall.R;
import net.egordmitriev.watchall.api.database.tables.WatchlistsTable;
import net.egordmitriev.watchall.appui.widgets.ColorPicker;
import net.egordmitriev.watchall.pojo.watchall.WatchlistModel;

import java.util.ArrayList;

/**
 * Created by EgorDm on 5/13/2016.
 */
public class WatchlistEditDialog extends DialogFragment {

    private EditText mTitleInput;
    private EditText mDescInput;
    private ColorPicker mColorInput;
    private CheckBox mPublicInput;
    public static WatchlistModel sData;
    public JsonObject toAdd;

    private Runnable mCallbackRunnable;

    public static WatchlistEditDialog newInstance(WatchlistModel data) {
        sData = data;
        return new WatchlistEditDialog();
    }

    public static WatchlistEditDialog newInstance(WatchlistModel data, JsonObject toadd) {
        WatchlistEditDialog dialog = newInstance(data);
        dialog.toAdd = toadd;
        return dialog;
    }

    public WatchlistEditDialog() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_watchlist, null);

        mTitleInput = (EditText) dialogView.findViewById(R.id.dialog_edit_title);
        mDescInput = (EditText) dialogView.findViewById(R.id.dialog_edit_description);
        mColorInput = (ColorPicker) dialogView.findViewById(R.id.dialog_edit_color);

        builder.setTitle((sData == null) ? R.string.title_create_list : R.string.title_edit_list);
        builder.setView(dialogView)
                .setPositiveButton((sData == null)
                        ? R.string.dialog_create
                        : R.string.dialog_edit, null)
                .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        WatchlistEditDialog.this.getDialog().cancel();
                    }
                });

        if (sData != null) {
            mTitleInput.setText(sData.getTitle());
            if (sData.detail != null) mDescInput.setText(sData.detail.description);
            mColorInput.setSelectedColor(sData.base.color);
        }

        Dialog ret = builder.create();
        ret.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return ret;
    }

    @Override
    public void onStart() {
        super.onStart();
        final AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean success = true;
                    if (mTitleInput.getText().toString().trim().isEmpty()) {
                        mTitleInput.setError("Title must not be empty!");
                        success = false;
                    }

                    if (success) {
                        if (sData == null) {
                            sData = new WatchlistModel(mTitleInput.getText().toString(), mDescInput.getText().toString(), mColorInput.getColor(), false);
                        } else {
                            sData.base.title = mTitleInput.getText().toString();
                            if (sData.detail == null) {
                                sData.detail = new WatchlistModel.Detail();
                            }
                            sData.detail.description = mDescInput.getText().toString();
                            sData.base.color = mColorInput.getColor();
                            //sData.base.is_public = mPublicInput.isChecked();
                        }

                        if (toAdd != null) {
                            if (sData.detail.list_contents == null) {
                                sData.detail.list_contents = new ArrayList<>();
                            }
                            sData.detail.list_contents.add(toAdd);
                            Toast.makeText(getActivity(), String.format(getString(R.string.toast_added_to_list), sData.base.title), Toast.LENGTH_SHORT).show();
                        }
                        WatchlistsTable.upsert(sData, sData.id);
                        if (mCallbackRunnable != null) {
                            mCallbackRunnable.run();
                        }
                        d.dismiss();
                    }
                }
            });
        }
    }

    public void setCallbackRunnable(Runnable callbackRunnable) {
        mCallbackRunnable = callbackRunnable;
    }
}
