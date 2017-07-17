package com.autolocations.auto_location;
import org.apache.spark.sql.Dataset;
//import org.apache.spark.api.java.function.FlatMapFunction;
//import org.apache.spark.sql.*;
//import org.apache.spark.sql.streaming.StreamingQuery;
import org.apache.spark.sql.SparkSession;
import java.util.Arrays;
import java.util.Iterator;
public class SparkSession_instance {

	SparkSession spark = SparkSession.builder().appName("Simple Application").getOrCreate();
	String logFile = "C:/Users/n0236074/workspace/spark-2.2.0-bin-hadoop2.7/README.md"; // Should be some file on your system

    Dataset<String> logData = spark.read().textFile(logFile).cache();

    long numAs = logData.filter(s -> s.contains("a")).count();
    long numBs = logData.filter(s -> s.contains("b")).count();
   
    //System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

    //spark.stop();

}
