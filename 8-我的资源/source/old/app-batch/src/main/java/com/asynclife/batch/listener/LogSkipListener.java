package com.asynclife.batch.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.SkipListener;

import com.asynclife.batch.domain.Account;

/**
 * @author Tobias Flohre
 */
public class LogSkipListener implements SkipListener<Account, Account> {

	private static final Log LOGGER = LogFactory.getLog(LogSkipListener.class);

	public void onSkipInProcess(Account partner, Throwable throwable) {
		LOGGER.info("Partner was skipped in process: "+partner+".",throwable);
	}

	public void onSkipInRead(Throwable arg0) {
	}

	public void onSkipInWrite(Account arg0, Throwable arg1) {
	}

}
