/*
 * Copyright 2025 T. Clément (@tclement0922)
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

export function toPascalCase(str: string): string {
  return str
    .replace(/[^a-zA-Z0-9]+(.)/g, (_, chr) => chr.toUpperCase())
    .replace(/^[a-z]/, (chr) => chr.toUpperCase())
    .replace(/^([^A-Z])/, (chr) => "_" + chr);
}

export function toCamelCase(str: string): string {
  return str
    .replace(/[^a-zA-Z0-9]+(.)/g, (_, chr) => chr.toUpperCase())
    .replace(/^[A-Z]/, (chr) => chr.toLowerCase())
    .replace(/^([^a-z])/, (chr) => "_" + chr);
}

export function toSnakeCase(str: string): string {
  return str
    .replace(/(?<=.)([A-Z0-9]+)/g, (_, chr) => "_" + chr.toLowerCase())
    .replace(/[A-Z]/g, (chr) => chr.toLowerCase())
    .replace(/^[0-9]/, (chr) => "_" + chr)
    .replace(/[^a-z0-9_]/g, "");
}
