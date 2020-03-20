#!/bin/bash
#
# Copyright (c) Istituto Nazionale di Fisica Nucleare (INFN). 2016-2020
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

set -e

if [[ -z ${IAM_API_JAR} ]]; then
  for f in ../target/iam-api-*.jar; do
    IAM_API_JAR=${f}
    break
  done
fi

if [[ ! -r ${IAM_API_JAR} ]]; then
  echo "Please set the IAM_API_JAR env variable so that it points to the IAM API jar location"
  exit 1
fi

echo "Building image using jar from ${IAM_API_JAR}"

cp ${IAM_API_JAR} iam-api.jar
build-docker-image.sh
rm iam-api.jar
