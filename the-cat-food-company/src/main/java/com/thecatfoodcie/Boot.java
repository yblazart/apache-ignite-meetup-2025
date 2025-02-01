package com.thecatfoodcie;

import com.thecatfoodcie.model.CatFood;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;

import java.math.BigDecimal;
import java.util.Collection;

@ApplicationScoped
@JBossLog
public class Boot {

    @Inject
    Ignite ignite;

    @Transactional
    public void init(@Observes StartupEvent event) {
        // Add some data to the database
        for (int i = 1; i <= 8; i++) {
            addCatFood("0" + i);
        }
        log.info("CaFood count: " + CatFood.count());
        Collection<ClusterNode> nodes = ignite.cluster().nodes();
        log.info("Ignite client of cluster of : " + nodes.size()+" nodes : ");
        nodes.forEach(this::logInfos);
    }

    private void logInfos(ClusterNode clusterNode) {
        log.info("Node ID: " + clusterNode.id()+ " client=" + clusterNode.isClient());
    }

    private void addCatFood(String number) {
        log.info("Adding Cat Food " + number);
        if (CatFood.find("code", "Food" + number).count() == 0) {
            log.info("Done for 1 !");
            CatFood.builder()
                    .code("Food" + number)
                    .name("Cat Food " + number)
                    .weight(10)
                    .price(BigDecimal.valueOf(10))
                    .imagePath("paquet" + number + ".webp")
                    .description("Delicious cat food")
                    .build()
                    .persist();
        }
    }

}
