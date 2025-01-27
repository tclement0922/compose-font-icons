/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("unused")

package dev.tclement.dokka.vitepress

import dev.tclement.dokka.vitepress.location.MarkdownLocationProvider
import dev.tclement.dokka.vitepress.renderer.VitePressRenderer
import dev.tclement.dokka.vitepress.renderer.preprocessors.BriefCommentPreprocessor
import dev.tclement.dokka.vitepress.renderer.preprocessors.DeprecationPreprocessor
import dev.tclement.dokka.vitepress.renderer.preprocessors.MultimoduleTablePreprocessor
import dev.tclement.dokka.vitepress.renderer.preprocessors.SymbolPreprocessor
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.renderers.PackageListCreator
import org.jetbrains.dokka.base.resolvers.local.LocationProviderFactory
import org.jetbrains.dokka.base.resolvers.shared.RecognizedLinkFormat
import org.jetbrains.dokka.plugability.*
import org.jetbrains.dokka.renderers.PostAction
import org.jetbrains.dokka.renderers.Renderer
import org.jetbrains.dokka.transformers.pages.PageTransformer

class DokkaVitePressRendererPlugin : DokkaPlugin() {

    val vitePressPreprocessors: ExtensionPoint<PageTransformer> by extensionPoint()

    private val dokkaBase by lazy { plugin<DokkaBase>() }

    val renderer: Extension<Renderer, *, *> by extending {
        CoreExtensions.renderer providing ::VitePressRenderer override dokkaBase.htmlRenderer
    }

    val locationProvider: Extension<LocationProviderFactory, *, *> by extending {
        dokkaBase.locationProviderFactory providing MarkdownLocationProvider::Factory override dokkaBase.locationProvider
    }

    val briefCommentPreprocessor: Extension<PageTransformer, *, *> by extending {
        vitePressPreprocessors with BriefCommentPreprocessor()
    }

    val symbolPreprocessor: Extension<PageTransformer, *, *> by extending {
        vitePressPreprocessors providing ::SymbolPreprocessor order { after(locationProvider) }
    }

    val deprecationPreprocessor: Extension<PageTransformer, *, *> by extending {
        vitePressPreprocessors with DeprecationPreprocessor()
    }

    val multimoduleTablePreprocessor: Extension<PageTransformer, *, *> by extending {
        vitePressPreprocessors with MultimoduleTablePreprocessor()
    }

    val packageListCreator: Extension<PageTransformer, *, *> by extending {
        vitePressPreprocessors providing { PackageListCreator(it, RecognizedLinkFormat.DokkaGFM) }
    }

    internal val alphaVersionNotifier by extending {
        CoreExtensions.postActions providing { ctx ->
            PostAction {
                ctx.logger.info(
                    "The VitePress output format is still in Alpha so you may find bugs and experience migration " +
                            "issues when using it. You use it at your own risk."
                )
            }
        }
    }

    @OptIn(DokkaPluginApiPreview::class)
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}
