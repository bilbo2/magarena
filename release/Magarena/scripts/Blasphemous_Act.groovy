[
    new MagicHandCastActivation(
        [MagicCondition.CARD_CONDITION],
        new MagicActivationHints(MagicTiming.Main, true),
        "Cast"
    ) {
        @Override
        public void change(final MagicCardDefinition cdef) {
            cdef.setHandAct(this);
        }

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            final int n = source.getGame().getNrOfPermanents(CREATURE);
            return [
                new MagicPayManaCostEvent(
                    source,
                    source.getCost().reduce(n)
                )
            ];
        }
    }
]
