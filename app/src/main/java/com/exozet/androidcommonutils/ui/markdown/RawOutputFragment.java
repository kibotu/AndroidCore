package com.exozet.androidcommonutils.ui.markdown;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.common.android.utils.logging.Logger;
import com.exozet.basejava.R;
import com.exozet.basejava.ui.BaseFragment;
import com.yydcdut.rxmarkdown.RxMDConfiguration;
import com.yydcdut.rxmarkdown.RxMarkdown;
import com.yydcdut.rxmarkdown.loader.DefaultLoader;
import com.yydcdut.rxmarkdown.syntax.text.TextFactory;

import butterknife.BindView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.text.TextUtils.isEmpty;

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class RawOutputFragment extends BaseFragment {

    public static String RAW_OUTPUT_TEXT = "RAW_OUTPUT_TEXT";

    @BindView(R.id.content)
    TextView content;

    @Override
    protected int getLayout() {
        return R.layout.fragment_raw_output;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String raw = bundle.getString(RAW_OUTPUT_TEXT);
            if (isEmpty(raw))
                return;

            Logger.v(tag(), raw);
            setMarkdown(raw, content);
        }
    }

    public static void setMarkdown(String text, @NonNull final TextView label) {

        RxMDConfiguration rxMDConfiguration = new RxMDConfiguration.Builder(label.getContext())
                .setDefaultImageSize(100, 100) //default image width & height
                .setBlockQuotesColor(Color.LTGRAY) //default color of block quotes
                .setHeader1RelativeSize(1.6f) //default relative size of header1
                .setHeader2RelativeSize(1.5f) //default relative size of header2
                .setHeader3RelativeSize(1.4f) //default relative size of header3
                .setHeader4RelativeSize(1.3f) //default relative size of header4
                .setHeader5RelativeSize(1.2f) //default relative size of header5
                .setHeader6RelativeSize(1.1f) //default relative size of header6
                .setHorizontalRulesColor(Color.LTGRAY) //default color of horizontal rules's background
                .setInlineCodeBgColor(Color.LTGRAY) //default color of inline code's background
                .setCodeBgColor(Color.LTGRAY) //default color of code's background
                .setTodoColor(Color.DKGRAY) //default color of todo
                .setTodoDoneColor(Color.DKGRAY) //default color of done
                .setUnOrderListColor(Color.WHITE) //default color of unorder list
                .setLinkColor(Color.RED) //default color of link text
                .setLinkUnderline(true) //default value of whether displays link underline
                .setRxMDImageLoader(new DefaultLoader(label.getContext())) //default image loader
                .setDebug(true) //default value of debug
                .setOnLinkClickCallback((view1, link) -> {
                    //link click callback
                })
                .build();

        RxMarkdown.with(text, label.getContext())
                .config(rxMDConfiguration)
                .factory(TextFactory.create())
                .intoObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        label.setText(charSequence, TextView.BufferType.SPANNABLE);
                    }
                });
    }
}
