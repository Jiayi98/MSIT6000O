package org.hkust.msit6000o.service;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
//import org.jcp.xml.dsig.internal.SignerOutputStream;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class SparkService {

//    @Value("${filePath}")
    static private String filePath = "./src/main/resources/static/";

    public String sparkProcessing(MultipartFile file, String fileName) throws Exception {
        // 1. 调用spark来process 文件
        // 2. 将上一步的结果上传到s3
        String temp_filename = file.getOriginalFilename().replace(".csv", "_result.txt");
        process(convertMultiPartFileToFile(file), temp_filename);
        System.out.println("上传成功啦，进入s3处理函数");

        return  temp_filename;

    }
    private File convertMultiPartFileToFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            System.out.println("Error converting multipartFile to file");
        }
        return convertedFile;
    }
    public static void writeFileContext(List<String>  strings, String path, Boolean isAppended) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        FileWriter content = content = new FileWriter(file, isAppended);
        BufferedWriter writer = writer = new BufferedWriter(content);
        for (String l:strings){
            writer.write(l + "\r\n");
        }
        writer.close();
    }
    public static void writewWithString(String  str, String path, Boolean isAppended) throws Exception {
        File file = new File(path);
        //如果没有文件就创建
        if (!file.isFile()) {
            file.createNewFile();
        }
        FileWriter content = content = new FileWriter(file, isAppended);
        BufferedWriter writer = writer = new BufferedWriter(content);
        writer.write(str + "\r\n");
        writer.close();
    }

    public static void clearFile(String path) throws IOException {
        File file = new File(path);
        FileWriter content = content = new FileWriter(file, false);
        BufferedWriter writer = writer = new BufferedWriter(content);
    }
    public static void process(File file, String temp_filename) throws Exception {
        //数据分析
        //将结果写入txt文件
        // spark.........write(filePath)
        String input_path = file.getAbsolutePath();
        String path = filePath + temp_filename;
        clearFile(path);
        SparkSession spark =SparkSession.builder()
                .appName("SparkSQLTest1")
                .config("spark.some.config.option", "some-value")
                .getOrCreate();
        Dataset<Row> df = spark.read().format("csv").option("header", "true").load(input_path);
        df.createOrReplaceTempView("StudentsPerformance");
        //各科均分
        Dataset<Row> avg = spark.sql("select avg(math_score) math_avg, avg(reading_score) reading_avg, avg(writing_score) writing_avg from StudentsPerformance");
        writewWithString("Average scores of courses:", path, true);
        String avg_str = avg.toJavaRDD().map(x -> x.toString()).collect().get(0);
        String[] avg_array = avg_str.substring(1, avg_str.length() - 1).split(",");
        String[] course = {"math ", "reading ", "writing "};
        String avg_all = "";
        for(int i = 0; i < avg_array.length; i++){
            avg_all = avg_all + course[i] + avg_array[i];
            if(i != (avg_array.length - 1))
                avg_all += '|';
        }
        avg_all += "\n";
        writewWithString(avg_all, path, true);
        //各科分数分布
        writewWithString("The distribution of math grades:", path, true);
        Dataset<Row> math_sql = spark.sql("select sum(if(math_score  < 60, 1, 0)), " +
                "sum(if(math_score between 60 and 69, 1, 0)), " +
                "sum(if(math_score between 70 and 79, 1, 0)), " +
                "sum(if(math_score between 80 and 89, 1, 0)), " +
                "sum(if(math_score between 90 and 100, 1, 0)) " +
                "from StudentsPerformance");
        String[] distr = {"< 60: ", "60 ~ 69: ","70 ~ 79: ","80 ~ 89: ","90 ~ 100: "};
        String math = math_sql.toJavaRDD().map(x -> x.toString()).collect().get(0);
        String[] math_array = math.substring(1, math.length() - 1).split(",");

        for(int i = 0; i < math_array.length; i++){
            String s = distr[i] + math_array[i];
            writewWithString(s, path, true);
        }
        writewWithString("\n", path, true);
        writewWithString("The distribution of reading grades:", path, true);
        Dataset<Row> reading_sql = spark.sql("select sum(if(reading_score  < 60, 1, 0)), " +
                "sum(if(reading_score between 60 and 69, 1, 0)), " +
                "sum(if(reading_score between 70 and 79, 1, 0)), " +
                "sum(if(reading_score between 80 and 89, 1, 0)), " +
                "sum(if(reading_score between 90 and 100, 1, 0)) " +
                "from StudentsPerformance");
        String reading = reading_sql.toJavaRDD().map(x -> x.toString()).collect().get(0);
        String[] reading_array = reading.substring(1, reading.length() - 1).split(",");
        for(int i = 0; i < reading_array.length; i++){
            String s = distr[i] + reading_array[i];
            writewWithString(s, path, true);
        }
        writewWithString("\n", path, true);
        writewWithString("The distribution of writing grades:", path, true);
        Dataset<Row> writing_sql = spark.sql("select sum(if(writing_score  < 60, 1, 0)), " +
                "sum(if(writing_score between 61 and 69, 1, 0)), " +
                "sum(if(writing_score between 71 and 79, 1, 0)), " +
                "sum(if(writing_score between 81 and 89, 1, 0)), " +
                "sum(if(writing_score between 91 and 100, 1, 0)) " +
                "from StudentsPerformance");
        String writing = writing_sql.toJavaRDD().map(x -> x.toString()).collect().get(0);
        String[] writing_array = writing.substring(1, writing.length() - 1).split(",");
        for(int i = 0; i < writing_array.length; i++){
            String s = distr[i] + writing_array[i];
            writewWithString(s, path, true);
        }
        writewWithString("\n", path, true);
        //各科成绩前十学生
        writewWithString("Top 10 students in math", path, true);
        Dataset<Row> mathtop_sql  = spark.sql("select * from StudentsPerformance order by math_score desc limit 10");
        List math_top = mathtop_sql.toJavaRDD().map(x -> x.toString()).collect();
        writeFileContext(math_top, path, true);
        writewWithString("\n", path, true);

        writewWithString("Top 10 students in reading", path, true);
        Dataset<Row> readingtop_sql  =  spark.sql("select * from StudentsPerformance order by reading_score desc limit 10");
        List reading_top = readingtop_sql.toJavaRDD().map(x -> x.toString()).collect();
        writeFileContext(reading_top, path, true);
        writewWithString("\n", path, true);

        writewWithString("Top 10 students in writing", path, true);
        Dataset<Row> writingtop_sql  =  spark.sql("select * from StudentsPerformance order by writing_score desc limit 10");
        List writing_top = writingtop_sql.toJavaRDD().map(x -> x.toString()).collect();
        writeFileContext(writing_top, path, true);
        spark.stop();

    }

    public static void main(String[] args) throws Exception {
        String path = "/Users/jiayizhou/Desktop/StudentsPerformance_2015_sp.csv";
        File file = new File(path);
        String temp_filename = "result.txt";
        process(file, temp_filename);
    }


}
