#!/bin/bash

http -f POST https://kc.test.io/auth/realms/alice/protocol/openid-connect/token client_id=iam-cli username=admin password=password grant_type=password  | jq -r .access_token | pbcopy
