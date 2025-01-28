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

import { saveAs } from "file-saver";
import { Glyph } from "fontkit";
import JSZip from "jszip";
import { toCamelCase, toPascalCase, toSnakeCase } from "./casing";
import type { ExportData } from "./components/GeneratorPage.vue";

function maybePublic(explicitApi: ExportData["explicitApi"]) {
  return explicitApi ? "public " : "";
}

function isVariable(font: ExportData["font"]) {
  return Object.keys(font.variationAxes).length > 0;
}

function variations(font: ExportData["font"]) {
  const variations: {
    name: string;
    axis: string;
    type: "Boolean" | "Int" | "Float";
    default: string;
  }[] = [];
  for (const axis in font.variationAxes) {
    if (axis === "wght" || axis === "opsz") continue;
    const axisData = font.variationAxes[axis];
    let type: "Boolean" | "Int" | "Float";
    let defaultValue: string;
    if (
      axisData.min === 0 &&
      axisData.max === 1 &&
      (axisData.default === 0 || axisData.default === 1)
    ) {
      type = "Boolean";
      defaultValue = axisData.default === 1 ? "true" : "false";
    } else if (
      Number.isInteger(axisData.min) &&
      Number.isInteger(axisData.max) &&
      Number.isInteger(axisData.default)
    ) {
      type = "Int";
      defaultValue = axisData.default.toString();
    } else {
      type = "Float";
      defaultValue = Number.isInteger(axisData.default)
        ? axisData.default + ".0f"
        : axisData.default.toString() + "f";
    }
    variations.push({
      name: toCamelCase(axisData.name),
      axis,
      type,
      default: defaultValue,
    });
  }
  return variations;
}

function functionParameters(font: ExportData["font"]) {
  const variationParameters = variations(font).map(
    ({ name, type, default: defaultValue }) =>
      `${name}: ${type} = ${defaultValue}`,
  );
  return [...variationParameters, "fontFeatureSettings: String? = null"].join(
    ", ",
  );
}

function functionType(font: ExportData["font"]) {
  return isVariable(font) ? "VariableIconFont" : "StaticIconFont";
}

function functionBody(
  font: ExportData["font"],
  fontFileName: string,
  indent: string = "",
) {
  if (isVariable(font)) {
    const minWeight = font.variationAxes.wght?.min ?? 100;
    const maxWeight = font.variationAxes.wght?.max ?? 900;
    const weights = [minWeight];
    for (let w = minWeight + 100; w <= maxWeight; w += 100) {
      weights.push(w);
    }
    const variationValues: string[] = [];
    for (const { name, axis, type } of variations(font)) {
      let value: string;
      switch (type) {
        case "Boolean":
          value = `if (${name}) 1f else 0f`;
          break;
        case "Int":
          value = `${name}.toFloat()`;
          break;
        case "Float":
          value = name;
          break;
      }
      variationValues.push(`FontVariation.Setting("${axis}", ${value})`);
    }
    return `
${indent}rememberVariableIconFont(
${indent}    fontResource = Res.font.${fontFileName},
${indent}    weights = arrayOf(${weights.map((w) => `FontWeight(${w})`).join(", ")}),
${indent}    fontVariationSettings = arrayOf(${variationValues.join(", ")}),
${indent}    fontFeatureSettings = fontFeatureSettings
${indent})`;
  } else {
    return `
${indent}rememberStaticIconFont(
${indent}    fontResource = Res.font.${fontFileName},
${indent}    fontFeatureSettings = fontFeatureSettings
${indent})`;
  }
}

function icons(glyphs: Glyph[]) {
  return glyphs.map(({ name, codePoints }) => [
    name,
    "\\u" + codePoints[0].toString(16).padStart(4, "0"),
  ]);
}

