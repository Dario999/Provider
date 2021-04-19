package uk.singular.dfs.provider.sandbox.services.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.components.AuditMessageQueue;
import uk.singular.dfs.provider.sandbox.enums.EventStatus;
import uk.singular.dfs.provider.sandbox.enums.OutcomeStatus;
import uk.singular.dfs.provider.sandbox.enums.Settlement;
import uk.singular.dfs.provider.sandbox.exceptions.InvalidMessageException;
import uk.singular.dfs.provider.sandbox.jms.producer.LogMessageSender;
import uk.singular.dfs.provider.sandbox.model.market.Market;
import uk.singular.dfs.provider.sandbox.model.market.Outcome;
import uk.singular.dfs.provider.sandbox.model.market.Payload;
import uk.singular.dfs.provider.sandbox.model.messages.Message;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.repository.MarketRepository;
import uk.singular.dfs.provider.sandbox.repository.OutcomeRepository;
import uk.singular.dfs.provider.sandbox.services.EventService;
import uk.singular.dfs.provider.sandbox.services.MarketService;
import uk.singular.dfs.provider.sandbox.services.MessageService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private EventService eventService;
    @Autowired
    private MarketService marketService;
    @Autowired
    private MarketRepository marketRepository;
    @Autowired
    private AuditMessageQueue auditMessageQueue;
    @Autowired
    private LogMessageSender logMessageSender;
    @Autowired
    private OutcomeRepository outcomeRepository;

    private static final Logger LOG = LoggerFactory.getLogger(MessageServiceImpl.class);
    private static Map<Integer,ReentrantLock> eventLocks = new HashMap<>();

    @Override
    public void sendMessage(Message message) throws InvalidMessageException {
        if(message.getType() == null){
            throw new InvalidMessageException("No message type provided");
        }
        if(message.getEventId() == null){
            throw new InvalidMessageException("No event id provided");
        }
        if(message.getPayload() == null){
            throw new InvalidMessageException("No payload provided");
        }
        Long timeStamp = System.currentTimeMillis();

        ReentrantLock eventLock = eventLocks.get(message.getEventId());
        if(eventLock == null){
            eventLock = new ReentrantLock();
            eventLocks.put(message.getEventId(),eventLock);
            eventLock.lock();
        }else{
            eventLock.lock();
        }

        LOG.info("Log acquired in: " + (System.currentTimeMillis()-timeStamp) + "ms. Event " + message.getEventId() + " locked.Timer started 10sec." );
        // for testing
        try {
            TimeUnit.SECONDS.sleep(10);
        }catch (InterruptedException e){
            LOG.warn(e.getMessage());
        }

        auditMessageQueue.add(message);

        try {
            switch (message.getType()) {
                case FIXTURE_CHANGE:
                    handleFixtureChange(message, eventLock);
                    break;
                case ODDS_CHANGE:
                    handleOddsChange(message, eventLock);
                    break;
                case BET_STOP:
                    handleBetStop(message, eventLock);
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            LOG.warn(e.getMessage());
        }finally {
            eventLock.unlock();
            LOG.info("Event " + message.getEventId() + " unlocked.Timer ended 10sec.");
        }

    }

    public void handleFixtureChange(Message message,ReentrantLock lock) throws InvalidMessageException {
        Event event = eventService.findEventById(message.getEventId());
        Event newEvent = message.getPayload().getEvent();
        if (event != null && newEvent != null)
        {
            if (eventsEqualCheck(event,newEvent))                      // no changes on the new event
            {
                LOG.info("Event {} has already been updated!", event.getId());
                logMessageSender.sendMessage(String.format("Event %s has already been updated!", event.getId()));
                return;
            }
            if (newEvent.getStatus() != null && !event.getStatus().equals(newEvent.getStatus())) {
                statusChangeLog(event.getId(), event.getStatus(), newEvent.getStatus());                  // status changed
                event.setStatus(newEvent.getStatus());
            }
            if (newEvent.getStartDate() != null && !event.getStartDate().equals(newEvent.getStartDate())) {
                dateChangeLog(event.getId(), event.getStartDate(), newEvent.getStartDate());      // date changed
                event.setStartDate(newEvent.getStartDate());
            }
            eventService.update(event);
        }
        if(event == null) {
            LOG.error("Event {} does not exist! Skipping {} message!", message.getEventId(), message.getType());
            logMessageSender.sendMessage(String.format("Event %d does not exist! Skipping %s message!",
                                                    message.getEventId(),message.getType().toString()));
            throw new InvalidMessageException("Event with id " + message.getEventId() + " not exists");
        }else if(newEvent == null) {
            LOG.error("Event not provided! Skipping {} message!", message.getType());
            logMessageSender.sendMessage(String.format("Event not provided! Skipping {} message!",message.getType().toString()));
            throw new InvalidMessageException("Event not provided");
        }
    }

    public boolean eventsEqualCheck(Event e1,Event e2) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        if(e2.getStatus() != null && e2.getStartDate() != null){
            String oldDate = simpleDateFormat.format(e1.getStartDate());
            String newDate = simpleDateFormat.format(e2.getStartDate());
            if(e1.getStatus().equals(e2.getStatus()) && oldDate.equals(newDate)){
                return true;
            }
        }else if(e2.getStatus() != null){
            return e1.getStatus() == e2.getStatus();
        }else if(e2.getStartDate() != null){
            String oldDate = simpleDateFormat.format(e1.getStartDate());
            String newDate = simpleDateFormat.format(e2.getStartDate());
            return oldDate.equals(newDate);
        }
        return false;
    }

    public void handleBetStop(Message message,ReentrantLock lock) throws InvalidMessageException {
        Payload payload = message.getPayload();
        if(payload == null){
            throw new InvalidMessageException("Payload not provided!");
        }
        List<Market> markets = message.getPayload().getMarkets();
        Integer eventId = message.getEventId();
        if(markets == null){
            throw new InvalidMessageException("Markets not provided");
        }

        Event event = eventService.findEventById(eventId);
        if(event == null){
            throw new InvalidMessageException("Event with id " + message.getEventId() + " not exists");
        }
        List<Market> existingMarket = marketRepository.findAllByEventIdEquals(eventId);
        if (markets.isEmpty()) {
            // if there are no markets provided all the markets for the event should be set to status Stopped
            for (Market market : existingMarket) {
                market.getOutcomes().forEach(x -> x.setStatus(OutcomeStatus.Stopped));
                marketRepository.save(market);
            }
        } else {
            // only markets with status stopped
            for (Market market : markets) {
                Market marketToFind = existingMarket.stream()
                        .filter(x -> x.getId().equals(market.getId()))
                        .findFirst().orElse(null);
                if (marketToFind != null) {
                    // checking the timestamp
                    if(market.getMessageTimestamp().before(marketToFind.getMessageTimestamp())){
                        System.out.println("Older timestamp for market " + market.getId() + " Skipping!");
                        continue;
                    }
                    marketToFind.setMessageTimestamp(market.getMessageTimestamp());
                    for (Outcome outcome : market.getOutcomes()) {
                        Outcome outcomeToFind = marketToFind.getOutcomes().stream()
                                .filter(x -> x.getId().equals(outcome.getId()) && OutcomeStatus.Stopped.equals(outcome.getStatus()))
                                .findFirst().orElse(null);
                        if(outcomeToFind != null){
                            if(outcome.getMessageTimestamp().before(outcomeToFind.getMessageTimestamp())){
                                System.out.println("Older timestamp for outcome " + outcome.getId() + " Skipping!");
                                continue;
                            }
                            outcomeToFind.setMessageTimestamp(outcome.getMessageTimestamp());
                            outcomeToFind.setStatus(outcome.getStatus());
                            outcomeRepository.save(outcomeToFind);
                        }
                    }
                    marketRepository.save(marketToFind);
                }else{
                    marketService.save(market,eventId);
                }
            }
        }
    }

    public void handleOddsChange(Message message,ReentrantLock lock) throws InvalidMessageException {
        Event event = eventService.findEventById(message.getEventId());
        if(event != null){
            List<Market> existingMarkets = marketRepository.findAllByEventId(message.getEventId());
            List<Market> newMarkets = message.getPayload().getMarkets();

            if(newMarkets == null || newMarkets.isEmpty()){
                throw new InvalidMessageException("ODDS_CHANGE message with no markets");
            }

            for(Market market : newMarkets){            // list markets from the message
                Market marketToFind = existingMarkets
                        .stream()
                        .filter(x -> x.getId().equals(market.getId()))
                        .findFirst().orElse(null);
                if(marketToFind == null){
                    marketNotExistLog(market, event);
                    marketService.save(market,event.getId());
                }else{
                    if(market.getMessageTimestamp().before(marketToFind.getMessageTimestamp())){
                        System.out.println("Older timestamp for market " + market.getId() + " Skipping!");
                        continue;
                    }
                    marketToFind.setMessageTimestamp(market.getMessageTimestamp());
                    int totalOutcomesUpdated = 0;
                    for(Outcome outcome : market.getOutcomes()){
                        boolean flag = false;
                        Outcome outcomeToFind = marketToFind.getOutcomes().stream()
                                .filter(x -> x.getId().equals(outcome.getId()))
                                .findFirst().orElse(null);

                        // marketToFind and outcomeToFind are from the database
                        // market and outcome are from the message
                        if(outcomeToFind != null){                  // outcome exists for the market
                            if(outcome.getMessageTimestamp().before(outcomeToFind.getMessageTimestamp())){
                                System.out.println("Older timestamp for outcome " + outcome.getId() + " Skipping!");
                                continue;
                            }
                            outcomeToFind.setMessageTimestamp(outcome.getMessageTimestamp());
                            if (outcome.getStatus() != null && !outcome.getStatus().equals(outcomeToFind.getStatus())) {
                                LOG.info("{} {} {} for event {} is updated.Old status: {}, New status: {}",
                                        marketToFind.getName(),marketToFind.getSpecifier(),outcome.getName(),
                                        event.getId(),outcomeToFind.getStatus(),outcome.getStatus());
                                logMessageSender.sendMessage(String.format("%s %s %s for event %s is updated.Old status: %s, New status: %s",
                                        marketToFind.getName(),marketToFind.getSpecifier(),outcome.getName(),
                                        event.getId(),outcomeToFind.getStatus(),outcome.getStatus()));
                                outcomeToFind.setStatus(outcome.getStatus());
                                flag = true;
                            }
                            if (outcome.getOdds() != null && !outcome.getOdds().equals(outcomeToFind.getOdds())) {
                                LOG.info("{} {} {} for event {} is updated.Old odds: {}, New odds: {}",
                                        marketToFind.getName(),marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getOdds(),outcome.getOdds());
                                logMessageSender.sendMessage(String.format("%s %s %s for event %s is updated.Old odds: %s, New odds: %s",
                                        marketToFind.getName(),marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getOdds(),outcome.getOdds()));
                                outcomeToFind.setOdds(outcome.getOdds());
                                flag = true;
                            }
                            if (outcome.getSettlement() != null && !outcome.getSettlement().equals(outcomeToFind.getSettlement())) {
                                outcomeToFind.setSettlement(outcome.getSettlement());
                                LOG.info("{} {} {} for event {} is settled.Settlement status: {}",marketToFind.getName(),
                                        marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getSettlement());
                                logMessageSender.sendMessage(String.format("%s %s %s for event %s is settled.Settlement status: %s",marketToFind.getName(),
                                        marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getSettlement()));
                                flag = true;
                            }
                            if(OutcomeStatus.Settled.equals(outcome.getStatus()) && outcome.getSettlement() == null
                                        && outcomeToFind.getSettlement() == null){
                                outcomeToFind.setSettlement(Settlement.UndecidedYet);
                                LOG.info("{} {} {} for event {} is settled.Settlement status: {}",marketToFind.getName(),
                                        marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getSettlement());
                                logMessageSender.sendMessage(String.format("%s %s %s for event %s is settled.Settlement status: %s",marketToFind.getName(),
                                        marketToFind.getSpecifier() != null ? market.getSpecifier() : "",outcome.getName(),
                                        event.getId(),outcomeToFind.getSettlement()));
                                flag = true;
                            }
                        }else{  // outcome not exists then save it
                            marketService.saveOutcome(outcome,market.getId());
                        }
                        if(flag) {
                            totalOutcomesUpdated++;
                        }
                        outcomeRepository.save(outcomeToFind);
                    }
                    LOG.info("{} {} for event {} is updated.Total outcomes updated: {}", marketToFind.getName(),
                            marketToFind.getSpecifier() != null ? market.getSpecifier() : "", event.getId(), totalOutcomesUpdated);
                    logMessageSender.sendMessage(String.format("%s %s for event %s is updated.Total outcomes updated: %s", marketToFind.getName(),
                            marketToFind.getSpecifier() != null ? market.getSpecifier() : "", event.getId(), totalOutcomesUpdated));

                    marketRepository.save(marketToFind);
                }
            }
        }else{
            LOG.error("Event {} does not exist! Skipping {} message!",message.getEventId(),message.getType());
            logMessageSender.sendMessage(String.format("Event %s does not exist! Skipping %s message!",message.getEventId(),message.getType()));
            throw new InvalidMessageException("Event with id " + message.getEventId() + " not exists");
        }
    }

    public void statusChangeLog(Integer eventId,EventStatus oldStatus,EventStatus newStatus){
        LOG.info("Event {} has been updated! Old status: {} New status: {}",eventId,oldStatus,newStatus);
        logMessageSender.sendMessage(String.format("Event %s has been updated! Old status: %s New status: %s",eventId,oldStatus,newStatus));
    }

    public void dateChangeLog(Integer eventId,Date oldDate, Date newDate){
        LOG.info("Event {} has been updated! Old startDate: {} New startDate: {}",eventId,oldDate,newDate);
        logMessageSender.sendMessage(String.format("Event %s has been updated! Old startDate: %s New startDate: %s",eventId,oldDate,newDate));
    }

    public void marketNotExistLog(Market market, Event event){
        if(market.isVariant()){
            LOG.info("Created new variant market for event {}. Market name: {}", event.getId(),market.getName());
            logMessageSender.sendMessage(String.format("Created new variant market for event %s. Market name: %s", event.getId(),market.getName()));
        }else{
            LOG.info("Created new market for event {}  Market name: {} totalOutcomes: {}",
                    event.getId(),market.getName(),market.getOutcomes().size());
            logMessageSender.sendMessage(String.format("Created new market for event %s  Market name: %s totalOutcomes: %s",
                                                    event.getId(),market.getName(),market.getOutcomes().size()));
        }
    }
}
