package de.diegrafen.exmatrikulatortd.model.tower;

/**
 *
 * Die verschiedenen Angriffsypen, die ein Turm haben kann. Ein Turm kann entweder einen normalen Schaden(@code{NORMAL}),
 * Stichschaden(@code{PIERCING}), Explosivschaden(@code{EXPLOSIVE}), Frostschaden(@code{FROST}) oder unheiligen Schaden
 * (@code{UNHOLY})verursachen.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 22:05
 */
public enum AttackType {
    NORMAL, PIERCING, EXPLOSIVE, FROST, UNHOLY;
}
