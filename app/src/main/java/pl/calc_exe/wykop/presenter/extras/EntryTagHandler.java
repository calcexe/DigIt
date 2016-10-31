package pl.calc_exe.wykop.presenter.extras;

import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;

import org.xml.sax.XMLReader;

import pl.calc_exe.wykop.view.extras.SpoilerSpan;
import pl.calc_exe.wykop.view.extras.EntrySpan;

/**
 * Handles custom tags, usually not supported by Html.fromHtml().
 */
public class EntryTagHandler implements Html.TagHandler {

    private Object getLast(Editable text, Class kind) {
        Object[] objects = text.getSpans(0, text.length(), kind);
        if (objects.length == 0) {
            return null;
        } else {
            for (int i = objects.length; i > 0; i--) {
                if (text.getSpanFlags(objects[i - 1]) == Spannable.SPAN_MARK_MARK) {
                    return objects[i - 1];
                }
            }
            return null;
        }
    }

    //TODO: Change implementation of this method. Remove repeated lines and moves them to separate method.
    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {

        switch (tag) {
            case "spoiler":
                if (opening) {
                    output.setSpan(null, output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                } else {
                    Object obj = getLast(output, SpoilerSpan.class);
                    int where = output.getSpanStart(obj);
                    output.setSpan(new SpoilerSpan(output.subSequence(where, output.length())), where, output.length(), 0);
                    output.replace(where, output.length(), "[SPOILER]");
                }
                break;
            case "tag":
                if (opening) {
                    output.setSpan(null, output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                } else {
                    Object obj = getLast(output, EntrySpan.class);
                    int where = output.getSpanStart(obj);

                    output.setSpan(new EntrySpan(output.subSequence(where, output.length()), EntrySpan.Type.HASH_TAG),
                            where, output.length(), 0);
                }
                break;
            case "user":
                if (opening) {
                    output.setSpan(null, output.length(), output.length(), Spannable.SPAN_MARK_MARK);
                } else {
                    Object obj = getLast(output, EntrySpan.class);
                    int where = output.getSpanStart(obj);

                    output.setSpan(new EntrySpan(output.subSequence(where, output.length()), EntrySpan.Type.USER),
                            where, output.length(), 0);
                }
                break;
        }
    }

}