package com.adswizz.profiler;

import com.ea.agentloader.AgentLoader;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author artiom.darie.
 */
@Ignore
public class AlpinistAgentTest {

    @Test
    public void testAgentmain() {
        AgentLoader.loadAgentClass(AlpinistAgent.class.getName(), "arguments");
    }


    @Test
    public void testClassName() {

        System.out.println(JVMUtils.getClassName());
        System.out.println(JVMUtils.getPackageName());

    }

}