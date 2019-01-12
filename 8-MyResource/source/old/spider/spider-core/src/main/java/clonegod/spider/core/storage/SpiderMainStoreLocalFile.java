package clonegod.spider.core.storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.io.FileUtils;

import clonegod.spider.core.engine.SpiderMain;

public class SpiderMainStoreLocalFile implements SpiderMainContextStore {

	private static final String OBJECT_STORE_DIR = "/var/logs/object";
	
	@Override
	public void store(SpiderMain spiderMain) {
		String sessionId = spiderMain.getPageContext().getSessionId();
		File datafile = new File(OBJECT_STORE_DIR, sessionId);
		datafile.getParentFile().mkdirs();
		try {
			saveToFile(spiderMain, datafile);
		} catch (Exception e) {
			throw new RuntimeException("save object to file failed, sessionId="+sessionId, e);
		}
	}
	
	private void saveToFile(Object object, File targetFile) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.close();
		FileUtils.writeByteArrayToFile(targetFile, baos.toByteArray());
	}

	@Override
	public SpiderMain reload(String sessionId) {
		File datafile = new File(OBJECT_STORE_DIR, sessionId);
		Object object;
		try {
			FileInputStream fis = new FileInputStream(datafile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			object = ois.readObject();
			ois.close();
		} catch (Exception e) {
			throw new RuntimeException("reload object from file failed, sessionId="+sessionId, e);
		}
		return (SpiderMain) object;
	}

}
