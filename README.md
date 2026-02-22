# Jiring News TCP Analyzer (Java SE 8)

## Task Description:
The task involves the development of two separate Java SE 8 programs: a "mock news feed" and a
"news analyzer ". Several instances of the mock news feed will be run simultaneously, each
connecting to the same news analyzer.

The mock news feed should periodically generate messages containing random news item.
The news analyzer should handle the messages from the news feeds and periodically display a
short summary about news items that are considered "interesting".


## Mock News Feed:
Creates a persistent TCP connection to the news analyzer and periodically sends news items over
that connection. Each news item should be comprised of a headline and a priority.
The headline of a news item should be a random combination of three to five words from the
following list: up, down, rise, fall, good, bad, success, failure, high, low.
The priority of a news item should be an integer within the range [0-9]. News messages with
higher priority should be generated with less probability than those with lower priority.
The frequency of news items being emitted by the feed should be configurable via a Java property.


## News Analyzer:
Listens on a TCP port for connections from mock news feeds and receives news item messages.
Inspects the news item headlines and decides whether they are overall positive or negative. If
more than 50% of words in the headline are positive ("up", "rise", "good", "success", "high"), the
news item as a whole is considered positive. Negative news items are ignored by the analyzer.
Every 10 seconds, the news analyzer should output to the console:
the count of positive news items seen during the last 10 seconds
the unique headlines of up to three of the highest-priority positive news items seen during the last
10 seconds.

**This repository contains two Java SE 8 programs communicating over a persistent TCP connection:**

- **Mock News Feed (feed)**: periodically generates random news items and sends them to the analyzer.
- **News Analyzer (analyzer)**: accepts multiple feed connections, filters *positive* news, and prints a periodic summary.

A shared **common** module contains DTOs and message codec utilities used by both programs.


## Building

```bash
mvn -q -DskipTests clean package
```

### Start Analyzer

```bash
java -jar analyzer/target/analyzer-1.0-SNAPSHOT.jar
```

**Example:**

```bash
java -Dnews.analyzer.port=9999 \
     -Dnews.analyzer.summary.interval.ms=10000 \
     -jar analyzer/target/analyzer-1.0-SNAPSHOT.jar
```

### Start Analyzer

```bash
java -jar feed/target/feed-1.0-SNAPSHOT.jar
```

**Example:**

```bash
java -Dnews.analyzer.port=9999 \
     -Dnews.analyzer.summary.interval.ms=10000 \
     -jar analyzer/target/analyzer-1.0-SNAPSHOT.jar
```