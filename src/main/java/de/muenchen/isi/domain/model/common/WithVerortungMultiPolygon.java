package de.muenchen.isi.domain.model.common;

/**
 * Für Models, deren Verortung einen eigenen Umgriff hat (Bauvorhaben und Abfragen).
 */
public interface WithVerortungMultiPolygon {
    VerortungMultiPolygonModel getVerortung();
}
