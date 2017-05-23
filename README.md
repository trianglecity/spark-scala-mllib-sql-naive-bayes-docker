
## 
## Spark Scala MLlib + SQL Naive Bayes on Docker.
##

The source code is based on 

	[1] http://avulanov.blogspot.com/2014/08/text-classification-with-apache-spark.html

	[2] the sentence polarity dataset v 1.0 is from http://www.cs.cornell.edu/people/pabo/movie-review-data/ 

NOTICE 1: the accuracy is 76%.

	Threshold: 1.0, F-score: 0.7668611435239205, Beta = 1

	Threshold: 0.0, F-score: 0.6687316091063962, Beta = 1

	accuracy = 0.7675663099115868


NOTICE 2: the original polarity dataset may be improved for better performance.


NOTICE 3: The spark version 2.1.2 is changed to 2.1.2-SNAPSHOT for the sbt dependencies.

##

[1] git clone this-source-code-folder

[2] cd downloaded-source-code-folder

[3] sudo docker build -t spark-scala-naivebayes:01 .
	
	wait ... wait ... wait ... wait ...

[4] sudo docker run --rm --privileged  -i --name scalanaivebayes -v $PWD:/home/spark_MLlib  -t "spark-scala-naivebayes:01"  bash

	a bash sell will be ready (root@548d1ec6255b:/#).


[5] root@548d1ec6255b:/# cd /home/spark_MLlib/

[6] root@548d1ec6255b:/home/spark_MLlib#  cd ./classification/naivebayes/

[7] root@548d1ec6255b:/home/spark_MLlib/classification/naivebayes# sbt clean compile

[8] root@548d1ec6255b:/home/spark_MLlib/classification/naivebayes# sbt clean package

[9] root@05adae1ace85:/home/spark_MLlib/classification/naivebayes/target/scala-2.11# /spark/bin/spark-submit  --class "NaiveBayesClassification" ./target/scala-2.11/naivebayes_2.11-1.0.jar


[10] the output may look something like this


	+-----+--------------------+
	|label|            sentence|
	+-----+--------------------+
	|    1|the rock is desti...|
	|    1|the gorgeously el...|
	|    1|effective but too...|
	|    1|if you sometimes ...|
	|    1|emerges as someth...|
	|    1|the film provides...|
	|    1|offers that rare ...|
	|    1|perhaps no pictur...|
	|    1|steers turns in a...|
	|    1|take care of my c...|
	|    1|this is a film we...|
	|    1|what really surpr...|
	|    1| ( wendigo is ) w...|
	|    1|one of the greate...|
	|    1|ultimately , it p...|
	|    1|an utterly compel...|
	|    1|illuminating if o...|
	|    1|a masterpiece fou...|
	|    1|the movie's ripe ...|
	|    1|offers a breath o...|
	+-----+--------------------+
	only showing top 20 rows
	
	positive = 5331

	+-----+--------------------+
	|label|            sentence|
	+-----+--------------------+
	|    0|simplistic , sill...|
	|    0|it's so laddish a...|
	|    0|exploitative and ...|
	|    0|[garbus] discards...|
	|    0|a visually flashy...|
	|    0|the story is also...|
	|    0|about the only th...|
	|    0|not so much farci...|
	|    0|unfortunately the...|
	|    0|all the more disq...|
	|    0|a sentimental mes...|
	|    0|while the perform...|
	|    0|interesting , but...|
	|    0|on a cutting room...|
	|    0|while the ensembl...|
	|    0|there is a differ...|
	|    0|nothing here seem...|
	|    0|such master scree...|
	|    0|here , common sen...|
	|    0|this 100-minute m...|
	+-----+--------------------+
	only showing top 20 rows
	
	negative = 5331

	polarity = 10662

	+-----+--------------------+--------------------+
	|label|            sentence|               words|
	+-----+--------------------+--------------------+
	|  1.0|this sci-fi techn...|[this, sci-fi, te...|
	|  1.0|a moving story of...|[a, moving, story...|
	|  0.0|nothing plot-wise...|[nothing, plot-wi...|
	|  1.0|with wit and empa...|[with, wit, and, ...|
	|  0.0|it's not difficul...|[it's, not, diffi...|
	|  0.0|even murphy's exp...|[even, murphy's, ...|
	|  1.0|its metaphors are...|[its, metaphors, ...|
	|  0.0|a beautifully sho...|[a, beautifully, ...|
	|  1.0|another in a long...|[another, in, a, ...|
	|  0.0|like those to rom...|[like, those, to,...|
	|  1.0|the tonal shifts ...|[the, tonal, shif...|
	|  0.0|the best thing ab...|[the, best, thing...|
	|  0.0|all movie long , ...|[all, movie, long...|
	|  0.0|this is amusing f...|[this, is, amusin...|
	|  1.0|the engagingly pr...|[the, engagingly,...|
	|  0.0|hope keeps arisin...|[hope, keeps, ari...|
	|  1.0|the entire movie ...|[the, entire, mov...|
	|  0.0|snipes relies too...|[snipes, relies, ...|
	|  1.0|mordantly funny a...|[mordantly, funny...|
	|  1.0|underachieves onl...|[underachieves, o...|
	+-----+--------------------+--------------------+
	only showing top 20 rows

	...
	...
		
	+-----+--------------------+
	|label|         rawFeatures|
	+-----+--------------------+
	|  1.0|(1000,[38,56,58,8...|
	|  1.0|(1000,[50,56,232,...|
	|  0.0|(1000,[56,84,170,...|
	|  1.0|(1000,[11,56,65,7...|
	|  0.0|(1000,[40,56,75,3...|
	|  0.0|(1000,[33,46,56,7...|
	|  1.0|(1000,[3,30,56,10...|
	|  0.0|(1000,[56,272,424...|
	|  1.0|(1000,[3,44,56,12...|
	|  0.0|(1000,[21,56,125,...|
	|  1.0|(1000,[3,16,56,92...|
	|  0.0|(1000,[56,305,662...|
	|  0.0|(1000,[44,56,95,1...|
	|  0.0|(1000,[56,662,696...|
	|  1.0|(1000,[56,171,250...|
	|  0.0|(1000,[0,56,58,23...|
	|  1.0|(1000,[56,305,468...|
	|  0.0|(1000,[56,168,289...|
	|  1.0|(1000,[56,81,187,...|
	|  1.0|(1000,[56,97,131,...|
	+-----+--------------------+
	only showing top 20 rows

	Threshold: 1.0, F-score: 0.7668611435239205, Beta = 1

	Threshold: 0.0, F-score: 0.6687316091063962, Beta = 1

	accuracy = 0.7675663099115868
	
