/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2022
 */
package de.muenchen.isi.configuration.nfcconverter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import org.apache.commons.lang3.NotImplementedException;

/**
 * ServletInputStream, der von einem Puffer ließt.
 */
public class NfcServletInputStream extends ServletInputStream {

    private final ByteArrayInputStream buffer;

    public NfcServletInputStream(final ByteArrayInputStream buffer) {
        this.buffer = buffer;
    }

    @Override
    public int read() throws IOException {
        return buffer.read();
    }

    @Override
    public boolean isFinished() {
        return buffer.available() == 0;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(final ReadListener listener) {
        throw new NotImplementedException("Not implemented");
    }
}
