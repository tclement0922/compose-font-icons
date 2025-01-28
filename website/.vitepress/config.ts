/*
 * Copyright 2024-2025 T. Clément (@tclement0922)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import { PrimeVueResolver } from "@primevue/auto-import-resolver";
import Components from "unplugin-vue-components/vite";
import { nodePolyfills as NodePolyfills } from "vite-plugin-node-polyfills";
import { defineConfig } from "vitepress";
import apiSidebar from "../api/sidebar";
import dokkaSymbolTransformer from "./dokkaSymbolTransformer";

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "Compose Font Icons",
  description: "Font icons composables for Compose Multiplatform",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: "Home", link: "/" },
      { text: "Accessors generator", link: "/generator" },
      { text: "API", link: "/api/", activeMatch: "^/api/" },
    ],

    sidebar: {
      "/api/": apiSidebar("/api/"),
    },

    socialLinks: [
      {
        icon: "github",
        link: "https://github.com/tclement0922/compose-font-icons",
      },
    ],
  },
  markdown: {
    codeTransformers: [dokkaSymbolTransformer],
  },
  vite: {
    assetsInclude: ["**/package-list"],
    css: {
      transformer: "lightningcss",
    },
    plugins: [
      Components({
        resolvers: [PrimeVueResolver()],
      }),
      NodePolyfills({
        include: ["buffer"],
        globals: {
          Buffer: true,
        },
      }),
    ],
  },
});
