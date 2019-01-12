package com.asynclife.batch.processor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.StringUtils;

import com.asynclife.batch.domain.Account;
import com.asynclife.batch.domain.IlleageAccountrException;


/**
 * {@link ItemProcessor} which validates data.
 * @author Tobias Flohre
 */
public class ValidationProcessor implements ItemProcessor<Account,Account> {

	private static final Log log = LogFactory.getLog(ValidationProcessor.class);
	
	public Account process(Account account) throws Exception {
		log.info(account);
		if (StringUtils.isEmpty(account.getElecAccountNo())){
			throw new IlleageAccountrException("ElecAccountNo is null or empty!");
		}
		return account;
	}

}
