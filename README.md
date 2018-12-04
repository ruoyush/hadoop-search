> Ruoyu Li <rul29@pitt.edu>

Mini Project for analyzing Apache access log

- hadoopDocker: docker file for configuring hadoop cluster

# Please compile the java files into jar files first:

- NG.java -> NG.jar
- SearchString.java -> searchstring.jar
- SearchPath.java -> searchpath.jar
- SearchSort.java -> searchsort.jar
- SearchIP.java -> searchip.jar
- SearchSIP.java -> searchsip.jar

# Part 1: N-gram
- Source file: NG.java
- Jar file: NG.jar

Command example: bin/hadoop jar path/to/jar/NG.jar input/ output/ n

* n could be any integer 

# Part 2: 
## 1. search the frequency of a string in a log file
- Source file: SearchString.java
- Jar file: searchstring.jar

Command example: 

```
bin/hadoop jar path/to/jar/searchstring.jar SearchString input/ output/
```

> /assets/img/home-logo.png	98776

## 2. search the frequency of an IP in a log file
- Source file: SearchSIP.java 
- Jar file: searchsip.jar

Command example: 

```
bin/hadoop jar path/to/jar/searchsip.jar input/ output/
```

> 10.153.239.5	547

## 3. search the frequency of the most accessed path in a log file
- Source file: SearchPath.java , SearchSort.java
- Jar file: searchpath.jar, searchsort.jar

Instructions: run searchpath.jar first then run searchsort.jar for ranking

Command example: 

```
bin/hadoop jar path/to/jar/searchpath.jar SearchPath input/ output/
hdfs dfs -rm input/*
hdfs dfs -mv output/* input/
hdfs dfs -rmr output
bin/hadoop jar path/to/jar/searchsort.jar SearchSort input/ output/
hdfs dfs -cat output/*
```

> 117348	/assets/css/combined.css

## 4. search the frequency of an IP in a log file
- Source file: SearchIP.java , SearchSort.java
- Jar file:  searchip.jar, searchsort.jar

Instructions: run searchip.jar first then run searchsort.jar for ranking

Command example: 

```
bin/hadoop jar path/to/jar/searchip.jar input/ output/
hdfs dfs -rm input/*
hdfs dfs -mv output/* input/
hdfs dfs -rmr output/*
bin/hadoop jar path/to/jar/searchsort.jar input/ output/
hdfs dfs -cat output/*
```

> 158614	10.216.113.172


