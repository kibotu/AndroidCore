package com.exozet.android.core.interfaces.annotations;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static androidx.fragment.app.FragmentTransaction.*;

@IntDef({TRANSIT_NONE, TRANSIT_FRAGMENT_OPEN, TRANSIT_FRAGMENT_CLOSE, TRANSIT_FRAGMENT_FADE})
@Retention(RetentionPolicy.SOURCE)
public @interface Transit {
}