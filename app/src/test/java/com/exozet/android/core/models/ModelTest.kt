package de.charite.balsam.models

import com.exozet.android.core.base.SerializableTests

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class ModelTest : SerializableTests(TestModel::class.java) {

    override val expectedJson = """
    {
        "message": "test"
    }
    """
}

data class TestModel(var message: String? = null)