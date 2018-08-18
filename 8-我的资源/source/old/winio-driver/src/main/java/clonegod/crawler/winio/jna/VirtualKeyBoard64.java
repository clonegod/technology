package clonegod.crawler.winio.jna;

import com.sun.jna.NativeLibrary;

import clonegod.crawler.util.CommonUtil;


public class VirtualKeyBoard64 {
	
	static {
		NativeLibrary.addSearchPath("WinIo64", VirtualKeyBoard64.class.getResource("/").getPath());
	}
	
	public static final WinIo64 winIo64 = WinIo64.INSTANCE;

	static {
		if (!WinIo64.INSTANCE.InitializeWinIo()) {
			// initialize failure reason: you need copy WinIo64.dll and WinIo64.sys to jdk64bit/bin directory!!!
			System.err.println("Cannot Initialize the WinIO");
			System.exit(1);
		} else {
			System.out.println("Successfully Initialize WinIo");
		}
	}
	
	public static void KeyDown(int key) throws Exception {
		User32Util.KBCWait4IBE64();
		winIo64.SetPortVal(WinIo64.CONTROL_PORT, 0xd2, 1);
		User32Util.KBCWait4IBE64();
		winIo64.SetPortVal(WinIo64.DATA_PORT, key, 1);
	}

	public static void KeyUp(int key) throws Exception {
		User32Util.KBCWait4IBE64();
		winIo64.SetPortVal(WinIo64.CONTROL_PORT, 0xd2, 1);
		User32Util.KBCWait4IBE64();
		winIo64.SetPortVal(WinIo64.DATA_PORT, (key | 0x80), 1);
	}

	public static void KeyPress(String text) throws Exception {
		for(char c : text.toCharArray()) {
			CommonUtil.sleep(500);
			KeyPress(VKMapping.toVK("" + c));
		}
	}
	
	public static void KeyPress(char key) throws Exception {
		KeyPress(VKMapping.toVK("" + key));
	}

	public static void KeyPress(int vk) throws Exception {
		int scan = User32.INSTANCE.MapVirtualKey(vk, 0);
		KeyDown(scan);
		CommonUtil.sleep(100); // 需要间隔一段时间，不能太快
		KeyUp(scan);
	}

	public static void main(String[] args) throws Exception {
		// 如果不添加路径，默认在java.exe同层目录添加winIo.dll和WinIo32.sys
		String s = "helloworld";
		for (int i = 0; i < s.length(); i++) {
			CommonUtil.sleep(500); // 每次输入停顿一下 
			KeyPress(s.charAt(i));
		}
	}

}
