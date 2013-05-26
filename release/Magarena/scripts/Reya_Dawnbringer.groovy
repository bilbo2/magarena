[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD
                    ),
                    new MagicGraveyardTargetPicker(true),
                    this,
                    "PN may\$ return target creature card\$ from " +
                    "his or her graveyard to the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetCard(game,new MagicCardAction() {
                    public void doAction(final MagicCard card) {
                        game.doAction(new MagicReanimateAction(event.getPlayer(),card,MagicPlayCardAction.NONE));
                    }
                });
            }
        }
    }
]
