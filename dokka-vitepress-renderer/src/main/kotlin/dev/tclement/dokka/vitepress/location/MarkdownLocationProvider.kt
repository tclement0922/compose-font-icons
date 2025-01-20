/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.tclement.dokka.vitepress.location

import org.jetbrains.dokka.base.resolvers.local.DokkaLocationProvider
import org.jetbrains.dokka.base.resolvers.local.LocationProvider
import org.jetbrains.dokka.base.resolvers.local.LocationProviderFactory
import org.jetbrains.dokka.pages.RootPageNode
import org.jetbrains.dokka.plugability.DokkaContext

class MarkdownLocationProvider(
    root: RootPageNode,
    dokkaContext: DokkaContext
) : DokkaLocationProvider(root, dokkaContext, ".md") {
    class Factory(private val context: DokkaContext) : LocationProviderFactory {
        override fun getLocationProvider(pageNode: RootPageNode): LocationProvider =
            MarkdownLocationProvider(pageNode, context)
    }
}
