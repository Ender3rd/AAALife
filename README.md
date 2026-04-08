# Compile and Run Instructions
TODO


 ## Implementation Notes
 - Account, Policy, and User are mostly stub objects.
 - ClaimChange could be altered to be the entire auditing system, but that would complicate scaling. Better to keep it to major changes and handle detailed audits more properly.
 - Updates to ClaimStatus must include the id of the previous ClaimStatus in the request so that the DB transaction can be failed if a race condition occurs. If needed, each session can have a unique request ID (probably cookie) to handle race conditions and mitigate replay/DDoS attacks in a general way.
 - The headers etag, stale-while-validate, and stale-while-error should be set, where appropriate, on all responses to GET requests (and elsewhere).
 - Any API which does not specify a Role(s) which are acceptable MUST NOT run its business logic and SHOULD fail before any integrations in the CoR are triggered.

## What is included
- Auditing via log4j, other CoR integrations.
- Claim creation and status updating.
- Document upload and storage.
- Note creation.
- Notes and documents provided on nearly all objects (Account, Policy, User, Claim, Document, Note).
- Integration points for auditing, document upload/retrieval, user authentication and authorization, ticketing systems.

## What is omitted
- Account and Policy are stub objects, for other non-claim stories to flesh out.
- User CRUD (including role grants) are not given APIs. Proper implementation is not feasible in this timeframe and so manual db entry is more secure until integration with OAuth, Active Directory or a secure local authentication and authorization mechanism can be implemented.
- UI is unimplemented
- Terraform and other deployment automation.
- A workflow system. Integration with Jira or similar will be more effective.
- Data Cleanup.
- Customer access within their account is unlimited. I.e., no restricted/hidden policies.
- Markdown, asciidoc, etc. for Notes. Should be 99% future proof.