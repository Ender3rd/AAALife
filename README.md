# Compile and Run Instructions
## Local Run
`mvn spring-boot:run`

## Docker
1. It is assumed that java, maven, and docker are installed, working, and in the PATH.
2. Download/clone source files.
3. Run `mvn clean install` to compile everything. There's no where to deploy to for this demo, so that goal does not work.
4. Run `docker image pull registry.hub.docker.com/library/eclipse-temurin:26_35-jre-alpine` to retrieve the base image for the dockerfile
5. From this file's directory (i.e., the project's base directory), run `docker build . --tag=localhost/claims:latest` to create the docker image.
6. Run `docker run localhost/claims` to start the image as a new container.

# Notes
- getAll is generally not available. Clients are expected to navigate through the object tree or to use dedicated search methods. Security and scalability improvement.
- Dedicated search methods are provided to navigate more quickly for what we anticipate to be common use cases. Analytics can be used to update our expectations over time.
- Most dedicated methods will have a start date. Later an end date. Again, to improve scale.
- Dedicated search methods have relevant indices created for them.
 
# What is included
- Auditing via log4j, other CoR integrations.
- Almost all entries are immutable for auditing.
- Claim creation and status updating.
- Document upload and storage.
- Note creation.
- Notes and documents provided on nearly all objects (Account, Policy, User, Claim, Document, Note).
- Integration points for auditing, document upload/retrieval, user authentication and authorization, ticketing systems.

# What is omitted
- Account and Policy are stub objects, for other non-claim stories to flesh out.
- ClaimChange could be altered to be the entire auditing system, but that would complicate scaling. Better to keep it to major changes and handle detailed audits more properly.
- Updates to ClaimStatus must include the id of the previous ClaimStatus in the request so that the DB transaction can be failed if a race condition occurs. If needed, each session can have a unique request ID (probably cookie) to handle race conditions and mitigate replay/DDoS attacks in a general way.
- The headers etag, stale-while-validate, and stale-while-error should be set, where appropriate, on all responses to GET requests (and elsewhere).
- Any API which does not specify a Role(s) which are acceptable MUST NOT run its business logic and SHOULD fail before any integrations in the CoR are triggered.
- Data structures for authentication and authorization are present but not enforced. All passwords are blank and cannot be stored. To encourage proper productionization.
- User CRUD (including role grants) are not given APIs. Proper implementation is not feasible in this timeframe and so manual db entry is more secure until integration with OAuth, Active Directory or a secure local authentication and authorization mechanism can be implemented.
- UI is unimplemented
- Terraform and other deployment automation.
- A workflow system. Integration with Jira or similar will be more effective.
- Data Cleanup.
- Customer access within their account is unlimited. I.e., no restricted/hidden policies.
- Markdown, asciidoc, etc. for Notes. Should be 99% future proof.
- Limiting visibility of objects by user access.