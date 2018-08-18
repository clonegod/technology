package clonegod.crawler.winio.jnative;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MyMessageSender {

	private static final String targetWindowName = "Internet Explorer";

	public static void sendMessage(char[] chars, int delayMin, int delayMax) {
		if(delayMin >= delayMax) {
			throw new RuntimeException("delayMin must litter than delayMax");
		}
		while(true) {
			try {
				String currentWindowName = User32.GetWindowText(User32.GetForegroundWindow());
				
				System.out.println(currentWindowName + "\t" + targetWindowName);
				
				// 向当前窗口发送消息
				
				if (User32.GetWindowText(User32.GetForegroundWindow()).contains(targetWindowName)) {
					for (char c : chars) {
						System.out.println(c);
						VirtualKeyBoard.KeyPress(VKMapping.toScanCode(String.valueOf(c)));
						log(c);
						Thread.sleep(delayMax + new Random().nextInt(delayMax - delayMin));
					}
					break;
				}
				
				TimeUnit.SECONDS.sleep(1);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void log(char c) {
		System.out.println(
				new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(new Date()) + "\tsend key:" + c);

	}

}
