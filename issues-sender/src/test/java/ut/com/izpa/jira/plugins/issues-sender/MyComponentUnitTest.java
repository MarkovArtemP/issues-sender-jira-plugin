package ut.com.izpa.jira.plugins.issues-sender;

import org.junit.Test;
import com.izpa.jira.plugins.issues-sender.api.MyPluginComponent;
import com.izpa.jira.plugins.issues-sender.impl.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}