# JonAnd's TaskManager v1.0

[![Build Status](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-JonPus.svg?token=qukmAfVpPKGSjbT1tbP7&branch=master)](https://travis-ci.com/Westerdals/pgr203-2019-eksamen-JonPus)

A simple program for creating projects and adding users.

## Usage

1. Create a file in root called `ProjectManager.properties` with the content:

```properties
dataSource.url=...
dataSource.username=...
dataSource.password=...
```

2. Build with `mvn package`
3. Start server `java -jar target\ProjectManager-1.0-SNAPSHOT-shaded.jar`
4. The app runs at **_http://localhost:8080/_**

### Functionality

1. Create projects
2. Create members
3. Show members associated to a project
4. Show projects a member is a part of
5. Add and remove members in a project
6. Edit and delete members and projects

## Design description

![Design](https://i.imgur.com/HJjXxQt.png)
![Design](https://i.imgur.com/O1ES4LZ.png)

By Jonathan Pusparajah & Andreas Walter Wilhelmsen