function mainFileContent(
  font: ExportData["font"],
  objectName: ExportData["objectName"],
  packageName: ExportData["packageName"],
  fileStructure: ExportData["fileStructure"],
  explicitApi: ExportData["explicitApi"],
  glyphs: Glyph[],
  target: ExportData["target"],
  resClass: ExportData["resClass"],
  iconMap: boolean,
) {
  let imports: string;
  if (fileStructure !== "multipleFiles") {
    imports = `

import androidx.compose.runtime.Composable`;
    if (isVariable(font)) {
      imports += `
import dev.tclement.fonticons.VariableIconFont
import dev.tclement.fonticons.rememberVariableIconFont
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight`;
    } else {
      imports += `
import dev.tclement.fonticons.StaticIconFont
import dev.tclement.fonticons.rememberStaticIconFont`;
    }
    if (resClass) {
      imports += `
import ${resClass} as Res`;
      if (target === "multiplatform")
        imports += `
import ${resClass.slice(0, resClass.lastIndexOf("."))}.${toSnakeCase(objectName)}`;
    }
  } else {
    if (resClass) {
      imports = `
import ${resClass} as Res`;
      if (target === "multiplatform")
        imports += `
import ${resClass.slice(0, resClass.lastIndexOf("."))}.${toSnakeCase(objectName)}`;
    } else {
      imports = "";
    }
  }

  const fontVersionField = `${maybePublic(explicitApi)}val version: String = "${(font.version.startsWith("Version ") ? font.version.substring(8) : font.version).replace('"', '\\"')}"`;

  const fontMethod =
    fileStructure !== "multipleFiles"
      ? `

    @Composable
    ${maybePublic(explicitApi)}fun rememberIconFont(${functionParameters(font)}): ${functionType(font)} = ${functionBody(font, toSnakeCase(objectName), "        ")}`
      : "";

  const iconMapField =
    iconMap && fileStructure !== "multipleFiles"
      ? `

    ${maybePublic(explicitApi)}val icons: Map<String, Char> = mapOf(
${icons(glyphs)
  .map(([name, codepoint]) => `        "${name}" to '${codepoint}'`)
  .join(",\n")}
    )`
      : "";

  const iconsFields =
    fileStructure === "singleFile"
      ? icons(glyphs)
          .map(
            ([name, codepoint]) => `

    ${maybePublic(explicitApi)}const val ${toPascalCase(name)}: Char = '${codepoint}'`,
          )
          .join("")
      : "";

  return `@file:Suppress(
    "UnusedReceiverParameter",
    "Unused",
    "ObjectPropertyName",
    "SpellCheckingInspection",
    "ConstPropertyName"
)

package ${packageName}${imports}

${maybePublic(explicitApi)}object ${objectName} {
    ${fontVersionField}${fontMethod}${iconMapField}${iconsFields}
}
`;
}

function functionFileContent(
  font: ExportData["font"],
  objectName: ExportData["objectName"],
  packageName: ExportData["packageName"],
  explicitApi: ExportData["explicitApi"],
  target: ExportData["target"],
  resClass: ExportData["resClass"],
) {
  let imports: string;
  if (isVariable(font)) {
    imports = `
import dev.tclement.fonticons.VariableIconFont
import dev.tclement.fonticons.rememberVariableIconFont
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight`;
  } else {
    imports = `
import dev.tclement.fonticons.StaticIconFont
import dev.tclement.fonticons.rememberStaticIconFont`;
  }
  if (resClass) {
    imports += `
import ${resClass} as Res`;
    if (target === "multiplatform")
      imports += `
import ${resClass.slice(0, resClass.lastIndexOf("."))}.${toSnakeCase(objectName)}`;
  }
  return `@file:Suppress(
    "UnusedReceiverParameter",
    "Unused",
    "SpellCheckingInspection"
)

package ${packageName}

import androidx.compose.runtime.Composable${imports}

@Composable
${maybePublic(explicitApi)}fun ${objectName}.rememberIconFont(${functionParameters(font)}): ${functionType(font)} = ${functionBody(font, toSnakeCase(objectName), "    ")}`;
}

function iconsFileContent(
  glyphs: Glyph[],
  packageName: ExportData["packageName"],
  explicitApi: ExportData["explicitApi"],
  objectName: ExportData["objectName"],
) {
  return `@file:Suppress(
    "UnusedReceiverParameter",
    "Unused",
    "ObjectPropertyName",
    "SpellCheckingInspection"
)

package ${packageName}${glyphs
    .map(
      ({ name, codePoints }) => `

${maybePublic(explicitApi)}val ${objectName}.${toPascalCase(name)}: Char get() = '\\u${codePoints[0].toString(16).padStart(4, "0")}'`,
    )
    .join("")}
`;
}

