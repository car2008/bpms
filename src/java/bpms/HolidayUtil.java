package bpms;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * 根据读取配置的file判断是否为法定节假日，及法定工作日
 * @author Administrator
 *
 */
public class HolidayUtil {
    
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    private List<String> holidays = new ArrayList<String>();
    private List<String> workdays = new ArrayList<String>();
    /**
     * 获取剩余的工作日天数
     * @param date
     * @return
     * @throws ParseException 
     */
    public int getWorkDayNum(String filePath,String dateStr) throws ParseException{
    	Date now=new Date();
    	boolean flag = true;
        int count=0;
        String today="";
    	Date da=sdf.parse(dateStr);
    	if(da.getTime()>=now.getTime()){
            parseFile(filePath);//读取file中的节假日和工作日
            //System.out.println(holidays);
           // System.out.println(workdays);
            Calendar c = Calendar.getInstance();
            for(int i=0;i<=3650;i++){
            	c.setTime(now); 
            	c.set(Calendar.DATE, c.get(Calendar.DATE) + i);  
            	int dateType = getDateType(c);
                today = sdf.format(c.getTime());
                //System.out.println("date..."+today);
                //如果文件不存在当前日期。判断是否周六日
                if(dateType==0){
                    if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||
                            c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
                        flag = false;
                    }else{
                    	flag=true;
                    }
                    
                }else{//如果存在当前日期，根据返回的类型判断
                    
                    if(dateType==1){//节假日
                        flag = false;
                    }else if(dateType==2){//工作日
                        flag = true;
                    }
                    
                }
              // System.out.println(today+"是："+(flag==true?"工作日":"节假日"));
                if(flag){
           		    count++;
    	       	}
               // System.out.println(count);
    	       	if(today.equals(dateStr) || today==dateStr){
    	       		break;
    	       	}
            }
    	}
    	
