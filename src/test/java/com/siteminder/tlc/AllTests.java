package com.siteminder.tlc;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.siteminder.tlc.services.MailGunServiceTest;
import com.siteminder.tlc.services.SendGridServiceTest;
import com.siteminder.tlc.validators.ContentValidationRuleTest;
import com.siteminder.tlc.validators.PrimaryRecipientsRuleTest;
import com.siteminder.tlc.validators.RecipientsValidationRuleTest;
import com.siteminder.tlc.validators.SenderValidationRuleTest;
import com.siteminder.tlc.validators.SubjectValidationRuleTest;

@RunWith(Suite.class)
@SuiteClasses({ 
	MailGunServiceTest.class, 
	SendGridServiceTest.class, 
	PrimaryRecipientsRuleTest.class,
	SenderValidationRuleTest.class,
	SubjectValidationRuleTest.class,
	ContentValidationRuleTest.class,
	RecipientsValidationRuleTest.class,
	StreamLambdaHandlerTest.class})
public class AllTests {

}
