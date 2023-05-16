package de.muenchen.isi.domain.service;

import de.muenchen.isi.infrastructure.repository.stammdaten.IdealtypischeBaurateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BaurateServiceTest {

    @Mock
    private IdealtypischeBaurateRepository idealtypischeBaurateRepository;

    private BaurateService baurateService;

    @BeforeEach
    public void beforeEach() {
        this.baurateService = new BaurateService(this.idealtypischeBaurateRepository);
        Mockito.reset(this.idealtypischeBaurateRepository);
    }

    @Test
    void determineBauraten() {}
}
