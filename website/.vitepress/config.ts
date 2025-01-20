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
