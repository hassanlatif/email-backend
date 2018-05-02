
## Table of Contents
* [Problem](#problem)
* [Solution ](#solution)
    * [Architecture](#architecture)
    * [Design choices](#design-choices)
    * [Constraints](#constraints)
    * [Tools and libraries](#tools-libraries)
* [Deployment](#deployment)
* [API call format](#api-call-format)
* [Online access](#demo)
* [To-do](#todo)

## Problem
To create a backend service that accepts the necessary information and sends emails. The application should provide an abstraction between two different email service providers. If one goes down, the backend service should failover to a different provider without affecting the customers. The solution should cater for multiple email recipients, CCs and BCCs. The backend should be implemented as one or more RESTful API calls.

## Solution
### Architecture
The following diagram shows the high-level architecture of the solution.

### Design choices
#### Chain of Responsibility pattern for failover
#### Visitor pattern (simplified) for validation
#### Single responsibility principle
### Constraints
### Tools and libraries