package clonegod.crawler.winio.jna;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class User32Util {

	/**
	 * 32bit keyboard
	 * @throws Exception
	 */
	public static void KBCWait4IBE() throws Exception {
		int val;
		do {
			Pointer p = new Memory(8);
			if (!WinIo32.INSTANCE.GetPortVal(WinIo32.CONTROL_PORT, p, 1)) {
				System.err.println("Cannot get the Port");
			}
			val = p.getInt(0);

		} while ((0x2 & val) > 0);
	}
	
	/**
	 * 64bit keyboard
	 * @throws Exception
	 */
	public static void KBCWait4IBE64() throws Exception {
		int val;
		do {
			Pointer p = new Memory(8);
			if (!WinIo64.INSTANCE.GetPortVal(WinIo64.CONTROL_PORT, p, 1)) {
				System.err.println("Cannot get the Port");
			}
			val = p.getInt(0);

		} while ((0x2 & val) > 0);
	}
}
