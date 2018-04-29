package foil;

public class Main {

	public static void main(String[] args) {
		Thread th=new Thread() {
			public void run() {
				while(true) {
					new Start();
					try {
						Thread.sleep(1000*60*60*24);//一天运行一次
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
	}
}
