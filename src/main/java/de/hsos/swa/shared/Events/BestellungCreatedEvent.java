package de.hsos.swa.shared.Events;

public class BestellungCreatedEvent {
    private final Long bestellungId;
    private final Long kundeId;

    public BestellungCreatedEvent(Long bestellungId, Long kundeId) {
        this.bestellungId = bestellungId;
        this.kundeId = kundeId;
    }

    public Long getBestellungId() {
        return bestellungId;
    }

    public Long getKundeId() {
        return kundeId;
    }

    @Override
    public String toString() {
        return "BestellungCreatedEvent{" +
                "bestellungId=" + bestellungId +
                ", kundeId=" + kundeId +
                '}';
    }
}
