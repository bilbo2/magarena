package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicDrawAction;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPayManaCostEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;

public class Sphinx_of_Magosi {
    public static final MagicPermanentActivation A = new MagicPermanentActivation(
            new MagicCondition[]{MagicManaCost.TWO_BLUE.getCondition()},
            new MagicActivationHints(MagicTiming.Draw),
            "Draw") {

        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent source) {
            return new MagicEvent[]{new MagicPayManaCostEvent(source,source.getController(),MagicManaCost.TWO_BLUE)};
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                    source,
                    this,
                    "Draw a card, then put a +1/+1 counter on SN.");
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1,true));
        }
    };
}
