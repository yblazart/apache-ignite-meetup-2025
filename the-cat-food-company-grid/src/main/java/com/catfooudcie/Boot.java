package com.catfooudcie;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;

import java.util.Collection;

@ApplicationScoped
@JBossLog
public class Boot {

    @Inject
    Ignite ignite;

    @Transactional
    public void init(@Observes StartupEvent event) {
        Collection<ClusterNode> nodes = ignite.cluster().nodes();
        log.info("Ignite cluster of : " + nodes.size() + " nodes : ");
        nodes.forEach(this::logInfos);
    }

    private void logInfos(ClusterNode clusterNode) {
        log.info("Node ID: " + clusterNode.id() + " client=" + clusterNode.isClient());
    }
}
