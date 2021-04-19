package uk.singular.dfs.provider.sandbox.services.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;
import uk.singular.dfs.provider.sandbox.repository.EventPagingAndSortingRepository;
import uk.singular.dfs.provider.sandbox.services.EventPagingAndSortingService;


@Service
public class EventPagingAndSortingServiceImpl implements EventPagingAndSortingService {

    private final EventPagingAndSortingRepository eventPagingAndSortingRepository;

    public EventPagingAndSortingServiceImpl(EventPagingAndSortingRepository eventPagingAndSortingRepository) {
        this.eventPagingAndSortingRepository = eventPagingAndSortingRepository;
    }

    public Page<Event> findAllByName(String name, Integer pageNumber, Integer numElements){
        Pageable pageable = PageRequest.of(pageNumber,numElements);
        return eventPagingAndSortingRepository.findAllByNameLike(name,pageable);
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventPagingAndSortingRepository.findAll(pageable);
    }

    @Override
    public Page<Event> findAllByParentId(Integer parentId,Pageable pageable){
        return eventPagingAndSortingRepository.findAllByParentId(parentId,pageable);
    }

}
