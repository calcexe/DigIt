package pl.calc_exe.wykop.view.extras;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.style.ClickableSpan;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.extras.Pages;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;

/**
 * Base class for supporting clickable Spans inside TextView.
 */
//TODO: Add separate views to display every type of Span instead of WebView.
public class EntrySpan extends ClickableSpan {

    private CharSequence value;
    private Type type;

    public EntrySpan(CharSequence value, Type type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public void onClick(View view) {
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();

        StringBuilder sb = new StringBuilder();
        switch (type){
            case HASH_TAG:
                sb.append("http://www.wykop.pl/tag/");
                break;
            case USER:
                sb.append("http://www.wykop.pl/ludzie/");
                break;
            case ENTRY:
                sb.append("http://www.wykop.pl/wpis/");
                break;
        }
        sb.append(value);

        bundle.putString("uri", sb.toString());
        fragment.setArguments(bundle);

        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

    public enum Type{HASH_TAG, USER, ENTRY}
}
