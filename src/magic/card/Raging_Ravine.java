package magic.card;

import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPowerToughness;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.action.MagicAddTurnTriggerAction;
import magic.model.action.MagicBecomesCreatureAction;
import magic.model.action.MagicChangeCountersAction;
import magic.model.condition.MagicArtificialCondition;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicWhenAttacksTrigger;

import java.util.EnumSet;

public class Raging_Ravine {
                        
    private static final MagicWhenAttacksTrigger CT = new MagicWhenAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent creature) {
            return (permanent == creature && permanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a +1/+1 counter on SN."):
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    };

    private static final MagicStatic PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            pt.set(3,3);
        }
    };

    private static final MagicStatic ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
        @Override
        public void modSubTypeFlags(
                final MagicPermanent permanent,
                final EnumSet<MagicSubType> flags) {
            flags.add(MagicSubType.Elemental);
        }
        @Override
        public int getTypeFlags(final MagicPermanent permanent,final int flags) {
            return flags|MagicType.Creature.getMask();
        }
    };

    private static final MagicStatic C = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
        @Override
        public int getColorFlags(final MagicPermanent permanent,final int flags) {
            return MagicColor.Red.getMask()|MagicColor.Green.getMask();
        }        
    };

    public static final MagicPermanentActivation A1 = new MagicPermanentActivation(
            new MagicCondition[]{new MagicArtificialCondition(
                    MagicManaCost.TWO_RED_GREEN.getCondition(),
                    MagicManaCost.ONE_RED_RED_GREEN_GREEN.getCondition())},
            new MagicActivationHints(MagicTiming.Animate),
            "Animate") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{
                new MagicPayManaCostEvent(source,source.getController(),
                MagicManaCost.TWO_RED_GREEN)};
        }

        @Override
        public MagicEvent getPermanentEvent(
                final MagicPermanent source,
                final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Until end of turn, SN becomes a 3/3 red and green Elemental creature with " + 
                "\"Whenever this creature attacks, put a +1/+1 counter on it.\" It's still a land.");
        }

        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            final MagicPermanent permanent=event.getPermanent();
            game.doAction(new MagicBecomesCreatureAction(permanent,PT,ST,C));
            game.doAction(new MagicAddTurnTriggerAction(permanent,CT));
        }
    };
}
