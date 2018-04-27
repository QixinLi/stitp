package foil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Start {
	public static String srcdir="D:\\stitpdata\\";   //foil算法文件地址目录
	
	public ArrayList<HumanPass> myHumanData=new ArrayList<HumanPass>();//人体红外热释电数据
	public ArrayList<Weather> myWeatherData=new ArrayList<Weather>();//天气情况数据
	public ArrayList<Holiday> myHolidayData=new ArrayList<Holiday>();//节假日数据
	public ArrayList<RuleSet> positiveSet=new ArrayList<RuleSet>();//P集合
	public ArrayList<RuleSet> negativeSet=new ArrayList<RuleSet>();//N集合
	public ArrayList<RuleSet> tpositiveSet=new ArrayList<RuleSet>();//P'集合
	public ArrayList<RuleSet> tnegativeSet=new ArrayList<RuleSet>();//N'集合
	public ArrayList<RuleSet> myRleave=new ArrayList<RuleSet>();//R_Leave规则集
	public ArrayList<RuleSet> myRback=new ArrayList<RuleSet>();//R_Back规则集
	public ArrayList<RuleSet> myR=new ArrayList<RuleSet>();//R规则集
	public ArrayList<RuleSet> myr=new ArrayList<RuleSet>();//r规则集
	public int ruleindex;//当前规则index
	public boolean rulebool;//当前规则值
	
	public Start() {
		getHumanPassData();
		//System.out.println("成功获取人体传感数据");
		getWeatherData();
		//System.out.println("成功获取天气数据");
		getHolidayData();
		//System.out.println("成功获取节假日数据");
		setRuleSet_Leave();
		//System.out.println("制定数据集（离开家）");
		//System.out.println("正在调用FOIL算法.....");
		FOIL();
		//System.out.println("算法执行完毕");
		myRleave.addAll(myR);
		for(int i=0;i<myR.size();i++) {
			System.out.println(myR.get(i).index+":"+myR.get(i).getAttrByIndex(myR.get(i).index));
		}
		setRuleSet_Back();
		FOIL();
		myRback.addAll(myR);
		for(int i=0;i<myR.size();i++) {
			System.out.println(myR.get(i).index+":"+myR.get(i).getAttrByIndex(myR.get(i).index));
		}
		try {
			writetofile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getHumanPassData() //获取本地人体热释电数据
	{
		File file = new File(srcdir+"humanpass1.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String timeData[]=tempString.split("-");
                if(timeData.length==6) {
                	HumanPass tempHuman = new HumanPass(
                			Integer.parseInt(timeData[0]),
                			Integer.parseInt(timeData[1]),
                			Integer.parseInt(timeData[2]),
                			Integer.parseInt(timeData[3]),
                			Integer.parseInt(timeData[4]),
                			Integer.parseInt(timeData[5])
                			);
                	myHumanData.add(tempHuman);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	public void getWeatherData() //获取本地天气情况数据
	{
		File file = new File(srcdir+"天气情况.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String wRead[]=tempString.split("#");
                String wData[]=wRead[0].split("-");
                if(wData.length==3) {
                	Weather tempWeather = new Weather(
                			Integer.parseInt(wData[0]),
                			Integer.parseInt(wData[1]),
                			Integer.parseInt(wData[2]),
                			Boolean.parseBoolean(wRead[1])
                			);
                	myWeatherData.add(tempWeather);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}
	
	public void getHolidayData() //获取法定节假日数据
	{
		File file = new File(srcdir+"节假日情况.txt");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                String wRead[]=tempString.split("-");
                if(wRead.length==4) {
                	Holiday tempHoliday = new Holiday(
                			Integer.parseInt(wRead[0]),
                			Integer.parseInt(wRead[1]),
                			Integer.parseInt(wRead[2]),
                			Integer.parseInt(wRead[3])
                			);
                	myHolidayData.add(tempHoliday);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
	}

	public void setRuleSet_Leave() //设置规则集（针对Leave）
	{
		positiveSet.clear();
		negativeSet.clear();
		boolean isleave=true;
		for(int i=0;i<myHumanData.size();i++) {
			if(isleave) {
				HumanPass temp=myHumanData.get(i);
				positiveSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),temp.hour));
				boolean isFirstLeave=false;
				long daybt=0;
				if(i==0)
				{
					isFirstLeave=true;
					daybt=0;
				}
				if(i!=0&&myHumanData.get(i-1).day!=temp.day)
				{
					isFirstLeave=true;
					daybt=getDayBetween(myHumanData.get(i-1).getDate(),temp.getDate());
					//System.out.println(daybt);
				}
				if(isFirstLeave) {
					for(int k=0;k<temp.hour&&k<24;k++) {
						negativeSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),k));
					}
				}
				else {
					if(daybt>1) {
						for(int d=1;d<daybt;d++) {
							for(int f=0;f<24;f++) {
								HumanPass dtemp=myHumanData.get(i-d);
								negativeSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(dtemp.year,dtemp.month,dtemp.day),getSun(dtemp.year,dtemp.month,dtemp.day),getHoliday(dtemp.year,dtemp.month,dtemp.day),f));
							}
						}
					}
					for(int k=myHumanData.get(i-1).hour+1;k<temp.hour;k++){
						negativeSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),k));
					}
				}
				isleave=false;
			}
			else {
				isleave=true;
			}
		}
	}
	
	public void setRuleSet_Back() //设置规则集（针对Back）
	{
		positiveSet.clear();
		negativeSet.clear();
		boolean isBack=false;
		for(int i=0;i<myHumanData.size();i++) {
			if(isBack) {
				HumanPass temp=myHumanData.get(i);
				positiveSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),temp.hour));
				boolean isFirstBack=false;
				if(i==0)
				{
					isFirstBack=true;
				}
				if(i!=0&&myHumanData.get(i-1).day!=temp.day)
				{
					isFirstBack=true;
					//System.out.println(daybt);
				}
				if(isFirstBack) {
					for(int k=0;k<temp.hour&&k<24;k++) {
						negativeSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),k));
					}
				}
				else {
					for(int k=myHumanData.get(i-1).hour+1;k<temp.hour;k++){
						negativeSet.add(new RuleSet(getWCondition(temp.year,temp.month,temp.day),getSat(temp.year,temp.month,temp.day),getSun(temp.year,temp.month,temp.day),getHoliday(temp.year,temp.month,temp.day),k));
					}
				}
				isBack=false;
			}
			else {
				isBack=true;
			}
		}
	}

	public void FOIL() //FOIL算法本体
	{
		myR.clear();
		while(positiveSet.size()>0) {
			//清空P',N',并重新赋值
			tpositiveSet.clear();
			tnegativeSet.clear();
			tpositiveSet.addAll(positiveSet);
			tnegativeSet.addAll(negativeSet);
			//将r置为空集
			myr.clear();
			while(tnegativeSet.size()>0 && myr.size()<5) 
			{
				findMaxGain();//找到一个拥有最大Gain值的属性
				//System.out.println(this.ruleindex+":"+this.rulebool);
				myr.add(new RuleSet(this.ruleindex,this.rulebool));//将该属性插入r
				deleteIrregularPNpie(this.ruleindex,this.rulebool);//将P' N'中所有不符合r的规则删去
			}
			//myR.addAll(myr);
			addrtoR();
			deletePbyr();
		}
	}
	
	public void findMaxGain() //找到目前Gain值最大的属性p
	{
		double maxGain=0;
		for(int i=-5;i<24;i++)
		{
			int ppie=tpositiveSet.size();
			int npie=tnegativeSet.size();
			boolean tempbool=true;
			for(int k=0;k<1;k++)//此处k设为<1表示只跑为true的属性，<2则是true/false都跑
			{
				int pstar=getP_Star(i,tempbool);
				int nstar=getN_Star(i,tempbool);
				if(pstar!=0) {
					
					double Gain = Math.pow(( (pstar*(ppie+npie))  /  (ppie*(pstar+nstar)) ) , pstar) ;
					//System.out.println("gain:"+Gain+",p*="+pstar+",n*="+nstar+",p'="+ppie+",n'="+npie);
					if(Gain>maxGain)
					{
						maxGain=Gain;
						this.ruleindex=i;
						this.rulebool=tempbool;
					}
				}
				tempbool=false;
			}
		}
	}
	
	public int getP_Star(int index,boolean setbool) //得出|P*|的值
	{
		int p_star=0;
		for(int k=0;k<tpositiveSet.size();k++) {
			if(tpositiveSet.get(k).getAttrByIndex(index)==setbool) {
				p_star++;
			}
		}
		return p_star;
	}
	
	public int getN_Star(int index,boolean setbool) //得出|N*|的值
	{
		int n_star=0;
		for(int k=0;k<tnegativeSet.size();k++) {
			if(tnegativeSet.get(k).getAttrByIndex(index)==setbool) {
				n_star++;
			}
		}
		return n_star;
	}

	public void deleteIrregularPNpie(int index,boolean bool) //删除P',N'中所有不符合r的行
	{
		for(int k=0;k<myr.size();k++) 
		{
			for(int i=0;i<tpositiveSet.size();i++)
			{
				if(tpositiveSet.get(i).getAttrByIndex(myr.get(k).index)!=myr.get(k).getAttrByIndex(myr.get(k).index))
				{
					tpositiveSet.remove(i);
					i--;
				}
			}
		}
		for(int k=0;k<myr.size();k++) 
		{
			for(int i=0;i<tnegativeSet.size();i++)
			{
				if(tnegativeSet.get(i).getAttrByIndex(myr.get(k).index)!=myr.get(k).getAttrByIndex(myr.get(k).index))
				{
					tnegativeSet.remove(i);
					i--;
				}
			}
		}

	}
	
	public void addrtoR() //将属性r添加到R（忽略其中的重复属性）
	{
		int [][]maxindex=new int[myr.size()][2];
		for(int i=0;i<myr.size();i++)
		{
			maxindex[i]=new int[2];
			for(int j=0;j<2;j++)
			{
				maxindex[i][j]=0;
			}
		}
		for(int i=0;i<myr.size();i++) {
			boolean needAdd=true;
			for(int j=0;j<myR.size();j++) {
				if(myR.get(j).index==myr.get(i).index&&myR.get(j).getAttrByIndex(myR.get(j).index)==myr.get(i).getAttrByIndex(myr.get(i).index)) {
					needAdd=false;
				}
			}
			if(needAdd) {
				myR.add(myr.get(i));
			}
		}
	}
	
	public void deletePbyr() //将P中不符合r的行删去
	{
		for(int i=0;i<positiveSet.size();i++) {
			boolean canDelete=true;
			for(int j=0;j<myr.size();j++) {
				int index=myr.get(j).index;
				if(positiveSet.get(i).getAttrByIndex(index)!=myr.get(j).getAttrByIndex(index)) {
					canDelete=false;
				}
			}
			if(canDelete) {
				positiveSet.remove(i);
				i--;
			}
		}
	}
	
	public long getDayBetween(String sDate,String bDate) //获取两个日期之间相隔的天数
	{
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long day = 0;
		long diff = 0;
		try {
			Date startDate= df.parse(sDate);
			Date bindDate= df.parse(bDate);
			long stime = startDate.getTime();
			long btime = bindDate.getTime();
			if(stime>btime){
				diff = stime - btime;
			}
			else
			{
				diff = btime - stime;
			}
				day = diff/(24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
	
	public boolean getSat(int year,int month,int day) //获取指定日期是否是周六
	{
		for(int i=0;i<myHolidayData.size();i++)
		{
			if(myHolidayData.get(i).year==year&&myHolidayData.get(i).month==month&&myHolidayData.get(i).day==day) {
				if(myHolidayData.get(i).isSat) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean getSun(int year,int month,int day) //获取指定日期是否是周日
	{
		for(int i=0;i<myHolidayData.size();i++)
		{
			if(myHolidayData.get(i).year==year&&myHolidayData.get(i).month==month&&myHolidayData.get(i).day==day) {
				if(myHolidayData.get(i).isSun) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean getHoliday(int year,int month,int day) //获取指定日期是否是节假日
	{
		for(int i=0;i<myHolidayData.size();i++)
		{
			if(myHolidayData.get(i).year==year&&myHolidayData.get(i).month==month&&myHolidayData.get(i).day==day) {
				if(myHolidayData.get(i).isHoliday) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return false;
	}

	public boolean getWCondition(int year,int month,int day) //获取指定日期的天气情况
	{
		for(int i=0;i<myWeatherData.size();i++)
		{
			if(myWeatherData.get(i).year==year&&myWeatherData.get(i).month==month&&myWeatherData.get(i).day==day) {
				if(myWeatherData.get(i).wCondition) {
					return true;
				}
				else {
					return false;
				}
			}
		}
		return true;
	}

	public void writetofile() throws IOException {
		File file = new File(srcdir+"output.txt");
        if(!file.exists())
        {
            file.createNewFile();
        }
        FileWriter fileWriter = new FileWriter(srcdir+"output.txt");
        String content="";
        for(int i=0;i<myRleave.size();i++) {
        	content+=myRleave.get(i).index+":"+myRleave.get(i).getAttrByIndex(myRleave.get(i).index)+";";
        }
        content+="\r\n";
        for(int i=0;i<myRback.size();i++) {
        	content+=myRback.get(i).index+":"+myRback.get(i).getAttrByIndex(myRback.get(i).index)+";";
        }
        BufferedWriter bufferWritter = new BufferedWriter(fileWriter);
        bufferWritter.write(content);
        bufferWritter.close();
	}
}



