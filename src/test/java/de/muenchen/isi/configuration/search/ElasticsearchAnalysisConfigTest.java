package de.muenchen.isi.configuration.search;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ElasticsearchAnalysisConfigTest {

    @Test
    @DisplayName("application.yml prüfen ob alle Registrierungen enthalten sind")
    public void applicationYmlRegistersConfigurer() throws Exception {
        Path p = Path.of("src/main/resources/application.yml");
        String content = Files.readString(p);
        assertTrue(
            content.contains("ElasticsearchKeepAliveConfig"),
            "ElasticsearchKeepAliveConfig muss in der application.yml registriert werden"
        );
        assertTrue(
            content.contains("ElasticsearchAnalysisConfig"),
            "ElasticsearchAnalysisConfig muss in der application.yml registriert werden"
        );
    }

    @Test
    @DisplayName("ElasticsearchAnalysisConfig definiert underscore Tokenizer und Analyzer")
    public void configClassDefinesTokenizerAndAnalyzer() throws Exception {
        Path p = Path.of("src/main/java/de/muenchen/isi/configuration/search/ElasticsearchAnalysisConfig.java");
        String content = Files.readString(p);
        assertTrue(
            content.contains("underscore_tokenizer"),
            "In ElasticsearchAnalysisConfig muss underscore_tokenizer definiert sein"
        );
        assertTrue(
            content.contains("isi_text_analyzer"),
            "In ElasticsearchAnalysisConfig muss isi_text_analyzer definiert sein"
        );
        assertTrue(content.contains("pattern"), "ElasticsearchAnalysisConfig muss einen pattern tokenizer verwenden");
    }

    @Test
    @DisplayName("Entities, isi_text_analyzer bei FullTextField Annotationen verwenden")
    public void entitiesUseAnalyzer() throws Exception {
        // files that we changed
        Path[] files = new Path[] {
            Path.of("src/main/java/de/muenchen/isi/infrastructure/entity/Abfrage.java"),
            Path.of("src/main/java/de/muenchen/isi/infrastructure/entity/Bauvorhaben.java"),
            Path.of(
                "src/main/java/de/muenchen/isi/infrastructure/entity/infrastruktureinrichtung/Infrastruktureinrichtung.java"
            ),
        };

        for (Path file : files) {
            String content = Files.readString(file);
            assertTrue(
                content.contains("@FullTextField(analyzer = \"isi_text_analyzer\""),
                () -> file + " muss @FullTextField(analyzer = \"isi_text_analyzer\") verwenden"
            );
        }
    }
}
