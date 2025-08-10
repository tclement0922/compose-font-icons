<!--
  - Copyright 2024-2025 T. Clément (@tclement0922)
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -      http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<script setup lang="ts">
import { computedAsync, useElementSize } from "@vueuse/core";
import { create, Font, Glyph } from "fontkit";
import { FileUploadSelectEvent, FloatLabel } from "primevue";
import { Content } from "vitepress";
import {computed, markRaw, ref, shallowRef, toRaw, useTemplateRef, watch} from "vue";
import { toPascalCase } from "../casing";
import generateZip from "../generateZip";

const file = shallowRef<File>();

const loadingFile = ref(false);
const font = computedAsync<Font | null>(
  async () => {
    if (!file.value) return null;
    const arrayBuffer = await file.value.arrayBuffer();
    return markRaw(create(Buffer.from(arrayBuffer)) as Font);
  },
  null,
  loadingFile,
);

const glyphs = computed<Glyph[]>(() => {
  return font.value?.characterSet
    ? font.value.characterSet
        .map((codePoint) => font.value!.glyphForCodePoint(codePoint))
        .filter(
          (glyph1, i, arr) =>
            arr.findIndex((glyph2) => glyph2.name === glyph1.name) === i,
        )
    : [];
});

const defaultDisabledGlyphs = [
  "CR",
  "0",
  "1",
  "2",
  "3",
  "4",
  "5",
  "6",
  "7",
  "8",
  "9",
  "digit_zero",
  "digit_one",
  "digit_two",
  "digit_three",
  "digit_four",
  "digit_five",
  "digit_six",
  "digit_seven",
  "digit_eight",
  "digit_nine",
  "a",
  "b",
  "c",
  "d",
  "e",
  "f",
  "g",
  "h",
  "i",
  "j",
  "k",
  "l",
  "m",
  "n",
  "o",
  "p",
  "q",
  "r",
  "s",
  "t",
  "u",
  "v",
  "w",
  "x",
  "y",
  "z",
  "uni00A0",
  ".notdef",
  "notdef",
]

const disabledGlyphs = ref<string[]>([]);

watch(glyphs, (glyphs) => {
  disabledGlyphs.value = defaultDisabledGlyphs.filter((name) => glyphs.find(g => g.name === name))
})

const glyphsContainer = useTemplateRef("glyphsContainer");
const { width: glyphsContainerWidth } = useElementSize(glyphsContainer);

const glyphsFirst = ref(0);
const glyphsPerPageCount = computed(() => {
  // (width + 8 for additional gap) / (32 for icon size + 16 * 2 for icon padding + 2 for border)
  const perRowCount = Math.floor((glyphsContainerWidth.value + 8) / 74);
  return perRowCount * 8;
});

type ExportFileStructure = "singleFile" | "twoFiles" | "multipleFiles";

type ExportTarget = "android" | "multiplatform";

export interface ExportData {
  font: Font;
  fontFile: File;
  disabledGlyphs: string[];
  createSubset: boolean;
  objectName: string;
  packageName: string;
  kotlinSourceDirName: string;
  fileStructure: ExportFileStructure;
  target: ExportTarget;
  resClass: string;
  includeFontFile: boolean;
  iconMap: boolean;
  explicitApi: boolean;
}

const exportData = ref<ExportData>();

function onExportClick() {
  exportData.value = {
    font: toRaw(font.value!),
    fontFile: file.value!,
    disabledGlyphs: disabledGlyphs.value,
    createSubset: Object.keys(font.value!.variationAxes).length === 0,
    objectName: toPascalCase(font.value!.familyName),
    kotlinSourceDirName: "kotlin",
    fileStructure: "singleFile",
    packageName: "",
    target: "multiplatform",
    resClass: "",
    includeFontFile: true,
    iconMap: false,
    explicitApi: false,
  };
}

