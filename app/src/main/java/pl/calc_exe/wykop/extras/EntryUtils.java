package pl.calc_exe.wykop.extras;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;

import org.greenrobot.eventbus.EventBus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.calc_exe.wykop.events.ReplaceFragmentEvent;
import pl.calc_exe.wykop.presenter.extras.EntryTagHandler;
import pl.calc_exe.wykop.view.fragments.WebViewFragment;

/**
 * Created by Mateusz on 2016-11-05.
 */

public class EntryUtils {

    private static EntryTagHandler tagHandler = new EntryTagHandler();

    public static Spanned htmlToSpanned(String body){
        //Sometimes fromHtml is crashing, so I need to add this two lines.
        //Don't ask why, copy - paste from stackoverflow.
        body = body.replace("<^[stu]", " <");
        body = body.replace("  ", " ");

        //Replace APIs spoiler code to my own.

        Pattern pattern = Pattern.compile("(?s)<code class=\"dnone\">(.*?)</code>");
        Matcher matcher = pattern.matcher(body);
        while (matcher.find()){
            body = matcher.replaceAll("<spoiler>" + Html.fromHtml(matcher.group(1)) + "</spoiler>");
        }

        //Replace APIs user link code to my own.
        body = body.replaceAll("(?s)<a href=\"@(.*?)\">.*?</a>", "<user>$1</user>");

        //Replace APIs hashtag link code to my own.
        body = body.replaceAll("(?s)<a href=\"#(.*?)\">.*?</a>", "<tag>$1</tag>");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(body, Html.FROM_HTML_MODE_LEGACY, null, tagHandler);
        } else{
            return Html.fromHtml(body, null, tagHandler);
        }

    }

    public static void showUser(String author){
        Fragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uri", "http://www.wykop.pl/ludzie/" + author);
        fragment.setArguments(bundle);
        EventBus.getDefault().post(new ReplaceFragmentEvent(fragment));
    }

}
