# Design Exercise

## Requirements Design
- Customer-submitted claims with supporting documents.
- Adjuster workflows to review, approve, deny, comment.
- Simple status and comms logs.
- Fraud detection (basic).
    - Dupe claims
    - Excessive Volume
- Secure file handling
- Audit trails
- Fraud pattern logging
- Role-based access
- Traceability
- Integration ready with underwriting and payment systems

## Architectural Components
- Claims microservice 
    - Springboot
    - docker
    - running on k8s (or corporate standard).
    - REST API implementations (Create and Retrieve mostly).
- Structured Database 
    - SQL, any flavour.
    - Non-document data storage.
- Event Stream 
    - Kafka, SNS, etc.
    - Push notifications to integrations, auditing, etc.
- Auditing microservice
    - Optional
    - Recieves push notifications and logs them.
    - Retrieves the log4j audit log if necessary.
    - More complicated fraud detection algorithms.
- Batch Actions
    - AWS Batch, k8s job, etc.
    - Bulk import from integrations that don't support webhooks.
        - Policy creation from underwriting.
        - Policy cancellation from payments.
    - Periodic cleanups
- Durable Storage
    - ONTAP, Symmetrix, FSx, etc.
    - To store the database.
    - If object storage is not available, to store Documents.
- Object Storage
    - ONTAP S3, S3, etc.
    - Document storage, lifecycle, security
- WORM Storage
    - ONTAP, S3, etc.
    - I'm not sure how an object store would handle the many tiny writes per second. Investigation needed, sales engineering demo?
    - Auditing destination with write-once guarantees from a third party.
    - Compliance audits will be easy.
    - If not available, other storage could be used after confirming compliance.
- Web Application Firewall (WAF)
    - AWS WAF, Cloudflare, etc.
    - DoS mitigation
    - Security monitoring and mitigation
    - SSL offloading
    - Compression Offloading
- Firewall
    - it's a firewall
- HTTP Caching Proxy
    - Squid, etc.
    - Offloading caching from the server
    - Including microservice to microservice calls

## Database
### Domain Objects (Also DB Tables)
All have a creation time, ID.
 - Account
 - Policy
    - Public ID
 - User
 - Role (DBEnum? Configuration? Code Enum?)
    - Customer
    - Adjuster
    - Payments
    - Underwriter
 - Claim
    - Due (Timestamp)
    - Status (from latest ClaimChange's ClaimStatus)
 - Document
    - File Location
    - Size
    - Hash
    - Parent (with type and identifier)
    - RelatesTo (with type and identifier)(Same as Parent if Parent is not a Note or Document)
 - Note
    - Same as Document but no file location, Size or Hash.
    - Content
 - ClaimChange
    - ClaimStatus
    - Created Timestamp
    - User
    - Claim
 - ClaimSatus (DBEnum? Configuration? Code Enum?)
    - Created
    - Reviewable
    - Denied
    - Approved

 ### Object Relationships
 - 1 Account : Many Policies
 - 1 Account : Many User
 - 1 User : 0-Many Accounts (Note: 0 is implicitly all. Multiple is an Adjuster limited to certain accounts)
 - Many User : Many Role (Note: Maybe Many:1)
 - 1 Policy : Many Claim
 - 1 User : Many ClaimChange
 - 1 Claim : Many ClaimChange
 - 1 Note or Document : 1 ClaimChange, Note, Document, User, Policy, Claim, or Account (parent)
 - 1 Note or Document : 1 ClaimChange, User, Policy, Claim, or Account (If parent is a Note or Document)

 ### Relationship Tables (in the DB)
 - UserAccountAccess

 ### Notable DB Indicies
 - Document and Note: (RelatesToIdentifier, RelatesToType) - for quick lookups
 - ClaimStatus (claim, created) - for current status

 ## Auditing and Integrations
API call will trigger configurable middleware in two rounds. The first round will use interceptors to implement a Chain of Responsibility (CoR) preceeding each request. This will mostly be for security, fraud, auditing, authentication, and authorization checks. The second round will be after the DB transaction is confirmed. That round will use an interceptor or similar to trigger an eventing system such as kafka to integrate with payments, ticketing systems, fraud detection, etc.

In the chain of responsibility middleware is primarily aimed at security applications such as DoS mitigation. Each link will be isolated from the others for exception handling, but will be able to terminate the chain. The first link triggered will always be auditing. The next links security, then integrations. The chain will be as early as feasible in HTTP processing to better scale when shedding load. 

If further reliability is needed the DB tables can have an extra column for messageSent, which is updated on successful completion of the second round. A batch job can be used to re-trigger missed rounds. The successful completion of the first round is implicit in the existence of the DB row.
Additional safeguards against excess load can be triggered with speciality cases in the Chain of Responsibility for things like resource exhaustion. Second-order Chains of Responsibility (or Strategy wrappers) around integration links can handle timeouts, exponential backoff, etc. to protect the system against failing external integrations.

The earliest link in the request CoR will be a log4j message which can be directed via configuration into a separate audit log on top of the routine debugging-oriented logs. Other interceptors can be used to integrate with specific auditing systems. This audit log can be configured to work with append-only filesystems, off-box filesystems (FSx), or other secure mechanisms.
User authentication and authorization can be changed by altering the CoR configuration to use Active Directory, OAuth, etc. in whichever combination is desired.

 ## Notes
 - Account, Policy, and User are mostly stub objects.
 - Notes on notes allows threaded communication.
 - Notes on User/Policy/Account is a very basic CRM. Proper integration can be achieved via import/export or by delegating the Note CRUD calls via CoR.
 - ClaimChange could be altered to be the entire auditing system, but that would complicate scaling. Better to keep it to major changes and handle detailed audits more properly.
 - Updates to ClaimStatus must include the id of the previous ClaimStatus in the request so that the DB transaction can be failed if a race condition occurs. If needed, each session can have a unique request ID (probably cookie) to handle race conditions and mitigate replay/DDoS attacks in a general way.
 - The full range of CRUD is not available to any object in this microservice. Row deletion is handled in dedicated cleanup functions only. Updates are limited to adapting to information from integrations with payments, underwriting, etc.
 - The headers etag, stale-while-validate, and stale-while-error should be set, where appropriate, on all responses to GET requests (and elsewhere).
 - Any API which does not specify a Role(s) which are acceptable MUST NOT run its business logic and SHOULD fail before any integrations in the CoR are triggered.
 - These choices represent an architecture for a large-scale actively managed web application. One for which the heavyweight nature of the many subsystems is worthwhile because the capabilities of each are utilized effectively. If a more startup/quick to create design is needed, only the application code (microservice), structured database, and durable storage are essential. The other services could be partially implemented with an extension of the Chain of Responsibility configuration, temporarily moved out of scope, or worked around.

## What is included
- Auditing via log4j, other CoR integrations, event monitoring integrations.
- Claim creation and status updating.
- Document upload and storage.
- Note creation.
- Notes and documents provided on nearly all objects (Account, Policy, User, Claim, Document, Note).
- Integration points for auditing, document upload/retrieval, user authentication and authorization, ticketing systems.

## What is omitted
- Account and Policy are stub objects, for other non-claim stories to flesh out.
- UI Design
- A structured workflow system with specific transitions, etc. Integration with Jira, a CRM, or similar will be more effective. A basic system with an AllowedTransitions table could be implemented.
- Customer access within their account is unlimited. I.e., no restricted/hidden policies.