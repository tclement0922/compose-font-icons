/*
 * Copyright 2024 T. Clément (@tclement0922)
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

import { ShikiTransformer } from "shiki";

export default {
  name: "dokkaSymbolTransformer",
  preprocess(code, options) {
    if (options.meta?.__raw?.startsWith("symbol")) {
      options.mergeWhitespaces = false;
      const matches = options.meta.__raw.matchAll(
        /\s"([0-9]+),([0-9]+)"="(\S+)"/g,
      );
      for (const match of matches) {
        const start = Number(match[1]);
        const length = Number(match[2]);
        let link = match[3].replace('\\"', '"');
        if (!link.startsWith("http")) {
          link = "/api/" + link;
          if (link.endsWith(".md")) {
            link = link.substring(0, link.length - 3);
          }
          if (link.endsWith("/")) {
            link = link.substring(0, link.length - 1);
          }
        }
        options.decorations ||= [];
        options.decorations.push({
          start,
          end: start + length,
          tagName: "a",
          properties: {
            href: link,
          },
        });
      }
    }
    return code;
  },
  pre(hast) {
    if (this.options.meta?.__raw?.startsWith("symbol")) {
      hast.properties.class ||= [];
      (hast.properties.class as string[]).push("wrap");
    }
    return hast;
  },
} as ShikiTransformer;