const exportErrors = computed(() => {
  const data = exportData.value;
  const errors: string[] = [];

  if (data) {
    if (!data.objectName) {
      errors.push("Object name is required");
    } else if (!data.objectName.match(/^[a-zA-Z_$][a-zA-Z\d_$]*$/)) {
      errors.push("Object name is invalid");
    }
    if (!data.kotlinSourceDirName) {
      errors.push("Kotlin source directory name is required");
    } else if (!data.kotlinSourceDirName.match(/^\S+$/)) {
      errors.push("Kotlin source directory name is invalid");
    }
    if (!data.fileStructure) {
      errors.push("File structure is required");
    }
    if (!data.target) {
      errors.push("Target is required");
    }
    if (data.target === "multiplatform" && !data.resClass) {
      errors.push("Res class full name is required");
    } else if (
      data.target === "multiplatform" &&
      !data.resClass.match(
        /^([a-zA-Z_$][a-zA-Z\d_$]*\.)*[a-zA-Z_$][a-zA-Z\d_$]*$/,
      )
    ) {
      errors.push("Res class full name is invalid");
    }
    if (!data.packageName) {
      errors.push("Package name is required");
    } else if (
      !data.packageName.match(
        /^([a-zA-Z_$][a-zA-Z\d_$]*\.)*[a-zA-Z_$][a-zA-Z\d_$]*$/,
      )
    ) {
      errors.push("Package name is invalid");
    }
  }

  return errors;
});
const exportWarnings = computed(() => {
  const data = exportData.value;
  const warnings: string[] = [];

  if (data) {
    if (data.objectName.match(/^[a-z_]/)) {
      warnings.push(
        "Object name doesn't follow Kotlin naming conventions, it should start with an uppercase letter",
      );
    }
    if (
      data.kotlinSourceDirName !== "kotlin" &&
      data.kotlinSourceDirName !== "java"
    ) {
      warnings.push(
        "Kotlin source directory is usually named 'kotlin' or 'java'",
      );
    }
    if (data.fileStructure === "multipleFiles") {
      warnings.push(
        "Compile-time performance may be impacted by using multiple files since there will be one file per icon",
      );
    }
    if (data.target === "multiplatform") {
      warnings.push(
        "Multiplatform target requires Compose Multiplatform Resources to be enabled",
      );
    }
  }

  return warnings;
});
</script>

