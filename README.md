### Introduction

During the live coding task, we want to check the basic knowledge and abilities related to **Java**, **Spring**, **Maven**, and **Git**,
design and programming principles and good practices.

Imagine that you have been assigned to a project that has been started by some junior member,
it has some bugs and suboptimal implementation.

Your task is to fix the bugs and make it production ready (**include all the facilities that are essential for production
maintenance and use, please remember about the test coverage as well**).

You are free to use external resources (if you don't remember some API you are free to check up on it) and include dependencies. 

The project has H2 in memory database set up with test data for your convenience.

### Task overview:

Please clone a following Spring Boot maven project:
https://github.com/kalatus/socialnetwork.git

Its business domain is simple it manages the following object (object defined in pseudo-code):

```
SocialNetworkPost {
    Date postDate // we are interested in date and time of the post
    String postCategory // is a closed set can be Music/Gaming/News/Entertainment
    String author
    String content
    Number viewCount
}
```

**The project should support two business cases**

1. Searching posts by author
2. Retrieving top 10 posts for each existing category in one response


### Further considerations:

For this business case the number of posts can be really huge (like 2^64), the same applies to their view count
* Please do not expose DB metadata via the API (like DB ID)
* Please use tests to expose potential bugs
* Please consider restfulness and API conventions
* Please consider scalability and performance of the API
* Please consider potential maintenance of this project and clean code principles
* Please think on what would be important for you to debug potential production issues
* If something requires explanation, or you simply have some questions, please just ask.

