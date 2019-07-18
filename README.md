[<img src="https://sling.apache.org/res/logos/sling.png"/>](https://sling.apache.org)

 [![Build Status](https://builds.apache.org/buildStatus/icon?job=Sling/sling-org-apache-sling-jobs/master)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-jobs/job/master) [![Test Status](https://img.shields.io/jenkins/t/https/builds.apache.org/job/Sling/job/sling-org-apache-sling-jobs/job/master.svg)](https://builds.apache.org/job/Sling/job/sling-org-apache-sling-jobs/job/master/test_results_analyzer/) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.apache.sling/org.apache.sling.jobs/badge.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.apache.sling%22%20a%3A%22org.apache.sling.jobs%22) [![JavaDocs](https://www.javadoc.io/badge/org.apache.sling/org.apache.sling.jobs.svg)](https://www.javadoc.io/doc/org.apache.sling/org.apache.sling.jobs) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

# Apache Sling Jobs Support

This module is part of the [Apache Sling](https://sling.apache.org) project.

Sling Jobs, not to be confused with Sling Event bundle.

This Bundle provides Job processing using a message queue. It aims to do that using off the shelf queues. It does not 
provide an API that allows interaction with the queue beyond that is supported by the ISO AMQP standard or the JMS API. Although the 
classes here may have similar names to the API in org.apache.sling.event.jobs, they are not the same. Methods present
in org.apache.sling.egent.jobs API that are not compatible with a distributed message queue concept are not included, and the 
API is designed with message passing in mind.


# How it works.

MoM Queues transport messages that control the state of a Job taking it through a lifecycle from creation to distruction. Not all messages
are required to live that lifecycle, only the create message is required under normal circumstances.

A MoM QueueReader implemented by the JobQueueConsumerFactory consumes messages from MoM Queues. This is a OSGi Configuration Factory
allowing many JobQueueConsumers to dequeue messages from a MoM Queue. When a message is dequeued and identified correctly as a 
Job related message, it is forward to the JobSubsystem component.
 
The JobSubsystem component allows JobConsumers to register with it. It interprets the messages, creating Jobs and managing their lifecycle.
It will identify JobConsumers that can consume the job and invoke the execute method on the JobConsumer.

JobConsumers are where the job itself is implemented. Components that implement this API are registered as services which get 
registered by OSGi DS with the JobSubsystem. Each JobConsumer declares a list of JobTypes it will consume. There is a special 
JobType of ANY_TYPE for consumers that want to consume any Job. To use the JobSubsystem you will need to implement your own 
JobConsumers. There are examples of JobConsumers in this source code area.
 
 