<template>
  <div class="GeneratorPage vp-doc">
    <Content />
    <div>
      <FileUpload
        custom-upload
        @select="(E: FileUploadSelectEvent) => (file = E.files[0] || undefined)"
        placeholder="Choose a font file"
        mode="basic"
        :maxFileSize="1000000000"
      />
      <span v-show="loadingFile">Loading...</span>
    </div>

    <template v-if="font">
      <h2>Font details</h2>
      <div class="font-details">
        <FloatLabel variant="on">
          <label for="postscript-name">Postscript name</label>
          <InputText
            id="postscript-name"
            v-model="font.postscriptName"
            fluid
            readonly
          />
        </FloatLabel>
        <FloatLabel variant="on">
          <label for="full-name">Full name</label>
          <InputText id="full-name" v-model="font.fullName" fluid readonly />
        </FloatLabel>
        <FloatLabel variant="on">
          <label for="family-name">Family name</label>
          <InputText
            id="family-name"
            v-model="font.familyName"
            fluid
            readonly
          />
        </FloatLabel>
        <FloatLabel variant="on">
          <label for="subfamily-name">Subfamily name</label>
          <InputText
            id="subfamily-name"
            v-model="font.subfamilyName"
            fluid
            readonly
          />
        </FloatLabel>
        <FloatLabel variant="on">
          <label for="version-name">Version</label>
          <InputText id="version-name" v-model="font.version" fluid readonly />
        </FloatLabel>
        <FloatLabel variant="on">
          <label for="copyright-name">Copyright</label>
          <InputText
            id="copyright-name"
            v-model="font.copyright"
            fluid
            readonly
          />
        </FloatLabel>
      </div>
      <template v-if="Object.keys(font.variationAxes).length">
        <h2>Variation axes</h2>
        <div class="variations">
          <Chip
            v-for="(variation, key) in font.variationAxes"
            :key
            :label="variation.name + ' (' + key + ')'"
          />
        </div>
      </template>
      <h2>Glyphs</h2>
      <div class="flex-row">
        <p>
          Click on a glyph to select/unselect it. Unselected glyphs won't be
          exported.
        </p>
        <p>
          Selected: <code>{{ glyphs.length - disabledGlyphs.length }}</code
          >/<code>{{ glyphs.length }}</code>
        </p>
      </div>
      <div class="glyphs-container" ref="glyphsContainer">
        <Button
          v-for="glyph in glyphs.slice(
            glyphsFirst,
            glyphsFirst + glyphsPerPageCount,
          )"
          :key="glyph.name"
          v-tooltip.top="
            glyph.name +
            ' (' +
            glyph.codePoints
              .map((cp) => 'U+' + cp.toString(16).toUpperCase())
              .join(', ') +
            ')'
          "
          :class="{
            disabled: disabledGlyphs.includes(glyph.name),
          }"
          @click="
            disabledGlyphs.includes(glyph.name)
              ? disabledGlyphs.splice(disabledGlyphs.indexOf(glyph.name), 1)
              : disabledGlyphs.push(glyph.name)
          "
        >
          <svg
            width="32px"
            height="32px"
            :viewBox="
              glyph.bbox.minX +
              ' ' +
              glyph.bbox.minY +
              ' ' +
              glyph.bbox.width +
              ' ' +
              glyph.bbox.height
            "
          >
            <path fill="currentColor" :d="glyph.path.toSVG()" />
          </svg>
        </Button>
      </div>
      <Paginator
        v-model:first="glyphsFirst"
        :rows="glyphsPerPageCount"
        :total-records="glyphs.length"
      />
      <Button @click="onExportClick">Export</Button>
    </template>
    <Dialog
      :visible="!!exportData"
      @update:visible="(v) => !v && (exportData = undefined)"
      modal
      header="Export"
      class="GeneratorPage-export-dialog"
    >
      <div>
        <FloatLabel variant="on">
          <InputText
            id="object-name"
            v-model="(exportData ?? {}).objectName"
            fluid
          />
          <label for="object-name">Object name</label>
        </FloatLabel>
        <FloatLabel variant="on">
          <InputText
            id="package-name"
            v-model="(exportData ?? {}).packageName"
            fluid
          />
          <label for="package-name">Package name</label>
        </FloatLabel>
        <FloatLabel variant="on">
          <InputText
            id="kotlin-source-dir-name"
            v-model="(exportData ?? {}).kotlinSourceDirName"
            fluid
          />
          <label for="kotlin-source-dir-name"
            >Kotlin source directory name</label
          >
        </FloatLabel>
        <FloatLabel variant="on">
          <Select
            id="file-structure"
            v-model="(exportData ?? {}).fileStructure"
            :options="[
              { label: 'Single file', value: 'singleFile' },
              { label: 'Two files', value: 'twoFiles' },
              { label: 'Multiple files', value: 'multipleFiles' },
            ]"
            optionLabel="label"
            optionValue="value"
            fluid
          />
          <label for="file-structure">File structure</label>
        </FloatLabel>
        <FloatLabel variant="on">
          <Select
            id="target"
            v-model="(exportData ?? {}).target"
            :options="[
              { label: 'Android', value: 'android' },
              { label: 'Multiplatform', value: 'multiplatform' },
            ]"
            optionLabel="label"
            optionValue="value"
            fluid
          />
          <label for="target">Target</label>
        </FloatLabel>
        <FloatLabel variant="on">
          <InputText
            id="res-class"
            v-model="(exportData ?? {}).resClass"
            fluid
          />
          <label for="res-class">Res class full name</label>
        </FloatLabel>
        <div class="switch-input">
          <ToggleSwitch
            v-model="(exportData ?? {}).includeFontFile"
            input-id="include-font-file"
          />
          <label for="include-font-file">Include font file</label>
        </div>
        <div class="switch-input">
          <ToggleSwitch
            v-model="(exportData ?? {}).createSubset"
            input-id="create-subset"
            :disabled="
              (exportData ?? {}).includeFontFile !== true ||
              Object.keys(font.variationAxes).length > 0
            "
          />
          <label for="create-subset">Create subset</label>
        </div>
        <div class="switch-input">
          <ToggleSwitch
            v-model="(exportData ?? {}).iconMap"
            input-id="icon-map"
          />
          <label for="icon-map">Icon map</label>
        </div>
        <div class="switch-input">
          <ToggleSwitch
            v-model="(exportData ?? {}).explicitApi"
            input-id="explicit-api"
          />
          <label for="explicit-api">Explicit API</label>
        </div>
      </div>
      <div>
        <ul class="messages-list">
          <li v-for="error in exportErrors" class="message error">
            {{ error }}
          </li>
          <li v-for="warning in exportWarnings" class="message warning">
            {{ warning }}
          </li>
        </ul>
        <Button
          label="Download"
          icon="pi pi-download"
          :disabled="exportData && exportErrors.length > 0"
          @click="exportData ? generateZip(exportData) : undefined"
        />
      </div>
    </Dialog>
    <div v-show="!font && !file">No font selected</div>
    <div v-show="!font && !!file">The file you picked is not supported. The supported types are: TTF, OTF, WOFF and WOFF2.</div>
  </div>
