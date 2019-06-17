package de.diegrafen.exmatrikulatortd.model.tower;

/**
 *
 * Die verschiedenen Aura-Typen, die ein Turm haben kann. Eine Aura kann entweder Gegner verlangsamen (@code{SLOW_AURA}),
 * die Angriffsgeschindigkeit befreundeter Türme erhöhen (@code{HASTE_AURA}) oder die Rüstung von Gegnern reduzieren
 * (@code{CORRUPTION_AURA}).
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 22:01
 */
public enum Aura {
    SLOW_AURA, HASTE_AURA, CORRUPTION_AURA
}
