package de.muenchen.isi.infrastructure.repository.stammdaten;

import de.muenchen.isi.infrastructure.entity.stammdaten.MimeTypeInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Diese Klasse hält für einen MIME-Type die möglichen Dateiendungen vor.
 * <p>
 * Die Basis dieser Zuordnung bildet die Datei "mime.types" in den Ressourcen.
 * <p>
 * Die jeweils aktuellste Datei kann unter folgender URL bezogen werden:
 * http://svn.apache.org/viewvc/httpd/httpd/trunk/docs/conf/mime.types?view=markup
 */
@Repository
@Slf4j
public class MimeTypeRepository {

    private static final String REGEX_MULTIPLE_WHITESPACE = "\\s+";

    private final Map<String, MimeTypeInformation> mimeTypeFileExtensionsMap;

    private final ResourceLoader resourceLoader;

    public MimeTypeRepository(final ResourceLoader resourceLoader) throws IOException {
        this.mimeTypeFileExtensionsMap = new HashMap<>();
        this.resourceLoader = resourceLoader;
        this.init();
    }

    public Optional<MimeTypeInformation> findByMimeType(final String mimeType) {
        return Optional.ofNullable(this.mimeTypeFileExtensionsMap.get(mimeType));
    }

    public List<MimeTypeInformation> findAllByMimeTypes(final List<String> mimeTypes) {
        return mimeTypes.stream()
                .map(this::findByMimeType)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Die in den Ressourcen abgelegte Datei repräsentiert die gängigsten MIME-Types und deren Dateiendungen.
     * <p>
     * http://svn.apache.org/viewvc/httpd/httpd/trunk/docs/conf/mime.types?view=markup
     * <p>
     * Diese Methode erstellt je Zeile in der Datei ein {@link MimeTypeInformation} und legt diese im Repository ab.
     */
    protected void init() throws IOException {
        final var mimeInputStream = this.resourceLoader.getResource("classpath:mime.types").getInputStream();
        IOUtils.readLines(new InputStreamReader(mimeInputStream)).stream()
                .filter(StringUtils::isNotBlank)
                .filter(line -> !StringUtils.startsWith("#", line))
                .map(this::parseLineToMimeTypeInfos)
                .forEach(mimeTypeInformation ->
                        this.mimeTypeFileExtensionsMap.put(
                                mimeTypeInformation.getMimeType(),
                                mimeTypeInformation
                        )
                );
        log.info("MIME-Type-Infos sind geladen.");
    }

    /**
     * Erstellt aus der verarbeitbaren Zeile ein {@link MimeTypeInformation}.
     * <p>
     * Eine Zeile startet mit dem MIME-Type und wird ergänzt durch die möglichen Dateiendungen ohne führenden Punkt.
     * Die einzelnen Zeilenabschnitte sind durch Tabs oder Leerzeichen getrennt.
     * <p>
     * Beispiele:
     * image/jpeg					jpeg jpg jpe
     * image/svg+xml					svg svgz
     * application/pdf					pdf
     *
     * @param line als verarbeitbare Zeile der mime.type-Datei in den Resourcen. Darf nicht null sein.
     * @return die {@link MimeTypeInformation} der Zeile.
     * @throws NullPointerException falls der Paramter den Wert null besitzt.
     */
    protected MimeTypeInformation parseLineToMimeTypeInfos(final String line) throws NullPointerException {
        final var lineElements = List.of(line.split(REGEX_MULTIPLE_WHITESPACE));
        final var fileExtensionsWithPointPrefix = lineElements.subList(1, lineElements.size()).stream()
                .map(fileExtensionWithoutPointPrefix -> "." + fileExtensionWithoutPointPrefix)
                .collect(Collectors.toList());
        final var mimeTypeInfos = new MimeTypeInformation();
        mimeTypeInfos.setMimeType(lineElements.get(0));
        mimeTypeInfos.setFileExtensions(fileExtensionsWithPointPrefix);
        return mimeTypeInfos;
    }

}
