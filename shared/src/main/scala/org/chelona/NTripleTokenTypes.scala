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

trait NTripleTokenTypes extends RDFTokenTypes {

  final val BLANK_NODE_LABEL = 1
  final val IRIREF = BLANK_NODE_LABEL << 1
  final val STRING_LITERAL_QUOTE = IRIREF << 1
  final val LANGTAG = STRING_LITERAL_QUOTE << 1
  final val BLANK_LINE = LANGTAG << 1
  final val COMMENT = BLANK_LINE << 1

  type NTripleTokenType = Int

  @inline
  def isBLANK_NODE_LABEL (`type`: NTripleTokenType) = (`type` & BLANK_NODE_LABEL) == BLANK_NODE_LABEL

  @inline
  def isIRIREF (`type`: NTripleTokenType) = (`type` & IRIREF) == IRIREF

  @inline
  def isSTRING_LITERAL_QUOTE (`type`: NTripleTokenType) = (`type` & STRING_LITERAL_QUOTE) == STRING_LITERAL_QUOTE

  @inline
  def isLANGTAG (`type`: NTripleTokenType) = (`type` & LANGTAG) == LANGTAG

  @inline
  def isLITERALTAG (`type`: NTripleTokenType) = (`type` & (STRING_LITERAL_QUOTE | IRIREF)) == (STRING_LITERAL_QUOTE | IRIREF)
}

object NTripleTokenTypes extends NTripleTokenTypes
