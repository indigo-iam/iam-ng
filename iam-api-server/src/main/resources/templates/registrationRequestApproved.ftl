Dear ${requester.name},

your registration request has been approved.

Your account username is:

${requester.username}

You will soon receive an email to reset your ${organisationName} account password, if 
local credentials are enabled for such organization.

Note that if you used an external identity provider during registration, 
you can already access your ${organisationName} account.

<#if message??>
Message from ${organisationName} administrators:

${message}

</#if>

The ${organisationName} registration service