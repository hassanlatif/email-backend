

## Table of Contents
* [Problem](#problem)
* [Solution ](#solution)
    * [Architecture](#architecture)
    * [Design choices/patterns](#design-choices-and-patterns)
    * [Constraints](#constraints)
    * [Tools and libraries](#tools-and-libraries)
* [Deployment instructions](#deployment-instructions)
    * [Deploying to local machine](#deploying-to-local-machine)
    * [Deploying to AWS](#deploying-to-aws)
* [Calling the API](#calling-the-api)
    * [Request format](#request-format)
    * [Response format](#response-format)
* [Online test access](#demo)
* [To-do](#to-do)

## Problem
To create a RESTful backend service that accepts the necessary information and sends emails. The application should provide an abstraction between two different email service providers. If one goes down, the backend service should failover to a different provider without affecting the customers. The solution should cater for multiple email recipients, CCs and BCCs. 

## Solution
### Architecture
The following diagram illustrates the high-level architecture of the solution.
![Email-backend service](https://raw.githubusercontent.com/hassanlatif/email-backend/master/images/Architecture.png)

### Design choices and patterns
#### Chain-of-responsibility pattern for provider failover 
The failover mechanism is implemented using the Chain-of-responsibility design pattern. The pattern is used to create a chain of responsibility between the service providers, so that if one provider fails the responsibility is shifted to the other provider. Therefore, instead of using if-then-else statement the providers arrange themselves in a chain to handle failover. Moreover, the pattern promotes loose coupling and provides a graceful mechanism to add more providers in the future. The implemented pattern is illustrated in the UML diagram below.

![Chain-of-responsibility pattern](https://raw.githubusercontent.com/hassanlatif/email-backend/master/images/UML-CoR.png)
#### Visitor pattern (simplified) for parameters validation
A stripped-down version of the Visitor design pattern is used for validating the email parameters, where the email is the visitor and the validation rules are the visitees. This greatly improves the readability and testability of the code as instead of if-then-else statement each validation rule encapsulates the single responsibility principal. The following code snippet from the application illustrates this:
```java
public static void validateEmail(Email email) throws InvalidParameterException {

    List<ValidationRule> rules = new ArrayList<>();

    rules.add(new PrimaryRecipientsRule());
    rules.add(new SenderValidationRule());
    rules.add(new RecipientsValidationRule());
    rules.add(new SubjectValidationRule());
    rules.add(new ContentValidationRule());

    for (ValidationRule rule : rules) {
        rule.validate(email);
    }
}
```
The Visitor pattern also makes the code more manageable as more validators can be easily added in the future. The following UML diagram illustrates the implemented pattern.

![Visitor pattern](https://raw.githubusercontent.com/hassanlatif/email-backend/master/images/UML-Visitor.png)
#### Custom Exceptions
To maintain a clear separation and have traceability of exceptions, two custom exception have been added namely InvalidParameterException and ServiceProviderException for client side exceptions and server side exceptions respectively.
### Constraints
#### Quality of Service (QoS)
Since the application is inherently a delegate/proxy service there aren't any QoS levels in place to provide guarantee for the delivery of the email. The application only delegates the requests and responses to the concerned parties.
#### Control of Service
The application is dependent on external service providers (which are mail servers) for the processing, queuing and delivery of the emails, and hence has effectively little to no control over the processing and delivery pipeline. 
#### Data Loss
Due to the above mentioned lack of control and QoS there are multiple points of possible data loss. The application relies on the "reputation" of the external mail servers which maybe be blocked by organizations of interest to the user of the application due to strict filtering policies. Moreover, the application doesn't provide a method to retain the email content once it is sent, and there is no mechanism to receive confirmation about the delivery of the email or to identify failure of delivery if the address was incorrect.
### Tools and libraries
The application is developed with the Java programming language. The two external libraries used are both open source, namely [JSON in Java](https://github.com/stleary/JSON-java) for JSON processing and [Jersey Web service framework](https://jersey.github.io/) for creating the RESTful API service. Maven is the choice of build and dependency management tool.
## Deployment instructions
### Deploying to local machine
The [AWS SAM Local](https://github.com/awslabs/aws-sam-local)  can be used to run the service on a local machine. For SAM Local to work,   [Docker](https://www.docker.com/get-docker)  (community or enterprise) needs to be installed and running on the local machine. Below steps cane be followed to run the service locally:

1. Download and install [Docker](https://www.docker.com/get-docker)
2. Install SAM Local:
```bash
$ npm install -g aws-sam-local
```
3. Clone or download the code.
4. Using an editor, open the file /email-backend/src/main/resources/emailservices.xml and add your API Keys and URLs for SendGrid and MailGun API services
5. Using a terminal, open the project root folder and build the jar file.
```bash
$ cd myworkspace/email-backend
$ mvn clean package
```
6. Still in the project root folder – where the  `sam.yaml`  file is located – start the API with the SAM Local CLI.
```bash
$ sam local start-api --template sam.yaml
...
Mounting com.sapessi.jersey.StreamLambdaHandler::handleRequest (java8) at http://127.0.0.1:3000/{proxy+} [OPTIONS GET HEAD POST PUT DELETE PATCH]
...
```
We now have a local emulator of API Gateway and Lambda up and running. Using an API development environment such as [Postman](https://www.getpostman.com/), you can send a test request to the following URL.
```bash
http://127.0.0.1:3000/email/send
```

The [API request format](#request-format) is given below.


### Deploying to AWS
The [AWS CLI](https://aws.amazon.com/cli/) can be used to deploy the application to AWS Lambda and Amazon API Gateway. Below are the steps to deploy the application on AWS.

1. Clone or download the code.
2. Using an editor, open the file /email-backend/src/main/resources/emailservices.xml and add your API Keys and URLs for SendGrid and MailGun API services
3. Using a terminal, open the project root folder and build the jar file.
```bash
$ cd myworkspace/email-backend
$ mvn clean package
```
4. You will need to create an S3 bucket to store the artifacts for deployment. Once you have created the S3 bucket, run the following command from the project’s root folder – where the  `sam.yaml`file is located:
```bash
$ aws cloudformation package --template-file sam.yaml --output-template-file output-sam.yaml --s3-bucket <YOUR S3 BUCKET NAME>
Uploading to xxxxxxxxxxxxxxxxxxxxxxxxxx  6464692 / 6464692.0  (100.00%)
Successfully packaged artifacts and wrote output template to file output-sam.yaml.
```
5. Execute the following command to deploy the packaged template
```bash
aws cloudformation deploy --template-file /your/path/output-sam.yaml --stack-name <YOUR STACK NAME>
```

6. Choose a stack name and run the  `aws cloudformation deploy`  command from the output of the package command.

```bash
$ aws cloudformation deploy --template-file output-sam.yaml --stack-name EmailBackendAPI --capabilities CAPABILITY_IAM
```

Once the application is deployed, you can describe the stack to show the API endpoint that was created. The endpoint should be the  `EmailBackendApi` key  of the  `Outputs`property:

```bash
$ aws cloudformation describe-stacks --stack-name ServerlessJerseyApi --query 'Stacks[0].Outputs[*].{Service:OutputKey,Endpoint:OutputValue}'
[
    {
        "Service": "EmailBackendApi",
        "Endpoint": "https://xxxxxxx.execute-api.us-west-2.amazonaws.com/Prod/email/send"
    }
]
```

Copy the  `Endpoint`  into an API development environment such as [Postman](https://www.getpostman.com/) to test your first request. The [API request format](#request-format) is given below.

## Calling the API
### Request format 
The API has only one RESTful endpoint for sending emails (/email/send). The endpoint accepts only POST method with content-type as JSON (application/json) so please be sure to set the header before making the calls. The request has the following fields:


| Field name    | Type    | Sub-fields | Optional|
| ------------  |:-------:|:----------:|:-------:|
| sender        | Object  | name, email|    N    |
| toRecipients  | Array   | name, email|    N    |
| ccRecipients  | Array   | name, email|    Y    |
| bccRecipients | Array   | name, email|    Y    |
| subject       | Value   | None       |    N    |
| content       | Value   | None       |    N    |

A sample JSON request is as follows:

```json
{
    "sender" : 
    {
        "name" : "Mr. John",
        "email": "john@mail.com"
    },
    "toRecipients" : 
    [
        {
            "name" : "Sarah",
            "email" : "sarah@mail.com"
        },
        {
            "name" : "Zara",
            "email" : "zara@mail.com"
        }       
    ],
    "ccRecipients" : 
    [
        {
            "name" : "Bob",
            "email" : "bob@mail.com"
        }
    ],
    "bccRecipients" : [
        {
            "name" : "Alice",
            "email" : "alice@mail.com"
         }
    ],
    "subject" : "Test Subject",
    "content" :"Hello World"
}
```
### Response format
The returned response is a JSON content-type with the following three fields:

| | | |
|-|-|-|
| code | Status code returned from the server  |
| message | Status message returned from the server  | 
| details | more details and possibly actions required by the user to correct the error 

Theres status codes are currently limited to the following three types:

| | | |
|-|-|-|
| 200 | Success  |
| 400 | Client side error   | 
| 500 | Server side error |

A sample response is as follows:

```json
{
    "code": "200",
    "message": "OK",
    "details": "Email send request was successful.",
}
```
## To-do

 - Adding a logging utility such as log4j - Currently using standard output and error which is supported by AWS CloudWatch but has formatting issues.
 - Adding a RESTful testing framework such as Jersey testing framework for RESTful endpoint testing
