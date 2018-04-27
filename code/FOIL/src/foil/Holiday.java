package foil;

public class Holiday {
	public int year;
	public int month;
	public int day;
	public boolean isSat=false;
	public boolean isSun=false;
	public boolean isHoliday=false;
	public Holiday(int year,int month,int day,int myindex) {
		this.year=year;
		this.month=month;
		this.day=day;
		if(myindex==11) {
			isSat=true;
		}
		else if(myindex==12) {
			isSun=true;
		}
		else if(myindex==2) {
			isHoliday=true;
		}
	}
}
