package foil;

public class RuleSet {
	public int index;
	public boolean weatherCondition;//-5 天气情况
	public boolean isWeekday;//-4 工作日
	public boolean isSat;//-3 周六
	public boolean isSun;//-2 周日
	public boolean isHoliday;//-1 法定节假日
	public boolean t0_1=false;
	public boolean t1_2=false;
	public boolean t2_3=false;
	public boolean t3_4=false;
	public boolean t4_5=false;
	public boolean t5_6=false;
	public boolean t6_7=false;
	public boolean t7_8=false;
	public boolean t8_9=false;
	public boolean t9_10=false;
	public boolean t10_11=false;
	public boolean t11_12=false;
	public boolean t12_13=false;
	public boolean t13_14=false;
	public boolean t14_15=false;
	public boolean t15_16=false;
	public boolean t16_17=false;
	public boolean t17_18=false;
	public boolean t18_19=false;
	public boolean t19_20=false;
	public boolean t20_21=false;
	public boolean t21_22=false;
	public boolean t22_23=false;
	public boolean t23_24=false;
	
	public RuleSet(boolean weatherCondition,boolean isSat,boolean isSun,boolean isHoliday,int timeindex) {
		this.weatherCondition=weatherCondition;
		this.isSat=isSat;
		this.isSun=isSun;
		this.isHoliday=isHoliday;
		if(!isSat&&!isSun&&!isHoliday) {
			this.isWeekday=true;
		}
		switch(timeindex) {
		case -1:
			break;
		case 0:
			t0_1=true;
			break;
		case 1:
			t1_2=true;
			break;
		case 2:
			t2_3=true;
			break;
		case 3:
			t3_4=true;
			break;
		case 4:
			t4_5=true;
			break;
		case 5:
			t5_6=true;
			break;
		case 6:
			t6_7=true;
			break;
		case 7:
			t7_8=true;
			break;
		case 8:
			t8_9=true;
			break;
		case 9:
			t9_10=true;
			break;
		case 10:
			t10_11=true;
			break;
		case 11:
			t11_12=true;
			break;
		case 12:
			t12_13=true;
			break;
		case 13:
			t13_14=true;
			break;
		case 14:
			t14_15=true;
			break;
		case 15:
			t15_16=true;
			break;
		case 16:
			t16_17=true;
			break;
		case 17:
			t17_18=true;
			break;
		case 18:
			t18_19=true;
			break;
		case 19:
			t19_20=true;
			break;
		case 20:
			t20_21=true;
			break;
		case 21:
			t21_22=true;
			break;
		case 22:
			t22_23=true;
			break;
		case 23:
			t23_24=true;
			break;
		default:
			break;
		}
	}

	public RuleSet(int index,boolean setbool) {
		this.index=index;
		switch(index) {
		case -5:
			weatherCondition=setbool;
			break;
		case -4:
			isWeekday=setbool;
			break;
		case -3:
			isSat=setbool;
			break;
		case -2:
			isSun=setbool;
			break;
		case -1:
			isHoliday=setbool;
			break;
		case 0:
			t0_1=setbool;
			break;
		case 1:
			t1_2=setbool;
			break;
		case 2:
			t2_3=setbool;
			break;
		case 3:
			t3_4=setbool;
			break;
		case 4:
			t4_5=setbool;
			break;
		case 5:
			t5_6=setbool;
			break;
		case 6:
			t6_7=setbool;
			break;
		case 7:
			t7_8=setbool;
			break;
		case 8:
			t8_9=setbool;
			break;
		case 9:
			t9_10=setbool;
			break;
		case 10:
			t10_11=setbool;
			break;
		case 11:
			t11_12=setbool;
			break;
		case 12:
			t12_13=setbool;
			break;
		case 13:
			t13_14=setbool;
			break;
		case 14:
			t14_15=setbool;
			break;
		case 15:
			t15_16=setbool;
			break;
		case 16:
			t16_17=setbool;
			break;
		case 17:
			t17_18=setbool;
			break;
		case 18:
			t18_19=setbool;
			break;
		case 19:
			t19_20=setbool;
			break;
		case 20:
			t20_21=setbool;
			break;
		case 21:
			t21_22=setbool;
			break;
		case 22:
			t22_23=setbool;
			break;
		case 23:
			t23_24=setbool;
			break;
		}
	}
	
	public boolean getAttrByIndex(int index) {
		boolean gotAttr=false;
		switch(index) {
		case -5:
			gotAttr=weatherCondition;
			break;
		case -4:
			gotAttr=isWeekday;
			break;
		case -3:
			gotAttr=isSat;
			break;
		case -2:
			gotAttr=isSun;
			break;
		case -1:
			gotAttr=isHoliday;
			break;
		case 0:
			gotAttr=t0_1;
			break;
		case 1:
			gotAttr=t1_2;
			break;
		case 2:
			gotAttr=t2_3;
			break;
		case 3:
			gotAttr=t3_4;
			break;
		case 4:
			gotAttr=t4_5;
			break;
		case 5:
			gotAttr=t5_6;
			break;
		case 6:
			gotAttr=t6_7;
			break;
		case 7:
			gotAttr=t7_8;
			break;
		case 8:
			gotAttr=t8_9;
			break;
		case 9:
			gotAttr=t9_10;
			break;
		case 10:
			gotAttr=t10_11;
			break;
		case 11:
			gotAttr=t11_12;
			break;
		case 12:
			gotAttr=t12_13;
			break;
		case 13:
			gotAttr=t13_14;
			break;
		case 14:
			gotAttr=t14_15;
			break;
		case 15:
			gotAttr=t15_16;
			break;
		case 16:
			gotAttr=t16_17;
			break;
		case 17:
			gotAttr=t17_18;
			break;
		case 18:
			gotAttr=t18_19;
			break;
		case 19:
			gotAttr=t19_20;
			break;
		case 20:
			gotAttr=t20_21;
			break;
		case 21:
			gotAttr=t21_22;
			break;
		case 22:
			gotAttr=t22_23;
			break;
		case 23:
			gotAttr=t23_24;
			break;
		default:
			break;
		}
		return gotAttr;
	}
	
}
