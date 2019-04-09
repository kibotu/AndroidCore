@file:JvmName("KoinExtensions")

package com.exozet.android.core.extensions

import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope


/**
 * inject lazily given dependency for Any
 * @param qualifier - bean qualifier / optional
 * @param scope
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> Any.inject(
    qualifier: Qualifier? = null,
    scope: Scope? = null,
    noinline parameters: ParametersDefinition? = null
) = lazy { GlobalContext.get().koin.get<T>(qualifier, scope, parameters) }