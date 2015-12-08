# Friendship

###Description
Friends' recommendation algorithm using Hadoop MapReduce framework.

This algorithm implements a simple “People You Might Know” social network friendship recommendation algorithm.<br />
The key idea is that if two people have a lot of mutual friends, then the system should recommend that they connect with each other.<br />
For each user, the algorithm will recommend at maximum of <b>10</b> users who are not already friends with that user, but have the most number of mutual friends in common with that user.

The implementation is inspired by [tonellotto](https://github.com/tonellotto); you can find [here](https://github.com/tonellotto/Distributed-Enabling-Platforms/tree/master/exercises/10.%20friendship) his solution.

###Compile
To compile the program, you'll need to use the following command lines:

```bash
cd Friendship
mvn package
```

###Usage
To run the program, you'll need to use the following command lines:

```bash
$HADOOP_HOME/bin/hadoop put $DATASET_PATH/graph_input.txt graph_input.txt
$HADOOP_HOME/bin/hadoop jar target/friendship-1.0-SNAPSHOT.jar pad.FriendDriver graph_input.txt output
```

### Input
You can download an example of social graph from this [link](./social_graph.txt).

The input file contains the adjacency list, it has multiple lines in the following format:

    <UserID><TAB><Friends>

- `<UserID>`: is a unique integer ID corresponding to an unique user.
- `<Friends>`: is a comma separated list of increasing unique IDs corresponding to the friends of the user.

### Output
The output will contains one line per user, in the following format:

    <User><TAB><Recommendations>

- `<User>` is a unique ID corresponding to an unique user.
- `<Recommendations>` is a comma separated list of unique IDs corresponding to the algorithm’s recommendation of people that `<User>` might know, ordered in decreasing number of mutual friends.

###License
The MIT License (MIT)

Copyright (c) 2015 Federico Conte

http://opensource.org/licenses/MIT
