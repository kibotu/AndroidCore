package com.exozet.android.core.storage

import com.google.firebase.database.FirebaseDatabase

/**
 * Created by <a href="https://about.me/janrabe">Jan Rabe</a>.
 */

object FirebaseDatabaseHelper {

    val database by lazy { FirebaseDatabase.getInstance() }
}