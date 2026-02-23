package gate;

import java.util.List;
import java.util.Optional;

public class GateManager {
    private final List<EntryGate> entryGates;
    private final List<ExitGate> exitGates;

    public GateManager(List<EntryGate> entryGates, List<ExitGate> exitGates) {
        this.entryGates = entryGates;
        this.exitGates = exitGates;
    }

    public EntryGate getEntryGates(String gateId) {
        Optional<EntryGate> gate = entryGates.stream()
                .filter(g -> g.getGateId().equals(gateId))
                .findAny();

        return gate.orElse(null);
    }

    public ExitGate getExitGates(String gateId) {
        Optional<ExitGate> gate = exitGates.stream()
                .filter(g  -> g.getGateId().equals(gateId))
                .findAny();

        return gate.orElse(null);
    }
}
