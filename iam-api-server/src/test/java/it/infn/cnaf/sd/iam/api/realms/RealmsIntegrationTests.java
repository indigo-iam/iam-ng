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
package it.infn.cnaf.sd.iam.api.realms;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.junit4.SpringRunner;

import it.infn.cnaf.sd.iam.api.utils.IamTest;
import it.infn.cnaf.sd.iam.api.utils.IntegrationTestSupport;

@RunWith(SpringRunner.class)
@IamTest
@WithAnonymousUser
public class RealmsIntegrationTests extends IntegrationTestSupport{

  @Test
  public void testUnauthenticatedGetRealmsWorks() throws Exception {
    
    mvc.perform(get("/Realms")).andExpect(status().isOk());
   
  }
}
