#!/bin/bash

http -f POST https://kc.test.example/auth/realms/alice/protocol/openid-connect/token client_id=admin-cli username=admin password=password grant_type=password  | jq -r .access_token | pbcopy