        //return today;
    	//String s=sdf.format(date);
    	//String str=dateStr;
    	//System.out.println(dateStr);
    	//int daynum=0;
    	//for(int i=0;i<=30;i++){
    	//	String ss=getWorkDay(filePath,i+1);
    	//	System.out.println(ss);
    	//	if(str.equals(ss)){
    	//		System.out.println("123");
    	//		daynum=i;
    	//		break;
    	//	}else{
    	//		System.out.println("456");
    	//	}
    	//}
    	return count-1;
    }
    /**
     * 判断当天是否是工作日 (工作日：true；节假日：false)
     * @param filePath
     * @return
     */
    public String getWorkDay(String filePath,int n){
    	boolean flag = true;
        int count=0;
        String today="";
        parseFile(filePath);//读取file中的节假日和工作日
        //System.out.println(holidays);
        //System.out.println(workdays);
        Date now=new Date();
        Calendar c = Calendar.getInstance();
        for(int i=0;i<=3650;i++){
        	c.setTime(now); 
        	c.set(Calendar.DATE, c.get(Calendar.DATE) + i);  
        	int dateType = getDateType(c);
            today = sdf.format(c.getTime());
            //System.out.println("date..."+today);
            //如果文件不存在当前日期。判断是否周六日
            if(dateType==0){
                if(c.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY||
                        c.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
                    flag = false;
                }else{
                	flag=true;
                }
                
            }else{//如果存在当前日期，根据返回的类型判断
                
                if(dateType==1){//节假日
                    flag = false;
                }else if(dateType==2){//工作日
                    flag = true;
                }
                
            }
          // System.out.println(today+"是："+(flag==true?"工作日":"节假日"));
            if(flag){
       		    count++;
	       	}
           // System.out.println(count);
            
	       	if(count==(n+1)){
	       		break;
	       	}
        }
        return today;
    }
    
    /**
     * 根据判断当前时间是否是节假日还是工作日  (excel中不存在当前日期：0；节假日：1；工作日：2)
     * 如果当前日期在excel中的节假日和工作日都写了，默认的工作日
     * @return
     */
    private int getDateType(Calendar c){
        int type = 0;
        
        String today = sdf.format(c.getTime());
        
        if(holidays.size()>0){
            for(String holiday:holidays){
                if(holiday.equals(today)){
                    type = 1;
                    break;
                }
            }
        }
        
        if(workdays.size()>0){
            for(String workday:workdays){
                if(workday.equals(today)){
                    type = 2;
                }
            }
        }
        
        return type;
    }
    
    
    /**
     * 读取excel中的节假日和工作日
     * @param filePath
     */
    private void parseFile(String filePath){
        if(filePath==null||"".equals(filePath)){
            return ;
        }
        try {
        	Scanner in = new Scanner(new File(filePath));
            String result1 = "";
        	while (in.hasNextLine()) {
            	
                result1 = in.nextLine() ;
                
                String[] strArray=result1.split("\t");
	            if(strArray==null||strArray.length==0){
	                return;
	            }
	            //获取第一列数据-节假日
	            String analyStartDate="";
	            String startDateStr="";
	            try{
	            	analyStartDate=strArray[0].trim();
	                if(analyStartDate=="" || analyStartDate.equals("") || "\\".contains(analyStartDate)){
	                	analyStartDate="";
	                }else{
	                	Pattern pa = Pattern.compile("\\D+");
	                	String[] numbers = pa.split(analyStartDate);
	                	int a=0;
	                	for(String number:numbers){
	                	   if(null!=number && !"".equals(number)){
	                		   if(a>0 && a<3){
	                			   int i = Integer.parseInt(number);
	                			   if(i<10 && !number.contains("0")){
	                				   startDateStr=startDateStr+"-"+"0"+number;
	                			   }else{
	                				   startDateStr=startDateStr+"-"+number;
	                			   }
	                			   a++;
	                		   }else if(a==0){
	                			   startDateStr=""+number;
	                			   a++;
	                		   }else{
	                			   break;
	                		   }
	                	   } 
	                	}
	                	if(a<3 || a>=4){
	                		startDateStr="";
	    	     		}
	                	analyStartDate=startDateStr;
	                	holidays.add(analyStartDate);
	                }
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            
	            //获取第二列数据-工作日
	            String workDate="";
	            String workDateStr="";
	            try{
	            	workDate=strArray[1].trim();
	                if(workDate=="" || workDate.equals("") || "\\".contains(workDate)){
	                	workDate="";
	                }else{
	                	Pattern pa = Pattern.compile("\\D+");
	                	String[] numbers = pa.split(workDate);
	                	int a=0;
	                	for(String number:numbers){
	                	   if(null!=number && !"".equals(number)){
	                		   if(a>0 && a<3){
	                			   int i = Integer.parseInt(number);
	                			   if(i<10 && !number.contains("0")){
	                				   workDateStr=workDateStr+"-"+"0"+number;
	                			   }else{
	                				   workDateStr=workDateStr+"-"+number;
	                			   }
	                			   a++;
	                		   }else if(a==0){
	                			   workDateStr=""+number;
	                			   a++;
	                		   }else{
	                			   break;
	                		   }
	                	   } 
	                	}
	                	if(a<3 || a>=4){
	                		workDateStr="";
	    	     		}
	                	workDate=workDateStr;
	                	workdays.add(workDate);
	                }
	            }catch(Exception e){
	            	e.printStackTrace();
	            }
	            
        	}
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    /*public static void main(String[] args) throws ParseException {
        HolidayUtil h = new HolidayUtil();
        int n=12;
    	String workday = h.getWorkDay("D:\\shiyanshuju\\workday-holiday\\3.txt",n);
        System.out.println(n+"个工作日之后是："+workday);	
        String s="2017-02-06";
        int a=h.getWorkDayNum("D:\\shiyanshuju\\workday-holiday\\3.txt",s);
        System.out.println("距离2017-02-06的工作日还有："+a+"天");
        // boolean flag = h.isWorkDay("D:/shiyanshuju/Proshuju/proshuju.txt",new Date());
        //        boolean flag = h.isWorkDay("");
    }*/
}
