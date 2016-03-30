package org.mandfer.tools.email;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author marcandreuf
 */
public class JMailerTest {

    private JMailer jmailer = new JMailer();

    @Test
    @Ignore
    public void shouldSendSimpleEmailTest() {
        jmailer.send("marcandreuf@gmail.com",
                "Test email from jmailer",
                "Discard this is a test.");
    }

    //TODO. Test Send test with mocked dependencies.

    @Test
    @Ignore
    public void dslSendMailTest() {
        //send().from("marcandreuf@gmail.com")
        //      .to("marcandreuf@gmail.com")
        //      .withSubject("This is a test") //.withNoSubject()
        //      .the().email("This is a emal boy test.");
        // .and().with().attachments()...
    }

}
