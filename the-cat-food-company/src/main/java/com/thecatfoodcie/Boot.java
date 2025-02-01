package com.thecatfoodcie;

import com.thecatfoodcie.model.CatFood;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import lombok.extern.jbosslog.JBossLog;

import java.math.BigDecimal;

@ApplicationScoped
@JBossLog
public class Boot {

    @Transactional
    public void init(@Observes StartupEvent event) {
        // Add some data to the database
        for (int i = 1; i <= 8; i++) {
            extracted("0" + i);
        }
        log.info("CaFood count: " + CatFood.count());
    }

    private void extracted(String number) {
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
