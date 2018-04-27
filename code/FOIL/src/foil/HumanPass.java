package foil;

public class HumanPass {
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public int second;
	public HumanPass(int year,int month,int day,int hour,int minute,int second) {
		this.year=year;
		this.month=month;
		this.day=day;
		this.hour=hour;
		this.minute=minute;
		this.second=second;
	}
	public String getDate() {
		return year+"-"+month+"-"+day;
	}
}
