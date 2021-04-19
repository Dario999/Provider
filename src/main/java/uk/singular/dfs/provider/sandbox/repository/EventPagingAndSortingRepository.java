package uk.singular.dfs.provider.sandbox.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uk.singular.dfs.provider.sandbox.model.metadata.Event;


@Repository
public interface EventPagingAndSortingRepository extends PagingAndSortingRepository<Event,Integer> {

    Page<Event> findAllByNameLike(String name, Pageable pageable);

    Page<Event> findAllByParentId(Integer parentId, Pageable pageable);

}
