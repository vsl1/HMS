# HMS - Hospital Management System
## Summary
HMS (Hospital Management System) is to help hospitals and health cares to manage their daily activities, such as management for patient, doctors, finance, medicines.
The system is based on Spring Boot and Spring Cloud. 

## Architecture
1. huip-eureka using Spring Cloud Netflix for microservices registration and discovery
2. huip-reservation Implement CRUD functions for the system
3. huip-router Implement ccorss-origin resource sharing
4. huip-user using Spring Security and OAuth 2.0 for SSO, and manage origanaztions in the system
5. huip-patient Implement communication through services by using RabbitMQ

## Skill Sets
| Skill |  Version |
---|:--:|
Spring Cloud Netflix | Finchley.RELEASE	
Spring Cloud Eureka  | 2.0.0.RELEASE	
Spring Cloud Zuul	   | 2.0.0.RELEASE	
Spring Cloud Hystrix | 2.0.0.RELEASE	
Spring Boot Admin	   |2.0.1	
Spring Boot	         |2.0.3.RELEASE	
Spring Security      |5.1.4.RELEASE	
RabbitMq |3.7.14
