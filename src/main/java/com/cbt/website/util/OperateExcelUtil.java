package com.cbt.website.util;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 操作Excle的工具类(Excle的导入导出)
 *
 * @author liuwei
 *
 */

public class OperateExcelUtil {
    /**
     * @MethodName  : listToExcel
     * @Description : 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
     * @param list      数据源
     * @param fieldMap      类的英文属性和Excel中的中文列名的对应关系
     * 如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
     * 如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
     * fieldMap.put("college.collegeName","学院名称")
     * @param sheetName 工作表的名称
     * @param sheetSize 每个工作表中记录的最大个数
     * @param out       导出流
     * @throws ExcelException
     */

    //========================================导出方法一====================================
    //List集合的元素类型是泛型<T>实体对象
    public static <T>  void   listToExcel (
            List<T> list ,
            LinkedHashMap<String,String> fieldMap,
            String sheetName,
            int sheetSize,
            OutputStream out
    ) throws Exception{


        if(list.size()==0 || list==null){
            throw new Exception("数据源中没有任何数据");
        }

        if(sheetSize>65535 || sheetSize<1){
            sheetSize=65535;
        }

        //创建工作簿并发送到OutputStream指定的地方
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(out);

            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            //1.计算一共有多少个工作表
            double sheetNum=Math.ceil(list.size()/new Integer(sheetSize).doubleValue());

            //2.创建相应的工作表，并向其中填充数据
            for(int i=0; i<sheetNum; i++){
                //如果只有一个工作表的情况
                if(1==sheetNum){
                    WritableSheet sheet=wwb.createSheet(sheetName, i);
                    fillSheet(sheet, list, fieldMap, 0, list.size()-1);

                    //有多个工作表的情况
                }else{
                    WritableSheet sheet=wwb.createSheet(sheetName+(i+1), i);

                    //获取开始索引和结束索引
                    int firstIndex=i*sheetSize;
                    int lastIndex=(i+1)*sheetSize-1>list.size()-1 ? list.size()-1 : (i+1)*sheetSize-1;
                    //填充工作表
                    fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
                }
            }

            wwb.write();
            wwb.close();

        }catch (Exception e) {
            e.printStackTrace();
            //如果是ExcelException，则直接抛出
            if(e instanceof Exception){
                throw (Exception)e;

                //否则将其它异常包装成ExcelException再抛出
            }else{
                e.printStackTrace();
                throw new Exception("导出Excel失败");
            }
        }

    }
    private static <T> void fillSheet(
            WritableSheet sheet,
            List<T> list,
            LinkedHashMap<String,String> fieldMap,
            int firstIndex,
            int lastIndex
    )throws Exception{

        //定义存放英文字段名和中文字段名的数组
        String[] enFields=new String[fieldMap.size()];
        String[] cnFields=new String[fieldMap.size()];

        //填充数组
        int count=0;
        for(Entry<String,String> entry:fieldMap.entrySet()){
            enFields[count]=entry.getKey();
            cnFields[count]=entry.getValue();
            count++;
        }
        //填充表头
        for(int i=0;i<cnFields.length;i++){
            Label label=new Label(i,0,cnFields[i]);
            sheet.addCell(label);
        }

        //填充内容
        int rowNo=1;
        for(int index=firstIndex;index<=lastIndex;index++){
            //获取单个对象
            T item=list.get(index);
            for(int i=0;i<enFields.length;i++){
                Object objValue=getFieldValueByNameSequence(enFields[i], item);
                String fieldValue=objValue==null ? "" : objValue.toString();
                Label label =new Label(i,rowNo,fieldValue);
                sheet.addCell(label);
            }

            rowNo++;
        }

        //设置自动列宽
        //setColumnAutoSize(sheet, 5);
    }
    private static  Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception{

        Object value=null;

        //将fieldNameSequence进行拆分
        String[] attributes=fieldNameSequence.split("\\.");
        if(attributes.length==1){
            value=getFieldValueByName(fieldNameSequence, o);
        }else{
            //根据属性名获取属性对象
            Object fieldObj=getFieldValueByName(attributes[0], o);
            String subFieldNameSequence=fieldNameSequence.substring(fieldNameSequence.indexOf(".")+1);
            value=getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;

    }
    private static Field getFieldByName(String fieldName, Class<?>  clazz){
        //拿到本类的所有字段
        Field[] selfFields=clazz.getDeclaredFields();

        //如果本类中存在该字段，则返回
        for(Field field : selfFields){
            if(field.getName().equals(fieldName)){
                return field;
            }
        }

        //否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz=clazz.getSuperclass();
        if(superClazz!=null  &&  superClazz !=Object.class){
            return getFieldByName(fieldName, superClazz);
        }

        //如果本类和父类都没有，则返回空
        return null;
    }
    private static  Object getFieldValueByName(String fieldName, Object o) throws Exception{

        Object value=null;
        Field field=getFieldByName(fieldName, o.getClass());

        if(field !=null){
            field.setAccessible(true);
            value=field.get(o);
        }else{
            throw new Exception(o.getClass().getSimpleName() + "类不存在字段名 "+fieldName);
        }

        return value;
    }

    //============================================导出方法二================================

    //List集合的元素类型是Map<String,Object>
    public static  void   writeToExcel (
            List<Map<String,Object>> list ,//数据列表
            LinkedHashMap<String,String> fieldMap, //表头
            String sheetName, //工作簿的名称
            int sheetSize,
            OutputStream out
    ) throws Exception{


        if(list.size()==0 || list==null){
            throw new Exception("数据源中没有任何数据");
        }

        if(sheetSize>65535 || sheetSize<1){
            sheetSize=65535;
        }

        //创建工作簿并发送到OutputStream指定的地方
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(out);

            //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
            //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            //1.计算一共有多少个工作表
            double sheetNum=Math.ceil(list.size()/new Integer(sheetSize).doubleValue());

            //2.创建相应的工作表，并向其中填充数据
            for(int i=0; i<sheetNum; i++){
                //如果只有一个工作表的情况
                if(1==sheetNum){
                    WritableSheet sheet=wwb.createSheet(sheetName, i);
                    fillWorkSheet(sheet, list, fieldMap, 0, list.size()-1);

                    //有多个工作表的情况
                }else{
                    WritableSheet sheet=wwb.createSheet(sheetName+(i+1), i);

                    //获取开始索引和结束索引
                    int firstIndex=i*sheetSize;
                    int lastIndex=(i+1)*sheetSize-1>list.size()-1 ? list.size()-1 : (i+1)*sheetSize-1;
                    //填充工作表
                    fillWorkSheet(sheet, list, fieldMap, firstIndex, lastIndex);
                }
            }

            wwb.write();
            wwb.close();

        }catch (Exception e) {
            e.printStackTrace();
            //如果是ExcelException，则直接抛出
            if(e instanceof Exception){
                throw (Exception)e;

                //否则将其它异常包装成ExcelException再抛出
            }else{
                e.printStackTrace();
                throw new Exception("导出Excel失败");
            }
        }

    }
    private static  void fillWorkSheet(
            WritableSheet sheet,
            List<Map<String,Object>> list,
            LinkedHashMap<String,String> fieldMap,
            int firstIndex,
            int lastIndex
    )throws Exception{

        //定义存放英文字段名和中文字段名的数组
        String[] enFields=new String[fieldMap.size()];
        String[] cnFields=new String[fieldMap.size()];

        //填充数组
        int count=0;
        for(Entry<String,String> entry:fieldMap.entrySet()){
            enFields[count]=entry.getKey();
            cnFields[count]=entry.getValue();
            count++;
        }
        //填充表头
        for(int i=0;i<cnFields.length;i++){
            Label label=new Label(i,0,cnFields[i]);
            sheet.addCell(label);
        }

        //填充内容
        int rowNo=1;
        for(int index=firstIndex;index<=lastIndex;index++){
            //获取单个对象
            Map<String,Object> item=list.get(index);
            for(int i=0;i<enFields.length;i++){
                Object objValue=item.get(enFields[i]);
                String fieldValue=objValue==null ? "" : objValue.toString();
                Label label =new Label(i,rowNo,fieldValue);
                sheet.addCell(label);
            }

            rowNo++;
        }

    }




    //======================================导入=========================================

    /**
     * 解析Excle文件中的数据(Excel包含了表头字段)
     *
     * @param fileName
     * @throws Exception
     *
     */
    public static String [][] readExcelInfo(String fileName) throws Exception {
        // 构造Workbook（工作薄）对象
        Workbook book = Workbook.getWorkbook(new File(fileName));
        // 获得第一个工作表对象
        Sheet sheet = book.getSheet(0);
        // 获得Excel文件多少行多少列
        int columnum = sheet.getColumns(); // 得到列数
        int rownum = sheet.getRows(); // 得到行数
        StringBuilder builder = new StringBuilder();
        for (int i = 1; i < rownum; i++) // 循环进行读写(表头不读取)
        {
            for (int j = 0; j < columnum; j++) {
                Cell cell1 = sheet.getCell(j, i);//获取工作表中的单元格
                String result = cell1.getContents();//获取单元格中的数据
                builder.append(result);
                if(i == rownum - 1 && j == columnum - 1){
                    break;
                }
                else {
                    builder.append(",");
                }
            }
        }
        book.close();// 关闭（工作薄）对象

        //对获取到的Excle数据进行格式处理
        String[] excleData = builder.toString().split(",");

        String [][] formatArr = new String [rownum - 1][columnum];

        for(int m = 0,x = 0; x < formatArr.length; x++){

            for(int y= 0; y < formatArr[x].length;y++){

                if(m < excleData.length){

                    formatArr[x][y] = excleData[m];
                    m++;
                }
            }
        }

        return formatArr;
    }

    //=========================MapToBean===========================

    /**
     * Map集合转换成Javabean
     * @param map 需要转换的Map集合
     * @param obj 需要转换成的JavaBean类型
     * @return    返回转换好的JavaBean
     */
    public static Object mapToBean(Map<String, Object> map, Object obj) {

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();

                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(obj, value);
                }
            }

        } catch (Exception e) {
            System.out.println("mapToBean 转换错误！ " + e);
        }

        return obj;
    }




}
