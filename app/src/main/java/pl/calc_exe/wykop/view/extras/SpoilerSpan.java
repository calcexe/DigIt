package pl.calc_exe.wykop.view.extras;

import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Mateusz on 2016-10-12.
 */

public class SpoilerSpan extends ClickableSpan {
    private final CharSequence value;

    public SpoilerSpan(CharSequence value) {
        this.value = value;
    }

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(view.getContext())
                .setTitle("Spoiler")
                .setMessage(Html.fromHtml(String.valueOf(value)))
                .show();
    }

    public CharSequence getValue() {
        return value;
    }
}
