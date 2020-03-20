/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.infn.cnaf.sd.iam.api.apis.root;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import it.infn.cnaf.sd.iam.api.apis.root.dto.RootResponseDTO;

@RestController
public class RootController {
  public static final String API_DOCS_PROPERTY = "${springdoc.swagger-ui.path}";

  final String apiDocsPath;

  public RootController(@Value(API_DOCS_PROPERTY) String apiDocsPath) {
    this.apiDocsPath = apiDocsPath;
  }

  @GetMapping({"", "/"})
  public RootResponseDTO getRoot() {
    return RootResponseDTO.builder().apiDocumentationPath(apiDocsPath).build();
  }

}
