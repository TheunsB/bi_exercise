# bi_exercise

This is a simple exercise that enables data analytics to be done on a twitter stream. Spark is used for streaming.

Add your own twitter api key/secret in twitter4j.properties.

## Set up spark
Create and configuration streaming context using spark

`val sparkConfiguration = new SparkConf().setAppName("twitter-stream").setMaster("local[*]")`
`val sparkContext = new SparkContext(sparkConfiguration)`
`val streamingContext = new StreamingContext(sparkContext, Seconds(5))`

## Filtering
Key words can be added to the filters.txt file under resources. Only tweets containing these key words will be displayed.
Further manipulation/formatting can be done during filtering.

## Analytics
//todo
Analytics can be performed on the results.

###Disclaimer

This was a simple exercise to stream twitter data and perform some basic analytics. Currently no analytics is being done,
it simply serves as an example on how to stream data using spark.
