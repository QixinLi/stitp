package foil;

public class Main {

	public static void main(String[] args) {
		Thread th=new Thread() {
			public void run() {
				while(true) {
					new Start();
					try {
						Thread.sleep(1000*60*60*24);//һ������һ��
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