function iconFileContent(
  glyph: Glyph,
  packageName: ExportData["packageName"],
  explicitApi: ExportData["explicitApi"],
  objectName: ExportData["objectName"],
) {
  return `@file:Suppress(
    "UnusedReceiverParameter",
    "Unused",
    "ObjectPropertyName",
    "SpellCheckingInspection"
)

package ${packageName}

${maybePublic(explicitApi)}val ${objectName}.${toPascalCase(glyph.name)}: Char get() = '\\u${glyph.codePoints[0].toString(16).padStart(4, "0")}'
`;
}

function iconMapFileContent(
  glyphs: Glyph[],
  packageName: ExportData["packageName"],
  explicitApi: ExportData["explicitApi"],
  objectName: ExportData["objectName"],
) {
  return `@file:Suppress(
    "UnusedReceiverParameter",
    "Unused",
    "SpellCheckingInspection"
)

package ${packageName}

${maybePublic(explicitApi)}val ${objectName}.icons: Map<String, Char> get() = mapOf(
${glyphs
  .map(
    ({ name, codePoints }) =>
      `        "${name}" to '\\u${codePoints[0].toString(16).padStart(4, "0")}`,
  )
  .join(",\n")}
    )
`;
}

export default async function ({
  font,
  fontFile,
  disabledGlyphs,
  createSubset,
  objectName,
  packageName,
  kotlinSourceDirName,
  fileStructure,
  target,
  resClass,
  includeFontFile,
  iconMap,
  explicitApi,
}: ExportData) {
  const zip = new JSZip();

  const glyphs = font.characterSet
    .map((codepoint) => font.glyphForCodePoint(codepoint))
    .filter(
      (glyph1, i, arr) =>
        arr.findIndex((glyph2) => glyph2.name === glyph1.name) === i,
    )
    .filter((glyph) => !disabledGlyphs.includes(glyph.name));

  if (includeFontFile) {
    let fontBytes: Uint8Array;
    if (createSubset && !isVariable(font)) {
      const subset = font.createSubset();
      glyphs.forEach(subset.includeGlyph.bind(subset));
      fontBytes = subset.encode();
    } else {
      fontBytes = new Uint8Array(await fontFile.arrayBuffer());
    }

    const resourceDir =
      target === "android" ? "res/font" : "composeResources/font";
    zip.folder(resourceDir)?.file(toSnakeCase(objectName) + ".ttf", fontBytes);
  }

  const sourceDir = kotlinSourceDirName + "/" + packageName.replace(".", "/");

  zip.file(
    sourceDir + "/" + objectName + ".kt",
    mainFileContent(
      font,
      objectName,
      packageName,
      fileStructure,
      explicitApi,
      glyphs,
      target,
      resClass,
      iconMap,
    ),
  );

  if (fileStructure === "twoFiles") {
    zip.file(
      sourceDir + "/" + objectName + "Icons.kt",
      iconsFileContent(glyphs, packageName, explicitApi, objectName),
    );
  }

  if (fileStructure === "multipleFiles") {
    zip.file(
      sourceDir + "/function.kt",
      functionFileContent(
        font,
        objectName,
        packageName,
        explicitApi,
        target,
        resClass,
      ),
    );
    if (iconMap) {
      zip.file(
        sourceDir + "/iconMap.kt",
        iconMapFileContent(glyphs, packageName, explicitApi, objectName),
      );
    }
    glyphs.forEach((glyph) => {
      zip.file(
        sourceDir + "/" + toPascalCase(glyph.name) + "Icon.kt",
        iconFileContent(glyph, packageName, explicitApi, objectName),
      );
    });
  }

  zip
    .generateAsync({ type: "blob", compression: "DEFLATE" })
    .then((content) => {
      saveAs(content, objectName + ".zip");
    });
}