</template>

<style>
.GeneratorPage {
  max-width: var(--vp-layout-max-width);
  margin: 16px auto;
  padding: 0 48px;

  .font-details {
    margin-top: 16px;
    display: grid;
    grid-template-columns: 1fr 1fr 1fr;
    gap: 16px;
  }

  @media (max-width: 768px) {
    .font-details {
      grid-template-columns: 1fr 1fr;
    }
  }

  @media (max-width: 576px) {
    .font-details {
      grid-template-columns: 1fr;
    }
  }

  .variations {
    margin-top: 16px;
    display: flex;
    flex-wrap: wrap;
    gap: 16px;
  }

  .glyphs-container {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    gap: 8px;
    justify-content: center;

    svg {
      scale: 1 -1;
    }

    .p-button {
      background: var(--p-form-field-background);
      color: var(--p-form-field-color);
      border-color: var(--p-inputtext-border-color);
      padding: 16px;

      &.disabled {
        opacity: 0.33;
        border-color: var(--p-red-400);
      }
    }
  }

  .p-paginator {
    --p-paginator-background: transparent;
  }

  .flex-row {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
  }

  h1 {
    margin: 20px 0 0;
  }

  h2 {
    border: none;
    padding: 0;
    margin: 16px 0 0;
  }

  h3 {
    margin: 14px 0 0;
  }

  h4 {
    margin: 12px 0 0;
  }

  h5 {
    margin: 10px 0 0;
  }

  h6 {
    margin: 10px 0 0;
  }

  nav {
    margin: 16px 0 0;
  }
}

.GeneratorPage-export-dialog {
  width: 800px;

  .p-dialog-content {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 16px;

    > div:first-child {
      display: grid;
      gap: 16px;
      padding: 8px 0;
      grid-template-columns: 1fr;
    }

    > div:last-child {
      display: flex;
      flex-direction: column;
      gap: 16px;
      padding: 8px 0;
      justify-content: space-between;
    }
  }

  .messages-list {
    .message {
      list-style-type: none;
      padding: 8px;
      margin-bottom: 8px;
      border-radius: var(--p-form-field-border-radius);
      background: var(--message-background);
      color: var(--message-color);
    }

    .message:last-child {
      margin-bottom: 0;
    }

    .error {
      --message-background: color-mix(
        in srgb,
        var(--p-red-500),
        transparent 90%
      );
      --message-color: var(--p-red-800);
    }

    .warning {
      --message-background: color-mix(
        in srgb,
        var(--p-yellow-500),
        transparent 90%
      );
      --message-color: var(--p-yellow-800);
    }
  }

  .switch-input {
    display: flex;
    align-items: center;
    gap: 8px;
  }
}

.dark .GeneratorPage-export-dialog .messages-list {
  .error {
    --message-color: var(--p-red-200);
  }

  .warning {
    --message-color: var(--p-yellow-200);
  }
}

@media (max-width: 800px) {
  .GeneratorPage-export-dialog {
    width: calc(100svw - 32px);

    .p-dialog-content {
      grid-template-columns: 1fr;
    }
  }
}
</style>
