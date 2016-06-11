package org.my.test;

import org.my.util.DatabaseOperator;
import org.my.util.TableItemValues;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2016/5/10.
 */
public class Test {
    public static void main(String[] args){
        DatabaseOperator operator = new DatabaseOperator("127.0.0.1",1433,"sa","123456","MusicDataAna");
        operator.add2table("Singer",new TableItemValues(new String[]{"刘德华"}));
        operator.deleteFromTable("Delete from singer where singer_name = '刘德华';");
        operator.updateTable("update singer set singer_name = '周杰伦' where singer_name = '刘德华';");
        ResultSet res = operator.selectFromTable("select * from singer;");

        try {
            while (res.next()){
                System.out.print(res.getInt(1));
                System.out.print(res.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
