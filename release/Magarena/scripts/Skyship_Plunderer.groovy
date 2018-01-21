[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicDamage dealtDamage) {
            return new MagicEvent(
                permanent,
                new MagicTargetChoice("target permanent or player"),
                this,
                "For each kind of counter on target permanent or player,\$ " +
                "give that permanent or player another counter of that kind."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                for (final MagicCounterType counterType : MagicCounterType.values()) {
                    if (it.hasCounters(counterType)) {
                        game.doAction(new ChangeCountersAction(it, counterType, 1));
                    }
                }
            });
        }
    }
]

