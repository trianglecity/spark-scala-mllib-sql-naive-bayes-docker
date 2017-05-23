
import org.apache.spark
import org.apache.spark._
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.rdd.RDD

import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.ml.linalg.SparseVector;
import org.apache.spark.ml.linalg.DenseVector;
import org.apache.spark.mllib.regression.LabeledPoint

import org.apache.spark.ml.feature.Tokenizer
import org.apache.spark.ml.feature.{HashingTF, IDF}
import org.apache.spark.ml.feature.StopWordsRemover

import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.sql.functions.rand

import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

object NaiveBayesClassification {

	def main(args: Array[String]):Unit = {
		
		println("... Scala Naive Bayes ...")
		val conf = new SparkConf().setAppName("Naive Bayes").setMaster("local")
		val sc = new SparkContext( conf)

		val sqlContext = new org.apache.spark.sql.SQLContext(sc)

		import sqlContext.implicits._

		val positiveData = sc.textFile("src/main/resources/data/rt-polaritydata/rt-polarity.pos").map {x=> LabelSentence(1,x)}.toDF("label", "sentence")
      		
		val negativeData = sc.textFile("src/main/resources/data/rt-polaritydata/rt-polarity.neg").map {x=> LabelSentence(0,x)}.toDF("label", "sentence")

		//sc.textFile("path/source", "path/file1", "path/file2").coalesce(1).saveAsTextFile("path/newSource")

		positiveData.show()
		println("positve# = " + positiveData.count)

		negativeData.show()
		println("negative# = " + negativeData.count)
		
				

		val polarityData = positiveData.union(negativeData)
		val shuffledDF = polarityData.orderBy(rand())
		println("polarity = " + shuffledDF.count)
	

		val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
		val wordsData = tokenizer.transform(shuffledDF)
		wordsData.show()


		val stopwords: Array[String] = sc.textFile("src/main/resources/data/english_stops_words.txt").collect()
		val remover = new StopWordsRemover()
					.setStopWords(stopwords)
  					.setCaseSensitive(false)
  					.setInputCol("words")
  					.setOutputCol("filtered")
		
		val filteredData = remover.transform(wordsData)
		filteredData.show()

		val hashingTF = new HashingTF()
					.setInputCol("filtered")
					.setOutputCol("rawFeatures")
					.setNumFeatures(100000)

		val featurizedData = hashingTF.transform(filteredData)

		//val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
		//val idfModel = idf.fit(featurizedData)

		//val rescaledData = idfModel.transform(featurizedData)
		//val labelFeatures = rescaledData.select("label", "features")
		val labelFeatures = featurizedData.select("label", "rawFeatures")

		labelFeatures.show()
		
		
		val labelVector = labelFeatures.map(row => LabeledPoint(row.getDouble(0), Vectors.dense(row(1).asInstanceOf[SparseVector].toDense.toArray)))
		val featuresRdd  = labelVector.rdd
		
		val Array(training, test) = featuresRdd.randomSplit(Array(0.6, 0.4))
		
		val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")
		val predictionAndLabels = test.map(p => (model.predict(p.features), p.label))

		val accuracy = 1.0 * predictionAndLabels.filter(x => x._1 == x._2).count() / test.count()

		

		// Instantiate metrics object
		val metrics = new BinaryClassificationMetrics(predictionAndLabels)
		
		// F-measure
		val f1Score = metrics.fMeasureByThreshold
			f1Score.foreach { case (t, f) =>
  			println(s"Threshold: $t, F-score: $f, Beta = 1")
		}

		println("accuracy = " + accuracy)	

		println("... Scala Naive Bayes end ...")
		sc.stop()
	}

	case class LabelSentence(id:Double, filed2:String)
}
