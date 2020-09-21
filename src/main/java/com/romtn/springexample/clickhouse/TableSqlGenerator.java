package com.romtn.springexample.clickhouse;

public class TableSqlGenerator {


    public static void main(String[] args) {
        String col = "c%s String COMMENT '数据列',";
        for (int i = 0; i < 100; i++) {
            System.out.println(String.format(col, i));
        }
    }

}
