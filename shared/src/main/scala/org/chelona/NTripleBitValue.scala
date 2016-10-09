/*
* Copyright (C) 2014-2016 Juergen Pfundt
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.chelona

object NTripleBitValue {
  val BLANK_NODE_LABEL = 1
  val IRIREF = BLANK_NODE_LABEL << 1
  val STRING_LITERAL_QUOTE = IRIREF << 1
  val LANGTAG = STRING_LITERAL_QUOTE << 1
  val BLANK_LINE = LANGTAG << 1
  val COMMENT = BLANK_LINE << 1

  type NTToken = Int

  @inline
  def isBLANK_NODE_LABEL (`type`: NTToken) = (`type` & BLANK_NODE_LABEL) == BLANK_NODE_LABEL

  @inline
  def isIRIREF (`type`: NTToken) = (`type` & IRIREF) == IRIREF

  @inline
  def isSTRING_LITERAL_QUOTE (`type`: NTToken) = (`type` & STRING_LITERAL_QUOTE) == STRING_LITERAL_QUOTE

  @inline
  def isLANGTAG (`type`: NTToken) = (`type` & LANGTAG) == LANGTAG

  @inline
  def isLITERALTAG (`type`: NTToken) = (`type` & (STRING_LITERAL_QUOTE | IRIREF)) == (STRING_LITERAL_QUOTE | IRIREF)
}