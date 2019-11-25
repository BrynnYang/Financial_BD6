import com.google.common.io.Resources;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;
import java.util.List;
import org.apache.hadoop.hbase.*;

import java.io.IOException;

public class HbaseDDL {
    private static Configuration configuration;
    private static Connection connection;
    private static Admin admin;
    static {
        //1.获得Configuration实例并进行相关设置
        configuration = HBaseConfiguration.create();
        configuration.addResource(Resources.getResource("hbase-site.xml"));
        //2.获得Connection实例
        try {
            connection = ConnectionFactory.createConnection(configuration);
            //3.获得Admin接口
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException {
        //创建表
        String  familyNames[]={"Description","Courses","Home"};
        createTable("students",familyNames);
        //向表中插入数据
        insert("students","001","Description","Name","Li Lei");
        insert("students","001","Description","Height","176");
        insert("students","001","Courses","Chinese","80");
        insert("students","001","Courses","Math","90");
        insert("students","001","Courses","Physics","95");
        insert("students","001","Home","Province","Zhejiang");

        insert("students","002","Description","Name","Han Meimei");
        insert("students","002","Description","Height","183");
        insert("students","002","Courses","Chinese","88");
        insert("students","002","Courses","Math","77");
        insert("students","002","Courses","Physics","66");
        insert("students","002","Home","Province","Beijing");

        insert("students","003","Description","Name","Xiao Ming");
        insert("students","003","Description","Height","162");
        insert("students","003","Courses","Chinese","90");
        insert("students","003","Courses","Math","90");
        insert("students","003","Courses","Physics","90");
        insert("students","003","Home","Province","Shanghai");
    }
    //创建表
    public static void createTable(String tableName, String familyNames[]) throws IOException {
        //如果表存在退出
        if (admin.tableExists(TableName.valueOf(tableName))) {
            System.out.println("Table exists!");
            return;
        }
        //通过HTableDescriptor类来描述一个表，HColumnDescriptor描述一个列族
        HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
        for (String familyName : familyNames) {
            tableDescriptor.addFamily(new HColumnDescriptor(familyName));
        }
        //tableDescriptor.addFamily(new HColumnDescriptor(familyName));
        admin.createTable(tableDescriptor);
        System.out.println("create table successfully!");
    }

    //插入数据
    public static void insert(String tableName, String rowKey, String family, String column, String value) throws IOException {
        //3.2获得Table接口,需要传入表名
        Table table =connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes(family), Bytes.toBytes(column), Bytes.toBytes(value));
        table.put(put);
        System.out.println("insert" +" "+ rowKey +" "+ column +" "+ value +" "+ " to table " + " "+tableName +" "+ " successfully!");
    }

}
