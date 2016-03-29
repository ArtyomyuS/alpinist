package com.adswizz.profiler;

import com.ea.agentloader.AgentLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author artiom.darie.
 */
public class AlpinistAgentTest {

    @Test
    public void agentmain() {
        AgentLoader.loadAgentClass(AlpinistAgent.class.getName(), "");
    }

}