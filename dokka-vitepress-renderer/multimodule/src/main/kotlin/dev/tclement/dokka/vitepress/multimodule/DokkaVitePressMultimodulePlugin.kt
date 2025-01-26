/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("unused")

package dev.tclement.dokka.vitepress.multimodule

import dev.tclement.dokka.vitepress.DokkaVitePressRendererPlugin
import dev.tclement.dokka.vitepress.location.MarkdownLocationProvider
import dev.tclement.dokka.vitepress.multimodule.location.MarkdownMultimoduleLocationProvider
import dev.tclement.dokka.vitepress.multimodule.renderer.VitePressMultimoduleRenderer
import org.jetbrains.dokka.CoreExtensions
import org.jetbrains.dokka.allModulesPage.AllModulesPagePlugin
import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.resolvers.local.LocationProviderFactory
import org.jetbrains.dokka.plugability.DokkaPlugin
import org.jetbrains.dokka.plugability.DokkaPluginApiPreview
import org.jetbrains.dokka.plugability.Extension
import org.jetbrains.dokka.plugability.PluginApiPreviewAcknowledgement
import org.jetbrains.dokka.renderers.Renderer
import org.jetbrains.dokka.templates.TemplateProcessingStrategy
import org.jetbrains.dokka.templates.TemplatingPlugin

class DokkaVitePressMultimodulePlugin : DokkaPlugin() {
    private val dokkaBase by lazy { plugin<DokkaBase>() }
    private val vitePressRendererPlugin by lazy { plugin<DokkaVitePressRendererPlugin>() }
    private val allModulesPagePlugin by lazy { plugin<AllModulesPagePlugin>() }
    private val templatingPlugin by lazy { plugin<TemplatingPlugin>() }

    val multimoduleRenderer: Extension<Renderer, *, *> by extending {
        CoreExtensions.renderer providing ::VitePressMultimoduleRenderer override listOf(
            dokkaBase.htmlRenderer,
            vitePressRendererPlugin.renderer
        )
    }

    val multimoduleLocationProvider: Extension<LocationProviderFactory, *, *> by extending {
        dokkaBase.locationProviderFactory providing MarkdownMultimoduleLocationProvider::Factory override listOf(
            vitePressRendererPlugin.locationProvider,
            allModulesPagePlugin.multimoduleLocationProvider
        )
    }

    val partialLocationProvider: Extension<LocationProviderFactory, *, *> by extending {
        allModulesPagePlugin.partialLocationProviderFactory providing MarkdownLocationProvider::Factory override allModulesPagePlugin.baseLocationProviderFactory
    }

    val templateProcessingStrategy: Extension<TemplateProcessingStrategy, *, *> by extending {
        (templatingPlugin.templateProcessingStrategy
                providing ::VitePressTemplateProcessingStrategy
                order { before(templatingPlugin.fallbackProcessingStrategy) })
    }

    @OptIn(DokkaPluginApiPreview::class)
    override fun pluginApiPreviewAcknowledgement(): PluginApiPreviewAcknowledgement =
        PluginApiPreviewAcknowledgement
}
