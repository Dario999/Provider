package uk.singular.dfs.provider.sandbox.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;


public interface EventPagingAndSortingService {

    Page<Event> findAllByName(String name, Integer pageNumber, Integer numElements);

    Page<Event> findAll(Pageable pageable);

    Page<Event> findAllByParentId(Integer parentId,Pageable pageable);

}
