package com.exozet.androidcommonutils.ui.markdown;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.exozet.basejava.R;
import com.exozet.basejava.ui.BaseFragment;
import com.mukesh.MarkdownView;

import butterknife.BindView;

import static android.text.TextUtils.isEmpty;

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

public class MarkdownFragment extends BaseFragment {

    public static final String MARKDOWN_FILENAME = "MARKDOWN_FILENAME";
    public static final String MARKDOWN_URL = "MARKDOWN_URL";

    @BindView(R.id.markdown_view)
    MarkdownView markdownView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_markdown;
    }

    @Override
    public int onEnterStatusBarColor() {
        return R.color.gray;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null && !isEmpty(arguments.getString(MARKDOWN_FILENAME)))
            markdownView.loadMarkdownFromAssets(arguments.getString(MARKDOWN_FILENAME));
        else if (arguments != null && !isEmpty(arguments.getString(MARKDOWN_URL)))
            markdownView.loadUrl(arguments.getString(MARKDOWN_URL));
    }
}
